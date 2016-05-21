package com.cn.fit.ui;

import android.os.Environment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日志记录
 *
 * @author kuangtiecheng
 */
public class Loger {

    private LogerImp instance;
    //日志名称
    private String logerName;

    //调试时可以设置为true；发布时需要设置为false；
    protected static boolean isOpen = true;
//	protected static boolean isOpen = false;

    /**
     * 开始输入日志信息<br\>
     * （只作为程序日志开关，在个人设置中开启，其他应用中不得调用）
     */
    public static void openPrint() {
        if (isOpen) {
            LogerImp.instance.startRun();
        }
    }

//	/**
//	 * 打印内存<br\>
//	 * （只作为程序日志开关，在个人设置中开启，其他应用中不得调用）
//	 */
//	public static void printMemory(){
//		LogerImp.instance.listenGC();
//	}

    /**
     * 关闭日志打印 <br\>
     * （只作为程序日志开关，在个人设置中开启，其他应用中不得调用）
     */
    public static void closePrint() {
        if (isOpen) {
            LogerImp.instance.stopRun();
        }
    }

    private static Loger loger = new Loger("[Loger]");

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized static void print(String msg) {
        if (isOpen) {
            loger.output(msg);
        }
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized static void print(String msg, Exception e) {
        MobclickAgent.reportError(AppMain.app, msg + "[LocalizedMessage]" + e.getLocalizedMessage());
        if (isOpen) {
            loger.output(msg, e);
        }
    }

    /**
     * 构造函数
     *
     * @param name String
     */
    public Loger(String name) {
        logerName = name;
        instance = LogerImp.getInstance();
    }

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized void output(String msg) {
        if (isOpen) {
            Log.i(logerName, msg);//显示在log日志上
            instance.submitMsg(logerName + " " + msg);
        }
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized void output(String msg, Exception e) {
        if (isOpen) {
            Log.i(logerName, msg, e);
            StringBuffer buf = new StringBuffer(msg);
            buf.append(logerName).append(" : ").append(msg).append("\n");
            buf.append(e.getClass()).append(" : ");
            buf.append(e.getLocalizedMessage());
            buf.append("\n");
            StackTraceElement[] stack = e.getStackTrace();
            for (StackTraceElement trace : stack) {
                buf.append("\t at ").append(trace.toString()).append("\n");
            }
            instance.submitMsg(buf.toString());
        }
    }

    /**
     * 打印当前的内存信息
     */
    public void printCurrentMemory() {
        if (isOpen) {
            StringBuilder logs = new StringBuilder();
            long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
            long totalMemory = Runtime.getRuntime().totalMemory() / 1024;
            long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
            logs.append("\t[Memory_free]: ").append(freeMemory).append(" kb");
            logs.append("\t[Memory_total]: ").append(totalMemory).append(" kb");
            logs.append("\t[Memory_max]: ").append(maxMemory).append(" kb");
            Log.i(logerName, logs.toString());
            instance.submitMsg(logerName + " " + logs.toString());
        }
    }
}

/**
 * 日志输出的具体实现类
 *
 * @author kuangtiecheng
 */
class LogerImp implements Runnable {

    private Loger log = new Loger("[LogerImp]");

    static LogerImp instance = new LogerImp();

    //日志存放的队列
    private List<String> printOutList = new ArrayList<String>();

    //日志文件
    private FileOutputStream fos = null;

    //日志输出流
    private PrintStream print = null;

    //时间格式
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    //线程轮询标识
    private boolean runFlag = false;

    //当前天，每天生成一个日志文件
    private int currDay = -1;

    private GcCheck gcRun = new GcCheck();

    class GcCheck implements Runnable {

        boolean flag = true;

        @Override
        public void run() {
            int count = 40;
            StringBuffer logs = new StringBuffer();
            while (flag) {
                if (count >= 50) {
                    long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
                    long totalMemory = Runtime.getRuntime().totalMemory() / 1024;
                    long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
                    logs.append("\t[Memory_free]:").append(freeMemory).append(" kb");
                    logs.append("\t[Memory_total]:").append(totalMemory).append(" kb");
                    logs.append("\t[Memory_max]:").append(maxMemory).append(" kb");
                    synchronized (printOutList) {
                        printOutList.add(logs.toString());
                    }
                    Log.i("Memory", logs.toString());
                    logs.setLength(0);
                    if (freeMemory < 400) {
                        System.gc();
                        count = 40;
                        logs.append("<GC>");
                    } else {
                        count = 0;
                    }
                }
                try {
                    count++;
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ;

    /**
     * 得到单例对象
     *
     * @return LogerImp
     */
    public static LogerImp getInstance() {
        return instance;
    }

    /**
     * 私有方法，单例
     */
    private LogerImp() {
    }

//	void listenGC(){
//		gcRun.flag = true;
//		new Thread(gcRun).start();
//	}

//	void stopLintenGC(){
//		gcRun.flag = false;
//	}

    //初始化文件输出流
    private void initPrint() {
        Calendar date = Calendar.getInstance();
        currDay = date.get(Calendar.DAY_OF_YEAR);
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = new String(dfm.format(date.getTime()) + ".txt");
        String path = null;
        try {
            if (null != print) {
                close();
            }
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/weiboShare/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            fos = new FileOutputStream(path + fileName, true);
            print = new PrintStream(fos, true);
        } catch (Exception e) {
            log.output("[LogerImp] 未能打开文件:" + path + " 文件名：" + fileName + " 异常描述:" + e.getLocalizedMessage());
        }
    }

    private Logcat logCat = new Logcat();

    /**
     * 线程开启
     */
    public void startRun() {
        if (!runFlag) {
            runFlag = true;
            new Thread(this).start();
            new Thread(logCat).start();
        } else {
            log.output("[LogerImp] < warn > thread already run !");
        }
    }

    /**
     * 线程停止
     */
    public void stopRun() {
        if (runFlag) {
            gcRun.flag = false;
            runFlag = false;
            logCat.stopLogCat();
            Log.i("Thread", "队列大小：" + printOutList.size());
            printToFile("[LogerImp] < info > thread stop !");
            close();
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

    /**
     * 向队列中增加日志数据
     *
     * @param msg String 日志数据
     */
    protected synchronized void submitMsg(String msg) {
        synchronized (printOutList) {
            printOutList.add(msg);
        }
    }

    public void run() {
        try {
            initPrint();
            printToFile("[LogerImp] < info > start new thread ... ");
            while (runFlag) {
                runMethod();
            }
            runFlag = false;
        } catch (Exception e) {
            printToFile("[LogerImp] < warn > thread error : " + e.getLocalizedMessage());
            if (runFlag) {
                printToFile("[LogerImp] 线程强制中断 " + e.getLocalizedMessage());
                new Thread(this).start();
            }
        }
    }

    //线程需要重复执行的操作
    private void runMethod() throws Exception {
        String line = null;
        synchronized (printOutList) {
            if (!printOutList.isEmpty()) {
                line = printOutList.remove(0);
            }
        }
        if (null != line) {
            printToFile(line);
        } else {
            Thread.sleep(10);
        }
    }

    //把数据持久到文件
    private void printToFile(String line) {
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DAY_OF_YEAR);
        if (day != currDay) {
            initPrint();
        }
        if (null == print) {
            return;
        }
        print.println(">>> " + format.format(date.getTime()) + " -- " + line);
        print.flush();
    }
}