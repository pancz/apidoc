package com.zhaoyun.docmanager.model.vo;

import java.util.Date;
import java.util.List;

public class DocAppVersionVo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.id
     * 版本主键
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.app_id
     * 应用主键
     *
     * @mbggenerated
     */
    private Integer appId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.is_default
     * 是否默认版本(0：否；1：是)(一个应用只能有一个默认版本)
     *
     * @mbggenerated
     */
    private Integer isDefault;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.version
     * pom中的version
     *
     * @mbggenerated
     */
    private String version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.desc
     * 版本介绍
     *
     * @mbggenerated
     */
    private String desc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.create_time
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.create_person
     * 创建人
     *
     * @mbggenerated
     */
    private String createPerson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.update_time
     * 修改时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column doc_app_version.update_person
     * 更新人
     *
     * @mbggenerated
     */
    private String updatePerson;

    /**  服务列表*/
    private List<DocServiceVo> docServiceVos;

    public List<DocServiceVo> getDocServiceVos() {
        return docServiceVos;
    }

    public void setDocServiceVos(List<DocServiceVo> docServiceVos) {
        this.docServiceVos = docServiceVos;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.id
     *
     * @return the value of doc_app_version.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.id
     *
     * @param id the value for doc_app_version.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.app_id
     *
     * @return the value of doc_app_version.app_id
     *
     * @mbggenerated
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.app_id
     *
     * @param appId the value for doc_app_version.app_id
     *
     * @mbggenerated
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.is_default
     *
     * @return the value of doc_app_version.is_default
     *
     * @mbggenerated
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.is_default
     *
     * @param isDefault the value for doc_app_version.is_default
     *
     * @mbggenerated
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.version
     *
     * @return the value of doc_app_version.version
     *
     * @mbggenerated
     */
    public String getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.version
     *
     * @param version the value for doc_app_version.version
     *
     * @mbggenerated
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.desc
     *
     * @return the value of doc_app_version.desc
     *
     * @mbggenerated
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.desc
     *
     * @param desc the value for doc_app_version.desc
     *
     * @mbggenerated
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.create_time
     *
     * @return the value of doc_app_version.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.create_time
     *
     * @param createTime the value for doc_app_version.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.create_person
     *
     * @return the value of doc_app_version.create_person
     *
     * @mbggenerated
     */
    public String getCreatePerson() {
        return createPerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.create_person
     *
     * @param createPerson the value for doc_app_version.create_person
     *
     * @mbggenerated
     */
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.update_time
     *
     * @return the value of doc_app_version.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.update_time
     *
     * @param updateTime the value for doc_app_version.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column doc_app_version.update_person
     *
     * @return the value of doc_app_version.update_person
     *
     * @mbggenerated
     */
    public String getUpdatePerson() {
        return updatePerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column doc_app_version.update_person
     *
     * @param updatePerson the value for doc_app_version.update_person
     *
     * @mbggenerated
     */
    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_app_version
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appId);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", version=").append(version);
        sb.append(", desc=").append(desc);
        sb.append(", createTime=").append(createTime);
        sb.append(", createPerson=").append(createPerson);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updatePerson=").append(updatePerson);
        sb.append("]");
        return sb.toString();
    }
}