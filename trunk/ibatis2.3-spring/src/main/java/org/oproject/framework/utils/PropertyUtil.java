package org.oproject.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

/**
 * <p>
 * 反射工具类,提供基于反射机制的对象工具方法
 * </p>
 * @author aohai.li
 * @version ibatis2.x-spring3.0, 2011-3-19
 * @since v1.0
 */
public class PropertyUtil {
	
	/**
	 * 日志对象
	 */
	private static Logger log = Logger.getLogger(PropertyUtil.class);
	
	/**
	 * 通过反射机制设置对象属性
	 * @param target    操作的对象
	 * @param fname     属性名称
	 * @param ftype     属性类型
	 * @param fvalue    属性值对象
	 */ 
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static void setFieldValue(Object target, String fname, Class ftype,
			Object fvalue)
	{
		
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass())))
		{
			return;
		}
		Class clazz = target.getClass();

		try
		{
			Field field = clazz.getDeclaredField(fname);
			if (!Modifier.isPublic(field.getModifiers()))
			{
				field.setAccessible(true);
			}
			field.set(target, fvalue);
		}
		catch (Exception fe)
		{
			log.error("set field error:" + fe.toString());
		}
	}   
	
	/**
	 * 通过反射机制设置对象属性
	 * 通过指定具体操作对象类型，可以对该类的机遇继承和获得的属性进行赋值操作。
	 * @param target    操作的对象
	 * @param target    操作的对象类型
	 * @param fname     属性名称
	 * @param ftype     属性类型
	 * @param fvalue    属性值对象
	 */ 
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static void setFieldValue(Object target, Class targetClass, String fname, Class ftype,
			Object fvalue)
	{
		
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass())))
		{
			return;
		}

		try
		{
			Field field = targetClass.getDeclaredField(fname);
			if (!Modifier.isPublic(field.getModifiers()))
			{
				field.setAccessible(true);
			}
			field.set(target, fvalue);
		}
		catch (Exception fe)
		{
			log.error("set field error:" + fe.toString());
		}
	} 

}
