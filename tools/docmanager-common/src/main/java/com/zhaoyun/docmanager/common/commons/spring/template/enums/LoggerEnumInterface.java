/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template.enums;

import org.slf4j.Logger;

/**
 * 日志枚举接口
 * 
 * <p>
 *      主要用于约束通过枚举，获取代码和备注，可以使实现本接口的枚举形成统一
 * </p>
 * 
 * @author user
 * @version $Id: EnumInterface.java, v 0.1 2015年9月10日 下午5:20:18 user Exp $
 */
public interface LoggerEnumInterface {

    /**
     * 返回日志对象
     * @return
     * @date: 2015年11月19日 下午3:05:15
     */
    public Logger getLogger();

    /**
     * 
     * @return 枚举代码
     */
    public String getCode();

    /**
     * 接口描述，用于业务日志输出
     * @return 枚举代码描述
     */
    public String getMessage();
}
