package com.cn.fit.model.alarm;

public class BeanRemindInfo {
    private String fileUrl;
    private String remindPersonName;
    private String remindTime;
    private String text;
    private long remindID;
    private byte repeat;
    private short type;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemindPersonName() {
        return remindPersonName;
    }

    public void setRemindPersonName(String remindPersonName) {
        this.remindPersonName = remindPersonName;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getRemindID() {
        return remindID;
    }

    public void setRemindID(long remindID) {
        this.remindID = remindID;
    }

    public byte getRepeat() {
        return repeat;
    }

    public void setRepeat(byte repeat) {
        this.repeat = repeat;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

}
