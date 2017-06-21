/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.util;

/**
 * @author zhongkefeng@zhaoyun.com
 * @version $Id: HttpClientUtil.java, v 0.1 2016年5月4日 下午3:12:24 user Exp $
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static CloseableHttpClient httpclient;
    private static RequestConfig requestConfig;

    private static final int HC_GETCONN_TIMEOUT = 10000;
    private static final int HC_LINK_TIMEOUT = 10000;
    private static final int HC_SO_TIMEOUT = 15000;

    static {

        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                // 获取manager中连接 超时时间
                .setConnectionRequestTimeout(HC_GETCONN_TIMEOUT)
                // 连接服务器 超时时间
                .setConnectTimeout(HC_LINK_TIMEOUT)
                // 服务器处理 超时时间
                .setSocketTimeout(HC_SO_TIMEOUT).build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpclient = httpClientBuilder.build();
    }

    /**
     * @param url
     * @param paramsMap
     * @return
     * @date: 2016年5月4日 下午3:33:59
     */
    public static String post(String url, Map<String, String> paramsMap) {
        HttpPost post = postForm(url, paramsMap);
        logger.info("Http Post Request, URL:" + url + "; ParameterMap:" + paramsMap.toString());
        return invoke(httpclient, post);
    }

    /**
     * 带HEADER的POST请求
     *
     * @param url
     * @param paramsMap
     * @param headerMap
     * @return
     * @date: 2016年5月4日 下午3:34:02
     */
    public static String postWithHeader(String url, Map<String, String> paramsMap, Map<String, String> headerMap) {
        HttpPost post = postForm(url, paramsMap);
        logger.info("Http Post Request, URL:" + url + "; ParameterMap:" + paramsMap.toString());
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return invoke(httpclient, post);
    }

    /**
     * @param url
     * @return
     * @date: 2016年5月4日 下午3:34:06
     */
    public static String get(String url) {
        logger.info("Http Get Request, URL:" + url);
        HttpGet get = new HttpGet(url);
        return invoke(httpclient, get);
    }

    /**
     * @param httpclient
     * @return
     * @date: 2016年5月4日 下午3:34:08
     */
    private static String invoke(CloseableHttpClient httpclient, HttpRequestBase httpUri) {
        CloseableHttpResponse response = null;
        try {
            httpUri.setConfig(requestConfig);
            response = sendRequest(httpclient, httpUri);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return paseResponse(response);
                } else {
                    logger.warn("Response ErrorCode: " + response.getStatusLine().getStatusCode());
                    return "请求失败，代码: " + response.getStatusLine().getStatusCode();
                }
            } else {
                logger.warn("Error Request Timeout");
                return "请求失败，无响应";
            }
        } catch (Exception e) {
            logger.error("Invoke Post Error", e);
            return "请求失败，原因: " + e.getMessage();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("response close Error", e);
            }
        }
    }

    /**
     * @param response
     * @return
     * @date: 2016年5月4日 下午3:34:11
     */
    private static String paseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        //String charset = EntityUtils.getContentCharSet(entity);
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return body;
    }

    /**
     * @param httpclient
     * @param httpost
     * @return
     * @date: 2016年5月4日 下午3:34:13
     */
    private static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient, HttpRequestBase httpost) {
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return response;
    }

    /**
     * @param url
     * @param params
     * @return
     * @date: 2016年5月4日 下午3:34:16
     */
    private static HttpPost postForm(String url, Map<String, String> params) {
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return httpost;
    }

    /***
     * @param request
     * @return
     * @description:获取IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if (isIpv4(ip))
            return ip;
        else
            return "192.168.1.1";

    }

    public static boolean isIpv4(String ipAddress) {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();

    }
}