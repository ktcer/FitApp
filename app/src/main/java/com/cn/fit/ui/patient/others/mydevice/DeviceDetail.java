package com.cn.fit.ui.patient.others.mydevice;

import android.os.Bundle;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class DeviceDetail extends ActivityBasic {
    private TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_device_detail);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("设备详情");
    }


}
