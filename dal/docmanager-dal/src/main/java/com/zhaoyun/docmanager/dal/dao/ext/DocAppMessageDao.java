package com.zhaoyun.docmanager.dal.dao.ext;

import java.util.List;

import com.zhaoyun.docmanager.dal.dao.MBGDocAppMessageDao;
import com.zhaoyun.docmanager.model.dvo.DocAppMessage;
import com.zhaoyun.docmanager.model.param.AppMessageQueryParam;

public interface DocAppMessageDao extends MBGDocAppMessageDao {
    List<DocAppMessage> queryAllByAppId(AppMessageQueryParam appMessageQueryParam);

    List<DocAppMessage> queryAllWithBLOBsByAppId(AppMessageQueryParam appMessageQueryParam);

    int batchReplace(List<DocAppMessage> docAppMessages);

    int batchDelete(List<Integer> ids);
}