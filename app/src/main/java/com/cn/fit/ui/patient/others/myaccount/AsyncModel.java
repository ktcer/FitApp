/**
 *
 */
package com.cn.fit.ui.patient.others.myaccount;

import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.util.AbsParam;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public abstract class AsyncModel extends AsyncTask<Integer, Integer, JSONObject> {
    public final String TAG = this.getClass().getName();
    public String url = "";

    @Override
    protected JSONObject doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONObject json = new JSONObject();
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            if (!result.equals("sendText error!"))
                json = new JSONObject(result);
            System.out.println(TAG + "=-=-=-=-=-= json...=-=-=-=-=-=" + json);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(TAG + "=-=-=-=-=-= json解析出错了...=-=-=-=-=-=");
        }
        return json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String path) {
        this.url = AbsParam.getBaseUrl() + path;
//		this.url = "http://192.168.202.108:8080/serviceplatform"+path;
    }

    protected abstract HashMap<String, String> getMap();

    protected abstract void setPath(String path);
}

