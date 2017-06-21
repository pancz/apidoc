/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

/**
 * 
 * @author user
 * @version $Id: AppVersionQueryPram.java, v 0.1 2016年8月15日 下午4:30:51 user Exp $
 */
public class AppVersionQueryParam extends PagedQueryParam {

    /**  */
    private static final long serialVersionUID = 1L;

    /**  版本主键*/
    private Integer           id;

    /**  应用主键*/
    private Integer           appId;

    /** pom中的group_id  */
    private String            groupId;

    /** pom中的artifact_id */
    private String            artifactId;

    /**  pom中的version*/
    private String            version;

    /**
     * 是否默认版本(0：否；1：是)(一个应用只能有一个默认版本)
     */
    private Integer           isDefault;

    /**  最大版本,专供版本比较的下拉列表*/
    private String            maxVersion;

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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

    /**
     * Getter method for property <tt>id</tt>.
     * 
     * @return property value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     * 
     * @param id value to be assigned to property id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>appId</tt>.
     * 
     * @return property value of appId
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * Setter method for property <tt>appId</tt>.
     * 
     * @param appId value to be assigned to property appId
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * Getter method for property <tt>version</tt>.
     * 
     * @return property value of version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter method for property <tt>version</tt>.
     * 
     * @param version value to be assigned to property version
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
