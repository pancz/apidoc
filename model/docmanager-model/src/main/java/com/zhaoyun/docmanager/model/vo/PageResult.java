/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */

package com.zhaoyun.docmanager.model.vo;

import java.util.List;

/**
 * 
 * @author user
 * @version $Id: PageResult.java, v 0.1 2016年8月15日 下午6:03:31 user Exp $
 */
public class PageResult<T> {
    /**
     * 总个数
     */
    private int     total;

    /**  总页数*/
    private int     pages;

    /**
     * 数据
     */
    private List<T> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

}
