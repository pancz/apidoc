/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.biz.exception;

import com.zhaoyun.docmanager.common.knife.result.StateCode;
import com.zhaoyun.docmanager.core.exception.BizException;

/**
 * Doc Exception
 *
 * @author user
 * @version : Doc-parent, v 0.1 2016/7/7 10:53 user Exp $$
 */
public class DocException extends BizException {

    private static final long serialVersionUID = 1L;

    private StateCode stateCode;
    private boolean error = true;

    /**
     *
     * @param stateCode
     * @param msg
     * @param e
     */
    public DocException(StateCode stateCode, String msg,
                             Exception e) {
        super(msg, e);
        this.stateCode = stateCode;
    }

    public DocException(StateCode stateCode, String msg) {
        this(stateCode, msg, null);
        error = false;
    }

    public DocException(boolean error, StateCode stateCode, String msg) {
        this(stateCode, msg, null);
        this.error = error;
    }

    public StateCode getStateCode() {
        return this.stateCode;
    }
    public boolean isError() {
        return error;
    }
}
