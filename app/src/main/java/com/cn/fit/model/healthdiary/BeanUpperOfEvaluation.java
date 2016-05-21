package com.cn.fit.model.healthdiary;

import java.util.List;

public class BeanUpperOfEvaluation {
    private long patientID;
    private long planID;
    private List<BeanAnswerOfEvaluation> list;

    public long getPatientID() {
        return patientID;
    }

    public void setPatientID(long patientID) {
        this.patientID = patientID;
    }

    public long getPlanID() {
        return planID;
    }

    public void setPlanID(long planID) {
        this.planID = planID;
    }

    public List<BeanAnswerOfEvaluation> getList() {
        return list;
    }

    public void setList(List<BeanAnswerOfEvaluation> list) {
        this.list = list;
    }
}
