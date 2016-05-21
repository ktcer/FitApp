package com.cn.fit.ui.patient.others.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class ActivityMessageCenter extends ActivityBasic {
    private ListView lv;
    private String[] array_time = new String[]{"2015-3-21", "2015-3-22", "2015-3-23"};
    private String[] array_reason = new String[]{"视频预约被邹护士拒绝", "视频预约被邹护士拒绝", "视频预约被邹护士拒绝"};
    private SimpleAdapter simpleAdapter;
    private TextView titleText, read_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagecenter);
        initial();
        load();

    }

    private void initial() {
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("消息中心");
        read_tv = (TextView) findViewById(R.id.right_tv);
        lv = (ListView) findViewById(R.id.lv_messageCenter);
    }

    private void load() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array_time.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("time", array_time[i]);
            listItem.put("reason", array_reason[i]);
            listItems.add(listItem);
        }
        simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.listitem_messagecenter, new String[]{"time", "reason"},
                new int[]{R.id.time_messageCenter, R.id.reason_messageCenter});
        lv.setAdapter(simpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Map<String, Object> map = (Map<String, Object>) ActivityMessageCenter.this.simpleAdapter.getItem(position);
                Intent intent = new Intent(ActivityMessageCenter.this, ActivityMessageCenterInfo.class);
                startActivity(intent);

            }

        });
    }
}
