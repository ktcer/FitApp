package com.cn.fit.ui.patient.others.myaccount;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.personinfo.BeanPersonInfo;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 获取用户个人信息
 *
 * @author kuangtiecheng
 */
public class AsyGetUserInfo extends AsyncTask<Integer, Integer, BeanPersonInfo> {

    String resultUrl = "";
    private Activity act;
    private static String tempUrl = "/base/app/getuserinfo";
    private long userId;

    public AsyGetUserInfo(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanPersonInfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        BeanPersonInfo beanPersonInfo = new BeanPersonInfo();
        param.put("userId", userId + "");
        param.put("userType", 0 + "");
        try {
            String url = AbsParam.getBaseUrl() + tempUrl;
            resultUrl = NetTool.sendPostRequest(url, param, "utf-8");
            beanPersonInfo = JsonArrayToList(resultUrl);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }

        return beanPersonInfo;
    }

    @Override
    protected void onPostExecute(BeanPersonInfo beanPersonInfo) {
        ((ActivityBasic) act).hideProgressBar();
        JSONObject loginResult;
        String myPhotos = "";
        try {
            loginResult = new JSONObject(resultUrl);
            myPhotos = loginResult.getString("picurl");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		UtilsSharedData.saveKeyMustValue(Constant.USER_NAME, beanPersonInfo.getUserName());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_ID,beanPersonInfo.getUserID());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_HEIGHT, beanPersonInfo.getHeight());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_WEIGHT, beanPersonInfo.getWeight());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_GENDER, beanPersonInfo.getSex());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_BIRTHDAY, beanPersonInfo.getBirth());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_GKMUM, beanPersonInfo.getGkNumber());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_IMAGEURL, beanPersonInfo.getImgUrl());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_NUBE, beanPersonInfo.getVideoNumber());
    }

    private BeanPersonInfo JsonArrayToList(String jsonString)
            throws Exception {
        Gson gson = new Gson();
        BeanPersonInfo beenInfo = new BeanPersonInfo();
        ;
        int len = jsonString.length();
        if (jsonString != "") {
            if (!(jsonString.equals(-1))) {
                beenInfo = gson.fromJson(jsonString,
                        new TypeToken<BeanPersonInfo>() {
                        }.getType());
            }
        }
        return beenInfo;
    }


}
