package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanPayGold;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncPayGold extends AsyncTask<Integer, Integer, BeanPayGold> {

    String result = "";
    private ActivityBasic act;
    private static String requestAddInfo = "/goldcoins/apprecharge";
    private BeanPayGold beenPayDone;
    private String moneyNumber;
    private long userId;


    public AscyncPayGold() {

    }

    public AscyncPayGold(ActivityBasic act, String money) {
        super();
        this.act = act;
        this.moneyNumber = money;
        beenPayDone = new BeanPayGold();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanPayGold doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userID", userId + "");
        param.put("userType", 0 + "");
        param.put("coins", moneyNumber);
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
    protected void onPostExecute(BeanPayGold result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanPayGold JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanPayGold beenPayDone = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenPayDone = gson.fromJson(jsonString,
                        new TypeToken<BeanPayGold>() {
                        }.getType());
            }
        }
        return beenPayDone;
    }

}
