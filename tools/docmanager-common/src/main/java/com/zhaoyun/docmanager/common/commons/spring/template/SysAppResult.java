/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import java.util.Map;

import com.zhaoyun.docmanager.common.commons.spring.template.enums.SysResultEnum;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.StateCode;

/**
 * 一个默认的继承Result的实现类。
 * 
 * <p>1. 增加构造函数，直接设置成功失败</p>
 * <p>2. 增加回滚控制，用于事务处理控制是否进行回滚</p>
 * 
 * @author user
 * @version $Id: DefaultBaseResult.java, v 0.1 2015年9月11日 上午11:08:46 user Exp $
 */
public class SysAppResult extends Result<Object> {

    /**  */
    private static final long  serialVersionUID = 1L;

    /** 成功 */
    public static SysAppResult success          = new SysAppResult(CommonStateCode.SUCCESS);

    /** 失败  */
    public static SysAppResult fail             = new SysAppResult(CommonStateCode.FAILED);

    /** 成功 */
    private SysResultEnum      resultStatus     = SysResultEnum.Y;

    /**
     * 是否回滚标识。true表示回滚；false表示不回滚
     */
    protected boolean          isRollback       = false;

    /**
     * 是否结束业务， true结束业务，返回处理结果，
     */
    protected boolean          endOperation     = false;

    /** 异常处理 */
    private Throwable          e;

    /**
     * 成功
     * @param isSuccess
     * @param data
     */
    public <T> SysAppResult(Result<T> rt) {
        this(rt.getData(), rt.getStateCode(), 
        		rt.getStatusText(), null, rt.getAppMsg(),rt.getExtData());
    }

    /**
     * 成功
     * @param isSuccess
     * @param data
     */
    public SysAppResult(Object data) {
    	if (!(data instanceof Result<?> )){
    		builderbuilder(data, CommonStateCode.SUCCESS, 
        			CommonStateCode.SUCCESS.getDesc(),null,null,null);
    	} else {
    		Result<?> rt = ((Result<?>) data);
    		builderbuilder(rt.getData(), rt.getStateCode(), 
            		rt.getStatusText(), null, rt.getAppMsg(),rt.getExtData());
    	}

    }
    
    /**
     * 成功
     * @param isSuccess
     * @param data
     */
    public SysAppResult(Object data, String appMsg) {
        this(data, CommonStateCode.SUCCESS, CommonStateCode.SUCCESS.getDesc());
    }

    /**
     * 成功
     * @param isSuccess
     * @param data
     */
    public SysAppResult(StateCode stateCode) {
        this(stateCode, stateCode.getDesc());
    }

    /**
     * 成功
     * @param isSuccess
     * @param data
     */
    public SysAppResult(StateCode stateCode, String extInfo) {
        this(null, stateCode, extInfo);
    }

    /**
     * 设置失败 
     * @param data
     * @param stateCode
     * @param extInfo
     */
    public SysAppResult(Object data, StateCode stateCode, String extInfo) {
        this(data, stateCode, extInfo, null,null);
    }
    
    /**
     * 设置失败 
     * @param data
     * @param stateCode
     * @param extInfo
     */
    public SysAppResult(Object data, StateCode stateCode, String extInfo, String appMsg) {
        this(data, stateCode, extInfo, null, appMsg);
    }

    /**
     * 设置失败 
     * @param data
     * @param stateCode
     * @param extInfo
     */
    public SysAppResult(Object data, StateCode stateCode, String extInfo, Throwable e) {
        this(data, stateCode, extInfo, e, null);
    }
    
    /**
     * 设置失败 
     * @param data
     * @param stateCode
     * @param extInfo
     */
    public SysAppResult(Object data, StateCode stateCode, String extInfo, Throwable e, String appMsg) {
        this(data, stateCode, extInfo, e, appMsg, null);
    }


    /**
     * 
     * @param data
     * @param stateCode
     * @param extInfo
     * @param extData
     */
    public SysAppResult(Object data, StateCode stateCode, String extInfo, Throwable e,
    					String appMsg,
                        Map<String, String> extData) {
        super();
        builderbuilder(data, stateCode, extInfo, e, appMsg, extData);
    }
    /**
     * 
     * @param data
     * @param stateCode
     * @param extInfo
     * @param extData
     */
    public void builderbuilder(Object data, StateCode stateCode, String extInfo, Throwable e,
    					String appMsg,
                        Map<String, String> extData) {
        setData(data);
        setStateCode(stateCode);
        setStatusText(extInfo);
        setExtData(extData);
        setAppMsg(appMsg);
        this.e = e;

        // 有异常信息，需要设置异常码
        if (this.e != null && CommonStateCode.SUCCESS.equals(stateCode)) {
            setStateCode(CommonStateCode.FAILED);
        }
        // 设置扩展信息
        if (extInfo == null && getStateCode() != null) {
            setStatusText(getStateCode().getDesc());
        }
        // 异常码小于0时，情况默认需要回滚，不需要回滚需要单独设置isRollback=false，
        if (!super.isSuccess()) {
            if (e != null) {
                this.resultStatus = SysResultEnum.E;
            } else {
                this.resultStatus = SysResultEnum.N;
            }
            this.isRollback = true;
        }

    }

    /**
     * 返回Result对象
     * @return
     * @date: 2015年11月23日 下午9:20:27
     */
    @SuppressWarnings("unchecked")
    public <T> Result<T> toResult() {
        Result<T> r = new Result<T>();
        r.setData((T) getData());
        r.setExtData(getExtData());
        r.setStateCode(getStateCode());
        r.setStatusText(getStatusText());
        r.setAppMsg(getAppMsg());
        return r;
    }

    /**
     * 
     * 是否回滚事务
     * <p>
     *    默认情况下是当业务操作失败就回滚数据库
     *    遇到特殊情况，可以通过子类设置“isRollback”的值来控制（如业务操作失败，但还是需要提交数据库的场景）
     * </p>
     * @return 是否回滚事务。true表示回滚；false表示不回滚
     */
    public boolean isRollbackDb() {
        return (!isSuccess()) && isRollback;
    }

    /**
     * Getter method for property <tt>isRollback</tt>.
     * 
     * @return property value of isRollback
     */
    public boolean isRollback() {
        return isRollback;
    }

    /**
     * Setter method for property <tt>isRollback</tt>.
     * 
     * @param isRollback value to be assigned to property isRollback
     */
    public void setRollback(boolean isRollback) {
        this.isRollback = isRollback;
    }

    /**
     * Getter method for property <tt>endOperation</tt>.
     * 
     * @return property value of endOperation
     */
    public boolean isEndOperation() {
        return endOperation;
    }

    /**
     * Setter method for property <tt>endOperation</tt>.
     * 
     * @param endOperation value to be assigned to property endOperation
     */
    public void setEndOperation(boolean endOperation) {
        this.endOperation = endOperation;
    }

    /**
     * Getter method for property <tt>isSuccess</tt>.
     * 
     * @return property value of isSuccess
     */
    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }

    /**
     * Getter method for property <tt>extInfo</tt>.
     * 
     * @return property value of extInfo
     */
    public String getExtInfo() {
        return getStatusText();
    }

    /**
     * Setter method for property <tt>extInfo</tt>.
     * 
     * @param extInfo value to be assigned to property extInfo
     */
    public void setExtInfo(String extInfo) {
        setStatusText(extInfo);
    }

    /**
     * Getter method for property <tt>resultStatus</tt>.
     * 
     * @return property value of resultStatus
     */
    public SysResultEnum getResultStatus() {
        return resultStatus;
    }

    /**
     * Setter method for property <tt>resultStatus</tt>.
     * 
     * @param resultStatus value to be assigned to property resultStatus
     */
    public void setResultStatus(SysResultEnum resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * Getter method for property <tt>e</tt>.
     * 
     * @return property value of e
     */
    public Throwable getE() {
        return e;
    }

    /**
     * Setter method for property <tt>e</tt>.
     * 
     * @param e value to be assigned to property e
     */
    public void setE(Throwable e) {
        this.e = e;
        this.resultStatus = SysResultEnum.E;
    }

}
