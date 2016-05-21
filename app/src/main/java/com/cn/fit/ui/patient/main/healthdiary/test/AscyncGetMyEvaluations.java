package com.cn.fit.ui.patient.main.healthdiary.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiarytest.BeanWenjuan;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncGetMyEvaluations extends AsyncTask<Integer, Integer, BeanWenjuan> {

    String result;
    private Activity act;
    private static String questions = "/webapp/questionnaire/getquestionnaire2";
    private long userId;

    public AscyncGetMyEvaluations(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanWenjuan doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientID", userId + "");
        param.put("wenjuanID", "3");
        BeanWenjuan mWEBeanWenjuan = null;
        try {
            String url = AbsParam.getBaseUrl() + questions;
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            mWEBeanWenjuan = JsonArrayToList(result);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        return mWEBeanWenjuan;
    }

    @Override
    protected void onPostExecute(BeanWenjuan result) {
        ((ActivityBasic) act).hideProgressBar();

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanWenjuan JsonArrayToList(String jsonString)
            throws Exception {
        Gson gson = new Gson();
        BeanWenjuan listBeanQuestions = gson.fromJson(jsonString,
                new TypeToken<BeanWenjuan>() {
                }.getType());
        return listBeanQuestions;

    }

}
