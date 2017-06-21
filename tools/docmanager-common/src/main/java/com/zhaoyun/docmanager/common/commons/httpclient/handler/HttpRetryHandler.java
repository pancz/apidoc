/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient.handler;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

/**
 * http请求重试处理类
 * 
 * @author user
 * @version $Id: HttpRetryHandler.java, v 0.1 2015年11月3日 下午4:45:14 user Exp $
 * 
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {
	private final static ThreadLocal<Integer> retryTimes = new ThreadLocal<>();

	@Override
	public boolean retryRequest(IOException exception, int executionCount,
			HttpContext context) {
		Integer maxTries = retryTimes.get();
		if(maxTries == null) { return false; }

		if ((exception instanceof ConnectTimeoutException ||
			 exception instanceof SocketTimeoutException)
                && executionCount <= maxTries) {

			return true;
		}

		return false;
	}
	
	public void accept(int retryTimes) {
		HttpRetryHandler.retryTimes.set(retryTimes);
	}

}
