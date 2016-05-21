package com.cn.fit.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.cn.fit.R;
import com.cn.fit.customer.baidulocation.BaiduLacationUtil;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.Loger;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.ui.patient.others.myaccount.AscyncLogin;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

public class ActivityWelcomePage extends ActivityBasic {
    public static boolean isQuit = false;
    private Loger log = new Loger("[Welcome]");
    private String userAccout, userPassowrd, macAddress;
    BaiduLacationUtil location;

    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        super.onCreate(savedInstanceState);
        // MobclickAgent.onError(this);
        setContentView(R.layout.wlecome);

        AppMain.isAppRunning = true;
        UtilsSharedData.initDataShare(this);
        ReadLogIni();
        String loca;
        location = new BaiduLacationUtil(this);
        loca = location.start();
        System.out.print("locationoutput11111111" + loca + "+++++++\n");
        if (loca == null) {
            loca = location.updateListener();
            System.out.print("locationoutput11111111" + loca + "+++++++\n");
        }
        if (loca != null) {
            UtilsSharedData.saveKeyMustValue(Constant.LOCATION, loca);
        }

    }

    /**
     * 读取ini文件
     */
    private void ReadLogIni() {
        userAccout = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
        userPassowrd = UtilsSharedData.getValueByKey(Constant.USER_PASS);
        macAddress = getLocalMacAddress();
        if (UtilsSharedData.getValueByKey(Constant.LOGIN_STATUS).equals("1")
                && userAccout != null && userPassowrd != null) {
            AscyncLogin async = new AscyncLogin(this, userAccout, userPassowrd,
                    macAddress);
            async.execute();
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(ActivityWelcomePage.this,
                            ActivityLogin.class);
                    intent.putExtra("ActivityTag", "MainActivity");
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isQuit = true;
    }

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
