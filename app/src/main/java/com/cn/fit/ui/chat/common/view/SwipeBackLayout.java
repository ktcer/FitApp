package com.cn.fit.ui.chat.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * com.cn.aihu.ui.chat.common.view in ECDemo_Android
 * Created by Jorstin on 2015/6/6.
 */
public class SwipeBackLayout extends FrameLayout {

    private boolean mInLayout;
    private boolean mEnable = true;
    private View mContentView;
    private float mScrimOpacity;
    private Rect mTmpRect = new Rect();
    /**
     * Default threshold of scroll
     */
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    private int mContentLeft;
    private int mContentTop;
    private Drawable mShadowRight;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

    }


    private class ViewDragCallback extends ViewDragHelper.Callback {
        private static final String TAG = "ViewDragCallback";

        @Override
        public boolean tryCaptureView(View view, int i) {
            return false;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            final int childWidth = releasedChild.getWidth();
            int left = 0, top = 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            LogUtil.d(TAG, "onViewDragStateChanged state " + state + ", requestedTranslucent %B fastRelease %B\"");
        }
    }
}
