package com.cn.fit.customer.baidulocation;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cn.fit.util.Logger;
import com.cn.fit.customer.baidulocation.LocationInfo.SItude;

/**
 * Created by ktcer on 2016/1/4.
 */
public class LocationProvider {
    private static LocationClient mLocationClient = null;

    private static SItude station = new SItude();
    private static MyBDListener listener = new MyBDListener();

    Context context;

    public LocationProvider(Context context) {
        super();
        this.context = context;
    }

    public void startLocation() {
        mLocationClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setScanSpan(1000);
        option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
//        option.setProdName("demo"); // 设置产品线名称
        option.setIgnoreKillProcess(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();//将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
        mLocationClient.requestLocation();
    }

    /**
     * 停止，减少资源消耗
     */
    public void stopListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    /**
     * 更新位置并保存到SItude中
     */
    public void updateListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
            Logger.i("update the location");
        }
    }

    /**
     * 获取经纬度信息
     *
     * @return
     */
    public SItude getLocation() {
        return station;
    }

    private static class MyBDListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getCity() == null) {
                int type = mLocationClient.requestLocation();
                Logger.e("first request false" + type);
            }
            station.latitude = location.getLatitude();
            station.longitude = location.getLongitude();
        }

//        @Override
//        public void onReceivePoi(BDLocation arg0) {
//            // return
//        }

    }
}
