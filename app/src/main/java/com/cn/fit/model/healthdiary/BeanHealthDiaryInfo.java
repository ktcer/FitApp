package com.cn.fit.model.healthdiary;

/**
 * 健康日记信息
 *
 * @author kuangtiecheng
 */
public class BeanHealthDiaryInfo {
    private long healthDiaryID;
    private String healthDiaryName;
    private String healthDiaryDesc;
    private String healthDiaryDetail;
    private long usersNum;
    private String imgUrl;


    public String getHealthDiaryDetail() {
        return healthDiaryDetail;
    }

    public void setHealthDiaryDetail(String healthDiaryDetail) {
        this.healthDiaryDetail = healthDiaryDetail;
    }

    public long getHealthDiaryID() {
        return healthDiaryID;
    }

    public void setHealthDiaryID(long healthDiaryID) {
        this.healthDiaryID = healthDiaryID;
    }

    public String getHealthDiaryName() {
        return healthDiaryName;
    }

    public void setHealthDiaryName(String healthDiaryName) {
        this.healthDiaryName = healthDiaryName;
    }

    public String getHealthDiaryDesc() {
        return healthDiaryDesc;
    }

    public void setHealthDiaryDesc(String healthDiaryDesc) {
        this.healthDiaryDesc = healthDiaryDesc;
    }

    public long getUsersNum() {
        return usersNum;
    }

    public void setUsersNum(long usersNum) {
        this.usersNum = usersNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
