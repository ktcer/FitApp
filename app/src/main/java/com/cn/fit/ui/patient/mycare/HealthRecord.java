package com.cn.fit.ui.patient.mycare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthrecord.BeanHealthdetailThreeBeen;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.refreshlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthRecord extends ActivityBasicListView {
    private static String queryNurse = "healthstatedetaillist";
    private List<BeanHealthdetailThreeBeen> healthList, tempHealthList;
    private HealthListAdapter healthListAdapter;
    private int subDepartmentID;
    private int diseaseID;
    private String diseaseName;
    private String healthStateItemName, healthStateID;
    protected int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_record);
        initView();
    }

    private void initView() {
        healthStateItemName = getIntent().getStringExtra("healthStateItemName");
        healthStateID = getIntent().getStringExtra("healthStateID");
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText(healthStateItemName + "详情");

        Log.i("subdepartmentid", subDepartmentID + "");
        Log.i("diseaseid", diseaseID + "");
        healthListAdapter = new HealthListAdapter(this);
        healthList = new ArrayList<BeanHealthdetailThreeBeen>();
        tempHealthList = new ArrayList<BeanHealthdetailThreeBeen>();
        listView = (XListView) findViewById(R.id.lv_health_record);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.setClass(HealthRecord.this, HealthRecordDeail.class);
                intent.putExtra("timeID", healthList.get(arg2 - 1).getTimeID());
                intent.putExtra("healthStateID", healthStateID);
                intent.putExtra("healthStateItemName", healthStateItemName);
                startActivity(intent);
            }
        });
        listView.setAdapter(healthListAdapter);
    }

    private class HealthListAdapter extends BaseAdapter {
        private Context context;

        public HealthListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return healthList.size();
        }

        @Override
        public Object getItem(int position) {
            return healthList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.listitem_intervene_histroy, null);
                holder.healthTime = (TextView) convertView
                        .findViewById(R.id.list_item_intervene_time);
                holder.content = (TextView) convertView
                        .findViewById(R.id.list_item_intervene_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.healthTime.setText(healthList.get(position).getTime());
            holder.content.setText(healthList.get(position).getContent());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView healthTime;
        TextView content;
    }

    private class QueryNurseTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            param.put("healthStateID", healthStateID);
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String a = AbsParam.getBaseUrl() + "/yyzx/app/" + queryNurse;
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/app/healthrecord/" + queryNurse, param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                healthList.clear();
            }

            for (BeanHealthdetailThreeBeen tmp : tempHealthList) {
                healthList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            healthListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }

    private void JsonArrayToList(String result) {
        JSONArray jsonArray;
        try {
            tempHealthList.clear();
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BeanHealthdetailThreeBeen nurseInfo = new BeanHealthdetailThreeBeen();
                nurseInfo.setContent(jsonObject.getString("content"));
                nurseInfo.setTime(jsonObject.getString("time"));
                nurseInfo.setTimeID(jsonObject.getString("timeID"));
                tempHealthList.add(nurseInfo);
            }
            if (tempHealthList.size() < 10) {
                canLoadMore = false;
            } else {
                canLoadMore = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        QueryNurseTask nurseTask = new QueryNurseTask();
        nurseTask.execute();
    }

    private QueryNurseTask nurseTask;

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        if (canLoadMore) {
            if (nurseTask != null && nurseTask.getStatus() == AsyncTask.Status.RUNNING) {
                nurseTask.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            nurseTask = new QueryNurseTask();
            nurseTask.execute();
        }

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        showProgressBar();
        QueryNurseTask nurseTask = new QueryNurseTask();
        nurseTask.execute();
    }
}
