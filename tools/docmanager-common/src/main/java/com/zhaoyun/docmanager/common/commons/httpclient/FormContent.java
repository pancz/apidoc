/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.httpclient;

import static com.google.common.base.Preconditions.checkArgument;
import static com.zhaoyun.docmanager.common.commons.io.FileUtils.isValidFile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 提交的表单对象
 * <p>
 *  sample:
 *    <pre>{@code FormContent formContent= FormContent.create()
                                    .addText("username", "tom")
                                    .addFile("file", new File("d://test.csv"));
            }
        </pre> 
 * </p>
 * @author user
 * @version $Id: InputContent.java, v 0.1 2016年7月15日 下午2:16:41 user Exp $
 */
public class FormContent {
    private Map<String, String> textMap = Maps.newHashMap();
    private Map<String, File>   fileMap = Maps.newHashMap();

    private FormContent() {
    }

    /**
     * 创建一个FormContent对象
     * @return
     * @date 2016年7月15日 下午5:12:34
     */
    public static FormContent create() {
        return new FormContent();
    }

    /**
     * 向表单FormContent中添加文本
     * @param name 表单name
     * @param value  文本值
     * @return
     * @date: 2016年7月15日 下午5:37:27
     */
    public FormContent addText(String name, String value) {
        checkArgument(isNotBlank(name), "name must not be blank");
        checkArgument(isNotBlank(value), "value must not be blank");

        textMap.put(name, value);
        return this;
    }

    /**
     * *向表单FormContent中添加文件
     * @param name 表单name
     * @param file 文件对象
     * @return
     * @date: 2016年7月15日 下午5:38:04
     */
    public FormContent addFile(String name, File file) {
        checkArgument(isNotBlank(name), "name must not be blank");
        checkArgument(isValidFile(file), "param file is  a invalid file");

        fileMap.put(name, file);
        return this;
    }

    Map<String, String> getTextMap() {
        return textMap;
    }

    Map<String, File> getFileMap() {
        return fileMap;
    }

}
