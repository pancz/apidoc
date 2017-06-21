package com.zhaoyun.docmanager.core.apiparse.parser;

import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.apiparse.entity.MethodParsedResult;
import com.zhaoyun.docmanager.core.util.StringUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于JDT API的解析类，用于解析源码文件，获得比如注释，方法名次等内容
 *
 * @author user
 * @version $Id: JDTParser.java, v 0.1 2016年4月28日 下午7:28:33 user Exp $
 */
public class JDTParser {
    protected static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JDTParser.class);

    //JDT 解析API
    private static ASTParser astParser = ASTParser.newParser(AST.JLS8);

    /**
     * 获取javabean的注释
     *
     * @param beanSourceFileAbsPath
     * @return key为列名 value为列注释
     * @throws IOException
     * @date: 2016年4月25日 上午11:34:59
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseBeanCommentMap(List<String> beanSourceFileAbsPaths) {
        //基于JDT API的类接口属性
        Map<String, String> map = new HashedMap<String, String>();
        for (String beanSourceFileAbsPath : beanSourceFileAbsPaths) {
            CompilationUnit compilationUnit;
            try {
                compilationUnit = getCompilationUnit(beanSourceFileAbsPath);
            } catch (IOException e) {
                return null;
            }
            if (compilationUnit == null) {
                return null;
            }
            //获取导入的包
            //获取类，由于facade interface文件只有一个类，不特殊处理内部类等情况，如果是枚举这里list是空的
            List<?> types = compilationUnit.types();
            if (types.isEmpty()) {
                return null;
            }
            TypeDeclaration type = (TypeDeclaration) types.get(0);
            FieldDeclaration fieldDeclaration[] = type.getFields();

            for (FieldDeclaration fieldDeclaration2 : fieldDeclaration) {
                List<VariableDeclarationFragment> commentList = fieldDeclaration2.fragments();
                if (commentList.isEmpty()) {
                    continue;
                }
                VariableDeclarationFragment fragment = commentList.get(0);
                Javadoc javadoc = fieldDeclaration2.getJavadoc();
                String key = fragment != null ? StringUtil.replaceBlank(fragment.toString()) : "";
                if (key.contains("=")) {
                    key = key.substring(0, key.indexOf("="));
                }
                String value = javadoc != null ? StringUtil.replaceBlank(javadoc.toString().replace("/", "").replace("*", "")) : "";
                map.put(key, value);

            }
        }

        return map;
    }

    /**
     * 解析facade源文件
     *
     * @param fileAbsPath java源文件绝对路径
     * @return JavaParsedResult 解析结果
     * @throws Exception
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws IOException
     * @date: 2016年4月14日 下午3:37:13
     */
    public static ClassParsedResult parseFacadeSource(String fileAbsPath) throws IllegalArgumentException,
            IllegalAccessException, IOException {
        //基于JDT API的类接口属性
        CompilationUnit compilationUnit = getCompilationUnit(fileAbsPath);
        if (compilationUnit == null) {
            return null;
        }
        //获取类描述 如果是空 则是一个枚举 
        List<?> types = compilationUnit.types();
        if (types.isEmpty()) {
            return null;
        }
        TypeDeclaration type = (TypeDeclaration) types.get(0);

        ClassParsedResult result = new ClassParsedResult();
        List<MethodParsedResult> parsedMethodList = new ArrayList<>();
        result.setClassQuaName(getClassName(fileAbsPath));
        result.setMethodList(parsedMethodList);
        parseClassComment(type, result);

        /*解析方法*/
        MethodDeclaration[] methodList = type.getMethods();
        for (final MethodDeclaration methodDeclaration : methodList) {
            LOGGER.info("parse facade({})'s method:{}", type.getName(), methodDeclaration.getName());
            final MethodParsedResult parsedMethod = new MethodParsedResult();
            parsedMethodList.add(parsedMethod);
            parsedMethod.setMethodName(methodDeclaration.getName().toString());
            Map<String, String> paramCommentMap = new HashedMap<>();
            /*解析方法注释*/
            parseMethodComment(methodDeclaration, paramCommentMap, parsedMethod);
            /*解析方法参数*/
            parsedMethod.setParamList(parseMethodParams(paramCommentMap, methodDeclaration.parameters()));
        }
        return result;
    }
/*
    private static boolean isDeprecated(TypeDeclaration type) {
        final AtomicInteger count = new AtomicInteger();
        type.accept(new ASTVisitor() {
            @Override
            public boolean visit(MarkerAnnotation node) {
                Name name = node.getTypeName();
                if ("Deprecated".equals(name.toString())) {
                    count.incrementAndGet();
                }
                return true;
            }
        });
        if (count.get() == 0) {
            return false;
        }
        MethodDeclaration[] methods = type.getMethods();
        for (MethodDeclaration md : methods) {
            md.accept(new ASTVisitor() {
                @Override
                public boolean visit(MarkerAnnotation node) {
                    Name name = node.getTypeName();
                    if ("Deprecated".equals(name.toString())) {
                        count.decrementAndGet();
                    }
                    return super.visit(node);
                }
            });
        }
        return count.get() == 1;
    }*/

    private static void parseClassComment(TypeDeclaration type, ClassParsedResult result) {
        Javadoc jd = type.getJavadoc();
        if (null != jd) {
            for (Object oo : jd.tags()) {
                TagElement te = (TagElement) oo;
                String tagName = te.getTagName();
                if ("@author".equals(tagName)) {
                    result.setAuthor(tagContent(te));
                } else if ("@version".equals(tagName)) {
                    result.setVersion(tagContent(te));
                } else {
                    if (StringUtils.isBlank(result.getDesc())) {
                        result.setDesc(tagContent(te));
                    }
                }
            }
        }
    }

    private static String tagContent(TagElement te) {
        List fragments = te.fragments();
        StringBuilder sb = new StringBuilder();
        for (Object o : fragments) {
            if (o instanceof TextElement) {
                TextElement temp = (TextElement) o;
                sb.append(temp.getText());
            } else if (o instanceof TagElement) {
                TagElement temp = (TagElement) o;
                sb.append(tagContent(temp));
            }
        }
        return sb.toString().replace("\n", "");
    }

    @SuppressWarnings("rawtypes")
    private static List<FieldsObj> parseMethodParams(Map<String, String> paramCommentMap, List paramsList)
            throws IllegalArgumentException,
            IllegalAccessException {
        List<FieldsObj> parsedParamList = Lists.newArrayList();
        for (int i = 0; i < paramsList.size(); i++) {
            FieldsObj resParam = new FieldsObj();
            parsedParamList.add(resParam);

            SingleVariableDeclaration varDec = (SingleVariableDeclaration) paramsList.get(i);
            resParam.setIndex(i);
            for (Field varDecField : varDec.getClass().getSuperclass().getDeclaredFields()) {
                varDecField.setAccessible(true);
                //方法名
                if (varDecField.getName().equals("variableName")) {
                    resParam.setFieldName(varDecField.get(varDec).toString());
                    resParam.setFieldComment(paramCommentMap.get(resParam.getFieldName()));
                }
            }

        }
        return parsedParamList;
    }

    /**
     * 获得基于JDT API的类结构
     *
     * @param fileAbsoLoc java文件绝对路径
     * @return
     * @throws IOException
     * @throws Exception
     * @date: 2016年4月14日 下午3:18:55
     */
    private static CompilationUnit getCompilationUnit(String fileAbsoLoc) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fileAbsoLoc));
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        astParser.setSource(new String(input).toCharArray());
        return (CompilationUnit) (astParser.createAST(null));
    }

    private static void parseMethodComment(MethodDeclaration methodDeclaration, Map<String, String> paramCommentMap,
                                           MethodParsedResult resMethod) {

        Javadoc javadoc = methodDeclaration.getJavadoc();
        if (javadoc == null) {
            return;
        }
        resMethod.setFullComment(javadoc.toString());
        Object[] commentTypeList = methodDeclaration.getJavadoc().tags().toArray();
        for (Object aCommentTypeList : commentTypeList) {
            TagElement tagElement = (TagElement) aCommentTypeList;
            List<?> fragmentsValList = tagElement.fragments();
            if (fragmentsValList == null || fragmentsValList.isEmpty()) {
                continue;
            }
            String optionalTagName = tagElement.getTagName();

            //返回描述
            if ("@return".equals(optionalTagName)) {
                resMethod.setReturnComment(StringUtil.replaceBlank(fragmentsValList.get(0).toString()));
            }
            else if ("@param".equals(optionalTagName)) {
                //参数名次:参数描述
                if (fragmentsValList.size() == 2) {
                    paramCommentMap.put(StringUtil.replaceBlank(fragmentsValList.get(0).toString()),
                            StringUtil.replaceBlank(fragmentsValList.get(1).toString()));
                }
                if (fragmentsValList.size() == 1) {
                    paramCommentMap.put(StringUtil.replaceBlank(fragmentsValList.get(0).toString()), " ");
                }
            }
            else if ("@name".equals(optionalTagName)) {
                resMethod
                    .setNameComment(StringUtil.replaceBlank(fragmentsValList.get(0).toString()));
            }
            else if ("@author".equals(optionalTagName)) {
                resMethod
                    .setAuthorComment(StringUtil.replaceBlank(fragmentsValList.get(0).toString()));
            }
            else if ("@date".equals(optionalTagName)) {
                resMethod
                    .setDateComment(StringUtil.replaceBlank(fragmentsValList.get(0).toString()));
            }
            //方法评论
            if (StringUtils.isBlank(optionalTagName)
                && StringUtils.isBlank(resMethod.getMethodComment())) {
                resMethod.setMethodComment(StringUtil.replaceBlank(fragmentsValList.get(0).toString()));
            }
        }

    }

    private static String getClassName(String fileAbsoLoc) {
        if (fileAbsoLoc.contains("/com/mogujie")) {
            return fileAbsoLoc.substring(fileAbsoLoc.indexOf("/com/mogujie") + 1, fileAbsoLoc.lastIndexOf(".")).replace("/", ".");
        } else if (fileAbsoLoc.contains("/com/toowell/")) {
            return fileAbsoLoc.substring(fileAbsoLoc.indexOf("/com/toowell/") + 1, fileAbsoLoc.lastIndexOf(".")).replace("/", ".");
        } else {
            LOGGER.error("Unknown fileAbsoLoc");
            return "";
        }
    }
}
