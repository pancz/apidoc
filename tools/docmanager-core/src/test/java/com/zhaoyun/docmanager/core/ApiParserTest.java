package com.zhaoyun.docmanager.core;

import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.core.apiparse.APIParse;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.apiparse.entity.MethodParsedResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiParserTest {
    private static final Logger logger = LoggerFactory.getLogger(ApiParserTest.class);

    /**
     * @param i   注释
     * @param str
     * @date: 2016年5月6日 下午4:06:03
     */
    public void get(int i, String str) {

    }

    public void get(String str, int i) {

    }

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        map.put("snap_depot", "http://maven.mogujie.org/nexus/content/repositories/snapshots/");
        //map.put("snap_depot", "http://192.168.2.126:8081/nexus/content/repositories/releases/");
        map.put("public_depot", "http://maven.mogujie.org/nexus/content/repositories/releases");
        SpringConfig.set(map);
    	String pomXml = "<dependency><groupId>com.mogujie.censor</groupId><artifactId>censor-server-api</artifactId><version>1.0.1-SNAPSHOT</version></dependency>";

        List<ClassParsedResult> list = APIParse.getJDTParsedResult(pomXml, "/Users/zhaoyun/apimanager/parse/");
        for (ClassParsedResult classParsedResult : list) {
            try {
                APIParse.getReflectParsedResult(classParsedResult, pomXml, "/Users/zhaoyun/apimanager/parse/");
            } catch (Exception e) {
                System.err.println("++++++ parse_error:" + classParsedResult.getClassQuaName() + "(" + classParsedResult.getMethodList().size() + ")" + " +++++++ " + e);
            }

        }
        System.out.println("-------------------- classSize:" + list.size() + "-----------------");
        StringBuilder stringBuilder = new StringBuilder();
        //表头
        stringBuilder.append("id").append("\t");
        stringBuilder.append("类名").append("\t");
        stringBuilder.append("方法名").append("\t");
        stringBuilder.append("方法说明").append("\t");
        stringBuilder.append("应用内的子模块").append("\t");
        stringBuilder.append("接口优先级:1-5").append("\t");
        stringBuilder.append("是否使用:是或否").append("\t");
        stringBuilder.append("使用方").append("\t");
        stringBuilder.append("近期改造计划").append("\t");
        stringBuilder.append("入参名:入参类型").append("\t");
        stringBuilder.append("出参类型").append("\t");
        stringBuilder.append("入参说明").append("\t");
        stringBuilder.append("出参说明").append("\t");

        System.out.println(stringBuilder);
        for (ClassParsedResult classParsedResult : list) {
            for (MethodParsedResult methodParsedResult : classParsedResult.getMethodList()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(StringUtils.substringAfterLast(classParsedResult.getClassQuaName(), ".")).append(".").append(methodParsedResult.getMethodName()).append("\t");
                // 类名
                stringBuilder.append(classParsedResult.getClassQuaName()).append("\t");
                // 方法名
                stringBuilder.append(methodParsedResult.getMethodName()).append("\t");
                // 方法说明
                stringBuilder.append(methodParsedResult.getMethodComment()).append("\t");
                // 应用内的子模块
                stringBuilder.append("").append("\t");
                // 接口优先级:1-5
                stringBuilder.append("").append("\t");
                // 是否使用:是或否
                stringBuilder.append("").append("\t");
                // 使用方
                stringBuilder.append("").append("\t");
                // 近期改造计划
                stringBuilder.append("").append("\t");

                // 参数类型:参数名
                if (methodParsedResult.getParamList() == null) {
                    stringBuilder.append("空\t");
                } else {
                    for (FieldsObj fieldsObj : methodParsedResult.getParamList()) {
                        stringBuilder.append(fieldsObj.getFieldName()).append(":").append(StringUtils.substringAfterLast(StringUtils.substringBefore(fieldsObj.getClassType(), "<"), ".")).append("|");
                    }
                    stringBuilder.append("\t");
                }
                // 出参类型
                if (methodParsedResult.getReturnObj() == null) {
                    stringBuilder.append("空").append("\t");
                } else {
                    String str = methodParsedResult.getReturnObj().getClassFullType();
                    stringBuilder.append(StringUtils.substringAfterLast(StringUtils.substringBefore(StringUtils.substring(str, 7, str.length() - 1), "<"), ".")).append("\t");
                }
                // 方法参数说明
                if (methodParsedResult.getParamList() == null) {
                    stringBuilder.append("空\t");
                } else {
                    for (FieldsObj fieldsObj : methodParsedResult.getParamList()) {
                        stringBuilder.append(fieldsObj.getFieldComment()).append("|");
                    }
                    stringBuilder.append("\t");
                }
                // 出参说明
                stringBuilder.append(methodParsedResult.getReturnComment()).append("\t");
                System.out.println(stringBuilder);
            }
        }
    }

    /**
     * 根据输入的内容，匹配出包含中文但不包含comment的<a>标签中的中文字符
     *
     * @param source 要匹配的内容
     * @return <a>标签中的中文字符
     */
    public static String matchChineseCharacters(String source) {
        //匹配出包含中文但不包含comment的<a>标签 -     /sources.jar$/
        String reg = "<a.*>(.*sources.jar$?)</a>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        StringBuilder character = new StringBuilder();
        while (matcher.find()) {
            String result = matcher.group();
            System.out.println(result);
        }
        return character.toString();
    }


}
