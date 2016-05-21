package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("ResourceAsColor")
public class ActivityEditNotificationTime extends ActivityBasic {

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
    private EditText remindText;//recoder_tx_fl 文字提醒
    public ArrayList<FButton> list;
    public final static String PATH = "path";
    //	public LinearLayout edit_delete;
    public FButton edit, delete;
    public BeanHealthDiaryLocal bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification_time);

//		edit_delete = (LinearLayout)findViewById(R.id.edit_delete);
//		edit_delete.setVisibility(View.VISIBLE);
        edit = (FButton) findViewById(R.id.edit);
        edit.setOnClickListener(this);
        delete = (FButton) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        bean = getIntent().getParcelableExtra(ActivityDiaryRemindList.DIARY_LOCAL);
        System.out.println("=-=-= ActivityEditNotificationTime  bean =-=-=" + bean);
        chooseFalg = AlarmUtils.getRemindType(bean);
        System.out.println("=-=-=chooseFalg=-=-=" + chooseFalg);

        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                getApplicationContext());

        videoSurfaceView = (SurfaceView) findViewById(R.id.recoder_play);
        recoderView = (FrameLayout) findViewById(R.id.recoder_play_fl);
        playVideo = (ImageView) findViewById(R.id.playVideo1);
        remindText = (EditText) findViewById(R.id.recoder_tx_fl);

        playVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(bean.getPath());
            }
        });
        TextView titile = (TextView) findViewById(R.id.middle_tv);
        titile.setText("编辑提醒时间");
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

        chkSunday.setOnClickListener(this);
        chkMonday.setOnClickListener(this);
        chkTuesday.setOnClickListener(this);
        chkWednesday.setOnClickListener(this);
        chkThursday.setOnClickListener(this);
        chkFriday.setOnClickListener(this);
        chkSaturday.setOnClickListener(this);
        btnPlaybackVioce.setOnClickListener(this);

        list.add(chkMonday);
        list.add(chkTuesday);
        list.add(chkWednesday);
        list.add(chkThursday);
        list.add(chkFriday);
        list.add(chkSaturday);
        list.add(chkSunday);

        try {
            initUnclickable(bean);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        playChoose(chooseFalg);
    }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if (resultCode == RESULT_OK) {
//			switch (requestCode) {
//			case 1: {
//				alarmDetails.alarmTone = data
//						.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//				txtToneSelection.setText(RingtoneManager.getRingtone(this,
//						alarmDetails.alarmTone).getTitle(this));
//				break;
//			}
//			default: 
//				break;			
//			}
//		}
//	}

    /**
     * 播放视频
     */
    public void playVideo(String path) {
        System.out.println("=-=-=播放路径=-=-=" + path);
        UtilsSharedData.initDataShare(this);
        playVideo.setVisibility(View.GONE);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
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
                time = AlarmUtils.timeFormat(timePicker);
                System.out.println("=-=-=time=-=-=" + time);
                date = getRemindDate(list);
                if (date != null) {
                    System.out.println("=-=-=date=-=-=" + date);
                    try {
                        saveToDatabase(chooseFalg);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
            case R.id.edit:
                setFbtnClickable(true);
                break;
            case R.id.delete:
                showComfirmCompleteDialog();
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
     * @throws ParseException
     * @author kuangtiecheng
     * 保存提醒信息到本地数据库
     */
    public void saveToDatabase(int chooseFalg2) throws ParseException {
        bean.setDateday(date);
        bean.setDaytime(time);

        if (chooseFalg2 == AlarmUtils.TEXT) {
            bean.setContent(remindText.getText().toString());
        }
        DataBaseHelper.onUpdate(this, bean);
        AlarmUtils.cancelAlarm(this, bean);
        List<BeanAlarm> beanList = DataBaseHelper.queryAlarm(this, bean.getId());
        for (int i = 0; i < beanList.size(); i++) {
            DataBaseHelper.deleteAlarm(this, beanList.get(i));
        }
//		BeanHealthDiaryLocal bean = beanList.get(beanList.size()-1);
//		DataBaseHelper.deleteAlarm(this, bean);
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
//		case 0://音频提醒
            case AlarmUtils.AUDIO://音频提醒
                btnPlaybackVioce.setVisibility(View.VISIBLE);
                recoderView.setVisibility(View.GONE);
                remindText.setVisibility(View.GONE);
                break;
//		case 1://视频提醒
            case AlarmUtils.VIDEO://视频提醒
                btnPlaybackVioce.setVisibility(View.GONE);
                recoderView.setVisibility(View.VISIBLE);
                remindText.setVisibility(View.GONE);
                break;
//		case 2://文字提醒
            case AlarmUtils.TEXT://文字提醒
                btnPlaybackVioce.setVisibility(View.GONE);
                recoderView.setVisibility(View.GONE);
                remindText.setVisibility(View.VISIBLE);
                remindText.setText(bean.getContent());
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
            btnPlaybackVioce.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_emerald));
            isPlayBack = false;
            btnPlaybackVioce.setText("开始音频回放");
        } else {
            audioRecorderAndPlaybackInterface.startPlayback(bean.getPath());
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

//	/**
//	 * @author kuangtiecheng
//	 * 判别类型
//	 *
//	 */
//	public int getRemindType(BeanHealthDiaryLocal bean){
//		int remindType = -1;
//		if(bean!=null){
//			String path = bean.getPath();
//			if (path != null && path.endsWith(".mp4")) {
//				remindType = 1; 
//			}else if(path != null && path.endsWith(".wav")){				
//				remindType = 0; 
//			}else if(path == null||path.length()==0){
//				remindType = 2; 				
//			}			
//		}
//		return remindType;
//	}

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 按id删除某条日记提醒
     */
    public void deleteItem(BeanHealthDiaryLocal beanHealthDiaryLocal) throws ParseException {
        bean.setDaytime(null);//按时间点删除
        AlarmUtils.cancelAlarm(getApplicationContext(), beanHealthDiaryLocal);
        DataBaseHelper.onDelete(this, bean);
//		AlarmManagerReceiver.cancelAlarms(this,""+bean.getDiaryid());

        String old = ActivityDiaryRemindList.class.getName();
        backTo(old);
    }

    /**
     * @author 丑旦
     * 删除确认框
     */
    public void showComfirmCompleteDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("是否删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    deleteItem(bean);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 初始化不可编辑状态
     */
    public void initUnclickable(BeanHealthDiaryLocal bean) throws ParseException {
        String dateDay = bean.getDateday().replaceAll("星期", "");
        System.out.println("=-=-=dateDay=-=-=" + dateDay);
        for (FButton fButton : list) {
            System.out.println("=-=-=fButton.getText()=-=-=" + fButton.getText());
            if (dateDay.contains(fButton.getText())) {
                fButton.setButtonColor(Color.GREEN);
            } else {
                fButton.setButtonColor(getResources().getColor(R.color.lightgray));
            }
        }

        Calendar calendar = AlarmUtils.getTime(bean.getDaytime());
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        setFbtnClickable(false);
    }

    /**
     * @throws ParseException
     * @author kuangtiecheng
     * 切换编辑状态
     */
    public void setFbtnClickable(boolean flag) {
        timePicker.setEnabled(flag);
        submitbtn.setEnabled(flag);
        remindText.setEnabled(flag);
        remindText.setCursorVisible(flag);
        for (FButton fButton : list) {
            fButton.setEnabled(flag);
        }

//		if (flag) {
//			playVideo.setEnabled(flag);
//			btnPlaybackVioce.setEnabled(flag);
//			remindText.setEnabled(flag);
//		}
    }
//	
//	/**
//	 * @author kuangtiecheng
//	 * 解析HH:mm时间
//	 * @throws ParseException 
//	 */
//	public Calendar getCalendar(String time) throws ParseException{
//		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
//		Date date = sf.parse(time);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		return calendar;
//	}
}
