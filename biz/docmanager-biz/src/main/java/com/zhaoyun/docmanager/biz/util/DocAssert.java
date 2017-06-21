/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.biz.util;

import com.zhaoyun.docmanager.biz.exception.DocException;
import org.apache.commons.lang3.StringUtils;

import com.zhaoyun.docmanager.common.commons.validate.ValidateTools;
import com.zhaoyun.docmanager.common.knife.result.CommonStateCode;

/**
 * CardAssert
 *
 * @author user
 * @version : cardprod-parent, v 0.1 2016/7/7 10:51 user Exp $$
 */
public abstract class DocAssert {

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws DocException if the object is {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new DocException(CommonStateCode.ILLEGAL_PARAMETER,message);
        }
    }

    /**
     * 校验对象
     * @param obj
     */
    public static void validate(Object obj) {
        try {
            String message = ValidateTools.validate(obj);
            if (StringUtils.isNotBlank(message)) {
                throw new DocException(CommonStateCode.ILLEGAL_PARAMETER, message);
            }
        } catch (Exception e) {
            throw new DocException(CommonStateCode.ILLEGAL_PARAMETER, e.getMessage());
        }
    }

    /**
     * Assert that the given String is not empty; that is,
     * it must not be {@code null} and not the empty String.
     * <pre class="code">Assert.hasLength(name);</pre>
     * @param text the String to check
     * @see StringUtils#isblank
     */
    public static void hasLength(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new DocException(CommonStateCode.ILLEGAL_PARAMETER,message);
        }
    }
}
