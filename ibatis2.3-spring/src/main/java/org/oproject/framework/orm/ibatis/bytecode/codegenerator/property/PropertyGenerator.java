/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.property;

import org.objectweb.asm.tree.ClassNode;

/**
 * <p>
 * 属性构建起
 * </p>
 * @see 
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public interface PropertyGenerator {

	/**
	 * @param cn
	 * @param propertyClass
	 * @param propertyName
	 * @param sqlMapClientTemplateBeanID
	 */
	void generate(ClassNode cn, Class<?> propertyClass, String propertyName, String sqlMapClientTemplateBeanID);
}
