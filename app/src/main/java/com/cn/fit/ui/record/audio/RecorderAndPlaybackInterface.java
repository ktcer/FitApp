package com.cn.fit.ui.record.audio;

public interface RecorderAndPlaybackInterface {
    boolean startRecording();

    boolean stopRecording();

    boolean startPlayback(String path);

    boolean pausePlayback();

    boolean resumePlayback();

    boolean stopPlayback();

    boolean isPlaying();

    boolean isPlayBackComplete();

    void recordingComplete(String filePath);

    void playbackComplete();

    void release();
}
