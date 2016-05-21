package com.cn.fit.customer.coach;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanCoachPage;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/11.
 */
public class AscynsCoachPage extends AsyncTask<Integer, Integer, BeanCoachPage> {

    public AscynsCoachPage() {
        super();
    }

    String result = null;
    private Context context;
    private long coachID;
    private String latitude;
    private String longitude;
    private BeanCoachPage bean;

    public AscynsCoachPage(Context context, long coachID, String latitude, String longitude) {
        this.context = context;
        this.coachID = coachID;
        this.latitude = latitude;
        this.longitude = longitude;
        bean = new BeanCoachPage();
    }


    @Override
    protected BeanCoachPage doInBackground(Integer... params) {
//        UtilsSharedData.initDataShare(context.this);
//        long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("coachID", coachID + "");
        param.put("latitude", latitude);
        param.put("longitude", longitude);
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                    + "/home/app/coachinfo", param, "utf-8");
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
    private BeanCoachPage JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanCoachPage beanCoachPage = new BeanCoachPage();
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanCoachPage = gson.fromJson(jsonString,
                        new TypeToken<BeanCoachPage>() {
                        }.getType());

            }
        }
        return beanCoachPage;
    }

    @Override
    protected void onPostExecute(BeanCoachPage beanCoachPage) {

    }
}
