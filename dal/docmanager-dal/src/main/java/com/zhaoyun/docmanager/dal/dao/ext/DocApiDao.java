package com.zhaoyun.docmanager.dal.dao.ext;

import com.zhaoyun.docmanager.dal.dao.MBGDocApiDao;
import com.zhaoyun.docmanager.model.dvo.DocApi;
import com.zhaoyun.docmanager.model.param.ApiQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocApiDao extends MBGDocApiDao {

    List<DocApi> queryAllByServiceId(ApiQueryParam apiQueryPram);

    List<DocApi> queryAllWithBLOBsByServiceId(ApiQueryParam apiQueryPram);

    /**  
     * @param docApiList
     */
    void batchDeleteById(@Param("docApiList") List<DocApi> docApiList);
}