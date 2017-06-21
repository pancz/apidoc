/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import com.zhaoyun.docmanager.common.commons.spring.template.enums.ActionEnumInterface;
import com.zhaoyun.docmanager.common.commons.spring.template.enums.LoggerEnumInterface;
import com.zhaoyun.docmanager.common.commons.spring.template.enums.SysHandlerType;

/**
 * tranContext 工厂
 * @author user
 * @version $Id: SysContextForcty.java, v 0.1 2015年11月27日 上午11:07:08 user Exp $
 */
public class SysAppFactory {

    /** 事务模板 **/
    private TransactionTemplate  transactionTemplate;
    
    private String 				 sysName;

    /** 摘要日志 **/
    private String               digestLoggerName;

    /** 详情日志   */
    private String               detailLoggerName;

    /** sysAppTemplate 处理模板 */
    private SysAppTemplate       sysAppTemplate = new SysAppTemplateImpl();

    /** 摘要日志*/
    private LoggerEnumInterface  digestLogger;

    /** 详细日志 */
    private LoggerEnumInterface  detailLogger;

    /** 加锁处理类 */
    private DefaultLockInterface lockHandler;

    /**
     * 创建SysAppContext对象，
     * @param actionEnum
     * @param digestTag
     * @param transactionTemplate
     * @param obj
     * @return
     * @date: 2015年11月27日 上午11:11:42
     */
    public SysAppContext tranContext(ActionEnumInterface actionEnum, String digestTag,
                                     Object... obj) {
        return new SysAppContext(actionEnum, digestTag, getDigestLogger(), getDetailLogger(),
            transactionTemplate, obj);
    }

    /**
     * 创建对象 不包含事务
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @return
     * @date: 2015年11月27日 上午11:12:08
     */
    public SysAppContext comContext(ActionEnumInterface actionEnum, String digestTag, Object... obj) {
        return new SysAppContext(actionEnum, digestTag, getDigestLogger(), getDetailLogger(), null,
            obj);
    }

    /**
     * 创建对象  包含加锁
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @return
     * @date: 2015年11月27日 上午11:12:08
     */
    public SysAppContext lockContext(ActionEnumInterface actionEnum, String digestTag,
                                     boolean istran, Object... obj) {
        SysAppContext context = new SysAppContext(actionEnum, digestTag, getDigestLogger(),
            getDetailLogger(), null, obj);
        context.setLock(true);
        context.setLockInterface(lockHandler);
        if (istran) {
            context.setTransactionTemplate(transactionTemplate);
            context.setHandlerType(SysHandlerType.TRAN);
        }
        return context;
    }

    /**
     * 创建对象， 包含加锁 幂等校验
     * @param actionEnum
     * @param digestTag
     * @param istran
     * @param obj
     * @return
     * @date: 2015年11月27日 上午11:38:10
     */
    public SysAppContext idempContext(ActionEnumInterface actionEnum, String digestTag,
                                      boolean istran, Object... obj) {
        SysAppContext context = new SysAppContext(actionEnum, digestTag, getDigestLogger(),
            getDetailLogger(), null, obj);
        context.setLock(true);
        context.setLockInterface(lockHandler);
        if (istran) {
            context.setTransactionTemplate(transactionTemplate);
            context.setHandlerType(SysHandlerType.TRAN);
        }
        return context;
    }

    /**
     * 事务模板
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @param callback
     * @return
     * @date: 2015年11月27日 上午11:48:23
     */
    public SysAppResult tranTemplate(ActionEnumInterface actionEnum, String digestTag,
                                     Object[] obj, SysAppBaseCallBack callback) {
        SysAppContext tranContext = tranContext(actionEnum, digestTag, obj);
        return sysAppTemplate.execute(tranContext, callback);
    }

    /**
     * 普通模板
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @param callback
     * @return
     * @date: 2015年11月27日 上午11:48:23
     */
    public SysAppResult comTemplate(ActionEnumInterface actionEnum, String digestTag, Object[] obj,
                                    SysAppBaseCallBack callback) {
        SysAppContext tranContext = comContext(actionEnum, digestTag, obj);
        return sysAppTemplate.execute(tranContext, callback);
    }

    /**
     * 加锁模板
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @param callback
     * @return
     * @date: 2015年11月27日 上午11:48:23
     */
    public SysAppResult lockTemplate(ActionEnumInterface actionEnum, String digestTag,
                                     boolean istran, Object[] obj, SysAppBaseCallBack callback) {
        SysAppContext tranContext = lockContext(actionEnum, digestTag, istran, obj);
        return sysAppTemplate.execute(tranContext, callback);
    }

    /**
     * 加锁模板  包含加锁 幂等校验
     * @param actionEnum
     * @param digestTag
     * @param obj
     * @param callback
     * @return
     * @date: 2015年11月27日 上午11:48:23
     */
    public SysAppResult idempTemplate(ActionEnumInterface actionEnum, String digestTag,
                                      boolean istran, Object[] obj, SysAppBaseCallBack callback) {
        SysAppContext tranContext = idempContext(actionEnum, digestTag, istran, obj);
        return sysAppTemplate.execute(tranContext, callback);
    }

    /**
     * 构造摘要日志对象
     * @return
     * @date: 2015年12月20日 下午12:22:22
     */
    public LoggerEnumInterface getDigestLogger() {
        if (digestLogger == null && !StringUtils.isEmpty(digestLoggerName)) {
            digestLogger = new LoggerEnumInterface() {

                @Override
                public String getMessage() {
                    return digestLoggerName;
                }

                @Override
                public Logger getLogger() {
                    return LoggerFactory.getLogger(digestLoggerName);
                }

                @Override
                public String getCode() {
                    return digestLoggerName;
                }
            };
        }
        return digestLogger;
    }

    /**
     * 构造详情日志对象
     * @return
     * @date: 2015年12月20日 下午12:22:22
     */
    public LoggerEnumInterface getDetailLogger() {
        if (detailLogger == null && !StringUtils.isEmpty(detailLoggerName)) {
            detailLogger = new LoggerEnumInterface() {
                @Override
                public String getMessage() {
                    return detailLoggerName;
                }

                @Override
                public Logger getLogger() {
                    return LoggerFactory.getLogger(detailLoggerName);
                }

                @Override
                public String getCode() {
                    return detailLoggerName;
                }
            };
        }
        return detailLogger;
    }

    /**
     * 注入摘要日志logger
     * @param digestLoggerName
     * @date: 2015年12月20日 下午12:37:59
     */
    public void setDigestLoggerName(String digestLoggerName) {
        this.digestLoggerName = digestLoggerName;
    }

    /**
     * 注入详情日志logger
     * @param detailLoggerName
     * @date: 2015年12月20日 下午12:38:02
     */
    public void setDetailLoggerName(String detailLoggerName) {
        this.detailLoggerName = detailLoggerName;
    }

    /**
     * 设置事务模板
     * @param transactionTemplate
     * @date: 2015年12月20日 下午12:40:39
     */
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 设置加锁处理
     * @param lockHandler
     * @date: 2015年12月24日 下午4:26:25
     */
    public void setLockHandler(DefaultLockInterface lockHandler) {
        this.lockHandler = lockHandler;
    }

    /**
     * 返回系统名
     * @return
     */
    public String getSysName(){
    	return this.sysName;
    }
}
