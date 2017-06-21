package com.zhaoyun.docmanager.dal.dao.ext;

import java.util.List;

import com.zhaoyun.docmanager.dal.dao.MBGDocAppVersionDao;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;

public interface DocAppVersionDao extends MBGDocAppVersionDao {

    List<DocAppVersion> queryList(AppVersionQueryParam appVersionQueryPram);

    int queryCount(AppVersionQueryParam appVersionQueryPram);

    List<String> queryAllVersionStr(AppVersionQueryParam appVersionQueryParam);
}