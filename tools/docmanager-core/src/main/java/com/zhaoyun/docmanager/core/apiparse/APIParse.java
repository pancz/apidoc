/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.apiparse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import com.zhaoyun.docmanager.core.apiparse.entity.Dependency;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.apiparse.parser.ReflectParser;
import com.zhaoyun.docmanager.core.exception.DocStateCode;
import com.zhaoyun.docmanager.core.util.HttpClientUtil;
import com.zhaoyun.docmanager.core.util.MyUrlClassLoader;
import com.zhaoyun.docmanager.core.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.MethodParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.MqMessageObj;
import com.zhaoyun.docmanager.core.apiparse.parser.JDTParser;
import com.zhaoyun.docmanager.core.apiparse.support.ParseClassTypeSupport;
import com.zhaoyun.docmanager.core.apiparse.support.PomConvert;
import com.zhaoyun.docmanager.core.exception.BizException;
import com.zhaoyun.docmanager.core.util.FileUtils;
import com.zhaoyun.docmanager.core.util.VersionCompareUtil;

/**
 * 类信息解析处理器
 *
 * @author user
 */
public class APIParse {

    private static final Logger logger = LoggerFactory.getLogger(APIParse.class);

    //STEP1:JDT解析
    //STEP2:反射解析
    //磁盘上保存一份jar包
    //主要是为了考虑前端解析速度，将一次反射一个jar包拆分成一次反射一个class文件
    public static List<ClassParsedResult> getJDTParsedResult(String pomXml, String workBasePath) {
        List<ClassParsedResult> parsedResultList = Lists.newArrayList();
        String sourceJarUrl = PomConvert.getSourceFileUrl(pomXml);//源码jar地址
        if (StringUtil.isEmpty(sourceJarUrl)) {
            logger.error("not exist jar source. pom:{} ", pomXml);
            return Lists.newArrayList();
        }
        String classJarFileUrl = PomConvert.getClassJarFileUrl(pomXml);//编译jar包地址
        if (StringUtil.isEmpty(classJarFileUrl)) {
            logger.error("not exist jar File. pom:{} ", pomXml);
            return Lists.newArrayList();
        }
        String sourceFileName = sourceJarUrl.substring(sourceJarUrl.lastIndexOf("/") + 1,
                sourceJarUrl.length());
        String classFileName = classJarFileUrl.substring(classJarFileUrl.lastIndexOf("/") + 1,
                classJarFileUrl.length());
        String sourceFileUnzipPath = workBasePath + StringUtil.remove(sourceFileName, ".jar");

        try {
            File sourceFile = FileUtils.downLoadFile(sourceJarUrl, workBasePath, sourceFileName);
            File classFile = FileUtils.downLoadFile(classJarFileUrl, workBasePath, classFileName);
            if (sourceFile.length() == 0 || classFile.length() == 0) {
                logger.error("target pom is empty of file");
                throw new BizException("目标文件为空");
            }
            FileUtils.deleteDir(sourceFileUnzipPath);
            List<String> sourcePaths = FileUtils.unZipFiles(new ZipFile(sourceFile),
                    sourceFileUnzipPath);
            for (String sourceAbsPath : sourcePaths) {
                //只解析facade文件
//                if (!sourceAbsPath.endsWith("Facade.java") && !sourceAbsPath.endsWith("Service.java")) {
//                    continue;
//                }
                if (!sourceAbsPath.contains("Service.java")) {
                    continue;
                }
                logger.info("getJDTParsedResult.parse facade:{}", sourceAbsPath);
                ClassParsedResult classParseResult = JDTParser.parseFacadeSource(sourceAbsPath);
                if (classParseResult != null) {
                    parsedResultList.add(classParseResult);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            logger
                    .error(String.format("getJDTParsedResult failed, not exist source file.pom:%s",
                            pomXml), e);
            throw new BizException("源码下载失败", DocStateCode.SOURCE_NOT_EXIST);
        } catch (Exception e) {
            logger.error("getJDTParsedResult failed,", e);
            throw new BizException("解析失败");
        }
        return parsedResultList;
    }

    public static MqMessageObj parseAllMqMessageInfo(String basePath) {
        MqMessageObj obj = new MqMessageObj();
        try {
            String releaseLatest = getLatestVersion("com.zhaoyun.docmanager.common.commons", "commondata", false);
            String snapshotLatest = getLatestVersion("com.zhaoyun.docmanager.common.commons", "commondata", true);
            String latestVersion = VersionCompareUtil.compare(releaseLatest, snapshotLatest) > 0 ? releaseLatest
                    : snapshotLatest;
            String pom = new Dependency("com.zhaoyun.docmanager.common.commons", "commondata", latestVersion).toPom();
            String classJarUrl = PomConvert.getClassJarFileUrl(pom);
            String sourceFileUrl = PomConvert.getSourceFileUrl(pom);
            URL urls[] = new URL[0];
            try {
                urls = new URL[]{new URL(classJarUrl)};
            } catch (MalformedURLException e) {
                logger.error("classJarUrl exception.", e);
                return obj;
            }
            MyUrlClassLoader classLoader = new MyUrlClassLoader(urls);
            Class clazz;
            try {
                clazz = classLoader.loadClass("com.zhaoyun.message.registry.Topic");
            } catch (ClassNotFoundException e) {
                logger.error("class(com.zhaoyun.message.registry.Topic) not found;", e);
                return obj;
            }
            Object[] enums = clazz.getEnumConstants();
            List<String> topicList = Lists.newArrayList();
            for (Object e : enums) {
                Method getName = e.getClass().getDeclaredMethod("getName");
                Object o = getName.invoke(e);
                topicList.add(o.toString());
            }
            obj.setTopicList(topicList);
            try {
                clazz = classLoader.loadClass("com.zhaoyun.message.registry.MessageType");
            } catch (ClassNotFoundException e) {
                logger.error("class(com.zhaoyun.message.registry.MessageType) not found;", e);
                return obj;
            }
            enums = clazz.getEnumConstants();
            List<String> messageTypeList = Lists.newArrayList();
            for (Object e : enums) {
                Method getName = e.getClass().getDeclaredMethod("getName");
                Object o = getName.invoke(e);
                messageTypeList.add(o.toString());
            }
            obj.setMessageTypeList(messageTypeList);
            List<String> allMessageEntity = allMessageEntity(classJarUrl, basePath);
            String path = downloadSourceFile(sourceFileUrl, basePath);
            List<FieldsObj> entityList = new ArrayList<>();
            try {
                for (String entity : allMessageEntity) {
                    clazz = classLoader.loadClass(entity);
                    List<FieldsObj> fieldsObjs = ReflectParser.parseGenericType(clazz, null);
                    FieldsObj o = new FieldsObj();
                    o.setClassType(clazz.getName());
                    o.setFieldName(VersionCompareUtil.toSimplyclassType(clazz.getName()));
                    o.setFieldObjs(fieldsObjs);
                    o.setSuperClassTypes(ReflectParser.getSuperClassTypes(clazz));
                    o.setTypeCode(ParseClassTypeSupport.getTypeCode(clazz));
                    ReflectParser.fillFieldComment(path, fieldsObjs, o);
                    entityList.add(o);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            obj.setMessageEntityList(entityList);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            FileUtils.deleteDir(basePath + "/temp/");
        }
        return obj;
    }

    /**
     * 解析指定的类
     *
     * @param groupId
     * @param artifactId
     * @param basePath
     * @param className  全限定名
     * @return
     * @name 解析指定的类
     * @author user
     * @date: 2016年9月19日 下午4:51:20
     */
    public static FieldsObj parseSpecifiedClass(String groupId, String artifactId, String basePath,
                                                String className) throws Exception {
        MyUrlClassLoader classLoader = null;
        try {
            String releaseLatest = getLatestVersion(groupId, artifactId, false);
            String snapshotLatest = getLatestVersion(groupId, artifactId, true);
            String latestVersion = VersionCompareUtil.compare(releaseLatest, snapshotLatest) > 0 ? releaseLatest
                    : snapshotLatest;
            String pom = new Dependency(groupId, artifactId, latestVersion).toPom();
            String classJarUrl = PomConvert.getClassJarFileUrl(pom);
            String sourceFileUrl = PomConvert.getSourceFileUrl(pom);
            String sourceFileName = sourceFileUrl.substring(sourceFileUrl.lastIndexOf("/") + 1,
                    sourceFileUrl.length());
            String classFileName = classJarUrl.substring(classJarUrl.lastIndexOf("/") + 1,
                    classJarUrl.length());
            String sourceFileUnzipPath = basePath + StringUtil.remove(sourceFileName, ".jar");
            File sourceFile = new File(sourceFileUnzipPath);
            File classFile = new File(basePath + classFileName);

            //本地不存在则远程load
            if (!classFile.exists() || !sourceFile.exists()) {
                URL urls[] = new URL[0];
                urls = new URL[]{new URL(classJarUrl)};
                classLoader = new MyUrlClassLoader(urls);
                Class<?> clazz;
                clazz = classLoader.loadClass(className);
                String path = downloadSourceFile(sourceFileUrl, basePath);
                List<FieldsObj> fieldsObjs = ReflectParser.parseGenericType(clazz, null);
                FieldsObj o = new FieldsObj();
                o.setClassType(clazz.getName());
                o.setFieldName(VersionCompareUtil.toSimplyclassType(clazz.getName()));
                o.setFieldObjs(fieldsObjs);
                o.setSuperClassTypes(ReflectParser.getSuperClassTypes(clazz));
                o.setTypeCode(ParseClassTypeSupport.getTypeCode(clazz));
                ReflectParser.fillFieldComment(path, fieldsObjs, o);
                return o;
            }
            //本地存在则本地load
            else {
                URL urls[] = {new URL("file:" + classFile.getAbsolutePath())};
                classLoader = new MyUrlClassLoader(urls);
                Class<?> clazz;
                clazz = classLoader.loadClass(className);
                List<FieldsObj> fieldsObjs = ReflectParser.parseGenericType(clazz, null);
                FieldsObj o = new FieldsObj();
                o.setClassType(clazz.getName());
                o.setFieldName(VersionCompareUtil.toSimplyclassType(clazz.getName()));
                o.setFieldObjs(fieldsObjs);
                o.setSuperClassTypes(ReflectParser.getSuperClassTypes(clazz));
                o.setTypeCode(ParseClassTypeSupport.getTypeCode(clazz));
                ReflectParser.fillFieldComment(sourceFileUnzipPath + "/", fieldsObjs, o);
                return o;
            }
        } finally {
            // 清理jar包的源url地址，防止下次仍然读取老路径，读不到新包的路径
            PomConvert.clearSourceAndClassPath();
            if (classLoader != null) {
                try {
                    classLoader.close();
                } catch (IOException e) {
                    logger.error("classLoader error", e);
                }
            }
        }
    }

    public static String downloadSourceFile(String sourceJarUrl, String basePath) {
        long current = System.currentTimeMillis();
        File file;
        try {
            String baseDir = basePath + "/temp/";
            file = FileUtils.downLoadFile(sourceJarUrl, baseDir, "tempSource_" + current);
            FileUtils.unZipFiles(new ZipFile(file), baseDir);
            return basePath + "/temp/";
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    public static List<String> allMessageEntity(String classJarUrl, String basePath) {
        List<String> resultList = new ArrayList<>();
        long current = System.currentTimeMillis();
        File file;
        try {
            String baseDir = basePath + "/temp/";
            file = FileUtils.downLoadFile(classJarUrl, baseDir, "temp_" + current);
            FileUtils.unZipFiles(new ZipFile(file), baseDir);
            File dir = new File(baseDir + "/com/zhaoyun/message/objects/");
            return allMessageClass(dir);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.deleteDir(basePath + "/temp/");
        }
        return resultList;
    }

    private static List<String> allMessageClass(File file) {
        ArrayList<String> list = Lists.newArrayList();
        if (null == file) {
            return Lists.newArrayList();
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File f : files) {
                    list.addAll(allMessageClass(f));
                }
            }
        } else {
            if (file.getName().endsWith("Message.class")) {
                String absolutePath = file.getAbsolutePath();
                String div = "com" + File.separator + "zhaoyun";
                int i = absolutePath.indexOf(div);
                int j = absolutePath.indexOf(".class");
                String className = absolutePath.substring(i, j);
                className = className.replace(File.separator, ".");
                list.add(className);
            }
        }
        return list;
    }

    public static ClassParsedResult getReflectParsedResult(ClassParsedResult classParsedResult,
                                                           String pomXml, String workBasePath) {
        String sourceJarUrl = PomConvert.getSourceFileUrl(pomXml);//源码jar地址
        String classJarFileUrl = PomConvert.getClassJarFileUrl(pomXml);//编译jar包地址
        String sourceFileName = sourceJarUrl.substring(sourceJarUrl.lastIndexOf("/") + 1,
                sourceJarUrl.length());
        String classFileName = classJarFileUrl.substring(classJarFileUrl.lastIndexOf("/") + 1,
                classJarFileUrl.length());
        String sourceFileUnzipPath = workBasePath + StringUtil.remove(sourceFileName, ".jar");
        File classFile = new File(workBasePath + classFileName);
        if (!classFile.exists()) {
            throw new BizException("请重新加载pom");
        }
        try {
            ReflectParser.parseClassFields(classParsedResult, classFile.getAbsolutePath(),
                    sourceFileUnzipPath + "/");
        } catch (BizException e) {
            throw new BizException(e);
        } catch (Exception e) {
            logger.error("getReflectParsedResult failed,", e);
            e.printStackTrace();
            throw new BizException("解析失败");
        }
        classParsedResult.setReflectParsed(true);
        return classParsedResult;
    }

    /**
     * @param groupId
     * @param artifactId
     * @param snapshot   传空：所有的；传true：snapshot；传false：release
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> allVersions(String groupId, String artifactId, Boolean snapshot) {
        List<String> versionList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(snapshot ? SpringConfig.get("snap_depot") : SpringConfig.get("public_depot"));
        //      sb.append("http://192.168.2.126:8081/nexus/content/repositories/snapshots/");
        sb.append(groupId.trim().replace(".", "/")).append("/");
        sb.append(artifactId).append("/maven-metadata.xml");
        String str;
        try {
            str = HttpClientUtil.get(sb.toString());
            if (!str.contains("<metadata>")) {
                logger.error("请求失败，代码: 404.{}", sb.toString());
                return versionList;
            }
        } catch (Exception e) {
            logger.error("", e);
            return versionList;
        }
        Document doc;
        try {
            doc = DocumentHelper.parseText(str);
        } catch (DocumentException e) {
            throw new BizException("metadata信息有误");
        }
        Element rootElement = doc.getRootElement();
        Element versioning = rootElement.element("versioning");
        Element versions = versioning.element("versions");
        List<Element> version = versions.elements("version");
        for (Element ele : version) {
            versionList.add(ele.getStringValue());
        }
        return versionList;
    }

    @SuppressWarnings("unchecked")
    public static List<String> allVersionsWithLastUpdated(String appName, Boolean snapshot) {
        List<String> versionList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(snapshot ? SpringConfig.get("snap_depot") : SpringConfig.get("public_depot"));
        sb.append("/com/mogujie/");
        sb.append(appName);
        sb.append("/maven-metadata.xml");
        String str = HttpClientUtil.get(sb.toString());
        Document doc;
        try {
            doc = DocumentHelper.parseText(str);
        } catch (DocumentException e) {
            throw new BizException("metadata信息有误");
        }
        Element rootElement = doc.getRootElement();
        Element versioning = rootElement.element("versioning");
        Element versions = versioning.element("versions");
        List<Element> version = versions.elements("version");
        for (Element ele : version) {
            versionList.add(ele.getStringValue());
        }
        Element lastUpdated = versioning.element("lastUpdated");
        versionList.add(lastUpdated.getStringValue());
        return versionList;
    }

    public static String getLatestVersion(String groupId, String artifactId, boolean snapshot) {
        List<String> versionList = allVersions(groupId, artifactId, snapshot);
        String latestVersion = "";
        for (String version : versionList) {
            if (VersionCompareUtil.compare(version, latestVersion) > 0) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }

    /**
     * API全名
     *
     * @param mp
     * @return
     * @date: 2016年8月22日 下午5:22:02
     */
    public static String getApiFullName(MethodParsedResult mp) {
        if (mp == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (mp.getReturnObj() != null) {
            if (StringUtils.isNotBlank(mp.getReturnObj().getClassFullType())) {
                sb.append(VersionCompareUtil.toSimplyFullclassType(mp.getReturnObj()
                        .getClassFullType()) + " ");
            } else {
                sb.append(VersionCompareUtil.toSimplyclassType(mp.getReturnObj().getClassType())
                        + " ");
            }
        }
        sb.append(mp.getMethodName() + "(");
        if (CollectionUtils.isNotEmpty(mp.getParamList())) {
            for (FieldsObj fieldsObj : mp.getParamList()) {
                if (StringUtils.isNotBlank(fieldsObj.getClassFullType())) {
                    sb.append(VersionCompareUtil.toSimplyFullclassType(fieldsObj.getClassFullType())
                            + " ");
                } else {
                    sb.append(VersionCompareUtil.toSimplyclassType(fieldsObj.getClassType()) + " ");
                }
                sb.append(fieldsObj.getFieldName() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    public static Map<String, AppNexusEntity> getAllAppWithAllVersion() {
        Map<String, AppNexusEntity> resultMap = new HashMap<>();
        String[] urls = {SpringConfig.get("snap_depot"), SpringConfig.get("public_depot")};
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            if (StringUtil.isEmpty(url)) {
                continue;
            }
            url += "/com/mogujie/censor/";
            String str = HttpClientUtil.get(url);
            //配置所有-source结尾的
            String reg = "<a.*>(.*)</a>";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            List<String> appList = Lists.newArrayList();
            while (matcher.find()) {
                String result = matcher.group();
                String appName = result.substring(result.indexOf(">") + 1,
                        result.lastIndexOf("<") - 1);
                if (!appName.startsWith("Parent Director")) {
                    appList.add(appName);
                }
            }
            for (Iterator<String> ite = appList.iterator(); ite.hasNext(); ) {
                String appName = ite.next();
//                String appUrl = url + (appName + "/" + (appName + "-facade/"));
                String appUrl = url + appName;
                String s = HttpClientUtil.get(appUrl);
                if (s.startsWith("请求失败")) {
                    ite.remove();
                    continue;
                }
                List<String> versionListWithLastUpdated = allVersionsWithLastUpdated(appName,
                        i == 0);
                if (CollectionUtils.isNotEmpty(versionListWithLastUpdated)) {
                    AppNexusEntity entity = resultMap.get(appName);
                    if (null == entity) {
                        entity = new AppNexusEntity();
                    }
                    if (i == 0) {
                        entity.setSnapshotLastUpdated(versionListWithLastUpdated
                                .get(versionListWithLastUpdated.size() - 1));
                        entity.setSnapshotVersionList(versionListWithLastUpdated.subList(0,
                                versionListWithLastUpdated.size() - 1));
                    } else if (i == 1) {
                        entity.setReleaseLastUpdated(versionListWithLastUpdated
                                .get(versionListWithLastUpdated.size() - 1));
                        entity.setReleaseVersionList(versionListWithLastUpdated.subList(0,
                                versionListWithLastUpdated.size() - 1));
                    }
                    resultMap.put(appName, entity);
                }
            }
        }
        return resultMap;
    }

    public static class AppNexusEntity {
        private String snapshotLastUpdated;
        private String releaseLastUpdated;
        private List<String> snapshotVersionList;
        private List<String> releaseVersionList;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

        public String getSnapshotLastUpdated() {
            return snapshotLastUpdated;
        }

        public void setSnapshotLastUpdated(String snapshotLastUpdated) {
            this.snapshotLastUpdated = snapshotLastUpdated;
        }

        public String getReleaseLastUpdated() {
            return releaseLastUpdated;
        }

        public void setReleaseLastUpdated(String releaseLastUpdated) {
            this.releaseLastUpdated = releaseLastUpdated;
        }

        public List<String> getSnapshotVersionList() {
            if (null == snapshotVersionList) {
                snapshotVersionList = new ArrayList<>();
            }
            return snapshotVersionList;
        }

        public void setSnapshotVersionList(List<String> snapshotVersionList) {
            this.snapshotVersionList = snapshotVersionList;
        }

        public List<String> getReleaseVersionList() {
            if (null == releaseVersionList) {
                releaseVersionList = new ArrayList<>();
            }
            return releaseVersionList;
        }

        public void setReleaseVersionList(List<String> releaseVersionList) {
            this.releaseVersionList = releaseVersionList;
        }
    }

}
