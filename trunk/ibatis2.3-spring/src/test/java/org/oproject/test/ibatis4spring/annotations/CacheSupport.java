/**
 * 
 */
package org.oproject.test.ibatis4spring.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * 自动化测试用，缓存注解
 * </p>
 * @see java.lang.annotation.RetentionPolicy.RUNTIME
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-18
 * @since v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheSupport {

	/**
	 * 缓存的Key
	 * @return
	 */
	String value();
	
	/**
	 * 超时时间，默认600000
	 * @return
	 */
	int timeout() default 600000;
	
	/**
	 * 枚举
	 * @return    返回缓存类型
	 */
	CacheType type() default CacheType.Simple;
	
	/**
	 * 缓存关键字
	 * @return
	 */
	String[] keys() default "";
}
