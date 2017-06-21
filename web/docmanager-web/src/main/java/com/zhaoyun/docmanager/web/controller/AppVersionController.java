/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.zhaoyun.docmanager.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyun.docmanager.biz.service.AppService;
import com.zhaoyun.docmanager.biz.util.DocAssert;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.core.apiparse.APIParse;
import com.zhaoyun.docmanager.core.util.VersionCompareUtil;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.param.AppVersionAddParam;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;
import com.zhaoyun.docmanager.model.vo.PageResult;

/**
 * @author user
 * @version $Id: AppVersionController.java, v 0.1 2016年8月16日 上午11:34:25 user Exp $
 */
@Controller
@RequestMapping("/version")
public class AppVersionController extends BaseController {
    @Resource
    private AppService appService;

    /**
     * 根据应用分页查询版本
     *
     * @param appVersionQueryParam
     * @return
     * @date: 2016年8月16日 下午2:46:18
     */
    @RequestMapping(value = "page")
    @ResponseBody
    public Result<PageResult<DocAppVersion>> queryPage(@Valid @RequestBody AppVersionQueryParam appVersionQueryParam) {
        PageResult<DocAppVersion> pageResult = appService.queryAppVersionByPage(appVersionQueryParam);
        return Results.newSuccessResult(pageResult);
    }

    /**
     * 添加App
     *
     * @return
     */
    @RequestMapping("addVersion")
    @ResponseBody
    public Result<Boolean> addVersion(@Valid AppVersionAddParam appVersionAddParam) {
        DocAssert.notNull(appVersionAddParam, "appVersionAddParam can not be null");
        DocAssert.validate(appVersionAddParam);

        appService.addVersion(appVersionAddParam);

        return Results.newSuccessResult(true);
    }

    /**
     * 从maven查询所有版本
     *
     * @return
     */
    @RequestMapping("all")
    @ResponseBody
    public Result<List<String>> queryAll(@Valid AppVersionQueryParam appVersionQueryParam) {
        DocAssert.notNull(appVersionQueryParam, "appVersionQueryParam can not be null");
        DocAssert.notNull(appVersionQueryParam.getGroupId(), "groupId can not be null");
        DocAssert.notNull(appVersionQueryParam.getArtifactId(), "artifactId can not be null");

        List<String> allVersions = APIParse.allVersions(appVersionQueryParam.getGroupId(), appVersionQueryParam.getArtifactId(), false);
        allVersions.addAll(APIParse.allVersions(appVersionQueryParam.getGroupId(), appVersionQueryParam.getArtifactId(), true));
        Collections.sort(allVersions, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -VersionCompareUtil.compare(o1, o2);
            }
        });
        //过滤高版本
        if (StringUtils.isNotBlank(appVersionQueryParam.getMaxVersion())) {
            Iterator<String> it = allVersions.iterator();
            while (it.hasNext()) {
                if (VersionCompareUtil.compare(it.next(), appVersionQueryParam.getMaxVersion()) >= 0) {
                    it.remove();
                }
            }
        }

        return Results.newSuccessResult(allVersions);
    }

    /**
     * 从db查询所有版本
     *
     * @return
     */
    @RequestMapping("all/db")
    @ResponseBody
    public Result<List<String>> queryAllFromDb(@Valid AppVersionQueryParam appVersionQueryParam) {
        DocAssert.notNull(appVersionQueryParam, "appVersionQueryParam can not be null");
        DocAssert.notNull(appVersionQueryParam.getAppId(), "appId can not be null");

        List<String> allVersions = appService.queryAllVersionStr(appVersionQueryParam);
        Collections.sort(allVersions, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -VersionCompareUtil.compare(o1, o2);
            }
        });
        //过滤高版本
        if (StringUtils.isNotBlank(appVersionQueryParam.getMaxVersion())) {
            Iterator<String> it = allVersions.iterator();
            while (it.hasNext()) {
                if (VersionCompareUtil.compare(it.next(), appVersionQueryParam.getMaxVersion()) >= 0) {
                    it.remove();
                }
            }
        }

        return Results.newSuccessResult(allVersions);
    }
}
