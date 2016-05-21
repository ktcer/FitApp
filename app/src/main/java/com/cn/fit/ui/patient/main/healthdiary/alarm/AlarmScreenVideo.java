package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackInterface;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackMediaRecorderImpl;
import com.cn.fit.util.FButton;

public class AlarmScreenVideo extends Activity {
    //	private String urlStream = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/04/vts_01_4.m3u8";
    private String urlStream = "";
    private VideoView myVideoView;
    private FButton goinBtn;
    public RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
    private BeanHealthDiaryLocal beanHealthDiaryLocal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_play);

        String id = getIntent().getStringExtra(AlarmUtils.ALARM_ID);
        Log.e("=-=-=AlarmScreenVideo 中接收的id=-=-=", "" + id);

        beanHealthDiaryLocal = AlarmUtils.getDiaryBean(this, id);
        Log.e("=-=-=AlarmScreenVideo  beanHealthDiaryLocal.toString()=-=-=", "" + beanHealthDiaryLocal.toString());

        myVideoView = (VideoView) this.findViewById(R.id.my_video_view);
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.INVISIBLE);
        goinBtn = (FButton) findViewById(R.id.goinProgram);
        myVideoView.setMediaController(mc);
        goinBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println("接收到提醒了" + AppMain.isAppRunning);
                System.out.println("AlarmScreenVideo.isRunning" + AlarmUtils.isRunning(AlarmScreenVideo.this));
                if (!AlarmUtils.isRunning(AlarmScreenVideo.this)) {
                    AlarmUtils.startApp(AlarmScreenVideo.this);
                }
                finish();
            }
        });


//		urlStream = getIntent().getExtras().getString(Constant.VEDIO_URL);
        urlStream = beanHealthDiaryLocal.getPath();
        System.out.println("=-=-=提醒路径  urlStream=-=-=" + urlStream);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (urlStream != null) {
                        myVideoView.setVideoURI(Uri.parse(urlStream));
                        myVideoView.start();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
