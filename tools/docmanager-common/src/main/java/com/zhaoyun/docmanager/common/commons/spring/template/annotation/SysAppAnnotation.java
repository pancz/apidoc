/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务注解
 *      业务编码、
 *      业务名称、
 *      业务主键： 用于定位唯一业务调用
 * 
 * @author user
 * @version $Id: SysAppAnnotation.java, v 0.1 2016年6月17日 下午9:13:57 user Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface SysAppAnnotation {

    /** 业务编码 */
    String actionCode() default "";

    /** 业务名称 */
    String actionMsg() default "";

    /** 业务主键，用于 prdex(1|xx|xx)(2|xx|xx)_xsdf(2|xx|xx) */
    String bizKey() default "";

}
