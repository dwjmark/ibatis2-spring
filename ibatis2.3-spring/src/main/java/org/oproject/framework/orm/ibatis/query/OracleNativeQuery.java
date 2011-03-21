package org.oproject.framework.orm.ibatis.query;

import org.oproject.framework.orm.ibatis.NativeQuery;

/**
 * <p>
 * 包装Oracle本地分页查询用到的带有行数取值的sql语句
 * </p>
 * @see org.oproject.framework.orm.ibatis.NativeQuery
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class OracleNativeQuery implements NativeQuery
{
	/* (non-Javadoc)
	 * @see com.huawei.cms.common.dao.sqlExecutor.NativeQuery#warpSQLStatement(java.lang.String, int, int)
	 */
	@Override
	public String warpSQLStatement(String sql, int skipResults, int maxResults)
	{
		StringBuffer sb = new StringBuffer(200);
		sb.append("select * from ( select row_limit.*, rownum rownum_ from (");
		sb.append(sql);
		sb.append(") row_limit where rownum <=");
		sb.append(skipResults + maxResults);
		sb.append(" ) where rownum_ >");
		sb.append(skipResults);
		return sb.toString();
	}
}
