/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.knife.result;

import java.io.Serializable;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;

/**
 * 状态码定义类
 * <p>由于StateCode通常作为常量类使用(实例唯一), 所以不开放属性的set/get方法</p>
 * 
 * @author user
 * @version $Id: StateCode.java, v 0.1 2015年9月10日 下午5:19:44 user Exp $
 */
public final class StateCode implements Serializable {
    
	private static final long serialVersionUID = -8555384052154889733L;

	/* 状态注册 */
	private final static Map<Integer, StateCode> lookup = Maps.newHashMap();
	/* 状态码值 */
    private Integer code;
    /* 状态描述 */
    private String desc;

    public StateCode(Integer code, String desc) {
    	this.code = code;
        this.desc = desc;
        //重复状态码侦测
        if(code != null && lookup.put(code, this) != null) {
        	throw new IllegalArgumentException(
    				String.format("duplicated code[%d]", code));
        }
    }

    /**
     * 根据状态码找到状态码类
     * 
     * @param code 状态码
     * @param clazz 要获取的stateCode的归属类，命名规范为：[APPID]StateCode
     * 
     * @return 状态码类实例
     */
    public static StateCode get(int code, Class<?> clazz) {
    	register(clazz);
    	StateCode stateCode = lookup.get(code);
    	/* 无效状态码 */
    	if(stateCode == null) { 
    		throw new IllegalArgumentException(
    				String.format("invalid code[%d]", code)); 
    	}

    	return stateCode;
    }

    /**
     * 激活状态码归集类
     *    
     * @param clazz 状态码归集类 
     * 
     * @author: user
     * @date: 2015年9月10日 下午5:20:35
     */
    public static void register(Class<?> clazz) {
    	try {
			Class.forName(clazz.getName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 状态码判定
     *   
     * @param code  状态码值
     * @param stateCode 要匹配的状态码类型
     * @return true 如果状态码成功匹配
     * @date: 2015年9月10日 下午5:21:32
     */
    public static boolean is(int code, StateCode stateCode) {
    	if(stateCode == null) { 
    		throw new IllegalArgumentException("stateCode is null");
    	}
    	
    	return stateCode.equals(get(code, StateCode.class));
    }

    /* 状态码 */
    public int getCode() {
        return code;
    }
    /* 状态码描述 */
    public String getDesc() {
        return desc;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null)  {  
			return false;  
		}

		if (getClass() != obj.getClass()) {  
			return false;
		}

		final StateCode other = (StateCode) obj;  

		return Objects.equal(this.code, other.code); 
	}

	@Override
	public int hashCode() {
		return HashCode.fromInt(code).asInt();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
						  .add("code", this.code)
						  .add("desc", this.desc)
						  .toString();
	}
}
