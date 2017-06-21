package com.zhaoyun.docmanager.dal.dao.ext;

import com.zhaoyun.docmanager.dal.dao.MBGDocServiceDao;
import com.zhaoyun.docmanager.model.dvo.DocService;
import com.zhaoyun.docmanager.model.param.ServiceQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocServiceDao extends MBGDocServiceDao {

    List<DocService> queryAllByAppVersionId(ServiceQueryParam serviceQueryPram);

    /**  
     * @param docServiceList
     */
    void batchDeleteById(@Param("docServiceList") List<DocService> docServiceList);
}