package com.cn.fit.ui.chat.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * com.cn.aihu.ui.chat.common.utils in ECDemo_Android
 * Created by Jorstin on 2015/3/24.
 */
public class CoreHandler extends Handler {

    /**
     * self-propagation messaage what
     */
    private static int HANDLER_WHAT;
    /**
     * Uniquely identifies the message
     */
    private final int mWhat;


    private long mDelayMillis = 0L;
    private final boolean mHandle;
    private final HandlerCallbck mHandlerCallbck;

    /**
     * @param looper
     * @param callbck
     * @param handle
     */
    public CoreHandler(Looper looper, HandlerCallbck callbck, boolean handle) {
        super(looper);
        mWhat = createWhat();
        mHandlerCallbck = callbck;
        mHandle = handle;
    }

    /**
     * @param callbck
     * @param handle
     */
    public CoreHandler(HandlerCallbck callbck, boolean handle) {
        mWhat = createWhat();
        mHandlerCallbck = callbck;
        mHandle = handle;
    }

    /**
     * Unique production news message what
     *
     * @return
     */
    private static int createWhat() {
        if (HANDLER_WHAT > 8192) {
            HANDLER_WHAT = 0;
        }
        HANDLER_WHAT += 1;
        return HANDLER_WHAT;
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    public void removeMessages() {
        removeMessages(mWhat);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public boolean hasMessages() {
        return hasMessages(mWhat);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     */
    public void sendEmptyMessageDelayed(long delayMillis) {
        mDelayMillis = delayMillis;
        removeMessages();
        sendEmptyMessageDelayed(mWhat, delayMillis);

    }

    @Override
    protected void finalize() throws Throwable {
        removeMessages();
        super.finalize();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what != mWhat && mHandlerCallbck == null) {
            return;
        }

        if (!mHandlerCallbck.dispatchMessage() || !mHandle) {
            return;
        }

        sendEmptyMessageDelayed(mWhat, mDelayMillis);
    }


    public interface HandlerCallbck {
        public abstract boolean dispatchMessage();
    }
}
