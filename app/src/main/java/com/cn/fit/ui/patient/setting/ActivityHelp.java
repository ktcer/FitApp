package com.cn.fit.ui.patient.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class ActivityHelp extends ActivityBasic {
    private ListView lv;
    private TextView tv;
    private String[] array_question = new String[]{"问：爱·护有什么功能？", "问：怎么添加私人健康秘书？",};
    private String[] array_answer = new String[]{"答：“爱护”是您随身的保健秘书。 具有以下功能特点： 1、让您随时随地享受最专业的护理指导和服务。 2、通过《健康监测》可以实时监测多项生理指标。 3、通过《健康日记》可以把健康指导方案细化到每时每刻。 4、通过《健康秘书》可以和您的健康秘书密切沟通。 5、通过《健康驿站》可以查询到最适合您的健康之旅。", "答：在秘书页面的私人秘书列表中点击“点击添加健康秘书”进行添加"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_setting);
        initial();
        load();
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("帮助中心");
        lv = (ListView) findViewById(R.id.lv_help);

    }

    private void load() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array_question.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("question", array_question[i]);
            listItem.put("answer", array_answer[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.listitem_help, new String[]{"question", "answer"},
                new int[]{R.id.tv1_help, R.id.tv2_help});
        lv.setAdapter(simpleAdapter);
    }
}
