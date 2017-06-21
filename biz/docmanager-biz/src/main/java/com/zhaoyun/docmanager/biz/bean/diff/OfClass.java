package com.zhaoyun.docmanager.biz.bean.diff;

/**
 * Created on 2016/8/16 20:01.
 * 未来不确定才更值得前行
 *
 * @author user
 */
public class OfClass {
    private String fieldName;
    private String fieldType;
    private OfClass ofClass;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public OfClass getOfClass() {
        return ofClass;
    }

    public void setOfClass(OfClass ofClass) {
        this.ofClass = ofClass;
    }
}