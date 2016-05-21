package com.cn.fit.ui.patient.others.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.personinfo.BeanPersonInfo;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.core.ClientUser;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityTestPersonInfo;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.yuntongxun.ecsdk.ECInitParams;

import org.json.JSONObject;

import java.util.HashMap;

public class AscyncLogin extends AsyncTask<Integer, Integer, String> {

    private Activity act;
    private static String base = "/base/app/";
    private int resultID = -1;
    private String mIdString;
    private String mPwdString;
    /**登录结果，0表示失败，1表示成功*/
    /**
     * 登录详情
     */
    private static String detail;
    private String retString;
    private static String macAddress;
    private BeanPersonInfo beanPersonInfo;

    public AscyncLogin(Activity act, String mIdString, String mPwdString, String macAddress) {
        this.act = act;
        this.mIdString = mIdString;
        this.macAddress = macAddress;
        this.mPwdString = mPwdString;
        UtilsSharedData.initDataShare(act);// ////////
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("telephoneNum", mIdString);
        param.put("password", mPwdString);
        param.put("userType", "0");
        param.put("macAddress", macAddress);
        try {
            String url = AbsParam.getBaseUrl() + base + "login";
            Log.i("input", url + param.toString());
            retString = NetTool.sendHttpClientPost(url, param, "utf-8");
            Log.i("result", retString);
            JsonArrayToList(retString);
        } catch (Exception e) {
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
            //			judgeCurrentPage();
            //			((ActivityBasic)act).setInputEnabled(true);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        ((ActivityBasic) act).setInputEnabled(true);
        if (resultID == 0) {
            ToastUtil.showMessage("您输入的账号或者密码错误");
            Log.e("Login Error", "it occurs to some error!!!!");
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
            ((ActivityBasic) act).setInputEnabled(true);
            judgeCurrentPage();
        } else if (resultID == 1) {
//			//先判断是否已经有视频号码了
//			if(beanPersonInfo.getVideoNumber()!=null){
//				if(beanPersonInfo.getVideoNumber().equals("0")){
//					//没有注册过视频号，需要向青牛后台注册
//					//TODO
//					beanPersonInfo.setVideoNumber("0");
//					//为后台用户增加视频号
//				}
//			}else{
//				//TODO
//				//没有注册过视频号，需要向青牛后台注册
//				beanPersonInfo.setVideoNumber("0");
//				//为后台用户增加视频号
//				//				}
//			}
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "1");
            UtilsSharedData.saveKey2Value(Constant.USER_NAME, beanPersonInfo.getName());
            UtilsSharedData.saveKey2Value(Constant.USER_IMAGEURL, beanPersonInfo.getTxlj());
            UtilsSharedData.saveKeyMustValue(Constant.USER_ID, beanPersonInfo.getId());
            UtilsSharedData.saveKeyMustValue(Constant.IFMODIFY, beanPersonInfo.getIfModify());


            CreateFolder.createFolder(mIdString);
            //			getFamilyMember();
            setChatUserInfo(beanPersonInfo);

            CCPAppManager.setContext(act);
            ContactsCache.getInstance().load();
            ContactsCache.getInstance().setOnGetContactsDoneListener(new ContactsCache.InterfaceGetContactsListener() {
                //
//				@Override
                public void getContactsDone() {
                    // TODO Auto-generated method stub
                    ToastUtil.showMessage("登录成功");
                    long BaseInfoState = UtilsSharedData.getLong(Constant.IFMODIFY, 0);
                    if (BaseInfoState == 1) {
                        Intent intent = new Intent(act, TabActivityMain.class);
                        act.startActivity(intent);
                        act.finish();
                    } else {
                        Intent intent = new Intent(act, ActivityTestPersonInfo.class);
                        act.startActivity(intent);
                        act.finish();
                    }

//
                }
            });

        } else {
            ((ActivityBasic) act).setInputEnabled(true);
            ToastUtil.showMessage("登录异常，请稍后再试！");
            judgeCurrentPage();
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
        }
    }

    /*
     * 登录聊天系统
     */
    private void setChatUserInfo(BeanPersonInfo beanPersonInfo) {
        String appkey = getConfig(ECPreferenceSettings.SETTINGS_APPKEY);
        String token = getConfig(ECPreferenceSettings.SETTINGS_TOKEN);
        ClientUser clientUser = new ClientUser(beanPersonInfo.getId() + "0");
        clientUser.setAppKey(appkey);
        clientUser.setAppToken(token);
        clientUser.setUserName(beanPersonInfo.getName());//beanPersonInfo.getUserName()
        clientUser.setLoginAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        clientUser.setPassword("");
        CCPAppManager.setClientUser(clientUser);
        SDKCoreHelper.init(act, ECInitParams.LoginMode.FORCE_LOGIN);
    }

    private String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId(), (String) settings.getDefaultValue());
        return value;
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonArrayToList(String jsonString) throws Exception {
        JSONObject loginResult = new JSONObject(retString);
        resultID = loginResult.getInt("result");
        Gson gson = new Gson();
        beanPersonInfo = gson.fromJson(jsonString, BeanPersonInfo.class);
    }

    //	private void getFamilyMember() {
    //		AscyncGetFamilyMember async = new AscyncGetFamilyMember(act) {
    //
    //			@Override
    //			protected String doInBackground(Integer... params) {
    //				// TODO Auto-generated method stub
    //				return super.doInBackground(params);
    //			}
    //
    //			@Override
    //			protected void onPostExecute(String result) {
    //				// TODO Auto-generated method stub
    //				super.onPostExecute(result);
    //				Toast.makeText(act.getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
    //				Intent intent = new Intent(act, TabActivityMain.class);
    //				act.startActivity(intent);
    //				act.finish();
    //			}
    //
    //		};
    //		async.execute();
    //	}


    private void judgeCurrentPage() {
        if (act.getClass().getName().equals("com.cn.aihu.ui.welcome.ActivityWelcomePage")) {
            //自动登录出现问题的话直接跳入登录界面
            Intent intent = new Intent(act,
                    ActivityLogin.class);
            intent.putExtra("ActivityTag", "MainActivity");
            act.startActivity(intent);
            act.finish();
        }
    }
}
