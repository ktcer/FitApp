package com.cn.fit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmScreenText;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmScreenVideo;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmScreenVoice;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmUtils;

public class AlarmManagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("收到闹铃广播了", "=-=-=onReceive=-=-=");
        int id = intent.getIntExtra(AlarmUtils.ALARM_ID, -1);
        System.out.println("=-=-=广播收到的提醒id=-=-=" + id);
        BeanHealthDiaryLocal beanHealthDiaryLocal = AlarmUtils.getDiaryBean(context, "" + id);
        int flag = AlarmUtils.getRemindType(beanHealthDiaryLocal);
        start(context, flag, id);
    }

    /**
     * @author 根据类型启动相应的activity
     */
    public void start(Context context, int flag, int id) {
        switch (flag) {
            case AlarmUtils.VIDEO:// 视频
                startActivity(context, AlarmScreenVideo.class, AlarmUtils.ALARM_ID, ""
                        + id);
                break;
            case AlarmUtils.AUDIO:// 语音
                startActivity(context, AlarmScreenVoice.class, AlarmUtils.ALARM_ID,
                        "" + id);
                break;
            case AlarmUtils.TEXT:// 文字
                startActivity(context, AlarmScreenText.class, AlarmUtils.ALARM_ID,
                        "" + id);
                break;
            default:
                break;
        }
    }

    /**
     * @author 启动activity
     */
    protected void startActivity(Context context, Class<?> cls, String key, String value) {
        Intent alarmIntent = new Intent(context, cls);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra(key, value);
        context.startActivity(alarmIntent);
    }
}
