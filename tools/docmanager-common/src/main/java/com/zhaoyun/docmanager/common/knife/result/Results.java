/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.knife.result;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 结果工具类，方便生成一些公共，常用的结果实例
 * 
 * @author user
 * @version $Id: Results.java, v 0.1 2015年9月10日 下午5:46:27 user Exp $
 */
public abstract class Results {
    /** 成功结果 */
    public static <T> Result<T> newSuccessResult(T data) {
        return newSuccessResult(data, CommonStateCode.SUCCESS.getDesc());
    }

    /** 成功结果 */
    public static <T> Result<T> newSuccessResult(T data, String statusText) {
        return newResult(data, CommonStateCode.SUCCESS, statusText);
    }
    
    /** 成功结果 */
    public static <T> Result<T> newSuccessResult(T data, String statusText, String appMsg) {
        return newResult(data, CommonStateCode.SUCCESS, statusText, appMsg);
    }

    /** 没有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(StateCode failedCode) {
        return newFailedResult(null, failedCode);
    }

    /** 没有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(StateCode failedCode, String statusText) {
        return newFailedResult(null, failedCode, statusText);
    }
    
    /** 没有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(StateCode failedCode, String statusText, String appMsg) {
        return newFailedResult(null, failedCode, statusText, appMsg);
    }

    /** 有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(T data, StateCode failedCode) {
        return newFailedResult(data, failedCode, failedCode.getDesc());
    }

    /** 有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(T data, StateCode failedCode, String statusText) {
        return newFailedResult(data, failedCode, statusText, null);
    }
    
    /** 有数据回传的失败结果 */
    public static <T> Result<T> newFailedResult(T data, StateCode failedCode, String statusText, String appMsg) {
        checkNotNull(failedCode);
        checkArgument(failedCode.getCode() < 0,
            "invalid code, for failed result, code must be negative integers");

        return newResult(data, failedCode, statusText, appMsg);
    }
    
    /** 仅返回状态码*/
    public static <T> Result<T> newResult(StateCode code) {
        return newResult(null, code, code.getDesc());
    }

    /** 有数据回传的结果 */
    public static <T> Result<T> newResult(T data, StateCode failedCode, String statusText) {
        return newResult(data, failedCode, statusText, null);
    }
    
    /** 有数据回传的结果 */
    public static <T> Result<T> newResult(T data, StateCode failedCode, String statusText, String appMsg) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setStateCode(failedCode);
        result.setStatusText(statusText);
        result.setAppMsg(appMsg);
        return result;
    }
}
