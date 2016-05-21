package com.cn.fit.ui.patient.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cn.fit.util.AESUtils;

public class User implements Comparable {
    private String mId;
    private String mPwd;
    private int addOrder;
    private static final String masterPassword = "FORYOU"; // AES加密算法的种子
    private static final String JSON_ID = "user_id";
    private static final String JSON_PWD = "user_pwd";
    private static final String TAG = "User";
    private static int resultID;
    private static String detail;

    public User(String id, String pwd, int addOrder) {
        this.mId = id;
        this.mPwd = pwd;
        this.addOrder = addOrder;
    }

    public User(JSONObject json) throws Exception {
        if (json.has(JSON_ID)) {
            String id = json.getString(JSON_ID);
            String pwd = json.getString(JSON_PWD);
            // 解密后存放
            mId = AESUtils.decrypt(masterPassword, id);
            mPwd = AESUtils.decrypt(masterPassword, pwd);
        }
    }

    public JSONObject toJSON() throws Exception {
        // 使用AES加密算法加密后保存
        String id = AESUtils.encrypt(masterPassword, mId);
        String pwd = AESUtils.encrypt(masterPassword, mPwd);
        Log.i(TAG, "加密后:" + id + "  " + pwd);
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_ID, id);
            json.put(JSON_PWD, pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public void setAddOrder(int addOrder) {
        this.addOrder = addOrder;
    }

    public String getId() {
        return mId;
    }

    public String getPwd() {
        return mPwd;
    }


    public void setId(String id) {
        mId = id;
    }

    public void setPwd(String pwd) {
        mPwd = pwd;
    }


    public static int getResultID() {
        return resultID;
    }

    public static void setResultID(int resultID) {
        User.resultID = resultID;
    }

    public static String getDetail() {
        return detail;
    }

    public static void setDetail(String detail) {
        User.detail = detail;
    }

    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        if (this.addOrder < ((User) arg0).addOrder)
            return 1;
        return -1;
    }
}
