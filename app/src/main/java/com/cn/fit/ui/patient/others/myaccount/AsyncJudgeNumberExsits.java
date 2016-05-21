package com.cn.fit.ui.patient.others.myaccount;

import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.util.AbsParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 判断账号是否已经存在
 *
 * @author kuangtiecheng
 */
public class AsyncJudgeNumberExsits extends AsyncTask<Integer, Integer, String> {

    String result = "";
    private String phoneNumber;
    private static String lastUrl = "/base/app/ifrepet";

    public AsyncJudgeNumberExsits(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("phone", phoneNumber);
        param.put("userType", 0 + "");
        try {
            String url = AbsParam.getBaseUrl() + lastUrl;
            result = NetTool.sendPostRequest(url, param, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(result);
//			ActivityMyAccountCenter.money = Float.parseFloat(json
//					.getString("money"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

    }


}
