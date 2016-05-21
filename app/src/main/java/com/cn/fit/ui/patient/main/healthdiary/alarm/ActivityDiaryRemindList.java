package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.patient.main.healthdiary.DataBaseHelper;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/28 上午11:15:52
 * @parameter
 * @return
 */
public class ActivityDiaryRemindList extends ActivityBasic implements OnItemClickListener {
    public ArrayList<BeanHealthDiaryLocal> listData;
    public DiaryRemindAdapter adapter;
    public ListView list;
    public BeanHealthDiaryLocal bean;
    public SettingItem mSettingSound;
    public final static String DIARY_LOCAL = "diary_local";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_remind);
        TextView middle_tv = (TextView) findViewById(R.id.middle_tv);
        middle_tv.setText("日记提醒");
        list = (ListView) findViewById(R.id.list);
        mSettingSound = (SettingItem) findViewById(R.id.settings_new_msg_sound);
        mSettingSound.getCheckedTextView().setChecked(getCheckStatus());
        mSettingSound.getCheckedTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewMsgNotification(mSettingSound);
            }

        });
    }

    private boolean getCheckStatus() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        boolean value = sharedPreferences.getBoolean(ECPreferenceSettings.SETTINGS_DIARY_REMIND_SHAKE.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_DIARY_REMIND_SHAKE.getDefaultValue());
        return value;
    }

    private void updateNewMsgNotification(SettingItem mSettingSound) {
        // TODO Auto-generated method stub
        if (mSettingSound == null) {
            return;
        }
        mSettingSound.toggle();
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_DIARY_REMIND_SHAKE, mSettingSound.isChecked(), true);
        } catch (InvalidClassException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void init() {
        listData = readDatabase();
        Log.e("=-=-=listData.size()=-=-=", "" + listData.size());
        adapter = new DiaryRemindAdapter(this, listData);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }

    /**
     * @author 丑旦
     * 查询所有的日记提醒
     */
    public ArrayList<BeanHealthDiaryLocal> readDatabase() {
        BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
        beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(
                Constant.USER_ID, 0));
        beanHealthDiaryLocal.setValid(DataBaseHelper.VALID_FLAG);
        ArrayList<BeanHealthDiaryLocal> list = DataBaseHelper.onQuery(
                this, beanHealthDiaryLocal);

        //打印信息
        Iterator<BeanHealthDiaryLocal> iterator = list.iterator();
        while (iterator.hasNext()) {
            Log.e("=-=-=iterator=-=-=", iterator.next().toString());
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        bean = listData.get(arg2);
        System.out.println("=-=-=bean=-=-=" + bean);
        Intent intent = new Intent(ActivityDiaryRemindList.this, ActivityEditNotificationTime.class);
        intent.putExtra(ActivityDiaryRemindList.DIARY_LOCAL, bean);
        startActivity(intent);
    }
}
