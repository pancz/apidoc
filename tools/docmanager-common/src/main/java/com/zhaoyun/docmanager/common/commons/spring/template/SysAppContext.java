/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import org.springframework.transaction.support.TransactionTemplate;

import com.zhaoyun.docmanager.common.commons.spring.template.enums.ActionEnumInterface;
import com.zhaoyun.docmanager.common.commons.spring.template.enums.LoggerEnumInterface;
import com.zhaoyun.docmanager.common.commons.spring.template.enums.SysHandlerType;

/**
 * 
 * 系统应用上下文
 * 
 * <p>
 *      在使用系统模板<code>SysAppTemplate</code>时需要传入系统应用上下文<code>SysAppContext</code><br>
 *      以便提供给模板相应的处理信息，按业务逻辑指定的需求进行处理。<br>
 *      目前，系统应用上下文<code>SysAppContext</code>可控制的信息有：<br>
 *      <ol>
 *          <li>操作类型，也就是业务用例名称，用于日志的主体输出</li>
 *          <li>处理类型，控制模板中是否使用事务控制或采用哪个事务模板进行控制</li>
 *          <li>需要通过日志打印的入参，<b>注意：对象一定要重写<code>toString</code>方法</b></li>
 *          <li>是否打印监控的日志</li>
 *          <li>Logger对象，以便控制日志输出文件</li>
 *      </ol>
 * </p>
 * 
 * @author user
 * @version $Id: SysAppTemplateImpl.java, v 0.1 2015年9月10日 下午5:26:44 user Exp $
 */
public class SysAppContext {

    /** 操作类型：必需设置 */
    private ActionEnumInterface  actionEnum;

    /** 处理类型：设置业务处理时需要的事物模板。默认是无事务控制的处理类型 */
    private SysHandlerType       handlerType   = SysHandlerType.NONE;

    /** 用于输出日志的入参 */
    private Object[]             obj;

    /** 日志对象，摘要日志输出 */
    private LoggerEnumInterface  digestLogger;

    /** 日志对象，业务日志输出 */
    private LoggerEnumInterface  nomalLogger;

    /** 摘要日志标记信息   不允许包含 ， */
    private String               digestTag     = null;

    /** 是否打印业务调用前的监控日志，默认是true */
    private boolean              isPrintPreLog = true;

    /**  是否加锁  ，启用加锁后，需要实现lockKey返回*/
    private boolean              isLock        = false;

    /**  是否进行幂等校验， 启用幂等校验后，默认启动加锁  */
    private boolean              isIdempotence = false;

    /** 需要解锁 */
    private boolean              needUnLock    = false;

    /** 事务模板 */
    private TransactionTemplate  transactionTemplate;

    /** context token，用于日志标示*/
    private String               token         = null;

    /**  默认加锁接口  ，需要引用*/
    private DefaultLockInterface lockInterface = null;

    public SysAppContext() {
        super();
    }

    /**
     * 非事务操作
     * @param actionEnum  操作枚举
     * @param digestTag   摘要日志标记信息
     * @param obj         业务日志信息（多object，采用toString（）相接模式）
     */
    public SysAppContext(ActionEnumInterface actionEnum, String digestTag, Object... obj) {
        this(actionEnum, digestTag, null, null, null, obj);
    }

    /**
     *  可设置事务
     * @param actionEnum  操作枚举
     * @param digestTag   摘要日志标记信息
     * @param obj         业务日志信息（多object，采用toString（）相接模式）
     * @param transactionTemplate 是否使用事务   是否使用事务 ,为null是不使用事务
     */
    public SysAppContext(ActionEnumInterface actionEnum, String digestTag,
                         TransactionTemplate transactionTemplate, Object... obj) {
        this(actionEnum, digestTag, null, null, transactionTemplate, obj);
    }

    /**
     * 设置上下文 
     * @param actionEnum    操作枚举
     * @param digestTag     摘要日志标记信息
     * @param digestLogger  摘要日志logger 
     * @param nomalLogger   业务日志logger
     * @param transactionTemplate   是否使用事务 ,为null是不使用事务
     * @param obj           业务日志信息（多object，采用toString（）相接模式）
     */
    public SysAppContext(ActionEnumInterface actionEnum, String digestTag,
                         LoggerEnumInterface digestLogger, LoggerEnumInterface nomalLogger,
                         TransactionTemplate transactionTemplate, Object... obj) {
        this.actionEnum = actionEnum;
        this.transactionTemplate = transactionTemplate;
        if (transactionTemplate != null) {
            this.handlerType = SysHandlerType.TRAN;
        }
        this.digestLogger = digestLogger;
        this.nomalLogger = nomalLogger;
        this.digestTag = digestTag;
        this.obj = obj;
        this.token = this.actionEnum.getActionCode() + this.digestTag + System.currentTimeMillis();
    }

    /**
     * Getter method for property <tt>digestTag</tt>.
     * 
     * @return property value of digestTag
     */
    public String getDigestTag() {
        return digestTag;
    }

    /**
     * Setter method for property <tt>digestTag</tt>.
     * 
     * @param digestTag value to be assigned to property digestTag
     */
    public void setDigestTag(String digestTag) {
        this.digestTag = digestTag;
    }

    /**
     * Getter method for property <tt>actionEnum</tt>.
     * 
     * @return property value of actionEnum
     */
    public ActionEnumInterface getActionEnum() {
        return actionEnum;
    }

    /**
     * Getter method for property <tt>obj</tt>.
     * 
     * @return property value of obj
     */
    public Object[] getObj() {
        return obj;
    }

    /**
     * Getter method for property <tt>handlerType</tt>.
     * 
     * @return property value of handlerType
     */
    public SysHandlerType getHandlerType() {
        return handlerType;
    }

    /**
     * Setter method for property <tt>handlerType</tt>.
     * 
     * @param handlerType value to be assigned to property handlerType
     */
    public void setHandlerType(SysHandlerType handlerType) {
        this.handlerType = handlerType;
    }

    /**
     * Getter method for property <tt>isPrintPreLog</tt>.
     * 
     * @return property value of isPrintPreLog
     */
    public boolean isPrintPreLog() {
        return isPrintPreLog;
    }

    /**
     * Setter method for property <tt>isPrintPreLog</tt>.
     * 
     * @param isPrintPreLog value to be assigned to property isPrintPreLog
     */
    public void setPrintPreLog(boolean isPrintPreLog) {
        this.isPrintPreLog = isPrintPreLog;
    }

    /**
     * Getter method for property <tt>isLock</tt>.
     * 
     * @return property value of isLock
     */
    public boolean isLock() {
        return isLock;
    }

    /**
     * Setter method for property <tt>isLock</tt>.
     * 
     * @param isLock value to be assigned to property isLock
     */
    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    /**
     * Getter method for property <tt>isIdempotence</tt>.
     * 
     * @return property value of isIdempotence
     */
    public boolean isIdempotence() {
        return isIdempotence;
    }

    public boolean isNeedUnLock() {
        return needUnLock;
    }

    public void setNeedUnLock(boolean needUnLock) {
        this.needUnLock = needUnLock;
    }

    /**
     * Setter method for property <tt>isIdempotence</tt>.
     * 
     * @param isIdempotence value to be assigned to property isIdempotence
     */
    public void setIdempotence(boolean isIdempotence) {
        this.isIdempotence = isIdempotence;
        if (isIdempotence) {
            this.isLock = true;
        }
    }

    /**
     * Setter method for property <tt>actionEnum</tt>.
     * 
     * @param actionEnum value to be assigned to property actionEnum
     */
    public void setActionEnum(ActionEnumInterface actionEnum) {
        this.actionEnum = actionEnum;
    }

    /**
     * Getter method for property <tt>digestLogger</tt>.
     * 
     * @return property value of digestLogger
     */
    public LoggerEnumInterface getDigestLogger() {
        return digestLogger;
    }

    /**
     * Setter method for property <tt>digestLogger</tt>.
     * 
     * @param digestLogger value to be assigned to property digestLogger
     */
    public void setDigestLogger(LoggerEnumInterface digestLogger) {
        this.digestLogger = digestLogger;
    }

    /**
     * Getter method for property <tt>nomalLogger</tt>.
     * 
     * @return property value of nomalLogger
     */
    public LoggerEnumInterface getNomalLogger() {
        return nomalLogger;
    }

    /**
     * Setter method for property <tt>nomalLogger</tt>.
     * 
     * @param nomalLogger value to be assigned to property nomalLogger
     */
    public void setNomalLogger(LoggerEnumInterface nomalLogger) {
        this.nomalLogger = nomalLogger;
    }

    /**
     * Getter method for property <tt>transactionTemplate</tt>.
     * 
     * @return property value of transactionTemplate
     */
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    /**
     * Setter method for property <tt>transactionTemplate</tt>.
     * 
     * @param transactionTemplate value to be assigned to property transactionTemplate
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * Getter method for property <tt>lockInterface</tt>.
     * 
     * @return property value of lockInterface
     */
    public DefaultLockInterface getLockInterface() {
        return lockInterface;
    }

    /**
     * Setter method for property <tt>lockInterface</tt>.
     * 
     * @param lockInterface value to be assigned to property lockInterface
     */
    public void setLockInterface(DefaultLockInterface lockInterface) {
        this.lockInterface = lockInterface;
    }

    /**
     * Getter method for property <tt>token</tt>.
     * 
     * @return property value of token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter method for property <tt>token</tt>.
     * 
     * @param token value to be assigned to property token
     */
    public void setToken(String token) {
        this.token = token;
    }

}
