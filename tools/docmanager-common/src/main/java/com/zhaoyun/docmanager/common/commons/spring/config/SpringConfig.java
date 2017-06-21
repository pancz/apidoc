/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.spring.config;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * 
 * @author user
 * @version $Id: MemberConfig.java, v 0.1 2015年9月16日 下午7:29:06 user Exp $
 */
public class SpringConfig {

    private final Map<String, String> props  = Maps.newHashMap();
    private static SpringConfig       config = null;

    private static SpringConfig get() {
        if (config == null) {
            synchronized ("config") {
                if (config == null) {
                    config = new SpringConfig();
                }
            }
        }
        return config;
    }

    /**
     * 
     * @param map
     * @author: user
     * @date: 2015年9月16日 下午7:36:28
     */
    public static void set(Map<String, String> map) {
        get().props.putAll(map);
    }

    /**
     * 
     * @param key
     * @param defaultStr
     * @return
     * @author: user
     * @date: 2015年9月16日 下午7:40:12
     */
    public static String get(String key, String defaultStr) {
        return StringUtils.defaultIfBlank(get().props.get(key), defaultStr);
    }

    /**
     * @param key
     * @param defaultStr
     * @return
     * @author: user
     * @date: 2015年9月16日 下午7:40:12
     */
    public static String get(String key) {
        return get().props.get(key);
    }
}
