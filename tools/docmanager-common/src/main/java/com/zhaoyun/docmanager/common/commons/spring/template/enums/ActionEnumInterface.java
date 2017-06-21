/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template.enums;

/**
 * 业务接口枚举
 * <p>
 *      <b>把业务操作名称归集到一起，主要是因为：</b>
 *      <ol>
 *      <li>以便对整个系统的业务用例进行管理</li>
 *      <li>通过此枚举，可以打印基于用例的日志</li>
 *      </ol>
 *      <pre>
 *          注意：这里的actionEnum需要搞清楚，是SysAppTemplate来使用的，还是只是单纯提供给LogFormat使用的, 
 *          因为SysAppTemplate中会打印摘要日志提供给监控系统进行业务监控，请开发在新增枚举值请先搞清楚，是干什么的
 *          此外还要注意:
 *              因为本类中的actionCode和bizType和message将会在日志统计脚本中使用到, 所以有一定的要求:
 *              1. actionCode长度不能大于40个字节;
 *              2. message中不能包含逗号","
 *              3. bizType 业务域
 *              4. bizCode 具体业务，多个接口可以对应统一业务，用于业务加锁操作，列如：新增接口
 *      </pre>
 * </p> 
 * @author user
 * @version $Id: EnumInterface.java, v 0.1 2015年9月10日 下午5:20:18 user Exp $
 */
public interface ActionEnumInterface {
    /**
     *  接口枚举名称
     * @return 枚举代码
     */
    public String getActionCode();

    /**
     * 业务模块
     * @return
     * @date: 2015年11月19日 下午4:18:27
     */
    public String getBizType();

    /**
     * 接口对应业务，  多个接口可以对应统一业务，列如：新增接口
     * @return 枚举代码
     */
    public String getBizCode();

    /**
     * 接口描述，用于业务日志输出
     * @return 枚举代码描述
     */
    public String getMessage();

}
