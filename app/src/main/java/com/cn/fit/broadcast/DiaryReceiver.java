package com.cn.fit.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmUtils;
import com.cn.fit.ui.welcome.ActivityWelcomePage;
import com.cn.fit.util.Constant;

/**
 * @kuangtiecheng 丑旦
 * @version 1.0
 * @date 创建时间：2015/11/11 下午4:44:44
 * @parameter
 * @return
 */
public class DiaryReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    public Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        String content = intent.getStringExtra(Constant.NOTIFY_CONTENT);
        System.out.println("=-=-=广播传过来的content=-=-=" + content);
        int id = intent.getIntExtra(Constant.NOTIFY_ID, -1);
        System.out.println("=-=-=广播传过来的id=-=-=" + id);
//		检测app是否运行，决定行为
        System.out.println("是否isRunning  " + AlarmUtils.isRunning(context));
        if (!AlarmUtils.isRunning(context)) {
            System.out.println("=-=-=广播传过来=-=-= 当前app不在运行");
            notification = AlarmUtils.getNotification(context, ActivityWelcomePage.class, id, content);
        } else {
//			if (AlarmUtils.isTopActivy(context, "com.cn.aihu.ui.patient.main.TabActivityMain")) {
//				System.out.println("=-=-=广播传过来=-=-= TabActivityMain处于栈顶，勿重启");
            notification = AlarmUtils.getNotification(context, TabActivityMain.class, id, content);
//			} else {
//				System.out.println("=-=-=广播传过来=-=-= 重启ActivityWelcomePage");
//				notification = AlarmUtils.getNotification(context, ActivityWelcomePage.class, id, content);
//			}
        }
        manager.notify(id, notification);
    }
}
