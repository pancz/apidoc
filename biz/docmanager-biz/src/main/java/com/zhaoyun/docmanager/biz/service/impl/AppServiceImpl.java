package com.zhaoyun.docmanager.biz.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import com.zhaoyun.docmanager.biz.DiffUtil;
import com.zhaoyun.docmanager.biz.service.AppService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.biz.exception.DocException;
import com.zhaoyun.docmanager.common.commons.objects.ObjectConverter;
import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;
import com.zhaoyun.docmanager.common.util.CollectionUtil;
import com.zhaoyun.docmanager.common.util.StringUtil;
import com.zhaoyun.docmanager.core.apiparse.APIParse;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.Dependency;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.apiparse.entity.MethodParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.MqMessageObj;
import com.zhaoyun.docmanager.core.apiparse.support.PomConvert;
import com.zhaoyun.docmanager.core.lucene.IndexSearch;
import com.zhaoyun.docmanager.core.lucene.SearchIndexBean;
import com.zhaoyun.docmanager.core.util.VersionCompareUtil;
import com.zhaoyun.docmanager.dal.dao.ext.DocApiDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppMessageDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppVersionDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocServiceDao;
import com.zhaoyun.docmanager.model.dvo.AppAddPramDO;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocAppMessage;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import com.zhaoyun.docmanager.model.param.AppMessageQueryParam;
import com.zhaoyun.docmanager.model.param.AppMessageSaveParam;
import com.zhaoyun.docmanager.model.param.AppQueryParam;
import com.zhaoyun.docmanager.model.param.AppVersionAddParam;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;
import com.zhaoyun.docmanager.model.vo.DocApiVo;
import com.zhaoyun.docmanager.model.vo.DocAppMessageVo;
import com.zhaoyun.docmanager.model.vo.DocAppVersionVo;
import com.zhaoyun.docmanager.model.vo.DocAppVo;
import com.zhaoyun.docmanager.model.vo.DocServiceVo;
import com.zhaoyun.docmanager.model.vo.MqMessageVo;
import com.zhaoyun.docmanager.model.vo.PageResult;

/**
 * Created on 2016/8/5 15:27 17:27.
 * 未来不确定才更值得前行
 *
 * @author user
 */
@Service("appService")
public class AppServiceImpl implements AppService {
    protected static Logger     LOGGER   = LoggerFactory.getLogger(AppServiceImpl.class);
    private String              basePath = SpringConfig.get("LOCAL_JAR_ROOT_PATH");
    @Resource
    private DocServiceDao       docServiceDao;
    @Resource
    private DocApiDao           docApiDao;
    @Resource
    private DocAppVersionDao    docAppVersionDao;
    @Resource
    private DocAppDao           docAppDao;
    @Resource
    private DocAppMessageDao    docAppMessageDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    @Transactional
    public void addPom(String groupId, String artifactId, String version, Integer appId,
                       String versionDesc) {

        LOGGER.info("START addPom. groupId:{},artifactId:{},version:{},appId:{}", groupId,
            artifactId, version, appId);
        if (StringUtil.isEmpty(version)) {
            version = APIParse.getLatestVersion(groupId, artifactId, false);
        }
        Dependency dp = new Dependency(groupId, artifactId, version);
        List<ClassParsedResult> list = APIParse.getJDTParsedResult(dp.toPom(), basePath);
        for (ClassParsedResult classParsedResult : list) {
            try {
                APIParse.getReflectParsedResult(classParsedResult, dp.toPom(), basePath);
            } catch (Throwable e) {
                LOGGER.error("++++++ parse_error:" + classParsedResult.getClassQuaName() + "("
                             + classParsedResult.getMethodList().size() + ")" + " +++++++ " + e);
            }
        }
        // 清理jar包的源url地址，防止下次仍然读取老路径，读不到新包的路径
        PomConvert.clearSourceAndClassPath();
        DocAppVersion appVersion = new DocAppVersion();
        appVersion.setAppId(appId);
        appVersion.setVersion(version);
        appVersion.setDesc(versionDesc);
        appVersion.setIsDefault(1);
        docAppVersionDao.insertSelective(appVersion);
        for (ClassParsedResult classParsedResult : list) {
            DocService dsModel = new DocService();
            dsModel.setAppVersionId(appVersion.getId());
            dsModel.setAuthor(classParsedResult.getAuthor());
            dsModel.setClzVersion(classParsedResult.getVersion());
            dsModel.setDesc(classParsedResult.getDesc());
            dsModel.setFullName(classParsedResult.getClassQuaName());
            dsModel.setIsDeprecated(classParsedResult.isDeprecated() ? 1 : 0);
            docServiceDao.insertSelective(dsModel);
            List<DocApi> infoList = new ArrayList<>();
            boolean svcDeprecated = classParsedResult.isDeprecated();
            for (MethodParsedResult mp : classParsedResult.getMethodList()) {
                DocApi daModel = new DocApi();
                daModel.setFullName(APIParse.getApiFullName(mp));
                daModel.setDesc(mp.getMethodComment());
                daModel.setName(mp.getMethodName());
                daModel.setParamIn(JSONObject.toJSONString(mp.getParamList()));
                daModel.setParamOut(JSONObject.toJSONString(mp.getReturnObj()));
                daModel.setServiceId(dsModel.getId());
                daModel.setSign(toSign(mp));
                daModel.setIsDeprecated(svcDeprecated || mp.isDeprecated() ? 1 : 0);
                daModel.setChName(mp.getNameComment());
                daModel.setAuthor(mp.getAuthorComment());
                daModel.setDate(mp.getDateComment());
                daModel.setFullComment(mp.getFullComment());
                docApiDao.insertSelective(daModel);
                infoList.add(daModel);
            }
            /*  lucene
            insertSearchIndex(dsModel, appVersion.getId());
              insertSearchIndex(infoList, dsModel, appVersion.getId());*/
        }
        LOGGER.info("END addPom. groupId:{},artifactId:{},version:{},appId:{}", groupId,
            artifactId, version, appId);
    }

    private void insertSearchIndex(DocService dsModel, int pomId) {
        SearchIndexBean b = new SearchIndexBean();
        b.setKeyId(dsModel.getId());
        b.setKeyType(1);
        b.setServiceDesc(dsModel.getDesc());
        b.setServiceName(dsModel.getFullName());
        b.setApiDesc("");
        b.setApiName("");
        b.setPomId(pomId);
        List<SearchIndexBean> beanList = new ArrayList<>();
        beanList.add(b);
        IndexSearch.addIndex(beanList);
    }

    private void insertSearchIndex(List<DocApi> l, DocService dsModel, int pomId) {
        List<SearchIndexBean> beanList = new ArrayList<>();
        for (DocApi diModel : l) {
            SearchIndexBean b = new SearchIndexBean();
            b.setApiDesc(diModel.getDesc());
            b.setServiceName(dsModel.getFullName());
            b.setServiceDesc(dsModel.getDesc());
            b.setKeyType(2);
            b.setKeyId(diModel.getId());
            b.setApiName(diModel.getDesc());
            b.setPomId(pomId);
            beanList.add(b);
        }
        IndexSearch.addIndex(beanList);

    }

    /**
     * @see AppService#addApp(AppAddPramDO)
     */
    @Override
    public void addApp(AppAddPramDO appAddPramDO) {
        docAppDao.addApp(appAddPramDO);
    }

    /**
     * @see AppService#queryAppVersionByPage(AppVersionQueryParam)
     */
    @Override
    public PageResult<DocAppVersion> queryAppVersionByPage(AppVersionQueryParam appVersionQueryPram) {
        PageResult<DocAppVersion> page = new PageResult<DocAppVersion>();
        int total = docAppVersionDao.queryCount(appVersionQueryPram);
        if (total == 0) {
            return page;
        }
        int pageSize = appVersionQueryPram.getPageSize();

        int pages = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
        List<DocAppVersion> data = docAppVersionDao.queryList(appVersionQueryPram);

        page.setTotal(total);
        page.setPages(pages);
        page.setData(data);
        return page;
    }

    /**
     * @see AppService#queryAllServiceByParam(ServiceQueryParam)
     */
    @Override
    public List<DocService> queryAllServiceByParam(ServiceQueryParam serviceQueryPram) {
        //用默认版本查询
        if (serviceQueryPram.getAppId() != null && serviceQueryPram.getAppVersionId() == null) {
            DocAppVersion version = queryDefaultVersionByAppId(serviceQueryPram.getAppId());
            if (version == null) {
                return Lists.newArrayList();
            } else {
                serviceQueryPram.setAppVersionId(version.getId());
            }
        }
        return docServiceDao.queryAllByAppVersionId(serviceQueryPram);
    }

    /**
     * @see AppService#queryAllApiBaseInfoByService(ApiQueryParam)
     */
    @Override
    public List<DocApi> queryAllApiBaseInfoByService(ApiQueryParam apiQueryPram) {
        return docApiDao.queryAllByServiceId(apiQueryPram);
    }

    /**
     * @see AppService#queryAllApp(AppQueryParam)
     */
    @Override
    public List<DocApp> queryAllApp(AppQueryParam appQueryPram) {
        return docAppDao.queryAll(appQueryPram);
    }

    /**
     * @see AppService#addVersion(AppVersionAddParam)
     */
    public void addVersion(final AppVersionAddParam appVersionAddParam) {
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transtatus) {
                try {
                    //是否已存在version
                    AppQueryParam appQueryPram = new AppQueryParam();
                    List<DocApp> allApp = queryAllApp(appQueryPram);
                    DocApp doc = null;
                    for (DocApp app : allApp) {
                        if (StringUtils.equals(app.getName().trim(), appVersionAddParam.getName()
                            .trim())) {
                            doc = app;
                            break;
                        }
                    }
                    AppVersionQueryParam appVersionQueryParam = new AppVersionQueryParam();
                    appVersionQueryParam.setAppId(doc.getId());
                    appVersionQueryParam.setVersion(appVersionAddParam.getVersion());
                    List<DocAppVersion> docAppVersionList = docAppVersionDao
                        .queryList(appVersionQueryParam);

                    if (CollectionUtil.isNotEmpty(docAppVersionList)) {
                        // 删除
                        DocAppVersion docAppVersion = docAppVersionList.get(0);
                        ServiceQueryParam serviceQueryParam = new ServiceQueryParam();
                        serviceQueryParam.setAppVersionId(docAppVersion.getId());
                        List<DocService> docServiceList = docServiceDao
                            .queryAllByAppVersionId(serviceQueryParam);
                        for (DocService docService : docServiceList) {
                            ApiQueryParam apiQueryParam = new ApiQueryParam();
                            apiQueryParam.setServiceId(docService.getId());
                            List<DocApi> docApiList = docApiDao.queryAllByServiceId(apiQueryParam);
                            if (CollectionUtil.isNotEmpty(docApiList)) {
                                docApiDao.batchDeleteById(docApiList);
                            }
                        }
                        if (CollectionUtil.isNotEmpty(docServiceList)) {
                            docServiceDao.batchDeleteById(docServiceList);
                        }
                        docAppVersionDao.deleteByPrimaryKey(docAppVersion.getId());
                    }

                    //加载POM
                    addPom(doc.getGroupId(), doc.getArtifactId(), appVersionAddParam.getVersion(),
                        doc.getId(), appVersionAddParam.getDesc());
                } catch (Exception e) {
                    transtatus.setRollbackOnly();
                    LOGGER.error("", e);
                    throw e;
                }
                return null;
            }
        });
    }

    /**
     * @see AppService#queryAllApiFullInfoByService(ApiQueryParam)
     */
    @Override
    public List<DocApi> queryAllApiFullInfoByService(ApiQueryParam apiQueryPram) {
        return docApiDao.queryAllWithBLOBsByServiceId(apiQueryPram);
    }

    /**
     * @see AppService#queryApiFullInfoById(java.lang.Integer)
     */
    @Override
    public DocApiVo queryApiFullInfoById(Integer id) {
        DocApi docApi = docApiDao.selectByPrimaryKey(id);
        DocApiVo docAppVo = ObjectConverter.convertObject(docApi, DocApiVo.class);
        String paramIn = docApi.getParamIn();
        List<FieldsObj> inList = JSON.parseObject(paramIn, new ParameterizedTypeImpl(
            new Type[] { FieldsObj.class }, List.class, List.class));
        for (int i = 0; i < inList.size(); i++) {
            FieldsObj o = inList.get(i);
            o = DiffUtil.processAll(true, o);
            inList.set(i, o);
        }
        docAppVo.setParamIn(JSON.toJSONString(inList));
        String paramOut = docApi.getParamOut();
        FieldsObj outObj = JSON.parseObject(paramOut, FieldsObj.class);
        outObj = DiffUtil.processAll(true, outObj);
        docAppVo.setParamOut(JSON.toJSONString(outObj));
        return docAppVo;
    }

    private String toSign(MethodParsedResult mp) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(mp.getMethodName());
        List<FieldsObj> paramList = mp.getParamList();
        sb.append(field2String(paramList));
        FieldsObj returnObj = mp.getReturnObj();
        List<FieldsObj> fieldsObjs = new ArrayList<>();
        fieldsObjs.add(returnObj);
        sb.append(field2String(fieldsObjs));
        sb.append(mp.getFullComment());
        return StringUtil.md5Encrypt(sb.toString());
    }

    private String field2String(List<FieldsObj> objList) {
        if (CollectionUtil.isEmpty(objList)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(512);
        Collections.sort(objList, new Comparator<FieldsObj>() {
            @Override
            public int compare(FieldsObj o1, FieldsObj o2) {
                String s1 = o1.getClassType() + o1.getFieldName();
                String s2 = o2.getClassType() + o1.getFieldName();
                return s1.compareTo(s2);
            }
        });
        for (FieldsObj o : objList) {
            if (null == o) {
                continue;
            }
            sb.append(o.getClassType());
            sb.append(o.getFieldName());
            List<FieldsObj> fieldObjs = o.getFieldObjs();
            sb.append(field2String(fieldObjs));
        }
        return sb.toString();
    }

    /**
     * @see AppService#queryApiFullInfoById(java.lang.Integer)
     */
    @Override
    public DocAppVo queryAppFullInfoById(Integer id) {
        DocApp docApp = docAppDao.selectByPrimaryKey(id);
        DocAppVo docAppVo = ObjectConverter.convertObject(docApp, DocAppVo.class);
        AppMessageQueryParam appMessageQueryParam = new AppMessageQueryParam();
        appMessageQueryParam.setAppId(id);
        appMessageQueryParam.setSortColumn("topic");
        appMessageQueryParam.setSortType("asc");
        List<DocAppMessage> docAppMessages = docAppMessageDao.queryAllByAppId(appMessageQueryParam);
        List<DocAppMessageVo> docAppMessageVos = ObjectConverter.convertList(docAppMessages,
            DocAppMessageVo.class);
        docAppVo.setDocAppMessageVos(docAppMessageVos);

        DocAppVersion docAppVersion = queryDefaultVersionByAppId(id);
        DocAppVersionVo docAppVersionVo = ObjectConverter.convertObject(docAppVersion,
            DocAppVersionVo.class);
        if (docAppVersionVo != null) {
            docAppVo.setDefaultVersionVo(docAppVersionVo);
            ServiceQueryParam serviceQueryPram = new ServiceQueryParam();
            serviceQueryPram.setAppVersionId(docAppVersionVo.getId());
            serviceQueryPram.setSortColumn("is_deprecated,full_name");
            serviceQueryPram.setSortType("asc");
            List<DocService> docServices = docServiceDao.queryAllByAppVersionId(serviceQueryPram);
            List<DocServiceVo> docServiceVos = ObjectConverter.convertList(docServices,
                DocServiceVo.class);
            if (CollectionUtils.isNotEmpty(docServiceVos)) {
                docAppVersionVo.setDocServiceVos(docServiceVos);
                for (DocServiceVo docServiceVo : docServiceVos) {
                    ApiQueryParam apiQueryPram = new ApiQueryParam();
                    apiQueryPram.setServiceId(docServiceVo.getId());
                    apiQueryPram.setSortColumn("is_deprecated,name");
                    apiQueryPram.setSortType("asc");
                    List<DocApi> docApis = docApiDao.queryAllByServiceId(apiQueryPram);
                    List<DocApiVo> docApiVos = ObjectConverter.convertList(docApis, DocApiVo.class);
                    if (CollectionUtils.isNotEmpty(docApiVos)) {
                        docServiceVo.setDocApiVos(docApiVos);
                    }
                }
            }
        }
        return docAppVo;
    }

    /**
     * 根据versionId查询版本信息
     * @param versionId
     * @return
     * @date: 2016年8月29日 下午8:58:46
     */
    @Override
    public DocAppVersion queryVersionBaseInfoById(Integer versionId) {
        return docAppVersionDao.selectByPrimaryKey(versionId);
    }

    /**
     * @see AppService#queryDefaultVersionByAppId(java.lang.Integer)
     */
    @Override
    @Transactional
    public DocAppVersion queryDefaultVersionByAppId(Integer appId) {
        AppVersionQueryParam appVersionQueryPram = new AppVersionQueryParam();
        appVersionQueryPram.setAppId(appId);
        appVersionQueryPram.setSortColumn("version");
        appVersionQueryPram.setSortType("desc");
        appVersionQueryPram.setIsDefault(1);
        DocAppVersion version = null;
        List<DocAppVersion> versions = docAppVersionDao.queryList(appVersionQueryPram);
        Collections.sort(versions, new Comparator<DocAppVersion>() {
            @Override
            public int compare(DocAppVersion o1, DocAppVersion o2) {
                return -VersionCompareUtil.compare(o1.getVersion(), o2.getVersion());
            }
        });
        //为空则取最高版本
        if (CollectionUtils.isEmpty(versions)) {
            appVersionQueryPram.setIsDefault(0);
            versions = docAppVersionDao.queryList(appVersionQueryPram);
            //如果没有任何版本则返回空
            if (CollectionUtils.isEmpty(versions)) {
                return null;
            }
            //取最高版本
            else {
                Collections.sort(versions, new Comparator<DocAppVersion>() {
                    @Override
                    public int compare(DocAppVersion o1, DocAppVersion o2) {
                        return -VersionCompareUtil.compare(o1.getVersion(), o2.getVersion());
                    }
                });
                version = versions.get(0);
                version.setIsDefault(1);
                docAppVersionDao.updateByPrimaryKey(version);
                return version;
            }
        }
        //有多个默认版本则取最高版本
        else if (versions.size() > 1) {
            version = versions.get(0);
            versions.remove(0);
            for (DocAppVersion docAppVersion : versions) {
                docAppVersion.setIsDefault(0);
                docAppVersionDao.updateByPrimaryKey(docAppVersion);
            }
            return version;
        }
        //正常情况
        else {
            version = versions.get(0);
        }
        return version;
    }

    /**
     * @see AppService#saveAppMessageList(AppMessageSaveParam)
     */
    @Override
    @Transactional
    public void saveAppMessageList(AppMessageSaveParam appMessageUpdateParam) {
        if (CollectionUtils.isNotEmpty(appMessageUpdateParam.getUpdateList())) {
            docAppMessageDao.batchReplace(appMessageUpdateParam.getUpdateList());
        }
        if (CollectionUtils.isNotEmpty(appMessageUpdateParam.getDeleteIds())) {
            docAppMessageDao.batchDelete(appMessageUpdateParam.getDeleteIds());
        }
    }

    /**
     * @see AppService#setDefaultVersion(java.lang.Integer, java.lang.String)
     */
    @Override
    public void setDefaultVersion(Integer appId, String version) {
        AppVersionQueryParam appVersionQueryPram = new AppVersionQueryParam();
        appVersionQueryPram.setAppId(appId);
        appVersionQueryPram.setSortColumn("version");
        appVersionQueryPram.setSortType("desc");

        //1.将其他版本置为非默认
        List<DocAppVersion> versions = docAppVersionDao.queryList(appVersionQueryPram);
        if (CollectionUtils.isNotEmpty(versions)) {
            for (DocAppVersion docAppVersion : versions) {
                docAppVersion.setIsDefault(0);
                docAppVersionDao.updateByPrimaryKey(docAppVersion);
            }
        }
        appVersionQueryPram.setVersion(version);
        versions = docAppVersionDao.queryList(appVersionQueryPram);

        //2.不为空则置为默认版本
        if (CollectionUtils.isNotEmpty(versions)) {
            DocAppVersion appVersion = versions.get(0);
            appVersion.setIsDefault(1);
            docAppVersionDao.updateByPrimaryKey(appVersion);
        }
    }

    /**
     * @see AppService#queryAllVersionStr(AppVersionQueryParam)
     */
    @Override
    public List<String> queryAllVersionStr(AppVersionQueryParam appVersionQueryParam) {
        return docAppVersionDao.queryAllVersionStr(appVersionQueryParam);
    }

    @Override
    public MqMessageVo<FieldsObj> queryAllMqInfo() {
        MqMessageObj obj = APIParse.parseAllMqMessageInfo(basePath);
        // 清理jar包的源url地址，防止下次仍然读取老路径，读不到新包的路径
        PomConvert.clearSourceAndClassPath();
        MqMessageVo<FieldsObj> vo = new MqMessageVo<>();
        vo.setMessageTypeList(obj.getMessageTypeList());
        vo.setTopicList(obj.getTopicList());
        vo.setMessageEntityList(obj.getMessageEntityList());
        return vo;
    }

    /**
     * @see AppService#queryAllMessageByAppId(java.lang.Integer)
     */
    @Override
    public List<DocAppMessage> queryAllMessageByAppId(Integer appId) {
        AppMessageQueryParam appMessageQueryParam = new AppMessageQueryParam();
        appMessageQueryParam.setAppId(appId);
        appMessageQueryParam.setSortColumn("topic");
        appMessageQueryParam.setSortType("asc");
        return docAppMessageDao.queryAllWithBLOBsByAppId(appMessageQueryParam);
    }

    /**
     * @see AppService#queryMessageById(java.lang.Integer)
     */
    @Override
    public DocAppMessage queryMessageById(Integer id) {
        return docAppMessageDao.selectByPrimaryKey(id);
    }

    /** 
     * @see AppService#queryAppBaseInfoById(java.lang.Integer)
     */
    @Override
    public DocAppVo queryAppBaseInfoById(Integer id) {
        return ObjectConverter.convertObject(docAppDao.selectByPrimaryKey(id), DocAppVo.class);
    }

    @Override
    public DocServiceVo queryServiceBaseInfoById(Integer id) {
        return ObjectConverter.convertObject(docServiceDao.selectByPrimaryKey(id),
            DocServiceVo.class);
    }

    /** 
     * @see AppService#parseSpecifiedClass(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public FieldsObj parseSpecifiedClass(String groupId, String artifactId, String className) {
        try {
            return APIParse.parseSpecifiedClass(groupId, artifactId, basePath, className);
        } catch (Exception e) {
            LOGGER.warn("", e);
            throw new DocException(false, CommonStateCode.PARSE_ERROR, "parseSpecifiedClass error");
        }
    }

}