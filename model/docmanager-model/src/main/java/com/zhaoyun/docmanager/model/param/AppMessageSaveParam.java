/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import java.io.Serializable;
import java.util.List;

import com.zhaoyun.docmanager.model.dvo.DocAppMessage;

/**
 * 
 * @author user
 * @version $Id: AppMessageSaveParam.java, v 0.1 2016年8月30日 下午1:51:44 user Exp $
 */
public class AppMessageSaveParam implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private List<DocAppMessage> updateList;

    private List<Integer>       deleteIds;

    public List<DocAppMessage> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<DocAppMessage> updateList) {
        this.updateList = updateList;
    }

    public List<Integer> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<Integer> deleteIds) {
        this.deleteIds = deleteIds;
    }

}
