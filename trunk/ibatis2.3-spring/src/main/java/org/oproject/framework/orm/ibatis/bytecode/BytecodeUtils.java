/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode;

/**
 * <p>
 * 字节码增强工具类，用于生成bytecode操作中经常出现的类型字符串等
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public abstract class BytecodeUtils {

	/**
	 * 私有构造
	 */
	private BytecodeUtils(){}
	
	/**
	 * 根据参数类型数组、返回类型生成方法描述
	 * 例如："(Ljava/lang/String;)Ljava/lang/Object;"
	 * @param paramsClass    参数类型
	 * @param returnClass    返回对象类型
	 * @return    方法描述信息Desc
	 */
	public static final String getMethodDesc(Class<?>[] paramsClass, Class<?> returnClass){
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(Class<?> paramClass : paramsClass){
			sb.append(getClassDesc(paramClass));
		}
		sb.append(")");
		if(null != returnClass && !"void".equals(returnClass.getName())){
			sb.append(getClassDesc(returnClass));
		}else{
			sb.append("V");
		}
		return sb.toString();
	}
	
	/**
	 * 返回参数类型的描述例如Ljava/lang/String;
	 * @param clazz    参数类型
	 * @return
	 */
	public static final String getClassDesc(Class<?> clazz){
		if(clazz.isArray()){
			return "[" + getClassDesc(clazz.getComponentType());
		}else if("int".equals(clazz.getName())){
			return "I";
		}else if("long".equals(clazz.getName())){
			return "J";
		}else if("boolean".equals(clazz.getName())){
			return "Z";
		}else if("char".equals(clazz.getName())){
			return "C";
		}else if("byte".equals(clazz.getName())){
			return "B";
		}else if("short".equals(clazz.getName())){
			return "S";
		}else if("float".equals(clazz.getName())){
			return "F";
		}else if("double".equals(clazz.getName())){
			return "D";
		}else {
			return "L" + clazz.getName().replaceAll("\\.", "/") + ";";
		}
	}
	
	/**
	 * 获取类型的名称，替换. 为/
	 * 例如java/lang/Object
	 * @param clazz
	 * @return
	 */
	public static final String getClassName(Class<?> clazz){
		return clazz.getName().replaceAll("\\.", "/");
	}
	
	/**
	 * 根据目标类路径、方法名称生成Ibatis查询语句statement的名称。
	 * 名称包括了命名空间空间
	 * @param targetClassPath
	 * @param method
	 * @return
	 */
	public static final String getIbatisStatementName(String targetClassPath, String method){
		String statementName = targetClassPath.substring(0, targetClassPath.length() - 8);
		int lastP = statementName.lastIndexOf("/");
		return statementName.substring(lastP + 1) + "." + method;
	}
}
