package com.cn.fit.ui.patient.main.mynurse.asynctask;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.alarm.BeanDeletRemind;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynDeletRemind extends AsyncTask<Integer, Integer, BeanDeletRemind> {

    String result = "";
    private Activity act;
    private static String requestAddInfo = "/family/remind/delete";
    private long userId;
    private String pText;
    private long expertID;
    private BeanDeletRemind beanDeletRemind;

    public AscynDeletRemind(Activity act, long expertID) {
        this.act = act;
        this.expertID = expertID;
        beanDeletRemind = new BeanDeletRemind();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanDeletRemind doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientID", userId + "");
        param.put("remindID", expertID + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beanDeletRemind = JsonArrayToList(result);
        } catch (Exception e) {
//			 act.hideProgressBar();
            e.printStackTrace();
        }
        return beanDeletRemind;
    }

    @Override
    protected void onPostExecute(BeanDeletRemind result) {
//		((ActivityBasic) act).hideProgressBar();
        // updateUI();
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanDeletRemind JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanDeletRemind beanDeletRemind = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanDeletRemind = gson.fromJson(jsonString,
                        new TypeToken<BeanDeletRemind>() {
                        }.getType());
            }
        }
        return beanDeletRemind;
    }

}

