package com.cn.fit.ui.record.audio;

import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;

public class RecorderAndPlaybackMediaRecorderImpl extends
        RecorderAndPlaybackAudioRecorderImpl {
    private MediaRecorder mediaRecorder;

    public RecorderAndPlaybackMediaRecorderImpl(Context context) {
        super(context);
    }

    @Override
    public boolean startRecording() {
        if (null != mediaRecorder) {
            stopRecording();
        }

        requestAudioFocus();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setMaxDuration(MAX_DURATION_MSEC);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(getAudioTmpFilesPath());
        mediaRecorder.setOnErrorListener(new OnErrorListener() {

            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                log("what=" + what + ",extra=" + extra);
            }
        });
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean stopRecording() {
        if (null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        return true;
    }

    @Override
    public void release() {
        super.release();

        if (null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

}
