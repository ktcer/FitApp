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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * 自定义头像显示控件
 *
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class CCPMaskLayout extends RelativeLayout {


    private View mView;
    private ImageView mImageView;
    private Drawable mForeDrawable;

    /**
     * @param context
     */
    public CCPMaskLayout(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPMaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPMaskLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MaskLayout, defStyle, 0);
        mForeDrawable = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /*
     *
     */
    public void initMaskLayoutRule() {
        removeView(mImageView);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);


        addView(mImageView, params);
    }

    /**
     * @return
     */
    public final View getContentView() {
        return mView;
    }


    /* (non-Javadoc)
     * @see android.view.View#onFinishInflate()
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mView = findViewById(R.id.content);
        if (mView == null) {
            LogUtil.e(LogUtil.getLogUtilsTag(CCPMaskLayout.class), "not found view by id, new one");
            mView = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mView.setLayoutParams(layoutParams);
            addView(mView);
        }

        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mImageView.setImageDrawable(mForeDrawable);
        addView(mImageView);
    }

    public enum MaskLayoutRule {
        Rule_ALIGN_PARENT_LEFT, // 9
        Rule_ALIGN_PARENT_RIGHT, // 11
        Rule_ALIGN_PARENT_BOTTOM // 12
    }
}
