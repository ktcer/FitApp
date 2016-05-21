package com.cn.fit.ui.patient.main.mynurse;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.consult.BeanSelfStatementResult;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.others.myorders.ActivityMyOrdersList;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 新增病情自述
 *
 * @author kuangtiecheng
 */
public class AddMyStatement extends ActivityBasic {
    private TextView submit_btn;
    private String msg;
    private String reserveID;
    private EditText tv_selfStatement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myselfstatement);
        initial();
        reserveID = getIntent().getExtras().getString(Constant.RESERVE_ID);
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("新增病情自述");
        submit_btn = (TextView) this.findViewById(R.id.right_tv);
        submit_btn.setOnClickListener(this);
        submit_btn.setText("提交");
        submit_btn.setVisibility(View.VISIBLE);
        tv_selfStatement = (EditText) findViewById(R.id.editText_myselfstatement);
        tv_selfStatement.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                new Thread(runnableBeanConsult).start();
//			Toast.makeText(AddMyStatement.this, "已提交", 1).show();
//			Intent intent=new Intent(myselfStatement.this,myAppoint.class);
//			startActivity(intent);
//			finish();
//			String old=MyCarePage.class.getName();
//			backTo(old);
//			startActivity(ActivityMyOrdersList.class);
                break;

            default:
                break;
        }
    }

    //请求护士排班信息
    private Map<String, String> map;
    private String retStr;//获取到返回的json数据
    private BeanSelfStatementResult beanSelfStatementResult;
    //网络请求
    Runnable runnableBeanConsult = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/yyzx/app/adddiseasedetail";

        @Override
        public void run() {
            try {
                String adb;
                adb = tv_selfStatement.getText().toString();
                UtilsSharedData.initDataShare(AddMyStatement.this);
                long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
                map = new HashMap<String, String>();
                map.put("patientID", userId + "");// 表示用户id
                map.put("reserveID", reserveID);// 表示用户id
                map.put("diseaseDetail", adb);// 表示用户id
                retStr = NetTool.sendPostRequest(url, map, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getVisitTime(retStr);

                Message msg = new Message();
                if (retStr == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                consultInfoHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Handler consultInfoHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    //展示获取的数据bean
//					hideProgressBar();
//					Toast.makeText(getApplicationContext(), beanSelfStatementResult.getDetail(),Toast.LENGTH_SHORT).show();
                    String old = TabActivityMain.class.getName();
                    backTo(old);
                    startActivity(ActivityMyOrdersList.class);
                    ToastUtil.showMessage("已提交");
                    break;

                default:

                    break;
            }
        }
    };

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getVisitTime(String jsonString) throws Exception {
        Gson gson = new Gson();
        beanSelfStatementResult = gson.fromJson(jsonString, BeanSelfStatementResult.class);
    }


}
