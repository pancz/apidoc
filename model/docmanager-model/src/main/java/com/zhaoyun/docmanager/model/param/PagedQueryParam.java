/**
 * com Inc
 * Copyright (c) 2014-2015年11月16日 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import java.io.Serializable;

import com.zhaoyun.docmanager.model.dvo.BaseExample;

/**
 * 分页查询对象
 *
 * @author user
 * @since Revision:1.0.0, Date: 2015年11月16日 下午2:58:44 
 */
public class PagedQueryParam extends BaseExample implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int defaultPageSize = 20;
	public static final int defaultPageNo = 1;
	
	private Integer pageSize = defaultPageSize;
	private Integer pageNo = defaultPageNo;
	
    /**
     * 排序方式
     */
    private String            sortType         = "desc";
    /**
     * 排序列
     */
    private String            sortColumn       = "create_time";

	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize == null || defaultPageSize <= 0 ? defaultPageSize : pageSize;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo == null || pageNo <= 0 ? defaultPageNo : pageNo;
	}
	
	public Integer getStartRow() {
		return (pageNo - 1) * pageSize;
	}

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
	
}
