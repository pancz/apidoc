/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.io;

import java.io.File;

/**
 * 文件操作工具类，继承了apache的FileUtils
 * @author user
 * @version $Id: FileUtils.java, v 0.1 2016年7月14日 下午9:12:33 user Exp $
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 是否是有效的文件
     * @param file
     * @return
     * @date: 2016年7月14日 下午9:04:14
     */
    public static boolean isValidFile(File file) {
        return file != null && 
               file.isFile() && 
               file.length() > 0;
    }
}
