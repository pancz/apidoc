package com.zhaoyun.docmanager.model.vo;

import java.util.List;

/**
 * Created on 2016/8/31 9:59.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
public class MqMessageVo<T> {
    private List<String> topicList;
    private List<String> messageTypeList;
    private List<T> messageEntityList;

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

    public List<T> getMessageEntityList() {
        return messageEntityList;
    }

    public void setMessageEntityList(List<T> messageEntityList) {
        this.messageEntityList = messageEntityList;
    }
}