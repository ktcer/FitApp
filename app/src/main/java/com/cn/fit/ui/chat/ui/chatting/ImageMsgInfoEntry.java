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
package com.cn.fit.ui.chat.ui.chatting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class ImageMsgInfoEntry implements Parcelable {

    // 消息ID
    private String id;
    // 消息图片url
    private String picurl;
    private String remoteUrl;
    // 消息图片缩略图url
    private String thumbnailurl;
    // 图片类型
    private String pictype;
    // 图片MD5校验值
    private String picmd5;
    // 图片大小，单位为字节
    private String picsize;


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return the picurl
     */
    public String getPicurl() {
        return picurl;
    }

    /**
     * @param picurl the picurl to set
     */
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    /**
     * @return the thumbnailurl
     */
    public String getThumbnailurl() {
        return thumbnailurl;
    }

    /**
     * @param thumbnailurl the thumbnailurl to set
     */
    public void setThumbnailurl(String thumbnailurl) {
        this.thumbnailurl = thumbnailurl;
    }

    /**
     * @return the pictype
     */
    public String getPictype() {
        return pictype;
    }

    /**
     * @param pictype the pictype to set
     */
    public void setPictype(String pictype) {
        this.pictype = pictype;
    }

    /**
     * @return the picmd5
     */
    public String getPicmd5() {
        return picmd5;
    }

    /**
     * @param picmd5 the picmd5 to set
     */
    public void setPicmd5(String picmd5) {
        this.picmd5 = picmd5;
    }

    /**
     * @return the picsize
     */
    public String getPicsize() {
        return picsize;
    }

    /**
     * @param picsize the picsize to set
     */
    public void setPicsize(String picsize) {
        this.picsize = picsize;
    }

    /**
     * @return the remoteUrl
     */
    public String getRemoteUrl() {
        return remoteUrl;
    }

    /**
     * @param remoteUrl the remoteUrl to set
     */
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    /**
     * @param id
     * @param remoteUrl
     * @param thumbnailurl
     * @param picurl
     */
    public ImageMsgInfoEntry(String id, String remoteUrl, String thumbnailurl,
                             String picurl) {
        super();
        this.id = id;
        this.picurl = picurl;
        this.remoteUrl = remoteUrl;
        this.thumbnailurl = thumbnailurl;
    }

    public ImageMsgInfoEntry(String msgId) {
        this.id = msgId;
    }

    private ImageMsgInfoEntry(Parcel in) {
        this.id = in.readString();
        this.picurl = in.readString();
        this.thumbnailurl = in.readString();
        this.pictype = in.readString();
        this.picmd5 = in.readString();
        this.picsize = in.readString();
        this.remoteUrl = in.readString();
    }

    public static final Creator<ImageMsgInfoEntry> CREATOR = new Creator<ImageMsgInfoEntry>() {

        @Override
        public ImageMsgInfoEntry createFromParcel(Parcel in) {
            return new ImageMsgInfoEntry(in);
        }

        @Override
        public ImageMsgInfoEntry[] newArray(int size) {
            return new ImageMsgInfoEntry[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.picurl);
        dest.writeString(this.thumbnailurl);
        dest.writeString(this.pictype);
        dest.writeString(this.picmd5);
        dest.writeString(this.picsize);
        dest.writeString(this.remoteUrl);
    }
}
