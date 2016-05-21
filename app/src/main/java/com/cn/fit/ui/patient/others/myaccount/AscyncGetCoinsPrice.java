package com.cn.fit.ui.patient.others.myaccount;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.personinfo.BeanCoinsPrice;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

public class AscyncGetCoinsPrice extends AsyncTask<Integer, Integer, List<BeanCoinsPrice>> {

    String result;
    private Activity act;
    private static String family = "/goldcoins/price";
    private long userId;

    public AscyncGetCoinsPrice(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected List<BeanCoinsPrice> doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", userId + "");
        List<BeanCoinsPrice> listBean = null;
        try {
            String url = AbsParam.getBaseUrl() + family;
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            listBean = JsonArrayToList(result);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        return listBean;
    }

    @Override
    protected void onPostExecute(List<BeanCoinsPrice> result) {
        ((ActivityBasic) act).hideProgressBar();
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private List<BeanCoinsPrice> JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        List<BeanCoinsPrice> listBean = gson.fromJson(jsonString, new TypeToken<List<BeanCoinsPrice>>() {
        }.getType());
        return listBean;
    }

}
