package com.cn.fit.coach.comment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanGetStar;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/3.
 */
public class AscynGetStar extends AsyncTask<Integer, Integer, BeanGetStar> {

    public AscynGetStar() {
        super();
    }

    String result = null;
    private Context context;

    private String urls = "/my/app/coachstar";
    private String pamrms = "coachID";
    private long ids;

    private BeanGetStar bean;

    public AscynGetStar(Context context, long ids) {
        this.context = context;
        this.ids = ids;

    }


    @Override
    protected BeanGetStar doInBackground(Integer... params) {
//        UtilsSharedData.initDataShare(context.this);
//        long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(pamrms, ids + "");
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl() + urls, param, "utf-8");
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
    private BeanGetStar JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanGetStar beanGetStar = new BeanGetStar();
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beanGetStar = gson.fromJson(jsonString,
                        new TypeToken<BeanGetStar>() {
                        }.getType());

            }
        }
        return beanGetStar;
    }

    @Override
    protected void onPostExecute(BeanGetStar beanGetStar) {

    }
}
