/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.template;

/**
 * 
 * @author user
 * @version $Id: SysAppLockInterface.java, v 0.1 2015年11月23日 下午5:34:11 user Exp $
 */
public interface DefaultLockInterface {
    
    /**
     * 加锁
     * @param lockKey
     * @return
     * @date: 2015年11月23日 下午5:35:38
     */
    SysAppResult lock(String lockKey);
    
    /**
     * 解锁
     * @param lockKey
     * @return
     * @date: 2015年11月23日 下午5:35:47
     */
    SysAppResult unlock(String lockKey);

}
