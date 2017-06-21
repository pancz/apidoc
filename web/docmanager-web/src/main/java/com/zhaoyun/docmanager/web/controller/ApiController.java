package com.zhaoyun.docmanager.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaoyun.docmanager.biz.bean.diff.ApiDiff;
import com.zhaoyun.docmanager.biz.bean.diff.PomDiff;
import com.zhaoyun.docmanager.biz.service.APIComparatorService;
import com.zhaoyun.docmanager.biz.service.AppService;
import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.common.util.CollectionUtil;
import com.zhaoyun.docmanager.core.util.VersionCompareUtil;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.param.AddFacadeParam;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import com.zhaoyun.docmanager.model.vo.DocApiVo;
import com.zhaoyun.docmanager.model.vo.DocAppVo;
import com.zhaoyun.docmanager.model.vo.DocServiceVo;
import com.zhaoyun.docmanager.web.controller.base.BaseController;

/**
 * Created by user on 2016/8/9 13:29.
 * 未来不确定才更值得前行
 */
@Controller
@RequestMapping("/api")
public class ApiController extends BaseController {
    protected static Logger      LOGGER = LoggerFactory.getLogger(ApiController.class);
    @Resource
    private AppService           appService;
    @Resource
    private APIComparatorService comparator;

    @RequestMapping("/facade/add")
    @ResponseBody
    public Result<String> addFacade(@Valid @RequestBody AddFacadeParam param) {
        appService.addPom(param.getGroupId(), param.getArtifactId(), param.getVersion(),
            param.getAppId(), param.getDesc());
        return Results.newSuccessResult("成功");
    }

    @RequestMapping(value = "/pom/diff", method = RequestMethod.POST)
    @ResponseBody
    public Result<PomDiff> diffPom(@RequestParam(value = "appId") Integer appId,
                                   @RequestParam("version1") String version1,
                                   @RequestParam("version2") String version2) {

        int compare = VersionCompareUtil.compare(version1, version2);
        if (compare < 0) {
            PomDiff pomDiff = comparator.diffPom(appId, version1, version2);
            return Results.newSuccessResult(pomDiff);
        } else {
            PomDiff pomDiff = comparator.diffPom(appId, version2, version1);
            return Results.newSuccessResult(pomDiff);
        }
    }

    @RequestMapping(value = "/pom/createDiffExcel", method = RequestMethod.GET)
    public void createDiffExcel(@RequestParam(value = "appId") Integer appId,
                                @RequestParam("version1") String version1,
                                @RequestParam("version2") String version2,
                                HttpServletResponse response) {

        try {
            int compare = VersionCompareUtil.compare(version1, version2);
            PomDiff pomDiff = null;
            if (compare < 0) {
                pomDiff = comparator.diffPom(appId, version1, version2);
            } else {
                pomDiff = comparator.diffPom(appId, version2, version1);
            }
            if (pomDiff != null) {
                //拿到各种差异列表
                List<ApiDiff> addApiList = pomDiff.getAddApiList();
                List<ApiDiff> changeApiList = pomDiff.getChangeApiList();
                List<ApiDiff> deleteApiList = pomDiff.getDeleteApiList();
                //创建HSSFWorkbook对象  
                HSSFWorkbook wb = new HSSFWorkbook();
                //创建HSSFSheet对象  
                HSSFSheet sheet = wb.createSheet("diff");
                //填充addApi
                if (CollectionUtil.isNotEmpty(addApiList)) {
                    //创建HSSFCell对象  
                    int size = addApiList.size();
                    for (int i = 0; i < size; i++) {
                        HSSFRow row = sheet.createRow(i);
                        HSSFCell firstCell = row.createCell(0);
                        if (i == 0) {
                            firstCell.setCellValue("增加的api共" + size);
                        }
                        row.createCell(1).setCellValue(addApiList.get(i).getServiceName() + "."
                                                       + addApiList.get(i).getApiName());
                        row.createCell(2).setCellValue(addApiList.get(i).getDesc2());
                    }
                } else {
                    HSSFRow row = sheet.createRow(0);
                    HSSFCell firstCell = row.createCell(0);
                    firstCell.setCellValue("增加的api共" + 0);
                }
                //填充changeApi
                if (CollectionUtil.isNotEmpty(changeApiList)) {
                    //创建HSSFCell对象  
                    int size = changeApiList.size();
                    for (int i = 0; i < size; i++) {
                        int addSize = addApiList.size();
                        HSSFRow row = null;
                        if (addSize == 0) {
                            row = sheet.createRow(i + 1);
                        } else {
                            row = sheet.createRow(i + addSize);
                        }
                        HSSFCell firstCell = row.createCell(0);
                        if (i == 0) {
                            firstCell.setCellValue("修改的api共" + size);
                        }
                        row.createCell(1).setCellValue(changeApiList.get(i).getServiceName() + "."
                                                       + changeApiList.get(i).getApiName());
                        row.createCell(2).setCellValue(changeApiList.get(i).getDesc2());
                    }

                } else {
                    int size = addApiList.size();
                    HSSFRow row = null;
                    if (size == 0) {
                        row = sheet.createRow(1);
                    } else {
                        row = sheet.createRow(size);
                    }
                    HSSFCell firstCell = row.createCell(0);
                    firstCell.setCellValue("修改的api共" + 0);

                }
                //填充deleteApi
                if (CollectionUtil.isNotEmpty(deleteApiList)) {
                    //创建HSSFCell对象  
                    int addSize = addApiList.size();
                    int changeSize = changeApiList.size();
                    int size = deleteApiList.size();
                    for (int i = 0; i < size; i++) {
                        HSSFRow row = null;
                        if (addSize == 0 && changeSize == 0) {
                            row = sheet.createRow(i + 2);
                        } else if (addSize == 0 && changeSize != 0) {
                            row = sheet.createRow(i + 1 + changeSize);

                        } else if (addSize != 0 && changeSize == 0) {
                            row = sheet.createRow(i + 1 + addSize);
                        } else {
                            row = sheet.createRow(i + addSize + changeSize);
                        }

                        HSSFCell firstCell = row.createCell(0);
                        if (i == 0) {
                            firstCell.setCellValue("删除的api共" + size);
                        }
                        row.createCell(1).setCellValue(deleteApiList.get(i).getServiceName() + "."
                                                       + deleteApiList.get(i).getApiName());
                        row.createCell(2).setCellValue(deleteApiList.get(i).getDesc2());
                    }

                } else {
                    int addSize = addApiList.size();
                    int changeSize = changeApiList.size();
                    HSSFRow row = null;
                    if (addSize == 0 && changeSize == 0) {
                        row = sheet.createRow(2);
                    } else if (addSize == 0 && changeSize != 0) {
                        row = sheet.createRow(1 + changeSize);

                    } else if (addSize != 0 && changeSize == 0) {
                        row = sheet.createRow(1 + addSize);
                    } else {
                        row = sheet.createRow(addSize + changeSize);
                    }

                    HSSFCell firstCell = row.createCell(0);
                    firstCell.setCellValue("删除的api共" + 0);

                }
                //输出Excel文件  
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=" + "diff.xls");
                response.getOutputStream();
                wb.write(response.getOutputStream());
                response.getOutputStream().flush();

            } else {
                //创建HSSFWorkbook对象  
                HSSFWorkbook wb = new HSSFWorkbook();
                //输出Excel文件  
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=" + "diff.xls");
                response.getOutputStream();
                wb.write(response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (Exception e) {
            LOGGER.error("生成差异文档时出错", e);
        }

    }

    /**
     * 根据服务查询api基本信息列表
     *
     * @param apiQueryParam
     * @return
     * @date: 2016年8月16日 下午2:46:57
     */
    @RequestMapping(value = "list/base")
    @ResponseBody
    public Result<List<DocApi>> queryBaseList(@Valid @RequestBody ApiQueryParam apiQueryParam) {
        List<DocApi> allApi = appService.queryAllApiBaseInfoByService(apiQueryParam);
        return Results.newSuccessResult(allApi);
    }

    /**
     * 查询api详情
     *
     * @param id
     * @return
     * @date: 2016年8月17日 下午4:35:54
     */
    @RequestMapping(value = "info")
    @ResponseBody
    public Result<DocApiVo> queryInfo(@RequestParam(value = "id") Integer id) {
        DocApiVo docApiVo = appService.queryApiFullInfoById(id);
        DocServiceVo docService = appService.queryServiceBaseInfoById(docApiVo.getServiceId());
        DocAppVersion docAppVersion = appService.queryVersionBaseInfoById(docService.getAppVersionId());
        DocAppVo docApp =  appService.queryAppBaseInfoById(docAppVersion.getAppId());
        String url = SpringConfig.get("superradar_url") + "/chain/online/methodDependencyList";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        //默认参数(除beginDen之外的参数)
        String defultParam = "?appName="+ docApp.getName() +"&methodName="+docService.getFullName()+"."+ docApiVo
                .getName()+"&endDate="+today;
/*
        Calendar calendar = Calendar.getInstance();//日历对象
        //当前日期
        calendar.setTime(new Date());
        String dayData = HttpClientUtil.get(url+defultParam+"&beginDate="+sdf.format(calendar.getTime()));
        //前一个星期
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
        String weekData = HttpClientUtil.get(url+defultParam+"&beginDate="+sdf.format(calendar.getTime()));
        //重设当前日期
        calendar.setTime(new Date());
        //前一个月
        calendar.add(Calendar.MONTH, -1);
        String monthData = HttpClientUtil.get(url+defultParam+"&beginDate="+sdf.format(calendar.getTime()));

        ChainInfoVo chainInfoVo = new ChainInfoVo();
        docApiVo.setChainInfoVo(chainInfoVo);
        Integer dayVisits = fetchVistisFromObject(dayData,"visits");
        Integer weekVisits = fetchVistisFromObject(weekData,"visits");
        Integer monthVisits = fetchVistisFromObject(monthData,"visits");

        Integer dayVisitsAvgTime = fetchVistisFromObject(dayData,"avgTime");
        Integer weekVisitsAvgTime = fetchVistisFromObject(weekData,"avgTime");
        Integer monthVisitsAvgTime = fetchVistisFromObject(monthData,"avgTime");

        //设置调用量信息
        chainInfoVo.setDayVisits(dayVisits);
        chainInfoVo.setWeekVisits(weekVisits);
        chainInfoVo.setMonthVisits(monthVisits);
        chainInfoVo.setDayVisitsAvgTime(dayVisitsAvgTime);
        chainInfoVo.setWeekVisitsAvgTime(weekVisitsAvgTime);
        chainInfoVo.setMonthVisitsAvgTime(monthVisitsAvgTime);
*/
        return Results.newSuccessResult(docApiVo);
    }

    //解析json
    private Integer fetchVistisFromObject(String jsonStr,String field){
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if(jsonObject!=null && jsonObject.containsKey("data")){
            JSONArray resultArray = (JSONArray)jsonObject.get("data");
            if(!CollectionUtil.isEmpty(resultArray)){
                JSONObject result = resultArray.getJSONObject(0);
                if(result.containsKey(field)){
                    return (Integer)result.get(field);
                }
            }

        }
        return 0;
    }
}
