/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.apiparse.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 解析的对象，可以用来表示 类/出参/入参
 *
 * @author user
 * @version $Id: ParamDesc.java, v 0.1 2016年4月19日 下午4:19:18 user Exp $
 */
public class FieldsObj implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 所属于类
     */
    private String            ofClassType;

    /** 父类集合 */
    private List<String>      superClassTypes;
    /**
     * 类型
     */
    private String            classType;
    /**
     * 对应列名次
     */
    private String            fieldName;
    /**
     * 注释
     */
    private String            fieldComment;
    /**
     * 对象
     */
    private List<FieldsObj>   fieldObjs;
    /**
     * 索引位置
     */
    private int               index;
    /**
     * 全部类型 从外层到里层
     */
    private String            classFullType;

    /**
     * 1 基本类型，2 array, 3 Collection, 4 Map, 5 自定义类型
     */
    private Integer           typeCode;

    public List<String> getSuperClassTypes() {
        return superClassTypes;
    }

    public void setSuperClassTypes(List<String> superClassTypes) {
        this.superClassTypes = superClassTypes;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getClassFullType() {
        return classFullType;
    }

    public void setClassFullType(String classFullType) {
        this.classFullType = classFullType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOfClassType() {
        return ofClassType;
    }

    public void setOfClassType(String ofClassType) {
        this.ofClassType = ofClassType;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public List<FieldsObj> getFieldObjs() {
        return fieldObjs;
    }

    public void setFieldObjs(List<FieldsObj> fieldObj) {
        this.fieldObjs = fieldObj;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
