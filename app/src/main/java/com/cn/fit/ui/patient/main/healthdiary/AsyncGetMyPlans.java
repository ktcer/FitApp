package com.cn.fit.ui.patient.main.healthdiary;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanMyPlan;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

/**
 * 获取我的所有方案
 */
public class AsyncGetMyPlans extends
        AsyncTask<Integer, Integer, List<BeanMyPlan>> {
    private String result = "";
    private Activity act;
    private static String myurl = "/interv/getExpertMessage";
    private long userId;
    private List<BeanMyPlan> beanMyPlans;

    public AsyncGetMyPlans(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected List<BeanMyPlan> doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientId", userId + "");
        try {
            String url = AbsParam.getBaseUrl() + myurl;
            result = NetTool.sendPostRequest(url, param, "utf-8");
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        try {
            Gson gson = new Gson();
            beanMyPlans = gson.fromJson(result, new TypeToken<List<BeanMyPlan>>() {
            }.getType());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return beanMyPlans;
    }

    @Override
    protected void onPostExecute(List<BeanMyPlan> beanMyPlans) {
        // ((ActivityBasic)act).hideProgressBar();

    }

}
