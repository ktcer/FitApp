package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanRecommendInfo;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynRecommendInfo extends AsyncTask<Integer, Integer, BeanRecommendInfo> {

    String result;
    private Activity act;
    private static String requestAddInfo = "/chat/getnews";
    private BeanRecommendInfo beenUserChat;

    public AscynRecommendInfo(Activity act) {
        this.act = act;
        beenUserChat = new BeanRecommendInfo();
    }

    @Override
    protected BeanRecommendInfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", 1 + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenUserChat = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beenUserChat;
    }

    @Override
    protected void onPostExecute(BeanRecommendInfo result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanRecommendInfo JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanRecommendInfo beenUserChatq = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenUserChatq = gson.fromJson(jsonString,
                        new TypeToken<BeanRecommendInfo>() {
                        }.getType());
            }
        }
        return beenUserChatq;
    }

}
