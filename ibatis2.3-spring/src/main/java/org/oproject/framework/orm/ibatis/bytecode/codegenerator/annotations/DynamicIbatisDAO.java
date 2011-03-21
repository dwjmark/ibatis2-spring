/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations;

/**
 * <p>
 * 动态Ibatis数据访问接口注解
 * </p>
 * @see java.lang.annotation.RetentionPolicy.RUNTIME
 * @see java.lang.annotation.ElementType.TYPE
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE})
public @interface DynamicIbatisDAO {

	/**
	 * DAO在IOC容器中的实例
	 * @return
	 */
	String value() default "";
	
	/**
	 * 注入的SqlMapClientTemplate在IOC容器中的Bean.ID
	 * @return
	 */
	String sqlMapClientTemplate() default "sqlMapClientTemplate";
}
