package com.cn.fit.model.nurse;

import java.util.List;

public class BeanHealthSecretaryInfo {
    private long nurseID;
    private String nurseName;
    private String resume;
    private String positionName;
    private String hospitalName;
    private String ability;
    private List<BeanEassy> essayList;
    private String picurl;
    private String imgurl;
    private String wxcode;
    private boolean hasunpay;
    private float coints;
    private String servetype;
    private String ddh;
    private String sex;
    private String fanscount;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFanscount() {
        return fanscount;
    }

    public void setFanscount(String fanscount) {
        this.fanscount = fanscount;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getServetype() {
        return servetype;
    }

    public void setServetype(String servetype) {
        this.servetype = servetype;
    }

    public float getCoints() {
        return coints;
    }

    public void setCoints(float coints) {
        this.coints = coints;
    }

    public boolean getHasunpay() {
        return hasunpay;
    }

    public void setHasunpay(boolean hasunpay) {
        this.hasunpay = hasunpay;
    }

    public String getWxcode() {
        return wxcode;
    }

    public void setWxcode(String wxcode) {
        this.wxcode = wxcode;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public long getNurseID() {
        return nurseID;
    }

    public void setNurseID(long nurseID) {
        this.nurseID = nurseID;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public List<BeanEassy> getEssayList() {
        return essayList;
    }

    public void setEssayList(List<BeanEassy> essayList) {
        this.essayList = essayList;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }


}
