/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.http;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * http相关辅助类
 * 
 * 
 * @author user
 * @version $Id: HttpUtils.java, v 0.1 2015年9月11日 上午10:17:16 user Exp $
 */
public class URIUtils {
    
    /**
     * 将Map映射为URL请求参数
     * 
     * @param params
     * @return 结果格式为：param1=val1&param2=val2
     */
    public static String concatUrlParams(Map<String,String> params) {
        return Joiner.on('&').withKeyValueSeparator("=").join(params);
    }

    /**
     * 获取url参数
     *
     * @param url
     * @return
     */
    public static Map<String,String> getParams(String url) {
        Map<String,String> params = Maps.newHashMap();
        List<NameValuePair> nvps = URLEncodedUtils.parse(url, Consts.UTF_8);
        for(NameValuePair nvp : nvps) {
            params.put(nvp.getName(), nvp.getValue());
        }

        return params;
    }
    
    /**
     * 获取完整uri
     * 
     * 目前有看到场景是需要传递Map<String,Object>的，
     * 为了方便起见，本方法能够自动移除非String类型的数据
     * 
     * @param baseUri 基础uri
     * @param params uri上带的参数
     * @return 完整拼接的uri
     */
    public static URI getRealUri(String baseUri, Map<String, ?> params) {
        List<NameValuePair> nvps = Lists.newArrayListWithCapacity(params.size());
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String && value != null) {
                String realStr = value.toString();
                if(isNotBlank(realStr)) {
                    nvps.add(new BasicNameValuePair(key, realStr));
                }
            }
        }

        return getRealUri(baseUri, nvps);
    }
    
    /**
     * 获取完整uri
     * 
     * 目前有看到场景是需要传递List<NameValuePair>的，
     * 
     * @param baseUri 基础uri
     * @param nvps uri上带的参数
     * @return 完整拼接的uri
     */
    public static URI getRealUri(String baseUri, List<NameValuePair> nvps) {
    	 try {
             URIBuilder uriBuilder = new URIBuilder(baseUri);
             if(CollectionUtils.isNotEmpty(nvps)) {
         		uriBuilder.addParameters(nvps);
         	}

         	return uriBuilder.build();
         } catch(Exception ex) {
             throw new RuntimeException(ex);
         }
    }
}
