package com.cn.fit.model.customer;

/**
 * Created by ktcer on 2016/1/11.
 */
public class BeanCoachPage {
    private String address;//位置 string
    private String birth;//生日 string
    private String coachProgramType;//教练擅长项目类型 string 若是多个，用“，”分隔，如：“健身，瑜伽，游泳”
    private float distance;//距离 number float
    private int members;//总会员数 number int
    private String name;// 姓名 string
    private String picUrl;//头像路径 string
    private String resume;//简介 string
    private String rewards;//奖励情况 string
    private String sex;//性别 string
    private String teachTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getCoachProgramType() {
        return coachProgramType;
    }

    public void setCoachProgramType(String coachProgramType) {
        this.coachProgramType = coachProgramType;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(String teachTime) {
        this.teachTime = teachTime;
    }
}
