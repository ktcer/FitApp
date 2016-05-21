package com.cn.fit.ui.record.video;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthdiary.alarm.ActivitySetNotificationTime;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CreateFolder;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.PhoneUtil;

/**
 * 录制视频页面 chenkeliang
 */
public class ActivityVideoRecord extends ActivityBasic {

    private VideoRecorderView recoderView;
    private Button videoController;
    private TextView message, cancel;
    private ImageView submit;
    private boolean isCancel = false;
    private boolean isFinish = false;// 设置是否录视频
    private String filePath = "";
    /**
     * 0:音频，1：视频，2：文字
     */
    private int chooseFalg = 1;
    private String vedioPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);
        chooseFalg = getIntent().getIntExtra(Constant.REMIND_TYPE, -1);
        chooseFalg = 1;

//		vedioPath = AppDisk.appInursePath
//				+ UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
//				+ File.separator + AppDisk.DCIM_RECORD;
//		System.out.println("=-=-=onCreate  vedioPath=-=-="+vedioPath);
        recoderView = (VideoRecorderView) findViewById(R.id.recoder);
        videoController = (Button) findViewById(R.id.videoController);
        message = (TextView) findViewById(R.id.message);
        cancel = (TextView) findViewById(R.id.cancel_record);
        submit = (ImageView) findViewById(R.id.submit_record);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        ViewGroup.LayoutParams params = recoderView.getLayoutParams();
        int[] dev = PhoneUtil.getResolution(this);
        params.width = dev[0];
        params.height = (int) (((float) dev[0]));
        recoderView.setLayoutParams(params);
        videoController.setOnTouchListener(new VideoTouchListener());

        recoderView.setRecorderListener(new VideoRecorderView.RecorderListener() {

            @Override
            public void recording(int maxtime, int nowtime) {

            }

            @Override
            public void recordSuccess(File videoFile) {
                System.out.println("recordSuccess");
                if (videoFile != null) {
                    System.out.println(videoFile.getAbsolutePath());
                    filePath = videoFile.getAbsolutePath(); // 视频文件保存路径
                    Log.e("=-=-=filePath=-=-=", filePath);
                }
                releaseAnimations();
            }

            @Override
            public void recordStop() {
                System.out.println("recordStop");
            }

            @Override
            public void recordCancel() {
                System.out.println("recordCancel");
                releaseAnimations();
            }

            @Override
            public void recordStart() {
                System.out.println("recordStart");
            }

            @Override
            public void videoStop() {
                System.out.println("videoStop");
            }

            @Override
            public void videoStart() {
                System.out.println("videoStart");
            }
        });

    }

    public class VideoTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CreateFolder.hasSDCard()) {
                        showToastDialog("请插入sd卡录制视频");
                        return false;
                    }
                    recoderView.startRecord();
                    isCancel = false;
                    pressAnimations();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getX() > 0
                            && event.getX() < videoController.getWidth()
                            && event.getY() > 0
                            && event.getY() < videoController.getHeight()) {
                        showPressMessage();
                        isCancel = false;
                    } else {
                        cancelAnimations();
                        isCancel = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isCancel) {
                        recoderView.cancelRecord();
                    } else {
                        recoderView.endRecord();
                        System.out.println("=-=-=endRecord()=-=-=");
                        isFinish = true;
                        showSummarizeDialog();
                    }
                    message.setVisibility(View.GONE);
                    releaseAnimations();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 移动取消弹出动画
     */
    public void cancelAnimations() {
        message.setBackgroundColor(getResources().getColor(
                android.R.color.holo_red_light));
        message.setTextColor(getResources().getColor(android.R.color.white));
        message.setText("松手取消");
    }

    /**
     * 显示提示信息
     */
    public void showPressMessage() {
        message.setVisibility(View.VISIBLE);
        message.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        message.setTextColor(getResources().getColor(
                android.R.color.holo_green_light));
        message.setText("上移取消");
    }

    /**
     * 按下时候动画效果
     */
    public void pressAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        videoController.startAnimation(animationSet);
    }

    /**
     * 释放时候动画效果
     */
    public void releaseAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        message.setVisibility(View.GONE);
        videoController.startAnimation(animationSet);
    }

//	  //判断是否录视频
//    public void setClick(boolean flag){
//    	if(flag){
//    		submitbtn.setClickable(flag);
//    		Intent intent = new Intent(this, ActivitySetNotificationTime.class);
//        	intent.putExtra("chose", chooseFalg);
//        	intent.putExtra(ActivitySetNotificationTime.PATH, NewAudioName);
//			startActivity(intent);
//    	}else
//    		ToastUtil.showMessage("请先录音");
//    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.cancel_record:
                finish();
                break;
            case R.id.submit_record:
                // finish();
                // 提交视频
//			Intent intent = new Intent(this, ActivitySetNotificationTime.class);
//			intent.putExtra("choose", chooseFalg);
//			intent.putExtra(ActivitySetNotificationTime.PATH, vedioPath);
//			startActivity(intent);
                setClick(isFinish);
                break;
            default:
                break;
        }
    }

    //判断是否录视频
    public void setClick(boolean flag) {
        if (flag) {
            submit.setClickable(flag);
            if (vedioPath.length() == 0 || vedioPath == null) {
                vedioPath = reName("");
            }
            Intent intent = new Intent(this, ActivitySetNotificationTime.class);
            intent.putExtra(Constant.REMIND_TYPE, chooseFalg);
            intent.putExtra(ActivitySetNotificationTime.PATH, vedioPath);
            startActivity(intent);
        } else
            ToastUtil.showMessage("请先视频");
    }

    // 弹出重命名弹窗
    private void showSummarizeDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setHint("请输入您录制视频的名称");
        inputServer.setMinLines(8);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(this.getResources().getColor(
                R.color.font_gray));
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        inputServer.setFocusable(true);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vedioPath = reName(inputServer.getText().toString());
                System.out.println("=-=-=vedioPath=-=-=" + vedioPath);
//				String path = AppDisk.appInursePath
//						+ UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
//						+ File.separator;
//				System.out.println("=-=-=path=-=-="+path);
//				vedioPath = path + AppDisk.DCIM_RECORD
//						+ inputServer.getText() + ".mp4";
                // System.out.println("=-=-=NewAudioName=-=-="+NewAudioName);
                // new Thread(new AudioCopyThread()).start();
                // startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                // startPlayBack.setEnabled(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = "";
                vedioPath = reName(str);
                System.out.println("=-=-=vedioPath=-=-=" + vedioPath);
//				Calendar now = Calendar.getInstance();
//				SimpleDateFormat sdf = new SimpleDateFormat(
//						"yyyy-M-dd-HH:mm:ss");
                // NewAudioName =
                // basicFolder+AppDisk.DCIM_RECORD+sdf.format(now.getTime())+".wav";
                // new Thread(new AudioCopyThread()).start();
                // startPlayBack.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
                // startPlayBack.setEnabled(true);
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /**
     * 给视屏文件重命名
     */
    public String reName(String str) {
        File oldFile = new File(filePath);
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if (str.length() == 0 || str == null) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1)
                    + timeStamp + ".mp4";
        } else {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1)
                    + str + ".mp4";
        }
        System.out.println("=-=-=reName  filePath=-=-=" + filePath);
        File newFile = new File(filePath);
        oldFile.renameTo(newFile);
        return newFile.getAbsolutePath();
    }

}