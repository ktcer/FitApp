package com.cn.fit.model.healthdiary;

/**
 * Created by Administrator on 2015/12/15.
 */
public class BeanPatientState {
    private int baseInfoState;
    private int bodyTestState;
    private int expertState;
    private int habbitTestState;
    private int planState;

    public int getBaseInfoState() {
        return baseInfoState;
    }

    public void setBaseInfoState(int baseInfoState) {
        this.baseInfoState = baseInfoState;
    }

    public int getBodyTestState() {
        return bodyTestState;
    }

    public void setBodyTestState(int bodyTestState) {
        this.bodyTestState = bodyTestState;
    }

    public int getExpertState() {
        return expertState;
    }

    public void setExpertState(int expertState) {
        this.expertState = expertState;
    }

    public int getHabbitTestState() {
        return habbitTestState;
    }

    public void setHabbitTestState(int habbitTestState) {
        this.habbitTestState = habbitTestState;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }
}
