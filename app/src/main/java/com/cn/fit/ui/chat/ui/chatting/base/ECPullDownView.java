package com.cn.fit.ui.chat.ui.chatting.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.cn.fit.R;

/**
 * com.cn.aihu.ui.chat.ui.chatting.base in ECDemo_Android
 * Created by Jorstin on 2015/4/17.
 */
public class ECPullDownView extends FrameLayout implements GestureDetector.OnGestureListener {


    private static final String TAG = "ECPullDownView";
    private static int timeInterval = 400;
    private static final int bAI = Color.parseColor("#00000000");
    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private int scrollType;
    private int bottomViewInitializeVisibility = View.INVISIBLE;
    private int topViewInitializeVisibility = View.INVISIBLE;
    private boolean bAD = false;
    private boolean bAH = false;
    private boolean bAJ = false;
    private int topViewHeightCurrentPotion = -2147483648;
    private int bAL = this.bgColor;
    private OnRefreshAdapterDataListener mOnRefreshAdapterDataListener;
    private int topViewHeight;
    private int bottomViewHeight;
    private boolean isScrollToTop = false;
    private boolean isScrollFarTop = false;
    private boolean isMoveTop = false;
    private boolean isMoveDown = false;
    private boolean isScrollStoped = false;
    private boolean isFristTouch = true;
    private boolean isHideTopView = false;
    private boolean isCloseTopAllowRefersh = true;
    private boolean hasbottomViewWithoutscroll = true;
    private OnListViewBottomListener mOnListViewBottomListener;
    private OnListViewTopListener mOnListViewTopListener;
    private View topView;
    private View bottomView;
    private int bgColor = Color.parseColor("#ffffffff");
    private Context context;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (scrollType) {
                case 0:
                    if (mOnRefreshAdapterDataListener != null) {
                        mOnRefreshAdapterDataListener.refreshData();
                    }
                    if (topView.getVisibility() != View.VISIBLE) {
                        break;
                    }
                    scrollTo(0, topViewHeight);
                    break;
                case 1:
                    if (bottomView.getVisibility() != View.VISIBLE) {
                        break;
                    }
                    scrollTo(0, bottomViewHeight);
                    break;

            }
            startScroll();
        }

        ;
    };

    public ECPullDownView(Context context) {
        this(context, null);
    }

    public ECPullDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECPullDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScroller = new Scroller(context, new AccelerateInterpolator());
        this.mGestureDetector = new GestureDetector(this);
        // this.adQ = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (!this.bAD) {
            View localView2 = inflate(this.context, R.layout.loading_view, null);
            View localView1 = inflate(this.context, R.layout.loading_view, null);
            addView(localView2, 0, new FrameLayout.LayoutParams(-1, -2));
            addView(localView1, new FrameLayout.LayoutParams(-1, -2));
            this.bAD = true;
        }
        int m = getChildCount();
        int j = 0;
        int i = 0;
        while (true) {
            if (j >= m) {
                this.topView = getChildAt(0);
                this.bottomView = getChildAt(-1 + getChildCount());
                this.topView.setVisibility(View.INVISIBLE);
                this.bottomView.setVisibility(View.INVISIBLE);
                this.topViewHeight = this.topView.getHeight();
                this.bottomViewHeight = this.bottomView.getHeight();
                this.topViewHeightCurrentPotion = this.topViewHeight;
                if ((!this.isHideTopView) && (this.topViewHeight != 0)) {
                    this.isHideTopView = true;
                    scrollTo(0, this.topViewHeight);
                }
                return;
            }
            View localView3 = getChildAt(j);
            int k = localView3.getMeasuredHeight();
            if (localView3.getVisibility() != View.GONE) {
                localView3.layout(0, i, localView3.getMeasuredWidth(), i + k);
                i += k;
            }
            j++;
        }
    }

    public final int getTopViewHeight() {
        return this.topViewHeight;
    }

    public final void startTopScroll() {
        if (!this.isCloseTopAllowRefersh) {
            if (this.topView.getVisibility() == View.INVISIBLE) {
                this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + this.topViewHeight, 200);
            }
            if (this.topView.getVisibility() == View.VISIBLE) {
                this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
            }
            this.scrollType = 0;
            this.isScrollStoped = true;
            this.isFristTouch = false;
        } else {
            this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + this.topViewHeight, 200);
        }
        postInvalidate();
    }


    public final void setOnRefreshAdapterDataListener(OnRefreshAdapterDataListener paramcs) {
        this.mOnRefreshAdapterDataListener = paramcs;
    }

    public final void setOnListViewTopListener(OnListViewTopListener paramei) {
        this.mOnListViewTopListener = paramei;
    }

    public final void setOnListViewBottomListener(OnListViewBottomListener parames) {
        this.mOnListViewBottomListener = parames;
    }


    public final void setIsCloseTopAllowRefersh(boolean paramBoolean) {
        this.isCloseTopAllowRefersh = paramBoolean;
    }

    public final void setHasbottomViewWithoutscroll(boolean paramBoolean) {
        this.hasbottomViewWithoutscroll = paramBoolean;
    }

    public final void setTopViewInitialize(boolean paramBoolean) {
        int i;
        if (!paramBoolean)
            i = View.INVISIBLE;
        else
            i = View.VISIBLE;
        this.topViewInitializeVisibility = i;
        if (this.topView != null)
            this.topView.setVisibility(topViewInitializeVisibility);
    }

    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.bAJ) {
            if (this.topViewHeightCurrentPotion == -2147483648) {
                this.topViewHeightCurrentPotion = this.topViewHeight;
            }
            if ((paramInt2 > this.topViewHeightCurrentPotion) || (this.bAL == bAI)) {
                if ((paramInt2 > this.topViewHeightCurrentPotion) && (this.bAL != this.bgColor)) {
                    setBackgroundColor(this.bgColor);
                    this.bAL = this.bgColor;
                }
            } else {
                // setBackgroundResource(2130838685);
                this.bAL = bAI;
            }
        }
    }


    @Override
    public boolean onDown(MotionEvent e) {
        if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        this.isMoveTop = true;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        int i = -1;
        int j = 1;
        if (distanceY <= 0.0F) {
            this.isMoveDown = false;
        } else {
            this.isMoveDown = true;
        }
        int k;

        if (((!this.isMoveDown) || (!this.isScrollFarTop)) &&
                ((this.isMoveDown) || (getScrollY() - this.topViewHeight <= 0) || (!this.isScrollFarTop))) {

            if (((this.isMoveDown) || (!this.isScrollToTop)) &&
                    ((!this.isMoveDown) || (getScrollY() - this.topViewHeight >= 0) || (!this.isScrollToTop))) {
                j = 0;
            } else {
                k = (int) (0.5D * distanceY);
                if (k != 0) {
                    i = k;
                } else if (distanceY > 0.0F) {
                    i = j;
                }
                if (i + getScrollY() > this.topViewHeight)
                    i = this.topViewHeight - getScrollY();
                scrollBy(0, i);
                return true;
            }
        } else {
            k = (int) (0.5D * distanceY);
            if (k != 0) {
                i = k;
            } else if (distanceY > 0.0F) {
                i = j;
            }
            if ((i + getScrollY() < this.topViewHeight) && (!this.isMoveDown))
                i = this.topViewHeight - getScrollY();
            scrollBy(0, i);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    public final void nh(String paramString) {
        this.bgColor = Color.parseColor(paramString);
        this.bAL = this.bgColor;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    private void startScroll() {
        if (getScrollY() - this.topViewHeight < 0) {
            if (!this.isCloseTopAllowRefersh) {
                if (this.topView.getVisibility() == View.INVISIBLE) {
                    this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + this.topViewHeight, 200);
                }
                if (this.topView.getVisibility() == View.VISIBLE) {
                    this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
                }
                this.scrollType = 0;
                this.isScrollStoped = true;
                this.isFristTouch = false;
            } else {
                this.mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + this.topViewHeight, 200);
            }
            postInvalidate();
        }
        if (getScrollY() > this.bottomViewHeight) {
            if (!this.hasbottomViewWithoutscroll) {
                if (this.bottomView.getVisibility() == View.INVISIBLE)
                    this.mScroller.startScroll(0, getScrollY(), 0, this.bottomViewHeight - getScrollY(), 200);
                if (this.bottomView.getVisibility() == View.VISIBLE)
                    this.mScroller.startScroll(0, getScrollY(), 0, this.bottomViewHeight - getScrollY() + this.bottomViewHeight, 200);
                this.scrollType = 1;
                this.isScrollStoped = true;
                this.isFristTouch = false;
            } else {
                this.mScroller.startScroll(0, getScrollY(), 0, this.bottomViewHeight - getScrollY(), 200);
            }
            postInvalidate();
        }
        this.isMoveTop = false;
        this.isMoveDown = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool = true;
        if (this.isFristTouch) {
            if (this.mOnListViewTopListener != null) {
                this.isScrollToTop = this.mOnListViewTopListener.getIsListViewToTop();
            } else {
                this.isScrollToTop = false;
            }
            if (this.mOnListViewBottomListener != null) {
                this.isScrollFarTop = this.mOnListViewBottomListener.getIsListViewToBottom();
            } else {
                this.isScrollFarTop = false;
            }
            if (this.topViewInitializeVisibility == View.VISIBLE) {
                if (!this.isCloseTopAllowRefersh) {
                    this.topView.setVisibility(View.VISIBLE);
                } else {
                    this.topView.setVisibility(View.INVISIBLE);
                }
            }
            if (this.bottomViewInitializeVisibility == 0) {
                if (!this.hasbottomViewWithoutscroll) {
                    this.bottomView.setVisibility(View.VISIBLE);
                } else {
                    this.bottomView.setVisibility(View.INVISIBLE);
                }
            }
            if (paramMotionEvent.getAction() != MotionEvent.ACTION_UP) {
                if (paramMotionEvent.getAction() != MotionEvent.ACTION_CANCEL) {
                    if (!this.mGestureDetector.onTouchEvent(paramMotionEvent)) {
                        bool = super.dispatchTouchEvent(paramMotionEvent);
                    } else {
                        paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
                        this.bAH = bool;
                        bool = super.dispatchTouchEvent(paramMotionEvent);
                    }
                } else {
                    startScroll();
                }
            } else {
                startScroll();
                bool = super.dispatchTouchEvent(paramMotionEvent);
            }
        }
        return bool;

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!this.mScroller.computeScrollOffset()) {
            if (this.isScrollStoped) {
                this.isScrollStoped = false;
                this.mHandler.sendEmptyMessageDelayed(0, timeInterval);
            }
        } else {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
        isFristTouch = this.mScroller.isFinished();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getScrollY() - this.topViewHeight < 0) {
                    this.isScrollToTop = true;
                }
                if (getScrollY() > this.bottomViewHeight) {
                    this.isScrollFarTop = true;
                }
                startScroll();
        }
        return true;
    }
}
