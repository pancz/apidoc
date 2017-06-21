package com.zhaoyun.docmanager.core.apiparse.parser;

import com.google.common.collect.Lists;
import com.zhaoyun.docmanager.core.apiparse.entity.ClassParsedResult;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;
import com.zhaoyun.docmanager.core.apiparse.entity.JDTType;
import com.zhaoyun.docmanager.core.apiparse.entity.MethodParsedResult;
import com.zhaoyun.docmanager.core.apiparse.support.ParseClassTypeSupport;
import com.zhaoyun.docmanager.core.util.MyUrlClassLoader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基于反射的class文件解析类
 * 主要用于解析类文件的方法，出参，入参
 *
 * @author user
 * @version $Id: ClassReflector.java, v 0.1 2016年4月28日 下午7:30:22 user Exp $
 */
public class ReflectParser {
    protected static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReflectParser.class);

    @SuppressWarnings("rawtypes")
    public static List<FieldsObj> parseGenericType(Type genericType, List<String> historyList) {
        ArrayList<String> history;
        if (null == historyList) {
            history = new ArrayList<>();
        } else {
            history = Lists.newArrayList(historyList);
        }
        JDTType jdtType = new JDTType(genericType);
        String name = jdtType.getClassName();
        if (null != name && history.contains(name)) {
            LOGGER.info("参数出现递归{}", name);
            return null;
        }
        history.add(name);
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Class rawClazz = (Class) parameterizedType.getRawType();
            if (rawClazz.getName().equals("java.lang.Class")) {
                return null;
            }
            Type actualTypeArguments[] = parameterizedType.getActualTypeArguments();
            List<TypeVariable> tvList = Arrays.asList(rawClazz.getTypeParameters());
            List<FieldsObj> list = Lists.newArrayList();
            for (Method method : rawClazz.getMethods()) {
                FieldsObj fieldsObj = new FieldsObj();
                String proName = getProName(method);
                if (StringUtils.isBlank(proName)) {
                    continue;
                }
                fieldsObj.setOfClassType(rawClazz.getName());
                fieldsObj.setFieldName(proName);
                Type methodType = method.getGenericReturnType();
                if (methodType instanceof Class) {
                    Class methodReturnClass = (Class) methodType;
                    fieldsObj.setClassType(methodReturnClass.getName());
                    fieldsObj.setSuperClassTypes(getSuperClassTypes(methodReturnClass));
                    fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(methodReturnClass));
                    fieldsObj.setFieldObjs(parseGenericType(methodType, history));
                } else if (methodType instanceof TypeVariable) {
                    TypeVariable tv = (TypeVariable) methodType;
                    int index = tvList.indexOf(tv);
                    Type acType = actualTypeArguments[index];
                    if (acType instanceof Class) {
                        fieldsObj.setClassFullType(acType.toString());
                        fieldsObj.setClassType(((Class) acType).getName());
                        fieldsObj.setSuperClassTypes(getSuperClassTypes((Class) acType));
                        fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode((Class) acType));
                        fieldsObj.setFieldObjs(parseGenericType(acType, history));
                    } else if (acType instanceof ParameterizedType) {
                        ParameterizedType atype = (ParameterizedType) acType;
                        Class clzOfT = (Class) atype.getRawType();
                        boolean isMap = ParseClassTypeSupport.isMap(clzOfT);
                        boolean clzOfTCollect = ParseClassTypeSupport.isCollection(clzOfT);
                        Type type[] = atype.getActualTypeArguments();
                        if (isMap) {
                            fieldsObj.setFieldComment("自定义map对象");
                            fieldsObj.setClassType(clzOfT.getName());
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(clzOfT));
                            fieldsObj.setClassFullType(atype.toString());

                            FieldsObj key = new FieldsObj();
                            key.setOfClassType(clzOfT.getName());
                            key.setFieldName("key");
                            key.setFieldComment("自定义map>key");
                            key.setClassType(new JDTType(type[0]).getClassName());
                            key.setTypeCode(new JDTType(type[0]).getTypeCode());
                            key.setFieldObjs(parseGenericType(type[0], history));

                            FieldsObj value = new FieldsObj();
                            value.setOfClassType(clzOfT.getName());
                            value.setFieldName("value");
                            value.setFieldComment("自定义map>value");
                            value.setClassType(new JDTType(type[1]).getClassName());
                            if (type[1] instanceof Class) {
                                value.setSuperClassTypes(getSuperClassTypes((Class) type[1]));
                            }
                            value.setTypeCode(new JDTType(type[1]).getTypeCode());
                            value.setFieldObjs(parseGenericType(type[1], history));
                            List<FieldsObj> keyValList = Lists.newArrayList();
                            keyValList.add(key);
                            keyValList.add(value);
                            fieldsObj.setFieldObjs(keyValList);
                        } else if (clzOfTCollect) {
                            fieldsObj.setFieldComment("自定义list对象");
                            fieldsObj.setClassType(clzOfT.getName());
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(clzOfT));
                            fieldsObj.setClassFullType(atype.toString());

                            FieldsObj listElement = new FieldsObj();
                            listElement.setOfClassType(clzOfT.getName());
                            listElement.setFieldName("value");
                            listElement.setFieldComment("自定义list属性");
                            listElement.setClassType(new JDTType(type[0]).getClassName());
                            if (type[0] instanceof Class) {
                                listElement.setSuperClassTypes(getSuperClassTypes((Class) type[0]));
                            }
                            listElement.setTypeCode(new JDTType(type[0]).getTypeCode());
                            listElement.setFieldObjs(parseGenericType(type[0], history));

                            fieldsObj.setFieldObjs(Lists.newArrayList(listElement));
                        } else {
                            fieldsObj.setClassFullType(atype.toString());
                            if (acType instanceof Class) {
                                fieldsObj.setSuperClassTypes(getSuperClassTypes((Class) acType));
                            }
                            fieldsObj.setClassType(clzOfT.getName());
                            fieldsObj.setFieldObjs(parseGenericType(atype, history));
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(clzOfT));
                        }
                    }
                } else if (methodType instanceof ParameterizedType) {
                    ParameterizedType methodPt = (ParameterizedType) methodType;
                    Class methodRawClz = (Class) methodPt.getRawType();
                    Type type[] = methodPt.getActualTypeArguments();
                    boolean isMap = ParseClassTypeSupport.isMap(methodRawClz);
                    boolean isCollection = ParseClassTypeSupport.isCollection(methodRawClz);
                    if (isCollection) {
                        fieldsObj.setFieldComment("自定义list对象");
                        fieldsObj.setClassType(methodRawClz.getName());
                        fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(methodRawClz));

                        FieldsObj listElement = new FieldsObj();
                        listElement.setOfClassType(methodRawClz.getName());
                        listElement.setFieldName("value");
                        listElement.setFieldComment("自定义list属性");
                        if (!(actualTypeArguments[0] instanceof Class)) {
                            listElement.setClassType("<T>");
                            listElement.setTypeCode(1);
                        } else {
                            listElement.setClassType(((Class) actualTypeArguments[0]).getName());
                            listElement.setSuperClassTypes(getSuperClassTypes(((Class) actualTypeArguments[0])));
                            listElement.setTypeCode(ParseClassTypeSupport.getTypeCode((Class) actualTypeArguments[0]));
                        }
                        listElement.setFieldObjs(parseGenericType(actualTypeArguments[0], history));

                        fieldsObj.setFieldObjs(Lists.newArrayList(listElement));
                    }
                    if (isMap) {
                        fieldsObj.setFieldComment("自定义map对象");
                        fieldsObj.setClassType(methodRawClz.getName());
                        fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(methodRawClz));

                        FieldsObj key = new FieldsObj();
                        key.setOfClassType(methodRawClz.getName());
                        key.setFieldName("key");
                        key.setFieldComment("自定义map>key");
                        key.setClassType(new JDTType(type[0]).getClassName());
                        key.setTypeCode(new JDTType(type[0]).getTypeCode());
                        key.setFieldObjs(parseGenericType(type[0], history));

                        FieldsObj value = new FieldsObj();
                        value.setOfClassType(methodRawClz.getName());
                        value.setFieldName("value");
                        value.setFieldComment("自定义map>value");
                        value.setClassType(new JDTType(type[1]).getClassName());
                        if (type[1] instanceof Class) {
                            value.setSuperClassTypes(getSuperClassTypes(((Class) type[1])));
                        }
                        value.setTypeCode(new JDTType(type[1]).getTypeCode());
                        value.setFieldObjs(parseGenericType(type[1], history));
                        List<FieldsObj> keyValList = Lists.newArrayList();
                        keyValList.add(key);
                        keyValList.add(value);
                        fieldsObj.setFieldObjs(keyValList);
                    }
                }
                list.add(fieldsObj);
            }

            return list;
        } else {
            if (genericType instanceof Class) {
                Class clazz = (Class) genericType;
                if (isNotNeedParse(clazz)) {
                    return null;
                }
                List<FieldsObj> list = Lists.newArrayList();
                for (Method method : clazz.getMethods()) {
                    String proName = getProName(method);
                    if (StringUtils.isBlank(proName)) {
                        continue;
                    }
                    Type methodType;
                    methodType = method.getGenericReturnType();
                    FieldsObj fieldsObj = new FieldsObj();
                    fieldsObj.setFieldName(proName);
                    if (methodType instanceof Class) {
                        Class methodClass = (Class) methodType;
                        if (methodClass.isEnum()) {
                            fieldsObj.setClassType("java.lang.String");
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode((Class) methodType));
                        } else {
                            fieldsObj.setClassType(((Class) methodType).getName());
                            fieldsObj.setSuperClassTypes(getSuperClassTypes((Class) methodType));
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode((Class) methodType));
                            //防止user中有个User出现死循环
                            if (!((Class) methodType).getName().equals(clazz.getName())) {
                                //枚举值不在解析
                                fieldsObj.setFieldObjs(parseGenericType(methodType, history));
                            }
                        }
                    } else if (methodType instanceof ParameterizedType) {
                        //List<User>
                        ParameterizedType methodPt = (ParameterizedType) methodType;
                        Class methodRawClz = (Class) methodPt.getRawType();
                        Type type = methodPt.getActualTypeArguments()[0];
                        boolean coletType = ParseClassTypeSupport.isCollection(methodRawClz);
                        boolean isMap = ParseClassTypeSupport.isMap(methodRawClz);
                        if (coletType) {
                            fieldsObj.setFieldComment("自定义list对象");
                            fieldsObj.setClassType(methodRawClz.getName());
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(methodRawClz));
                            FieldsObj listElement = new FieldsObj();
                            listElement.setOfClassType(methodRawClz.getName());
                            listElement.setFieldName("value");
                            listElement.setFieldComment("自定义list属性");
                            JDTType jt = new JDTType(type);
                            listElement.setClassType(jt.getClassName());
                            if (type instanceof Class) {
                                listElement.setSuperClassTypes(getSuperClassTypes((Class) type));
                            }
                            listElement.setTypeCode(jt.getTypeCode());
                            //防止user中有个list<User>出现死循环
                            if (!new JDTType(type).getClassName().equals(clazz.getName())) {
                                listElement.setFieldObjs(parseGenericType(type, history));
                            }
                            fieldsObj.setFieldObjs(Lists.newArrayList(listElement));
                        }
                        if (isMap) {
                            Type type2[] = methodPt.getActualTypeArguments();
                            fieldsObj.setFieldComment("自定义map对象");
                            fieldsObj.setClassType(methodRawClz.getName());
                            fieldsObj.setTypeCode(ParseClassTypeSupport.getTypeCode(methodRawClz));

                            FieldsObj key = new FieldsObj();
                            key.setOfClassType(methodRawClz.getName());
                            key.setFieldName("key");
                            key.setFieldComment("自定义map>key");
                            key.setClassType(new JDTType(type2[0]).getClassName());
                            key.setTypeCode(new JDTType(type2[0]).getTypeCode());
                            key.setFieldObjs(parseGenericType(type2[0], history));

                            FieldsObj value = new FieldsObj();
                            value.setOfClassType(methodRawClz.getName());
                            value.setFieldName("value");
                            value.setFieldComment("自定义map>value");
                            value.setClassType(new JDTType(type2[1]).getClassName());
                            if (type2[1] instanceof Class) {
                                value.setSuperClassTypes(getSuperClassTypes(((Class) type2[1])));
                            }
                            value.setTypeCode(new JDTType(type2[1]).getTypeCode());
                            value.setFieldObjs(parseGenericType(type2[1], history));
                            List<FieldsObj> keyValList = Lists.newArrayList();
                            keyValList.add(key);
                            keyValList.add(value);
                            fieldsObj.setFieldObjs(keyValList);
                        }
                    }
                    fieldsObj.setOfClassType(clazz.getName());
                    list.add(fieldsObj);
                }
                return list;

            }
        }
        return null;
    }

    private static Class<?> getClassFromType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return (Class<?>) type;
    }

    @SuppressWarnings("resource")
    public static ClassParsedResult parseClassFields(ClassParsedResult classParsedResult, String classJarFilePath, String unzipPath)
            throws
            IOException, ClassNotFoundException {
        URL urls[] = {new URL("file:" + classJarFilePath)};
        MyUrlClassLoader classLoader = new MyUrlClassLoader(urls);
        Class<?> clazz;
        clazz = classLoader.loadClass(classParsedResult.getClassQuaName());
        Deprecated deprecated = clazz.getAnnotation(Deprecated.class);
        if (null != deprecated) {
            classParsedResult.setDeprecated(true);
        }
        for (Method method : clazz.getDeclaredMethods()) {
            Type methodGenericReturnType = null;
            try {
                methodGenericReturnType = method.getGenericReturnType();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            List<FieldsObj> fieldsObjList = null;
            Class<?> rawClass = null;
            if (!(methodGenericReturnType instanceof ParameterizedType)) {
                rawClass = (Class<?>)methodGenericReturnType;
                //解析字段
                fieldsObjList = parseGenericType(methodGenericReturnType, null);
            } else {
                ParameterizedType methodReturnPt = (ParameterizedType) methodGenericReturnType;
                rawClass = (Class<?>) methodReturnPt.getRawType();
                //解析字段
                fieldsObjList = parseGenericType(methodReturnPt, null);
            }
            MethodParsedResult parsedMethod = findParsedMethod(method, classParsedResult.getMethodList());
            deprecated = method.getAnnotation(Deprecated.class);
            if (null == parsedMethod) {
                LOGGER.warn("*********************No parsedMethod found.*************************");
                continue;
            }
            if (null != deprecated) {
                parsedMethod.setDeprecated(true);
            }
            //初始化返回对象
            FieldsObj returnObj = new FieldsObj();
            returnObj.setClassType(rawClass.getName());
            returnObj.setClassFullType(methodGenericReturnType.toString());
            returnObj.setSuperClassTypes(getSuperClassTypes(rawClass));
            returnObj.setFieldObjs(fieldsObjList);
            returnObj.setTypeCode(ParseClassTypeSupport.getTypeCode(rawClass));
            returnObj.setFieldName("result");
            returnObj.setFieldComment(parsedMethod.getReturnComment());
            //处理字段注释
            fillFieldComment(unzipPath, fieldsObjList, returnObj);
            parsedMethod.setReturnObj(returnObj);

            Type paramGenericTypes[] = method.getGenericParameterTypes();
            //解析入参数
            parseParam(unzipPath, parsedMethod, paramGenericTypes);

        }
        classLoader.close();
        return classParsedResult;
    }

    private static void parseParam(String unzipPath, MethodParsedResult parsedMethod, Type[] paramGenericTypes)
            throws IOException {
        for (int i = 0; i < paramGenericTypes.length; i++) {
            Type paramGenericTy = paramGenericTypes[i];
            if (i >= parsedMethod.getParamList().size()) {
                System.err.println("parsedMethod error:" + unzipPath + "---" + parsedMethod.getMethodComment() + "--" + parsedMethod.getMethodName() + "----" + i + ">=" + parsedMethod.getParamList().size());
                continue;
            }
            FieldsObj param = parsedMethod.getParamList().get(i);
            if (paramGenericTy instanceof ParameterizedType) {
                ParameterizedType paramPt = (ParameterizedType) paramGenericTy;
                Class<?> paramRawClass = (Class<?>) paramPt.getRawType();
                Type actType = paramPt.getActualTypeArguments()[0];
                if (actType instanceof Class) {
                    boolean isCollection = ParseClassTypeSupport.isCollection(paramRawClass);
                    boolean isMap = ParseClassTypeSupport.isMap(paramRawClass);
                    if (isCollection) {
                        param.setFieldComment("自定义list对象");
                        param.setClassType(paramRawClass.getName());
                        param.setTypeCode(ParseClassTypeSupport.getTypeCode(paramRawClass));

                        FieldsObj listElement = new FieldsObj();
                        listElement.setOfClassType(paramRawClass.getName());
                        listElement.setFieldName("value");
                        listElement.setFieldComment("自定义list属性");
                        listElement.setSuperClassTypes(getSuperClassTypes((Class<?>) actType));
                        listElement.setClassType(((Class<?>) actType).getName());
                        listElement.setTypeCode(ParseClassTypeSupport.getTypeCode((Class<?>) actType));
                        listElement.setFieldObjs(parseGenericType(actType, null));
                        param.setFieldObjs(Lists.newArrayList(listElement));
                    } else if (isMap) {
                        param.setClassType(paramRawClass.getName());
                        param.setTypeCode(ParseClassTypeSupport.getTypeCode(paramRawClass));
                        if (actType instanceof Class) {
                            param.setSuperClassTypes(getSuperClassTypes((Class<?>) actType));
                        }
                        param.setFieldObjs(parseGenericType(actType, null));
                        // TODO: throw new BizException("入参不支持map");
                    } else {
                        param.setClassType(paramRawClass.getName());
                        if (actType instanceof Class) {
                            param.setSuperClassTypes(getSuperClassTypes((Class<?>) actType));
                        }
                        param.setTypeCode(ParseClassTypeSupport.getTypeCode(paramRawClass));
                        param.setFieldObjs(parseGenericType(paramPt, null));
                    }

                }

            } else if (paramGenericTy instanceof Class) {
                Class<?> paramClass = (Class<?>) paramGenericTy;
                param.setClassType(paramClass.getName());
                param.setTypeCode(ParseClassTypeSupport.getTypeCode(paramClass));
                param.setSuperClassTypes(getSuperClassTypes(paramClass));
                param.setFieldObjs(parseGenericType(paramGenericTy, null));
            }
            if (param.getFieldObjs() != null) {
                //处理字段注释
                fillFieldComment(unzipPath, param.getFieldObjs(), param);
            }
        }
    }

//    private static MethodParsedResult findParsedMethod(String name, List<MethodParsedResult> methodList) {
//        for (MethodParsedResult parsedMethod : methodList) {
//            if (parsedMethod.getMethodName().equals(name)) {
//                return parsedMethod;
//            }
//        }
//        return null;
//    }

    private static MethodParsedResult findParsedMethod(Method method, List<MethodParsedResult> methodList) {
        for (MethodParsedResult parsedMethod : methodList) {
            if (parsedMethod.getMethodName().equals(method.getName()) && parsedMethod.getParamList().size() == method.getParameterTypes().length) {
                return parsedMethod;
            }
        }
        return null;
    }

    public static List<FieldsObj> fillFieldComment(String sourceAbsPrefix, List<FieldsObj> fields, FieldsObj pField) throws IOException {
        List<String> sourceLocs = Lists.newArrayList();
        if (pField.getSuperClassTypes() != null) {
            for (String pClass : pField.getSuperClassTypes()) {
                sourceLocs.add(sourceAbsPrefix + pClass.replace(".", "/") + ".java");
            }
        }
        String sourceLoc = sourceAbsPrefix + pField.getClassType().replace(".", "/") + ".java";
        sourceLocs.add(sourceLoc);

        Map<String, String> fieldsCommentMap = JDTParser.parseBeanCommentMap(sourceLocs);
        if (CollectionUtils.isNotEmpty(fields)) {
            for (FieldsObj field : fields) {
                if (field.getOfClassType() == null) {
                    continue;
                }

                if (fieldsCommentMap != null) {
                    field.setFieldComment(fieldsCommentMap.get(field.getFieldName()));
                }
                if (field.getFieldObjs() != null) {
                    fillFieldComment(sourceAbsPrefix, field.getFieldObjs(), field);
                }
                if (field.getOfClassType().equals("com.zhaoyun.knife.result.Result")) {
                    if (field.getFieldName().equals("data")) {
                        field.setFieldComment("结果数据");
                    }
                }
                if (field.getOfClassType().equals("com.zhaoyun.marketcenter.facade.ro.PagedDataRO")) {
                    if (field.getFieldName().equals("resultList")) {
                        field.setFieldComment("结果列表");
                    }
                }
            }
        }
        return fields;
    }

    private static boolean isNotNeedParse(Class<?> parseClass) {
        //基础数据类型以及非用户自定义,及枚举类 不被解析
        return ParseClassTypeSupport.baseTypeList.contains(parseClass.getName())
                || !ParseClassTypeSupport.isUserdefined(parseClass) || parseClass.getName().endsWith("Enum");
    }

    private static String getProName(Method resultMethod) {
        String methodName = resultMethod.getName();
        String propName = "";
        if ((methodName.startsWith("get") || methodName.startsWith("is"))
                && !Void.class.equals(resultMethod.getReturnType()) && !methodName.equals("get")
                && !methodName.equals("getClass") && !methodName.equals("is") && !methodName.equals("getDeclaringClass")) {

            if (methodName.startsWith("is")) {
                propName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
            } else {
                propName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            }
        }
        return propName;
    }

    public static List<String> getSuperClassTypes(Class<?> clazz) {
        List<String> res = null;
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.getName().startsWith("java")) {
            res = new ArrayList<>();
            res.add(superClass.getName());
            superClass = superClass.getSuperclass();
        }
        while (superClass != null && !superClass.getName().startsWith("java")) {
            res.add(superClass.getName());
            superClass = superClass.getSuperclass();
        }
        return res;
    }

}
