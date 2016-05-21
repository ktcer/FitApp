package com.cn.fit.ui.patient.main.healthdiary;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanNodeComplete;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class AscynNodeComplete extends
        AsyncTask<Integer, Integer, BeanNodeComplete> {

    String result = "";
    private Activity act;
    private static String myurl = "/interv/node/complete";
    private long userId;
    private long patientPlanId;
    private long nodeId;
    private BeanNodeComplete beanNodeComplete;

    public AscynNodeComplete(Activity act, long patientPlanId, long nodeId) {
        this.act = act;
        this.patientPlanId = patientPlanId;
        this.nodeId = nodeId;
        beanNodeComplete = new BeanNodeComplete();
    }

    @Override
    protected BeanNodeComplete doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientPlanId", patientPlanId + "");
        param.put("nodeId", nodeId + "");
        try {
            String url = AbsParam.getBaseUrl() + myurl;
            System.out.println("=-=-= AscynNodeComplete url： " + url);
            result = NetTool.sendPostRequest(url, param, "utf-8");
            System.out.println("=-=-= AscynNodeComplete result： " + result);
            Log.i("result", result);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        try {
            JSONObject ob = new JSONObject(result);
            Gson gson = new Gson();
            beanNodeComplete = gson.fromJson(ob.getString("data"), BeanNodeComplete.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return beanNodeComplete;
    }

    @Override
    protected void onPostExecute(BeanNodeComplete beanNodeComplete) {
        // ((ActivityBasic)act).hideProgressBar();

    }

}
