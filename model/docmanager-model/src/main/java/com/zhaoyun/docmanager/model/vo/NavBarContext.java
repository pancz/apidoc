/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.vo;

import java.util.List;

import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocService;

/**
 * 
 * @author user
 * @version $Id: NavBarContext.java, v 0.1 2016年8月29日 上午9:53:13 user Exp $
 */
public class NavBarContext {
    private String           navType;

    private Integer          appId;

    private Integer          serviceId;

    private Integer          apiId;

    private List<DocApp>     apps;

    private List<DocService> services;

    private List<DocService> deprecatedServices;

    private List<DocApi>     apis;

    private List<DocApi>     deprecatedApis;

    public String getNavType() {
        return navType;
    }

    public void setNavType(String navType) {
        this.navType = navType;
    }

    public List<DocService> getDeprecatedServices() {
        return deprecatedServices;
    }

    public void setDeprecatedServices(List<DocService> deprecatedServices) {
        this.deprecatedServices = deprecatedServices;
    }

    public List<DocApi> getDeprecatedApis() {
        return deprecatedApis;
    }

    public void setDeprecatedApis(List<DocApi> deprecatedApis) {
        this.deprecatedApis = deprecatedApis;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public List<DocApp> getApps() {
        return apps;
    }

    public void setApps(List<DocApp> apps) {
        this.apps = apps;
    }

    public List<DocService> getServices() {
        return services;
    }

    public void setServices(List<DocService> services) {
        this.services = services;
    }

    public List<DocApi> getApis() {
        return apis;
    }

    public void setApis(List<DocApi> apis) {
        this.apis = apis;
    }

}
