package com.cn.fit.model.customer;

/**
 * Created by ktcer on 2016/1/20.
 */
public class BeanQureyMomment {
    private String comments;// 评论内容 string
    private String phone;// 电话 string
    private int starLevelClass;// 课程星级 number
    private int starLevelCoach;//  教练星级 number
    private String time;// 评论时间 string
    private String userUrl;//

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStarLevelClass() {
        return starLevelClass;
    }

    public void setStarLevelClass(int starLevelClass) {
        this.starLevelClass = starLevelClass;
    }

    public int getStarLevelCoach() {
        return starLevelCoach;
    }

    public void setStarLevelCoach(int starLevelCoach) {
        this.starLevelCoach = starLevelCoach;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
