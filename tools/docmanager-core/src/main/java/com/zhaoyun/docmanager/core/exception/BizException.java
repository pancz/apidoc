package com.zhaoyun.docmanager.core.exception;

import com.zhaoyun.docmanager.common.knife.result.StateCode;

/**
 * 业务异常
 *
 * @author user
 * @date: 2015年10月27日 下午3:20:48
 */
public class BizException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = -6600217455708324604L;

    private StateCode stateCode;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message, StateCode stateCode) {
        super(message);
        this.stateCode = stateCode;
    }

    public StateCode getStateCode() {
        return stateCode;
    }

    /**
     * 业务异常无需输出异常栈轨迹，故重写该方法
     *
     * @see Throwable#fillInStackTrace()
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
