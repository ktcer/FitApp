package com.cn.fit.model.customer;

import java.io.Serializable;

/**
 * Created by ktcer on 2015/12/29.
 * 第一个页面发现的bean
 */
public class BeanDiscovery implements Serializable {
    private long classID;//课程ID number
    private String cover;//封面图片地址 string
    private float distance;//距离 number ;//float
    private float finalPrice;// 现价 number float
    private int haveNums;//已报名人数 number
    private String location;//地点 string
    private int nums;//课程招的人数 number
    private float oriPrice;//原价 number flaot
    private String serveType;//服务方式 string 一对一，上门等
    private String startTime;//活动时间 string
    private String tag;//标签 string
    private String title;//
    private String programName;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getHaveNums() {
        return haveNums;
    }

    public void setHaveNums(int haveNums) {
        this.haveNums = haveNums;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public float getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(float oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
