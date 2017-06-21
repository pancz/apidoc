/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年2月4日 上午10:38:13 $
 */
public class DateUtils {
    
    /**默认日期格式*/
    public static final String DEFUALT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    public static final String LONG_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    
    /**
     * 把"yyyy-MM-dd HH:mm:ss"格式的字符串时间，转换成Date类型
     * 
     * @param timeStr 字符串时间，格式为：yyyy-MM-dd HH:mm:ss
     * @return 转换成的时间
     * @throws ParseException 格式错误 
     */
    public static Date parseDate(final String timeStr) throws ParseException {
        return new SimpleDateFormat(DEFUALT_DATE_TIME_PATTERN).parse(timeStr);
    }
    
    /**
     * 把Date类型的时间，转换成指定格式的字符串
     * @param date 需要转换的时间
     * @return 指定格式的字符串
     * @throws ParseException
     */
    public static String formateDate(final Date date) throws ParseException{
        return new SimpleDateFormat(DEFUALT_DATE_TIME_PATTERN).format(date);
    }
    
    /**
     * 把Date类型的时间，转换成指定格式的字符串
     * @param date 需要转换的时间
     * @return 指定格式的字符串
     * @throws ParseException
     */
    public static String formateDate(final Date date, final String dateTimePattern) throws ParseException{
        return new SimpleDateFormat(dateTimePattern).format(date);
    }
    
    /**
     * 把Long类型的时间，转换成指定格式的字符串
     * @param time 1970-01-01 00:00:00到现在的毫秒数
     * @return yyyy-MM-dd HH:mm:ss.SSS格式的时间字符串
     * @throws ParseException
     */
    public static String formateLongDate(final Long time) throws ParseException{
        return new SimpleDateFormat(LONG_DATE_TIME_PATTERN).format(new Date(time));
    }
    
    /**
     * 添加月份到时间中
     * @param date			原时间	
     * @param amount		添加的月数，可以为负数
     * @return				添加后的结果
     * @throws Exception
     */
    public static Date addMonths(Date date, int amount) throws Exception {  
        Calendar localCalendar = Calendar.getInstance();  
        localCalendar.setTime(date);  
        localCalendar.add(Calendar.MONTH, amount);
        return localCalendar.getTime();  
    }
    
    /**
     * 给指定的时间添加指定的小时
     * @param date			原时间
     * @param amount		添加的小时数，可以为负数
     * @return				添加后的结果
     * @throws Exception
     */
    public static Date addHours(Date date, int amount) throws Exception {  
        Calendar localCalendar = Calendar.getInstance();  
        localCalendar.setTime(date);  
        localCalendar.add(Calendar.HOUR, amount);
        return localCalendar.getTime();  
    }
    
    /**
     * 把"HH:mm:ss"格式的字符串时间，转换成Date类型
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public static Date parseDateForTime(final String timeStr) throws ParseException{
        return new SimpleDateFormat(DEFAULT_TIME_PATTERN).parse(timeStr);
    }
    
}
