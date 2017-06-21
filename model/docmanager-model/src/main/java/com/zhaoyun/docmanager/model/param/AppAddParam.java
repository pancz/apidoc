/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * AppAddPram
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年8月15日 下午3:29:35 $
 */
public class AppAddParam implements Serializable {

    /** serialVersionUID  */
    private static final long serialVersionUID = 1L;
    
    /** 应用名称  */
    private String name;
    
    /** 应用介绍   */
    private String desc;
    
    /** pom中的group_id  */
    private String groupId;
    
    /** pom中的artifact_id */
    private String artifactId;
    
    /** 创建人 */
    private String            createPerson     = "system";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
