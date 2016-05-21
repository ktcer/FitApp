package com.cn.fit.ui.chat.common;

import android.os.Handler;
import android.os.Looper;

import com.cn.fit.ui.chat.common.utils.LogUtil;

/**
 * Created by Jorstin on 2015/3/13.
 */
public final class CallCommandHandlerImpl extends Thread implements
        CallCommandHandler {

    private static final String TAG = "ECSDK.CallCommandHandlerImpl";
    private Looper looper;

    private Handler callHandler;

    CallCommandHandlerImpl() {
        start();
    }

    @Override
    public void destroy() {
        try {
            if (looper != null) {
                looper.quit();
            }
            callHandler = null;
            interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postCommand(Runnable command) {
        if (this.callHandler != null && currentThread().isAlive()) {
            this.callHandler.post(command);
            LogUtil.d(TAG, "[CallCommandHandlerImpl - postCommand] post command finish.");
        }
    }

    public void postCommand(Runnable command, long delay) {
        if (this.callHandler != null && currentThread().isAlive()) {
            this.callHandler.postDelayed(command, delay);
            LogUtil.d(TAG, "[CallCommandHandlerImpl - postCommand] post command finish.");
        }
    }

    public void run() {
        try {
            Thread.currentThread().setName("CallCommandHandlerImpl");
            LogUtil.d(TAG, "[CallCommandHandlerImpl - run] thread already running: "
                    + Thread.currentThread().getName());
            Looper.prepare();
            this.looper = Looper.myLooper();
            this.callHandler = new Handler();
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            destroy();
            LogUtil.d(TAG, "CallCommandHandlerImpl thread was destroyed.");
        }
    }

    @Override
    public Handler getCommandHandler() {
        if (callHandler == null) {
            LogUtil.e(TAG, "[CallCommandHandlerImpl - getCommandHandler] can't get command handler, it's null, recreate it?");
        }
        return callHandler;
    }
}

