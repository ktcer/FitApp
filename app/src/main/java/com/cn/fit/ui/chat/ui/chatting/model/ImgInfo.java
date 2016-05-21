/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cn.fit.ui.chat.storage.ImgInfoSqlManager;


/**
 * 图片消息
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class ImgInfo {
    private static int VALUES_ENPTY = -1;

    private long id = -1;
    private int msgSvrId = -1;
    private int offset = -1;
    private int totalLen = -1;
    private String bigImgPath;
    private String thumbImgPath;
    private int createtime = -1;
    private String msglocalid;
    private int status = -1;
    private int nettimes = -1;
    public boolean isGif = false;

    public void setCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.ID));
        this.msgSvrId = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.MSGSVR_ID));
        this.offset = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.OFFSET));
        this.totalLen = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.TOTALLEN));
        this.bigImgPath = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.BIG_IMGPATH));
        this.thumbImgPath = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.THUMBIMG_PATH));
        this.createtime = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.CREATE_TIME));
        this.msglocalid = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.MSG_LOCAL_ID));
        this.status = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.STATUS));
        this.nettimes = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.NET_TIMES));
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the msgSvrId
     */
    public int getMsgSvrId() {
        return msgSvrId;
    }

    /**
     * @param msgSvrId the msgSvrId to set
     */
    public void setMsgSvrId(int msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the totalLen
     */
    public int getTotalLen() {
        return totalLen;
    }

    /**
     * @param totalLen the totalLen to set
     */
    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    /**
     * @return the bigImgPath
     */
    public String getBigImgPath() {
        return bigImgPath;
    }

    /**
     * @param bigImgPath the bigImgPath to set
     */
    public void setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
    }

    /**
     * @return the thumbImgPath
     */
    public String getThumbImgPath() {
        return thumbImgPath;
    }

    /**
     * @param thumbImgPath the thumbImgPath to set
     */
    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    /**
     * @return the createtime
     */
    public int getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime the createtime to set
     */
    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    /**
     * @return the msglocalid
     */
    public String getMsglocalid() {
        return msglocalid;
    }

    /**
     * @param msglocalid the msglocalid to set
     */
    public void setMsglocalid(String msglocalid) {
        this.msglocalid = msglocalid;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the nettimes
     */
    public int getNettimes() {
        return nettimes;
    }

    /**
     * @param nettimes the nettimes to set
     */
    public void setNettimes(int nettimes) {
        this.nettimes = nettimes;
    }

    public ContentValues buildContentValues() {

        ContentValues values = new ContentValues();
        if (id != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.ID, id);
        }
        if (msgSvrId != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.MSGSVR_ID, msgSvrId);
        }
        if (offset != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.OFFSET, offset);
        }
        if (totalLen != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.TOTALLEN, totalLen);
        }
        if (!TextUtils.isEmpty(bigImgPath)) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.BIG_IMGPATH, bigImgPath);
        }
        if (!TextUtils.isEmpty(thumbImgPath)) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.THUMBIMG_PATH, thumbImgPath);
        }
        if (createtime != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.CREATE_TIME, createtime);
        }
        if (!TextUtils.isEmpty(msglocalid)) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.MSG_LOCAL_ID, msglocalid);
        }
        if (status != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.STATUS, status);
        }
        if (nettimes != VALUES_ENPTY) {
            values.put(ImgInfoSqlManager.ImgInfoColumn.NET_TIMES, nettimes);
        }
        return values;
    }
}
