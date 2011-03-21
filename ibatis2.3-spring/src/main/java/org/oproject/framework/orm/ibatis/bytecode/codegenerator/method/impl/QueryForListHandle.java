/*
 * description: 
 * date:        上午12:31:52
 * author:      ahli
 */
package org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.impl;

import java.lang.reflect.Method;
import java.util.List;
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
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * <p>
 * Ibatis集合结果集查询处理器
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @see org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class QueryForListHandle implements Opcodes, MethodGenerator{

	private static final String SQLMAPCLIENT_QUERYFORLIST = "queryForList";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		
		Class[] _paClasses = new Class[method.getParameterTypes().length + 2];
		_paClasses[0] = SqlMapClientTemplate.class;
		_paClasses[1] = String.class;
		_paClasses[2] = Object.class;
		if(_paClasses.length == 5){
			_paClasses[3] = int.class;
			_paClasses[4] = int.class;
			il.add(new VarInsnNode(ILOAD, 2));
			il.add(new VarInsnNode(ILOAD, 3));
		}
		il.add(new MethodInsnNode(INVOKEVIRTUAL, BytecodeUtils.getClassName(BatchExecuteUtils.class), 
				SQLMAPCLIENT_QUERYFORLIST, 
				BytecodeUtils.getMethodDesc(_paClasses, List.class)));
		il.add(new InsnNode(ARETURN));
		mn.maxStack = 0;
		mn.maxLocals = 0;
		classNode.methods.add(mn);
	}

	@Override
	public boolean validate(Method method) {
		if(!Pattern.matches("query[\\S]*ForList", method.getName())){
			return false;
		}else if(null == method.getParameterTypes() ){
			return false;
		}else if(method.getParameterTypes().length == 1){
			return true;
		}else if(method.getParameterTypes().length == 3 
				&& "int".equals(method.getParameterTypes()[1].getName()) 
				&& "int".equals(method.getParameterTypes()[2].getName())){
			return true;
		}
		return false;
	}
}
