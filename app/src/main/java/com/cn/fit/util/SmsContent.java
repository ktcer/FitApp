package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

/*
 * 提取系统短息验证码
 * @author chenkeliang
 * 
 */
public class SmsContent extends ContentObserver {

    public Uri SMS_URI_INBOX = Uri.parse("content://sms/");
    private Activity activity = null;
    private String smsContent = "";
    private EditText verifyText = null;

    //
    public SmsContent(Activity activity, Handler handler, EditText verifyText) {
        super(handler);
        this.activity = activity;
        this.verifyText = verifyText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;// 光标
        // 读取收件箱中指定号码的短信
        String[] projection = new String[]{"_id", "address", "body", "read"};
        cursor = activity.getContentResolver().query(SMS_URI_INBOX,
                projection, "address=? and read=?",
                new String[]{"106598058012199868", "0"}, "date desc");
        if (cursor != null) {// 如果短信为未读模式
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                String smsbody = cursor
                        .getString(cursor.getColumnIndex("body"));
                System.out.println("smsbody=======================" + smsbody);
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(smsbody.toString());
                smsContent = m.replaceAll("").trim().toString();
                verifyText.setText(smsContent);
            }
        }
    }


}
