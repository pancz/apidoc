/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.biz.service;

import com.zhaoyun.docmanager.common.constants.NavTypeEnum;
import com.zhaoyun.docmanager.model.vo.NavBarContext;

/**
 * 
 * @author user
 * @version $Id: NavBarService.java, v 0.1 2016年8月29日 上午10:02:54 user Exp $
 */
public interface NavBarService {
    void doService(NavBarContext navBarContext, NavTypeEnum navTypeEnum);
}
