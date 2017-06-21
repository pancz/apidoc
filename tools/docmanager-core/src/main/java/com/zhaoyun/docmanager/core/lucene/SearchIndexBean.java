package com.zhaoyun.docmanager.core.lucene;

import org.apache.lucene.document.Document;

/**
 * Created by user on 2016/8/10 13:25.
 * 未来不确定才更值得前行
 */
public class SearchIndexBean {
    private String key;
    public static final String KEY = "key";
    private String serviceName;
    public static final String SERVICE_NAME = "serviceName";
    private String serviceDesc;
    public static final String SERVICE_DESC = "serviceDesc";
    private String apiName;
    public static final String API_NAME = "apiName";
    private String apiDesc;
    public static final String API_DESC = "apiDesc";
    private int keyType;
    public static final String KEY_TYPE = "keyType";
    private int keyId;
    public static final String KEY_ID = "keyId";
    private int pomId;
    public static final String POM_ID = "pomId";

    public static SearchIndexBean toBeanFromIndex(Document doc) {
        SearchIndexBean b = new SearchIndexBean();
        b.setApiDesc(doc.get(API_DESC));
        b.setKey(doc.get(KEY));
        b.setServiceName(doc.get(SERVICE_NAME));
        b.setServiceDesc(doc.get(SERVICE_DESC));
        b.setApiName(doc.get(API_NAME));
        b.setKeyType(Integer.valueOf(doc.get(KEY_TYPE)));
        b.setKeyId(Integer.valueOf(doc.get(KEY_ID)));
        return b;
    }

    @Override
    public String toString() {
        return "SearchIndexBean{" +
                "key='" + key + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDesc='" + serviceDesc + '\'' +
                ", apiName='" + apiName + '\'' +
                ", apiDesc='" + apiDesc + '\'' +
                ", keyType=" + keyType +
                ", keyId=" + keyId +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public int getPomId() {
        return pomId;
    }

    public void setPomId(int pomId) {
        this.pomId = pomId;
    }
}
