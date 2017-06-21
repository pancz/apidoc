package com.zhaoyun.docmanager.biz.service;

import com.zhaoyun.docmanager.biz.bean.diff.PomDiff;

/**
 * Created on 2016/8/15 10:26.
 * 未来不确定才更值得前行
 *
 * @author user
 */
public interface APIComparatorService {

//    PomDiff diffPom(Integer appId, String v1, String v2);

    PomDiff diffPom(Integer appId, String v1, String v2);

}
