package com.cn.fit.model.detect;

import java.util.List;

public class BeanHistoryDetection {
    //	private Bitmap bitmap1;
//	private String data1;
//	private Bitmap bitmap2;
//	private String data2;
//	private String time;
    private long monitorDataId;
    private String unit;
    private String dataCode;
    private String dataName;
    private double refValueMax;
    private double refValueMin;
    private List<BeanMonitorDataItem> value;

    public long getMonitorDataId() {
        return monitorDataId;
    }

    public void setMonitorDataId(long monitorDataId) {
        this.monitorDataId = monitorDataId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public double getRefValueMax() {
        return refValueMax;
    }

    public void setRefValueMax(double refValueMax) {
        this.refValueMax = refValueMax;
    }

    public double getRefValueMin() {
        return refValueMin;
    }

    public void setRefValueMin(double refValueMin) {
        this.refValueMin = refValueMin;
    }

    public List<BeanMonitorDataItem> getValue() {
        return value;
    }

    public void setValue(List<BeanMonitorDataItem> value) {
        this.value = value;
    }


}
