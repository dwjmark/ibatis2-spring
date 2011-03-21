/**
 * 
 */
package org.oproject.framework.orm.ibatis.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oproject.framework.orm.ibatis.bytecode.DynamicDAOImplClassGenerator;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.annotations.DynamicIbatisDAO;
import org.oproject.framework.orm.ibatis.bytecode.codegenerator.method.MethodGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * <p>
 * Ibatis动态数据访问层工厂bean，基于asm实现根据接口命名规则而动态生成实现类，并将
 * 实现类在spring容器实例化对象前，定义到spring的上下文中。在接口上做了注释拷贝的
 * 操作，所以在接口上定义例如aspectj的AOP源注释是可以生效的。
 * </p>
 * @see org.springframework.context.ApplicationContextAware
 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class DynamicIbatisBeanFactoryPostProcessor implements ApplicationContextAware, BeanFactoryPostProcessor {
	
	/**
	 * 日志对象
	 */
	private static final Log log = LogFactory.getLog(DynamicIbatisBeanFactoryPostProcessor.class);
	
	/**
	 * 模式资源解析器
	 */
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	/**
	 * 应用上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 要扫描的对象路径集合
	 */
	private List<String> basePath = new ArrayList<String>();
	
	/**
	 * bean定义数组
	 */
	private Map<String, GenericBeanDefinition> beanDefinitionMap = new HashMap<String, GenericBeanDefinition>();
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 设置扫描路径
	 * @param list    List&lt;String&gt;
	 */
	public void setScanPath(List<String> list){
		basePath = list;
	}
	
	/**
	 * 设置扫描路径
	 * @return    扫描的对象路径配置集合
	 */
	public List<String> getScanPath(){
		return basePath;
	}
	
	/**
	 * 扫描包，并且初始化字节码增强DAOImplBean
	 * @param basePackage
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void scanPackageAndInitDAOBean(String basePackage, ConfigurableListableBeanFactory arg0) throws IOException{
		
		// 声明debug日志
		StringBuilder debugLogStringBuilder = null;//new StringBuilder();
		
		if(log.isDebugEnabled()){
			debugLogStringBuilder = new StringBuilder();
			debugLogStringBuilder.append("scan package =>");
		}
		// 基础包扫描路径classpath *:xxxx
		final String basePackageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage);
		// 在扫描路径后面拼装/**/*.class
		final String packageSearchPath = basePackageSearchPath + "/**/*.class";
		// 获取该结果中的所有资源对象
		final Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		// 遍历资源集合
		for(Resource resource : resources){
			// 获取类资源路径对象
			final ClassPathResource classPathResource = new ClassPathResource(resource.getURI().getPath());
			// 应用上下文的classloader的基础路径URL服务器运行的classes目录
			URL url = applicationContext.getClassLoader().getResource("/"); 
			if(null == url){
				url = applicationContext.getClassLoader().getResource("");
			}
			final String orginClassPath = classPathResource.getPath();
			final String _orginClassPath = orginClassPath.replaceAll(" ", "%20");
			final String urlPath = url.getPath();
			final String classPath = _orginClassPath.substring(urlPath.length() - 1);
			
			// 删除最后的".class"文件后缀，获取类名
			final String className = classPath.replace("/", ".").substring(0, classPath.length() - 6);
			try {
				// 加载类
				Class<?> interfaceClass = Class.forName(className);
				
				// 必须@DynamicIbatisDAO接口
				DynamicIbatisDAO ac = interfaceClass.getAnnotation(DynamicIbatisDAO.class);
				if(interfaceClass.isInterface() && null != ac){
					if(null != debugLogStringBuilder){
						debugLogStringBuilder.append(className).append(":");
					}
					
					// 2011-2-21日添加，必须定义Component属性
					Component component = interfaceClass.getAnnotation(Component.class);
					
					// 如果DynamicIbatisDAO没有设置bean的ID，则读取Component标签中的配置值
					final String beanName = "".equals(ac.value())?component.value() : ac.value();
					
					if("".equals(beanName.trim())){
						throw new IllegalArgumentException("must config bean id in springIOC.");
					}
					
					// 从容器中获取sqlMapClientTemplate
					String sqlMapClientTemplateBeanID = ac.sqlMapClientTemplate();

					if (null == sqlMapClientTemplateBeanID || "".equals(sqlMapClientTemplateBeanID.trim())) {
						throw new IllegalArgumentException(beanName
								+ ".property is null.");
					}
					// 通过反射注入
					Collection<MethodGenerator> methodGenerators = applicationContext.getBeansOfType(MethodGenerator.class).values();
					DynamicDAOImplClassGenerator classGenerator = new DynamicDAOImplClassGenerator(interfaceClass.getName(), interfaceClass.getName() + "$Impl");

					if(null != component ){
						classGenerator.addClassAnnotation(component);
					}
					
					classGenerator.initClass();
					classGenerator.addSqlMapClientProperty(SqlMapClientTemplate.class, "sqlMapClient", sqlMapClientTemplateBeanID);
					classGenerator.addImplementsMethod(methodGenerators);
					classGenerator.defineClass();
					Class clazz = Class.forName(interfaceClass.getName() + "$Impl");
					Object obj = arg0.createBean(clazz);
					if(null != debugLogStringBuilder){
						debugLogStringBuilder.append(obj).append(", ");
					}
					/*
					 *  创建一个通用的Bean定义对象,并且放入到beanDefinitionMap中
					 *  在所有包扫描、实现类创建完成后，统一设置到IOC容器中
					 */
					GenericBeanDefinition daoImplBeanDefinition = new GenericBeanDefinition();
					daoImplBeanDefinition.setBeanClass(clazz);
					beanDefinitionMap.put(beanName, daoImplBeanDefinition);
				}
			} catch (Exception e) {
				log.error(e.toString());
				throw new RuntimeException(e);
			}
		}
		if(null != debugLogStringBuilder){
			log.debug(debugLogStringBuilder.toString());
		}
	}
	
	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException {
		// 验证，如果基础配置路径是空的话，直接返回。
		if(null == basePath || basePath.isEmpty()){
			return;
		}
		// 遍历基础配置路径，并扫描对应配置下的资源，并且根据配置生成动态类
		for(String basePackage : basePath){
			try{
				// 扫描包，并且生成DAO对象，设置到ApplicationContext中
				scanPackageAndInitDAOBean(basePackage, arg0);
			}catch(IOException e){
				throw new IllegalArgumentException("scan package and init bean-> " + basePackage);
			}
		}
		
		if(arg0 instanceof BeanDefinitionRegistry && !beanDefinitionMap.isEmpty()){
			BeanDefinitionRegistry bdr = (BeanDefinitionRegistry)arg0;
			for(Map.Entry<String, GenericBeanDefinition> entry : beanDefinitionMap.entrySet()){
				bdr.registerBeanDefinition(entry.getKey(), entry.getValue());
			}
			
		}
	}
}
