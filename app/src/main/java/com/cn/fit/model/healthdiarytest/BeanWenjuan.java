package com.cn.fit.model.healthdiarytest;

import java.util.List;

public class BeanWenjuan {

    private Long wenuanID;
    private List<BeanQuestion> questions;

    public Long getWenuanID() {
        return wenuanID;
    }

    public void setWenuanID(Long wenuanID) {
        this.wenuanID = wenuanID;
    }

    public List<BeanQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<BeanQuestion> questions) {
        this.questions = questions;
    }


}
