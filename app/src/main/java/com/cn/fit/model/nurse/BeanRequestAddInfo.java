package com.cn.fit.model.nurse;

public class BeanRequestAddInfo {
    private long nurseID;
    private String nurseName;
    private String positionName;
    private String hospitalName;
    private String ability;
    private String department;
    private String imgUrl;
    private int ifhave;


    public int getIfhave() {
        return ifhave;
    }

    public void setIfhave(int ifhave) {
        this.ifhave = ifhave;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
