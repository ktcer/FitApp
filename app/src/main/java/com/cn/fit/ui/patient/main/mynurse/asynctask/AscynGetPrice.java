package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanGetPrice;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynGetPrice extends AsyncTask<Integer, Integer, BeanGetPrice> {

    String result = "";
    private ActivityBasic act;
    private static String requestAddInfo = "/travel/getprice";
    private BeanGetPrice beanGetPrice;
    private long travelID;

    public AscynGetPrice(ActivityBasic act, long travelID) {
        this.act = act;
        this.travelID = travelID;

        beanGetPrice = new BeanGetPrice();
    }

    @Override
    protected BeanGetPrice doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("travelID", travelID + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beanGetPrice = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();
            act.hideProgressBar();
        }
        return beanGetPrice;
    }

    @Override
    protected void onPostExecute(BeanGetPrice result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanGetPrice JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanGetPrice beanGetPrice = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanGetPrice = gson.fromJson(jsonString,
                        new TypeToken<BeanGetPrice>() {
                        }.getType());
            }
        }
        return beanGetPrice;
    }

}
