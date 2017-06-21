/**
 * com Inc
 * Copyright (c) 2014-2015年11月17日 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数值类型的参数校验，支持类型校验，值校验，长度较验，是否为空，正则校验
 *
 * @author user
 * @since Revision:1.0.0, Date: 2015年11月17日 下午9:14:37 
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
			
	/**
	 * 参数是否可为空
	 */
	public boolean required() default false;
	
	/**
	 * 参数是否可为空
	 */
	public boolean isNotBlank() default false;
	
	/**
	 * EMAIL,IP,NUMBER,PHONENUMBER,URL,DATE(格式"yyyy-dd-mm HH:mm:ss");
	 */
	public String type() default "";
	
	/**
	 * 字符串最大长度 -1 表示不校验, 
	 * 当value为字符串格式时表示字符串的长度。
	 * 当value为list或Array时，表示相应数组的长度
	 */
	public int maxLength() default -1; 
	
	/**
	 * 字符串的最小长度 -1 表示不校验
	 * 当value为字符串格式时表示字符串的长度。
	 * 当value为list或Array时，表示相应数组的长度
	 */
	public int minLength() default -1;
	
	/**
	 * number的最大值，格式为属性值的toString()格式
	 */
	public String maxValue() default "";
	
	/**
	 * number的最小值，格式为属性值的toString()格式
	 */
	public String minValue() default "";
	
    /**
     * 校验是否符合指定的正则表达式
     */
    public String regexp() default "";
    
    /**
     * 默认值，只能设置基本变量。当设置此值时，并且属性值为null时，用此值替换成相应的值
     */
    public String defaultValue() default "";
    
    /**
     * 枚举值列表，用逗号分割
     */
    public String enumValues() default "";
    
}
