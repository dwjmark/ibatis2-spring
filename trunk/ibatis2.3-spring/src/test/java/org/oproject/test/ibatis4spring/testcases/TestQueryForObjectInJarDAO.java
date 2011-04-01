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
import org.oproject.test.ibatis4spring.dao.JarDAO;
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
public class TestQueryForObjectInJarDAO{

	final static Object obj = new Object();
	
	@Autowired
	private Mockery mockery;
	
	@Autowired
	private JarDAO jarDAO;
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@After
	public void after(){
		// 验证sqlMapClient方法调用正确
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void select(){
		// 验证sqlMapClientTemplate
		mockery.checking(new Expectations() {{
            oneOf (sqlMapClientTemplate).queryForObject("Jar.select", "1");
            will(returnValue(obj));
        }});
		
		Object ret = jarDAO.select("1");
		// 验证返回结果和预期一致
		Assert.assertEquals(obj, ret);
	}
}
