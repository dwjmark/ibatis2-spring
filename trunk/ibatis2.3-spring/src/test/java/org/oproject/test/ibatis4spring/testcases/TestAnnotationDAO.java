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
import org.oproject.test.ibatis4spring.dao.AnnotationDAO;
import org.oproject.test.ibatis4spring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * spring中给予aspectj做aop的测试
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class TestAnnotationDAO {

	@Autowired
	private Mockery mockery;
	
	@Autowired
	AnnotationDAO annotationDAO;
	
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
	public void getUser(){
		
		final User user = new User();
		user.setId("1");
		
		// 验证sqlMapClientTemplate,该查询方法只会被调用一次
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("Annotation.getUser", "1");
            will(returnValue(user));
        }});
		
		User user1 = annotationDAO.getUser("1");
		User user2 = annotationDAO.getUser("1");
		
		// 验证返回结果和预期一致
		Assert.assertEquals(user, user1);
		Assert.assertEquals(user, user2);
	}
}
