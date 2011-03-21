/**
 * 
 */
package org.oproject.test.ibatis4spring.dao;

import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;
import org.oproject.test.ibatis4spring.annotations.CacheSupport;
import org.oproject.test.ibatis4spring.domain.User;

/**
 * <p>
 * 
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@DynamicIbatisDAO("annotationDAO")
public interface AnnotationDAO {

	@CacheSupport(value = "AnnotationDAO.getUser.{0}")
	User getUser(String id);
}
