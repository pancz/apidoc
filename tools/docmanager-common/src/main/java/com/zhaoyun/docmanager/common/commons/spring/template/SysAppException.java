/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import com.zhaoyun.docmanager.common.knife.result.StateCode;

/**
 *
 * 系统应用异常。
 * 
 * <p>
 *      在使用系统应用模板的业务逻辑中，当遇到业务校验不通过或捕获到业务系统，可以采用2种方式来处理。第一个就是返回失败的错误结果；
 *      第二个就是抛出系统应用异常SysAppException，
 * </p>
 *
 * @author user
 * @version $Id: SysAppException.java, v 0.1 2015年9月10日 下午5:26:44 user Exp $
 */
public class SysAppException extends RuntimeException {

    private static final long serialVersionUID = -3708111590848157964L;

    /** 
     * 错误码
     */
    private StateCode         stateCode;

    /**
     * 扩展描述
     */
    private String            extDescribe;

    /**
     * 系统应用异常构造器
     * @param stateCode    
     *              业务处理结果枚举
     */
    public SysAppException(StateCode stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * 
     * @param stateCode
     * @param extDescribe
     */
    public SysAppException(StateCode stateCode, String extDescribe) {
        this.stateCode = stateCode;
        this.extDescribe = extDescribe;
    }

    /**
     * 系统应用异常构造器
     * @param stateCode
     *                  业务处理结果枚举
     * @param t
     *                  抛出的异常
     */
    public SysAppException(StateCode stateCode, Throwable t) {
        super(t);
        this.stateCode = stateCode;
    }

    /**
     * 扩展描述
     * @param stateCode
     * @param t
     * @param extDescribe
     */
    public SysAppException(StateCode stateCode, Throwable t, String extDescribe) {
        super(t);
        this.stateCode = stateCode;
        this.extDescribe = extDescribe;
    }

    /**
     * 获取业务处理结果枚举
     * @return  业务处理结果枚举
     */
    public StateCode getStateCode() {
        return stateCode;
    }

    /**
     * 获取扩展描述
     * @return property value of extDescribe
     */
    public String getExtDescribe() {
        return extDescribe;
    }

}
