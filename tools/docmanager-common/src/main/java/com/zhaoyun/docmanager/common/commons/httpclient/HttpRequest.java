/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.zhaoyun.docmanager.common.commons.http.URIUtils;
import com.zhaoyun.docmanager.common.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.zhaoyun.docmanager.common.commons.httpclient.config.Config;
import com.zhaoyun.docmanager.common.commons.httpclient.config.RequestConfigBuilders;
import com.zhaoyun.docmanager.common.commons.httpclient.handler.HttpRetryHandler;

/**
 * http执行 入口
 * 
 * 在用户不指定自定义的{@link Config}的情况下，将启用默认配置
 * 
 * 设置如下(具体详见{@link DefaultConfigs}):
 * 	<ul>
 *     <li>最大连接数500</li>
 * 	   <li>每个Route的默认最大连接数200</li>
 *     <li>从pool中获取连接最多等待时间为2s</li>
 *     <li>连接超时时间为1.5s</li>
 *     <li>数据返回超时时间为5s</li>
 *     <li>SSL默认设置为信任所有</li>
 * 	</ul>
 * 
 * @author user
 * @version $Id: Request.java, v 0.1 2015年9月10日 下午5:10:36 user Exp $
 */
public class HttpRequest {
    /** 默认配置 */
    private Config config = DefaultConfigs.get();
    /** 目前只有post和get */
	private HttpRequestBase httpMethod;
	/** 方便拼接http请求的param */
	private URIBuilder uriBuilder;
	/** 用于cookie的存储 */
	private CookieStore cookieStore = new BasicCookieStore();
	
	private HttpRequest(HttpRequestBase httpMethod, String uri) {
		this.httpMethod = httpMethod;
		try {
            this.uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
	}

	/** Get 请求入口*/
    public static HttpRequest Get(final String uri) {
    	return new HttpRequest(new HttpGet(), uri);
    }

    /** Post 请求入口 */
    public static HttpRequest Post(final String uri) {
        return new HttpRequest(new HttpPost(), uri);
    }

	/** 配置HttpClient */
    public HttpRequest config(Config config) {
        this.config = checkNotNull(config, "config should not be null");
        return this;
    }

    /** 发起对api(carman)的请求时，需要在头部填充特定信息 */
    public HttpRequest withAPIHeaders() {
    	httpMethod.setHeader("Token", "ED30649818CACB78F91E39574DE68957D73BAC14A98C1FDD");
    	httpMethod.setHeader("Version", "1.0.0");
    	httpMethod.setHeader("Source","9");
    	httpMethod.setHeader("Net","");
    	httpMethod.setHeader("Channel","");
    	httpMethod.setHeader("UserId","");
    	httpMethod.setHeader("IMEI","");

        return this;
    }

    /** post提交的基础方法入口 */
    private HttpRequest body(final HttpEntity entity) {
        if (this.httpMethod instanceof HttpEntityEnclosingRequest/*patch, post, put*/) {
            ((HttpEntityEnclosingRequest) this.httpMethod).setEntity(entity);
        } else {
            //get请求会被拒绝
            throw new IllegalStateException(
                this.httpMethod.getMethod() + " request cannot enclose an entity");
        }
        return this;
    }

    /**
    *  post提交表单的内容
    *  @deprecated
    *  replaced by <code>HttpRequest.form(FormContent formContent)</code>
    */
    public HttpRequest bodyForm(final Iterable <? extends NameValuePair> formParams) {
        final List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (NameValuePair param : formParams) {
            
            paramList.add(param);
        }
        final ContentType contentType = ContentType.create(URLEncodedUtils.CONTENT_TYPE, Consts.UTF_8);
        final String s = URLEncodedUtils.format(paramList, Consts.UTF_8.name());
        return bodyString(s, contentType);
    }

    /** 
     * post请求的Map形式的body内容 
     * @deprecated
     * replaced by <code>HttpRequest.form(FormContent formContent)</code>
     */
    public HttpRequest bodyMap(Map<String,String> params) {
    	return bodyString(URIUtils.concatUrlParams(params));
    }

    /** 
     * post请求的body内容 
     * @deprecated
     * replaced by <code>HttpRequest.form(FormContent formContent)</code>
     */
    public HttpRequest bodyString(final String s) {
    	return bodyString(s, ContentType.create(URLEncodedUtils.CONTENT_TYPE, Consts.UTF_8));
    }

    /** 
     * post请求的body内容 
     * @deprecated
     * replaced by <code>HttpRequest.form(FormContent formContent)</code>
     */
    public HttpRequest bodyString(final String s, final ContentType contentType) {
        final Charset charset = contentType != null ? contentType.getCharset() : null;
        final byte[] raw = charset != null ? s.getBytes(charset) : s.getBytes();
        return body(new ByteArrayEntity(raw, contentType));
    }
    
    /** 
     * post请求 上传文件
     * @deprecated
     * replaced by <code>HttpRequest.form(FormContent formContent)</code>
     */
    public HttpRequest bodyFile(String inputName,File file){
        checkArgument(StringUtils.isNotBlank(inputName), "inputName should not be blank");
        Preconditions.checkArgument(FileUtils.isValidFile(file), "source is not a valid file");
        
        HttpEntity filentity=MultipartEntityBuilder.create().addBinaryBody(inputName, file).build();
        return body(filentity);
    }
    
   /**
    * post请求 提交表单
    * @param formContent 表单对象
    * @return
    * @date: 2016年7月15日 下午6:06:51
    */
    public HttpRequest form(FormContent formContent) {
        checkNotNull(formContent, "formContent must be not NULL");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : formContent.getTextMap().entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
        }

        for (Map.Entry<String, File> entry : formContent.getFileMap().entrySet()) {
            builder.addBinaryBody(entry.getKey(), entry.getValue());
        }

        return body(builder.build());

    }

    /** 添加http请求的头部信息 */
    public HttpRequest addHeader(final String name, final String value) {
        this.httpMethod.addHeader(name, value);
        return this;
    }

    /** 设置http请求的头部信息 */
    public HttpRequest setHeader(final String name, final String value) {
        this.httpMethod.setHeader(name, value);
        return this;
    }
    
    /** 添加http的请求参数 */
    public HttpRequest addParam(String param, String value) {
    	this.uriBuilder.addParameter(param, value);
        return this;
    }

    /** 添加http请求时的cookie信息 */
    public HttpRequest addCookie(String name, String value, String domain) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookieStore.addCookie(cookie);
        return this;
    }
    
    /** 批量添加cookie {@link CookieBuilder}*/
    public HttpRequest addCookies(List<Cookie> cookies) {
        for(Cookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }
        return this;
    }

    /** 在请求失败的情况下，重试的次数 */
	public HttpRequest retryIfFailed(int retryTimes) {
		HttpRetryHandler httpRetryHandler = config.getRetryHandler();
		checkNotNull(httpRetryHandler, "retry handler was not provided");
		httpRetryHandler.accept(retryTimes);

		return this;
	}

    /** 使用基础的RequestConfig，执行Http请求 */
    public HttpResult execute() {
    	return execute(RequestConfigBuilders.baseBuilder().build());
    }

    /**
     * 根据requestConfig，执行Http请求
     * 
     * @param requestConfig 本次执行的Http请求的配置
     * @return HttpResult 执行结果
     */
    public HttpResult execute(RequestConfig requestConfig) {
    	checkNotNull(requestConfig, "requestConfig should not be null");

    	HttpResponse httpResp = null;
        try {
            httpMethod.setURI(uriBuilder.build());
            httpMethod.setConfig(requestConfig);

            HttpClient httpClient = HttpClientHolder.get(config);

            HttpClientContext context = HttpClientContext.create();
            //添加对cookie的支持
            context.setCookieStore(cookieStore);

            httpResp = httpClient.execute(httpMethod, context);
            return new HttpResult(httpResp, context);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpClientUtils.closeQuietly(httpResp);
        }
    }
}

