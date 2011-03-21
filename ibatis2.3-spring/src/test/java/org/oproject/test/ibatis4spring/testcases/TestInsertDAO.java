/**
 * 
 */
package org.oproject.test.ibatis4spring.testcases;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oproject.test.ibatis4spring.dao.InsertDAO;
import org.oproject.test.ibatis4spring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * insert插入操作测试用例
 * </p>
 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner
 * @see org.springframework.test.context.ContextConfiguration
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-18
 * @since v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class TestInsertDAO {
	
	final static User user = new User();
	
	static {
		user.setId("1");
		user.setName("name");
	}
	
	@Autowired
	private Mockery mockery;
	
	@Autowired
	private InsertDAO insertDAO;
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	/**
	 * 在after中对mockery的方法调用请求进行验证
	 */
	@After
	public void after(){
		// 验证sqlMapClient方法调用正确
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void insert(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).insert("Insert.insert", user);
            will(returnValue(1));
        }});
		insertDAO.insert(user);
	}
	
	@Test
	public void insertUser(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).insert("Insert.insertUser", user);
            will(returnValue(1));
        }});
		insertDAO.insertUser(user);
	}
	
	@Test
	public void save(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).insert("Insert.save", user);
            will(returnValue(1));
        }});
		insertDAO.save(user);
	}
	
	@Test
	public void saveUser(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).insert("Insert.saveUser", user);
            will(returnValue(1));
        }});
		insertDAO.saveUser(user);
	}
}
