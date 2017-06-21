/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.zhaoyun.docmanager.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.biz.exception.DocException;
import com.zhaoyun.docmanager.biz.service.AppService;
import com.zhaoyun.docmanager.biz.util.DocAssert;
import com.zhaoyun.docmanager.common.commons.objects.ObjectConverter;
import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.core.apiparse.APIParse;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.support.PomConvert;
import com.zhaoyun.docmanager.core.util.HttpClientUtil;
import com.zhaoyun.docmanager.model.dvo.AppAddPramDO;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocConsumerVo;
import com.zhaoyun.docmanager.model.param.AppAddParam;
import com.zhaoyun.docmanager.model.param.AppQueryParam;
import com.zhaoyun.docmanager.model.param.AppVersionAddParam;
import com.zhaoyun.docmanager.model.vo.DocApiVo;
import com.zhaoyun.docmanager.model.vo.DocAppVo;
import com.zhaoyun.docmanager.model.vo.DocServiceVo;

/**
 * AppController
 *
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年8月15日 下午3:21:52 $
 */
@Controller
@RequestMapping("/app")
public class AppController extends BaseController {

    @Resource
    private AppService appService;

    private String basePath = SpringConfig.get("LOCAL_JAR_ROOT_PATH");

    /**
     * 添加App
     *
     * @return
     */
    @RequestMapping("addApp")
    @ResponseBody
    public Result<Boolean> addApp(@Valid AppAddParam appAddParam) {
        DocAssert.notNull(appAddParam, "appAddPram can not be null");
        DocAssert.validate(appAddParam);

        //判断名字是否有重复
        AppQueryParam appQueryPram = new AppQueryParam();
        List<DocApp> allApp = appService.queryAllApp(appQueryPram);
        for (DocApp app : allApp) {
            if (StringUtils.equals(app.getName().trim(), appAddParam.getName().trim())) {
                throw new DocException(CommonStateCode.ILLEGAL_PARAMETER, "Duplicate app name");
            }
        }

        //insert
        AppAddPramDO appAddPramDO = ObjectConverter.convertObject(appAddParam, AppAddPramDO.class);
        appService.addApp(appAddPramDO);

        return Results.newSuccessResult(true);
    }

    /**
     * 查询全部应用
     *
     * @return
     * @date: 2016年8月16日 下午2:46:42
     */
    @RequestMapping(value = "all")
    @ResponseBody
    public Object queryAll(@Valid AppQueryParam appQueryParam, String callback) {
        List<DocApp> allApp = appService.queryAllApp(appQueryParam);
        Result<List<DocApp>> result = Results.newSuccessResult(allApp);
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
     * 查询应用详情
     *
     * @return
     * @date: 2016年8月16日 下午2:46:42
     */
    @RequestMapping(value = "info")
    @ResponseBody
    public Result<DocAppVo> queryInfo(Integer id) {
        DocAppVo docAppVo = appService.queryAppFullInfoById(id);
        return Results.newSuccessResult(docAppVo);
    }

    /**
     * 指定版本刷新
     *
     * @param appId
     * @param appName
     * @param version
     * @return
     * @date: 2016年8月30日 下午3:33:12
     */
    @RequestMapping(value = "refresh")
    @ResponseBody
    public Result<Boolean> refresh(Integer appId, String appName, String version) {
        DocAssert.notNull(appId, "appId must not be null");
        DocAssert.notNull(appName, "appName must not be null");
        DocAssert.notNull(version, "version must not be null");
        AppVersionAddParam appVersionAddParam = new AppVersionAddParam();
        appVersionAddParam.setName(appName);
        appVersionAddParam.setVersion(version);
        appService.addVersion(appVersionAddParam);
        appService.setDefaultVersion(appId, version);
        return Results.newSuccessResult(true);
    }

    /**
     * 导出调用链
     *
     * @param appId
     * @date: 2016年8月30日 下午3:33:12
     */
    @RequestMapping(value = "importConsumer")
    public void importConsumer(HttpServletRequest request, HttpServletResponse response,
                               String beginDate, String endDate, Integer appId, String[] apiIds) {
        try {
            DocAssert.notNull(appId, "appId must not be null");
            DocAssert.notNull(apiIds, "apiIds must not be null");
            List<String> apistr = Arrays.asList(apiIds);
            DocAppVo docAppVo = appService.queryAppFullInfoById(appId);
            String url = SpringConfig.get("superradar_url")
                    + "/chain/online/consumerMethodListBatch";
            JSONArray params2 = new JSONArray();
            for (DocServiceVo serviceVo : docAppVo.getDefaultVersionVo().getDocServiceVos()) {
                if (serviceVo != null && serviceVo.getDocApiVos() != null) {
                    List<DocApiVo> docApiVosEs = serviceVo.getDocApiVos();
                    for (DocApiVo docApiVo : docApiVosEs) {
                        if (apistr.contains(docApiVo.getId().toString())) {
                            JSONObject data = new JSONObject();
                            data.put("appName", docAppVo.getName());
                            data.put("methodName",
                                    serviceVo.getFullName() + "." + docApiVo.getName());
                            data.put("beginDate", beginDate);
                            data.put("endDate", endDate);
                            data.put("flag", "0");
                            params2.add(data);
                        }
                    }
                }

            }
            List<JSONArray> smallParams = splitParamToSmall(params2, 6);
            //创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            String[] headers = {"调用应用名", "调用接口名", "平均响应耗时", "总的响应耗时", "总的访问次数", "被调接口名"};
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet();
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 20);
            workbook.setSheetName(0, "调用链信息");
            // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            //封装post参数
            Map map = new HashMap<String, String>();
            int totlerow = 0;
            for (JSONArray ja : smallParams) {
                String param = JSONArray.toJSONString(ja);
                //          get请求参数需要编码
                //          param = URLEncoder.encode(param, "UTF8");
                map.put("conditions", param);
                //excel填充数据
                String str = HttpClientUtil.post(url, map);
                if (StringUtils.isBlank(str)) {
                    LOGGER.error("请求接口调用链为空", str);
                    continue;
                }
                if (str.contains("请求失败")) {
                    LOGGER.error("请求接口调用链数据失败", str);
                    continue;
                }

                JSONObject jsonObject = JSONObject.parseObject(str);
                if (jsonObject.containsKey("data")) {
                    JSONArray dataList = (JSONArray) jsonObject.get("data");
                    for (Object jo : dataList) {
                        JSONObject listandMeta = (JSONObject) jo;
                        JSONArray comsumers = (JSONArray) listandMeta.get("list");
                        JSONObject meta = (JSONObject) listandMeta.get("meta");
                        // json数组直接转换成对象数组
                        // List<DocConsumerVo> list = JSON.parseArray(comsumers.toJSONString(),DocConsumerVo.class);
                        for (int j = 0; j < comsumers.size(); j++) {
                            totlerow++;
                            HSSFRow dyrows = sheet.createRow(totlerow);
                            //json对象转换成业务Vo
                            DocConsumerVo docConsumerVo = JSONObject.toJavaObject(
                                    (JSON) comsumers.get(j), DocConsumerVo.class);
                            dyrows.createCell(0).setCellValue(docConsumerVo.getAppName());
                            dyrows.createCell(1).setCellValue(docConsumerVo.getMethodName());
                            dyrows.createCell(2).setCellValue(docConsumerVo.getAvgTime());
                            dyrows.createCell(3).setCellValue(docConsumerVo.getSumTime());
                            dyrows.createCell(4).setCellValue(docConsumerVo.getVisits());
                            dyrows.createCell(5).setCellValue((String) meta.get("methodName"));
                        }
                    }
                }
            }
            //输出Excel文件
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + "consumer.xls");
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            LOGGER.error("生成调用链文档时出错", e);
        }
    }

    private List<JSONArray> splitParamToSmall(JSONArray params2, int size) {
        List<JSONArray> result = Lists.newArrayList();
        int arrSize = params2.size() % size == 0 ? params2.size() / size : params2.size() / size
                + 1;
        if (params2 == null) {
            return result;
        }
        for (int i = 0; i < arrSize; i++) {
            JSONArray jsonArray = new JSONArray();
            //把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= params2.size() - 1) {
                    jsonArray.add(params2.get(j));
                }
            }
            result.add(jsonArray);
        }
        return result;
    }

    /**
     * 获取pom包的解析内容
     *
     * @param pomXml
     * @return
     * @author liliang
     * @date: 2017年3月13日 下午4:35:54
     */
    @RequestMapping(value = "getPomInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ClassParsedResult>> getPomInfo(@RequestParam(value = "pomXml") String pomXml) {
        List<ClassParsedResult> list = APIParse.getJDTParsedResult(pomXml, basePath);
        for (ClassParsedResult classParsedResult : list) {
            try {
                APIParse.getReflectParsedResult(classParsedResult, pomXml, basePath);
            } catch (Throwable e) {
                LOGGER.error("++++++ parse_error:" + classParsedResult.getClassQuaName() + "("
                        + classParsedResult.getMethodList().size() + ")" + " +++++++ " + e);
            }
        }
        // 清理jar包的源url地址，防止下次仍然读取老路径，读不到新包的路径
        PomConvert.clearSourceAndClassPath();
        return Results.newSuccessResult(list);
    }

}
