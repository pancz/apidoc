/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.freemarker;

import java.util.List;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;

/**
 * 自定义的FreeMarkerConfigurer
 * @author user
 * @version $Id: CustomFreeMarkerConfigurer.java, v 0.1 2016年8月26日 下午5:29:22 user Exp $
 */
public class CustomFreeMarkerConfigurer extends FreeMarkerConfigurer {
    @Override
    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {

        return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));

    }
}
