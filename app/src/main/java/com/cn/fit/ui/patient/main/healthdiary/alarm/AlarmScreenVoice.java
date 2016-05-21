package com.cn.fit.ui.patient.main.healthdiary.alarm;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackInterface;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackMediaRecorderImpl;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.FButton;

public class AlarmScreenVoice extends Activity {
    private FButton goinBtn, btnPlayback;

    private Timer timer;
    private TimerTask task;
    private boolean isPlayBack = false;// 设置正在回放
    //	private static String basicFolder;
    // NewAudioName可播放的音频文件
//	private static String AudioName;
//	private static String audioDirector;
//	private List<String> contentString = new ArrayList<String>();
    private String path = "";//音频路径
    private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
    private BeanHealthDiaryLocal beanHealthDiaryLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_play_voice);

        String id = getIntent().getStringExtra(AlarmUtils.ALARM_ID);
        Log.e("=-=-=AlarmScreenVoice 接收的id=-=-=", "" + id);

//		audioDirector = AppDisk.appInursePath
//				+ UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
//				+ File.separator + AppDisk.DCIM_RECORD;
//		contentString = CreateFolder.getAllFileNameInFolder(audioDirector);
        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                getApplicationContext());
//		UtilsSharedData.initDataShare(this);
//		basicFolder = AppDisk.appInursePath
//				+ UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
//				+ File.separator;
//		AudioName = basicFolder + AppDisk.DCIM_RECORD + "love.raw";

        beanHealthDiaryLocal = AlarmUtils.getDiaryBean(this, id);
        Log.e("=-=-=AlarmScreenVoice beanHealthDiaryLocal.toString()=-=-=", "" + beanHealthDiaryLocal.toString());
        path = beanHealthDiaryLocal.getPath();
        goinBtn = (FButton) findViewById(R.id.goinProgram_voice);
        btnPlayback = (FButton) findViewById(R.id.Btn_playback_fb);
        playVoice();

        goinBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("接收到提醒了" + AppMain.isAppRunning);
                System.out.println("AlarmScreenText.isRunning" + AlarmUtils.isRunning(AlarmScreenVoice.this));
                if (!AlarmUtils.isRunning(AlarmScreenVoice.this)) {
                    AlarmUtils.startApp(AlarmScreenVoice.this);
                }
                finish();
            }
        });
    }

    private void playVoice() {
        if (!CreateFolder.hasSDCard()) {
            Toast.makeText(this, "请插入sd卡进行回放", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isPlayBack) {
            if (timer != null && task != null) {
                task.cancel();
                timer.cancel();
                task = null;
                timer = null;
            }
            // btnPlaybackVioce.setText("开始试听");
            btnPlayback.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_emerald));
            isPlayBack = false;
        } else {
//			audioRecorderAndPlaybackInterface.startPlayback(audioDirector
//					+ contentString.get(contentString.size() - 1));
            audioRecorderAndPlaybackInterface.startPlayback(path);
            btnPlayback.setText("停止音频提醒");
            btnPlayback.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_orange));
            isPlayBack = true;
            timer = new Timer();
            /*
			 * 监听音频回放是否完成
			 */
            task = new TimerTask() {
                public void run() {
                    if (audioRecorderAndPlaybackInterface.isPlayBackComplete()) {
                        audioRecorderAndPlaybackInterface.stopPlayback();
                        mHandler.sendEmptyMessage(0);
                        isPlayBack = false;
//						task.cancel();
//						timer.cancel();
                        task = null;
                        timer = null;
                    }
                }
            };
            timer.schedule(task, 0, 100);
        }

    }

    private MyHandler mHandler = new MyHandler();

    class MyHandler extends Handler {

        public MyHandler() {

        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 此处可以更新UI
            if (beanHealthDiaryLocal.getContent().equals("") || beanHealthDiaryLocal.getContent() == null)
                btnPlayback.setText("开始音频提醒，嘿嘿");
            else
                btnPlayback.setText(beanHealthDiaryLocal.getContent());
            btnPlayback.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_emerald));

            if (goinBtn.isSelected()) {
                audioRecorderAndPlaybackInterface.stopPlayback();
                audioRecorderAndPlaybackInterface.release();
            } else {
                playVoice();
            }

        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        audioRecorderAndPlaybackInterface.stopPlayback();
        audioRecorderAndPlaybackInterface.release();
    }

}
