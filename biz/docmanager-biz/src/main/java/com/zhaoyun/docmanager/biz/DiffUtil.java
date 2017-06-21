package com.zhaoyun.docmanager.biz;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.zhaoyun.docmanager.common.util.CollectionUtil;
import com.zhaoyun.docmanager.common.util.StringUtil;
import com.zhaoyun.docmanager.core.apiparse.entity.FieldsObj;

/**
 * Created on 2016/8/25 15:21.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
public class DiffUtil {

    public static FieldsObj processAll(boolean needRemoveResult, FieldsObj o) {
        if (null == o) {
            return null;
        }
        if (needRemoveResult) {
            o = removeResult(o);
        }
        processFX(o);
        toSimplyName(o);
        List<FieldsObj> fieldObjs = o.getFieldObjs();
        if (CollectionUtil.isNotEmpty(fieldObjs)) {
            for (int i = 0; i < fieldObjs.size(); i++) {
                FieldsObj oo = fieldObjs.get(i);
                if (null != oo) {
                    oo = processAll(false, oo);
                    fieldObjs.set(i, oo);
                }
            }
        }
        return o;
    }

    public static void toSimplyName(FieldsObj obj) {
        if (null == obj) {
            return;
        }
        String name = obj.getClassType();
        if (StringUtil.isNotBlank(name)) {
            int i = name.lastIndexOf(".");
            if (-1 != i) {
                String pkg = name.substring(0, i);
                if (!pkg.startsWith("java.") || pkg.startsWith("java.lang")) {
                    obj.setClassType(name.substring(i + 1));
                }
            }
        }
    }

    public static void processFX(FieldsObj o1) {
        if (null == o1) {
            return;
        }

        if (Objects.equals(o1.getTypeCode(), 3) || Objects.equals(o1.getTypeCode(), 4)) {
            String type = o1.getClassType();
            type += "<";
            List<FieldsObj> fieldObjs = o1.getFieldObjs();
            if (CollectionUtils.isNotEmpty(fieldObjs)) {
              //对list
                if (Objects.equals(o1.getTypeCode(), 3)) {
                    FieldsObj value = fieldObjs.get(0);
                    o1.setFieldObjs(value.getFieldObjs());
                }
                //对map
                if (Objects.equals(o1.getTypeCode(), 4)) {
                    FieldsObj value = fieldObjs.get(1);
                    o1.setFieldObjs(value.getFieldObjs());
                }
                for (Iterator<FieldsObj> ite = fieldObjs.iterator(); ite.hasNext();) {
                    FieldsObj o = ite.next();
                    String classType = o.getClassType();
                    type += classType.substring(classType.lastIndexOf(".") + 1) + ",";
                    if (Objects.equals(1, o.getTypeCode())) {
                        ite.remove();
                    }
                }
            }
            type = type.substring(0, type.length() - 1);
            type += ">";
            o1.setClassType(type);

        }
    }

    public static FieldsObj removeResult(FieldsObj obj) {
        if (null == obj) {
            return null;
        }
        String classType = obj.getClassType();
        if ("com.zhaoyun.knife.result.Result".equals(classType)) {
            List<FieldsObj> fieldObjs = obj.getFieldObjs();
            for (FieldsObj o : fieldObjs) {
                if ("data".equals(o.getFieldName())) {
                    return o;
                }
            }
        }
        return obj;
    }
}
