package com.cn.fit.model.healthdiary;

/**
 * 我的方案简要信息
 */
public class BeanMyPlan {

    private long expertId;
    private long planId;
    private String expertName;

    public long getExpertId() {
        return expertId;
    }

    public void setExpertId(long expertId) {
        this.expertId = expertId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }
}
