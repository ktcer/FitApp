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

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class ChattingItemContainer extends RelativeLayout {

    public static final String TAG = LogUtil.getLogUtilsTag(ChattingItemContainer.class);

    private int mResource;
    private LayoutInflater mInflater;

    /**
     * @param inflater
     * @param resource
     */
    @SuppressWarnings("deprecation")
    public ChattingItemContainer(LayoutInflater inflater, int resource) {
        super(inflater.getContext());
        mInflater = inflater;
        mResource = resource;

        // add timeView for chatting item.
        TextView textView = new TextView(getContext(), null, R.style.ChattingUISplit);
        textView.setId(R.id.chatting_time_tv);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0F);
        LayoutParams textViewLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textViewLayoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.NormalPadding), 0,
                getResources().getDimensionPixelSize(R.dimen.NormalPadding));

        addView(textView, textViewLayoutParams);

        // add message content view
        View chattingView = mInflater.inflate(mResource, null);
        int id = chattingView.getId();
        if (id == -1) {
            LogUtil.v(TAG, "content view has no id, use defaul id");
            id = R.id.chatting_content_area;
            chattingView.setId(id);
        }

        LayoutParams chattingViewLayoutParams = new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        chattingViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.chatting_time_tv);
        chattingViewLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.chatting_checkbox);
        addView(chattingView, chattingViewLayoutParams);

        View maskView = new View(getContext());
        maskView.setId(R.id.chatting_maskview);
        maskView.setVisibility(View.GONE);
        LayoutParams maskViewLayoutParams = new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        maskViewLayoutParams.addRule(RelativeLayout.ALIGN_TOP, id);
        maskViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, id);
        addView(maskView, maskViewLayoutParams);
    }

}

