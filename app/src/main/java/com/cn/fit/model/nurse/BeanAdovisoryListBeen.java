package com.cn.fit.model.nurse;

public class BeanAdovisoryListBeen {
    /**
     * 头像
     */
    private String imagUrl;
    /**
     * 姓名
     */
    private String name;
    /**
     * 职业  主任医师
     */
    private String staff;
    /**
     * 问题
     */
    private String question;
    /**
     * 回答
     */
    private String answer;
    /**
     * 医院
     */
    private String hospital;
    /**
     * 部门-- 骨科
     */
    private String department;
    /**
     * 时间
     */
    private String time;

    public String getImagUrl() {
        return imagUrl;
    }

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
