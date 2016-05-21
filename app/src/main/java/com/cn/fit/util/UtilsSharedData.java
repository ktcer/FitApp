package com.cn.fit.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UtilsSharedData {

    private final static String PREFS_NAME = "com.cn.aihu";

    private static SharedPreferences dataShare;

    /**
     * 初始化
     *
     * @param paramContext
     */
    public static void initDataShare(Context paramContext) {
        dataShare = paramContext.getSharedPreferences(PREFS_NAME, 0);
    }

    /**
     * 存储键值对数据
     *
     * @param key   String 键值
     * @param value String 键值的对应数据
     * @return boolean 是否保存成功
     */
    public static boolean saveKey2Value(String key, String value) {
        return saveKeyMustValue(key, value);
    }

    /**
     * 存储键值对数据
     *
     * @param key   String 键值
     * @param value String 键值的对应数据
     * @return boolean 是否保存成功
     */
    public static boolean saveKeyMustValue(String key, String value) {
        if (null != dataShare) {
            SharedPreferences.Editor editor = dataShare.edit();
            editor.putString(key, value);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public static boolean saveKeyMustValue(String key, boolean value) {
        if (null != dataShare) {
            SharedPreferences.Editor editor = dataShare.edit();
            editor.putBoolean(key, value);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public static boolean saveKeyMustValue(String key, long value) {
        if (null != dataShare) {
            SharedPreferences.Editor editor = dataShare.edit();
            editor.putLong(key, value);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 清除SharedPreferences产生的缓存数据
     */
    public static void clearData() {
        if (null != dataShare) {
            SharedPreferences.Editor editor = dataShare.edit();
            editor.clear();
            editor.commit();
        }
    }

    /**
     * 返回key值对应的value
     *
     * @param key String 键值
     * @return String
     */
    public static String getValueByKey(String key) {
        if (null != dataShare) {
            return dataShare.getString(key, "");
        } else {
            return "";
        }
    }

    /**
     * 返回key值对应的value，所数据解析异常返回默认值
     *
     * @param key   String 键值
     * @param value int 默认值
     * @return int
     */
    public static int getIntByKey(String key, int value) {
        String data = getValueByKey(key);
        try {
            return Integer.valueOf(data);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    /**
     * 返回key值对应的value，所数据解析异常返回默认值
     *
     * @param key   String 键值
     * @param value long 默认值
     * @return int
     */
    public static long getLong(String key, long value) {
        if (null != dataShare) {
            return dataShare.getLong(key, value);
        }
        return -1;
    }


    /**
     * 返回key值对应的value，所数据解析异常返回false
     *
     * @param key String 键值
     * @return boolean
     */
//	public static boolean getBooleanByKey(String key){
//		String data = getValueByKey(key);
//		return Boolean.parseBoolean(data);
//	}
    public static boolean getBoolean(String key) {
        if (null != dataShare) {
            return dataShare.getBoolean(key, false);
        }
        return false;
    }

    /**
     * 根据key值查询对应的数据是否为空字符串
     *
     * @param key String 键值
     * @return boolean
     */
    public static boolean isEmpty(String key) {
        String data = getValueByKey(key);
        return "".equals(data);
    }
}
