/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.zhaoyun.docmanager.common.commons.spring.template.enums.SysHandlerType;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;

/**
 * 系统应用模板实现。
 * 
 * <p>
 *      通过使用系统应用模板，可以使开发人员只关注自己的业务逻辑实现，这里系统应用模板提供如下几个功能(类似于AOP)<br>
 *      <ol>
 *      <li>业务调用日志输出的控制</li>
 *      <li>用于系统监控的digest日志输出</li>
 *      <li>加锁控制</li>
 *      <li>幂等性控制</li>
 *      <li>事务控制</li>
 *      <li>异常控制，对于系统抛出的异常、业务抛出的异常、业务返回的失败结果，都可以进行异常控制，以及对异常日志的输出。</li>
 *      </ol>
 *      <br>
 *      <b>模板使用方式：</b>
 *      <pre>  
 *           // 构建系统应用上下文
 *           SysAppContext context = new SysAppContext(ActionEnum.DEFAULT_OPERATOR,obj,logger)
 *           sysAppTemplate.execute(context, new SysAppCallback() {
 *                  public BaseResult callback() {
 *                   // 业务逻辑代码
 *                  }
 *           }
 *      </pre>     
 * </p>
 * 
 * @author user
 * @version $Id: SysAppTemplateImpl.java, v 0.1 2015年9月10日 下午5:26:44 user Exp $
 */
@Service("sysAppTemplate")
public class SysAppTemplateImpl implements SysAppTemplate {

    /** 默认业务日志 */
    private static Logger nomalLogger  = LoggerFactory.getLogger(SysAppTemplateImpl.class);

    /** 默认摘要日志 */
    private static Logger digestLogger = LoggerFactory.getLogger(SysAppTemplateImpl.class);

    /**
     * 
     * @see SysAppTemplate#execute(SysAppContext, SysAppCallback)
     */
    @Override
    public SysAppResult execute(final SysAppContext context, SysAppCallback callback) {

        long startTime = System.currentTimeMillis();

        SysAppResult baseResult = null;
        try {
            // #1 参数校验
            baseResult = checkContext(context, callback);
            if (baseResult != null) {
                return baseResult;
            }

            // #2 事前日志输出
            //            nomalLogger(context, baseResult);

            // #3 校验参数
            baseResult = callback.checkHandler();
            if (baseResult != null && (!baseResult.isSuccess() || baseResult.isEndOperation())) {
                return baseResult;
            }

            // #4 校验参数
            baseResult = doHandler(context, callback);

            if (baseResult == null) {
                baseResult = new SysAppResult(null, CommonStateCode.FAILED, "Result 返回不能为null");
            }

        } catch (SysAppException e) {
            baseResult = new SysAppResult(null, e.getStateCode(), e.getExtDescribe(), e);
        } catch (Throwable e) {
            baseResult = new SysAppResult(null, CommonStateCode.INNER_SERVER_ERROR, e.getMessage(),
                e);
        } finally {
            try {
                long elapse = System.currentTimeMillis() - startTime;
                nomalLogger(context, baseResult);
                digestLogger(context, elapse, baseResult);
            } catch (Exception e) {
                getNomalLogger(context).error("记录流水日志时出现异常", e);
            }

            // #5 事后事件，处理结果不影响业务处理，有异常，打印警告
            SysAppResult afterBiz = callback.afterBiz();
            if (afterBiz != null) {
                nomalLoggerWarn(context, afterBiz);
            }
        }

        return baseResult;
    }

    /**
     * 上下文校验
     * @param context
     * @param callback 
     * @return
     * @date: 2015年11月19日 下午3:56:27
     */
    private SysAppResult checkContext(SysAppContext context, SysAppCallback callback) {
        // #1 如果系统应用上下文为空，直接返回失败结果
        if (context == null) {
            return new SysAppResult(CommonStateCode.ILLEGAL_PARAMETER, "context参数缺失");
        }
        // 
        if (callback == null) {
            return new SysAppResult(CommonStateCode.ILLEGAL_PARAMETER, "callback参数缺失");
        }

        // #2 校验参数
        if (context.getActionEnum() == null) {
            return new SysAppResult(CommonStateCode.ILLEGAL_PARAMETER, "操作枚举方法缺失");
        }

        if (context.getHandlerType() == SysHandlerType.TRAN
            && context.getTransactionTemplate() == null) {
            return new SysAppResult(CommonStateCode.ILLEGAL_PARAMETER, "事务模板缺失");
        }

        if (context.getHandlerType() != SysHandlerType.NONE
            && context.getHandlerType() != SysHandlerType.TRAN) {
            return new SysAppResult(null, CommonStateCode.ILLEGAL_PARAMETER, "HandlerType类型异常");
        }

        return null;
    }

    /**
     * 业务处理，包含事务和非事务模式
     * 事务模式，包含加锁、幂等、业务处理模块
     * 
     * @param context
     * @param callback
     * @return
     * @date: 2015年11月23日 下午6:31:53
     */
    private SysAppResult doHandler(SysAppContext context, SysAppCallback callback) {
        SysAppResult baseResult = null;
        // #3 业务处理
        switch (context.getHandlerType()) {
            case NONE: // 普通模式
                baseResult = handler(context, callback);
                break;
            case TRAN: // 模式
                baseResult = tradeTransaction(context, callback);
                break;
            default:
                break;
        }
        return baseResult;
    }

    /**
     * 使用事务模板进行业务。
     *      异常情况、result设置isRollbackDb=true时，事务需要回滚
     * @param callback
     * @param context
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:41:38
     */
    private SysAppResult tradeTransaction(final SysAppContext context, final SysAppCallback callback) {

        return context.getTransactionTemplate().execute(new TransactionCallback<SysAppResult>() {
            @Override
            public SysAppResult doInTransaction(TransactionStatus status) {
                SysAppResult baseResult = null;
                try {
                    baseResult = handler(context, callback);
                } catch (SysAppException e) {
                    baseResult = new SysAppResult(null, e.getStateCode(), e.getExtDescribe(), e);
                } catch (Throwable e) {
                    baseResult = new SysAppResult(null, CommonStateCode.INNER_SERVER_ERROR, e
                        .getMessage(), e);
                }

                if (baseResult != null && baseResult.isRollbackDb()) {
                    status.setRollbackOnly();
                }
                return baseResult;
            }
        });
    }

    /**
     * 业务处理 
     * @param context
     * @param callback
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:47:56
     */
    private SysAppResult handler(final SysAppContext context, SysAppCallback callback) {

        StringBuffer buf = new StringBuffer(context.getObj().toString());
        SysAppResult baseResult = null;
        String lockKey = null;
        try {
            // #1 需要加锁时设置lockkey， 使用默认加锁子类实现lockKey()方法返回key；使用自定义锁lockKey可以不重新
            if (context.isLock()) {
                // 设置lockKey
                lockKey = callback.lockKey().toUpperCase();
                lockKey = context.getActionEnum().getBizCode() + lockKey;
            }
            // #2 加锁，需要加锁的业务，设置context加锁， 默认为zookeeper加锁，可以重写lock() unlock() 方法
            if (context.isLock()) {
                // 设置lockKey
                buf.append("；加锁");
                baseResult = callback.lock(context, lockKey);
                if (baseResult != null && (!baseResult.isSuccess() || baseResult.isEndOperation())) {
                    buf.append("失败 ");
                    return baseResult;
                }
                context.setNeedUnLock(true);
                buf.append("成功 ");
            }

            // #3  幂等性校验
            buf.append("；幂等性校验");
            baseResult = callback.checkIdempotence();
            if (baseResult != null && (!baseResult.isSuccess() || baseResult.isEndOperation())) {
                return baseResult;
            }

            // #4 处理事务回调
            buf.append("；业务操作执行】");
            baseResult = callback.doBiz();

        } finally {
            // #5 释放锁 释放锁失败，需要设置--数据回滚，
            if (context.isLock() && context.isNeedUnLock()) {
                SysAppResult unlock = callback.unLock(context, lockKey);
                if (unlock != null && (!unlock.isSuccess() || unlock.isEndOperation())) {
                    // 需要回滚，直接返回
                    if (unlock.isRollback) {
                        return unlock;
                    }
                    // 不需要回滚，打印warn日志
                    nomalLoggerWarn(context, unlock);
                }
            }
            // 测试日志输出
            if (getNomalLogger(context).isDebugEnabled()) {
                getNomalLogger(context).debug(buf.toString());
            }
        }
        return baseResult;
    }

    /**
     * 摘要日志输出
     * @param context
     * @param elapse
     * @param baseResult
     * @date: 2015年11月23日 下午5:53:09
     */
    private void digestLogger(SysAppContext context, long elapse, SysAppResult baseResult) {
        getDigestLogger(context).info(SysLogFormatUtil.digestLog(context, elapse, baseResult));
    }

    /**
     * 
     * @param context
     * @param baseResult
     * @date: 2015年11月19日 下午6:31:37
     */
    private void nomalLogger(SysAppContext context, SysAppResult baseResult) {
        String logStr = null;
        if (getNomalLogger(context).isDebugEnabled()) {
            logStr = SysLogFormatUtil.format(context, baseResult,  true, context.getObj());
        } else {
            logStr = SysLogFormatUtil.format(context, baseResult,  false, context.getObj());
        }

        Throwable e = baseResult.getE();
        if (e != null) {
            if (e instanceof SysAppException) {
                // 对于SysAppException输出warn日志
                getNomalLogger(context).warn(logStr, e);
            } else {
                getNomalLogger(context).error(logStr, e);
            }
        } else if (baseResult.isRollbackDb()) {
            // 需要回滚事务时，记录警告日志 
            getNomalLogger(context).warn(logStr);

        } else if (baseResult.isFailed()) {
            // 执行结果异常
            getNomalLogger(context).warn(logStr);

        } else if (context.isPrintPreLog() && getNomalLogger(context).isInfoEnabled()) {
            getNomalLogger(context).info(logStr);
        }
    }

    /**
     * 结果处理。
     *      如果业务失败，主要进行警告日志输出
     * @param baseResult
     * @param context
     * @author: user
     * @date: 2015年9月14日 下午2:46:15
     */
    private void nomalLoggerWarn(SysAppContext context, SysAppResult baseResult) {
        if (baseResult != null && !baseResult.isSuccess()) {
            getNomalLogger(context).warn(
                SysLogFormatUtil.format(context, baseResult, false, context.getObj()));
        }
    }

    /**
     * 获取业务日志输出对象。
     * @param context
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:46:43
     */
    private Logger getNomalLogger(SysAppContext context) {
        return context.getNomalLogger() == null ? nomalLogger : context.getNomalLogger()
            .getLogger();
    }

    /**
     * 获取摘要日志输出对象。
     * @param context
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:46:43
     */
    private Logger getDigestLogger(SysAppContext context) {
        return context.getNomalLogger() == null ? digestLogger : context.getDigestLogger()
            .getLogger();
    }

}
