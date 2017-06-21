package com.zhaoyun.docmanager.core.apiparse.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类解析结果
 *
 * @author user
 * @version $Id: JavaParseResult.java, v 0.1 2016年4月14日 下午3:23:59 user Exp $
 */
public class ClassParsedResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类全限定名
     */
    private String classQuaName;
    /**
     * 类方法列表
     */
    private List<MethodParsedResult> methodList;

    /**
     * 作者
     */
    private String author;

    /**
     * 版本
     */
    private String version;

    /**
     * 描述
     */
    private String desc;

    private boolean isReflectParsed;

    private boolean isDeprecated;

    public boolean isReflectParsed() {
        return isReflectParsed;
    }

    public void setReflectParsed(boolean isReflectParsed) {
        this.isReflectParsed = isReflectParsed;
    }

    public String getClassQuaName() {
        return classQuaName;
    }

    public void setClassQuaName(String classType) {
        this.classQuaName = classType;
    }

    public List<MethodParsedResult> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<MethodParsedResult> methodList) {
        this.methodList = methodList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }
}
