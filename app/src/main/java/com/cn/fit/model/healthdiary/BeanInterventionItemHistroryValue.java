package com.cn.fit.model.healthdiary;

/**
 * 干预项目历史值，某一天的值
 *
 * @author kuangtiecheng
 *         2015年4月30日下午9:44:43
 */
public class BeanInterventionItemHistroryValue {

    private long _id;
    private String date;
    private double value;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}