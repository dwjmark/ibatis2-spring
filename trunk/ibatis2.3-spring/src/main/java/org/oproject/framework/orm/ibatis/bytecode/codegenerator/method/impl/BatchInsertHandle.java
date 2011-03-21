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
import org.oproject.framework.orm.ibatis.BatchExecuteUtils;
import org.oproject.framework.orm.ibatis.bytecode.BytecodeUtils;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;

/**
 * <p>
 * Ibatis批量插入操作处理
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class BatchInsertHandle implements Opcodes, MethodGenerator{

	/**
	 * sqlMapClientTemplate方法：insert
	 */
	private static final String SQLMAPCLIENT_METHOD_INSERT = "batchInsert";

	@SuppressWarnings("unchecked")
	@Override
	public void generate(ClassNode classNode, Method method, String sqlClientProperty, Class<?> sqlClientClass) {
		// 方法名称
		final String methodName = method.getName();

		// 构建一个方法节点
		MethodNode mn = new MethodNode(ACC_PUBLIC, methodName, BytecodeUtils.getMethodDesc(method.getParameterTypes(), method.getReturnType()), null, null);
		
		InsnList il = mn.instructions;
		il.add(new MethodInsnNode(INVOKESTATIC, BytecodeUtils.getClassName(BatchExecuteUtils.class), "getInstance", BytecodeUtils.getMethodDesc(new Class[]{}, BatchExecuteUtils.class)));
		il.add(new VarInsnNode(ALOAD, 0));
		il.add(new FieldInsnNode(GETFIELD, classNode.name, sqlClientProperty,BytecodeUtils.getClassDesc(sqlClientClass)));
		il.add(new LdcInsnNode(BytecodeUtils.getIbatisStatementName(classNode.name, methodName)));
		il.add(new VarInsnNode(ALOAD, 1));
		il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(BatchExecuteUtils.class), 
					SQLMAPCLIENT_METHOD_INSERT, 
					"(Lorg/springframework/orm/ibatis/SqlMapClientTemplate;Ljava/lang/String;Ljava/util/Collection;)V"));
		il.add(new InsnNode(RETURN));
		classNode.methods.add(mn);
	}

	@Override
	public boolean validate(Method method) {
		if(!Pattern.matches("(batchSave|batchInsert|batchAdd)[\\S]*", method.getName())){
			return false;
		}else if(method.getParameterTypes().length != 1){
			return false;
		}
		return true;
	}
}
