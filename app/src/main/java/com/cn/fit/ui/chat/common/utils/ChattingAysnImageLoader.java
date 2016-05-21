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
package com.cn.fit.ui.chat.common.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.cn.fit.R;
import com.cn.fit.ui.chat.storage.ImgInfoSqlManager;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.chat.ui.chatting.ImageMsgInfoEntry;
import com.cn.fit.ui.chat.ui.chatting.model.ImgInfo;
import com.cn.fit.util.FileUtils;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;


/**
 * 缓存加载图片
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2015-1-4
 */
public class ChattingAysnImageLoader {

    private static ChattingAysnImageLoader mImageLoader;

    private Context mContext;
    private ChattingImageCallBack callBack;
    public HashMap<String, Integer> imageHeight = new HashMap<String, Integer>();

    private final byte[] LOCKED = new byte[0];

    // the worker thread and a done flag so we know when to exit
    private boolean mDone;
    private Thread mDecodeThread;
    private Stack<QueueEntry> TASK_QUEUE = null;
    private Set<String> TASK_QUEUE_INDEX = null;

    private int mScreenWidth;

    public int getmScreenWidth() {
        if (mScreenWidth <= 0) {
            mScreenWidth = DensityUtil.getImageWeidth(mContext, 1.0F) - DensityUtil.getDisplayMetrics(mContext, 40F);
            mScreenWidth = mScreenWidth / 4;
        }
        return mScreenWidth;
    }

    public HashMap<String, Bitmap> IMG_CACHE_INDEX = null;


    public Bitmap getImage(String key) {
        //Bitmap bitMap = null;
        synchronized (LOCKED) {
            if (IMG_CACHE_INDEX != null && IMG_CACHE_INDEX.containsKey(key)) {
                Bitmap bitmap = IMG_CACHE_INDEX.get(key);
                if (bitmap != null) {
                    LogUtil.d("[ChattingAysnImageLoader]loading from cache " + key);
                    return bitmap;
                } else {
                    IMG_CACHE_INDEX.remove(key);
                }
            }
            return null;
        }
    }

    public void removeKeyCache(String key) {
        if (key != null && !"".equals(key.trim())) {
            if (TASK_QUEUE_INDEX != null && TASK_QUEUE_INDEX.contains(key)) {
                TASK_QUEUE_INDEX.remove(key);
                LogUtil.d("[ChattingAysnImageLoader] remove task key from tsask queue");
            }
        }
    }

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (callBack != null) {
                    callBack.onChattingImageLoaded();
                }
            } else {
                ToastUtil.showMessage(R.string.imgdownload_fail);
            }
        }
    };


    public ChattingAysnImageLoader(Context context) {
        this.mContext = context;
        checkBean();
        // 通过图片路径,图片大小
        start();
    }

    private void checkBean() {
        // 初始化创建图片线程,并等待处理
        if (TASK_QUEUE == null) {
            TASK_QUEUE = new Stack<QueueEntry>(); // 线程请求创建图片的队列
        }
        if (TASK_QUEUE_INDEX == null) {
            TASK_QUEUE_INDEX = new HashSet<String>(); // 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列
        }
        if (IMG_CACHE_INDEX == null) {
            IMG_CACHE_INDEX = new HashMap<String, Bitmap>(12);
        }
    }

    private void start() {
        checkBean();
        if (mDecodeThread != null) {
            return;
        }
        mDone = false;
        Thread t = new Thread(new WorkerThread());
        t.setDaemon(true);
        t.setName("image-loader");
        mDecodeThread = t;
        t.start();
    }

    public void stop() {
        cleanTask();
        synchronized (TASK_QUEUE) {
            mDone = true;
            TASK_QUEUE.notifyAll();
        }
        if (mDecodeThread != null) {
            try {
                Thread t = mDecodeThread;
                mDecodeThread = null;
                LogUtil.d("[ChattingAysnImageLoader] mDecodeThread : " + mDecodeThread);
            } catch (Exception ex) {
            }
        }

        TASK_QUEUE_INDEX = null;
        IMG_CACHE_INDEX = null;

        mImageLoader = null;
    }

    private class WorkerThread implements Runnable {

        // Pick off items on the queue, one by one, and compute their bitmap.
        // Place the resulting bitmap in the cache, then call back by executing
        // the given runnable so things can get updated appropriately.
        public void run() {
            while (true) {
                QueueEntry queue = null;
                synchronized (TASK_QUEUE) {
                    if (mDone) {
                        break;
                    }
                    if (TASK_QUEUE != null && !TASK_QUEUE.isEmpty()) {
                        queue = TASK_QUEUE.pop();
                    } else {
                        try {
                            LogUtil.d("[ChattingAysnImageLoader] loading image wait...");
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            // ignore the exception
                        }
                        continue;

                    }
                }

                // do loading..
                LogUtil.d("[ChattingAysnImageLoader] loading image begin...");
                if (queue != null) {
                    ImageMsgInfoEntry entry = queue.infoEntry;
                    if (entry == null) {
                        continue;
                    }
                    if (TASK_QUEUE_INDEX != null && queue.isLoadThium) {
                        TASK_QUEUE_INDEX.remove(queue.key);
                    }

                    // attachment pic
                    String imageUrl = "";
                    // 加载列表图片
                    if (queue.isLoadThium) {
                        imageUrl = entry.getThumbnailurl();
                        if (imageUrl == null) {
                            imageUrl = entry.getPicurl();
                        }
                    } else {
                        // 加载大图
                        imageUrl = entry.getRemoteUrl();
                    }
                    Bitmap bitmap = loadImageBitmap(queue, imageUrl);

                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
    }

    public interface ChattingImageCallBack {
        void onChattingImageLoaded();
    }

    /**
     * 加入一个图片处理请求到图片创建队列
     *
     * @param id
     * @param infoEntry
     * @param flag
     */
    public void addTask(String id, ImageMsgInfoEntry infoEntry, boolean flag) {
        LogUtil.d("[ChattingAysnImageLoader] add url " + infoEntry.toString());
        if (mDecodeThread == null) {
            start();
        }

        QueueEntry entry = new QueueEntry();
        entry.infoEntry = infoEntry;
        entry.isLoadThium = flag;
        if (infoEntry == null || infoEntry.getId() == null) {
            return;
        }
        String key = infoEntry.getId();
        entry.key = key;
        synchronized (TASK_QUEUE) {
            while (TASK_QUEUE.size() > 20) {
                QueueEntry e = TASK_QUEUE.lastElement();
                TASK_QUEUE.remove(e);
                TASK_QUEUE_INDEX.remove(key);
            }
            if (!TASK_QUEUE_INDEX.contains(key)
                    && !IMG_CACHE_INDEX.containsKey(key)) {
                TASK_QUEUE.push(entry);
                TASK_QUEUE_INDEX.add(key);
                TASK_QUEUE.notify();
            }
        }

    }

    public Bitmap loadImageBitmap(final QueueEntry queue, String url) {

        if (url == null || TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            String extension = FileUtils.getExtension(url);
            final String saveName = DemoUtils.md5(url) + extension;
            LogUtil.d("this image saveName " + saveName);
            if (new File(FileAccessor.getImagePathName(), saveName).exists()) {
                LogUtil.d("loading from sdcard " + saveName);
                return scalingImage(mContext, saveName, queue);

            } else {
                if (FileAccessor.isExistExternalStore()) {//存储卡在的时候才下载
                    ECMessage message = ECMessage.createECMessage(ECMessage.Type.IMAGE);
                    ECFileMessageBody body = new ECFileMessageBody();
                    final String filePath = new File(FileAccessor.getImagePathName(), saveName).getAbsolutePath();
                    body.setLocalUrl(filePath);
                    body.setRemoteUrl(url);
                    message.setBody(body);
                    SDKCoreHelper.getECChatManager().downloadMediaMessage(message, new ECChatManager.OnDownloadMessageListener() {

                        @Override
                        public void onProgress(String msgId, int totalByte, int progressByte) {

                        }


                        @Override
                        public void onDownloadMessageComplete(ECError e, ECMessage message) {
                            ImgInfo thumbImgInfo = ImgInfoSqlManager.getInstance().getImgInfo(queue.infoEntry.getId());
                            thumbImgInfo.setBigImgPath(saveName);
                            ImgInfoSqlManager.getInstance().updateImageInfo(thumbImgInfo);
                            //BitmapFactory.Options options = DemoUtils.getBitmapOptions(fileMessageBody.getLocalUrl());
                            //message.setUserData("outWidth://" + options.outWidth + ",outHeight://" + options.outHeight + ",THUMBNAIL://" + message.getMsgId());
                            scalingImage(mContext, saveName, queue);
                        }
                    });
                }
            }
            return null;
        } catch (OutOfMemoryError err) {
            LogUtil.d("[ChattingAysnImageLoader]OOM:" + err.getMessage());
            return loadImageBitmap(queue, url);
        } catch (Exception e) {
            LogUtil.d("[ChattingAysnImageLoader]Exception:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap scalingImage(Context mContext, String saveName, QueueEntry queue) {
        try {
            if (saveName == null) {
                return null;
            }
            if (!new File(FileAccessor.getImagePathName(), saveName).exists()) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(new File(FileAccessor.getImagePathName(), saveName).getAbsolutePath());
            if (bitmap != null) {
                synchronized (LOCKED) {
                    if (queue.isLoadThium) {
                        if (IMG_CACHE_INDEX != null) {
                            IMG_CACHE_INDEX.put(saveName, bitmap);
                        }
                    }
                }
                mHandler.sendEmptyMessage(1);
            }
            return bitmap;
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
            return null;
        } catch (OutOfMemoryError e) {
            LogUtil.d(e.getMessage());
            System.gc();
            return null;
        }
    }

    public int getImageHeight(String saveName) {
        if (imageHeight != null && saveName != null && imageHeight.get(saveName) != null) {
            return imageHeight.get(saveName);
        }
        return LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    // 队列缓存参数对象
    static class QueueEntry {
        private String key;
        private ImageMsgInfoEntry infoEntry;
        private boolean isLoadThium;
    }

    public void cleanTask() {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE_INDEX.clear();
            TASK_QUEUE.clear();
            for (String key : IMG_CACHE_INDEX.keySet()) {
                Bitmap bitmap = IMG_CACHE_INDEX.get(key);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            IMG_CACHE_INDEX.clear();
        }
    }

    public boolean ismDone() {
        return mDone;
    }

    public void setmDone(boolean mDone) {
        this.mDone = mDone;
    }

    public ChattingImageCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ChattingImageCallBack callBack) {
        this.callBack = callBack;
    }

    public static ChattingAysnImageLoader getInstance(Context context) {
        if (mImageLoader == null) {
            mImageLoader = new ChattingAysnImageLoader(context);
        }

        return mImageLoader;
    }
}
