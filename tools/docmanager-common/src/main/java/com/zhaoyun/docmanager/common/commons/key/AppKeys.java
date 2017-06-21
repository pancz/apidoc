/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.key;

import com.google.common.base.Joiner;

/**
 * App key的生成策略类
 * 
 * @version $Id: AppKeys.java, v 0.1 2015年11月03日 下午2:39:04 user Exp $
 *
 */
public abstract class AppKeys {
	/**
	 * 默认以':'分隔
	 * 
	 * @param appId 应用ID
	 * @param moduleId 模块ID
	 * @param bizType 具体业务ID
	 * @return 运行时ID
	 */
	public static String appKey(String appId, String moduleId, String bizType) {
		return Joiner.on(DELIMITER.SEMICOLON).join(appId, moduleId, bizType);
	}
	
	
}
