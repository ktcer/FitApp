package com.cn.fit.ui.patient.others.myaccount;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;


public abstract class AsyncBasic extends AsyncModel {
    public Context mContext;

    public AsyncBasic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        return new HashMap<String, String>();
    }

    @Override
    protected void setPath(String path) {
        // TODO Auto-generated method stub
        setUrl(path);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result == null) {
            Toast.makeText(mContext, "网络出问题了", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(mContext);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }
}
