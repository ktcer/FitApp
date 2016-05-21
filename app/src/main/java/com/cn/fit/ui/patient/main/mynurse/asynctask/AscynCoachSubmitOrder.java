package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanSubmitOrder;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynCoachSubmitOrder extends AsyncTask<Integer, Integer, BeanSubmitOrder> {

    String result = "";
    private ActivityBasic act;
    private static String requestAddInfo = "/ad/app/submitorder";
    private BeanSubmitOrder beanSubmitOrder;
    private long classID;
    private float money;
    private int num;

    public AscynCoachSubmitOrder() {

    }

    public AscynCoachSubmitOrder(ActivityBasic act, long classID, float money, int num) {
        this.act = act;
        this.classID = classID;
        this.money = money;
        this.num = num;
        beanSubmitOrder = new BeanSubmitOrder();
    }


    @Override
    protected BeanSubmitOrder doInBackground(Integer... params) {
        UtilsSharedData.initDataShare(act);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("classID", classID + "");
        param.put("money", money + "");
        param.put("num", num + "");
        param.put("userID", userId + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beanSubmitOrder = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();
            act.hideProgressBar();
        }
        return beanSubmitOrder;
    }

    @Override
    protected void onPostExecute(BeanSubmitOrder result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanSubmitOrder JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanSubmitOrder beanSubmitOrder = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanSubmitOrder = gson.fromJson(jsonString,
                        new TypeToken<BeanSubmitOrder>() {
                        }.getType());
            }
        }
        return beanSubmitOrder;
    }

}
