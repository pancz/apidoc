package com.zhaoyun.docmanager.core.util;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 类url加载器，主要是为了重写addUrl方法
 *
 * @author user
 * @version $Id: MyUrlClassLoader.java, v 0.1 2016年4月25日 上午11:20:06 user Exp $
 */
public class MyUrlClassLoader extends URLClassLoader {

    public MyUrlClassLoader(URL[] urls) {
        super(urls, Thread.currentThread().getContextClassLoader());
    }

    public void addUrl(URL url) {
        super.addURL(url);
    }

}
