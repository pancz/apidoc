/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.knife.result;

import java.io.Serializable;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.zhaoyun.docmanager.common.commons.base.Source;

/**
 * 返回结果类
 * <p>常用于dubbo服务接口的返回值的封装</p>
 * 
 * @author user
 * @version $Id: Result.java, v 0.1 2015年9月10日 下午5:19:15 user Exp $
 */
public class Result<T> implements Serializable {
	private static final long serialVersionUID = -3887725517645851694L;

	/* 结果数据 */
	private T data;
	/* 状态码 */
	private StateCode stateCode;
	/* 状态描述辅助信息 */
	private String statusText;
	/* 扩展信息 */
	private Map<String, String> extData;
	/* 源系统 */
	private Source source;
	/* 用于app文案透出 */
	private String appMsg;

	public Result() {}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	//是否处理成功, 意味着没有业务失败和系统失败
	public boolean isSuccess() {
		return stateCode.getCode() >= 0;
	}
	
	//是否处理失败，包括业务异常和系统异常
	public boolean isFailed() {
		return !isSuccess();
	}

	//是否业务异常
	public boolean isBizFailed() {
		return isFailed() && !isSystemError();
	}
		
	//是否系统异常
	public boolean isSystemError() {
		int code = getStateCode().getCode();
		return code <= -900 && code > -1000;
	}

	public StateCode getStateCode() {
		return stateCode;
	}

	public void setStateCode(StateCode stateCode) {
		this.stateCode = stateCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public Map<String, String> getExtData() {
		return extData;
	}

	public void setExtData(Map<String, String> extData) {
		this.extData = extData;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	
	public String getAppMsg() {
		return appMsg;
	}

	public void setAppMsg(String appMsg) {
		this.appMsg = appMsg;
	}

	@Override
	public String toString() {
		ToStringHelper toString =  MoreObjects.toStringHelper(this);
		if(source != null) {
			toString.add("source", source);
		}
		
		return
			toString.add("data", this.data)
					.add("stateCode", this.stateCode)
					.add("statusText", this.statusText)
					.add("extData", this.extData)
					.add("appMsg", this.appMsg)
					.omitNullValues()
					.toString();
	}
}