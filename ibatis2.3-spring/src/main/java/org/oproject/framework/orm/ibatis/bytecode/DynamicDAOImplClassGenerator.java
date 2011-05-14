/**
 * 
 */
package org.oproject.framework.orm.ibatis.bytecode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.property.BeanPropertyAndSetterMethodGenerator;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>
 * 动态数据访问实现类构造器
 * </p>
 * @see org.objectweb.asm.Opcodes
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class DynamicDAOImplClassGenerator implements Opcodes {

	/**
	 * 接口名称，以.分割包和类
	 */
	private String interfaceName = null;
	
	/**
	 * 接口名称路径，以/分割包和类
	 */
	private String interfaceNamePath = null;
	
	/**
	 * 目标类名
	 */
	private String targetClassName = null;
	
	/**
	 * 实现接口的目标类路径，以/分割包和类
	 */
	private String targetClassNamePath = null;
	
	/**
	 * 类节点
	 */
	private ClassNode classNode = new ClassNode();
	
	/**
	 * 接口类型信息
	 */
	@SuppressWarnings("rawtypes")
	private Class interfaceClass = null;
	
	/**
	 * 接口声明的方法集合
	 */
	private Method[] interfaceMethods = null;
	
	/**
	 * SqlClient类
	 */
	private Class<?> sqlClientClass;
	
	/**
	 * SqlClient属性
	 */
	private String sqlClientProperty;
	
	/**
	 * 类源注解
	 */
	private List<Annotation> annotation = new ArrayList<Annotation>();
	
	/**
	 * 默认构造函数
	 * @param interfaceName 数据访问接口
	 */
	public DynamicDAOImplClassGenerator(String interfaceName, String newClassName){
		this.interfaceName = interfaceName;
		this.interfaceNamePath = interfaceName.replaceAll("\\.", "/");
		this.targetClassName = newClassName;
		this.targetClassNamePath = targetClassName.replaceAll("\\.", "/");;
	}
	
	/**
	 * 定义类到当前线程上下文ClassLoader中
	 * @throws Exception
	 */
	public void defineClass() throws Exception{
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		this.classNode.accept(cw);
		net.sf.cglib.core.ReflectUtils.defineClass(targetClassName, cw.toByteArray(), Thread.currentThread().getContextClassLoader());
		
		//生成具体的类到文件
		//writeClassToFile(cw);
	}
	
	/**
	 * 生成具体的类到文件
	 * @param cw
	 */
	private void writeClassToFile(ClassWriter cw) {
		 final byte[] bs = cw.toByteArray();  
		 java.io.OutputStream out = null;
		try {
			java.io.File file = new java.io.File(targetClassNamePath+".class");
			java.io.File filep = file.getParentFile();
			if(!filep.exists())
			{
				filep.mkdirs();
			}
			System.out.println("write asm class to : " + file.getAbsolutePath());
			out = new java.io.FileOutputStream(file);
		     out.write(bs);  
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} 
		finally
		{
			if(out != null)
			{
				try
				{
					out.close();
				}catch(java.io.IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}

	
	/**
	 * 初始化类
	 * @throws Exception
	 */
	public void initClass() throws Exception{
		
		// 从定义的接口参数中获取接口信息，并对接口进行验证
		setInterfaceInfo();
		
		// 创建类信息
		createClassInfo();
		
		// 创建构造函数
		createMethodConstruct();
	}
	
	/**
	 * 读取接口信息，并且进行验证
	 * @throws ClassNotFoundException 当ClassLoader无法装载接口类型的时候，抛出；
	 * @exception IllegalArgumentException 当接口名称不是以"DAO"结尾或者接口参数类型不是一个Interface类
	 * 型的时候，抛出该异常。
	 */
	private void setInterfaceInfo() throws ClassNotFoundException{
		
		/*
		 * 如果参数接口不是以DAO结尾，则抛出异常。
		 * 接口类名称必须是以DAO结尾。 
		 */
		if(!interfaceName.endsWith("DAO")){
			throw new IllegalArgumentException(interfaceName + " is not end with 'DAO'.");
		}
		
		/*
		 * 加载接口接口名称
		 * 如果参数接口不是接口，抛出异常。
		 */
		interfaceClass = Thread.currentThread().getContextClassLoader().loadClass(interfaceName);
		if(!interfaceClass.isInterface()){
			throw new IllegalArgumentException(interfaceName + " is not a Interface.");
		}
		// 将接口方法数组设置到当前类参数上
		this.interfaceMethods = interfaceClass.getMethods();
	}
	
	/**
	 * 在内存中创建类定义，并且设置该类的基础信息
	 */
	@SuppressWarnings("unchecked")
	private void createClassInfo() {
		this.classNode.version = V1_6;
		this.classNode.access = ACC_PUBLIC;
		this.classNode.name = targetClassNamePath;
		this.classNode.superName = "java/lang/Object";
		this.classNode.interfaces.add(interfaceNamePath);

		/*
		 * 判断是否存在类上面的源注释，如果存在则定义源注释
		 */
		if(null != annotation && !annotation.isEmpty()){
			for(Annotation ann : annotation){
				// 获取源注解的类型
				Class<? extends Annotation> annotationClazz = ann.annotationType();
				/*
				 * 获取元注解中定义的方法，这里不支持继承关系的元注解
				 * 依次拷贝源注解中的属性到实现类上
				 */
				AnnotationVisitor av = this.classNode.visitAnnotation(BytecodeUtils.getClassDesc(annotationClazz), true);
				//copyAnnotationToVisitor(ann, av);
				
				//直接由Spring 反射
				copyAnnotationToVisitorBySpring(ann, av);
				av.visitEnd();
			}
		}
	}
	
	/**
	 * 创建类的构造函数
	 */
	@SuppressWarnings("unchecked")
	private void createMethodConstruct() {
		MethodNode mn_construct = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
		InsnList li = mn_construct.instructions;
		li.add(new VarInsnNode(ALOAD, 0));
		li.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
		li.add(new InsnNode(RETURN));
		mn_construct.maxLocals = 0;
		mn_construct.maxStack = 0;
		this.classNode.methods.add(mn_construct);
	}
	
	/**
	 * 创建属性以及对应的getter、setter方法
	 * @param propertyClass
	 */
	public void addSqlMapClientProperty(Class<?> propertyClass, String propertyName, String sqlMapClientTemplateBeanID){
		this.sqlClientClass = propertyClass;
		this.sqlClientProperty = propertyName;
		new BeanPropertyAndSetterMethodGenerator().generate(this.classNode, propertyClass, propertyName, sqlMapClientTemplateBeanID);
	}
	
	/**
	 * 实现接口方法
	 * @param methodGenerators
	 */
	public void addImplementsMethod(Collection<MethodGenerator> methodGenerators){
		for(Method method: interfaceMethods){
			boolean setMethodSuccess = false;
			for(MethodGenerator methodGenerator : methodGenerators){
				if(methodGenerator.validate(method)){
					methodGenerator.generate(classNode, method, sqlClientProperty, sqlClientClass);
					// 从队列中取最后添加的对象，做元注解的支持
					copyMethodAnnotations(method);
					setMethodSuccess = true;
					break;
				}
			}
			if(!setMethodSuccess){
				throw new IllegalArgumentException("没有找到适合的方法处理器:" + method.getName());
			}
		}
	}

	/**
	 * 生成元注解
	 * @param method
	 */
	private void copyMethodAnnotations(Method method) {
		Annotation[] methodAnnotations = method.getAnnotations();
		if(null != methodAnnotations && methodAnnotations.length > 0){
			MethodNode mn = (MethodNode)classNode.methods.get(classNode.methods.size() - 1);
			for(Annotation annotation : methodAnnotations){
				// 获取源注解的类型
				// 获取元注解中定义的方法，这里不支持继承关系的元注解
				AnnotationVisitor av = mn.visitAnnotation(BytecodeUtils.getClassDesc(annotation.annotationType()), true);
				//copyAnnotationToVisitor(annotation, av);
				
				//直接由Spring 反射
				copyAnnotationToVisitorBySpring(annotation, av);
				av.visitEnd();
			}
		}
	}
	
	/**
	 * 直接通过Spring 的AnnotationUtils类获得注解属性，并添加到visit
	 * @param annotation
	 * @param av
	 */
	private void copyAnnotationToVisitorBySpring(Annotation annotation,
			AnnotationVisitor av)
	{
		Map<String, Object> map = AnnotationUtils.getAnnotationAttributes(annotation);
        Set<Map.Entry<String,Object>> set = map.entrySet();
        for (Map.Entry<String, Object> entry : set) {
        	String key = entry.getKey();
        	Object obj = entry.getValue();
        	if(obj != null && obj.toString() != null && !(obj.toString().trim().equals("")))
        	{
        		av.visit(key, obj);
        	}	
		}
	}

	/**
	 * 拷贝、并且在对应的访问器上克隆源注解
	 * @param annotation    被克隆源注解
	 * @param av            访问器
	 */
	@SuppressWarnings("rawtypes")
	private void copyAnnotationToVisitor(Annotation annotation,
			AnnotationVisitor av) {
		// 或者源注解类型
		Class<? extends Annotation> annotationClazz = annotation.annotationType();
		// 获取源注解上定义的方法，不支持集成父类的方法定义
		Method[] methods = annotationClazz.getDeclaredMethods();
		// 依次复制元注解中的各个配置
		for(Method annMethod: methods){
			try {
				// 申明一个返回结果类型
				Class retClass = annMethod.getReturnType();
				// 申明一个变量，存储返回结果对象
				Object annValue = annMethod.invoke(annotation, new Object[]{});
				warpVisitAnnotationValue(retClass, annMethod.getName(), annValue, av);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	/**
	 * 包装后的Annotation的属性访问方法
	 * @param clazz    Annotation属性返回类型
	 * @param name     Annotation属性名称
	 * @param value    Annotation属性返回值
	 * @param av       Annotation访问器
	 */
	@SuppressWarnings("rawtypes")
	private void warpVisitAnnotationValue(Class clazz, String name, Object value, AnnotationVisitor av){
		if(clazz.isArray()){
			Object[] valueArray = (Object[])value;
			AnnotationVisitor arrayAV = av.visitArray(name);
			for(Object val : valueArray){
				warpVisitAnnotationValue(val.getClass(), "", val, arrayAV);
			}
			arrayAV.visitEnd();
		}else if(clazz.isEnum()){
			av.visitEnum(name, BytecodeUtils.getClassDesc(clazz), value.toString());
		}else{
			av.visit(name, value);
		}
	}
	
	public void addClassAnnotation(Annotation ann){
		this.annotation.add(ann);
	}
}
