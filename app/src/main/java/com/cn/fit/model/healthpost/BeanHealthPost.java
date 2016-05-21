package com.cn.fit.model.healthpost;

import java.io.Serializable;

public class BeanHealthPost implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String title;
    private String oriprice;
    private String finalprice;
    private String tag;
    private String introduce;
    private String cover;
    private long travelID;
    private String activetime;

    public BeanHealthPost(String title, String oriprice, String finalprice
            , String tag, String introduce, String cover, long travelID, String activetime) {
        this.title = title;
        this.oriprice = oriprice;
        this.finalprice = finalprice;
        this.tag = tag;
        this.introduce = introduce;
        this.cover = cover;
        this.travelID = travelID;
        this.activetime = activetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriprice() {
        return oriprice;
    }

    public void setOriprice(String oriprice) {
        this.oriprice = oriprice;
    }

    public String getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(String finalprice) {
        this.finalprice = finalprice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getTravelID() {
        return travelID;
    }

    public void setTravelID(long travelID) {
        this.travelID = travelID;
    }

    public String getActivetime() {
        return activetime;
    }

    public void setActivetime(String activetime) {
        this.activetime = activetime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
