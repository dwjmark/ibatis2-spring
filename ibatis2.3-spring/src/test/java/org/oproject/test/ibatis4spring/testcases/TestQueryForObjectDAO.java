/**
 * 
 */
package org.oproject.test.ibatis4spring.testcases;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oproject.test.ibatis4spring.dao.QueryForObjectDAO;
import org.oproject.test.ibatis4spring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * 
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
@Configuration
public class TestQueryForObjectDAO{

	final static User user = new User();
	
	static {
		user.setId("1");
	}
	
	@Autowired
	private Mockery mockery;
	
	@Autowired
	private QueryForObjectDAO queryForObjectDAO;
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@After
	public void after(){
		// 验证sqlMapClient方法调用正确
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void getInt(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("QueryForObject.getInt", "1");
            will(returnValue(1));
        }});
		
		int ret = queryForObjectDAO.getInt("1");
		// 验证返回结果和预期一致
		Assert.assertEquals(1, ret);
	}

	@Test
	public void getString(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("QueryForObject.getString", "1");
            will(returnValue("1234567"));
        }});
		
		String ret = queryForObjectDAO.getString("1");
		// 验证返回结果和预期一致
		Assert.assertEquals("1234567", ret);
	}
	
	@Test
	public void getUser(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("QueryForObject.getUser", "1");
            will(returnValue(user));
        }});
		
		User user1 = queryForObjectDAO.getUser("1");
		// 验证返回结果和预期一致
		Assert.assertEquals(user, user1);
	}
	
	@Test
	public void loadUser(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("QueryForObject.loadUser", 1);
            will(returnValue(user));
        }});
		
		User user1 = queryForObjectDAO.loadUser(1);
		// 验证返回结果和预期一致
		Assert.assertEquals(user, user1);
	}
}
