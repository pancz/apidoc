/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient;

import java.util.List;

import com.zhaoyun.docmanager.common.commons.base.Encodings;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Http请求结果类
 * 
 * @author user
 * @version $Id: HttpResult.java, v 0.1 2015年9月10日 下午5:16:04 user Exp $
 */
public class HttpResult {
    private HttpResponse httpResp;
    private HttpClientContext context;
    private String data;

    HttpResult(HttpResponse httpResp, HttpClientContext context) {
        this.httpResp = httpResp;
        this.context = context;
        try {
            this.data = EntityUtils.toString(httpResp.getEntity(), Encodings.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    //是否200
    public boolean isSuccess() {
        return httpResp.getStatusLine().getStatusCode() == 200;
    }
    
    //http请求获取的结果数据
    public String getData() {
        return data;
    }
    
    //获取结果类型为json格式
    public JSONObject getJsonData() {
        return JSONObject.parseObject(getData());
    }
    
    //获取cookies
    public List<Cookie> getCookies() {
        return context.getCookieStore().getCookies();
    }

    //http请求返回response的状态码
    public int getStatusCode() {
        return httpResp.getStatusLine().getStatusCode();
    }
    
    //http请求HttpResponse对象，可以获取更详细信息,注意这个是输出流已经被关闭掉的HttpResponse
    public HttpResponse getHttpResponse() {
        return this.httpResp;
    }
}
