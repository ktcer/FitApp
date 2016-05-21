package com.cn.fit.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.cn.fit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuangtiecheng
 * @Description:心电图视图
 */
public class HeartRateView extends View {
    private List mPointList = new ArrayList();
    private int mPointY = 0;
    private Paint mPoint = new Paint();   //画笔

    public HeartRateView(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        this(context, attrs, 0);
    }

    public HeartRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        //初始化画笔
        mPoint.setColor(getContext().getResources().getColor(R.color.green
        ));
        mPoint.setStrokeWidth(2.0f);
        mPoint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        //mPointY = (int) (Math.random() * 100);
        if (mPointList.size() >= 2) {
            for (int k = 0; k < -1 + mPointList.size(); k++) {
                paramCanvas.drawLine(((Point) mPointList.get(k)).x,
                        ((Point) mPointList.get(k)).y,
                        ((Point) mPointList.get(k + 1)).x,
                        ((Point) mPointList.get(k + 1)).y, mPoint);
            }
        }
        Point localPoint1 = new Point(getWidth(), getHeight() / 2 + mPointY);
        int i = mPointList.size();
        int j = 0;
        if (i > 201) {                    //最多绘制100个点，多余的出栈
            mPointList.remove(0);
            while (j < 200) {
                Point localPoint3 = (Point) mPointList.get(j);
                localPoint3.x = (-7 + localPoint3.x);
                j++;
            }
            mPointList.add(localPoint1);
            return;
        }

        while (j < mPointList.size()) {    //每新产生使前面的每一个点左移7
            Point localPoint2 = (Point) mPointList.get(j);
            localPoint2.x = (-7 + localPoint2.x);
            j++;
        }
        mPointList.add(localPoint1);
    }

    public final void ClearList() {
        mPointList.clear();
    }

    public final void AddPointToList(int paramInt) {
        mPointY = paramInt;
        invalidate();//重绘
    }

    public void stop() {
        mPointList.clear();
        invalidate();
    }

}