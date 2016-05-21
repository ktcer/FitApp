package com.cn.fit.ui.patient.others.myaccount;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 根据用户获取当前金币支付单价
 *
 * @author kuangtiecheng
 */
public class AsyncGetUnitPrice extends AsyncTask<Integer, Integer, Float> {

    String result = "";
    private Activity act;
    private static String mycoins = "/goldcoins/unitprice";
    private long userId;
    private float coinUnitprice;

    public AsyncGetUnitPrice(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected Float doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userID", userId + "");
        param.put("userType", 0 + "");
        try {
            String url = AbsParam.getBaseUrl() + mycoins;
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(result);
            coinUnitprice = Float.parseFloat(json
                    .getString("unitPrice"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        return coinUnitprice;
    }

    @Override
    protected void onPostExecute(Float result) {
        ((ActivityBasic) act).hideProgressBar();
    }


}
