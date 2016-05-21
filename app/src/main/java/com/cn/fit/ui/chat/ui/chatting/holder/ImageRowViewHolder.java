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


/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class ImageRowViewHolder extends BaseHolder {

    public ImageView chattingContentIv;
    public View uploadingView;
    public TextView uploadingText;
    public ImageView maskView;
    public ImageView mGifIcon;

    /**
     * @param type
     */
    public ImageRowViewHolder(int type) {
        super(type);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.model.BaseHolder#initBaseHolder(android.view.View)
     */
    public BaseHolder initBaseHolder(View baseView, boolean receive) {
        super.initBaseHolder(baseView);

        chattingTime = (TextView) baseView.findViewById(R.id.chatting_time_tv);
        chattingContentIv = (ImageView) baseView.findViewById(R.id.chatting_content_iv);
        checkBox = (CheckBox) baseView.findViewById(R.id.chatting_checkbox);
        chattingMaskView = baseView.findViewById(R.id.chatting_maskview);
        uploadingView = baseView.findViewById(R.id.uploading_view);
        mGifIcon = (ImageView) baseView.findViewById(R.id.img_gif);

        if (receive) {
            chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
            progressBar = (ProgressBar) baseView.findViewById(R.id.downloading_pb);
            type = 1;
        } else {
            progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
            uploadingText = (TextView) baseView.findViewById(R.id.uploading_tv);
            chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
            type = 2;
        }
        maskView = (ImageView) baseView.findViewById(R.id.chatting_content_mask_iv);

        return this;
    }
}

