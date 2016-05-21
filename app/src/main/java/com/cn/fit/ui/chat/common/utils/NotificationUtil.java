/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
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
package com.cn.fit.ui.chat.common.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;

/**
 * <p>Title: NotificationUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class NotificationUtil {

    public static final String TAG = LogUtil.getLogUtilsTag(Notification.class);

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static Notification buildNotification(Context context, int icon,
                                                 int defaults, boolean onlyVibrate, String tickerText,
                                                 String contentTitle, String contentText, Bitmap largeIcon,
                                                 PendingIntent intent) {

        if (Build.VERSION.SDK_INT > VERSION_CODES.HONEYCOMB) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setLights(-16711936, 300, 1000)
                    .setSmallIcon(icon)
                    .setTicker(tickerText)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(intent);

            if (onlyVibrate) {
                defaults &= Notification.DEFAULT_VIBRATE;
            }

            LogUtil.d(TAG, "defaults flag " + defaults);
            builder.setDefaults(defaults);
            if (largeIcon != null) {
                builder.setLargeIcon(largeIcon);
            }
            return builder.getNotification();
        }

        Notification notification = new Notification();
        notification.ledARGB = -16711936;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.flags = (Notification.FLAG_SHOW_LIGHTS | notification.flags);
        notification.icon = icon;
        notification.tickerText = tickerText;
        LogUtil.d(TAG, "defaults flag " + defaults);
        if (onlyVibrate) {
            defaults &= Notification.DEFAULT_VIBRATE;
        }
        notification.defaults = defaults;
        Notification.Builder builderSuccess = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentIntent(intent)
                .setContentText(contentText);
        notification = builderSuccess.getNotification();
        return notification;
    }

}
