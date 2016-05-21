package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanUserChat;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 向爱护后台补充聊天消息
 *
 * @author kuangtiecheng
 */
public class AscynUserChat extends AsyncTask<Integer, Integer, BeanUserChat> {

    String result = "";
    private static String requestAddInfo = "/chat/livewords";
    private String userId;
    private String pText;
    private String expertID;
    private BeanUserChat beenUserChat;

    public AscynUserChat(String userId, String pText, String expertID) {
        this.userId = userId;
        this.pText = pText;
        this.expertID = expertID;
    }

    @Override
    protected BeanUserChat doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientID", userId);
        param.put("expertID", expertID);
        param.put("pText", pText);
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
    protected void onPostExecute(BeanUserChat result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanUserChat JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanUserChat beenUserChatq = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenUserChatq = gson.fromJson(jsonString,
                        new TypeToken<BeanUserChat>() {
                        }.getType());
            }
        }
        return beenUserChatq;
    }

}
