package com.zhaoyun.docmanager.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.biz.DiffUtil;
import com.zhaoyun.docmanager.biz.bean.diff.ApiDiff;
import com.zhaoyun.docmanager.biz.bean.diff.FieldDiff;
import com.zhaoyun.docmanager.biz.bean.diff.OfClass;
import com.zhaoyun.docmanager.biz.bean.diff.PomDiff;
import com.zhaoyun.docmanager.biz.service.APIComparatorService;
import com.zhaoyun.docmanager.common.util.CollectionUtil;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.util.StringUtil;
import com.zhaoyun.docmanager.dal.dao.ext.DocApiDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppVersionDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocServiceDao;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 2016/8/16 18:04.
 * 未来不确定才更值得前行
 *
 * @author user
 */
@Service
public class APIComparator implements APIComparatorService {
    protected static Logger LOGGER = LoggerFactory.getLogger(APIComparator.class);
    @Resource
    private DocServiceDao serviceDao;
    @Resource
    private DocApiDao apiDao;
    @Resource
    private DocAppVersionDao versionDao;
    @Resource
    private DocAppDao appDao;
    @Qualifier("auxThreadPool")
    @Autowired
    private ThreadPoolExecutor auxThreadPool;

    private List<ApiDiff> getApiDiffListFromDocService(DocService docservice, boolean isDel) {
        List<ApiDiff> list = new ArrayList<>();
        if (null == docservice) {
            return list;
        }
        ApiQueryParam apiQueryParam = new ApiQueryParam();
        apiQueryParam.setServiceId(docservice.getId());
        List<DocApi> docApiList = apiDao.queryAllWithBLOBsByServiceId(apiQueryParam);
        return getDiffFieldListFromApi(docApiList, docservice.getFullName(), isDel);
    }

    private List<ApiDiff> getDiffFieldListFromApi(Collection<DocApi> docApiList, String serviceName, boolean isDel) {
        List<ApiDiff> list = new ArrayList<>();
        for (DocApi api : docApiList) {
            if (null == api) {
                continue;
            }
            ApiDiff apiDiff = new ApiDiff();
            apiDiff.setApiName(api.getName());
            apiDiff.setServiceName(StringUtil.isEmpty(serviceName) ? "" : serviceName.substring(serviceName.lastIndexOf(".") + 1));
            apiDiff.setDesc1(api.getDesc());
            if (!isDel) {
                apiDiff.setDesc2(api.getDesc());
            }
            String paramIn = api.getParamIn();
            if (StringUtil.isNotEmpty(paramIn)) {
                List<FieldsObj> inList = JSON.parseObject(paramIn, new ParameterizedTypeImpl(new Type[]{FieldsObj.class}, List.class, List.class));
                List<FieldDiff> inDiffList = getDiffFieldList(inList, isDel);
                apiDiff.setParamIn(inDiffList);
            }
            String paramOut = api.getParamOut();
            if (StringUtil.isNotEmpty(paramOut) && !"null".equals(paramOut)) {
                FieldsObj fieldsObj = JSON.parseObject(paramOut, FieldsObj.class);
                List<FieldsObj> outList = new ArrayList<>();
                outList.add(fieldsObj);
                List<FieldDiff> diffFieldList = getDiffFieldList(outList, isDel);
                FieldDiff out = diffFieldList.get(0);
/*                if (out.getType1() != null && out.getType1().startsWith("com.zhaoyun.knife.result.Result")) {
                    out = out.getChildList().get(0);
                }*/
                apiDiff.setParamOut(out);
            }
            list.add(apiDiff);
        }
        return list;
    }

    private List<FieldDiff> getDiffFieldList(Collection<FieldsObj> objList, boolean isDel) {
        List<FieldDiff> diffList = new ArrayList<>();
        for (FieldsObj obj : objList) {
            if (null == obj || (Objects.equals("com.zhaoyun.knife.result.Result", obj.getOfClassType()) && !"data".equals(obj.getFieldName()))) {
                continue;
            }
            obj = DiffUtil.removeResult(obj);
            DiffUtil.processFX(obj);
            DiffUtil.toSimplyName(obj);
            FieldDiff diff = new FieldDiff();
            if (!isDel) {
                diff.setDesc2(obj.getFieldComment());
                diff.setType2(StringUtil.isEmpty(obj.getClassType()) ? obj.getClassType() : obj.getClassType());
            } else {
                diff.setDesc1(obj.getFieldComment());
                diff.setType1(StringUtil.isEmpty(obj.getClassType()) ? obj.getClassType() : obj.getClassType());
            }
            diff.setName(obj.getFieldName());
            List<FieldsObj> childList = obj.getFieldObjs();
            if (CollectionUtil.isNotEmpty(childList)) {
                List<FieldDiff> list = getDiffFieldList(childList, isDel);
                diff.setChildList(list);
            }
            diffList.add(diff);
        }
        return diffList;
    }


    @Override
    public PomDiff diffPom(Integer appId, String v1, String v2) {
        PomDiff pomDiff = new PomDiff();
        AppVersionQueryParam queryParam = new AppVersionQueryParam();
        queryParam.setVersion(v1);
        queryParam.setAppId(appId);
        DocApp app = appDao.selectByPrimaryKey(appId);
        pomDiff.setAppName(app.getName());
        List<DocAppVersion> docAppVersionList1 = versionDao.queryList(queryParam);
        queryParam.setVersion(v2);
        List<DocAppVersion> docAppVersionList2 = versionDao.queryList(queryParam);
        if (CollectionUtil.isEmpty(docAppVersionList1) || CollectionUtil.isEmpty(docAppVersionList2)) {
            LOGGER.warn("diff not exist pom version: {} and {}", v1, v2);
            return null;
        }
        DocAppVersion dav1 = docAppVersionList1.get(0);
        DocAppVersion dav2 = docAppVersionList2.get(0);
        pomDiff.setVersion1(v1);
        pomDiff.setVersion2(v2);
        ServiceQueryParam queryServiceParam = new ServiceQueryParam();
        queryServiceParam.setAppVersionId(dav1.getId());
        List<DocService> serviceList1 = serviceDao.queryAllByAppVersionId(queryServiceParam);
        queryServiceParam.setAppVersionId(dav2.getId());
        List<DocService> serviceList2 = serviceDao.queryAllByAppVersionId(queryServiceParam);
        Map<String, DocService> svcMap1 = docSvcList2Map(serviceList1);
        Map<String, DocService> svcMap2 = docSvcList2Map(serviceList2);
        List<DocService> removeList = new ArrayList<>();
        final List<ApiDiff> diffList = new ArrayList<>();
        final List<ApiDiff> addList = new ArrayList<>();
        final List<ApiDiff> delList = new ArrayList<>();
        List<ImmutablePair<DocService, DocService>> toDiffSvcList = new ArrayList<>();
        Iterator<Map.Entry<String, DocService>> ite = svcMap1.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, DocService> en = ite.next();
            String svcName = en.getKey();
            DocService svc = svcMap2.get(svcName);
            if (null == svc) {
                removeList.add(en.getValue());
                ite.remove();
                continue;
            }
            toDiffSvcList.add(new ImmutablePair<>(en.getValue(), svc));
            ite.remove();
            svcMap2.remove(svcName);
        }
        Collection<DocService> addServiceList = svcMap2.values();
        int count = addServiceList.size() + removeList.size() + toDiffSvcList.size();
        final CountDownLatch cdl = new CountDownLatch(count);
        if (CollectionUtil.isNotEmpty(toDiffSvcList)) {
            for (final ImmutablePair<DocService, DocService> pair : toDiffSvcList) {
                auxThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<ApiDiff> apiDiffList = diffService(pair.getLeft(), pair.getRight());
                            for (ApiDiff diff : apiDiffList) {
                                if (Objects.equals(FieldDiff.DiffType.ADD.getType(), diff.getDiffType())) {
                                    addList.add(diff);
                                } else if (Objects.equals(FieldDiff.DiffType.DEL.getType(), diff.getDiffType())) {
                                    delList.add(diff);
                                } else if (Objects.equals(FieldDiff.DiffType.CHANGE.getType(), diff.getDiffType())) {
                                    diffList.add(diff);
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error("", e);
                        } finally {
                            cdl.countDown();
                        }
                    }
                });
            }
        }
        if (CollectionUtil.isNotEmpty(addServiceList)) {
            for (final DocService ds : addServiceList) {
                auxThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<ApiDiff> apiDiffList = getApiDiffListFromDocService(ds, false);
                            addList.addAll(apiDiffList);
                        } catch (Exception e) {
                            LOGGER.error("", e);
                        } finally {
                            cdl.countDown();
                        }
                    }
                });
            }
        }
        if (CollectionUtil.isNotEmpty(removeList)) {
            for (final DocService ds : removeList) {
                auxThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<ApiDiff> apiDiffList = getApiDiffListFromDocService(ds, true);
                            delList.addAll(apiDiffList);
                        } catch (Exception e) {
                            LOGGER.error("", e);
                        } finally {
                            cdl.countDown();
                        }
                    }
                });
            }
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        pomDiff.setAddApiList(addList);
        pomDiff.setDeleteApiList(delList);
        pomDiff.setChangeApiList(diffList);
        return pomDiff;
    }

    private List<ApiDiff> diffService(DocService s1, DocService s2) {
        List<ApiDiff> diffList = new ArrayList<>();
        ApiQueryParam queryApiParam = new ApiQueryParam();
        queryApiParam.setServiceId(s1.getId());
        List<DocApi> apiList1 = apiDao.queryAllWithBLOBsByServiceId(queryApiParam);
        queryApiParam.setServiceId(s2.getId());
        List<DocApi> apiList2 = apiDao.queryAllWithBLOBsByServiceId(queryApiParam);
        Map<String, DocApi> apiMap1 = docApiList2Map(apiList1);
        Map<String, DocApi> apiMap2 = docApiList2Map(apiList2);
        List<DocApi> delList = new ArrayList<>();
        Iterator<Map.Entry<String, DocApi>> ite = apiMap1.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, DocApi> en = ite.next();
            String apiName = en.getKey();
            DocApi api = apiMap2.get(apiName);
            if (null == api) {
                delList.add(en.getValue());
                ite.remove();
                continue;
            }
            ApiDiff apiDiff = diffApi(en.getValue(), api);
            if (null != apiDiff) {
                apiDiff.setServiceName(StringUtil.isEmpty(s1.getFullName()) ? "" : s1.getFullName().substring(s1.getFullName().lastIndexOf(".") + 1));
                diffList.add(apiDiff);
            }
            ite.remove();
            apiMap2.remove(apiName);
        }
        Collection<DocApi> values = apiMap2.values();
        if (CollectionUtil.isNotEmpty(values)) {
            List<ApiDiff> addList = getDiffFieldListFromApi(values, s1.getFullName(), false);
            for (ApiDiff ad : addList) {
                ad.setDiffType(FieldDiff.DiffType.ADD.getType());
            }
            diffList.addAll(addList);
        }
        List<ApiDiff> deleteList = getDiffFieldListFromApi(delList, s1.getFullName(), true);
        for (ApiDiff ad : deleteList) {
            ad.setDiffType(FieldDiff.DiffType.DEL.getType());
        }
        diffList.addAll(deleteList);
        return diffList;
    }

    private ApiDiff diffApi(DocApi api1, DocApi api2) {
        ApiDiff apiDiff = new ApiDiff();
        apiDiff.setApiName(api1.getName());
        apiDiff.setDesc1(api1.getDesc());
        apiDiff.setDesc2(api2.getDesc());
        apiDiff.setDiffType(FieldDiff.DiffType.CHANGE.getType());
        String sign1 = api1.getSign();
        String sign2 = api2.getSign();
        if (Objects.equals(sign1, sign2)) {
            return null;
        }
        String in1 = api1.getParamIn();
        String in2 = api2.getParamIn();
        List<FieldsObj> inList1 = JSON.parseObject(in1, new ParameterizedTypeImpl(new Type[]{FieldsObj.class}, List.class, List.class));
        List<FieldsObj> inList2 = JSON.parseObject(in2, new ParameterizedTypeImpl(new Type[]{FieldsObj.class}, List.class, List.class));
        List<FieldDiff> diffList = compareFieldList(inList1, inList2, null);
        apiDiff.setParamIn(diffList);
        String out1 = api1.getParamOut();
        String out2 = api2.getParamOut();
        FieldsObj outObj1 = JSON.parseObject(out1, FieldsObj.class);
        outObj1 = DiffUtil.removeResult(outObj1);
        FieldsObj outObj2 = JSON.parseObject(out2, FieldsObj.class);
        outObj2 = DiffUtil.removeResult(outObj2);
        List<FieldsObj> fieldsObjs1 = Lists.newArrayList(outObj1);
        List<FieldsObj> fieldsObjs2 = Lists.newArrayList(outObj2);
        List<FieldDiff> dList = compareFieldList(fieldsObjs1, fieldsObjs2, null);
        FieldDiff out = CollectionUtil.isEmpty(dList) ? null : dList.get(0);
/*        if (null != out && out.getType1() != null && out.getType1().startsWith("com.zhaoyun.knife.result.Result")) {
            out = out.getChildList().get(0);
        }*/
        apiDiff.setParamOut(out);
        return apiDiff;
    }


    private List<FieldDiff> compareFieldList(List<FieldsObj> list1, List<FieldsObj> list2, OfClass of) {
        List<FieldDiff> diffList = new ArrayList<>();
        Map<String, FieldsObj> inMap1 = fieldsObjList2Map(list1);
        Map<String, FieldsObj> inMap2 = fieldsObjList2Map(list2);
        Iterator<Map.Entry<String, FieldsObj>> ite = inMap1.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, FieldsObj> en = ite.next();
            String name = en.getKey();
            FieldsObj o = en.getValue();
            FieldsObj obj = inMap2.get(name);
            if (null == obj) {
                List<FieldsObj> oList = new ArrayList<>();
                oList.add(o);
                List<FieldDiff> fieldDiffList = getDiffFieldList(oList, true);
                FieldDiff diff2 = fieldDiffList.get(0);
                diff2.setDiffType(FieldDiff.DiffType.DEL.getType());
                diffList.add(diff2);
                ite.remove();
                continue;
            }
            FieldDiff df = compareField(o, obj, of);
            if (null != df) {
                diffList.add(df);
            }
            ite.remove();
            inMap2.remove(en.getKey());
        }

        for (FieldsObj o : inMap2.values()) {
            List<FieldsObj> oList = new ArrayList<>();
            oList.add(o);
            List<FieldDiff> fieldDiffList = getDiffFieldList(oList, false);
            FieldDiff diff2 = fieldDiffList.get(0);
            fillDiffType(diff2, FieldDiff.DiffType.ADD);
            diffList.add(diff2);
        }
        //排序
        Collections.sort(diffList, new Comparator<FieldDiff>() {
            @Override
            public int compare(FieldDiff o1, FieldDiff o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return diffList;
    }

    private FieldDiff compareField(FieldsObj o1, FieldsObj o2, OfClass of) {
        //java类型code（1 基本类型，2 array, 3 Collection, 4 Map, 5 自定义类型）
        if (null != of) {
            DiffUtil.processFX(o1);
            DiffUtil.processFX(o2);
        }
        FieldDiff diff = new FieldDiff();
        diff.setName(o1.getFieldName());
        diff.setType1(o1.getClassType());
        diff.setDesc1(o1.getFieldComment());
        diff.setType2(o2.getClassType());
        diff.setDesc2(o2.getFieldComment());
        if (Objects.equals(o1.getTypeCode(), 1)) {
            diff.setDiffType(FieldDiff.DiffType.NOCHANGE.getType());
            return diff;
        }

        diff.setDiffType(FieldDiff.DiffType.CHANGE.getType());
        List<FieldsObj> objList1 = o1.getFieldObjs();
        OfClass ofClass = new OfClass();
        ofClass.setFieldName(o1.getFieldName());
        ofClass.setFieldType(o1.getClassType());
        ofClass.setOfClass(of);
        List<FieldsObj> objList2 = o2.getFieldObjs();
        List<FieldDiff> diffList = compareFieldList(objList1, objList2, ofClass);
        if (CollectionUtil.isEmpty(diffList)) {
            return null;
        }
        diff.setChildList(diffList);
        return diff;
    }

    private Map<String, FieldsObj> fieldsObjList2Map(List<FieldsObj> objList) {
        Map<String, FieldsObj> map = new HashMap<>();
        if (null != objList) {
            for (FieldsObj obj : objList) {
                if (null != obj) {
                    map.put(obj.getClassType() + "_" + obj.getFieldName(), obj);
                }
            }
        }
        return map;
    }

    private Map<String, DocApi> docApiList2Map(List<DocApi> apiList) {
        Map<String, DocApi> map = new HashMap<>();
        for (DocApi da : apiList) {
            map.put(da.getName(), da);
        }
        return map;
    }

    private Map<String, DocService> docSvcList2Map(List<DocService> svcList) {
        Map<String, DocService> map = new HashMap<>();
        for (DocService svc : svcList) {
            map.put(svc.getFullName(), svc);
        }
        return map;
    }

    private void fillDiffType(FieldDiff diff, FieldDiff.DiffType type) {
        diff.setDiffType(type.getType());
        List<FieldDiff> childList = diff.getChildList();
        if (CollectionUtil.isNotEmpty(childList)) {
            for (FieldDiff d : childList) {
                fillDiffType(d, type);
            }
        }
    }

/*    private boolean needProcessFX(String className) {
        return "java.util.Map".equals(className)
                || "java.util.List".equals(className)
                || "java.util.Set".equals(className);
    }*/

}


