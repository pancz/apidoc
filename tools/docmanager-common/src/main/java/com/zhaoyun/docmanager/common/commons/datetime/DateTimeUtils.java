/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.datetime;

import org.joda.time.LocalTime;

/**
 * 日期处理工具类
 * 
 * 
 * @author user
 * @version $Id: DateTimeUtils.java, v 1.0.1 2015年12月1日 上午11:43:16 user Exp $
 */
public abstract class DateTimeUtils {
    /**
     * 判断时间范围
     * 
     * @param target 要判断的时间
     * @param begin  开始时间
     * @param end 结束时间
     * @return 如果target时间界于开始和结束时间，则返回true，否则返回false
     */
    public static boolean isBetween(String target, String begin, String end) {
        LocalTime localTarget = LocalTime.parse(target);
        LocalTime localBegin = LocalTime.parse(begin);
        LocalTime localEnd = LocalTime.parse(end);
        
        return isBetween(localTarget, localBegin, localEnd);
    }

    /**
     * 判断时间范围
     * 
     * @param target 要判断的时间
     * @param begin  开始时间
     * @param end 结束时间
     * @return 如果target时间界于开始和结束时间，则返回true，否则返回false
     */
    public static boolean isBetween(LocalTime target, LocalTime begin, LocalTime end) {
        return target.isAfter(begin) && target.isBefore(end);
    }
}
