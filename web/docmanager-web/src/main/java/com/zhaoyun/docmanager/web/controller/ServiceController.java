/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.zhaoyun.docmanager.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyun.docmanager.biz.service.AppService;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;

/**
 * 
 * @author user
 * @version $Id: ServiceController.java, v 0.1 2016年8月16日 上午11:46:41 user Exp $
 */
@Controller
@RequestMapping("/service")
public class ServiceController extends BaseController {
    @Resource
    private AppService appService;

    /**  
     * 根据版本查询服务列表
     * @param serviceQueryParam
     * @return
     * @date: 2016年8月16日 下午2:45:15
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Result<List<DocService>> queryList(@Valid @RequestBody ServiceQueryParam serviceQueryParam) {
        List<DocService> allService = appService.queryAllServiceByParam(serviceQueryParam);
        return Results.newSuccessResult(allService);
    }
}
