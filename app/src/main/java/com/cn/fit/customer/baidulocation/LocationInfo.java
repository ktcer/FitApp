package com.cn.fit.customer.baidulocation;

/**
 * Created by ktcer on 2016/1/4.
 */
public class LocationInfo {
    /**
     * 经纬度信息结构体
     */
    public static class SItude {
        /**
         * 纬度
         */
        public double latitude;
        /**
         * 经度
         */
        public double longitude;

        @Override
        public String toString() {
            return "SItude{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}