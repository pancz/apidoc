/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.datetime;

import java.util.concurrent.TimeUnit;

import com.google.common.base.MoreObjects;

/**
 * 时间类
 * <p>
 * 	用于方便地处理时间信息
 * </p>
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年2月24日 下午3:13:04 $
 */
public class Time {
	/** 时间单位 */
	private TimeUnit unit;
	/** 时间数量 */
	private long duration;

	private Time(long duration, TimeUnit unit) {
		this.duration = duration;
		this.unit = unit;
	}

	/** 构造时间类, 单位为毫秒 */
	public static Time milliSeconds(long milliSecs) {
		return new Time(milliSecs, TimeUnit.MILLISECONDS); 
	}

	/** 构造Time类, 单位为秒 */
	public static Time seconds(long secs) {
		return new Time(secs, TimeUnit.SECONDS);
	}
	
	/** 构造时间类, 单位为分 */
	public static Time minutes(long mins) {
		return new Time(mins, TimeUnit.MINUTES);
	}
	
	/** 构造时间类, 单位为时 */
	public static Time hours(long hours) {
		return new Time(hours, TimeUnit.HOURS);
	}

	/** 构造时间类, 单位为天 */
	public static Time days(long days) {
		return new Time(days, TimeUnit.DAYS);
	}
	
	/** 转成毫秒 */
	public long toMillis() {
		return getUnit().toMillis(getDuration());
	}

	/** 转成秒 */
	public long toSeconds() {
		return getUnit().toSeconds(getDuration());
	}
	
	/** 转成分钟 */
	public long toMinutes() {
		return getUnit().toMinutes(getDuration());
	}

	/** 获取时间单位 */
	public TimeUnit getUnit() {
		return this.unit;
	}

	/** 获取时长 */
	public long getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				  .add("duration", this.duration)
				  .add("unit", this.unit)
				  .toString();
	}
}
