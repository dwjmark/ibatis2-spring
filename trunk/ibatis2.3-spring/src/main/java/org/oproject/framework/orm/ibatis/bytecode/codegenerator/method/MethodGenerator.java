/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.method;

import java.lang.reflect.Method;

import org.objectweb.asm.tree.ClassNode;

/**
 * <p>
 * 方法生成器接口,类生成器首先调用validate方法，验证当前方法生成器是否适用方法，如果不适用则返回false。如果适用返回true。
 * 类生成器会重新调用generate方法生成对应方法代码
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public interface MethodGenerator {

	/**
	 * 验证该方法是否适用当前方法生成器
	 * @param method    方法对象
	 * @return
	 */
	boolean validate(Method method);
	
	/**
	 * 生成代码，并且添加到classNode中
	 * @param classNode            动态类节点
	 * @param metohd               接口定义的方法对象
	 * @param sqlClientProperty    sqlMapClient对象属性名称
	 * @param sqlClientClass       sqlMapClient具体实现类类信息
	 */
	void generate(ClassNode classNode, Method metohd, String sqlClientProperty, Class<?> sqlClientClass);
}
