package com.cn.fit.ui.patient.main.healthdiary.alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TimePicker;

import com.cn.fit.R;
import com.cn.fit.broadcast.AlarmManagerReceiver;
import com.cn.fit.model.healthdiary.BeanAlarm;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.patient.main.healthdiary.DataBaseHelper;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/5 下午7:03:44
 * @parameter
 * @return
 */
public class AlarmUtils {
    public final static String ALARM_ID = "alarm_id";
    public final static int VIDEO = 1;//视频
    public final static int AUDIO = 0;//音频
    public final static int TEXT = 2;//文本

    /**
     * @author kuangtiecheng 获取AlarmManager
     */
    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng 设置闹铃
     */
    public static void setAlarms(Context context, ArrayList<BeanHealthDiaryLocal> list) throws ParseException {
        for (int j = 0; j < list.size(); j++) {
            BeanHealthDiaryLocal beanHealthDiaryLocal = list.get(j);
            setAlarm(context, beanHealthDiaryLocal);
        }
    }

    /**
     * @throws ParseException 如果trigger time已经过去，闹铃会立即触发
     * @author kuangtiecheng 设置闹铃
     */
    public static void setAlarm(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) throws ParseException {
        long intervalMillis = 24 * 60 * 60 * 1000 * 7;//重复时长
        List<PendingIntent> list = getPendingIntent(context, beanHealthDiaryLocal);
        AlarmManager alarmManager = getAlarmManager(context);
        String time = beanHealthDiaryLocal.getDaytime();
        int[] hour = getHourMinute(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.e("=-=-=设置之前=-=-=", "" + calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour[0]);
        calendar.set(Calendar.MINUTE, hour[1]);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String date = beanHealthDiaryLocal.getDateday();
        String[] array = date.split(",");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("星期一")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            } else if (array[i].equals("星期二")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);

            } else if (array[i].equals("星期三")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);

            } else if (array[i].equals("星期四")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);

            } else if (array[i].equals("星期五")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

            } else if (array[i].equals("星期六")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

            } else if (array[i].equals("星期日")) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            }
            long firstRingUp = calendar.getTimeInMillis();
            Log.e("=-=-=设置之后=-=-=", "" + firstRingUp);
            System.out.println("=-=-=设置的闹铃是：=-=-="
                    + calendar.get(Calendar.DAY_OF_WEEK) + "  "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstRingUp, intervalMillis, list.get(i));
        }
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 解析"HH:mm"时间字符串,获得Calendar
     */
    public static Calendar getTime(String time) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        Date date = sf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * @author kuangtiecheng
     * 解析"HH:mm"时间字符串
     */
    public static int[] getHourMinute(String time) {
        int[] hourMinute = {-1, -1};
        String[] array = time.split(":");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("00")) {
                hourMinute[i] = 0;
            } else if (array[i].startsWith("0")) {
                hourMinute[i] = Integer.parseInt(array[i].replaceAll("0", ""));
            } else {
                hourMinute[i] = Integer.parseInt(array[i]);
            }
            System.out.println("" + hourMinute[i]);
        }
        return hourMinute;
    }

    /**
     * @param requestCode,区分不同的闹铃,该参数不能重复，否则闹铃会失效
     * @author kuangtiecheng
     * 封装PendingIntent,与Broadcast绑定
     */
    public static List<PendingIntent> getPendingIntent(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) {
//		Log.e("=-=-=getPendingIntent=-=-=", "beanHealthDiaryLocal  "+ beanHealthDiaryLocal);
        List<BeanAlarm> list = DataBaseHelper.queryAlarm(context, beanHealthDiaryLocal.getId());
        List<PendingIntent> listIntent = new ArrayList<PendingIntent>();

        for (int i = 0; i < list.size(); i++) {
//			Log.e("=-=-=getPendingIntent BeanAlarm=-=-=", ""+list.get(i));
            Intent intent = new Intent(context, AlarmManagerReceiver.class);// intent-filter
            intent.putExtra(AlarmUtils.ALARM_ID, beanHealthDiaryLocal.getId());
            if (android.os.Build.VERSION.SDK_INT >= 12) {
                intent.setFlags(32);//3.1以后的版本需要设置Intent.FLAG_INCLUDE_STOPPED_PACKAGES = 32
            }
            PendingIntent pi = PendingIntent.getBroadcast(context, list.get(i).getAlarmid(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            listIntent.add(pi);
        }
        return listIntent;
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 根据id取消某个时间点的闹铃
     */
    public static void cancelAlarm(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) throws ParseException {
        List<PendingIntent> list = getPendingIntent(context, beanHealthDiaryLocal);
        for (PendingIntent pendingIntent : list) {
            AlarmManager alarmManager = getAlarmManager(context);
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 取消所有闹铃
     */
    public static void cancelAlarms(Context context, ArrayList<BeanHealthDiaryLocal> list) throws ParseException {
        for (int i = 0; i < list.size(); i++) {
            cancelAlarm(context, list.get(i));
        }
    }

    /**
     * 按id查询响铃的闹钟,确定显示的内容
     */
    public static BeanHealthDiaryLocal getDiaryBean(Context context, String id) {
        BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
        beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(
                Constant.USER_ID, 0));
        beanHealthDiaryLocal.setId(Integer.valueOf(id));
        beanHealthDiaryLocal.setValid(DataBaseHelper.VALID_FLAG);

        ArrayList<BeanHealthDiaryLocal> list = DataBaseHelper.onQuery(
                context, beanHealthDiaryLocal);
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return beanHealthDiaryLocal;
    }

    /**
     * @author 丑旦
     * 格式化输出时间："HH:mm"
     */
    public static String timeFormat(TimePicker timePicker) {
        StringBuilder builder = new StringBuilder();
        if (timePicker.getCurrentHour() < 10) {
            builder.append("0").append(timePicker.getCurrentHour());
            builder.append(":");
            if (timePicker.getCurrentMinute() < 10)
                builder.append("0").append(timePicker.getCurrentMinute());
            else
                builder.append(timePicker.getCurrentMinute());
        } else {
            builder.append(timePicker.getCurrentHour()).append(":");
            if (timePicker.getCurrentMinute() < 10)
                builder.append("0").append(timePicker.getCurrentMinute());
            else
                builder.append(timePicker.getCurrentMinute());
        }
        return builder.toString();
    }

    /**
     * @author kuangtiecheng
     * 判别类型
     */
    public static int getRemindType(BeanHealthDiaryLocal bean) {
        int remindType = -1;
        if (bean != null) {
            String path = bean.getPath();
            if (path != null && path.endsWith(".mp4")) {
                remindType = VIDEO;
            } else if (path != null && path.endsWith(".wav")) {
                remindType = AUDIO;
            } else if (path == null || path.length() == 0) {
                remindType = TEXT;
            }
        }
        return remindType;
    }

    /**
     * @param context 判别app是否运行
     * @kuangtiecheng
     */
    public static boolean isRunning(Context context) {
        AppMain.isAppRunning = false;//恢复初值
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        String MY_PKG_NAME = "com.cn.aihu";
        if (context.getResources().getString(R.string.environment).equals("0")) {
            //开发环境
            MY_PKG_NAME = "com.cn.aihu.developing";
        } else if (context.getResources().getString(R.string.environment).equals("1")) {
            //测试环境
            MY_PKG_NAME = "com.cn.aihu.staging";
        } else if (context.getResources().getString(R.string.environment).equals("2")) {
            //正式环境
            MY_PKG_NAME = "com.cn.aihu";
        }

        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                AppMain.isAppRunning = true;
                break;
            }
        }
        return AppMain.isAppRunning;
    }

    /**
     * 封装Notification
     */
    public static Notification getNotification(Context context, Class<?> cls,
                                               int requestCode, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent playIntent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(
                context.getResources().getString(R.string.app_name))
                .setContentText(content).setSmallIcon(R.drawable.notification_icon)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .setTicker(content);
        builder.build().sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "20");
        return builder.build();
    }

    /**
     * 检测某activity是否在当前Task的栈顶
     */
    public static boolean isTopActivy(Context context, String activity) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;

        if (null != runningTaskInfos) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.getClassName();
            Log.e("cmpname", "cmpname:" + cmpNameTemp);
        }

        if (null == cmpNameTemp)
            return false;
        return cmpNameTemp.equals(activity);
    }

    /**
     * @kuangtiecheng 启动app
     */
    public static void startApp(Context context) {
        String MY_PKG_NAME = "com.cn.aihu";
        if (context.getResources().getString(R.string.environment).equals("0")) {
            //开发环境
            MY_PKG_NAME = "com.cn.aihu.developing";
        } else if (context.getResources().getString(R.string.environment).equals("1")) {
            //测试环境
            MY_PKG_NAME = "com.cn.aihu.staging";
        } else if (context.getResources().getString(R.string.environment).equals("2")) {
            //正式环境
            MY_PKG_NAME = "com.cn.aihu";
        }
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(MY_PKG_NAME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
