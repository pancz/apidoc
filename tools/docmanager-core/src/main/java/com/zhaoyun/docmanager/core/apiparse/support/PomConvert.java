/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.apiparse.support;

import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.core.exception.BizException;
import com.zhaoyun.docmanager.core.util.HttpClientUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pom信息解析
 *
 * @author user
 * @version $Id: PomParser.java, v 0.1 2016年4月14日 下午3:06:21 user Exp $
 */
public class PomConvert {

    private static String latestClassString = null;

    private static String latestSourceString = null;

    /**
     * 将pom信息转换为source仓库地址
     * <p>
     * <dependency>
     * <groupId>com.zhaoyun.marketcenter</groupId>
     * <artifactId>marketcenter-facade</artifactId>
     * <version>1.0.0</version>
     * </dependency>
     * </p>
     *
     * @param pomXml
     * @return
     * @date: 2016年4月14日 下午3:08:57
     */
    public static String getSourceFileUrl(String pomXml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(pomXml);
        } catch (DocumentException e) {
            throw new BizException("pom信息有误");
        }
        Element rootElt = doc.getRootElement();
        Element groupEl = rootElt.element("groupId");
        Element artEl = rootElt.element("artifactId");
        Element verEl = rootElt.element("version");
        if (groupEl == null || artEl == null || verEl == null) {
            throw new BizException("pom信息有误");
        }
        String versionStr = verEl.getStringValue();
        boolean isShot = versionStr.toUpperCase().contains("SNAPSHOT");
        StringBuffer sb = new StringBuffer();
        sb.append(isShot ? SpringConfig.get("snap_depot") : SpringConfig.get("public_depot"));
        sb.append(groupEl.getStringValue().trim().replace(".", "/")).append("/");
        sb.append(artEl.getStringValue()).append("/");
        sb.append(verEl.getStringValue()).append("/");
        if (isShot) {
            // 添加全局变量，目的：减少频繁请求url，影响效率 by  LiLiang
            if (StringUtils.isBlank(latestSourceString)
                    || !latestSourceString.toUpperCase().contains(artEl.getStringValue().toUpperCase() + "/" + versionStr.toUpperCase())) {
                latestSourceString = getLatestSource(sb.toString());
            }
            return latestSourceString;
        } else {
            sb.append(artEl.getStringValue()).append("-").append(versionStr).append("-sources.jar");
        }
        return sb.toString();
    }

    /**
     * 将pom信息转换为jar仓库地址
     * <p>
     * <dependency>
     * <groupId>com.zhaoyun.marketcenter</groupId>
     * <artifactId>marketcenter-facade</artifactId>
     * <version>1.0.0</version>
     * </dependency>
     * </p>
     *
     * @param pomXml
     * @return
     * @date: 2016年4月14日 下午3:08:57
     */
    public static String getClassJarFileUrl(String pomXml) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(pomXml);
        } catch (DocumentException e) {
            throw new BizException("pom信息有误");
        }
        Element rootElt = doc.getRootElement();
        Element groupEl = rootElt.element("groupId");
        Element artEl = rootElt.element("artifactId");
        Element verEl = rootElt.element("version");
        if (groupEl == null || artEl == null || verEl == null) {
            throw new BizException("pom信息有误");
        }
        String versionStr = verEl.getStringValue();
        boolean isShot = versionStr.toUpperCase().contains("SNAPSHOT");
        StringBuffer sb = new StringBuffer();
        sb.append(isShot ? SpringConfig.get("snap_depot") : SpringConfig.get("public_depot"));
//        sb.append("http://192.168.2.126:8081/nexus/content/repositories/snapshots/");
        sb.append(groupEl.getStringValue().trim().replace(".", "/")).append("/");
        sb.append(artEl.getStringValue()).append("/");
        sb.append(verEl.getStringValue()).append("/");
        if (isShot) {
            // 添加全局变量，目的：减少频繁请求url，影响效率 by  LiLiang
            if (StringUtils.isBlank(latestClassString)
                    || !latestClassString.toUpperCase().contains(artEl.getStringValue().toUpperCase() + "/" + versionStr.toUpperCase())) {
                latestClassString = getLatestClass(sb.toString());
            }
            return latestClassString;
        } else {
            sb.append(artEl.getStringValue()).append("-").append(versionStr).append(".jar");
        }
        return sb.toString();
    }

    /**
     * 找到最新版快照包
     *
     * @param dir 快照包根目录
     * @return
     * @date: 2016年5月11日 下午2:21:51
     */
    public static String getLatestSource(String dir) {
        String str = HttpClientUtil.get(dir);
        //配置所有-source结尾的
        String reg = "<a.*>(.*sources.jar$?)</a>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        List<String> results = Lists.newArrayList();
        while (matcher.find()) {
            String result = matcher.group();
            results.add(result.substring(result.indexOf("\"") + 1, result.lastIndexOf("\"")));
        }
        if (CollectionUtils.isEmpty(results)) {
            return "";
        }
        Collections.sort(results, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {

                o1 = o1.replace("-sources.jar", "");
                o1 = o1.substring(o1.lastIndexOf("-") + 1, o1.length());

                o2 = o2.replace("-sources.jar", "");
                o2 = o2.substring(o2.lastIndexOf("-") + 1, o2.length());
                return Integer.parseInt(o1) < Integer.parseInt(o2) ? 1 : 0;
            }
        });
        return results.get(results.size() - 1);
    }

    /**
     * 找到最新版快照包
     *
     * @param dir 快照包根目录
     * @return
     * @date: 2016年5月11日 下午2:21:37
     */
    public static String getLatestClass(String dir) {
        String str = HttpClientUtil.get(dir);
        //配置所有-结尾的
        String reg = "<a.*>(.*\\d.jar$?)</a>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        List<String> results = Lists.newArrayList();
        while (matcher.find()) {
            String result = matcher.group();
            results.add(result.substring(result.indexOf("\"") + 1, result.lastIndexOf("\"")));
        }
        if (results.isEmpty()) {
            return null;
        }
        Collections.sort(results, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {

                int num1 = Integer.parseInt(o1.substring(o1.lastIndexOf("-") + 1, o1.lastIndexOf(".")));
                int num2 = Integer.parseInt(o2.substring(o1.lastIndexOf("-") + 1, o2.lastIndexOf(".")));
                return num1 < num2 ? 1 : 0;
            }
        });
        return results.get(results.size() - 1);
    }

    /**
     * 清理jar包的源url地址，防止下次仍然读取老路径，读不到新包的路径
     */
    public static void clearSourceAndClassPath() {
        latestClassString = null;
        latestSourceString = null;
    }
}
