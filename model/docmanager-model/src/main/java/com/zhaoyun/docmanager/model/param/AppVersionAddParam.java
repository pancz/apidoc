/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import java.io.Serializable;

/**
 * AppVersionAddParam
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年8月16日 下午1:41:12 $
 */
public class AppVersionAddParam implements Serializable{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** 系统名称 */
    private String name;
    
    /** facade版本号 */
    private String version;
    
    /** 描述  */
    private String desc;
    
    /** 创建人 */
    private String createPerson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }
    
}
