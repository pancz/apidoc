/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.model.param;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author user
 * @version $Id: AppQueryPram.java, v 0.1 2016年8月15日 下午5:25:40 user Exp $
 */
public class AppQueryParam extends PagedQueryParam {

    /**  */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        System.out.println(getSuperClassTypes(AppQueryParam.class));
    }

    public static List<String> getSuperClassTypes(Class<?> clazz) {
        List<String> res = null;
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.getName().startsWith("java")) {
            res = new ArrayList<>();
            res.add(superClass.getName());
            superClass = superClass.getSuperclass();
        }
        while (superClass != null && !superClass.getName().startsWith("java")) {
            res.add(superClass.getName());
            superClass = superClass.getSuperclass();
        }
        return res;
    }

}
