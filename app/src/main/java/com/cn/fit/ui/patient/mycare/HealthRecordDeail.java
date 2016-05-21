package com.cn.fit.ui.patient.mycare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthrecord.BeanHealthDetailBeen;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthRecordDeail extends ActivityBasic {
    private TextView detailHealthKey;
    private TextView detailHealthValue;
    private String healthKey;
    private String healthValue;
    private String healthStateID, timeID, healthStateItemName;
    private List<BeanHealthDetailBeen> healthInfoList;
    private TextView rightTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_health_info);
        initView();

        showProgressBar();
        QueryHealthTask queryHealthTask = new QueryHealthTask();
        queryHealthTask.execute();
    }

    private void initView() {
        timeID = getIntent().getStringExtra("timeID");
        healthStateID = getIntent().getStringExtra("healthStateID");
        healthStateItemName = getIntent().getStringExtra("healthStateItemName");
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("详情");

        rightTV = (TextView) findViewById(R.id.right_tv);
        rightTV.setVisibility(View.VISIBLE);
        rightTV.setOnClickListener(this);
        rightTV.setText("增加");

        healthInfoList = new ArrayList<BeanHealthDetailBeen>();
        detailHealthKey = (TextView) findViewById(R.id.detail_health_info_key);
        detailHealthValue = (TextView) findViewById(R.id.detail_health_info_value);
    }

    private class QueryHealthTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            param.put("healthStateID", healthStateID);
            param.put("timeID", timeID);
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/app/healthrecord/" + "healthstatedetaildetail",
                        param, "utf-8");
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
            hideProgressBar();
            healthKey = "";
            for (int i = 0; i < healthInfoList.size(); i++) {
                healthKey += healthInfoList.get(i).getKey() + ":"
                        + healthInfoList.get(i).getValue() + "\n\n";
            }
            detailHealthKey.setText(healthKey);

        }
    }

    private void JsonArrayToList(String result2) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result2);
            for (int i = 0; i < jsonArray.length(); i++) {
                BeanHealthDetailBeen healthDetailBeen = new BeanHealthDetailBeen();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                healthDetailBeen.setKey(jsonObject.getString("key"));
                healthDetailBeen.setValue(jsonObject.getString("value"));
                healthInfoList.add(healthDetailBeen);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
//			Toast.makeText(HealthRecordDeail.this, "哈哈这是新增加的，疾病当然越少越好",
//					Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(HealthRecordDeail.this, AddHealthDetail.class);
//			intent.putExtra("timeID", healthList.get(arg2 - 1).getTimeID());
                intent.putExtra("healthStateID", healthStateID);
//			intent.putExtra("healthStateItemName", healthStateItemName);
                startActivity(intent);

                break;

            default:
                break;
        }

    }

}
