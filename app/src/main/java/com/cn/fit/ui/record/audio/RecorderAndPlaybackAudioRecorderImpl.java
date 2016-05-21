package com.cn.fit.ui.record.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class RecorderAndPlaybackAudioRecorderImpl implements
        RecorderAndPlaybackInterface {
    private static final String TAG = RecorderAndPlaybackAudioRecorderImpl.class
            .getSimpleName();

    //	protected static final int RECORDER_BPP = 16;
//	protected static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    protected static final String AUDIO_RECORDER_FOLDER = "EFAudioRecorder";
    protected static final String AUDIO_RECORDER_TEMP_FILE = "record";
    protected static final int RECORDER_SAMPLERATE = 44100;
    protected static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    protected static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    protected static final int MAX_DURATION_MSEC = 60 * 1000;

    // private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    private Context context;
    private AudioRecord audioRecord;

    protected File audioFile;

    private MediaPlayer mediaPlayer;
    protected int playerStatus = PlayerConstants.STATUS_INVALID;

    public RecorderAndPlaybackAudioRecorderImpl(Context context) {
        this.context = context;
        audioFile = createAudioTmpFiles();

        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean startRecording() {
        log("startRecording");
        if (null != audioRecord) {
            isRecording = false;

            int i = audioRecord.getState();
            if (i == 1) {
                audioRecord.stop();
            }
            audioRecord.release();

            audioRecord = null;
            recordingThread = null;
        }

        // audioRecord = new AudioRecord(audioSource, sampleRateInHz,
        // channelConfig, audioFormat, bufferSizeInBytes);
        requestAudioFocus();
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        bufferSize = bufferSize * 2;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);
        int i = audioRecord.getState();

        if (i == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
            isRecording = true;

            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // write audio file.
                    saveAudioRecorderFile();

                }
            }, "audio recorder");
            recordingThread.start();

        } else if (i == AudioRecord.STATE_UNINITIALIZED) {
            return false;
        }

        return true;
    }

    private void saveAudioRecorderFile() {
        log("saveAudioRecorderFile");
        byte[] data = new byte[bufferSize];
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(audioFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (null == outputStream) {
            return;
        }

        int read = 0;

        while (isRecording) {
            read = audioRecord.read(data, 0, bufferSize);
            if (AudioRecord.ERROR_INVALID_OPERATION == read
                    || AudioRecord.ERROR_BAD_VALUE == read) {
                // occur errors.
                break;
            } else {
                try {
                    outputStream.write(data, 0, read);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createAudioTmpFiles() {
        String path = getAudioTmpFilesPath();

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return file;
    }

    protected String getAudioTmpFilesPath() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        // File filesDir = context.getApplicationContext().getFilesDir();
        // filepath = filesDir.getPath();

        File dir = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir.getAbsolutePath() + File.separator
                + AUDIO_RECORDER_TEMP_FILE;
    }

    @Override
    public boolean stopRecording() {
        log("stopRecording");
        if (null != audioRecord) {
            isRecording = false;

            int i = audioRecord.getState();
            if (i == 1) {
                audioRecord.stop();
            }
            audioRecord.release();

            audioRecord = null;
            recordingThread = null;
        }

        recordingComplete(audioFile.getPath());

        return true;
    }

    @Override
    public boolean isPlaying() {
        return PlayerConstants.STATUS_STARTED == playerStatus;
    }

    @Override
    public boolean startPlayback(String audioPath) {
        log("startPlayback");
        mediaPlayer = createMediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                log("onError,what=" + what + ",extra=" + extra);
                playerStatus = PlayerConstants.STATUS_ERROR;
                return false;
            }
        });

        mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                playerStatus = PlayerConstants.STATUS_PREPARED;
                mediaPlayer.start();
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playbackComplete();
                releaseMediaPlayer();
            }
        });

        try {
//			String path = "file://" + getAudioTmpFilesPath();
//			path = "file:///sdcard/new.wav";
            log("startPlayback,path=" + audioPath);
            mediaPlayer.setDataSource(audioPath);
            playerStatus = PlayerConstants.STATUS_INITIALIZED;
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private MediaPlayer createMediaPlayer() {
        log("createMediaPlayer");
        MediaPlayer player = null;
        if (null != mediaPlayer) {
            releaseMediaPlayer();
            player = new MediaPlayer();
            player.setScreenOnWhilePlaying(true);

        } else {
            player = new MediaPlayer();
            player.setScreenOnWhilePlaying(true);
        }

        return player;
    }

    private void releaseMediaPlayer() {
        log("releaseMediaPlayer");
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
            playerStatus = PlayerConstants.STATUS_INVALID;
        }
    }

    @Override
    public boolean stopPlayback() {
        log("stopPlayback");
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            releaseMediaPlayer();
            playerStatus = PlayerConstants.STATUS_STOPPED;
        }

        return true;
    }

    @Override
    public boolean pausePlayback() {
        log("stopPlayback");
        if (null != mediaPlayer) {
            mediaPlayer.pause();
            playerStatus = PlayerConstants.STATUS_PAUSED;
        }

        return true;
    }

    @Override
    public boolean resumePlayback() {
        log("stopPlayback");
        if (null != mediaPlayer) {
            mediaPlayer.start();
            playerStatus = PlayerConstants.STATUS_STARTED;
        }

        return true;
    }

    @Override
    public void recordingComplete(String filePath) {
        log("recordingComplete");
    }

    @Override
    public void playbackComplete() {
        playerStatus = PlayerConstants.STATUS_PLACKBACK_COMPLETED;
        log("playbackComplete");
    }

    protected void log(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void release() {
        if (null != audioRecord) {
            isRecording = false;

            int i = audioRecord.getState();
            if (i == 1) {
                audioRecord.stop();
            }
            audioRecord.release();

            audioRecord = null;
            recordingThread = null;
        }

        removeAudioFocus();
        releaseMediaPlayer();
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Interface definition for a callback to be invoked when the audio focus of
     * the system is updated.
     */
    public interface OnAudioFocusChangeListener {
        /**
         * Called on the listener to notify it the audio focus for this listener
         * has been changed. The focusChange value indicates whether the focus
         * was gained, whether the focus was lost, and whether that loss is
         * transient, or whether the new focus holder will hold it for an
         * unknown amount of time. When losing focus, listeners can use the
         * focus change information to decide what behavior to adopt when losing
         * focus. A music player could for instance elect to lower the volume of
         * its music stream (duck) for transient focus losses, and pause
         * otherwise.
         *
         * @param focusChange the type of focus change, one of
         *                    {@link AudioManager#AUDIOFOCUS_GAIN},
         *                    {@link AudioManager#AUDIOFOCUS_LOSS},
         *                    {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT} and
         *                    {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}.
         */
        public void onAudioFocusChange(int focusChange);
    }

    OnAudioFocusChangeListener mOnAudioFocusChangeListener = new OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // resume playback
                    log("AUDIOFOCUS_GAIN,startPlayer()");
                    // startPlayer();

                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                    // Lost focus for an unbounded amount of time: stop playback
                    // and release media player
                    // stopPlayer();
                    log("AUDIOFOCUS_LOSS,pausePlayer()");
                    // pausePlayer();
                    pausePlayback();

                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Lost focus for a short time, but we have to stop
                    // playback. We don't release the media player because
                    // playback
                    log("AUDIOFOCUS_LOSS_TRANSIENT,pausePlayer()");
                    pausePlayback();

                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Lost focus for a short time, but it's ok to keep playing
                    // at an attenuated level
                    log("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    pausePlayback();

                    break;
            }
        }
    };

    AudioFocusHelper mAudioFocusHelper;

    /**
     * 开始播放时需要调用
     */
    protected void requestAudioFocus() {
        if (android.os.Build.VERSION.SDK_INT < 8) {
            mAudioFocusHelper = null;

        } else {
            if (null == mAudioFocusHelper) {
                mAudioFocusHelper = new AudioFocusHelper(context,
                        mOnAudioFocusChangeListener);
            }

            mAudioFocusHelper.requestFocus();

        }
    }

    /**
     * 暂停、停止播放时需要调用
     */
    protected void removeAudioFocus() {
        if (android.os.Build.VERSION.SDK_INT < 8) {
        } else {

            if (null != mAudioFocusHelper) {
                mAudioFocusHelper.abandonFocus();
            }
        }
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        private boolean mConsumed = true;

        //
        // private void log(String tag, String msg) {
        // log(tag, msg);
        // }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // log(TAG, "onCallStateChanged,state=" + state + ",incomingNumber="
            // + incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // log(TAG, "CALL_STATE_IDLE");
                    if (!mConsumed) {
                        resumePlayback();
                        mConsumed = true;
                    }

                    break;

                case TelephonyManager.CALL_STATE_RINGING: {
                    // log(TAG, "CALL_STATE_RINGING");
                    mConsumed = false;
                    pausePlayback();

                    break;
                }

                default:
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    };

    @Override
    public boolean isPlayBackComplete() {
        // TODO Auto-generated method stub
        return PlayerConstants.STATUS_INVALID == playerStatus;
    }

}
