package com.cn.fit.model.healthdiary;

import java.util.List;

/**
 * App根据数据结构绘制页面，web端根据数据渲染页面
 *
 * @author kuangtiecheng
 */
public class BeanInterventionLogResult {

    private String startDate;
    private String nextStartDate;
    private String planStartDate;
    private String planEndDate;
    private int state;
    private int days;
    private long patientPlanId;
    private List<BeanInterventionDay> nodes;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNextStartDate() {
        return nextStartDate;
    }

    public void setNextStartDate(String nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getPatientPlanId() {
        return patientPlanId;
    }

    public void setPatientPlanId(long patientPlanId) {
        this.patientPlanId = patientPlanId;
    }

    public List<BeanInterventionDay> getNodes() {
        return nodes;
    }

    public void setNodes(List<BeanInterventionDay> nodes) {
        this.nodes = nodes;
    }

}