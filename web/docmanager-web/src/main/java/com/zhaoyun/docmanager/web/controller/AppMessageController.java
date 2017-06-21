/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyun.docmanager.biz.DiffUtil;
import com.zhaoyun.docmanager.biz.service.AppService;
import com.zhaoyun.docmanager.biz.util.DocAssert;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.model.dvo.DocAppMessage;
import com.zhaoyun.docmanager.model.param.AppMessageSaveParam;
import com.zhaoyun.docmanager.model.vo.DocAppVo;
import com.zhaoyun.docmanager.model.vo.MqMessageVo;
import com.zhaoyun.docmanager.web.controller.base.BaseController;

/**
 * @author user
 * @version $Id: AppMessageController.java, v 0.1 2016年8月31日 上午10:10:10 user Exp $
 */
@Controller
@RequestMapping("/message")
public class AppMessageController extends BaseController {

    @Resource
    private AppService appService;

    /**
     * message消息列表
     *
     * @date: 2016年8月16日 下午2:46:57
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object queryList(Integer appId,String callback) {
        DocAssert.notNull(appId, "appId can not be null");
        List<DocAppMessage> appMessages = appService.queryAllMessageByAppId(appId);
        Result<List<DocAppMessage>> result = Results.newSuccessResult(appMessages);
        if (StringUtils.isBlank(callback)) {
            //需要把result转换成字符串
            return result;
        }
        //如果字符串不为空，需要支持jsonp调用 spring4.1 以上可用
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * 保存message消息列表
     *
     * @param appMessageSaveParam
     * @return
     * @date: 2016年8月31日 上午10:17:29
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public Result<Boolean> save(@RequestBody AppMessageSaveParam appMessageSaveParam) {
        DocAssert.notNull(appMessageSaveParam, "appMessageSaveParam must not be null");
        if (CollectionUtils.isNotEmpty(appMessageSaveParam.getUpdateList())) {
            for (DocAppMessage docAppMessage : appMessageSaveParam.getUpdateList()) {
                DocAssert.hasLength(docAppMessage.getMessageObjectJson(),
                        "MessageObjectJson must not be null");
            }
        }
        appService.saveAppMessageList(appMessageSaveParam);
        return Results.newSuccessResult(true);
    }

    @RequestMapping(value = "mqMessage")
    @ResponseBody
    public Result<MqMessageVo<FieldsObj>> mqMessage() {
        MqMessageVo<FieldsObj> mqMessageVo = appService.queryAllMqInfo();
        if (mqMessageVo != null && CollectionUtils.isNotEmpty(mqMessageVo.getMessageEntityList())) {
            for (FieldsObj field : mqMessageVo.getMessageEntityList()) {
                DiffUtil.processAll(false, field);
            }
        }
        return Results.newSuccessResult(mqMessageVo);
    }

    /**
     * 查询message详情
     *
     * @param id
     * @return
     * @date: 2016年8月17日 下午4:35:54
     */
    @RequestMapping(value = "info")
    @ResponseBody
    public Result<DocAppMessage> queryInfo(@RequestParam(value = "id") Integer id) {
        DocAppMessage docAppMessage = appService.queryMessageById(id);
        return Results.newSuccessResult(docAppMessage);
    }

    /**
     * 解析指定的类
     *
     * @param appId
     * @param className
     * @return
     * @name 解析指定的类
     * @author user
     * @date: 2016年9月19日 下午7:25:03
     */
    @RequestMapping(value = "parseClass")
    @ResponseBody
    public Result<FieldsObj> parseClass(Integer appId, String className) {
        DocAppVo docApp = appService.queryAppBaseInfoById(appId);
        FieldsObj fieldsObj = appService.parseSpecifiedClass(docApp.getGroupId(),
                docApp.getArtifactId(), className);
        return Results.newSuccessResult(fieldsObj);
    }
}
