package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanPayDone;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncCoachPayDone extends AsyncTask<Integer, Integer, BeanPayDone> {

    String result;
    private ActivityBasic act;
    private static String requestAddInfo = "/ad/app/paydone";
    private BeanPayDone beenPayDone;
    private String ddh;

    public AscyncCoachPayDone() {

    }

    public AscyncCoachPayDone(ActivityBasic act, String ddh) {
        super();
        this.act = act;
        this.ddh = ddh;
        beenPayDone = new BeanPayDone();
    }

    @Override
    protected BeanPayDone doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("ddh", ddh);
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenPayDone = JsonArrayToList(result);
        } catch (Exception e) {
            act.hideProgressBar();
            e.printStackTrace();
        }
        return beenPayDone;
    }

    @Override
    protected void onPostExecute(BeanPayDone result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanPayDone JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanPayDone beenPayDone = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenPayDone = gson.fromJson(jsonString,
                        new TypeToken<BeanPayDone>() {
                        }.getType());
            }
        }
        return beenPayDone;
    }

}
