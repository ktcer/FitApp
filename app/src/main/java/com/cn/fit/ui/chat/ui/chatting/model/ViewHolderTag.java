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


import com.yuntongxun.ecsdk.ECMessage;

/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class ViewHolderTag {

    public int position;
    public ECMessage detail;
    public int type;
    public int rowType;
    public boolean receive;
    public boolean voipcall;


    public static ViewHolderTag createTag(ECMessage detail, int type, int position) {
        ViewHolderTag holderTag = new ViewHolderTag();
        holderTag.position = position;
        holderTag.type = type;
        holderTag.detail = detail;
        return holderTag;
    }

    public static ViewHolderTag createTag(ECMessage detail, int type, int position, int rowType, boolean receive) {
        ViewHolderTag holderTag = new ViewHolderTag();
        holderTag.position = position;
        holderTag.type = type;
        holderTag.rowType = rowType;
        holderTag.detail = detail;
        holderTag.receive = receive;
        return holderTag;
    }

    /**
     * @param detail
     * @param position
     * @return
     */
    public static ViewHolderTag createTag(ECMessage detail, int position) {
        ViewHolderTag holderTag = new ViewHolderTag();
        holderTag.position = position;
        holderTag.detail = detail;
        holderTag.type = TagType.TAG_PREVIEW;
        return holderTag;
    }

    public static ViewHolderTag createTag(ECMessage detail, int type, int position, boolean voipcall) {
        ViewHolderTag holderTag = new ViewHolderTag();
        holderTag.position = position;
        holderTag.detail = detail;
        holderTag.type = type;
        holderTag.voipcall = voipcall;
        return holderTag;
    }

    public static class TagType {
        public static final int TAG_PREVIEW = 0;
        public static final int TAG_VIEW_FILE = 1;
        public static final int TAG_VOICE = 2;
        public static final int TAG_VIEW_PICTURE = 3;
        public static final int TAG_RESEND_MSG = 4;
        public static final int TAG_VIEW_CONFERENCE = 5;
        public static final int TAG_VOIP_CALL = 6;
    }
}
