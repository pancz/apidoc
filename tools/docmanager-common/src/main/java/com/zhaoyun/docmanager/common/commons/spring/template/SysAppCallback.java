/**
  * com Inc
  * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

/**
 *
 * 系统应用模板回调接口.
 * 
 * <p>
 *      在使用系统模板时，把业务逻辑不关注的打印日志、系统异常、事务控制交给了模板，
 *      真正的业务逻辑由系统模板通过回调接口的方式来实现
 * </p>
 *
 * @author user
 * @version $Id: SysAppTemplateImpl.java, v 0.1 2015年9月10日 下午5:26:44 user Exp $
 */
public interface SysAppCallback {
    /**
     * 参数校验
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:57:53
     */
    public SysAppResult checkHandler();


    /**
     * 返回默认加锁主键，
     *      默认使用zookeeper进行加锁，
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:51:07
     */
    public String lockKey();

    /**
     * 加锁
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:53:04
     */
    public SysAppResult lock(SysAppContext context, String lockKey);

    /**
     * 解锁  
     *   释放锁失败时，需要设置释放需要数据回滚。 事务模式数据进行回滚，
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:53:43
     */
    public SysAppResult unLock(SysAppContext context, String lockKey);



    /**
     * 幂等性校验 
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:00:59
     */
    public SysAppResult checkIdempotence();

    
    /**
     * 
     * 业务处理
     * @return 业务处理结果
     */
    public SysAppResult doBiz();
    
    /**
     * 事后处理
     * @return
     * @date: 2015年11月19日 下午3:32:04
     */
    public SysAppResult afterBiz();
}
