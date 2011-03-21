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
import org.oproject.framework.orm.PageResult;
import org.oproject.framework.orm.ibatis.BatchExecuteUtils;
import org.oproject.framework.orm.ibatis.bytecode.BytecodeUtils;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;

/**
 * <p>
 * Ibatis分页查询操作处理
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class QueryForPageResultHandle implements Opcodes, MethodGenerator{

	private static final String SQLMAPCLIENT_QUERYFORLIST = "queryForPageResult";
	
	@SuppressWarnings("unchecked")
	@Override
	public void generate(ClassNode classNode, Method method, String sqlClientProperty, Class<?> sqlClientClass) {
		// 方法名称
		final String methodName = method.getName();
		final Class<?>[] paramClasses = method.getParameterTypes();
		// 构建一个方法节点
		MethodNode mn = new MethodNode(ACC_PUBLIC, methodName, BytecodeUtils.getMethodDesc(method.getParameterTypes(), method.getReturnType()), null, null);
		InsnList il = mn.instructions;
		il.add(new MethodInsnNode(INVOKESTATIC, BytecodeUtils.getClassName(BatchExecuteUtils.class), "getInstance", 
				BytecodeUtils.getMethodDesc(new Class[]{}, BatchExecuteUtils.class)));
		il.add(new VarInsnNode(ALOAD, 0));
		il.add(new FieldInsnNode(GETFIELD, classNode.name, sqlClientProperty,BytecodeUtils.getClassDesc(sqlClientClass)));
		il.add(new LdcInsnNode(BytecodeUtils.getIbatisStatementName(classNode.name, methodName + "Count")));
		il.add(new LdcInsnNode(BytecodeUtils.getIbatisStatementName(classNode.name, methodName)));
		
		// 如果是三个参数
		if(paramClasses.length == 3){
			il.add(new VarInsnNode(ALOAD, 1));
			il.add(new VarInsnNode(ILOAD, 2));
			il.add(new VarInsnNode(ILOAD, 3));
			il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(BatchExecuteUtils.class), 
					SQLMAPCLIENT_QUERYFORLIST, 
					"(Lorg/springframework/orm/ibatis/SqlMapClientTemplate;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;II)" + 
					BytecodeUtils.getClassDesc(PageResult.class)));		
		}else{
			il.add(new VarInsnNode(ILOAD, 1));
			il.add(new VarInsnNode(ILOAD, 2));
			il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(BatchExecuteUtils.class), 
					SQLMAPCLIENT_QUERYFORLIST, 
					"(Lorg/springframework/orm/ibatis/SqlMapClientTemplate;Ljava/lang/String;Ljava/lang/String;II)" + 
					BytecodeUtils.getClassDesc(PageResult.class)));		
		}
		il.add(new InsnNode(ARETURN));
		classNode.methods.add(mn);
	}

	@Override
	public boolean validate(Method method) {
		final String methodName = method.getName();
		final Class<?>[] paramClasses = method.getParameterTypes();
		
		if(!Pattern.matches("query[\\S]*ForPageResult", methodName)){
			return false;
		}else if(null == paramClasses ){
			return false;
		}else if(paramClasses.length == 2 
				&& "int".equals(paramClasses[0].getName()) 
				&& "int".equals(paramClasses[1].getName())){
			return true;
		}else if(paramClasses.length == 3 
				&& "int".equals(paramClasses[1].getName()) 
				&& "int".equals(paramClasses[2].getName())){
			return true;
		}
		return false;
	}
}
