package com.zhaoyun.docmanager.biz.bean.diff;

import java.util.List;

/**
 * Created on 2016/8/16 18:11.
 * 未来不确定才更值得前行
 *
 * @author user
 */
public class ApiDiff {
    private String apiName;
    private String serviceName;
    private String desc1;
    private String desc2;
    private List<FieldDiff> paramIn;
    private FieldDiff paramOut;
    private Integer diffType;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public List<FieldDiff> getParamIn() {
        return paramIn;
    }

    public void setParamIn(List<FieldDiff> paramIn) {
        this.paramIn = paramIn;
    }

    public FieldDiff getParamOut() {
        return paramOut;
    }

    public void setParamOut(FieldDiff paramOut) {
        this.paramOut = paramOut;
    }

    public Integer getDiffType() {
        return diffType;
    }

    public void setDiffType(Integer diffType) {
        this.diffType = diffType;
    }
}
