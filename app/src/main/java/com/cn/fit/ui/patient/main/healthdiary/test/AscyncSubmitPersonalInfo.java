package com.cn.fit.ui.patient.main.healthdiary.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.others.myaccount.ActivityPersonalInfo;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 提交个人信息
 *
 * @author kuangtiecheng
 */

public class AscyncSubmitPersonalInfo extends
        AsyncTask<Integer, Integer, String> {

    private Activity act;
    private String retString;
    /** 登录结果，0表示失败，1表示成功 */
    /**
     * 登录详情
     */
    public String resultDetail = "";
    public int resultStatus;
    private String name, gender, birthday, height, weight, userId;
    private boolean isfix;//true是ActivityTestPersonInfo填写个人信息

    public AscyncSubmitPersonalInfo(Activity act, String userId, String name,
                                    String gender, String birthday, String height, String weight, boolean isfix) {
        this.act = act;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.isfix = isfix;
//		 UtilsSharedData.initDataShare(act);// ////////
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("id", userId);
        param.put("name", name);
        if (gender != null) {
            param.put("sex", gender);//gender.equals("男") ? "00070002" : "00070003")
        }
        if (birthday != null) {
            if (!birthday.equals("-")) {
                //防止有些机型出现的日期显示问题
                birthday.replace("年", "-");
                birthday.replace("月", "-");
                birthday.replace("日", "-");
                param.put("birth", birthday);
            }
        }
        param.put("height", height);
        param.put("weight", weight);
        try {
            String url = AbsParam.getBaseUrl() + "/base/app/edituserinfo";
            retString = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", retString + ">>>>>>>>>>" + param);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
        }
        return retString;
    }

    @Override
    protected void onPostExecute(String result) {
        JSONObject RegisterResult;
        try {
            if (result != null) {
                RegisterResult = new JSONObject(result);
                resultStatus = RegisterResult.getInt("result");
//				resultDetail = RegisterResult.getString("detail");
                if (resultStatus == 1) {
                    UtilsSharedData.saveKeyMustValue("iffixinfoState", true);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (isfix) {
            UtilsSharedData.saveKey2Value(Constant.USER_NAME, name);
            UtilsSharedData.saveKey2Value(Constant.USER_GENDER, gender);
            UtilsSharedData.saveKey2Value(Constant.USER_BIRTHDAY, birthday);
            UtilsSharedData.saveKey2Value(Constant.USER_HEIGHT, height);
            UtilsSharedData.saveKey2Value(Constant.USER_WEIGHT, weight);
        }
        UtilsSharedData.initDataShare(act);// ////////
        switch (ActivityPersonalInfo.type1) {
            case 0:
                if (resultStatus == 1) {
                    UtilsSharedData.saveKey2Value(Constant.USER_NAME, name);
                }
                break;
            case 1:
//			String gender1;
                if (resultStatus == 1) {
//				if(gender.equals("00070002")){
//					 gender1 = "男";
//				}else{
//					gender1 = "女";
//				}
                    UtilsSharedData.saveKey2Value(Constant.USER_GENDER, gender);
                }
                break;
            case 2:
                if (resultStatus == 1) {
                    UtilsSharedData.saveKey2Value(Constant.USER_BIRTHDAY, birthday);
                }
                break;
            case 3:
                if (resultStatus == 1) {
                    UtilsSharedData.saveKey2Value(Constant.USER_HEIGHT, height);
                }
                break;
            case 4:
                if (resultStatus == 1) {
                    UtilsSharedData.saveKey2Value(Constant.USER_WEIGHT, weight);
                }
                break;
            default:
                break;
        }

        ((ActivityBasic) act).hideProgressBar();

    }

}
