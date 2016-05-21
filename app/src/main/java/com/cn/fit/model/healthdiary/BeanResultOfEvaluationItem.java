package com.cn.fit.model.healthdiary;

import java.io.Serializable;
import java.util.List;

public class BeanResultOfEvaluationItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8727883869440198835L;
    private String item;
    private String value;
    private String state;
    private String util;
    private byte haschild;
    private int level;
    private List<BeanResultOfEvaluationItem> children;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUtil() {
        return util;
    }

    public void setUtil(String util) {
        this.util = util;
    }

    public byte getHaschild() {
        return haschild;
    }

    public void setHaschild(byte haschild) {
        this.haschild = haschild;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<BeanResultOfEvaluationItem> getChildren() {
        return children;
    }

    public void setChildren(List<BeanResultOfEvaluationItem> children) {
        this.children = children;
    }


}
