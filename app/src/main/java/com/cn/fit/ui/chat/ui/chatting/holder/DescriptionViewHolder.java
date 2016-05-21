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
package com.cn.fit.ui.chat.ui.chatting.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.base.CCPTextView;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;


/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class DescriptionViewHolder extends BaseHolder {

    public View chattingContent;
    /**
     * TextView that display IMessage description.
     */
    private EmojiconTextView descTextView;

    /**
     * @param type
     */
    public DescriptionViewHolder(int type) {
        super(type);

    }

    public BaseHolder initBaseHolder(View baseView, boolean receive) {
        super.initBaseHolder(baseView);

        chattingTime = (TextView) baseView.findViewById(R.id.chatting_time_tv);
        chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
        descTextView = (EmojiconTextView) baseView.findViewById(R.id.chatting_content_itv);
        checkBox = (CheckBox) baseView.findViewById(R.id.chatting_checkbox);
        chattingMaskView = baseView.findViewById(R.id.chatting_maskview);
        chattingContent = baseView.findViewById(R.id.chatting_content_area);
        if (receive) {
            type = 7;
            return this;
        }

        uploadState = (ImageView) baseView.findViewById(R.id.chatting_state_iv);
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 8;
        return this;
    }

    /**
     * {@link CCPTextView} Display imessage text
     *
     * @return
     */
    public EmojiconTextView getDescTextView() {
        if (descTextView == null) {
            descTextView = (EmojiconTextView) getBaseView().findViewById(R.id.chatting_content_itv);
        }
        return descTextView;
    }

    /**
     * @return
     */
    public ImageView getChattingState() {
        if (uploadState == null) {
            uploadState = (ImageView) getBaseView().findViewById(R.id.chatting_state_iv);
        }
        return uploadState;
    }

    /**
     * @return
     */
    public ProgressBar getUploadProgressBar() {
        if (progressBar == null) {
            progressBar = (ProgressBar) getBaseView().findViewById(R.id.uploading_pb);
        }
        return progressBar;
    }

}
