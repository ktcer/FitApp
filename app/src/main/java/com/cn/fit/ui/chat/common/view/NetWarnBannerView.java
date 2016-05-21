/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
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
package com.cn.fit.ui.chat.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.fit.R;

/**
 * 网络提醒BannerView
 * Created by Jorstin on 2015/3/18.
 */
public class NetWarnBannerView extends LinearLayout {

    private View mContetLayout;
    private ImageView mNetWarnIcon;
    private TextView mNetDetail;
    private TextView mNetDetailTips;
    private TextView mNetHintTips;
    private ProgressBar mProgressBar;

    /**
     * @param context
     */
    public NetWarnBannerView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public NetWarnBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public NetWarnBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     *
     */
    private void init() {
        View.inflate(getContext(), R.layout.net_warn_item, this);
        mContetLayout = findViewById(R.id.nwview);
        mNetWarnIcon = (ImageView) findViewById(R.id.nw_icon);
        mNetDetail = (TextView) findViewById(R.id.nw_detail);
        mNetDetailTips = (TextView) findViewById(R.id.nw_detail_tip);
        mNetHintTips = (TextView) findViewById(R.id.nw_hint_tip);
        mProgressBar = (ProgressBar) findViewById(R.id.nw_prog);

    }

    public final void setNetWarnText(CharSequence text) {
        mNetDetail.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContetLayout.setVisibility(View.VISIBLE);
    }

    public final void setNetWarnDetailTips(CharSequence text) {
        mNetDetailTips.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContetLayout.setVisibility(View.VISIBLE);
    }

    public final void setNetWarnHintText(CharSequence text) {
        mNetHintTips.setText(text);
        mProgressBar.setVisibility(View.GONE);
        mContetLayout.setVisibility(View.VISIBLE);
    }


    /**
     * set gone
     */
    public void hideWarnBannerView() {
        if (mContetLayout == null) {
            return;
        }
        mContetLayout.setVisibility(View.GONE);
    }

    /**
     * 重新连接
     *
     * @param reconnect
     */
    public final void reconnect(boolean reconnect) {
        mContetLayout.setVisibility(View.VISIBLE);
        if (reconnect) {
            mProgressBar.setVisibility(View.VISIBLE);
            mNetWarnIcon.setVisibility(View.INVISIBLE);
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        mNetWarnIcon.setVisibility(View.VISIBLE);

    }
}

