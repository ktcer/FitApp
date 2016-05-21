package com.cn.fit.ui.patient.others.myaccount;

import java.util.Random;

import android.os.Handler;

import com.cn.fit.util.progressbutton.ProcessButton;

public class ProgressGenerator {

    // public interface OnCompleteListener {
    //
    // public void onComplete();
    // }
    //
    // private OnCompleteListener mListener;
    private int mProgress;
    private boolean isStart = false;
    private ProcessButton button;

    // public ProgressGenerator(OnCompleteListener listener) {
    // mListener = listener;
    // }
    public ProgressGenerator(ProcessButton button) {
        this.button = button;
    }

    public void start() {
        isStart = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStart) {
                    mProgress += 5;
                    button.setProgress(mProgress);
                    if (mProgress < 95) {
                        handler.postDelayed(this, generateDelay());
                    } else {
                        mProgress = 0;
                        handler.postDelayed(this, generateDelay());
                        // mListener.onComplete();
                    }
                }
            }
        }, generateDelay());
    }

    public void stop() {
        isStart = false;
        mProgress = 0;
        button.setProgress(mProgress);
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}
