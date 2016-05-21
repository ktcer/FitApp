package com.cn.fit.ui.patient.setting;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.welcome.AscycUpData;

public class ActivityAboutUs extends ActivityBasic {
    private TextView tv1;
    private SettingItem upDataApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityaboutus);
        initial();

    }

    private void initial() {
        tv1 = (TextView) this.findViewById(R.id.tv1_aboutUs);
        upDataApp = (SettingItem) this.findViewById(R.id.txt_checkupdate);
        upDataApp.setOnClickListener(this);

        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("关于我们");
        TextPaint tp = tv1.getPaint();
        tp.setFakeBoldText(true);
        tv1.setText("Ver:" + AppMain.versionName);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_checkupdate:
//			upCheck up =	new upCheck();
//			up.execute();
                //这里来检测版本是否需要更新
                AscycUpData ascycUpdate = new AscycUpData(this);
                ascycUpdate.execute();
                break;
            default:
                break;
        }
    }

}
