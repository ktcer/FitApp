package com.cn.fit.model.customer;

import java.io.Serializable;

/**
 * Created by ktcer on 2016/1/3.
 */
public class BeanDiscoveryDetail implements Serializable {
    private long classID;// 课程id number
    private long coachID;//专家id number
    private long coachMembers;//专家会员总数 number
    private String coachName;// 专家名 string
    private String coachPic;//专家头像路径 string
    private String cover;//封面图片路径 string
    private String detail;//详情 string
    private float distance;//距离 number
    private int duration;//课程时长 number
    private String goal;//目的 string
    private String location;// 地点 string
    private String startTime;//活动时间 number
    private float longitude;
    private float latitude;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }

    public long getCoachID() {
        return coachID;
    }

    public void setCoachID(long coachID) {
        this.coachID = coachID;
    }

    public long getCoachMembers() {
        return coachMembers;
    }

    public void setCoachMembers(long coachMembers) {
        this.coachMembers = coachMembers;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachPic() {
        return coachPic;
    }

    public void setCoachPic(String coachPic) {
        this.coachPic = coachPic;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "BeanDiscoveryDetail{" +
                "classID=" + classID +
                ", coachID=" + coachID +
                ", coachMembers=" + coachMembers +
                ", coachName='" + coachName + '\'' +
                ", coachPic='" + coachPic + '\'' +
                ", cover='" + cover + '\'' +
                ", detail='" + detail + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", goal='" + goal + '\'' +
                ", location='" + location + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}
