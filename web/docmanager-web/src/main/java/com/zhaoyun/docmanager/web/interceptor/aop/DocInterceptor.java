/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.interceptor.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.zhaoyun.docmanager.biz.exception.DocException;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.core.exception.BizException;

/**
 * docmanager controller层方法执行拦截器
 * 
 * @author user
 * @version $Id: CardProdInterceptor.java, v 0.1 2016年7月7日 下午1:56:14 user Exp $
 */
@Component
@Aspect
public class DocInterceptor {

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void serviceExecuteMethod() {
    }

    private String toLogMessage(String name, Object... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("::");
        for (int i = 0; i < params.length; i++) {
            stringBuilder.append(params[i]).append("|");
        }
        return stringBuilder.toString();
    }

    private <T> Result<T> processServiceException(Logger logger, String msg, Throwable t) {
        if (t instanceof DocException) {
            if (((DocException) t).isError()) {
                logger.error(msg, t);
            }else{
                logger.warn(msg, t);
            }
            return Results.newFailedResult(((DocException) t).getStateCode(), t.getMessage());
        } else if (t instanceof BizException) {
            logger.warn(msg, t);
            return Results.newFailedResult(((BizException) t).getStateCode(), t.getMessage());
        } else {
            logger.error(msg, t);
            return Results.newFailedResult(CommonStateCode.FAILED, "system error, please retry");
        }
    }

    private static String parseMethodName(String name) {
        if (StringUtils.hasLength(name)) {
            StringBuilder sb = new StringBuilder();
            char[] names = name.toCharArray();
            for (int i = 0; i < names.length; i++) {
                if (names[i] >= 'A' && names[i] <= 'Z') {
                    sb.append(String.format("_%s", names[i]));
                } else {
                    sb.append(String.valueOf(names[i]).toUpperCase());
                }
            }
            return sb.toString();
        }
        return "";
    }

    private static String parseParams(Object[] params) {
        if (params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object p : params) {
                if (p != null) {
                    sb.append(JSONObject.toJSONString(p));
                }
            }
            return sb.toString();
        }
        return "";
    }

    @Around(value = "serviceExecuteMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String targetClassMethodName = joinPoint.getSignature().getName();
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            result = processServiceException(logger, String.format("%s.%s(%s) error",
                targetClassName, targetClassMethodName, parseParams(joinPoint.getArgs())), e);
        } finally {
            logger.info(toLogMessage(parseMethodName(targetClassMethodName),
                "cost:" + (System.currentTimeMillis() - start), parseParams(joinPoint.getArgs()),
                result));
        }
        return result;
    }

}
