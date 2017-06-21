/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author user
 * @version $Id: AppMessageQueryParam.java, v 0.1 2016年8月29日 下午9:19:08 user Exp $
 */
public class AppMessageQueryParam extends PagedQueryParam {

    /**  */
    private static final long serialVersionUID = 1L;

    /**  应用id*/
    private Integer           appId;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
