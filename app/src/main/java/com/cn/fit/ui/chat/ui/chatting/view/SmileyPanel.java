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

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.base.CCPDotView;
import com.cn.fit.ui.chat.common.base.CCPFlipper;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.EmoticonUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class SmileyPanel extends ChatFooterPanel implements
        CCPFlipper.OnFlipperPageListener,
        CCPFlipper.OnCCPFlipperMeasureListener {


    private static int APP_PANEL_HEIGHT_LANDSCAPE = 122;
    private static int APP_PANEL_HEIGHT_PORTRAIT = 179;

    private int mDefaultVerticalSpacing = DensityUtil.fromDPToPix(CCPAppManager.getContext(), 48);

    /**
     * default App panel emoji.
     */
    public static String APP_PANEL_NAME_DEFAULT = "emoji";

    private Context mContext;

    private int mEmojiPanelHeight = -1;

    private WindowManager mWindowManager;

    private CCPFlipper mFlipper;
    private CCPDotView mDotView;

    private ArrayList<EmojiGrid> mArrayList;

    private boolean mInitPanelHeight;

    /**
     * @param context
     * @param attrs
     */
    public SmileyPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        inflate(context, R.layout.ccp_smile_panel, this);

        mFlipper = ((CCPFlipper) findViewById(R.id.smiley_panel_flipper));
        mDotView = ((CCPDotView) findViewById(R.id.smiley_panel_dot));
        initEmojiPanel();
    }

    /**
     * init {@link com.cn.fit.ui.chat.ui.chatting.view.SmileyPanel} Child View
     * {@link EmojiGrid}
     */
    private void initEmojiPanel() {

        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "initEmojiPanel");
        mFlipper.removeAllViews();
        mFlipper.setOnFlipperListner(this);
        mFlipper.setOnCCPFlipperMeasureListener(this);

        mInitPanelHeight = true;

        View smileyPanelDisplayView = findViewById(R.id.smiley_panel_display_view);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) smileyPanelDisplayView.getLayoutParams();
        if (getWindowDisplayMode() == 2) {
            layoutParams.height = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_LANDSCAPE);
        } else {
            int panelHeight = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_PORTRAIT);
            if (mEmojiPanelHeight > 0) {
                panelHeight = mEmojiPanelHeight;
            }
            layoutParams.height = panelHeight;
        }
        smileyPanelDisplayView.setLayoutParams(layoutParams);

        if (mArrayList != null && mArrayList.size() > 0) {
            Iterator<EmojiGrid> iterator = mArrayList.iterator();
            while (iterator.hasNext()) {
                EmojiGrid emojiGrid = (EmojiGrid) iterator.next();
                if (emojiGrid != null) emojiGrid.releaseEmojiView();
            }
            mArrayList.clear();
        }

    }

    private int getWindowDisplayMode() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        Display localDisplay = mWindowManager.getDefaultDisplay();
        return localDisplay.getWidth() < localDisplay.getHeight() ? Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    public void swicthToPanel(String panelName) {
        LogUtil.d("AppPanel.swicthToPanel panelName: " + panelName);
        setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(panelName)) {
            return;
        }

        if (mArrayList == null) {
            mArrayList = new ArrayList<EmojiGrid>();
        }

        if (APP_PANEL_NAME_DEFAULT.equals(panelName)) {
            doEmoji(mArrayList);
        }
    }

    private int getSmileyPanelVerticalSpacing() {
        if (getWindowDisplayMode() != 2) {
            return (mEmojiPanelHeight - (3 * mDefaultVerticalSpacing)) / (4);
        }
        return (APP_PANEL_HEIGHT_LANDSCAPE - (2 * mDefaultVerticalSpacing)) / (3);
    }


    /**
     * @param mArrayList
     */
    private void doEmoji(ArrayList<EmojiGrid> mArrayList) {

        mFlipper.removeAllViews();
        if (mArrayList == null || mArrayList.size() <= 0) {
            doEmoji();
            return;
        }

        Iterator<EmojiGrid> iterator = mArrayList.iterator();
        while (iterator.hasNext()) {
            EmojiGrid emojiGrid = (EmojiGrid) iterator.next();
            mFlipper.addView(emojiGrid, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            emojiGrid.setOnEmojiItemClickListener(mItemClickListener);
        }

        if (mArrayList.size() <= 1) {
            mDotView.setVisibility(View.INVISIBLE);
            return;
        }

        mDotView.setVisibility(View.VISIBLE);
        mDotView.setDotCount(mArrayList.size());
        mDotView.setSelectedDot(mFlipper.getVisiableIndex());
    }

    private void doEmoji() {

        mFlipper.removeAllViews();

        int pageCount = (int) Math.ceil(EmoticonUtil.getInstace().getEmojiSize() / 20 + 0.1);
        LogUtil.d("AppPanel.doEmoji emoji total pageCount :" + pageCount);

        for (int i = 0; i < pageCount; i++) {
            EmojiGrid emojiGrid = new EmojiGrid(getContext());
            emojiGrid.initEmojiGrid(20, i, pageCount, i + 1, 7, getWidth());
            emojiGrid.setOnEmojiItemClickListener(mItemClickListener);
            mFlipper.addView(emojiGrid, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            mFlipper.setInterceptTouchEvent(true);

            //int smileyPanelVerticalSpacing = getSmileyPanelVerticalSpacing();
            //emojiGrid.setVerticalSpacing(smileyPanelVerticalSpacing);

            mArrayList.add(emojiGrid);
        }
        if (mArrayList.size() <= 1) {
            mDotView.setVisibility(View.INVISIBLE);
            return;
        }

        mDotView.setVisibility(View.VISIBLE);
        mDotView.setDotCount(pageCount);
        mDotView.setSelectedDot(0);
    }

    /**
     * Change the height of the panel
     *
     * @param height
     */
    public final void setPanelHeight(int height) {
        if (mEmojiPanelHeight == height) {
            return;
        }

        mEmojiPanelHeight = height;
        mInitPanelHeight = true;
    }


    /**
     * @param visibility
     */
    public void setPanelGone() {
        mFlipper.removeAllViews();
        mDotView.removeAllViews();
        doChatTools();
    }

    public void doChatTools() {
        setVisibility(View.GONE);
    }


    /**
     * release SmileyPanel view.
     */
    public void releaseSmileyPanel() {
        setPanelGone();
        mFlipper.setOnFlipperListner(null);
        mFlipper.setOnCCPFlipperMeasureListener(null);

        if (mArrayList != null && mArrayList.size() > 0) {
            Iterator<EmojiGrid> iterator = mArrayList.iterator();
            while (iterator.hasNext()) {
                EmojiGrid emojiGrid = (EmojiGrid) iterator.next();
                if (emojiGrid != null) emojiGrid.releaseEmojiView();
            }
            mArrayList.clear();
        }

        mFlipper = null;
        mDotView = null;

    }


    @Override
    public void onPause() {

    }


    @Override
    public void onResume() {
        swicthToPanel(APP_PANEL_NAME_DEFAULT);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.ui.base.im.ChatFooterPanel#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseSmileyPanel();
    }

    @Override
    public void reset() {

    }

    @Override
    public void onCCPFlipperMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    @Override
    public void onFlipperPage(int startIndex, int finalIndex) {
        if (mDotView == null) {
            return;
        }
        if (finalIndex > mDotView.getDotCount()) {
            finalIndex = mDotView.getDotCount();
        }
        mDotView.setSelectedDot(finalIndex);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.ui.base.im.ChatFooterPanel#setChatFooterPanelHeight(int)
     */
    @Override
    public void setChatFooterPanelHeight(int height) {

    }

}
