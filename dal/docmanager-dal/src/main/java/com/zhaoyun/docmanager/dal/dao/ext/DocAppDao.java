package com.zhaoyun.docmanager.dal.dao.ext;

import java.util.List;

import com.zhaoyun.docmanager.dal.dao.MBGDocAppDao;
import com.zhaoyun.docmanager.model.dvo.AppAddPramDO;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.param.AppQueryParam;

public interface DocAppDao extends MBGDocAppDao {

    /**  
     * @param appAddPramDO
     */
    void addApp(AppAddPramDO appAddPramDO);

    List<DocApp> queryAll(AppQueryParam appQueryPram);

}