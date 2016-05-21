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
package com.cn.fit.ui.chat.ui.chatting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public abstract class ChatFooterPanel extends LinearLayout {

    protected EmojiGrid.OnEmojiItemClickListener mItemClickListener;

    /**
     * @param context
     * @param attrs
     */
    public ChatFooterPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Register a callback to be invoked when an item in this EmojiGird View has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    protected void setOnEmojiItemClickListener(EmojiGrid.OnEmojiItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this EmojiGird View has
     * been clicked, or null id no callback has been set.
     */
    public final EmojiGrid.OnEmojiItemClickListener getOnEmojiItemClickListener() {
        return mItemClickListener;
    }

    public void onDestroy() {

    }

    public abstract void setChatFooterPanelHeight(int height);

    /**
     * {@link com.cn.fit.ui.chat.ui.chatting.view.ChatFooterPanel} onPause
     */
    public abstract void onPause();

    /**
     * {@link com.cn.fit.ui.chat.ui.chatting.view.ChatFooterPanel} onResume
     */
    public abstract void onResume();

    /**
     * {@link com.cn.fit.ui.chat.ui.chatting.view.ChatFooterPanel} reset
     */
    public abstract void reset();
}

