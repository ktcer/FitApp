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
package com.cn.fit.ui.chat.common.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;


/**
 * <p>Title: CCPFlipper.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class CCPFlipper extends ViewGroup {


    Interpolator mFLipperInterpolator;
    Scroller mScroller;

    /**
     * Helper for tracking the velocity of touch events, for implementing
     * flinging and other such gestures.
     */
    VelocityTracker mTracker;

    /**
     * @see #onMeasure(int, int)
     */
    OnCCPFlipperMeasureListener onCCPFlipperMeasureListener;

    /**
     *
     */
    OnFlipperPageListener onFlipperPageListener;

    /**
     * Distance a touch can wander before we think the user is scrolling in pixels
     */
    private int minScaledTouchSlop;

    /**
     *
     */
    private int mLastScreen;

    /**
     * current screen view index.
     */
    private int mCurScreen;

    /**
     *
     */
    private int mToScreen;

    private static final int REST_STATE = 0;
    private static final int SCROLLING_STATE = 1;

    // touch state identification
    private int mTouchState = REST_STATE;

    /**
     * The first pointer index of X-coordinate
     */
    private float mTouchX;

    /**
     * The first pointer index of Y-coordinate
     */
    private float mTouchY;

    /**
     *
     */
    private boolean mExecutFlipper = false;

    private boolean mEnabled = false;


    public CCPFlipper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initFlipper(context);
    }

    public CCPFlipper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        initFlipper(context);
    }

    public CCPFlipper(Context context) {
        this(context, null);

        initFlipper(context);
    }


    public void initFlipper(Context context) {

        mFLipperInterpolator = new FlipperInterpolator();
        mScroller = new Scroller(getContext(), mFLipperInterpolator);

        this.mLastScreen = -1;
        this.mCurScreen = 0;
        this.mToScreen = 0;
        minScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * @param l
     */
    public void setOnCCPFlipperMeasureListener(OnCCPFlipperMeasureListener l) {
        this.onCCPFlipperMeasureListener = l;
    }

    /**
     * @param l
     */
    public void setOnFlipperListner(OnFlipperPageListener l) {
        this.onFlipperPageListener = l;
    }

    /**
     *
     */
    public boolean isFLipper() {

        return this.mExecutFlipper;
    }

    public void releaseFlipper() {

        this.mLastScreen = -1;
        this.mCurScreen = 0;
        this.mToScreen = 0;
    }

    public int getCurrentIndex() {

        LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class), "CCPlipper.getCurrentIndex cur screen is: " + this.mCurScreen);
        return this.mCurScreen;
    }

    /**
     * @param screenIndex
     * @param duration
     */
    public void processVelocity(int screenIndex, int duration) {

        int index = Math.max(0, Math.min(screenIndex, getChildCount() - 1));

        Scroller scroller = null;
        int unScrollx = 0;
        int scrollx = 0;
        if (getScrollX() != index * getWidth()) {

            unScrollx = index * getWidth() - getScrollX();
            scroller = this.mScroller;
            scrollx = getScrollX();

            if (duration > 0) {
                return;
            }

            int _duration = DensityUtil.round(getContext(), 2 * Math.abs(unScrollx));
            if (duration > 0) {
                _duration = duration;
            }

            scroller.startScroll(scrollx, 0, unScrollx, 0, _duration);
            if (mCurScreen != index) {
                this.mExecutFlipper = true;
                this.mToScreen += index - this.mCurScreen;
            }

            this.mLastScreen = this.mCurScreen;
            this.mCurScreen = index;

            // Static heavy painted View picture
            invalidate();
        }

    }

    /**
     * @param finalIndex
     */
    public final void setScroolIndex(int finalIndex) {
        this.mToScreen = finalIndex;
    }

    /**
     * Sliding to the specified page
     *
     * @param index
     */
    public final void slipInto(int index) {
        int maxIndex = Math.max(0, Math.min(index, -1 + getChildCount()));
        mExecutFlipper = false;
        if (mScroller != null && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        if (onFlipperPageListener != null) {
            onFlipperPageListener.onFlipperPage(mLastScreen, maxIndex);
        }
        mLastScreen = mCurScreen;
        mCurScreen = maxIndex;
        mToScreen = maxIndex;
        scrollTo(maxIndex * getWidth(), 0);
    }

    /**
     * @return
     */
    public int getVisiableIndex() {
        return mToScreen;
    }

    @Override
    public void computeScroll() {

        // Returns true if the animation is not over.
        // Because of the previous startScroll,
        // so only in the completion of startScroll then false
        if (this.mScroller.computeScrollOffset()) {

            // The animation effect, according to the current value of each scrolling
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());

            // At this time also need to refresh the View, otherwise there may be errors
            postInvalidate();
        } else {
            if (this.mExecutFlipper) {
                this.mExecutFlipper = false;
                if (this.onFlipperPageListener != null) {
                    this.onFlipperPageListener.onFlipperPage(this.mLastScreen, this.mToScreen);
                }
            }
        }


    }

    /**
     * @param enabled
     */
    public void setInterceptTouchEvent(boolean enabled) {
        this.mEnabled = enabled;
    }

    /**
     * release the velocityTracker
     */
    void releaseVelocityTracker() {
        if (null != this.mTracker) {
            this.mTracker.clear();
            this.mTracker.recycle();
            this.mTracker = null;
        }

        mTouchX = 0.0F;
        mTouchY = 0.0F;
        mTouchState = REST_STATE;

    }


    /**
     * Implement this method to intercept hover events before they are handled
     * by child views.
     *
     * @return True if the view group would like to intercept the hover event
     * and prevent its children from receiving it.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!mEnabled) {
            return super.onInterceptTouchEvent(ev);
        }

        // Implementation of the onInterceptTouchEvent method to intercept
        // the mobile event User finger touch screen
        if ((ev.getAction() == MotionEvent.ACTION_MOVE)
                && (mTouchState != REST_STATE)) {
            return true;
        }

        if (getChildCount() != 1) {
            int action = ev.getAction();
            //if(action != MotionEvent.ACTION_MOVE) {

            float x = ev.getX();
            float y = ev.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // begin to press the touch screen events
                    // save the X coordinate that press
                    this.mTouchX = x;
                    this.mTouchY = y;
                    mTouchState = mScroller.isFinished() ? REST_STATE : SCROLLING_STATE;
                    break;

                case MotionEvent.ACTION_MOVE:
                    // begin to press the touch screen mobile Move event
                    // Judge ACTION_MOVE events between mobile X coordinate space
                    int distanceX = (int) Math.abs(this.mTouchX - x);
                    int distanceY = (int) Math.abs(this.mTouchY - y);
                    LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class), "xDif = " + distanceX + ", yDif = " + distanceY);
                    if ((distanceX > this.minScaledTouchSlop) && (distanceY < this.minScaledTouchSlop)) {
                        LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class),
                                "CCPFlipper.onInterceptHoverEvent distanceX:"
                                        + distanceX + " , distanceY:"
                                        + distanceY + " , minScaledTouchSlop:"
                                        + minScaledTouchSlop);
                        mTouchState = SCROLLING_STATE;
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // finger off the screen
                    mTouchState = REST_STATE;

                    break;
                default:
                    break;
            }

            return mTouchState != REST_STATE;
            //}
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getChildCount() != 1) {
            if (mTracker == null) {
                // Returns a new VelocityTracker.
                mTracker = VelocityTracker.obtain();
            }
            // Add a user's movement to the tracker
            mTracker.addMovement(event);

            int action = event.getAction();
            float x = event.getX();
            // OnTouchEvent screen touch event of user
            switch (action) {
                case MotionEvent.ACTION_DOWN:

                    if (mScroller != null
                            && !mScroller.isFinished()) {
                        // stop the Flipper animation.
                        mScroller.abortAnimation();
                    }
                    // when the finger press screen trigger event, recording the X coordinates
                    this.mTouchX = x;

                    break;
                case MotionEvent.ACTION_MOVE:

                    int distance = (int) (this.mTouchX - x);
                    this.mTouchX = x;

                    scrollBy(distance, 0);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // when the finger off the screen, a record of the mVelocityTracker record,
                    // and achieved X axis sliding velocity
                    VelocityTracker tracker = this.mTracker;
                    // Initial rate units
                    tracker.computeCurrentVelocity(1000);
                    int xVelocity = (int) tracker.getXVelocity();

                    // when the X axis sliding speed is greater than 600, and mCurScreen > 0
                    if ((xVelocity > 600) && (this.mCurScreen > 0)) {
                        // mobile picture to the left
                        processVelocity(this.mCurScreen - 1, -1);

                    } else if ((xVelocity < -600) && (this.mCurScreen < getChildCount() - 1)) {
                        // Move to the right of the picture
                        processVelocity(this.mCurScreen + 1, -1);

                    } else {
                        int width = getWidth();
                        processVelocity((getScrollX() + width / 2) / width, -1);
                    }

                    releaseVelocityTracker();
                    break;
                default:
                    break;
            }
            //mScrollingX = MyViewGroup.this.getScrollX();
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int childCount = getChildCount();
        int left = 0;
        for (int k = 0; k < childCount; k++) {
            View view = getChildAt(k);
            if (view != null && view.getVisibility() != View.GONE) {
                int measuredWidth = view.getMeasuredWidth();
                LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class), "CCPFlipper onLayout childWidth : " + measuredWidth);
                view.layout(left, 0, left + measuredWidth, view.getMeasuredHeight());

                left += measuredWidth;
            }
        }

        long curElapsedRealtime = SystemClock.elapsedRealtime();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class), "CCPFlipper.onLayout use " + (curElapsedRealtime - elapsedRealtime)
                + " ms, CCPFlipper onLayout changed:" + changed
                + " Left,Top,Right,Bottom:" + l + "," + t + "," + r + "," + b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        long elapsedRealtime = SystemClock.elapsedRealtime();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("CCPFlipper.onMeasure error mode");
        }

        if (this.onCCPFlipperMeasureListener != null) {
            this.onCCPFlipperMeasureListener.onCCPFlipperMeasure(sizeWidth, sizeHeight);
        }

        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            getChildAt(index).measure(widthMeasureSpec, heightMeasureSpec);
        }
        scrollTo(sizeWidth * mCurScreen, 0);
        long curElapsedRealtime = SystemClock.elapsedRealtime();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPFlipper.class), "CCPFlipper onMeasure:" + sizeWidth
                + "," + MeasureSpec.getSize(heightMeasureSpec)
                + " childCount:" + childCount + ", use "
                + (curElapsedRealtime - elapsedRealtime));
    }


    /**
     * <p>Title: FlipperInterpolator</p>
     * <p>Description: An interpolator where the change dot then Slide the spring back effect. </p>
     * <p>Company: http://www.cloopen.com/</p>
     *
     * @author Jorstin Chan
     * @version 3.6
     * @date 2013-12-24
     */
    class FlipperInterpolator implements Interpolator {

        private float mTension = 1.3F;

        @Override
        public float getInterpolation(float input) {

            input = input - 1.0F;

            return 1.0F + input * input * (input * (1.0F + mTension) + mTension);
        }

    }

    /**
     * <p>Title: OnCCPFlipperMeasureListener</p>
     * <p>Description: Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overriden by subclasses to provide accurate and efficient
     * measurement of their contents.</p>
     * <p>Company: http://www.cloopen.com/</p>
     *
     * @author Jorstin Chan
     * @version 3.6
     * @date 2013-12-24
     */
    public abstract interface OnCCPFlipperMeasureListener {

        /**
         * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
         *                          The requirements are encoded with
         *                          {@link android.view.View.MeasureSpec}.
         * @param heightMeasureSpec vertical space requirements as imposed by the parent.
         *                          The requirements are encoded with
         *                          {@link android.view.View.MeasureSpec}.
         */
        public abstract void onCCPFlipperMeasure(int widthMeasureSpec, int heightMeasureSpec);
    }

    /**
     * <p>Title: OnFlipperPageListener</p>
     * <p>Description: </p>
     * <p>Company: http://www.cloopen.com/</p>
     *
     * @author Jorstin Chan
     * @version 3.6
     * @date 2013-12-24
     */
    public abstract interface OnFlipperPageListener {

        /**
         * @param startIndex
         * @param finalIndex
         */
        public abstract void onFlipperPage(int startIndex, int finalIndex);
    }

}


