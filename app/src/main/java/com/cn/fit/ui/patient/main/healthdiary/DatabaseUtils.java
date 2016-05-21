package com.cn.fit.ui.patient.main.healthdiary;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/11 上午9:08:39
 * @parameter
 * @return
 */
public class DatabaseUtils {
    public static String CREAT_DIARY = "create table if not exists diary_remind (id integer primary key autoincrement, userid integer, dateday text, daytime text, content text, path text, diaryid integer, valid integer)";
    public static String CREAT_ALARM = "create table if not exists alarm (alarmid integer primary key autoincrement, id integer, dateday text, daytime text, content text, path text,valid integer)";
    public static String CREAT_USER = "create table if not exists user (id integer primary key autoincrement,userid integer ,userinfo text,foreign key (userid) references diary_remind(userid))";
}
