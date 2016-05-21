package com.cn.fit.ui.bluetooth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class BluetoothFallSettingActivity extends ActivityBasic {
    private ListView deviceLV;
    private Button searchBtn;
    private Button disconnectBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_fall_setting);
        initView();
    }

    private void initView() {
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("蓝牙设备");
        deviceLV = (ListView) findViewById(R.id.bluetooth_fall_device_list);
        searchBtn = (Button) findViewById(R.id.bluttooth_fall_search_btn);
        disconnectBtn = (Button) findViewById(R.id.bluttooth_fall_disconnect_btn);
    }

}
