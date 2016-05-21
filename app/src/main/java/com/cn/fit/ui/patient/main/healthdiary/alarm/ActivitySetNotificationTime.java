package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanAlarm;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthdiary.DataBaseHelper;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackInterface;
import com.cn.fit.ui.record.audio.RecorderAndPlaybackMediaRecorderImpl;
import com.cn.fit.ui.record.video.VideoRecorderView.RecorderListener;
import com.cn.fit.util.AppDisk;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ResourceAsColor")
public class ActivitySetNotificationTime extends ActivityBasic {

//	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    private TextView submitbtn;
    //	private AlarmModel alarmDetails;
    private TimePicker timePicker;
    private FButton chkSunday;
    private FButton chkMonday;
    private FButton chkTuesday;
    private FButton chkWednesday;
    private FButton chkThursday;
    private FButton chkFriday;
    private FButton chkSaturday;
    //	private TextView txtToneSelection;
    private FButton btnPlaybackVioce;
    private Timer timer;
    private TimerTask task;
    private boolean isPlayBack = false;// 设置正在回放
    private static String basicFolder;
    // NewAudioName可播放的音频文件
    private static String AudioName;
    private static String audioDirector;
    private List<String> contentString = new ArrayList<String>();
    private String time;
    private String date;//日期字符串，","隔开
    private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
    /**
     * 0:音频，1：视频，2：文字
     */
    private int chooseFalg = 0;

    private ImageView playVideo;
    // 视频播放
    private MediaPlayer mediaPlayer;
    private SurfaceView videoSurfaceView;
    private RecorderListener recorderListener;
    private FrameLayout recoderView;
    private TextView remindText;//recoder_tx_fl 文字提醒
    public ArrayList<FButton> list;
    public final static String PATH = "path";
    public String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        chooseFalg = getIntent().getIntExtra(Constant.REMIND_TYPE, -1);
        System.out.println("=-=-=chooseFalg=-=-=" + chooseFalg);
        path = getIntent().getStringExtra(ActivitySetNotificationTime.PATH);
        System.out.println("=-=-=path=-=-=" + path);
        audioDirector = AppDisk.appInursePath
                + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
                + File.separator + AppDisk.DCIM_RECORD;
        System.out.println("=-=-=audioDirector=-=-=" + audioDirector);
        contentString = CreateFolder.getAllFileNameInFolder(audioDirector);
        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                getApplicationContext());
        UtilsSharedData.initDataShare(this);
        basicFolder = AppDisk.appInursePath
                + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
                + File.separator;
        AudioName = basicFolder + AppDisk.DCIM_RECORD + "love.raw";
        System.out.println("=-=-=AudioName=-=-=" + AudioName);
        videoSurfaceView = (SurfaceView) findViewById(R.id.recoder_play);
        recoderView = (FrameLayout) findViewById(R.id.recoder_play_fl);
        playVideo = (ImageView) findViewById(R.id.playVideo1);
        remindText = (TextView) findViewById(R.id.recoder_tx_fl);

        playVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(path);
            }
        });
        TextView titile = (TextView) findViewById(R.id.middle_tv);
        titile.setText("设置提醒时间");
        submitbtn = (TextView) findViewById(R.id.right_tv);
        submitbtn.setText("提交");
        submitbtn.setVisibility(View.VISIBLE);
        submitbtn.setOnClickListener(this);
        timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        timePicker.setIs24HourView(true);

        list = new ArrayList<FButton>();
        chkSunday = (FButton) findViewById(R.id.alarm_details_repeat_sunday_bt);
        chkMonday = (FButton) findViewById(R.id.alarm_details_repeat_monday_tb);
        chkTuesday = (FButton) findViewById(R.id.alarm_details_repeat_tuesday_bt);
        chkWednesday = (FButton) findViewById(R.id.alarm_details_repeat_wednesday_bt);
        chkThursday = (FButton) findViewById(R.id.alarm_details_repeat_thursday_bt);
        chkFriday = (FButton) findViewById(R.id.alarm_details_repeat_friday_bt);
        chkSaturday = (FButton) findViewById(R.id.alarm_details_repeat_saturday_bt);
        btnPlaybackVioce = (FButton) findViewById(R.id.Btn_playback_vioce);

//		chkSunday.setButtonColor(Color.LTGRAY);
//		chkMonday.setButtonColor(Color.LTGRAY);
//		chkTuesday.setButtonColor(Color.LTGRAY);
//		chkWednesday.setButtonColor(Color.LTGRAY);
//		chkThursday.setButtonColor(Color.LTGRAY);
//		chkFriday.setButtonColor(Color.LTGRAY);
//		chkSaturday.setButtonColor(Color.LTGRAY);
        list.add(chkMonday);
        list.add(chkTuesday);
        list.add(chkWednesday);
        list.add(chkThursday);
        list.add(chkFriday);
        list.add(chkSaturday);
        list.add(chkSunday);

        chkSunday.setOnClickListener(this);
        chkMonday.setOnClickListener(this);
        chkTuesday.setOnClickListener(this);
        chkWednesday.setOnClickListener(this);
        chkThursday.setOnClickListener(this);
        chkFriday.setOnClickListener(this);
        chkSaturday.setOnClickListener(this);
        btnPlaybackVioce.setOnClickListener(this);
        playChoose(chooseFalg);
    }

    /**
     * 播放视频
     */
    public void playVideo(String path) {
        System.out.println("=-=-=播放路径=-=-=" + path);
        // surfaceView.setVisibility(View.GONE);
        // videoSurfaceView.setVisibility(View.VISIBLE);
        UtilsSharedData.initDataShare(this);
//		String basicFolder = AppDisk.appInursePath
//				+ UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
//				+ File.separator;
        playVideo.setVisibility(View.GONE);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
//			mediaPlayer.setDataSource(basicFolder + AppDisk.DCIM_VIDEO
//					+ "1.mp4");		
//			System.out.println("=-=-=basicFolder=-=-="+basicFolder + AppDisk.DCIM_VIDEO+ "1.mp4");
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(videoSurfaceView.getHolder());
            mediaPlayer.prepare();// 缓冲
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (recorderListener != null)
            recorderListener.videoStart();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (recorderListener != null)
                    recorderListener.videoStop();
                playVideo.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
//			updateModelFromLayout();
//			AlarmManagerReceiver.cancelAlarms(this);
//			if (alarmDetails.id < 0) {
//				dbHelper.createAlarm(alarmDetails);
//			} else {
//				dbHelper.updateAlarm(alarmDetails);
//			}
//			AlarmManagerReceiver.setAlarms(this);			
//			setResult(RESULT_OK);
                time = timeFormat(timePicker);
                System.out.println("=-=-=time=-=-=" + time);
                date = getRemindDate(list);
                if (date != null) {
                    System.out.println("=-=-=date=-=-=" + date);
                    saveToDatabase(chooseFalg);
                    String old = ActivityDiaryRemindList.class.getName();
                    backTo(old);
                }
                break;
            case R.id.Btn_playback_vioce:
                playChoose(chooseFalg);
                playVoice();
                break;
            case R.id.alarm_details_repeat_sunday_bt:
            case R.id.alarm_details_repeat_monday_tb:
            case R.id.alarm_details_repeat_tuesday_bt:
            case R.id.alarm_details_repeat_wednesday_bt:
            case R.id.alarm_details_repeat_thursday_bt:
            case R.id.alarm_details_repeat_friday_bt:
            case R.id.alarm_details_repeat_saturday_bt:
                if (((FButton) v).getButtonColor() == getResources().getColor(R.color.lightgray)) {
                    ((FButton) v).setButtonColor(Color.GREEN);
                } else {
                    ((FButton) v).setButtonColor(getResources().getColor(R.color.lightgray));
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author kuangtiecheng
     * 选中标志位颜色...用颜色来区分选中的日期
     */
    public String getRemindDate(ArrayList<FButton> list) {
        StringBuilder builder = new StringBuilder();
        Iterator<FButton> iterator = list.iterator();
        while (iterator.hasNext()) {
            FButton fbutton = iterator.next();
            if (fbutton.getButtonColor() == Color.GREEN) {
                builder.append("星期").append(fbutton.getText()).append(",");
            }
        }
        System.out.println("=-=-=builder=-=-=" + builder.toString());
        if (builder.length() == 0) {
            ToastUtil.showMessage("请选择提醒日期");
            return null;
        } else
            return builder.deleteCharAt(builder.length() - 1).toString();
    }

    /**
     * @author kuangtiecheng
     * 格式化输出时间："HH:mm"
     */
    public String timeFormat(TimePicker timePicker) {
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
     * 保存提醒信息到本地数据库
     */
    public void saveToDatabase(int chooseFalg2) {
        BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
        beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(Constant.USER_ID, 0));
        beanHealthDiaryLocal.setDaytime(time);
        beanHealthDiaryLocal.setDateday(date);
        beanHealthDiaryLocal.setId(null);
        beanHealthDiaryLocal.setValid(DataBaseHelper.VALID_FLAG);
        switch (chooseFalg2) {
            case 0://音频
                beanHealthDiaryLocal.setContent("音频提醒");
                if (path.length() > 0) {
                    beanHealthDiaryLocal.setPath(path);
                    beanHealthDiaryLocal.setContent(path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")));
                }
                break;
            case 1://视频
                beanHealthDiaryLocal.setContent("视频提醒");
                if (path.length() > 0) {
                    beanHealthDiaryLocal.setPath(path);
                    beanHealthDiaryLocal.setContent(path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")));
                }
                break;
            case 2://文字
                beanHealthDiaryLocal.setContent(UtilsSharedData.getValueByKey("textremind"));
                beanHealthDiaryLocal.setPath("");
                break;
            default:
                break;
        }
        System.out.println("=-=-=saveToDatabase beanHealthDiaryLocal=-=-=" + beanHealthDiaryLocal);
        DataBaseHelper.onInsert(this, beanHealthDiaryLocal);
        ArrayList<BeanHealthDiaryLocal> beanList = DataBaseHelper.onQuery(this, beanHealthDiaryLocal);
        BeanHealthDiaryLocal bean = beanList.get(beanList.size() - 1);
        String[] array = bean.getDateday().split(",");
        for (int j = 0; j < array.length; j++) {
            BeanAlarm beanAlarm = new BeanAlarm();
            beanAlarm.setDateday(array[j]);
            beanAlarm.setDaytime(bean.getDaytime());
            beanAlarm.setId(bean.getId());
            beanAlarm.setValid(DataBaseHelper.VALID_FLAG);
            DataBaseHelper.insertAlarm(this, beanAlarm);
        }
        DataBaseHelper.printAlarm(this, "alarm");
        try {
            AlarmUtils.setAlarm(this, bean);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @author kuangtiecheng
     * 打印数据表
     */
    public void printDatabase() {
        DataBaseHelper.print(this, "diary_remind");
    }

    private void playChoose(int chooseFalg2) {
        switch (chooseFalg2) {
            case 0://音频提醒
                btnPlaybackVioce.setVisibility(View.VISIBLE);
                recoderView.setVisibility(View.GONE);
                remindText.setVisibility(View.GONE);
                break;
            case 1://视频提醒
                btnPlaybackVioce.setVisibility(View.GONE);
                recoderView.setVisibility(View.VISIBLE);
                remindText.setVisibility(View.GONE);
                break;
            case 2://文字提醒
                btnPlaybackVioce.setVisibility(View.GONE);
                recoderView.setVisibility(View.GONE);
                remindText.setVisibility(View.VISIBLE);
                String textRemind = UtilsSharedData.getValueByKey("textremind");//content
                remindText.setText(textRemind);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mediaPlayer == null)
            return;
        mediaPlayer.setDisplay(null);
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void playVoice() {
        if (!CreateFolder.hasSDCard()) {
            showToastDialog("请插入sd卡进行回放");
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
            btnPlaybackVioce.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_emerald));
            isPlayBack = false;
        } else {
            audioRecorderAndPlaybackInterface.startPlayback(audioDirector
                    + contentString.get(contentString.size() - 1));
            System.out.println("=-=-=startPlayback=-=-=" + audioDirector
                    + contentString.get(contentString.size() - 1));

            btnPlaybackVioce.setText("停止音频回放");
            btnPlaybackVioce.setButtonColor(getResources().getColor(
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
                        task.cancel();
                        timer.cancel();
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
            btnPlaybackVioce.setText("开始音频回放");
            btnPlaybackVioce.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_emerald));
        }
    }

}
