package com.cn.fit.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.util.CreateFolder;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/***
 * 升级服务
 *
 * @author kuangtiecheng
 */
public class ServiceUpdate extends Service {

//	public static final String Install_Apk = "Install_Apk";
    /********
     * download progress step
     *********/
    private static final int down_step_custom = 3;
    private static final int NOTIFY_ID = 1003;
    private static final int TIMEOUT = 10 * 1000;// 超时
    private static String down_url;
    private static final String ACTION_CANCEL_DOWNLOAD_APK = "action_cancel_download_apk";
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;
    private static boolean isCanceled = false;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private RemoteViews contentView;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 方法描述：onStartCommand方法
     *
     * @param Intent intent, int flags, int startId
     * @return int
     * @see ServiceUpdate
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        app_name = intent.getStringExtra("Key_App_Name");
        down_url = intent.getStringExtra("Key_Down_Url");

        // create file,应该在这个地方加一个返回值的判断SD卡是否准备好，文件是否创建成功，等等！
        CreateFolder.createApkFile(app_name);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CANCEL_DOWNLOAD_APK);
        registerReceiver(onclickCancelListener, filter);
        if (CreateFolder.isCreateFileSucess == true) {
            createNotification();
            createThread();
        } else {
            Toast.makeText(this, "请插入sd卡进行更新", Toast.LENGTH_SHORT).show();
            /***************stop service************/
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /*********
     * update UI
     ******/
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:

                    /*********下载完成，点击安装***********/
//					Uri uri = Uri.fromFile(CreateFolder.updateFile);
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//					intent.setDataAndType(uri,"application/vnd.android.package-archive");
//					pendingIntent = PendingIntent.getActivity(ServiceUpdate.this, 0, intent, 0);
//
//					notification.flags = Notification.FLAG_AUTO_CANCEL;
//					Notification.Builder builderSuccess = new Notification.Builder(ServiceUpdate.this)
//							.setAutoCancel(true)
//							.setContentTitle(app_name)
//							.setContentIntent(pendingIntent)
//							.setContentText("下载成功")
//							.setSmallIcon(R.drawable.icon);
//					notification=builderSuccess.getNotification();
//					//notification.setLatestEventInfo(ServiceUpdate.this,app_name, app_name + getString(R.string.down_sucess), null);
//					notificationManager.notify(R.layout.notification_item, notification);

                    /*****下载完成直接安装APK******/
                    notificationManager.cancel(NOTIFY_ID);
                    installApk();

//					stopService(updateIntent);
                    /***stop service*****/
                    stopSelf();
                    break;

                case DOWN_ERROR:
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    //notification.setLatestEventInfo(ServiceUpdate.this,app_name, getString(R.string.down_fail), pendingIntent);
                    Notification.Builder builderFail = new Notification.Builder(ServiceUpdate.this)
                            .setAutoCancel(true)
                            .setContentTitle(app_name)
                            .setContentText("下载失败")
                            .setSmallIcon(R.drawable.notification_icon);
                    notification = builderFail.getNotification();
                    /***stop service*****/
                    //onDestroy();
                    stopSelf();
                    break;

                default:
                    //stopService(updateIntent);
                    /******Stop service******/
                    //stopService(intentname)
                    //stopSelf();
                    break;
            }
        }
    };

    private void installApk() {
        // TODO Auto-generated method stub
        /*********下载完成，点击安装***********/
        Uri uri = Uri.fromFile(CreateFolder.updateFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        /**********加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task**********/
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        ServiceUpdate.this.startActivity(intent);
    }

    /**
     * 方法描述：createThread方法, 开线程下载
     *
     * @param
     * @return
     * @see ServiceUpdate
     */
    public void createThread() {
        new DownLoadThread().start();
    }


    private class DownLoadThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            try {
                long downloadSize = downloadUpdateFile(down_url, CreateFolder.updateFile.toString());
                if (isCanceled) {
                    isCanceled = false;
                } else {
                    if (downloadSize > 0) {
                        // down success
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 方法描述：createNotification方法
     *
     * @param
     * @return
     * @see ServiceUpdate
     */
    public void createNotification() {

        notification = new Notification(
                //R.drawable.video_player,//应用的图标
                R.drawable.notification_icon,//应用的图标
                app_name + "下载中",
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name + "下载中");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        Intent btnCancelIntent = new Intent(ACTION_CANCEL_DOWNLOAD_APK);
        PendingIntent pendButtonIntent = PendingIntent.getBroadcast(this, 0, btnCancelIntent, 0);
        contentView.setOnClickPendingIntent(R.id.ivCancel, pendButtonIntent);
        notification.contentView = contentView;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    BroadcastReceiver onclickCancelListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CANCEL_DOWNLOAD_APK)) {
                // TODO;
                // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                // 取消通知
                isCanceled = true;
                notificationManager.cancel(NOTIFY_ID);
                stopSelf();
            }
        }
    };

    /***
     * down file
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file) throws Exception {

        int down_step = down_step_custom;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小

        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();

        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
            //这个地方应该加一个下载失败的处理，但是，因为我们在外面加了一个try---catch，已经处理了Exception,
            //所以不用处理
        }

        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

        byte buffer[] = new byte[1024];
        int readsize = 0;

        while ((readsize = inputStream.read(buffer)) != -1) {
            if (isCanceled) {
                break;
            }
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            /*** 每次增张3%**/
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // 改变通知栏
                contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                notification.contentView = contentView;
                notificationManager.notify(NOTIFY_ID, notification);
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();
        return downloadCount;
    }

}