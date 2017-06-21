/**
 * com Inc
 * Copyright (c) 2014-2016年4月20日 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.exception;

import com.zhaoyun.docmanager.common.knife.result.StateCode;

/**
 * API异常类
 *
 * @author user
 * @since Revision:1.0.0, Date: 2016年4月20日 下午5:53:39
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private StateCode stateCode;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message, StateCode stateCode) {
        super(message);
        this.stateCode = stateCode;
    }

    public StateCode getStateCode() {
        return stateCode;
    }

}
