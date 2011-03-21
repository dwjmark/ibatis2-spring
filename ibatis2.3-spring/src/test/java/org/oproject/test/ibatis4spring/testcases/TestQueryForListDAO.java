/**
 * 
 */
package org.oproject.test.ibatis4spring.testcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oproject.test.ibatis4spring.dao.QueryForListDAO;
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
public class TestQueryForListDAO{

	final static User user = new User();
	
	static {
		user.setId("1");
	}
	
	@Autowired
	private Mockery mockery;
	
	@Autowired
	private QueryForListDAO queryForListDAO;
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	final List<User> queryResult = new ArrayList<User>();
	
	@After
	public void after(){
		// 验证sqlMapClient方法调用正确
		mockery.assertIsSatisfied();
	}
	
	@Before
	public void before(){
		queryResult.clear();
		queryResult.add(user);
	}
	
	@Test
	public void queryForList(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForList("QueryForList.queryUserForList", null);
            will(returnValue(queryResult));
        }});
		
		List<User> ret = queryForListDAO.queryUserForList(null);
		// 验证返回结果和预期一致
		Assert.assertEquals(queryResult, ret);
	}

	@Test
	public void queryUser2ForList(){
		final Map<String, Object> params = null;
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForList("QueryForList.queryUser2ForList", params, 1, 2);
            will(returnValue(queryResult));
        }});
		
		List<User> ret = queryForListDAO.queryUser2ForList(null, 1, 2);
		// 验证返回结果和预期一致
		Assert.assertEquals(queryResult, ret);
	}
}
