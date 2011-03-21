/**
 * 
 */
package org.oproject.framework.orm.ibatis;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.oproject.framework.orm.PageResult;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * <p>
 * 基于Spring的SqlMapClientTemplate对象批量处理工具类，在这里将批量处理、分页查询
 * 等操作封装为简单的api的目的是，那该死的asm写这个代码完全是在自残，求自虐。
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class BatchExecuteUtils {

	/**
	 * 单态
	 */
	private final static BatchExecuteUtils instance = new BatchExecuteUtils();
	
	/**
	 * 私有构造函数
	 */
	private BatchExecuteUtils(){}

	/**
	 * 获取QueryUtils实例
	 * @return    QueryUtils实例
	 */
	public static BatchExecuteUtils getInstance(){
		return instance;
	}
	
	/**
	 * 批量保存
	 * @param sqlMapClientTemplate
	 * @param stmt
	 * @param col
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void batchInsert(SqlMapClientTemplate sqlMapClientTemplate, final  String stmt, final Collection<? extends Object> col){
		sqlMapClientTemplate.execute(new SqlMapClientCallback()
		{
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException
			{
				executor.startBatch();
				for(Object obj : col){
					executor.insert(stmt, obj);
				}
				return executor.executeBatch();
			}
		});
	}
	
	/**
	 * 批量更新操作
	 * @param sqlMapClientTemplate    spring的sqlMapClientTemplate对象
	 * @param stmt                    SQL语句别名
	 * @param col                     批量更新参数集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void batchUpdate(SqlMapClientTemplate sqlMapClientTemplate, final String stmt, final Collection<? extends Object> col){
		sqlMapClientTemplate.execute(new SqlMapClientCallback()
		{
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException
			{
				executor.startBatch();
				for(Object obj : col){
					executor.update(stmt, obj);
				}
				return executor.executeBatch();
			}
		});
	}
	
	/**
	 * 批量删除操作
	 * @param sqlMapClientTemplate    spring的sqlMapClientTemplate对象
	 * @param stmt                    SQL语句别名
	 * @param col                     批量删除参数集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void batchDelete(SqlMapClientTemplate sqlMapClientTemplate, final String stmt, final Collection<? extends Object> col){
		sqlMapClientTemplate.execute(new SqlMapClientCallback()
		{
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException
			{
				executor.startBatch();
				for(Object obj : col){
					executor.delete(stmt, obj);
				}
				return executor.executeBatch();
			}
		});
	}
	
	@SuppressWarnings({"rawtypes" })
	public List queryForList(SqlMapClientTemplate sqlMapClientTemplate, String statementName){
		return sqlMapClientTemplate.queryForList(statementName);
	}
	
	@SuppressWarnings({"rawtypes" })
	public List queryForList(SqlMapClientTemplate sqlMapClientTemplate, String statementName, Object params){
		return sqlMapClientTemplate.queryForList(statementName, params);
	}
	
	@SuppressWarnings({"rawtypes" })
	public List queryForList(SqlMapClientTemplate sqlMapClientTemplate, String statementName, int skipResults, int maxResults){
		try {
			CustomizedSQLExecutor
					.setCurrentPaginType(CustomizedSQLExecutor.DBType.Native);
			return sqlMapClientTemplate.queryForList(statementName,
					skipResults, maxResults);
		} finally {
			CustomizedSQLExecutor
					.setCurrentPaginType(CustomizedSQLExecutor.DBType.Default);
		}
	}
	
	@SuppressWarnings({"rawtypes" })
	public List queryForList(SqlMapClientTemplate sqlMapClientTemplate, String statementName, Object params, int skipResults, int maxResults){
		try {
			CustomizedSQLExecutor
					.setCurrentPaginType(CustomizedSQLExecutor.DBType.Native);
			return sqlMapClientTemplate.queryForList(statementName, params,
					skipResults, maxResults);
		} finally {
			CustomizedSQLExecutor
					.setCurrentPaginType(CustomizedSQLExecutor.DBType.Default);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageResult queryForPageResult(SqlMapClientTemplate sqlMapClientTemplate, 
			String count, String query, 
			int pageNum, int pageSize){
		// 申明一个空返回集合对象，NULL Object模式，避免返回Null造成麻烦
		PageResult pr = PageResult.EMPTY_PAGE;
		// 通过一定个查询，获取总记录数
		final int totalRowNum = (Integer) sqlMapClientTemplate.queryForObject(count);

		// 如果查询集合大于0，表示有查询结果
		if (totalRowNum > 0)
		{
			try
			{
				CustomizedSQLExecutor
						.setCurrentPaginType(CustomizedSQLExecutor.DBType.Native);
				// 构造分页对象
				pr = new PageResult(totalRowNum, pageSize, pageNum);
				List list = sqlMapClientTemplate.queryForList(query, pr.getStartRow(), pageSize);
				pr.setResultList(list);
			}
			finally
			{
				CustomizedSQLExecutor
						.setCurrentPaginType(CustomizedSQLExecutor.DBType.Default);
			}
		}
		return pr;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageResult queryForPageResult(SqlMapClientTemplate sqlMapClientTemplate, 
			String count, String query, 
			Object obj, int pageNum, int pageSize){
		// 申明一个空返回集合对象，NULL Object模式，避免返回Null造成麻烦
		PageResult pr = PageResult.EMPTY_PAGE;
		// 通过一定个查询，获取总记录数
		final int totalRowNum = obj == null ? (Integer) sqlMapClientTemplate
				.queryForObject(count) : (Integer) sqlMapClientTemplate
				.queryForObject(count, obj);

		// 如果查询集合大于0，表示有查询结果
		if (totalRowNum > 0)
		{
			try
			{
				CustomizedSQLExecutor
						.setCurrentPaginType(CustomizedSQLExecutor.DBType.Native);
				// 构造分页对象
				pr = new PageResult(totalRowNum, pageSize, pageNum);
				List list = obj == null ? sqlMapClientTemplate.queryForList(
						query, pr.getStartRow(), pageSize)
						: sqlMapClientTemplate.queryForList(query, obj, pr
								.getStartRow(), pageSize);
				pr.setResultList(list);
			}
			finally
			{
				CustomizedSQLExecutor
						.setCurrentPaginType(CustomizedSQLExecutor.DBType.Default);
			}
		}
		return pr;
	}
}
