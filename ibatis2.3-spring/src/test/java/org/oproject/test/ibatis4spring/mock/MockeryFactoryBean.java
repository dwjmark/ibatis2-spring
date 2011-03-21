/**
 * 
 */
package org.oproject.test.ibatis4spring.mock;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * 
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class MockeryFactoryBean implements FactoryBean<Mockery>{

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public Mockery getObject() throws Exception {
		return new JUnit4Mockery() {{
	        setImposteriser(ClassImposteriser.INSTANCE);
	    }};
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<Mockery> getObjectType() {
		// TODO Auto-generated method stub
		return Mockery.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
}
