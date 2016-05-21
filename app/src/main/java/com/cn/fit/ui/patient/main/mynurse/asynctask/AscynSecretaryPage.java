package com.cn.fit.ui.patient.main.mynurse.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanHealthSecretaryInfo;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscynSecretaryPage extends AsyncTask<Integer, Integer, BeanHealthSecretaryInfo> {

    String result;
    private ActivityBasic act;
    private static String requestAddInfo = "/yyzx/app/queryreservablenurseinfo";
    private long userId;
    private long expertID;
    private BeanHealthSecretaryInfo beenSecretaryInfo;

    public AscynSecretaryPage(ActivityBasic act, long expertID) {
        this.act = act;
        this.expertID = expertID;
        beenSecretaryInfo = new BeanHealthSecretaryInfo();
        act.showProgressBar();
        // AppMain.memberList = new ArrayList<BeanFamilyMemberInfo>();
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanHealthSecretaryInfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("patientID", userId + "");//如果patientID传的是userId,则查询的是绑定的保健秘书的信息；
//		param.put("patientID","-1");
        param.put("nurseID", expertID + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenSecretaryInfo = JsonArrayToList(result);
        } catch (Exception e) {
            act.hideProgressBar();
            e.printStackTrace();
        }
        return beenSecretaryInfo;
    }

    @Override
    protected void onPostExecute(BeanHealthSecretaryInfo result) {
//		 act.hideProgressBar();
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanHealthSecretaryInfo JsonArrayToList(String jsonString)
            throws Exception {
        Gson gson = new Gson();
        BeanHealthSecretaryInfo beenInfo = new BeanHealthSecretaryInfo();
        ;
        int len = jsonString.length();
        if (jsonString != "") {
            if (!(jsonString.equals(-1))) {
                beenInfo = gson.fromJson(jsonString,
                        new TypeToken<BeanHealthSecretaryInfo>() {
                        }.getType());
            }
        }
        return beenInfo;
    }

}
