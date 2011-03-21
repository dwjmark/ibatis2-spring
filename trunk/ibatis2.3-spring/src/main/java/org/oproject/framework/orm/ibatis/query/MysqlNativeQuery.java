package org.oproject.framework.orm.ibatis.query;

import org.oproject.framework.orm.ibatis.NativeQuery;

/**
 * <p>
 * 包装Mysql本地分页查询用到的带有行数取值的sql语句
 * </p>
 * @see org.oproject.framework.orm.ibatis.NativeQuery
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class MysqlNativeQuery implements NativeQuery
{
	/* (non-Javadoc)
	 * @see com.huawei.cms.common.dao.sqlExecutor.NativeQuery#warpSQLStatement(java.lang.String, int, int)
	 */
	@Override
	public String warpSQLStatement(String sql, int skipResults, int maxResults)
	{
		StringBuffer sb = new StringBuffer(200);
		sb.append(sql);
		sb.append(" limit ");
		sb.append(skipResults);
		sb.append(", ");
		sb.append(maxResults);
		return sb.toString();
	}
}
