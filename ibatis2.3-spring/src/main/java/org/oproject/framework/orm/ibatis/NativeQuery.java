package org.oproject.framework.orm.ibatis;

/**
 * <p>
 * 本地查询接口
 * 定义本地查询方法
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public interface NativeQuery
{
	/**
	 * 包装本地SQL查询语句
	 * @param sql            原始sql语句
	 * @param skipResults    查询的行数
	 * @param maxResults     最大获取数
	 * @return    包装后的SQL语句字符串
	 */
	String warpSQLStatement(String sql, int skipResults, int maxResults);
}
