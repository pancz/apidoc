/**
 * com Inc
 * Copyright (c) 2014-2015年11月17日 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.objects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * 类型转换工具类
 *
 * @author user
 * @since Revision:1.0.0, Date: 2015年11月17日 下午2:47:32 
 */
public abstract class ObjectConverter {
	
	/**
	 * 把source对象转换成另一个clazz类型的对象, 两种对象相同属性的类型，必须相同，否则会抛出BeansException异常。
	 * 内部采用BeanUtils.copyProperties(source, object)来实现属性复制。
	 * 
	 * @param source	源对象
	 * @param clazz		目标对象的类型
	 * @return
	 */
	public static <T> T convertObject(Object source, Class<T> clazz, String... ignoreProperties) {
		if (source == null) {
			return null;
		}
		
		T object = BeanUtils.instantiate(clazz);
		BeanUtils.copyProperties(source, object, ignoreProperties);
		return object;
	}
	
	/**
	 * 拷贝list,只支持List<Object>结构；不支持list里面的元素为集合类型
	 * 
	 * @param source	源list数据
	 * @param clazz		需要转换成的list元素类型
	 * @return	转换后的列表对象
	 */
	public static <T> List<T> convertList(List<?> source, Class<T> clazz, String... ignoreProperties) {
		if (source == null) {
			return null;
		}
		
		List<T> couponDOs = new ArrayList<T>();
		for (Object element : source) {
			couponDOs.add(convertObject(element, clazz, ignoreProperties));
		}
		return couponDOs;
	}

}
