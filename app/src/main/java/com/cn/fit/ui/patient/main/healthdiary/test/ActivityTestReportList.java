package com.cn.fit.ui.patient.main.healthdiary.test;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluation;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.floatactionbutton.FloatingActionButton;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityTestReportList extends ActivityBasicListView {


    private List<BeanResultOfEvaluation> infoList, tempInfoList;
    private TestReportListAdapter testReportListAdapter;
    private FloatingActionButton addTestBtn;
    protected int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testreportlist);
        initial();

    }

    /**
     * 基本组件初始化
     */
    private void initial() {
        addTestBtn = (FloatingActionButton) findViewById(R.id.fab_addtest);
        addTestBtn.setOnClickListener(this);
        testReportListAdapter = new TestReportListAdapter(this);
        listView = (XListView) this.findViewById(R.id.report_list);
        listView.setTag("listView");
        ((TextView) findViewById(R.id.middle_tv)).setText("评估报告");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        addTestBtn.attachToListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(ActivityTestReportList.this,
                        ActivityTestReportDetail.class);
                intent.putExtra(Constant.TESTREPORTDETAIL, infoList.get(position - 1));
                startActivity(intent);

            }
        });
        infoList = new ArrayList<BeanResultOfEvaluation>();
        tempInfoList = new ArrayList<BeanResultOfEvaluation>();
        listView.setAdapter(testReportListAdapter);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryTestReportList taskQNL = new QueryTestReportList();
        taskQNL.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab_addtest:
                Intent intent = new Intent(this, ActivityEvaluationSecond.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 查询测评结果列表
     *
     * @author kuangtiecheng
     */
    private class QueryTestReportList extends
            AsyncTask<Integer, Integer, String> {

        public QueryTestReportList() {
            super();
        }

        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            UtilsSharedData.initDataShare(ActivityTestReportList.this);
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", userId + "");
            param.put("wenjuanID", "0");

            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/webapp/questionnaire/getquesresult2", param,
                        "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                JsonArrayToList(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                e.printStackTrace();
                hideProgressBar();

            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void JsonArrayToList(String jsonString) throws Exception {
            Gson gson = new Gson();
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    tempInfoList = gson.fromJson(jsonString,
                            new TypeToken<List<BeanResultOfEvaluation>>() {
                            }.getType());

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanResultOfEvaluation tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            testReportListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();

        }
    }

    private class TestReportListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public TestReportListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
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
                        R.layout.listitem_testreport, null);
                holder.reportTime = (TextView) convertView
                        .findViewById(R.id.report_time);
                holder.reportTitle = (TextView) convertView
                        .findViewById(R.id.report_title);
                holder.reportResult = (TextView) convertView
                        .findViewById(R.id.report_result);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.reportTime.setText(infoList.get(position).getDate());// infoList.get(position)
            holder.reportTitle.setText(infoList.get(position).getWenjuanName());// infoList.get(position)
            if (infoList.get(position).getWenjuanName().equals("身体测评")) {
                holder.reportResult.setVisibility(View.GONE);
            } else {
                holder.reportResult.setVisibility(View.VISIBLE);
                holder.reportResult.setText("测评结果：" + infoList.get(position).getDetail());
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView reportTime;
        TextView reportTitle;
        TextView reportResult;

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        // showProgressBar();
        QueryTestReportList taskQNL = new QueryTestReportList();
        taskQNL.execute();
    }

    QueryTestReportList taskQNL;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            if (taskQNL != null && taskQNL.getStatus() == AsyncTask.Status.RUNNING) {
                taskQNL.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            taskQNL = new QueryTestReportList();
            taskQNL.execute();
        }
    }

}
