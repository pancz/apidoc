package com.zhaoyun.docmanager.biz.bean.diff;

import java.util.List;

/**
 * Created on 2016/8/16 18:04.
 * 未来不确定才更值得前行
 *
 * @author user
 */
public class PomDiff {
    private String appName;
    private String version1;
    private String version2;
    private List<ApiDiff> addApiList;
    private List<ApiDiff> changeApiList;
    private List<ApiDiff> deleteApiList;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion1() {
        return version1;
    }

    public void setVersion1(String version1) {
        this.version1 = version1;
    }

    public String getVersion2() {
        return version2;
    }

    public void setVersion2(String version2) {
        this.version2 = version2;
    }

    public List<ApiDiff> getAddApiList() {
        return addApiList;
    }

    public void setAddApiList(List<ApiDiff> addApiList) {
        this.addApiList = addApiList;
    }

    public List<ApiDiff> getChangeApiList() {
        return changeApiList;
    }

    public void setChangeApiList(List<ApiDiff> changeApiList) {
        this.changeApiList = changeApiList;
    }

    public List<ApiDiff> getDeleteApiList() {
        return deleteApiList;
    }

    public void setDeleteApiList(List<ApiDiff> deleteApiList) {
        this.deleteApiList = deleteApiList;
    }
}
