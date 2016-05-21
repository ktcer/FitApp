package com.cn.fit.ui.patient.mycare;


public class historicDetectionList {
    private String date;
    private String time;
    /**
     * 体温或者收缩压的值
     */
    private String valueTWorSSY;
    /**
     * 舒张压
     */
    private String valueSZY;
    /**
     * 舒张压
     */
    private String valueHR;
    /**
     * 最大值
     */
    private long refValueMax;
    /**
     * 最小值
     */
    private long refValueMin;
    /**
     * 单位
     */
    private String unit;
    private String dataName;
    private long monitorDataId;
    /**
     * 判定是否需要日期标题栏
     */
    private boolean whetherNeedTitle;

    public boolean isWhetherNeedTitle() {
        return whetherNeedTitle;
    }

    public void setWhetherNeedTitle(boolean whetherNeedTitle) {
        this.whetherNeedTitle = whetherNeedTitle;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValueTWorSSY() {
        return valueTWorSSY;
    }

    public void setValueTWorSSY(String valueTWorSSY) {
        this.valueTWorSSY = valueTWorSSY;
    }

    public String getValueSZY() {
        return valueSZY;
    }

    public void setValueSZY(String valueSZY) {
        this.valueSZY = valueSZY;
    }

    public long getRefValueMax() {
        return refValueMax;
    }

    public void setRefValueMax(long refValueMax) {
        this.refValueMax = refValueMax;
    }

    public long getRefValueMin() {
        return refValueMin;
    }

    public void setRefValueMin(long refValueMin) {
        this.refValueMin = refValueMin;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getMonitorDataId() {
        return monitorDataId;
    }

    public void setMonitorDataId(long monitorDataId) {
        this.monitorDataId = monitorDataId;
    }

    public String getValueHR() {
        return valueHR;
    }

    public void setValueHR(String valueHR) {
        this.valueHR = valueHR;
    }
}
