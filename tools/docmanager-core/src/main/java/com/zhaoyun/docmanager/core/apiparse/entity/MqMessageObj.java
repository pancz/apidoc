package com.zhaoyun.docmanager.core.apiparse.entity;

import java.util.List;

/**
 * Created on 2016/8/31 10:14.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
public class MqMessageObj {
    private List<String> topicList;
    private List<String> messageTypeList;
    private List<FieldsObj> messageEntityList;

    public List<String> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }

    public List<String> getMessageTypeList() {
        return messageTypeList;
    }

    public void setMessageTypeList(List<String> messageTypeList) {
        this.messageTypeList = messageTypeList;
    }


    public List<FieldsObj> getMessageEntityList() {
        return messageEntityList;
    }

    public void setMessageEntityList(List<FieldsObj> messageEntityList) {
        this.messageEntityList = messageEntityList;
    }
}
