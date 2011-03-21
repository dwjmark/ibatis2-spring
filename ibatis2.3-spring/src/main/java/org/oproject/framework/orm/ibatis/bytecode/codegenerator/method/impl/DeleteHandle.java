/*
 * description: 
 * date:        上午12:31:52
 * author:      ahli
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.oproject.framework.orm.ibatis.bytecode.BytecodeUtils;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;

/**
 * <p>
 * Ibatis删除操作处理
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class DeleteHandle implements Opcodes, MethodGenerator{

	private static final String SQLMAPCLIENT_DELETE = "delete";
	
	@SuppressWarnings("unchecked")
	@Override
	public void generate(ClassNode classNode, Method method, String sqlClientProperty, Class<?> sqlClientClass) {
		// 方法名称
		final String methodName = method.getName();
		// 返回值类型
		final Class<?> returnClass = method.getReturnType();
		
		// 构建一个方法节点
		MethodNode mn = new MethodNode(ACC_PUBLIC, methodName, BytecodeUtils.getMethodDesc(method.getParameterTypes(), method.getReturnType()), null, null);
		
		InsnList il = mn.instructions;
		il.add(new VarInsnNode(ALOAD, 0));
		il.add(new FieldInsnNode(GETFIELD, classNode.name, sqlClientProperty, BytecodeUtils.getClassDesc(sqlClientClass)));
		il.add(new LdcInsnNode(BytecodeUtils.getIbatisStatementName(classNode.name, methodName)));
		il.add(new VarInsnNode(ALOAD, 1));
		il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(sqlClientClass), SQLMAPCLIENT_DELETE, "(Ljava/lang/String;Ljava/lang/Object;)I"));
		if(null != returnClass && "int".equals(returnClass.getName())){
			il.add(new InsnNode(IRETURN));
		}else{
			il.add(new InsnNode(RETURN));
		}
		mn.maxStack = 0;
		mn.maxLocals = 0;
		classNode.methods.add(mn);
	}

	@Override
	public boolean validate(Method method) {
		if(!Pattern.matches("(delete|remove|del)[\\S]*", method.getName())){
			return false;
		}
		if(method.getParameterTypes() == null || method.getParameterTypes().length != 1){
			return false;
		}
		return true;
	}
}
