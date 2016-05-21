package com.cn.fit.ui.patient.main.healthdiary.test;

import android.view.View;

public interface NLevelListItem {

    public boolean isExpanded();

    public void toggle();

    public NLevelListItem getParent();

    public View getView();
}
