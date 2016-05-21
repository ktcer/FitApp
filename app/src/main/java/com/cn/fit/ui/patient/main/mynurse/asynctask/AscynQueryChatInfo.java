package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanQueryChat;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AscynQueryChatInfo extends AsyncTask<Integer, Integer, List<BeanQueryChat>> {

    String result = "";
    private Activity act;
    private static String requestAddInfo = "/chat/getreply";
    private long userId;
    private long expertID;
    private List<BeanQueryChat> beenQueryChat;
    private int pageNum;

    public AscynQueryChatInfo(Activity act, long expertID, int pageNum) {
        this.act = act;
        this.expertID = expertID;
        this.pageNum = pageNum;
        beenQueryChat = new ArrayList<BeanQueryChat>();
//		 AppMain.memberList = new ArrayList<BeanFamilyMemberInfo>();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected List<BeanQueryChat> doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientID", userId + "");
        param.put("expertID", expertID + "");
        param.put("pageSize", "10");
        param.put("pageNum", pageNum + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenQueryChat.clear();
            beenQueryChat = JsonArrayToList(result);
        } catch (Exception e) {
//			((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        return beenQueryChat;
    }

    @Override
    protected void onPostExecute(List<BeanQueryChat> result) {
//		((ActivityBasic) act).hideProgressBar();
        // updateUI();
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private List<BeanQueryChat> JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        List<BeanQueryChat> beenQueryChat = null;
        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenQueryChat = gson.fromJson(jsonString,
                        new TypeToken<List<BeanQueryChat>>() {
                        }.getType());
            }
        }
        return beenQueryChat;
    }

}
