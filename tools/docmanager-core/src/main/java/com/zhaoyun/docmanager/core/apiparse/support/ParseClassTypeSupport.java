package com.zhaoyun.docmanager.core.apiparse.support;

import java.util.Arrays;
import java.util.List;

/**
 * 类型支持
 *
 * @author user
 * @version $Id: ClassTypeSupport.java, v 0.1 2016年4月25日 下午3:35:22 user Exp $
 */
public class ParseClassTypeSupport {

    /*基础数据类型以及封装类*/
    static String javaBaseType[] = {"byte", "java.lang.Byte", "short", "java.lang.Short", "int", "java.lang.Integer",
            "long", "java.lang.Long", "double", "java.lang.Double", "float", "java.lang.Float", "boolean",
            "java.lang.Boolean", "java.lang.String", "java.util.Date"};
    /*基础引用类型*/
    public static List<String> baseTypeList = Arrays.asList(javaBaseType);

    public static boolean isBaseType(Class<?> classType) {
        return baseTypeList.contains(classType.getName()) || classType.isEnum();
    }

    public static boolean isArray(Class<?> classType) {
        return classType.getName().startsWith("[");
    }

    public static boolean isCollection(Class<?> clazz) {
        return java.util.Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Class<?> classType) {
        return java.util.Map.class.isAssignableFrom(classType);
    }

    public static boolean isUserdefined(Class<?> classType) {
        return (classType.getName().startsWith("com.zhaoyun") || classType.getName().startsWith("com.toowell"));
    }

    public static Integer getTypeCode(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        if (isBaseType(clazz)) {
            return 1;
        }
        if (isArray(clazz)) {
            return 2;
        }
        if (isCollection(clazz)) {
            return 3;
        }
        if (isMap(clazz)) {
            return 4;
        }
        if (isUserdefined(clazz)) {
            return 5;
        }
        return null;
    }

    public enum ClassCode {

        BASE(1), ARRAY(2), COLLECTION(3), MAP(4), USER_DEFINE(5);

        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        private ClassCode(int code) {
            this.code = code;
        }

        public static ClassCode getCode(int code) {
            for (ClassCode value : ClassCode.values()) {
                if (value.getCode() == code) {
                    return value;
                }
            }
            return null;
        }
    }

}
