/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.property;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.oproject.framework.orm.ibatis.bytecode.BytecodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <p>
 * Bean属性、对应setter方法创建器
 * </p>
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.property.PropertyGenerator
 * @see org.objectweb.asm.Opcodes
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class BeanPropertyAndSetterMethodGenerator implements PropertyGenerator, Opcodes {

	/* (non-Javadoc)
	 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.property.PropertyGenerator#generate(org.objectweb.asm.tree.ClassNode, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void generate(ClassNode classNode, Class<?> propertyClass, String propertyName, String sqlMapClientTemplateBeanID) {
		// 获取接口的类型描述
		final String propertyClassDesc = BytecodeUtils.getClassDesc(propertyClass);
		// 属性名称
		final String propertyShortName = (propertyName == null ? propertyClass.getSimpleName() : propertyName);
		// setter方法名称
		final String propertySetterMethodName = "set" + propertyShortName.substring(0, 1).toUpperCase() + propertyShortName.substring(1);
		// 创建私有变量
		FieldNode fieldNode = new FieldNode(ACC_PUBLIC, propertyShortName, propertyClassDesc, null, null);
		
		if(null != sqlMapClientTemplateBeanID){
			org.objectweb.asm.AnnotationVisitor av_Autowired = fieldNode.visitAnnotation(BytecodeUtils.getClassDesc(Autowired.class), true);
			av_Autowired.visitEnd();
			
			org.objectweb.asm.AnnotationVisitor av_Qualifier = fieldNode.visitAnnotation(BytecodeUtils.getClassDesc(Qualifier.class), true);
			av_Qualifier.visit("value", sqlMapClientTemplateBeanID);
			av_Qualifier.visitEnd();
		}
		
		classNode.fields.add(fieldNode);
		
		// 创建setter方法
		final MethodNode setterMethod = new MethodNode(ACC_PUBLIC, propertySetterMethodName, BytecodeUtils.getMethodDesc(new Class<?>[]{propertyClass}, null), null, null);
		InsnList il = setterMethod.instructions;
		il.add(new VarInsnNode(ALOAD, 0));
		il.add(new VarInsnNode(org.objectweb.asm.Type.getType(propertyClass).getOpcode(ILOAD), 1));
		il.add(new FieldInsnNode(PUTFIELD, classNode.name, propertyShortName, propertyClassDesc));
		il.add(new InsnNode(RETURN));
		setterMethod.maxLocals = 0;
		setterMethod.maxStack = 0;
		classNode.methods.add(setterMethod);
	}
}
