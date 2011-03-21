/**
 * 
 */
package org.oproject.test.ibatis4spring.mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;

/**
 * <p>
 * 
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@SuppressWarnings(value = {"deprecation", "rawtypes"})
public class TestMockSqlMapClient implements SqlMapClient {

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapClient#flushDataCache()
	 */
	@Override
	public void flushDataCache() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapClient#flushDataCache(java.lang.String)
	 */
	@Override
	public void flushDataCache(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapClient#getSession()
	 */
	@Override
	public SqlMapSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapClient#openSession()
	 */
	@Override
	public SqlMapSession openSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapClient#openSession(java.sql.Connection)
	 */
	@Override
	public SqlMapSession openSession(Connection arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(java.lang.String)
	 */
	@Override
	public int delete(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public int delete(String arg0, Object arg1) throws SQLException {
		System.out.println("[delete]->" + arg0 + ", " + arg1);
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#executeBatch()
	 */
	@Override
	public int executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#executeBatchDetailed()
	 */
	@Override
	public List executeBatchDetailed() throws SQLException, BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(java.lang.String)
	 */
	@Override
	public Object insert(String arg0) throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object insert(String arg0, Object arg1) throws SQLException {
		System.out.println("[insert]->" + arg0 + ", " + arg1);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String)
	 */
	@Override
	public List queryForList(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, java.lang.Object)
	 */
	@Override
	public List queryForList(String arg0, Object arg1) throws SQLException {
		System.out.println("[queryForList]->" + arg0 + ", " + arg1);
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, int, int)
	 */
	@Override
	public List queryForList(String arg0, int arg1, int arg2)
			throws SQLException {
		System.out.println("[queryForList]->" + arg0 + ", " + arg1);
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, java.lang.Object, int, int)
	 */
	@Override
	public List queryForList(String arg0, Object arg1, int arg2, int arg3)
			throws SQLException {
		System.out.println("[queryForList]->" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3);
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public Map queryForMap(String arg0, Object arg1, String arg2)
			throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(java.lang.String, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public Map queryForMap(String arg0, Object arg1, String arg2, String arg3)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String)
	 */
	@Override
	public Object queryForObject(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object queryForObject(String arg0, Object arg1) throws SQLException {
		System.out.println("[queryForObject]->" + arg0 + ", " + arg1);
		return new Object();
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object queryForObject(String arg0, Object arg1, Object arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForPaginatedList(java.lang.String, int)
	 */
	@Override
	public PaginatedList queryForPaginatedList(String arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForPaginatedList(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public PaginatedList queryForPaginatedList(String arg0, Object arg1,
			int arg2) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(java.lang.String, com.ibatis.sqlmap.client.event.RowHandler)
	 */
	@Override
	public void queryWithRowHandler(String arg0, RowHandler arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(java.lang.String, java.lang.Object, com.ibatis.sqlmap.client.event.RowHandler)
	 */
	@Override
	public void queryWithRowHandler(String arg0, Object arg1, RowHandler arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#startBatch()
	 */
	@Override
	public void startBatch() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(java.lang.String)
	 */
	@Override
	public int update(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public int update(String arg0, Object arg1) throws SQLException {
		System.out.println("[update]->" + arg0 + ", " + arg1);
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#commitTransaction()
	 */
	@Override
	public void commitTransaction() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#endTransaction()
	 */
	@Override
	public void endTransaction() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#getCurrentConnection()
	 */
	@Override
	public Connection getCurrentConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#getUserConnection()
	 */
	@Override
	public Connection getUserConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#setUserConnection(java.sql.Connection)
	 */
	@Override
	public void setUserConnection(Connection arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#startTransaction()
	 */
	@Override
	public void startTransaction() throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#startTransaction(int)
	 */
	@Override
	public void startTransaction(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}
}
