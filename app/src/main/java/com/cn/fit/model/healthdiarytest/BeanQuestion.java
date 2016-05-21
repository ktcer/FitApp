package com.cn.fit.model.healthdiarytest;

import java.util.List;


public class BeanQuestion {
    private Long questionID;
    private String context;
    private Short type;
    private List<BeanOption> options;
    private String util;
    private Byte haschild;
    private Integer level;
    private List<BeanQuestion> children;

    public Long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public List<BeanOption> getOptions() {
        return options;
    }

    public void setOptions(List<BeanOption> options) {
        this.options = options;
    }

    public String getUtil() {
        return util;
    }

    public void setUtil(String util) {
        this.util = util;
    }

    public Byte getHaschild() {
        return haschild;
    }

    public void setHaschild(Byte haschild) {
        this.haschild = haschild;
    }

    public List<BeanQuestion> getChildren() {
        return children;
    }

    public void setChildren(List<BeanQuestion> children) {
        this.children = children;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


}
