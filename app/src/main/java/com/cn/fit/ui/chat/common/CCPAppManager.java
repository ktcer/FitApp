/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.MimeTypesTools;
import com.cn.fit.ui.chat.core.ClientUser;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ImageGalleryActivity;
import com.cn.fit.ui.chat.ui.chatting.ImageGralleryPagerActivity;
import com.cn.fit.ui.chat.ui.chatting.ImageMsgInfoEntry;
import com.cn.fit.ui.chat.ui.chatting.ViewImageInfo;
import com.cn.fit.ui.chat.ui.chatting.view.ChatFooterPanel;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

/**
 * 存储SDK一些全局性的常量
 * Created by Jorstin on 2015/3/17.
 */
public class CCPAppManager {

    public static Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
    /**
     * Android 应用上下文
     */
    private static Context mContext = null;
    /**
     * 包名
     */
    public static String pkgName = "com.cn.aihu.ui.chat";
    /**
     * SharedPreferences 存储名字前缀
     */
    public static final String PREFIX = "com.cn.aihu.ui.chat_";
    public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 0x10000000;
    /**
     * IM功能UserData字段默认文字
     */
    public static final String USER_DATA = "yuntongxun.ecdemo";
    public static HashMap<String, Integer> mPhotoCache = new HashMap<String, Integer>();
    public static ArrayList<ECSuperActivity> activities = new ArrayList<ECSuperActivity>();
    /**
     * IM聊天更多功能面板
     */
    private static ChatFooterPanel mChatFooterPanel;

    public static String getPackageName() {
        return pkgName;
    }

    private static ClientUser mClientUser;

    /**
     * 返回SharePreference配置文件名称
     *
     * @return
     */
    public static String getSharePreferenceName() {
        return pkgName + "_preferences";
    }

    /**
     * 返回上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 设置上下文对象
     *
     * @param context
     */
    public static void setContext(Context context) {
        mContext = context;
        pkgName = context.getPackageName();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPAppManager.class),
                "setup application context for package: " + pkgName);
    }

    public static ChatFooterPanel getChatFooterPanel(Context context) {
        return mChatFooterPanel;
    }

    /**
     * 缓存账号注册信息
     *
     * @param user
     */
    public static void setClientUser(ClientUser user) {
        mClientUser = user;
    }

    /**
     * 保存注册账号信息
     *
     * @return
     */
    public static ClientUser getClientUser() {
        if (mClientUser != null) {
            return mClientUser;
        }
        String registAccount = getAutoRegistAccount();
        if (!TextUtils.isEmpty(registAccount)) {
            mClientUser = new ClientUser("");
            return mClientUser.from(registAccount);
        }
        UtilsSharedData.initDataShare(mContext);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
        mClientUser = new ClientUser(userId + "1");
        String appkey = getConfig(ECPreferenceSettings.SETTINGS_APPKEY);
        String token = getConfig(ECPreferenceSettings.SETTINGS_TOKEN);
        mClientUser.setAppKey(appkey);
        mClientUser.setAppToken(token);
        return mClientUser;

    }

    private static String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId(), (String) settings.getDefaultValue());
        return value;
    }

    private static String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
        return registAccount;
    }

    /**
     * @param context
     * @param path
     */
    public static void doViewFilePrevieIntent(Context context, String path) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            String type = MimeTypesTools.getMimeType(context, path);
            File file = new File(path);
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(LogUtil.getLogUtilsTag(CCPAppManager.class), "do view file error " + e.getLocalizedMessage());
        }
    }

    /**
     * @param cotnext
     * @param value
     */
    public static void startChattingImageViewAction(Context cotnext, ImageMsgInfoEntry value) {
        Intent intent = new Intent(cotnext, ImageGralleryPagerActivity.class);
        intent.putExtra(ImageGalleryActivity.CHATTING_MESSAGE, value);
        cotnext.startActivity(intent);
    }

    /**
     * 批量查看图片
     *
     * @param ctx
     * @param position
     * @param session
     */
    public static void startChattingImageViewAction(Context ctx, int position, ArrayList<ViewImageInfo> session) {
        Intent intent = new Intent(ctx, ImageGralleryPagerActivity.class);
        intent.putExtra(ImageGralleryPagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putParcelableArrayListExtra(ImageGralleryPagerActivity.EXTRA_IMAGE_URLS, session);
        ctx.startActivity(intent);
    }

    /**
     * 获取应用程序版本名称
     *
     * @return
     */
    public static String getVersion() {
        String version = "0.0.0";
        if (mContext == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取应用版本号
     *
     * @return 版本号
     */
    public static int getVersionCode() {
        int code = 1;
        if (mContext == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }


    public static void addActivity(ECSuperActivity activity) {
        activities.add(activity);
    }

    public static void clearActivity() {
        for (ECSuperActivity activity : activities) {
            if (activity != null) {
                activity.finish();
                activity = null;
            }
            activities.clear();
        }
    }

    /**
     * 打开浏览器下载新版本
     *
     * @param context
     */
    public static void startUpdater(Context context) {
        Uri uri = Uri.parse("http://dwz.cn/F8Amj");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static HashMap<String, Object> prefValues = new HashMap<String, Object>();

    /**
     * @param key
     * @param value
     */
    public static void putPref(String key, Object value) {
        prefValues.put(key, value);
    }

    public static Object getPref(String key) {
        return prefValues.remove(key);
    }

    public static void removePref(String key) {
        prefValues.remove(key);
    }
}
