/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author user
 * @version $Id: VersionCompareUtil.java, v 0.1 2016年8月30日 下午5:09:40 user Exp $
 */
public class VersionCompareUtil {
    private static final Pattern VERSIOM_PATTERN = Pattern.compile("\\d+");
    private static final Pattern FX_PATTERN      = Pattern.compile("<|>");
    /**
     * if(version1<version2) : 返回负数
     * if(version1===version2) : 返回0
     * if(version1>version2) : 返回正数
     * 同版本号带有snapshot的是低版本
     *
     * @param version1
     * @param version2
     * @return
     * @date: 2016年8月30日 下午5:30:01
     */
    public static int compare(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        Matcher m1 = VERSIOM_PATTERN.matcher(version1);
        Matcher m2 = VERSIOM_PATTERN.matcher(version2);
        int result = 0;
        for (boolean f1 = m1.find(),f2 = m2.find(); f1||f2; f1 = m1.find(),f2 = m2.find()) {
            int v1 = 0, v2 = 0;
            if(f1){
                v1 =  Integer.valueOf(m1.group());
            }
            if(f2){
                v2 =  Integer.valueOf(m2.group());
            }
            if (v1 > v2) {
                result = 1;
                break;
            } else if (v1 < v2) {
                result = -1;
                break;
            }
        }
        if (result != 0) {
            return result;
        } else {
            if (version1.length() > version2.length()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**  
     *返回简写类名
     * @param classType
     * @return
     * @date: 2016年8月31日 下午4:28:17
     */
    public static String toSimplyclassType(String classType) {
        if (StringUtil.isNotBlank(classType)) {
            int i = classType.lastIndexOf(".");
            if (-1 != i) {
                if (!classType.substring(0, i).startsWith("java.")
                    || classType.startsWith("java.lang")) {
                    return classType.substring(i + 1);
                }
                return classType;
            }
        }
        return classType;
    }

    /**  
     * 带泛型返回简写类名
     * @param classFullType
     * @return
     * @date: 2016年9月1日 上午11:04:18
     */
    public static String toSimplyFullclassType(String classFullType) {
        Matcher m = FX_PATTERN.matcher(classFullType);
        int start = 0;
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(toSimplyclassType(classFullType.substring(start, m.start())) + m.group());
            start = m.start();
        }
        if (start <= 0) {
            return classFullType;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(compare("1.1.10", "1.1.9"));
        System.out.println(compare("1.1.10", "1.1.10"));
        System.out.println(compare("1.1.10", "1.1.10.1"));
        System.out.println(compare("1.1.10", "1.1.11"));
        System.out.println(compare("1.1.10.123123", "1.1.11"));
        System.out.println(compare("1.1.10.123123", "1.1.10.1231"));
        System.out.println(compare("1.1.10.123123", "1.1.10.123123-SNAPSHOT"));
    }
}
