package com.zhaoyun.docmanager.biz.bean.diff;

import com.zhaoyun.docmanager.common.util.StringUtil;

import java.util.List;

/**
 * Created on 2016/8/16 18:13.
 * 未来不确定才更值得前行
 *
 * @author user
 */
public class FieldDiff {
    private String name;
    private String pkg1;
    private String type1;
    private String desc1;
    private String pkg2;
    private String type2;
    private String desc2;
    private List<FieldDiff> childList;
    private Integer diffType;

    public List<FieldDiff> getChildList() {
        return childList;
    }

    public void setChildList(List<FieldDiff> childList) {
        this.childList = childList;
    }

    public String getPkg1() {
        return pkg1;
    }

    public String getPkg2() {
        return pkg2;
    }


    public enum DiffType {
        NOCHANGE(0, "no change"), ADD(1, "add"), DEL(2, "del"), CHANGE(3, "CHANGE");

        DiffType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        private int type;
        private String desc;

        public int getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        if (StringUtil.isNotEmpty(type1) && type1.lastIndexOf(".") != -1 && !type1.startsWith("java.")) {
            String simpleName = type1.substring(type1.lastIndexOf(".") + 1);
            String pkgName = type1.substring(0, type1.lastIndexOf("."));
            this.type1 = simpleName;
            this.pkg1 = pkgName;
            return;
        }
        this.type1 = type1;

    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        if (StringUtil.isNotEmpty(type2) && type2.lastIndexOf(".") != -1 && !type2.startsWith("java.")) {
            String simpleName = type2.substring(type2.lastIndexOf(".") + 1);
            String pkgName = type2.substring(0, type2.lastIndexOf("."));
            this.type2 = simpleName;
            this.pkg2 = pkgName;
            return;
        }
        this.type2 = type2;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }


    public Integer getDiffType() {
        return diffType;
    }

    public void setDiffType(Integer diffType) {
        this.diffType = diffType;
    }
}
