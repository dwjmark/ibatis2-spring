package org.oproject.framework.orm.ibatis;

import java.sql.Connection;
import java.sql.SQLException;

import org.oproject.framework.utils.PropertyUtil;

import com.ibatis.sqlmap.engine.execution.DefaultSqlExecutor;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * <p>
 * CMS4期的SQL执行器，在SPRING容器启动的时候，加载init方法，将自己关联到ibatis的sqlExecutor对象；
 * 通过自身覆盖父类的query方法，增加拼装nativeSQL查询分页的语句。对开发人员保持ibatis原有的queryForList
 * api不变；在调用的时候，仅仅需要调用一个静态赋值就可以了。
 * <p>
 * 2011-2-23,从ibatis2.3.5版本开始，SqlExecutor从类调整为抽象接口，所以该类继承关系调整为
 * DefaultSqlExecutor
 * @see com.ibatis.sqlmap.engine.execution.DefaultSqlExecutor
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class CustomizedSQLExecutor extends DefaultSqlExecutor
{
	/**
	 * 数据库类型
	 * @author ahli
	 * @version IMPV100R001DA0, Jun 26, 2009 
	 * @since CMS IMPV100R001DA0
	 */
	public static enum DBType{
		Native, Default
	}
	
	/**
	 * 本地查询策略
	 */
	private NativeQuery nativeQuery;
	
	/**
	 * 
	 */
	private SqlMapClientImpl sqlMapClient;
	
	/**
	 * 线程变量
	 */
	private static final ThreadLocal<DBType> threadLocal = new ThreadLocal<DBType>();
	
	/**
	 * 设置DB类型
	 * @param type    DB类型
	 */
	public static void setCurrentPaginType(DBType type){
		threadLocal.set(type);
	}
	
	/**
	 * 设置本地查询策略对象
	 * @param nativeQuery
	 */
	public void setNativeQuery(NativeQuery nativeQuery)
	{
		this.nativeQuery = nativeQuery;
	}

	/**
	 * 设置sqlMapClientImpl
	 * @param sqlMapClientImpl the sqlMapClientImpl to set
	 */
	public void setSqlMapClient(SqlMapClientImpl sqlMapClient)
	{
		this.sqlMapClient = sqlMapClient;
	}

	/**
	 * 初始化，替换sqlMapExecutorDelegate的delegate对象的sqlExecutor属性。
	 */
	public void init(){
	
		// 从容器中获取ibatis的对象实例
		SqlMapExecutorDelegate sqlMapExecutorDelegate = sqlMapClient.delegate;
		
		// 通过反射，重新关联属性
		PropertyUtil.setFieldValue(sqlMapExecutorDelegate, "sqlExecutor", CustomizedSQLExecutor.class, this);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.engine.execution.SqlExecutor#executeQuery(com.ibatis.sqlmap.engine.scope.StatementScope, java.sql.Connection, java.lang.String, java.lang.Object[], int, int, com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback)
	 */
	@Override
	public void executeQuery(StatementScope statementScope, Connection conn,
			String sql, Object[] parameters, int skipResults, int maxResults,
			RowHandlerCallback callback) throws SQLException
	{
		// 判断当前线程的查询类型，如果是本地类型
		if (DBType.Native.equals(threadLocal.get()))
		{
			// 拼装SQL语句
			sql = nativeQuery.warpSQLStatement(sql, skipResults, maxResults);

			// 重置查询的分页条件为默认值
			skipResults = SqlExecutor.NO_SKIPPED_RESULTS;
			maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;
		}

		// 调用超类的查询
		super.executeQuery(statementScope, conn, sql, parameters, skipResults,
				maxResults, callback);
	}
}
