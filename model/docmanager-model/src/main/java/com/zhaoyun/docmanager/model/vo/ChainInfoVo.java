/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 *
 *
 * @author yanglizhong
 * @version v 0.1 2016/11/28 14:17
 */
public class ChainInfoVo implements Serializable {

    /**
     * 日调用量
     */
    private Integer dayVisits;
    /**
     * 周调用量
     */
    private Integer weekVisits;
    /**
     * 月调用量
     */
    private Integer monthVisits;

    /**
     * 日调用平均响应时间
     */
    private Integer dayVisitsAvgTime;
    /**
     * 周调用平均响应时间
     */
    private Integer weekVisitsAvgTime;
    /**
     * 月调用平均响应时间
     */
    private Integer monthVisitsAvgTime;

    public Integer getDayVisits() {
        return dayVisits;
    }

    public void setDayVisits(Integer dayVisits) {
        this.dayVisits = dayVisits;
    }

    public Integer getDayVisitsAvgTime() {
        return dayVisitsAvgTime;
    }

    public void setDayVisitsAvgTime(Integer dayVisitsAvgTime) {
        this.dayVisitsAvgTime = dayVisitsAvgTime;
    }

    public Integer getMonthVisits() {
        return monthVisits;
    }

    public void setMonthVisits(Integer monthVisits) {
        this.monthVisits = monthVisits;
    }

    public Integer getMonthVisitsAvgTime() {
        return monthVisitsAvgTime;
    }

    public void setMonthVisitsAvgTime(Integer monthVisitsAvgTime) {
        this.monthVisitsAvgTime = monthVisitsAvgTime;
    }

    public Integer getWeekVisits() {
        return weekVisits;
    }

    public void setWeekVisits(Integer weekVisits) {
        this.weekVisits = weekVisits;
    }

    public Integer getWeekVisitsAvgTime() {
        return weekVisitsAvgTime;
    }

    public void setWeekVisitsAvgTime(Integer weekVisitsAvgTime) {
        this.weekVisitsAvgTime = weekVisitsAvgTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
