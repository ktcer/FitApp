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
package com.cn.fit.ui.chat.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.yuntongxun.ecsdk.ECMessage;

/**
 * 会话消息数据库管理
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-11
 */
public class ConversationSqlManager extends AbstractSQLManager {

    private static ConversationSqlManager instance;

    private ConversationSqlManager() {
        super();
    }

    public static ConversationSqlManager getInstance() {
        if (instance == null) {
            instance = new ConversationSqlManager();
        }
        return instance;
    }

    /**
     * @return
     */
    public static Cursor getConversationCursor() {
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username ,name ,im_thread.contactid ,isnotice\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid order by dateTime desc;";
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 通过会话ID查找消息数据库主键
     *
     * @param sessionId 会话ID
     * @return
     */
    public static long querySessionIdForBySessionId(String sessionId) {
        Cursor cursor = null;
        long threadId = 0;
        if (sessionId != null) {
            String where = IThreadColumn.THREAD_ID + " = '" + sessionId + "' ";
            try {
                cursor = getInstance().sqliteDB().query(
                        DatabaseHelper.TABLES_NAME_IM_SESSION, null, where,
                        null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        threadId = cursor.getLong(cursor
                                .getColumnIndexOrThrow(IThreadColumn.ID));
                    }
                }
            } catch (SQLException e) {
                LogUtil.e(TAG + " " + e.toString());
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return threadId;
    }

    /**
     * 生成一个新的会话消息
     *
     * @param msg
     * @return
     */
    public static long insertSessionRecord(ECMessage msg) {
        if (msg == null || TextUtils.isEmpty(msg.getSessionId())) {
            throw new IllegalArgumentException("insert thread table "
                    + DatabaseHelper.TABLES_NAME_IM_SESSION
                    + "error , that Argument ECMessage:" + msg);
        }
        long row = -1;
        ContentValues values = new ContentValues();
        try {
            values.put(IThreadColumn.THREAD_ID, msg.getSessionId());
            values.put(IThreadColumn.DATE, System.currentTimeMillis());
            values.put(IThreadColumn.UNREAD_COUNT, 0);
            values.put(IThreadColumn.CONTACT_ID, msg.getForm());
            row = getInstance().sqliteDB().insertOrThrow(
                    DatabaseHelper.TABLES_NAME_IM_SESSION, null, values);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LogUtil.e(TAG + " " + ex.toString());
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return row;
    }

    public int qureySessionUnreadCount() {
        int count = 0;
        String[] columnsList = {"count(" + IThreadColumn.UNREAD_COUNT + ")"};
        String where = IThreadColumn.UNREAD_COUNT + " > 0";
        Cursor cursor = null;
        try {
            cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_SESSION,
                    columnsList, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("count("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return count;
    }

    public int qureyAllSessionUnreadCount() {
        int count = 0;
        String[] columnsList = {"sum(" + IThreadColumn.UNREAD_COUNT + ")"};
        Cursor cursor = null;
        try {
            cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_SESSION,
                    columnsList, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("sum("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public static void delSession(String contactId) {
        String where = IThreadColumn.THREAD_ID + " = '" + contactId + "' ";
        getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_SESSION, where, null);
    }

    /**
     * 更新会话已读状态
     *
     * @param id
     * @return
     */
    public static long setChattingSessionRead(long id) {
        if (id <= 0) {
            return -1;
        }
        ContentValues values = new ContentValues();
        try {
            String where = IThreadColumn.ID + " = " + id + " and " + IThreadColumn.UNREAD_COUNT + "!=0";
            values.put(IThreadColumn.UNREAD_COUNT, 0);
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_SESSION, values, where, null);
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
            e.getStackTrace();
        } finally {
            if (values != null) {
                values.clear();
            }
        }
        return -1;
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        instance = null;
    }
}
