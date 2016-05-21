package com.cn.fit.model.family;

public class BeanFamilyMemberInfo {

    private long relationId;
    private long ownerId;
    private String ownerName;
    private long memberId;
    private String memberLoginName;
    private String memberName;
    private String remarkName;
    private String imageUrl;
    private String lastUnusual;
    private String phone;
    private String disease;
    private short state;
    private int index;
    private Integer memberGknumber;
    private Integer ownerGknumber;

    public String getMemberLoginName() {
        return memberLoginName;
    }

    public void setMemberLoginName(String memberLoginName) {
        this.memberLoginName = memberLoginName;
    }

    public Integer getMemberGknumber() {
        return memberGknumber;
    }

    public void setMemberGknumber(Integer memberGknumber) {
        this.memberGknumber = memberGknumber;
    }

    public Integer getOwnerGknumber() {
        return ownerGknumber;
    }

    public void setOwnerGknumber(Integer ownerGknumber) {
        this.ownerGknumber = ownerGknumber;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastUnusual() {
        return lastUnusual;
    }

    public void setLastUnusual(String lastUnusual) {
        this.lastUnusual = lastUnusual;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

}
