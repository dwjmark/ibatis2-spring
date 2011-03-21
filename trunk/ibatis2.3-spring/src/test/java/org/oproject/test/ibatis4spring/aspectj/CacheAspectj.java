/**
 * 
 */
package org.oproject.test.ibatis4spring.aspectj;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.oproject.test.ibatis4spring.annotations.CacheSupport;

/**
 * <p>
 * 自动化测试用，缓存拦截器
 * </p>
 * @see java.lang.annotation.RetentionPolicy.RUNTIME
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-18
 * @since v1.0
 */
@Aspect
public class CacheAspectj {

	/**
	 * 日志对象
	 */
	private static final Log log = LogFactory.getLog(CacheAspectj.class);
	
	/**
	 * 模拟用内存
	 */
	private Map<String, Object> cache = new HashMap<String, Object>();
	
	/**
	 * 拦截所有
	 * @param joinPoint       切入点
	 * @param cacheSupport    缓存支持注释
	 * @return                被拦截方法返回对象
	 * @throws Throwable      拦截过程中抛出异常
	 */
	@Around("@annotation(cacheSupport)")
	public Object invokeService(ProceedingJoinPoint joinPoint, 
			CacheSupport cacheSupport) throws Throwable
	{
		final String key = cacheSupport.value();
		log.info("获取缓存Key:" + key);
		Object cachedValue = cache.get(key);
		if(null == cachedValue){
			log.info("未缓存，读取数据，并缓存");
			String[] keys =  cacheSupport.keys();
			log.info("value:" + cacheSupport.value() + ", timeout:" + cacheSupport.timeout() + ", " + Arrays.toString(keys));
			cachedValue = joinPoint.proceed();
			cache.put(key, cachedValue);
			return cachedValue;
		}else{
			log.info("返回缓存数据");
			return cache.get(key);
		}
	}
}
