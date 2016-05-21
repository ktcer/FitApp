package com.cn.fit.model.consult;

import java.util.List;

/*
 * 预约咨询视频数据详情
 */

public class BeanVideoDetail {
    private String Time;
    private String Avilable;
    private List<Integer> periodList;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAvilable() {
        return Avilable;
    }

    public void setAvilable(String avilable) {
        Avilable = avilable;
    }

    public List<Integer> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Integer> periodList) {
        this.periodList = periodList;
    }

}
