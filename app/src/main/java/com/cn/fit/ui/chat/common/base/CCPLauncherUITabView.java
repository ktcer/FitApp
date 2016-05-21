package com.cn.fit.ui.chat.common.base;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * 主界面Tab容器
 * Created by Jorstin on 2015/3/18.
 */
public class CCPLauncherUITabView extends RelativeLayout implements View.OnClickListener {

    /**
     * Show that the main tab at first.
     */
    public static final int TAB_VIEW_MAIN = 0;

    /**
     * Show that the second tab view
     */
    public static final int TAB_VIEW_SECOND = 1;

    /**
     * Show that the third list .
     */
    public static final int TAB_VIEW_THIRD = 2;

    /**
     * The holder for main TabView
     */
    private TabViewHolder mMainTabView;

    /**
     * The holder for Second TabView
     */
    private TabViewHolder mSecondTabView;

    /**
     * The holder for third TabView
     */
    private TabViewHolder mThirdTabView;

    /**
     * Follow the label moved slowly
     */
    private Bitmap mIndicatorBitmap;

    /**
     *
     */
    private Matrix mMatrix = new Matrix();

    /**
     * Slide unit
     */
    private int mTabViewBaseWidth;

    /**
     * The current label location, is the need to move the index
     */
    private int mCurrentSlideIndex;

    /**
     *
     */
    private ImageView mSlideImage;

    /**
     * UITableView items.
     */
    //private String[] mItems;

    /**
     * UITableView item size.
     */
    //private int mItemSize;

    /**
     * tab view click.
     */
    private long mClickTime = 0L;

    /**
     * Listener used to dispatch click events.
     */
    private OnUITabViewClickListener mListener;
    /**
     * 游标个数
     */
    private int count = 3;

    /**
     *
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.v(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "onMainTabClick");
            if (mListener != null) {
                mListener.onTabClick(TAB_VIEW_MAIN);
            }
        }

    };

    /**
     * @param context
     */
    public CCPLauncherUITabView(Context context) {
        super(context);

        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPLauncherUITabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPLauncherUITabView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    /**
     * 初始化三个Tab 视图
     */
    private void init() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setId(R.id.main_tab_root);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        addView(layout, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(getContext());
        imageView.setImageMatrix(mMatrix);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setId(R.id.main_tab_navigation_img);
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(-1, DensityUtil.fromDPToPix(getContext(), count));//3 //3个游标的时候是3，count默认是3
        imageViewLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.main_tab_root);
        addView(mSlideImage = imageView, imageViewLayoutParams);

        // TabView dial
        TabViewHolder tabViewMain = createTabView(TAB_VIEW_MAIN);
        tabViewMain.tabView.setText(R.string.main_title);
        LinearLayout.LayoutParams enbGroupLayoutParams = new LinearLayout.LayoutParams(
                0, getResources().getDimensionPixelSize(R.dimen.DefaultTabbarHeight));
        enbGroupLayoutParams.weight = 1.0F;
        layout.addView(tabViewMain.tabView, enbGroupLayoutParams);
        mMainTabView = tabViewMain;

        // TabView communication
        TabViewHolder tabViewSecond = createTabView(TAB_VIEW_SECOND);
        tabViewSecond.tabView.setText(R.string.main_contact);
        LinearLayout.LayoutParams secondLayoutParams = new LinearLayout.LayoutParams(
                0, getResources().getDimensionPixelSize(R.dimen.DefaultTabbarHeight));
        secondLayoutParams.weight = 1.0F;
        layout.addView(tabViewSecond.tabView, secondLayoutParams);
        mSecondTabView = tabViewSecond;

        // TabView contacts
        TabViewHolder tabViewContacts = createTabView(TAB_VIEW_THIRD);
        tabViewContacts.tabView.setText(R.string.main_group);
        LinearLayout.LayoutParams contactsLayoutParams = new LinearLayout.LayoutParams(
                0, getResources().getDimensionPixelSize(R.dimen.DefaultTabbarHeight));
        contactsLayoutParams.weight = 1.0F;
        layout.addView(tabViewContacts.tabView, contactsLayoutParams);
        mThirdTabView = tabViewContacts;
    }

    /**
     * Register a callback to be invoked when this UITabView is clicked.
     *
     * @param l The callback that will run
     */
    public void setOnUITabViewClickListener(OnUITabViewClickListener l) {
        mListener = l;
    }


    /**
     * Set a list of items to be displayed in the UITableView as the content, you will be notified of the
     * selected item via the supplied listener. This should be an array type i.e. R.array.foo
     *
     * @param itemsId
     */
    public final void setTabViewItems(int itemsId) {
        //mItems = getResources().getStringArray(itemsId);
        //mItemSize = mItems.length;
    }

    /**
     * @param index
     */
    public final void setTabViewText(int index, int resid) {
        switch (index) {
            case TAB_VIEW_MAIN:

                mMainTabView.tabView.setText(resid);
                break;
            case TAB_VIEW_SECOND:
                mSecondTabView.tabView.setText(resid);
                break;

            case TAB_VIEW_THIRD:
                mThirdTabView.tabView.setText(resid);
                break;
            default:
                break;
        }
    }

    /**
     * create new TabView.
     *
     * @param index
     * @return
     */
    public TabViewHolder createTabView(int index) {
        TabViewHolder tabViewHolder = new TabViewHolder();
        tabViewHolder.tabView = new CCPTabView(getContext(), index);
        tabViewHolder.tabView.setTag(Integer.valueOf(index));
        tabViewHolder.tabView.setOnClickListener(this);
        return tabViewHolder;
    }


    public final void resetTabViewDesc() {
        if (mMainTabView == null || mSecondTabView == null || mThirdTabView == null) {
            return;
        }

        mMainTabView.tabView.notifyChange();
        mSecondTabView.tabView.notifyChange();
        mThirdTabView.tabView.notifyChange();
    }


    @Override
    public void onClick(View v) {
        int intValue = ((Integer) v.getTag()).intValue();
        if ((mCurrentSlideIndex == intValue) && (intValue == TAB_VIEW_SECOND) && (System.currentTimeMillis() - mClickTime <= 300L)) {
            LogUtil.v(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "onMainTabDoubleClick");
            mHandler.removeMessages(0);
            // Processing double click
            mClickTime = System.currentTimeMillis();
            mCurrentSlideIndex = intValue;
            return;
        }

        if (mListener != null) {
            if (intValue != TAB_VIEW_MAIN || mCurrentSlideIndex != TAB_VIEW_SECOND) {
                mClickTime = System.currentTimeMillis();
                mCurrentSlideIndex = intValue;
                mListener.onTabClick(intValue);
                return;
            }
            mHandler.sendEmptyMessageDelayed(0, 300L);
        }

        mClickTime = System.currentTimeMillis();
        mCurrentSlideIndex = intValue;
        LogUtil.v(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "on UITabView click, index " + intValue + ", but listener is " + mListener);
    }

    /**
     *
     */
    public final void doSentEvents() {
        if (mSecondTabView == null || mMainTabView == null) {
            return;
        }

        mMainTabView.tabView.notifyChange();
        mSecondTabView.tabView.notifyChange();
        mThirdTabView.tabView.notifyChange();
    }

    /**
     * 移动
     *
     * @param start    开始位置
     * @param distance 移动距离
     */
    public final void doTranslateImageMatrix(int start, float distance) {
        mMatrix.setTranslate(mTabViewBaseWidth * (start + distance), 0.0F);
        mSlideImage.setImageMatrix(mMatrix);
    }

    /**
     * Set the TabView to operate
     *
     * @param visibility
     */
    public final void setUnreadDotVisibility(boolean visibility) {

    }

    /**
     * Update the interface number of unread
     *
     * @param unreadCount
     */
    public final void updateMainTabUnread(int unreadCount) {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "updateMainTabUnread unread count " + unreadCount);
        if (unreadCount > 0) {
            if (unreadCount > 99) {
                mMainTabView.tabView.setUnreadTips(getResources().getString(R.string.unread_count_overt_100));
                return;
            }
            mMainTabView.tabView.setUnreadTips(String.valueOf(unreadCount));
            return;
        }
        mMainTabView.tabView.setUnreadTips(null);
    }


    public final void updateSecondTabUnread(int unreadCount) {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "updateSecondTabUnread unread count " + unreadCount);
        if (unreadCount > 0) {
            if (unreadCount > 99) {
                mSecondTabView.tabView.setUnreadTips(getResources().getString(R.string.unread_count_overt_100));
                return;
            }
            mSecondTabView.tabView.setUnreadTips(String.valueOf(unreadCount));
            return;
        }
        mSecondTabView.tabView.setUnreadTips(null);
    }

    /**
     * Update the interface number of unread
     *
     * @param unreadCount
     */
    public final void updateContactsTabUnread(int unreadCount) {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "updateContactsTabUnread unread count " + unreadCount);
        if (unreadCount > 0) {
            if (unreadCount > 99) {
                mThirdTabView.tabView.setUnreadTips(getResources().getString(R.string.unread_count_overt_100));
                return;
            }
            mThirdTabView.tabView.setUnreadTips(String.valueOf(unreadCount));
            return;
        }
        mThirdTabView.tabView.setUnreadTips(null);
    }

    /**
     * Change the selected TabView color
     *
     * @param index
     */
    @SuppressLint("ResourceAsColor")
    public final void doChangeTabViewDisplay(int index) {
        mCurrentSlideIndex = index;
        switch (index) {
            case TAB_VIEW_MAIN:
                mMainTabView.tabView.setTextColor(getResources().getColorStateList(R.color.blue_second));
                mSecondTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                mThirdTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                break;
            case TAB_VIEW_SECOND:
                mMainTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                mSecondTabView.tabView.setTextColor(getResources().getColorStateList(R.color.blue_second));
                mThirdTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                break;
            case TAB_VIEW_THIRD:
                mMainTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                mSecondTabView.tabView.setTextColor(getResources().getColorStateList(R.color.launcher_tab_text_selector));
                mThirdTabView.tabView.setTextColor(getResources().getColorStateList(R.color.blue_second));
            default:
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtil.d(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class), "on layout, width " + (r - l));
        int width = mTabViewBaseWidth = ((r - l) / count);//3 //3个游标的时候是3，count默认是3

        if (mIndicatorBitmap == null || mIndicatorBitmap.getWidth() != mTabViewBaseWidth) {

            int from = -1;
            if (mIndicatorBitmap != null) {
                from = mIndicatorBitmap.getWidth();
            }
            LogUtil.d(LogUtil.getLogUtilsTag(CCPLauncherUITabView.class),
                    "sharp width changed, from " + from + " to " + width);

            mIndicatorBitmap = Bitmap.createBitmap(width,
                    DensityUtil.fromDPToPix(getContext(), count + 1),//3个游标的时候是4，count默认是3
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(mIndicatorBitmap);
            canvas.drawColor(getResources().getColor(R.color.blue_second));
            doTranslateImageMatrix(mCurrentSlideIndex, 0.0F);
            mSlideImage.setImageBitmap(mIndicatorBitmap);
            doChangeTabViewDisplay(mCurrentSlideIndex);
        }

    }

    /**
     * @author Jorstin Chan
     * @version 1.0
     * @date 2014-4-26
     */
    public class TabViewHolder {

        CCPTabView tabView;
    }

    /**
     * Interface definition for a callback to be invoked when a UITabView is clicked.
     */
    public abstract interface OnUITabViewClickListener {

        /**
         * Called when a UITabView has been clicked.
         *
         * @param tabIndex index of UITabView
         */
        public abstract void onTabClick(int tabIndex);
    }


    public void setTabViewText(List<Integer> text, int count) {
        this.mMainTabView.tabView.setText(text.get(0));
        this.mSecondTabView.tabView.setText(text.get(1));
        this.count = count;
        if (count < 3) {
            mThirdTabView.tabView.setVisibility(View.GONE);
        } else {
            this.mThirdTabView.tabView.setText(text.get(2));
            mThirdTabView.tabView.setVisibility(View.VISIBLE);
        }

    }


}
