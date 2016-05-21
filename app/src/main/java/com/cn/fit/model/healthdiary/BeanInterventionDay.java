package com.cn.fit.model.healthdiary;

import java.util.List;

public class BeanInterventionDay {

    private String date;
    private List<BeanIntervNode> nodes;
    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BeanIntervNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<BeanIntervNode> nodes) {
        this.nodes = nodes;
    }

}
