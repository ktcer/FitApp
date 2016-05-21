package com.cn.fit.ui.patient.mycare;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的干预方案详情（目前暂时不显示）
 *
 * @author kuangtiecheng
 */
public class MyPlanInfo extends ActivityBasic {
    private ListView listView;
    private String[] array_advice = new String[]{"1、不要独自静坐", "2、跌倒时要静坐"};
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplaninfo);
        initial();
        load();
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("干预方案详情");
        listView = (ListView) this.findViewById(R.id.lv_myPlanInfo);
    }

    private void load() {
        List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array_advice.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("advice", array_advice[i]);
            Items.add(item);
        }
        adapter = new SimpleAdapter(MyPlanInfo.this, Items, R.layout.listitem_myplaninfo, new String[]{"advice"}, new int[]{R.id.tv_myPlanInfo});
        listView.setAdapter(adapter);
    }

}

