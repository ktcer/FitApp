package com.cn.fit.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import android.os.Looper;

public class Logcat implements Runnable {

    private Process mLogcatProc;

    private Loger log = new Loger("[LogCat]");

    private boolean theadFlag = true;

    /**
     * 停止
     */
    public void stopLogCat() {
        theadFlag = false;
    }

    public void run() {
        int count = 0;
        theadFlag = true;
        while (theadFlag) {
            if (count < 200) {
                count++;
                sleep();
                continue;
            } else {
                count = 0;
            }
            logcat();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logcat() {
        try {
            mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (null == line) {
                    break;
                }
                Looper.prepare();
                if (null == print) {
                    initPrint();
                }
                print.write(line.getBytes());
                Looper.loop();
            }
        } catch (Exception e) {
        }
    }

    //日志输出流
    private PrintStream print = null;

    //日志文件
    private FileOutputStream fos = null;

    //初始化文件输出流
    private void initPrint() {
        Calendar date = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = new String(dfm.format(date.getTime()) + "_LogCat.txt");
        try {
            if (null != print) {
                close();
            }
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/weiboShare/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            fos = new FileOutputStream(path + fileName, true);
            print = new PrintStream(fos, true);
        } catch (FileNotFoundException e) {
            log.output("[LogerImp] 未能打开文件:" + fileName + " 异常描述:" + e.getLocalizedMessage());
        }
    }

    private void close() {
        print.flush();
        print.close();
        print = null;
        try {
            fos.close();
            fos = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
