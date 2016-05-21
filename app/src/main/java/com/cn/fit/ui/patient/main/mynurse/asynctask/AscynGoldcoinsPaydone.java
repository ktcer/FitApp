package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanGoldPayDone;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynGoldcoinsPaydone extends AsyncTask<Integer, Integer, BeanGoldPayDone> {

    String result = "";
    private ActivityBasic act;
    private static String requestAddInfo = "/goldcoins/paydone";
    private BeanGoldPayDone beenPayDone;
    private long userId;


    public AscynGoldcoinsPaydone() {

    }

    public AscynGoldcoinsPaydone(ActivityBasic act) {
        super();
        this.act = act;
        beenPayDone = new BeanGoldPayDone();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanGoldPayDone doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("ddh", AppMain.orderNumber);
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
    protected void onPostExecute(BeanGoldPayDone result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanGoldPayDone JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanGoldPayDone beenPayDone = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenPayDone = gson.fromJson(jsonString,
                        new TypeToken<BeanGoldPayDone>() {
                        }.getType());
            }
        }
        return beenPayDone;
    }

}
