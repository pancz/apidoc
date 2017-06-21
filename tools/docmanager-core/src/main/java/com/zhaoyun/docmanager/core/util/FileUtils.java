/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.core.util;

import com.zhaoyun.docmanager.core.exception.BizException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author user
 * @version $Id: FileUtils.java, v 0.1 2016年4月14日 下午3:12:47 user Exp $
 */
public class FileUtils {

    /**
     * 从远程url获取文件源并在本地创建文件
     *
     * @param remoteUrl    远程url地址
     * @param localAbsPath 本地文件绝对路径
     * @return
     * @throws IOException
     * @throws MalformedURLException
     * @date: 2016年4月14日 下午3:57:23
     */
    public static File downLoadFile(String remoteUrl, String savePath, String fileName)
            throws MalformedURLException,
            IOException {
        if (StringUtil.isBlank(remoteUrl)) {
            throw new BizException("remoteUrl is blank");
        }
        if (StringUtil.isBlank(savePath) || StringUtil.isBlank(fileName)) {
            throw new BizException("locpath or filename is blank");
        }
        File file = new File(savePath + fileName);
        // file.deleteOnExit();
        org.apache.commons.io.FileUtils.copyURLToFile(new URL(remoteUrl), file);
        return file;
    }

    /**
     * 解压压缩文件到指定目录，并返回所有解压文件的绝对路径
     *
     * @param zip       压缩文件对象
     * @param unzipPath 解压目的文件夹
     * @return
     * @throws IOException
     * @date: 2016年4月14日 下午3:14:41
     */
    public static List<String> unZipFiles(ZipFile zip, String unzipPath) throws IOException {
        List<String> fileLoc = new ArrayList<>();
        for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (unzipPath + "/" + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
            fileLoc.add(outPath);
        }
        return fileLoc;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean
     */
    public static void deleteDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                assert files != null;
                for (File subFile : files) {
                    if (subFile.isDirectory())
                        deleteDir(subFile.getPath());
                    else
                        subFile.delete();
                }
            }
            file.delete();
        }
    }

}
