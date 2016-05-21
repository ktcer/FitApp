package com.cn.fit.ui.patient.others.myaccount;

import java.util.HashMap;

import android.content.Context;

public class AsyncRemark extends AsyncBasic {

    public AsyncRemark(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        setPath("/goldcoins/sign");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", "" + getUserId(0));
        map.put("userType", "" + 0);
        return map;
    }
}
