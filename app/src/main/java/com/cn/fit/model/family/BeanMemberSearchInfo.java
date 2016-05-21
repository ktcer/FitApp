package com.cn.fit.model.family;

public class BeanMemberSearchInfo {

    private long userId;
    private String userName;
    private String phone;
    private String encryptPhone;
    private String imageUrl;
    private boolean hasAdd;
    private int addState;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasAdd() {
        return hasAdd;
    }

    public void setHasAdd(boolean hasAdd) {
        this.hasAdd = hasAdd;
    }

    public String getEncryptPhone() {
        return encryptPhone;
    }

    public void setEncryptPhone(String encryptPhone) {
        this.encryptPhone = encryptPhone;
    }

    public int getAddState() {
        return addState;
    }

    public void setAddState(int addState) {
        this.addState = addState;
    }
}
