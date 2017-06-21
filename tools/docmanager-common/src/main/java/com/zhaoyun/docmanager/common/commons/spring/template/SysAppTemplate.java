/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

/**
 *
 * 系统应用模板。
 * <p>
 *      通过使用系统应用模板，可以使开发人员只关注自己的业务逻辑实现，这里系统应用模板提供如下几个功能(类似于AOP)<br>
 *      <ol>
 *          <li>业务调用日志输出的控制</li>
 *          <li>用于系统监控的digest日志输出</li>
 *          <li>事务控制</li>
 *          <li>异常控制，对于系统抛出的异常、业务抛出的异常、业务返回的失败结果，都可以进行异常控制，以及对异常日志的输出。</li>
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
public interface SysAppTemplate {
    /**
     * 
     * 业务处理。
     * 
     * @param context
     *               系统应用上下文
     * @param callback
     *               回调接口
     * @return       
     *               业务处理结果
     */
    public SysAppResult execute(SysAppContext context, SysAppCallback callback);

}
