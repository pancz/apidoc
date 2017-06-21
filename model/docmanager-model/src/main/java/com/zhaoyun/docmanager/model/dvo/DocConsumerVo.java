/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.dvo;

/**
 *
 *
 * @author yanglizhong
 * @version v 0.1 2016/11/7 16:32
 */
public class DocConsumerVo {
    /** 应用名称 */
    private String appName;
    /** 平均响应耗时，sumTime/6 */
    private String avgTime;
    /** 方法名称 */
    private String methodName;
    /** 总的响应耗时 */
    private String sumTime;
    /** 总的访问次数 */
    private String visits;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }
}
