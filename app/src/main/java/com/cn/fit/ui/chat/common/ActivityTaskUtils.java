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

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 打印Activity相关信息
 * Created by Jorstin on 2015/3/18.
 */
public class ActivityTaskUtils {

    private Context mContext;

    /**
     *
     */
    public ActivityTaskUtils(Context context) {
        mContext = context;
    }

    public String toString() {
        Context context = mContext;
        if (context == null) {
            return null;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        if (activityManager == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        StringBuffer buffer = new StringBuffer();
        for (ActivityManager.RunningTaskInfo info : runningTasks) {
            if (!info.baseActivity.getClassName().startsWith(packageName) && !info.topActivity.getClassName().startsWith(packageName)) {
                continue;
            }
            Object[] args = new Object[5];
            args[0] = info.id;
            args[1] = info.numRunning;
            args[2] = info.numActivities;
            args[3] = info.topActivity.getShortClassName();
            args[4] = info.baseActivity.getShortClassName();
            String.format("{id:%d num:%d/%d top:%s base:%s}", args);
        }

        return buffer.toString();
    }
}
