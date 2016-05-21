package com.cn.fit.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cn.fit.socket.SocketClient;
import com.cn.fit.util.Constant;

public class ServiceGps extends Service implements LocationListener {

    private static final String TAG = "ServiceGps";
    public static long msec = 0;
    private boolean bThreadRunning = true;
    private String hostIP = "118.26.142.167";
    private int port = 36100;
    private LocationManager locationManager;
    private Double latitude = 39.9765586;
    private Double longitude = 116.4895345;
    private String userID, userName, imei;

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        ServiceGps getService() {
            return ServiceGps.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        if (intent != null) {
            userID = intent.getStringExtra(Constant.USER_ID);
            userName = intent.getStringExtra(Constant.USER_ACCOUNT);
        } else {
            userID = "1";
            userName = "232";
        }
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) locationManager
                .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                        this);
        else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) locationManager
                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        this);
        else Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (bThreadRunning) {
                    try {
//						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//							//gps已打开
//						} else {
//							toggleGPS();
//							try {
//								Thread.sleep(2000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
                        SocketClient client = new SocketClient(hostIP, port);
                        if (client.getClient() != null) {
                            System.out.println(client.sendMsg(formData()));
                            client.closeSocket();
                        }
                        Thread.sleep(5 * 60000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        bThreadRunning = false;
        super.onDestroy(); // 可以不用
    }

    private String formData() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timeStr = sdf.format(calendar.getTime());

        TelephonyManager telephonemanage = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonemanage.getDeviceId();

        //类型,imei,userID,userName,经度，纬度，时间
        String result = "A1001" + "," + imei + "," + userID + "," + userName + "," + longitude + "," + latitude + "," + timeStr;
        Log.i("result", result);
        System.out.println("result:" + result);
        return result;
    }


    private void toggleGPS() {

        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        this.sendBroadcast(intent);

        String provider = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        latitude = arg0.getLatitude();
        longitude = arg0.getLongitude();
    }


    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}
