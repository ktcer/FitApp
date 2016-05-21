package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanPayMyWalletMoney;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncPaymoney extends AsyncTask<Integer, Integer, BeanPayMyWalletMoney> {

    String result = "";
    private ActivityBasic act;
    private static String requestAddInfo = "/mywallet/recharge";
    private BeanPayMyWalletMoney beanPayMyWalletMoney;
    private String money;
    private long userId;
    private String inAccount;


    public AscyncPaymoney() {

    }

    public AscyncPaymoney(ActivityBasic act, String money) {
        super();
        this.act = act;
        this.money = money;
        beanPayMyWalletMoney = new BeanPayMyWalletMoney();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        inAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
    }

    @Override
    protected BeanPayMyWalletMoney doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userID", userId + "");
        param.put("userType", 0 + "");
        param.put("money", money);
        param.put("inAccount", inAccount);
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beanPayMyWalletMoney = JsonArrayToList(result);
        } catch (Exception e) {
            act.hideProgressBar();
            e.printStackTrace();
        }
        return beanPayMyWalletMoney;
    }

    @Override
    protected void onPostExecute(BeanPayMyWalletMoney result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanPayMyWalletMoney JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanPayMyWalletMoney beanPayMyWalletMoney = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanPayMyWalletMoney = gson.fromJson(jsonString,
                        new TypeToken<BeanPayMyWalletMoney>() {
                        }.getType());
            }
        }
        return beanPayMyWalletMoney;
    }

}
