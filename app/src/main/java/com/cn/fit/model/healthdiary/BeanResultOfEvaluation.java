package com.cn.fit.model.healthdiary;

import java.io.Serializable;
import java.util.List;

public class BeanResultOfEvaluation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7169358716785726568L;
    private long wenjuanID;
    private String wenjuanName;
    private String date;
    private String status;
    private String detail;
    private int result;
    private String warming;
    private List<BeanResultOfEvaluationItem> report;

    public long getWenjuanID() {
        return wenjuanID;
    }

    public void setWenjuanID(long wenjuanID) {
        this.wenjuanID = wenjuanID;
    }

    public String getWenjuanName() {
        return wenjuanName;
    }

    public void setWenjuanName(String wenjuanName) {
        this.wenjuanName = wenjuanName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getWarming() {
        return warming;
    }

    public void setWarming(String warming) {
        this.warming = warming;
    }

    public List<BeanResultOfEvaluationItem> getReport() {
        return report;
    }

    public void setReport(List<BeanResultOfEvaluationItem> report) {
        this.report = report;
    }

}
