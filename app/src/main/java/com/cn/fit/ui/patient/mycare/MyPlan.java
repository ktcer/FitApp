package com.cn.fit.ui.patient.mycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的干预方案(目前暂时不显示)
 *
 * @author kuangtiecheng
 */
public class MyPlan extends ActivityBasic {
    private ListView listView;
    private String[] array_time = new String[]{"2015-03-27", "2015-03-27", "2015-03-27", "2015-03-27"};
    private String[] array_what = new String[]{"跌倒干预", "跌倒干预", "跌倒干预", "跌倒干预"};
    private String[] array_person = new String[]{"制定人：李护士长", "制定人：李护士长", "制定人：李护士长", "制定人：李护士长"};
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplan);
        initial();
        load();
    }

    private void initial() {
        listView = (ListView) this.findViewById(R.id.lv_myplan);
    }

    private void load() {
        List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array_time.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("time", array_time[i]);
            item.put("what", array_what[i]);
            item.put("person", array_person[i]);
            Items.add(item);
        }
        adapter = new SimpleAdapter(this, Items,
                R.layout.listitem_myplan, new String[]{"time", "what",
                "person"}, new int[]{R.id.tv1_myplan,
                R.id.tv2_myplan, R.id.tv3_myplan});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Map<String, Object> abc = (Map<String, Object>) MyPlan.this.adapter.getItem(position);
                String what = abc.get("what").toString();
                Intent intent = new Intent(MyPlan.this, MyPlanInfo.class);
                startActivity(intent);
            }

        });
    }
}
