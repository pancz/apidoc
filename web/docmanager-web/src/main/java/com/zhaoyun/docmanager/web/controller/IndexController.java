/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

/**
 * 
 * @author user
 * @version $Id: IndexController.java, v 0.1 2016年8月26日 下午5:40:15 user Exp $
 */
@Controller
public class IndexController {

    private final static String INDEX = "/index";

    private final static String APIINFO = "/apiInfo";

    private final static String APPINFO = "/appInfo";

    private final static String VERSIONDIFF = "/versiondiff";

    private final static String MESSAGELIST = "/messageList";

    private final static String MESSAGEEDIT = "/messageEdit";

    private final static String MESSAGEINFO = "/messageInfo";

    private final static String STATIC_0    = "/static0";

    private final static String STATIC_1    = "/static1";

    @RequestMapping(value = { "/", "/index" })
    public ModelAndView index() {
        return new ModelAndView(INDEX);
    }

    @RequestMapping(value = { "/apiInfo" })
    public ModelAndView apiInfo(String navType,Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(APIINFO, model);
    }

    @RequestMapping(value = { "/appInfo" })
    public ModelAndView appInfo(String navType, Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(APPINFO, model);
    }

    @RequestMapping(value = { "/versiondiff" })
    public ModelAndView versiondiff(String navType, Integer navId, String version1, String version2) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        model.put("version1", version1);
        model.put("version2", version2);
        return new ModelAndView(VERSIONDIFF, model);
    }

    @RequestMapping(value = { "/messageList" })
    public ModelAndView messageList(String navType, Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(MESSAGELIST, model);
    }

    @RequestMapping(value = { "/messageEdit" })
    public ModelAndView messageEdit(String navType, Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(MESSAGEEDIT, model);
    }

    @RequestMapping(value = { "/messageInfo" })
    public ModelAndView messageInfo(String navType, Integer navId, Integer messageId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        model.put("messageId", messageId);
        return new ModelAndView(MESSAGEINFO, model);
    }

    @RequestMapping(value = { "/static0" })
    public ModelAndView static0(String navType, Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(STATIC_0, model);
    }

    @RequestMapping(value = { "/static1" })
    public ModelAndView static1(String navType, Integer navId) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("navId", navId);
        model.put("navType", navType);
        return new ModelAndView(STATIC_1, model);
    }
}
