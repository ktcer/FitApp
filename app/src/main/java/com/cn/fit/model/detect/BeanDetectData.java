package com.cn.fit.model.detect;

public class BeanDetectData {
    private int id;
    private String type;
    private double value;
    private String unit;
    private double refmaxvalue;
    private double refminvalue;
    private int subtype;
    private String time;
    private int submit_type;
    private int userid;

    private BeanDetectData() {
    }

    public BeanDetectData(int id, String type, double value, String unit,
                          double refmaxvalue, double refminvalue, int subtype, String time,
                          int submit_type, int userid) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.refmaxvalue = refmaxvalue;
        this.refminvalue = refminvalue;
        this.subtype = subtype;
        this.time = time;
        this.submit_type = submit_type;
        this.userid = userid;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getRefmaxvalue() {
        return refmaxvalue;
    }

    public void setRefmaxvalue(double refmaxvalue) {
        this.refmaxvalue = refmaxvalue;
    }

    public double getRefminvalue() {
        return refminvalue;
    }

    public void setRefminvalue(double refminvalue) {
        this.refminvalue = refminvalue;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSubmit_type() {
        return submit_type;
    }

    public void setSubmit_type(int submit_type) {
        this.submit_type = submit_type;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
