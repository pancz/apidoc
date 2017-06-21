package com.zhaoyun.docmanager.core.apiparse.entity;

import com.zhaoyun.docmanager.core.apiparse.support.ParseClassTypeSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created on 2016/8/29 13:32.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
public class JDTType {
    private Type type;
    private String className;
    private boolean isFX;
    private Class clz;

    public JDTType(Type genericType) {
        if (null != genericType) {
            this.type = genericType;
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                clz = ((Class) pt.getRawType());
                className = clz.getName();
                isFX = true;
            } else if (genericType instanceof TypeVariable) {
                TypeVariable tType = (TypeVariable) genericType;
                this.className = tType.getGenericDeclaration().toString();
            } else {
                clz = (Class) genericType;
                className = clz.getName();
//            isFX = false; boolean default is false;
            }
        }
    }

    public Integer getTypeCode() {
        return ParseClassTypeSupport.getTypeCode(clz);
    }

    public String getClassName() {
        return this.className;
    }


    public Type getType() {
        return type;
    }

    public boolean isFX() {
        return isFX;
    }
}
