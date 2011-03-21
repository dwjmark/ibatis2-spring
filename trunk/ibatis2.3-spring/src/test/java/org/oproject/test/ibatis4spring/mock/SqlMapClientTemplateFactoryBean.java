/**
 * 
 */
package org.oproject.test.ibatis4spring.mock;

import org.jmock.Mockery;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * <p>
 * 
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class SqlMapClientTemplateFactoryBean implements FactoryBean<SqlMapClientTemplate> {

	private Mockery mockery = null;
	
	public void setMockery(Mockery mockery) {
		this.mockery = mockery;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public SqlMapClientTemplate getObject() throws Exception {
		return mockery.mock(SqlMapClientTemplate.class);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<SqlMapClientTemplate> getObjectType() {
		return SqlMapClientTemplate.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
}
