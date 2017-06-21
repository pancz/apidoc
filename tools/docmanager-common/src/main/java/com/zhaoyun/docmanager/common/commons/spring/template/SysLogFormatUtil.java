/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

import org.apache.commons.lang3.StringUtils;

import com.zhaoyun.docmanager.common.commons.spring.template.enums.ActionEnumInterface;
import com.zhaoyun.docmanager.common.commons.spring.template.enums.SysResultEnum;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.common.knife.result.StateCode;

/**
 * 
 * 日志输出格式化工具.
 * <p>
 *      通过日志的格式化工具，控制一个系统输出的大部分业务日志规范，以便更好的管理和查询日志。
 * </p>
 * 
 * @author user
 * @version $Id: LogFormatUtils.java, v 0.1 2015年9月10日 下午5:30:27 user Exp $
 */
public class SysLogFormatUtil {

    private static final String FULL_LEFT_SIGN  = "【";
    private static final String FULL_RIGHT_SIGN = "】";
    private static final String HALF_LEFT_SIGN  = "[";
    private static final String HALF_RIGHT_SIGN = "]";

    private static final String CROSSBAND_SIGN  = " - ";
    private static final String EQUAL_SIGN      = " = ";
//    private static final String SPACE_SIGN      = " ";

    private static final String CODE_STR        = "异常码";
//    private static final String MESSAGE_STR     = "原因";
    private static final String PARAMETER_STR   = "参数";
    private static final String RESULT_STR      = "处理结果";

    /**
     * 格式如：【查询用户信息】 参数 - 具体参数信息
     * 
     * @param actionEnum 业务操作枚举
     * @param obj 具体参数对象
     * @return 格式化后的日志字符串
     */
    public static String format(ActionEnumInterface actionEnum, Object... obj) {
        return format(actionEnum, null, obj);
    }

//    /**
//     * 
//     * @param context
//     * @param resultEnum
//     * @param extLogInfo
//     * @param obj
//     * @return
//     * @date: 2015年12月24日 下午5:23:30
//     */
//    public static String format(SysAppContext context, StateCode resultEnum, String extLogInfo,
//                                Object... obj) {
//        return format(context, null, resultEnum, false, extLogInfo, obj);
//    }

    /**
     * 
     * 格式如：【查询用户信息】 token -[] - {扩展日志} - 参数 - [userId = *******]- 处理结果 - [代码 = STATUS_ERROR|认证状态不正确] -[result= 返回内容] -
     * 
     * @param actionEnum 系统用例名称枚举
     * @param resultEnum 处理结果
     * @param extLogInfo 日志扩展信息
     * @param obj 日志信息
     * @return 格式化的日志字符串
     */
    public static String format(SysAppContext context, SysAppResult baseResult,
    							boolean isdebug,
                                Object... obj) {
    	return format(context.getActionEnum().getMessage(),
    			context.getToken(),baseResult, isdebug, obj);
    }

    
    /**
     * 
     * 格式如：【查询用户信息】 token -[] -参数- [userId = *******]- 处理结果 - [代码 = STATUS_ERROR|认证状态不正确] -[result= 返回内容] -
     * 
     * @param actionEnum 系统用例名称枚举
     * @param resultEnum 处理结果
     * @param extLogInfo 日志扩展信息
     * @param obj 日志信息
     * @return 格式化的日志字符串
     */
    public static String format(String actionName, String token,
                                Object result,
                                boolean isdebug, 
                                Object... obj) {
        StringBuilder sb = new StringBuilder();
        // 格式：“【系统用例名称】”
        if (actionName != null) {
            sb.append(FULL_LEFT_SIGN).append(actionName)
                .append(FULL_RIGHT_SIGN);
        } else {
            sb.append(FULL_LEFT_SIGN).append("无动作类型").append(FULL_RIGHT_SIGN);
        }
        // 格式：“token -[] ”
        if (token != null) {
            sb.append("token").append(CROSSBAND_SIGN);
            sb.append(HALF_LEFT_SIGN).append(token);
            sb.append(HALF_RIGHT_SIGN);
        }

        // 格式：“参数 - ”
        if (obj != null) {
            sb.append(PARAMETER_STR).append(CROSSBAND_SIGN);
            sb.append(FULL_LEFT_SIGN);
            // 格式：
            for (int i = 0; i < obj.length; i++) {
                sb.append(HALF_LEFT_SIGN).append(obj[i]).append(HALF_RIGHT_SIGN);
            }
            sb.append(FULL_RIGHT_SIGN);
        }

        if (result != null) {
        	if (!(result instanceof Result )){
        		sb.append(CROSSBAND_SIGN).append(RESULT_STR).append(CROSSBAND_SIGN);
        		sb.append(result.toString());
        	}else{
        		@SuppressWarnings("unchecked")
				Result<Object> baseResult = (Result<Object>)result;
	        	
	        	StateCode resultEnum = baseResult.getStateCode();
	            // 格式：“ - 返回结果 - ”
	            sb.append(CROSSBAND_SIGN).append(RESULT_STR).append(CROSSBAND_SIGN);
	
	            String rt = resultEnum.getCode() + "|" + resultEnum.getDesc();
	            rt += "|" ;
	            if(StringUtils.isNoneBlank(baseResult.getStatusText())){
	            	rt += baseResult.getStatusText();
	            }
	            rt += "|" ;
	            if(StringUtils.isNoneBlank(baseResult.getAppMsg())){
	            	rt += baseResult.getAppMsg();
	            }
	             
	            // 格式：“[处理结果 = 异常码|描述|扩展信息|app文案]”
	            sb.append(HALF_LEFT_SIGN).append(CODE_STR).append(EQUAL_SIGN).append(rt)
	                .append(HALF_RIGHT_SIGN);
	
	            String resultStr = "null";
	            if (baseResult != null && baseResult.getData() != null) {
	                resultStr = baseResult.getData().toString();
	            }
	            if (!isdebug && resultStr.length() > 300) {
	                resultStr = resultStr.substring(0, 300) + "...";
	            }
	            // 格式：“[result= 返回内容] - ”
	            sb.append(HALF_LEFT_SIGN).append("result").append(EQUAL_SIGN).append(resultStr)
	                .append(HALF_RIGHT_SIGN).append(CROSSBAND_SIGN);
	        	}
        }
        return sb.toString();
    }

    
    /**
     * 构建digest日志。
     * <p>
     *      流水帐格式:
     *      时间[@业务类型@,操作代码,操作名词,是否成功(Y/N/E),结果码,业务标记信息,处理时长(ms)]
     *      <b>注意：在打印日志时，第一个字段（业务类型），两侧会加上@这样的特殊符号，方便后期统计</b>
     * </p>
     * @param context
     * @param elapse
     * @param baseResult
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:47:36
     */
    public static String digestLog(SysAppContext context, long elapse, SysAppResult baseResult) {
    	return digestLog(context.getActionEnum().getBizType(),
    			context.getActionEnum().getActionCode(), 
    			context.getActionEnum().getMessage(), 
    			context.getToken(), context.getDigestTag(),
    			elapse, baseResult);
    }
    
    
    /**
     * 构建digest日志。
     * <p>
     *      流水帐格式:
     *      时间[@业务类型@,操作代码,操作名词,是否成功(Y/N/E),结果码,业务标记信息,处理时长(ms)]
     *      <b>注意：在打印日志时，第一个字段（业务类型），两侧会加上@这样的特殊符号，方便后期统计</b>
     * </p>
     * @param context
     * @param elapse
     * @param baseResult
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:47:36
     */
    @SuppressWarnings("unchecked")
	public static String digestLog(String sourceType, String actionCode, String actionMsg,
    		String token, String digestTag, long elapse, Object result) {
    	Result<Object> baseResult = null;
    	
    	if (result instanceof  Result){
    		baseResult = (Result<Object> )result;
    	}else{
    		if (result != null){
    			baseResult = Results.newSuccessResult(null);
    		}else{
    			baseResult = Results.newFailedResult(CommonStateCode.FAILED);
    		}
    	}
    	
    	StateCode errorCode = CommonStateCode.SUCCESS;
        if (baseResult != null) {
            errorCode = baseResult.getStateCode();
            if (errorCode == null) {
                errorCode = CommonStateCode.FAILED;
            }
        }

        StringBuffer sb = new StringBuffer();

        sb.append("[");
        // 业务类型
        sb.append("@");
        sb.append(StringUtils.defaultIfEmpty(sourceType, "-"));
        sb.append("@,");
        // 操作代码
        sb.append(StringUtils.defaultIfEmpty(actionCode, "-")).append(
            ",");
        // 操作名词
        sb.append(StringUtils.defaultIfEmpty(actionMsg, "-"))
            .append(",");
        // token
        sb.append(StringUtils.defaultIfEmpty(token, "-")).append(",");
        // 扩展信息：userId
        sb.append(StringUtils.defaultIfEmpty(digestTag, "-")).append(",");
        // 是否成功，结果码  YNE 
        if (baseResult.isSystemError()){
        	sb.append(SysResultEnum.E.getCode());
        }else if(baseResult.isBizFailed()){
        	sb.append(SysResultEnum.N.getCode());
        }else{
        	sb.append(SysResultEnum.Y.getCode());
        }
        sb.append(",");
        sb.append(errorCode.getCode()).append(",");

        // 处理时长
        sb.append(elapse + "ms");
        sb.append("]");

        return sb.toString();
    }
}
