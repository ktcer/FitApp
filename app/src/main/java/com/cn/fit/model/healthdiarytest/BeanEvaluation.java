package com.cn.fit.model.healthdiarytest;

import java.util.List;

import com.cn.fit.model.healthdiary.BeanOptions;

public class BeanEvaluation {
    private long questionID;
    private String context;
    private int type;
    private String unite;
    private List<BeanOptions> options;

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<BeanOptions> getOptions() {
        return options;
    }

    public void setOptions(List<BeanOptions> options) {
        this.options = options;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

}
