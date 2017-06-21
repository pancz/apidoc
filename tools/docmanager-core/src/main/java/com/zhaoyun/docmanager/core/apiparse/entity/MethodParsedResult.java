package com.zhaoyun.docmanager.core.apiparse.entity;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * 方法的解析结果
 *
 * @author user
 * @version $Id: ResMethod.java, v 0.1 2016年4月15日 上午10:48:32 user Exp $
 */
public class MethodParsedResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法描述
     */
    private String methodComment;
    /**
     * 返回描述
     */
    private String returnComment;
    /**
     * 返回结果
     */
    private FieldsObj returnObj;
    /**
     * 参数列表
     */
    private List<FieldsObj> paramList = Lists.newArrayList();

    /**
     * 是否过时的
     */
    private boolean isDeprecated;

    /** @name注解  */
    private String            nameComment;

    /** @author注解  */
    private String            authorComment;

    /** @date注解  */
    private String            dateComment;

    /** 全部注释  */
    private String            fullComment;

    public String getFullComment() {
        return fullComment;
    }

    public void setFullComment(String fullComment) {
        this.fullComment = fullComment;
    }

    public String getNameComment() {
        return nameComment;
    }

    public void setNameComment(String nameComment) {
        this.nameComment = nameComment;
    }

    public String getAuthorComment() {
        return authorComment;
    }

    public void setAuthorComment(String authorComment) {
        this.authorComment = authorComment;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public String getReturnComment() {
        return returnComment;
    }

    public void setReturnComment(String returnComment) {
        this.returnComment = returnComment;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodComment() {
        return methodComment;
    }

    public void setMethodComment(String methodComment) {
        this.methodComment = methodComment;
    }

    public FieldsObj getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(FieldsObj returnObj) {
        this.returnObj = returnObj;
    }

    public List<FieldsObj> getParamList() {
        return paramList;
    }

    public void setParamList(List<FieldsObj> paramList) {
        this.paramList = paramList;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }
}
