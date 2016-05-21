package com.cn.fit.model.nurse;

/**
 * 预约信息
 *
 * @author kuangtiecheng
 */
public class BeanReserveInfo {
    private String reserveID;
    private int reserveTypeID;
    private String reserveDateString;
    private String reservePeriod;
    private String nurseName;
    private String nursePosition;
    private String diseaseName;
    private int reserveState;
    private String reserveStates;

    public String getReserveStates() {
        return reserveStates;
    }

    public void setReserveStates(String reserveStates) {
        this.reserveStates = reserveStates;
    }

    public String getReserveID() {
        return reserveID;
    }

    public void setReserveID(String reserveID) {
        this.reserveID = reserveID;
    }

    public int getReserveTypeID() {
        return reserveTypeID;
    }

    public void setReserveTypeID(int reserveTypeID) {
        this.reserveTypeID = reserveTypeID;
    }

    public String getReserveDateString() {
        return reserveDateString;
    }

    public void setReserveDateString(String reserveDateString) {
        this.reserveDateString = reserveDateString;
    }

    public String getReservePeriod() {
        return reservePeriod;
    }

    public void setReservePeriod(String reservePeriod) {
        this.reservePeriod = reservePeriod;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNursePosition() {
        return nursePosition;
    }

    public void setNursePosition(String nursePosition) {
        this.nursePosition = nursePosition;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getReserveState() {
        return reserveState;
    }

    public void setReserveState(int reserveState) {
        this.reserveState = reserveState;
    }

}
