/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.ArrayUtils;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * 序列化工具类，基于hessian
 * 
 * @author user
 * @version $Id: SerializeUtils.java, v 0.1 2015年9月11日 下午7:04:17 user Exp $
 */
public class SerializeUtils {
    //序列化
    public static byte[] serialize(Object object) {
        if(object == null) { return null; }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HessianOutput hos = new HessianOutput(baos);
            hos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //反序列化
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if(ArrayUtils.isEmpty(bytes)) { return null; }

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            HessianInput input = new HessianInput(bais);
            return (T)input.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
