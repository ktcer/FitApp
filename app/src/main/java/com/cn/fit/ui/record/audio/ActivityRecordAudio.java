
package com.cn.fit.ui.record.audio;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthdiary.alarm.ActivitySetNotificationTime;
import com.cn.fit.util.AppDisk;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ResourceAsColor")
public class ActivityRecordAudio extends ActivityBasic implements OnClickListener {
    // 音频获取源
    private int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static int sampleRateInHz = 8000;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private int bufferSizeInBytes = 0;
    private AudioRecord audioRecord;
    private boolean isRecord = false;// 设置正在录制的状态
    private boolean isSubmit = false;//设置能否提交 
    private boolean isPlayBack = false;// 设置正在回放
    private static String basicFolder;
    // AudioName裸音频数据文件
    private static String AudioName;
    // NewAudioName可播放的音频文件
    private static String NewAudioName;
    private FButton startRecording;
    private FButton startPlayBack;
    private Timer timer;
    private TimerTask task;
    private TextView submitbtn;
    private ImageView mRecorderAni;
    private AnimationDrawable RecorderAni = null;
    private TextView mTimerView;
    private String mTimerFormat;
    private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;
    /**
     * 0:音频，1：视频，2：文字
     */
    private int chooseFalg = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiorecorder);
        Intent intent = getIntent();
        chooseFalg = intent.getIntExtra(Constant.REMIND_TYPE, -1);
        submitbtn = (TextView) findViewById(R.id.right_tv);
        mRecorderAni = (ImageView) findViewById(R.id.recorder_ani);
        mTimerView = (TextView) findViewById(R.id.timerView);
        mTimerView.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/UnidreamLED.ttf"));
        mTimerFormat = getResources().getString(R.string.timer_format);
        submitbtn.setText("提交");
        submitbtn.setVisibility(View.VISIBLE);
        submitbtn.setOnClickListener(this);
        TextView titile = (TextView) findViewById(R.id.middle_tv);
        titile.setText("录制音频提醒");
        startRecording = (FButton) findViewById(R.id.Btn_Record);
        startRecording.setCornerRadius(3);
        startRecording.setOnClickListener(this);
        startPlayBack = (FButton) findViewById(R.id.Btn_playback);
        startPlayBack.setCornerRadius(3);
        startPlayBack.setOnClickListener(this);
        UtilsSharedData.initDataShare(this);
        basicFolder = AppDisk.appInursePath + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT) + File.separator;
        AudioName = basicFolder + AppDisk.DCIM_RECORD + "love.raw";
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//预设命名
        NewAudioName = basicFolder + AppDisk.DCIM_RECORD + timeStamp + ".wav";
        creatAudioRecord();
        audioRecorderAndPlaybackInterface = new RecorderAndPlaybackMediaRecorderImpl(
                getApplicationContext());
        startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
        startPlayBack.setEnabled(false);
        RecorderAni = (AnimationDrawable) mRecorderAni.getBackground();
        RecorderAni.setAlpha(90);
        mTimerView.setAlpha(0.35f);
    }

    /**
     * Update the big MM:SS timer. If we are in playback, also update the
     * progress bar.
     * 更新录音时间
     */
    long time = (long) 0;

    private void updateTimerView() {
        if (isRecord) {
            time++;
            mHandler.postDelayed(mUpdateTimer, 1000);
        } else {
            time = 0;
        }
        String timeStr = String.format(mTimerFormat, time / 3600, (time / 60) % 60, time % 60);
        mTimerView.setText(timeStr);
    }

    Runnable mUpdateTimer = new Runnable() {
        public void run() {
            updateTimerView();
        }
    };

    //判断是否录音
    public void setClick(boolean flag) {
        if (flag) {
            submitbtn.setClickable(flag);
            Intent intent = new Intent(this, ActivitySetNotificationTime.class);
            intent.putExtra(Constant.REMIND_TYPE, chooseFalg);
            intent.putExtra(ActivitySetNotificationTime.PATH, NewAudioName);
            startActivity(intent);
        } else
            ToastUtil.showMessage("请先录音");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.Btn_Record:
                if (!CreateFolder.hasSDCard()) {
                    showToastDialog("请插入sd卡进行录音");
                    return;
                }
                if (isRecord) {
                    showSummarizeDialog();
                    stopRecord();
                    mRecorderAni.startAnimation(AnimationUtils.loadAnimation(this, R.anim.stop_record));
                    RecorderAni.stop();
                    RecorderAni.setAlpha(90);
                    mTimerView.setAlpha(0.35f);
                    startRecording.setText("开始录音");
                    startRecording.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                } else {
                    startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
                    startPlayBack.setEnabled(false);
                    startRecord();
                    mHandler.post(mUpdateTimer);
                    mRecorderAni.startAnimation(AnimationUtils.loadAnimation(this, R.anim.start_record));
                    RecorderAni.start();
                    RecorderAni.setAlpha(255);
                    mTimerView.setAlpha(1);
                    startRecording.setText("停止录音");
                    startRecording.setButtonColor(getResources().getColor(R.color.fbutton_color_orange));
                }

                break;
            case R.id.Btn_playback:
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
                    audioRecorderAndPlaybackInterface.stopPlayback();
                    startPlayBack.setText("开始试听");
                    startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                    isPlayBack = false;
                } else {
                    audioRecorderAndPlaybackInterface.startPlayback(NewAudioName);
                    startPlayBack.setText("停止试听");
                    startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_orange));
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
                break;
            case R.id.right_tv:
//    			finish();
                setClick(isSubmit);

//            	Intent intent = new Intent(this, ActivitySetNotificationTime.class);
//            	intent.putExtra("chose", chooseFalg);
//            	intent.putExtra(ActivitySetNotificationTime.PATH, NewAudioName);
//    			startActivity(intent);
                break;

            default:
                break;
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
            startPlayBack.setText("开始试听");
            startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioRecorderAndPlaybackInterface.release();
    }

    // 弹出重命名弹窗
    private void showSummarizeDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setHint("爱护，音频提醒");
        inputServer.setMinLines(8);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(getResources().getColor(R.color.font_gray));
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("请为您录制的音频命名");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewAudioName = basicFolder + AppDisk.DCIM_RECORD + inputServer.getText() + ".wav";
                System.out.println("=-=-=NewAudioName=-=-=" + NewAudioName);
                new Thread(new AudioCopyThread()).start();
                startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                startPlayBack.setEnabled(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                NewAudioName = basicFolder + AppDisk.DCIM_RECORD + sdf.format(new Date()) + ".wav";
                new Thread(new AudioCopyThread()).start();
                startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                startPlayBack.setEnabled(true);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void creatAudioRecord() {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord
                .getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        // 创建AudioRecord对象
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat,
                bufferSizeInBytes);
    }

    private void startRecord() {
        if (null == audioRecord) {
            creatAudioRecord();
        }
        audioRecord.startRecording();
        // 让录制状态为true
        isRecord = true;
        // 开启音频文件写入线程
        new Thread(new AudioRecordThread()).start();
    }

    private void stopRecord() {
        close();
    }

    private void close() {
        if (audioRecord != null) {
            System.out.println("stopRecord");
            isRecord = false;// 停止文件写入
            audioRecord.stop();
            audioRecord.release();// 释放资源
            audioRecord = null;
            isSubmit = true;// 可以提交了
        }
    }

    class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            writeDateTOFile();// 往文件中写入裸数据
        }
    }

    class AudioCopyThread implements Runnable {
        @Override
        public void run() {
            copyWaveFile(AudioName, NewAudioName);// 给裸数据加上头文件
        }

    }

    /**
     * 这里将数据写入文件，但是并不能播放，因为AudioRecord获得的音频是原始的裸音频，
     * 如果需要播放就必须加入一些格式或者编码的头信息。但是这样的好处就是你可以对音频的 裸数据进行处理，比如你要做一个爱说话的TOM
     * 猫在这里就进行音频的处理，然后重新封装 所以说这样得到的音频比较容易做一些音频的处理。
     */
    private void writeDateTOFile() {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        byte[] audiodata = new byte[bufferSizeInBytes];
        FileOutputStream fos = null;
        int readsize = 0;
        try {
            File file = new File(AudioName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);// 建立一个可存取字节的文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (isRecord == true) {
            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                try {
                    fos.write(audiodata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            fos.close();// 关闭写入流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 这里得到可播放的音频文件
    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = sampleRateInHz;
        int channels = 2;
        channels = 1;
        long byteRate = 16 * sampleRateInHz * channels / 8;
        // byteRate=byteRate/8;
        byte[] data = new byte[bufferSizeInBytes];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels,
                    byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //删除raw文件
        File file = new File(inFilename);
        file.delete();
    }

    /**
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有 自己特有的头文件。
     */
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen,
                                     long longSampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }
}
