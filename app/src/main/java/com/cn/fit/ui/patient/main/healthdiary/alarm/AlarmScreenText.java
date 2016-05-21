package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.FButton;

public class AlarmScreenText extends ActivityBasic {
    private TextView textRimend;
    private FButton goinBtn;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_play_text);
        String id = getIntent().getStringExtra(AlarmUtils.ALARM_ID);
        Log.e("=-=-=AlarmScreenText中接收的id=-=-=", "" + id);
        BeanHealthDiaryLocal beanHealthDiaryLocal = AlarmUtils.getDiaryBean(this, id);
        Log.e("=-=-=AlarmScreenText beanHealthDiaryLocal.toString()=-=-=", "" + beanHealthDiaryLocal.toString());
        textRimend = (TextView) findViewById(R.id.Btn_textrimend_fb);
        textRimend.setText(beanHealthDiaryLocal.getContent());

        goinBtn = (FButton) findViewById(R.id.goinProgram_text);
        goinBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("接收到提醒了" + AppMain.isAppRunning);
                System.out.println("AlarmScreenText.isRunning" + AlarmUtils.isRunning(AlarmScreenText.this));
                if (!AlarmUtils.isRunning(AlarmScreenText.this)) {
                    AlarmUtils.startApp(AlarmScreenText.this);
                }
                finish();
            }
        });
    }
}
