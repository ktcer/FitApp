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
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.base.CCPDotView;
import com.cn.fit.ui.chat.common.base.CCPFlipper;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.chatting.AppPanelControl;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class AppPanel extends LinearLayout implements CCPFlipper.OnFlipperPageListener
        , CCPFlipper.OnCCPFlipperMeasureListener {

    private static int APP_PANEL_HEIGHT_LANDSCAPE = 158;
    private static int APP_PANEL_HEIGHT_PORTRAIT = 215;

    /**
     * The maximum number of rows of panel
     */
    private static int APPPANEL_MAX_ROWS = 2;

    /**
     * The min number of rows of panel
     */
    private static int APPPANEL_MIN_ROWS = 1;

    /**
     * The min number of column of panel
     */
    private static int APPPANEL_MIN_COLUMN = 1;

    private Context mContext;

    private WindowManager mWindowManager;

    private OnAppPanelItemClickListener mAppPanelItemClickListener;

    private CCPFlipper mFlipper;
    private CCPDotView mDotView;

    private LayoutParams mPanelLayoutParams;

    private List<AppGrid> mAppGrid;

    private int mAppPanelHeight = -1;
    private int mDisplayWidth = 0;
    private int mDisplayHeight = 0;

    /**
     * width of {@link com.cn.fit.ui.chat.ui.chatting.view.AppPanel} {@link AppGrid}
     */
    private int mGridWidth;

    /**
     * height of {@link com.cn.fit.ui.chat.ui.chatting.view.AppPanel} {@link AppGrid}
     */
    private int mGridHeight;

    private int mCCPCapabilityItems = 7;

    private boolean mAppPanelHeightChange = true;

    AppPanelControl mAppPanelControl;


    final AppGrid.OnCapabilityItemClickListener mCapabilityItemClickListener
            = new AppGrid.OnCapabilityItemClickListener() {

        @Override
        public void onPanleItemClick(int index, int capabilityId, String capabilityName) {
            if (mAppPanelItemClickListener == null) {
                return;
            }
            switch (capabilityId) {
                case R.string.app_panel_pic:
                    mAppPanelItemClickListener.OnSelectImageClick();
                    break;
                case R.string.app_panel_tackpic:
                    mAppPanelItemClickListener.OnTakingPictureClick();

                    break;
                case R.string.app_panel_file:
                    mAppPanelItemClickListener.OnSelectFileClick();
                    break;
                default:
                    break;
            }
        }
    };


    public AppPanel(Context context) {
        super(context);
        this.mContext = context;

        initAppPanelControl();
        initAppPanel();
    }

    public AppPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        initAppPanelControl();
        initAppPanel();
    }

    /**
     *
     */
    private void initAppPanelControl() {
        mAppPanelControl = new AppPanelControl();
        mCCPCapabilityItems = mAppPanelControl.cap.length;
    }

    private void computeCapabilityCount() {
        mCCPCapabilityItems = 3;
    }

    /**
     *
     */
    private void initAppPanel() {
        mPanelLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getWidth() < display.getHeight()) {
            mDisplayWidth = display.getWidth();
            mDisplayHeight = display.getHeight();
        } else {
            mDisplayWidth = display.getHeight();
            mDisplayHeight = display.getWidth();

        }
        View.inflate(getContext(), R.layout.app_panel, this);
        mFlipper = (CCPFlipper) findViewById(R.id.app_panel_flipper);
        mDotView = (CCPDotView) findViewById(R.id.app_panel_dot);

        try {

            initFlipper();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void refreshAppPanel() {
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "AppPanel refleshed");
        try {

            int currentIndex = mFlipper.getCurrentIndex();
            initAppGrid();
            mFlipper.slipInto(currentIndex);
            mDotView.setSelectedDot(currentIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param l
     */
    public void setOnAppPanelItemClickListener(OnAppPanelItemClickListener l) {
        mAppPanelItemClickListener = l;
    }

    private void initAppGrid() {
        if (mGridWidth == 0 || mGridHeight == 0) {
            return;
        }

        mAppGrid = new ArrayList<AppGrid>();
        mFlipper.removeAllViews();

        int columnWidth = DensityUtil.getMetricsDensity(getContext(), 73.0F);
        int rowsHeight = DensityUtil.getMetricsDensity(getContext(), 90.0F);
        requestLayout();

        int column = mGridWidth / columnWidth;
        int rows = mGridHeight / rowsHeight;

        if (rows > APPPANEL_MAX_ROWS) {
            rows = APPPANEL_MAX_ROWS;
        }

        int rowSpace = (mGridHeight - (rowsHeight * rows)) / (rows + 1);
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "AppPanel gridWidth "
                + mGridWidth + " , gridHeight " + mGridHeight
                + " ,rows spacing " + rowSpace);

        if (column == 0) {
            column = APPPANEL_MIN_COLUMN;
        }
        if (rows == 0) {
            rows = APPPANEL_MIN_ROWS;
        }

        int itemsPerPage = column * rows;
        int pageCount = (int) Math.ceil((1 + mCCPCapabilityItems) / (itemsPerPage + 0.1));

        LogUtil.d(LogUtil.getLogUtilsTag(getClass()),
                "initAppGrid, totalItemCount = " + mCCPCapabilityItems
                        + ", itemsPerPage = " + itemsPerPage + ", pageCount = "
                        + pageCount);

        for (int i = 0; i < pageCount; i++) {
            AppGrid appGrid = (AppGrid) inflate(getContext(), R.layout.app_grid, null);
            appGrid.setAppPanelItems(mAppPanelControl.getCapability());
            appGrid.setAppPanelBase(i, mCCPCapabilityItems, itemsPerPage, pageCount, column, mCCPCapabilityItems);
            appGrid.setPanelVerticalSpacing(rowSpace);
            mFlipper.setInterceptTouchEvent(true);
            mFlipper.addView(appGrid, mPanelLayoutParams);
            mAppGrid.add(appGrid);
        }

        if (mAppGrid != null) {
            for (AppGrid capability : mAppGrid) {
                capability.setOnCapabilityItemClickListener(mCapabilityItemClickListener);
            }
        }

        if (mAppGrid.size() <= 0) {
            mDotView.setVisibility(View.GONE);
        } else {
            mDotView.setVisibility(View.VISIBLE);
            mDotView.setDotCount(mAppGrid.size());
            int currentIndex = mFlipper.getCurrentIndex();
            mFlipper.slipInto(currentIndex);
            mDotView.setSelectedDot(currentIndex);
        }
        computeCapabilityCount();
    }

    /**
     *
     */
    private void initFlipper() {
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "AppPanel initFlipper");
        mFlipper.removeAllViews();
        mFlipper.setOnFlipperListner(this);
        mFlipper.setOnCCPFlipperMeasureListener(this);

        initFlipperRotateMe();
    }

    /**
     * Screen rotation change height
     */
    public final void initFlipperRotateMe() {
        if (mAppPanelHeightChange) {
            mAppPanelHeightChange = false;
            View panelDisplayView = findViewById(R.id.app_panel_display_view);
            LayoutParams layoutParams = (LayoutParams) panelDisplayView.getLayoutParams();
            if (getWindowDisplayMode() != Configuration.ORIENTATION_LANDSCAPE) {
                LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "initFlipper, mode portrait :" + APP_PANEL_HEIGHT_PORTRAIT);

                int panelHeight = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_PORTRAIT);
                if (mAppPanelHeight > 0) {
                    panelHeight = mAppPanelHeight;
                }
                layoutParams.width = mDisplayWidth;
                layoutParams.height = panelHeight;
            } else {
                LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "initFlipper, mode landscape :" + APP_PANEL_HEIGHT_LANDSCAPE);
                layoutParams.width = mDisplayHeight;
                layoutParams.height = DensityUtil.getMetricsDensity(getContext(), APP_PANEL_HEIGHT_LANDSCAPE);
            }
            panelDisplayView.setLayoutParams(layoutParams);
        }
    }

    /**
     */
    public boolean isPanelVisible() {

        return getVisibility() == View.VISIBLE;
    }

    private int getWindowDisplayMode() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        Display localDisplay = mWindowManager.getDefaultDisplay();
        return localDisplay.getWidth() < localDisplay.getHeight() ? Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Change the height of the panel
     *
     * @param height
     */
    public final void setPanelHeight(int height) {
        if (mAppPanelHeight == height) {
            return;
        }

        mAppPanelHeight = height;
        mAppPanelHeightChange = true;
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

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((newConfig.orientation != Configuration.ORIENTATION_PORTRAIT)
                && (newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            return;
        }
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "onConfigChanged:" + newConfig.orientation);
        mFlipper.slipInto(0);
    }


    @Override
    public void onCCPFlipperMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "onMeasure width:" + widthMeasureSpec + " height:" + heightMeasureSpec + " isMeasured:" + mAppPanelHeightChange);
        if (widthMeasureSpec == 0 || heightMeasureSpec == 0) {
            LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "onMeasure, width or height is 0");
            return;
        }

        if (getWindowDisplayMode() == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "landspace");
        } else {
            LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "portrait");
        }

        mGridWidth = widthMeasureSpec;
        mGridHeight = heightMeasureSpec;
        refreshAppPanel();
    }

    public interface OnAppPanelItemClickListener {
        void OnTakingPictureClick();

        void OnSelectImageClick();

        void OnSelectFileClick();

    }
}
