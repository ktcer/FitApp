package com.cn.fit.ui.patient.main.healthpost.doctorinterview;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.Constant;

import java.net.URL;

public class VideoPlay extends ActivityBasic {
    private String urlStream = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/04/vts_01_4.m3u8";
    private VideoView myVideoView;
    private URL url;
    private Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_play);
        myVideoView = (VideoView) this.findViewById(R.id.my_video_view);
        MediaController mc = new MediaController(this);
//		mc.setVisibility(View.GONE);
        myVideoView.setMediaController(mc);
        myVideoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(final MediaPlayer arg0) {
                // TODO Auto-generated method stub
                runnable = new Runnable() {
                    public void run() {
                        if (!arg0.isPlaying()) {
                            showProgressBar();
                        } else {
                            hideProgressBar();
                        }
                        handler.postDelayed(runnable, 500);// 每0.5秒监听一次是否在播放视频
                    }
                };

            }
        });

        urlStream = getIntent().getExtras().getString(Constant.VEDIO_URL);
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

//		runnable = new Runnable() {
//		public void run() {
//			myVideoView.setOnInfoListener(new OnInfoListener() {
//			    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//			        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
//			        	showProgressBar();
//			        }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
//			            //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
//			            if(mp.isPlaying()){
////			            	hideProgressBar();
//			            	showProgressBar();
//			            }
//			        }
//			        return true;
//			    }
//			});
//			myVideoView.setOnPreparedListener(new OnPreparedListener() {
//			    public void onPrepared(MediaPlayer mp) { 
////			    	hideProgressBar();//缓冲完成就隐藏
//			    	showProgressBar();
//			    }
//			});
//			handler.postDelayed(runnable, 300);// 每0.5秒监听一次是否在播放视频
//		}
//	};

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
