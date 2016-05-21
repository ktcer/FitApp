package com.cn.fit.ui.patient.main.healthpost.doctorinterview;

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
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

public class ActivityDoctorInterview extends ActivityBasic
//implements 
//OnHeaderRefreshListener,
//OnFooterRefreshListener 
{
    private ListView listview;
    private String[] array = new String[]{"专家指导", "养生保健", "运动健身", "合理膳食", "心理讲坛", "护理常识"};
    private int[] picture = new int[]{R.drawable.doctor, R.drawable.taiji, R.drawable.running,
            R.drawable.eating, R.drawable.mental, R.drawable.care};
    private SimpleAdapter adapter;
    private String ActivityTag = "";

    //	private PullToRefreshView mPullToRefreshView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorinterview);
        initial();
        load();
    }

    private void initial() {
//        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
//		mPullToRefreshView.setOnHeaderRefreshListener(this);
//		mPullToRefreshView.setOnFooterRefreshListener(this);
        listview = (ListView) findViewById(R.id.lv_doctorinterview);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("专家访谈");
    }

    private void load() {
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", array[i]);
            item.put("picture", picture[i]);
            listItem.add(item);
        }
        adapter = new SimpleAdapter(this, listItem,
                R.layout.list_item, new String[]{"name", "picture"},
                new int[]{R.id.list_item_title, R.id.list_item_image});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 0) {
                    ActivityTag = "ActivityVideoList";
                    wehtherLogin(ActivityTag, ActivityVideoList.class);
                } else {
                    Toast.makeText(getApplicationContext(), "暂无此分类视频", 1000).show();
                }
//				startActivity(ActivityVideoList.class);
            }

        });
    }

    private void wehtherLogin(String ActivityTag, Class<?> cls) {
        UtilsSharedData sharedData = new UtilsSharedData();
        if (sharedData.isEmpty(Constant.LOGIN_STATUS) || sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("0")) {
            Intent intent = new Intent(ActivityDoctorInterview.this, ActivityLogin.class);
            intent.putExtra("ActivityTag", ActivityTag);
            startActivity(intent);
        } else {
            startActivity(cls);
        }
    }


//	@Override
//	public void onFooterRefresh(PullToRefreshView view) {
//		Toast.makeText(this, "onFooterRefresh", 0).show();
//		mPullToRefreshView.onFooterRefreshComplete();
//	}
//
//	@Override
//	public void onHeaderRefresh(PullToRefreshView view) {
//		Toast.makeText(this, "onFooterRefresh", 0).show();
//		mPullToRefreshView.onHeaderRefreshComplete();
//	}
}
