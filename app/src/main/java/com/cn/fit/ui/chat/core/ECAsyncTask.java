/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.core;

import android.content.Context;
import android.os.AsyncTask;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public abstract class ECAsyncTask extends AsyncTask {

    private static final String TAG = "ECDemo.ECAsyncTask";
    private Context mContext;
    private long mThreadId = 0;

    public ECAsyncTask(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }


    /**
     * 是否网络请求
     *
     * @return
     */
    protected boolean isNetworkRequest() {
        return true;
    }


    public void runTask() {
        if (!isCancelled()) {
            cancel(true);
        }
        if (mThreadId <= 0L && !isNetworkRequest()) {
            return;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mContext == null) {
            cancel(true);
            ECResult exception = new ECResult();
            exception.throwable = new Exception();
            setResult(exception);
            return;
        }
        onAsyncTaskPreExecute();
    }

    private void setResult(ECResult result) {
        if (result.throwable == null) {
            onResult(result.obj);
            return;
        }
        if (result.throwable instanceof Exception) {
            onException((Exception) result.throwable);
            return;
        }
        onException(new Exception(result.throwable));
    }

    protected void onException(Exception exception) {
        if (exception == null) {
            return;
        }
        onError(R.string.errormsg_server);
        LogUtil.e(TAG, exception.getMessage());
    }

    public void onError(int resid) {
        ToastUtil.showMessage(resid);
    }

    public final boolean isIdle() {
        return (getStatus() != Status.RUNNING);
    }

    protected void onAsyncTaskPreExecute() {

    }

    protected void onResult(Object obj) {

    }


    public class ECResult {
        Object obj;
        Throwable throwable;
    }
}
