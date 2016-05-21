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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

/**
 * 自定义头像显示View
 * Created by Jorstin on 2015/3/18.
 */
public class PhotoBitmapDrawable extends BitmapDrawable {

    private Paint mPaint;
    private Rect mRect;
    private Bitmap mMaskBitmap;
    private int mColor;
    private int mWidth;
    private boolean mStroke;

    /**
     * @param bitmap
     * @deprecated
     */
    public PhotoBitmapDrawable(Bitmap bitmap, Bitmap mask, Paint paint) {
        super(bitmap);
        init(mask, paint);
    }

    private void init(Bitmap mask, Paint paint) {
        mPaint = paint;
        mMaskBitmap = mask;
        mRect = new Rect(0, 0, 0, 0);
    }

    @Override
    public void draw(Canvas canvas) {
        int saveCount = canvas.saveLayer(new RectF(canvas.getClipBounds()), null, 31);
        super.draw(canvas);
        if (mStroke) {
            Rect rect = canvas.getClipBounds();
            rect.left += mWidth / 2;
            rect.top += mWidth / 2;
            rect.bottom -= mWidth / 2;
            rect.right -= mWidth / 2;
            Paint paint = new Paint();
            paint.setColor(mColor);
            paint.setStrokeWidth(mWidth);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, paint);
        }
        canvas.drawBitmap(mMaskBitmap, null, mRect, mPaint);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (mRect.right < right) {
            mRect.right = right;
        }
        if (mRect.bottom < bottom) {
            mRect.bottom = bottom;
        }
    }
}
