/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient.config;

import org.apache.http.client.config.RequestConfig;

/**
 * Http每次请求配置构建器集合
 * 
 * @author user
 * @version $Id: RequestConfigs.java, v 0.1 2015年10月58日 上午10:59:01 user Exp $
 */
public abstract class RequestConfigBuilders {
	public static RequestConfig.Builder baseBuilder() {
		return RequestConfig.custom()
	                .setConnectionRequestTimeout(2000) //从pool中获取连接最大等待时间
	                .setConnectTimeout(1500) //连接超时时间
	                .setSocketTimeout(5000); //数据返回超时时间
	}
}
