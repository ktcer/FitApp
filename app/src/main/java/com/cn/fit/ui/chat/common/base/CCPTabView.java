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
package com.cn.fit.ui.chat.common.base;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAccessibilityManager;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * 每一个CCPTabView代表一个导航按钮，配合{@link CCPLauncherUITabView}使用
 * Created by Jorstin on 2015/3/18.
 */
public class CCPTabView extends ViewGroup {

    /**
     * The tab index.
     */
    private int index;

    /**
     * The tab padding.
     */
    private int padding;

    /**
     *
     */
    public int total;

    /**
     * The tab name.
     */
    public TextView mTabDescription;

    /**
     * The tab tips;
     */
    public TextView mTabUnreadTips;

    /**
     * The tab image.
     */
    public ImageView mTabImage;

    /**
     * @param context
     */
    public CCPTabView(Context context) {
        super(context);
        total = 3;
        padding = 0;

        init();
    }

    public CCPTabView(Context context, int index) {
        this(context);
        this.index = index;
        notifyChange();
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        total = 3;
        padding = 0;

        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        total = 3;
        padding = 0;

        init();
    }

    /**
     *
     */
    private void init() {

        int dip2px = DensityUtil.dip2px(2.0F);

        padding = getResources().getDimensionPixelSize(R.dimen.SmallestPadding);
        TextView description = new TextView(getContext());
        description.setSingleLine();
        description.setEllipsize(TextUtils.TruncateAt.END);
        description.setTextColor(getResources().getColor(R.color.ccp_green));
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.HintTextSize) + dip2px);
        description.setText(R.string.tab_view_name);
        description.setTypeface(null, Typeface.NORMAL);
        addView(mTabDescription = description);

        ImageView descriptionImage = new ImageView(getContext());
        descriptionImage.setImageResource(R.drawable.unread_dot);
        descriptionImage.setVisibility(View.INVISIBLE);
        addView(mTabImage = descriptionImage);

        TextView tips = new TextView(getContext());
        tips.setTextColor(getResources().getColor(android.R.color.white));
        tips.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.SmallestTextSize));
        tips.setBackgroundResource(R.drawable.to_read_tips_onbackbtn);
        tips.setGravity(Gravity.CENTER);
        tips.setVisibility(View.INVISIBLE);

        addView(mTabUnreadTips = tips);

        setBackgroundResource(R.drawable.tab_item_bg);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int widthD_value = r - l;
        int heightD_value = b - t;
        int left = (widthD_value - mTabDescription.getMeasuredWidth()) / 2;
        int right = left + mTabDescription.getMeasuredWidth();

        int top = (heightD_value - mTabDescription.getMeasuredHeight()) / 2;
        int bottom = top + mTabDescription.getMeasuredHeight();
        mTabDescription.layout(left, top, right, bottom);

        int imageLeft = left + padding;
        int imageRight = imageLeft + mTabImage.getMeasuredWidth();
        int imageTop = (heightD_value - mTabImage.getMeasuredHeight()) / 2;
        int imageBottom = imageTop + mTabImage.getMeasuredHeight();
        mTabImage.layout(imageLeft, imageTop, imageRight, imageBottom);

        int unreadTipsLeft;
        int unreadTipsRight;
        int unreadTipsTop;
        int unreadTipsBottom;

        if ((left - padding) < mTabUnreadTips.getMeasuredWidth()) {
            unreadTipsLeft = widthD_value - mTabUnreadTips.getMeasuredWidth();
            unreadTipsRight = unreadTipsLeft + mTabUnreadTips.getMeasuredWidth();
            unreadTipsTop = (heightD_value - mTabUnreadTips.getMeasuredHeight()) / 2;
            unreadTipsBottom = unreadTipsTop + mTabUnreadTips.getMeasuredHeight();
            mTabUnreadTips.layout(unreadTipsLeft, unreadTipsTop, unreadTipsRight, unreadTipsBottom);
            return;
        }
        unreadTipsLeft = right + padding;
        unreadTipsRight = unreadTipsLeft + mTabUnreadTips.getMeasuredWidth();
        unreadTipsTop = (heightD_value - mTabUnreadTips.getMeasuredHeight()) / 2;
        unreadTipsBottom = unreadTipsTop + mTabUnreadTips.getMeasuredHeight();
        mTabUnreadTips.layout(unreadTipsLeft, unreadTipsTop, unreadTipsRight, unreadTipsBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = View.MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int makeHeightMeasureSpec = 0;
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        } else {
            makeHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        }

        mTabDescription.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), makeHeightMeasureSpec);
        mTabUnreadTips.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), makeHeightMeasureSpec);
        mTabImage.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), makeHeightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    /**
     *
     */
    public final void notifyChange() {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPTabView.class), "build " + index + " desc , unread " + getUnreadText());
        CCPAccessibilityManager.getInstance().buildViewDesc(this, mTabDescription.getText().toString(), getUnreadText().toString(), index);
    }

    /**
     * @param visibility
     */
    public final void setTabImageVisibility(boolean visibility) {

        mTabImage.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * set tab name description.
     *
     * @param resid
     */
    public final void setText(int resid) {
        mTabDescription.setText(resid);
    }

    /**
     * @return
     */
    public final CharSequence getUnreadText() {
        return mTabUnreadTips.getText().toString();
    }

    /**
     * set tab name color description.
     *
     * @param color
     */
    public final void setTextColor(int color) {
        mTabDescription.setTextColor(color);
    }

    public final void setTextColor(ColorStateList colors) {
        mTabDescription.setTextColor(colors);
    }

    public final void setUnreadTips(final String count) {
        if (TextUtils.isEmpty(count)) {
            mTabUnreadTips.setVisibility(View.INVISIBLE);
            return;
        }

        mTabUnreadTips.setVisibility(View.VISIBLE);
        mTabUnreadTips.post(new Runnable() {

            @Override
            public void run() {
                mTabUnreadTips.setText(count);
                notifyChange();
            }
        });
    }

}

