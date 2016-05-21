package com.cn.fit.ui.patient.others.myaccount;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.cn.fit.R;
import com.cn.fit.util.FButton;

public class AsyncQueryRemarkState extends AsyncBasic {
    public FButton btn;
    private Context mContext;

    public AsyncQueryRemarkState(Context mContext, FButton btn) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.btn = btn;
        this.mContext = mContext;
        setPath("/goldcoins/ifsign");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", "" + getUserId(0));
        map.put("userType", "" + 0);
        return map;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        try {
            if (result.getInt("state") == 1)
                setBtnRemark(true);
            else
                setBtnRemark(false);
            System.out.println("=-=-=-= result =-=-=-=: " + result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setBtnRemark(boolean isRemark) {
        if (isRemark) {
            btn.setText(mContext.getString(R.string.remark_finish));
            btn.setEnabled(false);
            btn.setBackgroundColor(mContext.getResources().getColor(R.color.fbutton_color_concrete));
        }
        return;

    }
}
