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
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.oproject.framework.orm.ibatis.bytecode.BytecodeUtils;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;

/**
 * <p>
 * Ibatis插入操作处理
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class InsertHandler implements Opcodes, MethodGenerator{

	/**
	 * sqlMapClientTemplate方法：insert
	 */
	private static final String SQLMAPCLIENT_METHOD_INSERT = "insert";
	
	/* (non-Javadoc)
	 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator#generate(org.objectweb.asm.tree.ClassNode, java.lang.reflect.Method, java.lang.String, java.lang.Class)
	 */
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
		il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(sqlClientClass), SQLMAPCLIENT_METHOD_INSERT, "(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"));
		if(null != returnClass && !"void".equals(returnClass.getName())){
			il.add(new TypeInsnNode(CHECKCAST, returnClass.getName().replaceAll("\\.", "/")));
			il.add(new InsnNode(ARETURN));
		}else{
			il.add(new InsnNode(RETURN));
		}
		mn.maxStack = 0;
		mn.maxLocals = 0;
		classNode.methods.add(mn);
	}

	/**
	 * 验证表达式"(save|insert|add)[\\S]*"
	 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator#validate(java.lang.reflect.Method)
	 */
	@Override
	public boolean validate(Method method) {
		if(!Pattern.matches("(save|insert|add)[\\S]*", method.getName())){
			return false;
		}
		if(method.getParameterTypes() == null || method.getParameterTypes().length != 1){
			return false;
		}
		return true;
	}
}
