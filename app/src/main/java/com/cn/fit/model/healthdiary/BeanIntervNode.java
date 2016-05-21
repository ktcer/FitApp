package com.cn.fit.model.healthdiary;

import java.io.Serializable;
import java.util.List;

public class BeanIntervNode implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8824824909157406871L;
    private String time;
    private long _id;
    private boolean needRemind;
    private String remindContent;
    private String summary;
    private List<BeanIntervItem> intervItemsList;
    private String contentStr;
    private List<BeanContentNode> contentList;
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public boolean isNeedRemind() {
        return needRemind;
    }

    public void setNeedRemind(boolean needRemind) {
        this.needRemind = needRemind;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public List<BeanIntervItem> getIntervItemsList() {
        return intervItemsList;
    }

    public void setIntervItemsList(List<BeanIntervItem> intervItemsList) {
        this.intervItemsList = intervItemsList;
    }

    public List<BeanContentNode> getContentList() {
        return contentList;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public void setContentList(List<BeanContentNode> contentList) {
        this.contentList = contentList;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
