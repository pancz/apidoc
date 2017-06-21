/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.google.common.collect.Lists;

/**
 * Cookie构建器
 *
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年4月19日 下午8:03:02 $
 */
public class CookieBuilder {
    private CookieBuilder() {}

    private List<Cookie> cookies = Lists.newArrayList();
    private String domain;

    /** 创建CookieBuilder实例 */
    public static CookieBuilder create() {
        CookieBuilder builder = new CookieBuilder();
        return builder;
    }
    
    /** 统一设置domain, 后续添加cookie在未指定domain的情况下, 默认使用该domain */
    public CookieBuilder withDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /** 添加cookie, 并且使用withDomain设定的默认domain */
    public CookieBuilder newCookie(String name, String value) {
        return newCookie(name, value, this.domain);
    }

    /** 添加cookie时, 指定domain */
    public CookieBuilder newCookie(String name, String value, String domain) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(defaultIfBlank(domain, this.domain));
        cookies.add(cookie);
        return this;
    }
    
    public List<Cookie> build() {
        return this.cookies;
    }
}
