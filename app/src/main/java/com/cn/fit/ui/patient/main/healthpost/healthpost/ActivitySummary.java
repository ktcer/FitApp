package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthpost.BeanItemOfSummary;
import com.cn.fit.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.List;

public class ActivitySummary extends ActivityBasic {
    private TextView tv;
    private ListView lv;
    private List<BeanItemOfSummary> list = new ArrayList<BeanItemOfSummary>();
    ;
    private AdapterOfSummary adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysummary);
        lv = (ListView) findViewById(R.id.lv_ActivitySummary);
        tv = (TextView) findViewById(R.id.middle_tv);
        tv.setText("活动概要");
        SetData();

    }

    private void SetData() {
        BeanItemOfSummary AIOS0 = new BeanItemOfSummary("始发地", "北京");
        BeanItemOfSummary AIOS1 = new BeanItemOfSummary("旅游类型", "草原");
        BeanItemOfSummary AIOS2 = new BeanItemOfSummary("适用户外项目", "普通旅游");
        BeanItemOfSummary AIOS3 = new BeanItemOfSummary("目的地", "坝上");
        BeanItemOfSummary AIOS4 = new BeanItemOfSummary("日程", "3日游");
        BeanItemOfSummary AIOS5 = new BeanItemOfSummary("出行方式", "包车");
        BeanItemOfSummary AIOS6 = new BeanItemOfSummary("服务监督", "18514590850");
        BeanItemOfSummary AIOS7 = new BeanItemOfSummary("活动强度", "休闲");
        BeanItemOfSummary AIOS8 = new BeanItemOfSummary("联系方式", "18220420234");
        list.add(AIOS0);
        list.add(AIOS1);
        list.add(AIOS2);
        list.add(AIOS3);
        list.add(AIOS4);
        list.add(AIOS5);
        list.add(AIOS6);
        list.add(AIOS7);
        list.add(AIOS8);
        adapter = new AdapterOfSummary(this, list);
        lv.setAdapter(adapter);
    }


    public class AdapterOfSummary extends BaseAdapter {
        private Context mcontext;
        private List<BeanItemOfSummary> plist;

        public AdapterOfSummary(Context mcontext,
                                List<BeanItemOfSummary> plist) {
            this.mcontext = mcontext;
            this.plist = plist;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return plist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return plist.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_summary, null);
            if (convertView != null) {
                holder = new ViewHolder();
                holder.items = (TextView) convertView.findViewById(R.id.itemOfSummary);
                holder.context = (TextView) convertView.findViewById(R.id.contentOfSummary);
            }

            holder.items.setText(plist.get(position).getItems());
            holder.context.setText(plist.get(position).getContent());
            return convertView;
        }

    }

    class ViewHolder {
        TextView items;
        TextView context;
    }

}
