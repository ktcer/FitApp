package com.cn.fit.ui.chat.ui.group;

import android.content.ContentValues;
import android.database.Cursor;

import com.cn.fit.ui.chat.common.utils.DemoUtils;

/**
 * com.cn.aihu.ui.chat.ui.group in ECDemo_Android
 * Created by Jorstin on 2015/3/26.
 */
public class DemoGroupNotice {

    private String id;
    private String sender;
    private long dateCreate;
    private String admin;
    private int auditType;
    private int confirm;
    private int version;
    private String declared;
    private String groupId;
    private String groupName;
    private String member;
    private String nickName;
    private String content;
    private int isRead;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public DemoGroupNotice() {

    }

    public DemoGroupNotice(int type) {
        setId(DemoUtils.md5(System.currentTimeMillis() + ""));
        setAuditType(type);
    }


    public void setCursor(Cursor cursor) {
        setId(cursor.getString(0));
        this.content = cursor.getString(1);
        setAdmin(cursor.getString(2));
        setConfirm(cursor.getInt(3));
        setGroupId(cursor.getString(4));
        setMember(cursor.getString(5));
        setDateCreate(cursor.getLong(6));
        setGroupName(cursor.getString(7));
        setNickName(cursor.getString(8));
        setAuditType(cursor.getInt(9));
        setDeclared(cursor.getString(10));
    }

    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        values.put("declared", getDeclared());
        values.put("verifymsg", content);
        values.put("notice_id", getId());
        values.put("groupId", getGroupId());
        values.put("admin", getAdmin());
        values.put("member", getMember());
        values.put("isRead", isRead);
        values.put("confirm", getConfirm());
        values.put("dateCreated", getDateCreate());
        values.put("type", getAuditType());
        values.put("nickName", getNickName());
        values.put("groupName", getGroupName());
        values.put("version", getVersion());
        return values;
    }

    @Override
    public String toString() {
        return "DemoGroupNotice{" +
                "content='" + content + '\'' +
                ", isRead=" + isRead + super.toString() +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDeclared() {
        return declared;
    }

    public void setDeclared(String declared) {
        this.declared = declared;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
