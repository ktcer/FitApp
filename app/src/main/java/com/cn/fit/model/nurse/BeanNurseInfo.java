package com.cn.fit.model.nurse;

import android.graphics.Bitmap;

/**
 *
 * @author kuangtiecheng
 */
public class BeanNurseInfo {
    private long nurseID;
    private String nurseName;
    private String positionName;
    private String subDepartmentName;
    private String imgUrl;
    private String url;
    private String resume;
    private Bitmap protrait;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSubDepartmentName() {
        return subDepartmentName;
    }

    public void setSubDepartmentName(String subDepartmentName) {
        this.subDepartmentName = subDepartmentName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Bitmap getProtrait() {
        return protrait;
    }

    public void setProtrait(Bitmap protrait) {
        this.protrait = protrait;
    }

}
