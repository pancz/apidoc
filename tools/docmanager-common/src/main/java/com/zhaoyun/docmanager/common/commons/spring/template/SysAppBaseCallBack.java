/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

/**
 * 默认回调接口实现，
 *  1、默认实现lock(),unlock() ,lockKey() ,checkHandler(),checkIdempotence(),
 *  2、继承SysAppBaseCallBack后，只需要实现callback()，
 *  3、子类根据需要实现lock(),unlock() ,lockKey() ,checkHandler(),checkIdempotence()方法
 *  4、使用默认zookeeper进行加锁，需要重写lockKey();
 *  5、自定义锁，需要实现lock() unlock() 
 * @author user
 * @version $Id: SysAppBaseCallBack.java, v 0.1 2015年9月14日 下午1:49:14 user Exp $
 */
public abstract class SysAppBaseCallBack implements SysAppCallback {

    /**
     * 加锁   
     *  <p>1、默认使用zookeeper进行加锁，需要重新lockkey()方法</p>
     *  <p>2、子类覆盖写自己的加锁逻辑，可以使用 select for update nowait  或 其他加锁方式</p>
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:53:04
     */
    @Override
    public SysAppResult lock( SysAppContext context, String lockKey) {
        if (context.getLockInterface() == null){
            return null;
        }
        return context.getLockInterface().lock(lockKey);
    }

    /**
     * 解锁   
     *  <p>1、默认使用zookeeper进行加锁，需要重新lockkey()方法</p>
     *  <p>2、子类覆盖实现自己的解锁逻辑</p>
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:53:43
     */
    @Override
    public SysAppResult unLock( SysAppContext context, String lockKey) {
        if (context.getLockInterface() == null){
            return null;
        }
        return context.getLockInterface().unlock(lockKey);
    }

    /**
     * 返回默认加锁主键，
     *      默认使用zookeeper进行加锁，
     * @return
     * @author: user
     * @date: 2015年9月14日 下午1:51:07
     */
    @Override
    public String lockKey() {
        return "";
    }

    /**
     * 幂等性校验 
     * @return
     * @author: user
     * @date: 2015年9月14日 下午2:00:59
     */
    @Override
    public SysAppResult checkIdempotence() {
        return null;
    }

    @Override
    public SysAppResult afterBiz() {
        return null;
    }
}
