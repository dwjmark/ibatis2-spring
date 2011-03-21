/**
 * 
 */
package org.oproject.test.ibatis4spring.dao;

import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;
import org.oproject.test.ibatis4spring.domain.User;
import org.springframework.stereotype.Component;

/**
 * <p>
 * delete测试接口
 * </p>
 * @see org.springframework.stereotype.Component
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-18
 * @since v1.0
 */
@Component("deleteDAO")
@DynamicIbatisDAO(sqlMapClientTemplate = "sqlMapClientTemplate")
public interface DeleteDAO {

	void delete(User user);
	
	int deleteUser(User user);
	
	int removeUser(User user);
}
