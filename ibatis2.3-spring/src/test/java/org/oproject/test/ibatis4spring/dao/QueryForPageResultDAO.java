/**
 * 
 */
package org.oproject.test.ibatis4spring.dao;

import java.util.Map;

import org.oproject.framework.orm.PageResult;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;
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
@DynamicIbatisDAO("queryForPageResultDAO")
public interface QueryForPageResultDAO {

	PageResult<User> queryUserForPageResult(Map<String, Object> params, int pageNum, int pageSize);
}
