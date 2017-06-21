/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.constants;

/**
 * 导航栏类型枚举
 * @author user
 * @version $Id: NavTypeEnum.java, v 0.1 2016年8月26日 下午6:15:34 user Exp $
 */
public enum NavTypeEnum {

     ROOT_STATIC(0, "文档说明", null), 
     STATIC_0(1, "接口规范", ROOT_STATIC),
     STATIC_1(1, "注解规范", ROOT_STATIC),
     ROOT_APP(0, "应用", null),
     APP(1, "应用项", ROOT_APP),
     ROOT_API(2,"API接口",APP),
     MESSAGE(2,"消息接口",APP),
     SERVICE(3,"服务项",ROOT_API),
     DEPRECATED_SERVICE_LIST(3,"过期的服务",ROOT_API),
     DEPRECATED_SERVICE(4,"过期服务项",DEPRECATED_SERVICE_LIST),
     DEPRECATED_SERVICE_API(5,"过期的服务的API项",DEPRECATED_SERVICE),
     DEPRECATED_API_LIST(4,"过期的API",SERVICE),
     API(4,"API项",SERVICE),
     DEPRECATED_API(5,"过期的API项",DEPRECATED_API_LIST)
     ;

    private int level;

    private String      desc;


    private NavTypeEnum parentType;

    NavTypeEnum(int level, String desc, NavTypeEnum parentType) {
        this.level = level;
        this.desc = desc;
        this.parentType = parentType;
    }

    public String getDesc() {
        return desc;
    }

    public NavTypeEnum getParentType() {
        return parentType;
    }

    public int getLevel() {
        return level;
    }

}
