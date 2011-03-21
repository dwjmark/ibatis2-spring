/**
 * 
 */
package org.oproject.test.ibatis4spring.testcases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oproject.framework.orm.PageResult;
import org.oproject.test.ibatis4spring.dao.QueryForPageResultDAO;
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
 * 
 * @see
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@Configuration
public class TestQueryForPageResultDAO {

	final static User user = new User();

	static {
		user.setId("1");
	}

	@Autowired
	private Mockery mockery;

	@Autowired
	private QueryForPageResultDAO queryForPageResultDAO;

	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;

	final PageResult<User> queryPageResult = new PageResult<User>(20, 15, 1);
	final List<User> queryResult = new ArrayList<User>();

	@After
	public void after() {
		// 验证sqlMapClient方法调用正确
		mockery.assertIsSatisfied();
	}

	@Before
	public void before() {
		queryResult.clear();
		queryResult.add(user);
		queryPageResult.setResultList(queryResult);
	}

	@Test
	public void queryUser2ForPageResult() {
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {
			{
				// 模拟第一次查询，获取记录总数
				oneOf(sqlMapClientTemplate).queryForObject(
						"QueryForPageResult.queryUserForPageResultCount");
				will(returnValue(20));
				
				// 模拟第二次查询，获取记录
				oneOf(sqlMapClientTemplate).queryForList(
						"QueryForPageResult.queryUserForPageResult", 0, 15);
				will(returnValue(queryResult));
			}
		});

		PageResult<User> ret = queryForPageResultDAO.queryUserForPageResult(
				null, 1, 15);
		// 验证返回结果和预期一致
		Assert.assertEquals(queryPageResult.getResultList(), ret.getResultList());
	}

}
