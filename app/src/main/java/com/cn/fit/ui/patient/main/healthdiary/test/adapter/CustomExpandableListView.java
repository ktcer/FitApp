package com.cn.fit.ui.patient.main.healthdiary.test.adapter;

import android.content.Context;
import android.widget.ExpandableListView;

public class CustomExpandableListView extends ExpandableListView {

    public CustomExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        widthMeasureSpec = MeasureSpec
                .makeMeasureSpec(960, MeasureSpec.AT_MOST);

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(4000,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            // TODO: Workaround for
            // http://code.google.com/p/android/issues/detail?id=22751
        }
    }

}
