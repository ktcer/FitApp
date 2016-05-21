package com.cn.fit.util;

import android.util.Log;

import com.cn.fit.BuildConfig;

/**
 * 日志记录
 *
 * @author Administrator
 */
public class LogPrint {

    //调试时可以设置为true；发布时需要设置为false；
    protected static boolean isOpen = true && BuildConfig.DEBUG;

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized static void debug(Object msg) {
        print(null, msg.toString(), 0);
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized static void debug(Object msg, Exception e) {
        print(null, msg.toString(), e, 0);
    }

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized static void info(Object msg) {
        print(null, msg.toString(), 1);
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized static void info(Object msg, Exception e) {
        print(null, msg.toString(), e, 1);
    }

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    @SuppressWarnings("rawtypes")
    public synchronized static void info(Class obj, Object msg) {
        String name = obj.getName();
        print(name, msg.toString(), 1);
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    @SuppressWarnings("rawtypes")
    public synchronized static void info(Class obj, Object msg, Exception e) {
        String name = obj.getName();
        print(name, msg.toString(), e, 1);
    }

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized static void warn(Object msg) {
        print(null, msg.toString(), 2);
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized static void warn(Object msg, Exception e) {
        print(null, msg.toString(), e, 2);
    }

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    public synchronized static void error(Object msg) {
        print(null, msg.toString(), 3);
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    public synchronized static void error(Object msg, Exception e) {
        print(null, msg.toString(), e, 3);
    }

    /**
     * 日志名
     */
    private static final String LogName = "[LogPrint]";

    /**
     * 输出日志信息
     *
     * @param msg String 日志
     */
    private synchronized static void print(String tag, String msg, int type) {
        if (null == tag || "".equals(tag)) {
            tag = LogName;
        } else {
            tag += ("-" + LogName);
        }
        if (!isOpen) {
            return;
        }
        switch (type) {
            case 0:
                Log.d(tag, msg);
                break;
            case 1:
                Log.i(tag, msg);
                break;
            case 2:
                Log.w(tag, msg);
                break;
            case 3:
                Log.e(tag, msg);
                break;
        }
    }

    /**
     * 输出日志信息及异常发生的详细信息
     *
     * @param msg String 日志
     * @param e   Exception
     */
    private synchronized static void print(String tag, String msg, Exception e, int type) {
        if (null == tag || "".equals(tag)) {
            tag = LogName;
        } else {
            tag += ("-" + LogName);
        }
        if (!isOpen) {
            return;
        }
        switch (type) {
            case 0:
                Log.d(tag, msg, e);
                break;
            case 1:
                Log.i(tag, msg, e);
                break;
            case 2:
                Log.w(tag, msg, e);
                break;
            case 3:
                Log.e(tag, msg, e);
                break;
        }
        StringBuffer buf = new StringBuffer(msg);
        buf.append(tag).append(" : ").append(msg).append("\n");
        buf.append(e.getClass()).append(" : ");
        buf.append(e.getLocalizedMessage());
        buf.append("\n");
        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement trace : stack) {
            buf.append("\t at ").append(trace.toString()).append("\n");
        }
        System.out.println(buf.toString());
    }
}