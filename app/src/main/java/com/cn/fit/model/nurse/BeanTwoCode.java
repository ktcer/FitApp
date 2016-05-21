package com.cn.fit.model.nurse;

public class BeanTwoCode {
    private int codeType;
    private long userID;
    private String name;

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTclj() {
        return tclj;
    }

    public void setTclj(String tclj) {
        this.tclj = tclj;
    }

    private String tclj;

}
