package com.zhaoyun.docmanager.common.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Created by user on 2016/8/5 16:19.
 * 未来不确定才更值得前行
 */
public class HttpUtil {
    protected static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static void downloadToLocal(String url, String fileName) {
        try {
            URL httpUrl = new URL(url);
            File f = new File(fileName);
            FileUtils.copyURLToFile(httpUrl, f);
        } catch (java.io.FileNotFoundException ex) {
            throw new RuntimeException("不存在的资源");
        } catch (Exception e) {
            log.error("下载文件发生异常", e);
        }
    }
}
