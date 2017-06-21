/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhaoyun.docmanager.common.commons.spring.template.SysLogFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhaoyun.docmanager.common.commons.spring.template.SysAppResult;
import com.zhaoyun.docmanager.common.commons.spring.template.SysAppTemplateImpl;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.knife.result.Results;

/**
 * 
 * 
 * @author user
 * @version $Id: DalLoggingAspect.java, v 0.1 2015年11月26日 下午4:28:39 user Exp $
 */
//@Component
//@Aspect
public class SysAppAnnotationAspect {

    /** 默认业务日志 */
    private static Logger nomalLogger  = LoggerFactory.getLogger(SysAppTemplateImpl.class);

    /** 默认摘要日志 */
    private static Logger digestLogger = LoggerFactory.getLogger(SysAppTemplateImpl.class);

    
    private static final String regex = "\\([0-9]+[\\|]+[0-9a-zA-Z\\|]*?\\)";

    /**
     * 定义一个方法，用于声明切入点表达式。一般的，该方法中再不需要添加其他的代码
     * 使用@Pointcut 来声明切入点表达式
     * 后面的其他通知直接使用方法名直接引用方法名即可
     */
    @Pointcut("execution(@com.zhaoyun.docmanager.common.commons.spring.annotation.SysAppAnnotation * *(..))")
    public void declareJoinPointExpression() {

    }
    /**
     * 环绕通知需要携带ProceedingJoinPoint类型的参数
     * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint类型的参数可以决定是否执行目标方法。
     * 而且环绕通知必须有返回值，返回值即为目标方法的返回值
     */
    @Around("declareJoinPointExpression()")
    public Object aroundMethod(ProceedingJoinPoint pjd) {
        Object result = null;
        long startTime = System.currentTimeMillis();
        String actionCode = pjd.getSignature().getDeclaringTypeName() + "."
                            + pjd.getSignature().getName();
        String actionMsg = null;
        String bizKey = null;
        //        Arrays.toString(pjd.getArgs())
        Object[] args = pjd.getArgs();
        Signature sig = pjd.getSignature();
        SysAppAnnotation annotation = null;
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            annotation = msig.getMethod().getAnnotation(SysAppAnnotation.class);
            if (annotation != null) {
                if (!StringUtils.isBlank(annotation.actionCode())) {
                    actionCode = annotation.actionCode();
                }
                actionMsg = annotation.actionMsg();
                bizKey = key(args, annotation);
            }
        }
        SysAppResult sysresult =  null;
        //执行目标方法
        try {
            result = pjd.proceed(args);
        } catch (Throwable e) {
            result = Results.newFailedResult(null, CommonStateCode.INNER_SERVER_ERROR,
                e.getMessage());
            sysresult = new SysAppResult(null, CommonStateCode.INNER_SERVER_ERROR, null, e);
        } finally {
            try {
                long elapse = System.currentTimeMillis() - startTime;
                if (sysresult != null){
                	sysresult = new SysAppResult(result);
                }
                afterHandler(bizKey, actionCode,actionMsg, annotation, sysresult, elapse, args);
            } catch (Exception e) {
//                getNomalLogger(context).error("记录流水日志时出现异常", e);
            }

        }

        return result;
    }

    
    /**
     * 
     * @param bizKey
     * @param actionCode
     * @param actionMsg
     * @param annotation
     * @param result
     * @param args 
     */
    private void afterHandler(String bizKey, String actionCode,
			String actionMsg, SysAppAnnotation annotation, 
			SysAppResult result, long elapse, Object[] args) {
    	String commonLog = SysLogFormatUtil.format(actionMsg, null, result,false,  args);
    	String digestLog = SysLogFormatUtil.digestLog(null, actionCode, actionMsg, null, bizKey, elapse, result);
      nomalLogger(commonLog, result);
      digestLogger(digestLog, result);
		
	}
    /**
     * 
     * @param commonLog
     * @param result
     */
	private void digestLogger(String commonLog, SysAppResult result) {
		// TODO Auto-generated method stub
		
	}
	/**
     * 
     * @param context
     * @param baseResult
     * @date: 2015年11月19日 下午6:31:37
     */
    @SuppressWarnings("rawtypes")
	private void nomalLogger(String logStr ,  SysAppResult result) {
		if (result.isSystemError()){
//			if (result.getE() == null){
//				getNomalLogger(context).warn(logStr, result.getE());
//			}else{
//				if (result.getE() instanceof SysAppException) {
//	                // 对于SysAppException输出warn日志
//	                getNomalLogger(context).warn(logStr, result.getE());
//	            } else {
//	                getNomalLogger(context).error(logStr, result.getE());
//	            }
//			}
//		} else if(result.isBizFailed()){
//			getNomalLogger(context).warn(logStr, result.getE());
//		} else {
//			getNomalLogger(context).info(logStr, result.getE());
		}
    }
    
    
    
    
    /**
     * 
     * 方法调用前置
     * @param bizCode 业务代码
     * @param invocation 被调用方法
     * @param keyArg 关键参数索引
     * @param keyProps 关键参数属性
     * @return 日志信息
     */
    private String key(Object[] objs, SysAppAnnotation annotated) {
        // #1 没有设置直接返回
        if (StringUtils.isBlank(annotated.bizKey())) {
            return annotated.bizKey();
        }
        // prdex(1|xx|xx)(2|xx|xx)_xsdf(2|xx|xx) 

        // 根据(1|xx|xx) 分组
        String key = annotated.bizKey();
        String newKey = key.replaceAll(regex, "@_@");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);
        List<String> params = new ArrayList<String>();
        while (matcher.find()) {
            params.add(matcher.group(0));
        }
        // 没有参数直接返回
        if (params.size() == 0) {
            return newKey;
        }
        // 参数为空时返回null
        if (objs == null) {
            return null;
        }

        for (String param : params) {
            String value = "";
            String[] ps = param.substring(1, param.length() - 1).split("\\|");
            if (ps.length >= 1 && Integer.parseInt(ps[0]) < objs.length) {
                Object o = objs[Integer.parseInt(ps[0])];

                if (ps.length == 1) {
                    value = o.toString();
                }

                StringBuffer v1 = new StringBuffer();
                for (int i = 1; i < ps.length; i++) {
                    try {
                        Method getter = o.getClass().getMethod(
                            "get" + ps[i].substring(0, 1).toUpperCase() + ps[i].substring(1));
                        if (i == 0) {
                            v1.append(getter.invoke(o).toString());
                        } else {
                            v1.append(getter.invoke(o).toString());
                        }

                    } catch (Exception e) {
                        //                        logger.warn(CACHE_ERROR_PREFIX + ",Cache打印获取key=" + keyPropName
                        //                                    + "值异常，请检查声明", e);
                    }
                }
                value = value + v1;

            }
            newKey = newKey.replaceFirst("@_@", value);
        }

        return newKey;
    }
}