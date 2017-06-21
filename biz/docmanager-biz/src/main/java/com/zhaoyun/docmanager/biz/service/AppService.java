package com.zhaoyun.docmanager.biz.service;

import java.util.List;

import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.model.dvo.AppAddPramDO;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocAppMessage;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import com.zhaoyun.docmanager.model.param.AppMessageSaveParam;
import com.zhaoyun.docmanager.model.param.AppQueryParam;
import com.zhaoyun.docmanager.model.param.AppVersionAddParam;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;
import com.zhaoyun.docmanager.model.vo.DocApiVo;
import com.zhaoyun.docmanager.model.vo.DocAppVo;
import com.zhaoyun.docmanager.model.vo.DocServiceVo;
import com.zhaoyun.docmanager.model.vo.MqMessageVo;
import com.zhaoyun.docmanager.model.vo.PageResult;

/**
 * Created by user on 2016/8/5 15:26.
 * 未来不确定才更值得前行
 */
public interface AppService {

    void addPom(String groupId, String artifactId, String version, Integer appId, String versionDesc);

    /**
     * 增加APP
     *
     * @param appAddPramDO
     */
    void addApp(AppAddPramDO appAddPramDO);

    /**
     * 查询全部应用 
     * @param appQueryParam 可设置排序
     * @return
     * @date: 2016年8月16日 下午2:42:26
     */
    List<DocApp> queryAllApp(AppQueryParam appQueryParam);

    /**
     * 分页查询应用版本
     * @param appVersionQueryParam
     * @return
     * @date: 2016年8月16日 下午2:42:57
     */
    PageResult<DocAppVersion> queryAppVersionByPage(AppVersionQueryParam appVersionQueryParam);

    /**
     * 根据参数查询全部服务
     * @param serviceQueryParam
     * @return
     * @date: 2016年8月16日 下午2:43:30
     */
    List<DocService> queryAllServiceByParam(ServiceQueryParam serviceQueryParam);

    /**
     * @param appVersionAddParam
     */
    void addVersion(AppVersionAddParam appVersionAddParam);

    /**
     * 根据服务查询全部api基本信息
     * @param apiQueryParam
     * @return
     * @date: 2016年8月16日 下午2:43:55
     */
    List<DocApi> queryAllApiBaseInfoByService(ApiQueryParam apiQueryParam);

    /**
     * 根据服务查询全部api完整信息
     * @param apiQueryParam
     * @return
     * @date: 2016年8月16日 下午2:44:15
     */
    List<DocApi> queryAllApiFullInfoByService(ApiQueryParam apiQueryParam);

    /**
     * 根据主键查询api完整信息
     *
     * @param id
     * @return
     * @date: 2016年8月16日 下午3:23:51
     */
    DocApiVo queryApiFullInfoById(Integer id);

    /** 
     * 根据主键查询app完整信息 
     * @param id
     * @return
     * @date: 2016年8月29日 下午8:48:38
     */
    DocAppVo queryAppFullInfoById(Integer id);

    /**
     * 根据versionId查询版本信息
     * @param versionId
     * @return
     * @date: 2016年11月15日 下午11:48:38
     */
    DocAppVersion queryVersionBaseInfoById(Integer versionId);

    /**  
     * 根据appId查询默认版本
     * @param appId
     * @return
     * @date: 2016年8月29日 下午8:58:46
     */
    DocAppVersion queryDefaultVersionByAppId(Integer appId);

    /**  
     * 保存应用的消息列表
     * @param appMessageUpdateParam
     * @date: 2016年8月30日 下午3:19:20
     */
    void saveAppMessageList(AppMessageSaveParam appMessageUpdateParam);

    /**  
     * 设置默认版本
     * @param appId
     * @param version
     * @date: 2016年8月30日 下午3:21:21
     */
    void setDefaultVersion(Integer appId, String version);

    /**  
     * 查询所有的版本字符
     * @param appVersionQueryParam
     * @return
     * @date: 2016年8月30日 下午6:32:00
     */
    List<String> queryAllVersionStr(AppVersionQueryParam appVersionQueryParam);

    /**
     * 查询所有的消息类型信息
     *
     * @return vo
     */
    MqMessageVo<FieldsObj> queryAllMqInfo();

    /**
     * 查询应用的所有消息
     *
     * @param appId
     * @return
     * @date: 2016年8月31日 上午10:06:43
     */
    List<DocAppMessage> queryAllMessageByAppId(Integer appId);

    /**
     * 查询单条消息
     *
     * @param id
     * @return
     * @date: 2016年8月31日 上午10:06:55
     */
    DocAppMessage queryMessageById(Integer id);

    /** 
     * 根据主键查询app基本信息 
     * @param id
     * @return
     * @date: 2016年8月29日 下午8:48:38
     */
    DocAppVo queryAppBaseInfoById(Integer id);
    /**
     * 根据主键查询service基本信息
     * @param id
     * @return
     * @date: 2016年11月15日 下午11:48:38
     */
    DocServiceVo queryServiceBaseInfoById(Integer id);

    /**  
     * 解析指定的类
     *
     * @name  解析指定的类
     * @param groupId
     * @param artifactId
     * @param className 类全限定名
     * @return
     * @author user
     * @date: 2016年9月19日 下午7:22:24
     */
    FieldsObj parseSpecifiedClass(String groupId, String artifactId,
                                  String className);
}
