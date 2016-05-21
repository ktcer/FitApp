package com.cn.fit.customer.discovery;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanDiscoveryDetail;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 活动详情
 * Created by ktcer on 2016/1/3.
 */
public class QureyDiscoveryDetail extends AsyncTask<Integer, Integer, BeanDiscoveryDetail> {

    public QureyDiscoveryDetail() {
        super();
    }

    String result = null;
    private Context context;
    private long classID;
    private double latitude;
    private double longitude;
    private BeanDiscoveryDetail bean;

    public QureyDiscoveryDetail(Context context, long classID, double latitude, double longitude) {
        this.context = context;
        this.classID = classID;
        this.latitude = latitude;
        this.longitude = longitude;
        bean = new BeanDiscoveryDetail();
    }

    @Override
    protected BeanDiscoveryDetail doInBackground(Integer... params) {
//        UtilsSharedData.initDataShare(context.this);
//        long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("classID", classID + "");
        param.put("latitude", latitude + "");
        param.put("longitude", longitude + "");
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                    + "/ad/app/detail", param, "utf-8");
            Log.i("result", result);
            bean = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return bean;
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanDiscoveryDetail JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanDiscoveryDetail beanDiscoveryDetail = new BeanDiscoveryDetail();
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanDiscoveryDetail = gson.fromJson(jsonString,
                        new TypeToken<BeanDiscoveryDetail>() {
                        }.getType());

            }
        }
        return beanDiscoveryDetail;
    }

    @Override
    protected void onPostExecute(BeanDiscoveryDetail beanDiscoveryDetail) {

    }
}
