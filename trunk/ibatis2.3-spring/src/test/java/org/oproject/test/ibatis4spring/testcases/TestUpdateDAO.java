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
import org.oproject.test.ibatis4spring.dao.UpdateDAO;
import org.oproject.test.ibatis4spring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * insert更新操作测试用例
 * </p>
 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner
 * @see org.springframework.test.context.ContextConfiguration
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-18
 * @since v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class TestUpdateDAO {
	
	final static User user = new User();
	
	static {
		user.setId("1");
		user.setName("name");
	}
	
	@Autowired
	private Mockery mockery;
	
	@Autowired
	private UpdateDAO updateDAO;
	
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
	public void update(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).update("Update.update", user);
        }});
		updateDAO.update(user);
	}
	
	@Test
	public void updateUser(){
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).update("Update.updateUser", user);
            will(returnValue(2));
        }});
		int rows = updateDAO.updateUser(user);
		Assert.assertEquals(2, rows);
	}
}
