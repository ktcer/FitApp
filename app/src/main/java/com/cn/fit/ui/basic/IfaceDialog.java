package com.cn.fit.ui.basic;

public interface IfaceDialog {

    /**
     * 判断当前Activity页面是否退出，退出后关闭HTTP请求，并不对HTTP请求结果进行解析
     */
    boolean closeActivity();

    /**
     * 关闭调用等待窗口
     */
    void dismiss();

    /**
     * 显示对话窗口
     *
     * @param title    String
     * @param message  String
     * @param drawable Drawable
     */
    void showAlertDialog(String title, String message, int drawable);

    /**
     * 显示对话窗口
     *
     * @param title   String
     * @param message String
     */
    void showAlertDialog(String title, String message);

    /**
     * 显示进度窗口
     *
     * @param title   String
     * @param message String
     */
    void showProgressDialog(String title, String message);

    /**
     * 显示进度窗口
     *
     * @param title   String
     * @param message String
     * @param run     Runnable
     */
    void showProgressDialog(String title, String message, Runnable run);

    /**
     * 显示进度窗口，默认窗口title为：提示信息
     *
     * @param message String
     * @param run     Runnable
     */
    void showProgressDialog(String message, Runnable run);

    /**
     * 显示进度窗口
     *
     * @param title       String
     * @param message     String
     * @param run         Runnable
     * @param delayedTime long
     */
    void showProgressDialog(String title, String message, Runnable run, long delayedTime);

    /**
     * 显示提示
     *
     * @param message String
     */
    void showToastDialog(String message);

    /**
     * 显示提示
     *
     * @param message String
     */
    void showToastDialogLongTime(String message);

    /**
     * 提示信息，信息经确认后系统退出
     *
     * @param title String 信息名
     * @param msg   String 信息内容
     */
    void showNextExitDialog(String title, String msg);

    /**
     * 提示信息，然后退回到首页重新登录
     *
     * @param title String 信息名
     * @param msg   String 信息内容
     */
    void showDialogAndReturnMainPageReLogin(String title, String msg);
}
