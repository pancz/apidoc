package com.zhaoyun.docmanager.model.vo;

/**
 * 关键字搜索返回对象VO
 *
 * @author user
 */
public class SearchIndexVo {
    /**
     * Facade名称
     */
    private String serviceName;
    /**
     * Facade说明
     */
    private String serviceDesc;
    /**
     * api名称
     */
    private String apiName;
    /**
     * api说明
     */
    private String apiDesc;
    /**
     * vo类型 [1:Facade 2:api]
     */
    private int keyType;
    /**
     * 记录id
     */
    private int keyId;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
}
