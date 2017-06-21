package com.zhaoyun.docmanager.model.param;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/8/9 13:33.
 * 未来不确定才更值得前行
 */
public class AddFacadeParam {
    @NotBlank
    private String groupId;
    @NotBlank
    private String artifactId;
    @NotBlank
    private String version;
    @NotNull
    private Integer appId;
    private String desc;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
