package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanAddMoney;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynAddMoneyPayDone extends AsyncTask<Integer, Integer, BeanAddMoney> {

    String result;
    private ActivityBasic act;
    private static String requestAddInfo = "/mywallet/paydone";
    private BeanAddMoney beanAddMoney;
    private long userId;

    public AscynAddMoneyPayDone() {

    }

    public AscynAddMoneyPayDone(ActivityBasic act) {
        super();
        this.act = act;
        beanAddMoney = new BeanAddMoney();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanAddMoney doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("ddh", AppMain.orderNumberwWllet);
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beanAddMoney = JsonArrayToList(result);
        } catch (Exception e) {
            act.hideProgressBar();
            e.printStackTrace();
        }
        return beanAddMoney;
    }

    @Override
    protected void onPostExecute(BeanAddMoney result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanAddMoney JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanAddMoney beanAddMoney = new BeanAddMoney();

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanAddMoney = gson.fromJson(jsonString,
                        new TypeToken<BeanAddMoney>() {
                        }.getType());
            }
        }
        return beanAddMoney;
    }

}
