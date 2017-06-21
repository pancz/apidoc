/**
 * com Inc
 * Copyright (c) 2014-2015年11月18日 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.validate;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 类的属性验信息保存对象，用于封装校验的数据。外部不直接使用此类
 * 
 *
 * @author user
 * @since Revision:1.0.0, Date: 2015年11月18日 上午10:41:28 
 */
public class FieldValidator implements Comparable<FieldValidator> {
	private static final String defaultDateFormate = "yyyy-MM-dd HH:mm:ss";
	
	private Validate validate;
	private Field field;
	private Pattern regexpPattern;
	private Pattern typePattern;
	private Double maxValue;
	private Double minValue;
	private Object defaultValue;
	private Set<String> enumValues;
	
	public FieldValidator(Validate validate, Field field) throws ParseException{
		this.field = field;
		this.validate = validate;
		
		if (StringUtils.isNotBlank(validate.regexp())) {
			this.regexpPattern = Pattern.compile(validate.regexp());
		}
		
		typePattern = initTypePattern(validate.type());
		
		if (StringUtils.isNotBlank(validate.maxValue())) {
			maxValue = Double.valueOf(validate.maxValue());
		}
		
		if (StringUtils.isNotBlank(validate.minValue())) {
			minValue = Double.valueOf(validate.minValue());
		}
		
		if (StringUtils.isNotBlank(validate.defaultValue())) {
			defaultValue = parseDefaultValue(validate, field);
		}
		
		if (StringUtils.isNotBlank(validate.enumValues())) {
			Set<String> vSet = new HashSet<String>();
			for (String value : validate.enumValues().split(",")) {
				vSet.add(value);
			}
			enumValues = vSet;
		}
		
	}
	
	private Pattern initTypePattern(String type) {
		String typeExp = null;
		if ("EMAIL".equalsIgnoreCase(validate.type())) {
			typeExp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		} else if ("IP".equalsIgnoreCase(validate.type())) {
			typeExp = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
		} else if ("NUMBER".equalsIgnoreCase(validate.type())) {
			typeExp = "^\\d+$";
		} else if ("PHONENUMBER".equalsIgnoreCase(validate.type())) {
			typeExp = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
		} else if ("URL".equalsIgnoreCase(validate.type())) {
			typeExp = "[a-zA-z]+:\\/[^\\s]*";
		} else if ("DATE".equalsIgnoreCase(validate.type())) {
			typeExp = "^([1][7-9][0-9][0-9]|[2][0][0-9][0-9])(\\-)([0][1-9]|[1][0-2])(\\-)([0-2][1-9]|[3][0-1])( )([0-1][0-9]|[2][0-3])(:)([0-5][0-9])(:)([0-5][0-9])$";
		}
			
		return typeExp != null ? Pattern.compile(typeExp) : null;
	}

	private Object parseDefaultValue(Validate validate, Field field) throws ParseException {
		Object value = null;
		if (byte.class.isAssignableFrom(field.getType()) || Byte.class.isAssignableFrom(field.getType())) {
			value = Byte.valueOf(validate.defaultValue());
		} else if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
			value = Integer.valueOf(validate.defaultValue());
		} else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
			value = Long.valueOf(validate.defaultValue());
		} else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
			value = Float.valueOf(validate.defaultValue());
		} else if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
			value = Double.valueOf(validate.defaultValue());
		} else if (boolean.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType())) {
			value = Boolean.valueOf(validate.defaultValue());
		} else if (String.class.isAssignableFrom(field.getType())) {
			value = String.valueOf(validate.defaultValue());
		} else if (Date.class.isAssignableFrom(field.getType())) {
			if (StringUtils.isNumeric(validate.defaultValue())) {
				value = new Date(Long.valueOf(validate.defaultValue()));
			} else {
				value = DateUtils.parseDate(validate.defaultValue(), defaultDateFormate);
			}
		}
		return value;
	}

	public Validate getValidate() {
		return validate;
	}

	public Field getField() {
		return field;
	}

	public Pattern getRegexpPattern() {
		return regexpPattern;
	}

	public Pattern getTypePattern() {
		return typePattern;
	}

	public Number getMaxValue() {
		return maxValue;
	}

	public Number getMinValue() {
		return minValue;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Set<String> getEnumValues() {
		return enumValues;
	}

	@Override
	public int compareTo(FieldValidator o) {
		return field.getName().compareTo(o.getField().getName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FieldValidator)) {
			return false;
		} else {
			return field.getName().equals(((FieldValidator)o).getField().getName());
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
