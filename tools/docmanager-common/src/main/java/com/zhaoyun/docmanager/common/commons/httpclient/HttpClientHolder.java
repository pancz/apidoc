/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.common.collect.Maps;
import com.zhaoyun.docmanager.common.commons.httpclient.config.Config;

/**
 * HttpClient的持有和维护类，内部会维护一个map根据具体传入的Config.key来关联
 * 
 * @author user
 * @version $Id: HttpClientHolder.java, v 0.1 2015年9月10日 下午5:11:23 user Exp $
 */
public class HttpClientHolder {
	private final static Map<String, CloseableHttpClient> httpClientMap = Maps.newHashMap();
	
	/**
	 * HttpClient对象的缓存管理
	 * 
	 * @param config HttpClient的配置
	 * @return HttpClient 
	 */
	public static CloseableHttpClient get(Config config) {
	    checkNotNull(config, "config object is null");
	    checkNotNull(config.getKey(), "config.key is null");

		if(!httpClientMap.containsKey(config.getKey())) {
			synchronized(HttpClientHolder.class) {
				if(!isExist(config.getKey())) {
				    CloseableHttpClient httpClient = createHttpClient(config);
	                httpClientMap.put(config.getKey(), httpClient);
	            }
	        }
	    }
				
		return httpClientMap.get(config.getKey());
	}

    private static CloseableHttpClient createHttpClient(Config config) {
		return HttpClients.custom()
					   .setRetryHandler(config.getRetryHandler())
					   .setConnectionManager(config.getConnectionManager())
					   .setSSLSocketFactory(new SSLConnectionSocketFactory(config.getSslContext()))
					   .build();
	}

    /**
     * 查看Config的key是否已经存在
     * @param key
     * @return true 存在，false 不存在
     */
    public static boolean isExist(String key) {
    	return httpClientMap.containsKey(key);
    }
    
}