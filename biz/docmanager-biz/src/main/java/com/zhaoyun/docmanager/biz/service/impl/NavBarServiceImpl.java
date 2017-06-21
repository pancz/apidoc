/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.biz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.zhaoyun.docmanager.biz.service.NavBarService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zhaoyun.docmanager.common.constants.NavTypeEnum;
import com.zhaoyun.docmanager.dal.dao.ext.DocApiDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppVersionDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocServiceDao;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import com.zhaoyun.docmanager.model.param.AppQueryParam;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;
import com.zhaoyun.docmanager.model.vo.NavBarContext;

/**
 * 
 * @author user
 * @version $Id: NavBarServiceImpl.java, v 0.1 2016年8月29日 上午10:07:46 user Exp $
 */
@Service
public class NavBarServiceImpl implements NavBarService {

    @Resource
    private DocServiceDao    serviceDao;
    @Resource
    private DocApiDao        apiDao;
    @Resource
    private DocAppVersionDao appVersionDao;
    @Resource
    private DocAppDao        docAppDao;

    /** 
     * @see NavBarService#doService(NavBarContext, NavTypeEnum)
     */
    @Override
    public void doService(NavBarContext navBarContext, NavTypeEnum navTypeEnum) {
        switch (navTypeEnum) {
            case APP:
                processApp(navBarContext);
                break;

            case SERVICE:
            case DEPRECATED_SERVICE:
                processService(navBarContext);
                break;

            case API:
            case DEPRECATED_API:
            case DEPRECATED_SERVICE_API:
                processApi(navBarContext);
                break;
            default:
                break;
        }

    }

    private void processApp(NavBarContext navBarContext) {
        AppQueryParam appQueryPram = new AppQueryParam();
        appQueryPram.setSortColumn("name");
        appQueryPram.setSortType("asc");
        List<DocApp> apps = docAppDao.queryAll(appQueryPram);
        navBarContext.setApps(apps);
    }

    private void processApi(NavBarContext navBarContext) {
        DocApi api = apiDao.selectByPrimaryKey(navBarContext.getApiId());
        Integer serviceId = api.getServiceId();
        navBarContext.setServiceId(serviceId);
        ApiQueryParam apiQueryPram = new ApiQueryParam();
        apiQueryPram.setServiceId(serviceId);
        apiQueryPram.setSortColumn("name");
        apiQueryPram.setSortType("asc");

        apiQueryPram.setIsDeprecated(1);
        List<DocApi> deprecatedApis = apiDao.queryAllByServiceId(apiQueryPram);

        apiQueryPram.setIsDeprecated(0);
        List<DocApi> apis = apiDao.queryAllByServiceId(apiQueryPram);

        if (CollectionUtils.isNotEmpty(deprecatedApis)) {
            navBarContext.setDeprecatedApis(deprecatedApis);
        }
        if (CollectionUtils.isNotEmpty(apis)) {
            navBarContext.setApis(apis);
        }

    }

    private void processService(NavBarContext navBarContext) {
        DocService service = serviceDao.selectByPrimaryKey(navBarContext.getServiceId());
        Integer versionId = service.getAppVersionId();
        DocAppVersion version = appVersionDao.selectByPrimaryKey(versionId);
        navBarContext.setAppId(version.getAppId());
        ServiceQueryParam serviceQueryPram = new ServiceQueryParam();
        serviceQueryPram.setAppVersionId(versionId);
        serviceQueryPram.setSortColumn("full_name");
        serviceQueryPram.setSortType("asc");

        serviceQueryPram.setIsDeprecated(1);
        List<DocService> deprecatedServices = serviceDao.queryAllByAppVersionId(serviceQueryPram);

        serviceQueryPram.setIsDeprecated(0);
        List<DocService> services = serviceDao.queryAllByAppVersionId(serviceQueryPram);

        if (CollectionUtils.isNotEmpty(deprecatedServices)) {
            navBarContext.setDeprecatedServices(deprecatedServices);
        }
        if (CollectionUtils.isNotEmpty(services)) {
            navBarContext.setServices(services);
        }
    }

}
