package com.zhaoyun.docmanager.biz.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.zhaoyun.docmanager.biz.service.AppService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.zhaoyun.docmanager.core.apiparse.APIParse;
import com.zhaoyun.docmanager.core.util.StringUtil;
import com.zhaoyun.docmanager.core.util.VersionCompareUtil;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppDao;
import com.zhaoyun.docmanager.dal.dao.ext.DocAppVersionDao;
import com.zhaoyun.docmanager.model.dvo.DocApp;
import com.zhaoyun.docmanager.model.dvo.DocAppVersion;
import com.zhaoyun.docmanager.model.param.AppQueryParam;
import com.zhaoyun.docmanager.model.param.AppVersionAddParam;
import com.zhaoyun.docmanager.model.param.AppVersionQueryParam;

/**
 * Created on 2016/8/26 11:32.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
@Service
public class MaintainAllAppTask implements InitializingBean{
    protected static Logger     LOGGER = LoggerFactory.getLogger(MaintainAllAppTask.class);
    @Autowired
    private DocAppDao           docAppDao;
    @Autowired
    private DocAppVersionDao    docAppVersionDao;
    @Autowired
    private AppService appService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    public void syncNexus() {
        LOGGER.info("***********start sync nexus***************");
        List<DocApp> docAppList = docAppDao.queryAll(new AppQueryParam());
        Map<String, DocApp> existAppMap = new HashMap<>();
        for (DocApp docApp : docAppList) {
            existAppMap.put(docApp.getName(), docApp);
        }
        Map<String, APIParse.AppNexusEntity> allAppMap = APIParse.getAllAppWithAllVersion();
        Iterator<Map.Entry<String, APIParse.AppNexusEntity>> ite = allAppMap.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, APIParse.AppNexusEntity> entry = ite.next();
            String app = entry.getKey();
            if (existAppMap.containsKey(app)) {
                DocApp docApp = existAppMap.get(app);
                String releaseLastUpdated = docApp.getReleaseTimeVersion();
                String snapshotLastUpdated = docApp.getSnapshotTimeVersion();
                APIParse.AppNexusEntity entity = allAppMap.get(app);
                String nexusReleaseLastUpdated = entity.getReleaseLastUpdated();
                String nexusSnapshotLastUpdated = entity.getSnapshotLastUpdated();
                final List<String> list = entity.getReleaseVersionList();
                list.addAll(entity.getSnapshotVersionList());
                final SyncAppEntity syncEntity = new SyncAppEntity();
                if (!Objects.equals(releaseLastUpdated, nexusReleaseLastUpdated)) {
                    syncEntity.setAppName(app);
                    syncEntity.setAppId(docApp.getId());
                    syncEntity.setUdpateRelease(true);
                    syncEntity.setReleaseLastUpdated(nexusReleaseLastUpdated);
                }
                if (!Objects.equals(snapshotLastUpdated, nexusSnapshotLastUpdated)) {
                    syncEntity.setAppName(app);
                    syncEntity.setAppId(docApp.getId());
                    syncEntity.setUpdateSnapshot(true);
                    syncEntity.setSnapshotLastUpdated(nexusSnapshotLastUpdated);
                }
                if (StringUtil.isNotEmpty(syncEntity.getAppName())) {
                    processSyncAppEntity(syncEntity, list);
                }
                ite.remove();
            }
        }
        Set<String> addAppSet = allAppMap.keySet();
        for (final String key : addAppSet) {
            final APIParse.AppNexusEntity entity = allAppMap.get(key);
            final List<String> list = entity.getReleaseVersionList();
            list.addAll(entity.getSnapshotVersionList());
            addApp(key, list, entity.getSnapshotLastUpdated(), entity.getReleaseLastUpdated());

        }
        LOGGER.info("***********end sync nexus***************");

    }

    /*暂时注释
        public void addApp(final String appName, final List<String> versionList,
                           final String snapshotLastUpdated, final String releaseLastUpdated) {
            LOGGER.info(
                "START addApp. appName:{},versionList:{},snapshotLastUpdated:{},releaseLastUpdated:{}",
                appName, versionList, snapshotLastUpdated, releaseLastUpdated);
            transactionTemplate.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    try {
                        DocApp da = new DocApp();
                        da.setGroupId("com.zhaoyun." + appName);
                        da.setArtifactId(appName + "-facade");
                        da.setSnapshotTimeVersion(snapshotLastUpdated);
                        da.setReleaseTimeVersion(releaseLastUpdated);
                        da.setName(appName);
                        docAppDao.insertSelective(da);
                        for (String v : versionList) {
                            AppVersionAddParam p = new AppVersionAddParam();
                            p.setName(appName);
                            p.setVersion(v);
                            try {
                                appService.addVersion(p);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("", e);
                        status.setRollbackOnly();
                    }
                    return null;
                }
            });
            LOGGER.info(
                "END addApp. appName:{},versionList:{},snapshotLastUpdated:{},releaseLastUpdated:{}",
                appName, versionList, snapshotLastUpdated, releaseLastUpdated);
        }
        */
    public void addApp(final String appName, final List<String> versionList,
                       final String snapshotLastUpdated, final String releaseLastUpdated) {
        LOGGER.info(
            "START addApp. appName:{},versionList:{},snapshotLastUpdated:{},releaseLastUpdated:{}",
            appName, versionList, snapshotLastUpdated, releaseLastUpdated);

        try {
            DocApp da = new DocApp();
            da.setGroupId("com.zhaoyun." + appName);
            da.setArtifactId(appName + "-facade");
            da.setSnapshotTimeVersion(snapshotLastUpdated);
            da.setReleaseTimeVersion(releaseLastUpdated);
            da.setName(appName);
            docAppDao.insertSelective(da);
            for (String v : versionList) {
                AppVersionAddParam p = new AppVersionAddParam();
                p.setName(appName);
                p.setVersion(v);
                try {
                    appService.addVersion(p);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        LOGGER.info(
            "END addApp. appName:{},versionList:{},snapshotLastUpdated:{},releaseLastUpdated:{}",
            appName, versionList, snapshotLastUpdated, releaseLastUpdated);
    }

    public void processSyncAppEntity(final SyncAppEntity se, final List<String> nexusAppVersionList) {
        LOGGER.info("START processSyncAppEntity. syncAppEntity:{},nexusAppVersionList:{}", se,
            nexusAppVersionList);
        boolean success = true;
        String appName = se.getAppName();
        Integer appId = se.getAppId();
        try {
            AppVersionQueryParam versionQueryParam = new AppVersionQueryParam();
            versionQueryParam.setAppId(appId);
            List<DocAppVersion> docAppVersionList = docAppVersionDao.queryList(versionQueryParam);
            List<String> appVersionList = new ArrayList<>();
            for (DocAppVersion dav : docAppVersionList) {
                appVersionList.add(dav.getVersion());
            }
            for (Iterator<String> ite = nexusAppVersionList.iterator(); ite.hasNext();) {
                String nv = ite.next();
                if (appVersionList.contains(nv)) {
                    ite.remove();
                }
            }
            if (!nexusAppVersionList.isEmpty()) {
                for (String version : nexusAppVersionList) {
                    AppVersionAddParam p = new AppVersionAddParam();
                    p.setName(appName);
                    p.setVersion(version);
                    try {
                        appService.addVersion(p);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                String latestVersion = "";
                for (String version : nexusAppVersionList) {
                    if (VersionCompareUtil.compare(version, latestVersion) > 0) {
                        latestVersion = version;
                    }
                }
                if (!"".equals(latestVersion)) {
                    AppVersionAddParam p = new AppVersionAddParam();
                    p.setName(appName);
                    p.setVersion(latestVersion);
                    try {
                        appService.addVersion(p);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
            success = false;
        }
        if (success) {
            DocApp da = new DocApp();
            da.setId(appId);
            if (se.isUpdateRelease()) {
                da.setReleaseTimeVersion(se.getReleaseLastUpdated());
            }
            if (se.isUpdateSnapshot()) {
                da.setSnapshotTimeVersion(se.getSnapshotLastUpdated());
            }
            if (StringUtil.isNotEmpty(da.getReleaseTimeVersion())
                || StringUtil.isNotEmpty(da.getSnapshotTimeVersion())) {
                docAppDao.updateByPrimaryKeySelective(da);
            }
        }
        LOGGER.info("END processSyncAppEntity. syncAppEntity:{},nexusAppVersionList:{}", se,
            nexusAppVersionList);
    }

    public static class SyncAppEntity {
        private Integer appId;
        private String  appName;
        private boolean updateSnapshot;
        private String  snapshotLastUpdated;
        private boolean updateRelease;
        private String  releaseLastUpdated;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public boolean isUpdateSnapshot() {
            return updateSnapshot;
        }

        public void setUpdateSnapshot(boolean updateSnapshot) {
            this.updateSnapshot = updateSnapshot;
        }

        public boolean isUpdateRelease() {
            return updateRelease;
        }

        public void setUdpateRelease(boolean updateRelease) {
            this.updateRelease = updateRelease;
        }

        public Integer getAppId() {
            return appId;
        }

        public void setAppId(Integer appId) {
            this.appId = appId;
        }

        public String getSnapshotLastUpdated() {
            return snapshotLastUpdated;
        }

        public void setSnapshotLastUpdated(String snapshotLastUpdated) {
            this.snapshotLastUpdated = snapshotLastUpdated;
        }

        public String getReleaseLastUpdated() {
            return releaseLastUpdated;
        }

        public void setReleaseLastUpdated(String releaseLastUpdated) {
            this.releaseLastUpdated = releaseLastUpdated;
        }
    }

	@Override
	public void afterPropertiesSet() throws Exception {
//		syncNexus();
		
	}
}
