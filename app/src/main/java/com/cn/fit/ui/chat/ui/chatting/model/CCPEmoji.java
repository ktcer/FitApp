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

/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class CCPEmoji {

    /**
     * Expression corresponding resource picture ID
     */
    private int id;

    /**
     * Expression resources corresponding text description
     */
    private String EmojiDesc;

    /**
     * File name expression resources
     */
    private String EmojiName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmojiDesc() {
        return EmojiDesc;
    }

    public void setEmojiDesc(String emojiDesc) {
        EmojiDesc = emojiDesc;
    }

    public String getEmojiName() {
        return EmojiName;
    }

    public void setEmojiName(String emojiName) {
        EmojiName = emojiName;
    }


}

