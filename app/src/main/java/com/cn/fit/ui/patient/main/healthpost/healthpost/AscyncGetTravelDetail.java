package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.os.AsyncTask;

import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanTravelDetail;
import com.cn.fit.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncGetTravelDetail extends AsyncTask<Integer, Integer, BeanTravelDetail> {
    private String travelID;
    private BeanTravelDetail BeanTD;

    /**
     *
     */
    public AscyncGetTravelDetail() {
    }

    /**
     * @param travelID
     */
    public AscyncGetTravelDetail(String travelID) {
        this.travelID = travelID;
    }

    @Override
    protected BeanTravelDetail doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("travelID", travelID + "");
        try {
            String url = AbsParam.getBaseUrl() + "/travel/detail";
            String retString = NetTool.sendPostRequest(url, param, "utf-8");
            BeanTD = jsonToBTD(retString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return BeanTD;
    }

    @Override
    protected void onPostExecute(BeanTravelDetail result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    private BeanTravelDetail jsonToBTD(String str) {
        BeanTravelDetail BTD = new BeanTravelDetail();
        Gson gson = new Gson();
        if (str != null & str != "-1") {
            BTD = gson.fromJson(str, new TypeToken<BeanTravelDetail>() {
            }.getType());
        }
        return BTD;
    }
}
