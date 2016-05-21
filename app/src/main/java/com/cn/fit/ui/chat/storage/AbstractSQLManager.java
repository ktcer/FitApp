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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.DatabaseTools;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.yuntongxun.ecsdk.ECMessage;


/**
 * 数据库访问接口
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-11
 */
public abstract class AbstractSQLManager {

    public static final String TAG = AbstractSQLManager.class.getName();

    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqliteDB;

    public AbstractSQLManager() {
//        openDatabase(AppMain.getInstance(), CCPAppManager.getVersionCode());
        openDatabase(AppMain.getInstance(), DatabaseTools.getDatabaseversion());
    }

    private void openDatabase(Context context, int databaseVersion) {
        if (databaseHelper == null) {
            UtilsSharedData.initDataShare(context);
            String tempName = UtilsSharedData.getLong(Constant.USER_ID, 0) + "0" + "_" + "ECSDK_Msg.db";
            databaseHelper = new DatabaseHelper(context, this, tempName, databaseVersion);
        }
        //DatabaseManager.initializeInstance(databaseHelper);
        if (sqliteDB == null) {
            sqliteDB = databaseHelper.getWritableDatabase();
            //sqliteDB = DatabaseManager.getInstance().openDatabase();
        }

    }

    public void destroy() {
        try {
            if (databaseHelper != null) {
                databaseHelper.close();
            }
            closeDB();
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }

    private void open(boolean isReadonly) {
        if (sqliteDB == null) {
            if (isReadonly) {
                sqliteDB = databaseHelper.getReadableDatabase();
            } else {
                sqliteDB = databaseHelper.getWritableDatabase();/*DatabaseManager.getInstance().openDatabase()*/
                ;
            }
        }
    }

    public final void reopen() {
        closeDB();
        open(false);
        LogUtil.w("[SQLiteManager] reopen this db.");
    }

    private void closeDB() {
        if (sqliteDB != null) {
            //DatabaseManager.getInstance().closeDatabase();
            sqliteDB = null;
            sqliteDB.close();
            sqliteDB = null;
        }
    }

    protected final SQLiteDatabase sqliteDB() {
        open(false);
        return sqliteDB;
    }


    /**
     * 创建基础表结构
     */
    static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * 数据库名称
         */
        static final String DATABASE_NAME = "ECSDK_Msg.db";
        static final String DESC = "DESC";
        static final String ASC = "ASC";
        static final String TABLES_NAME_IM_SESSION = "im_thread";
        static final String TABLES_NAME_IM_MESSAGE = "im_message";
        static final String TABLES_NAME_CONTACT = "contacts";
        static final String TABLES_NAME_GROUPS = "groups";
        static final String TABLES_NAME_GROUPS_2 = "groups2";
        static final String TABLES_NAME_GROUP_MEMBERS = "group_members";
        static final String TABLES_NAME_SYSTEM_NOTICE = "system_notice";


        private AbstractSQLManager mAbstractSQLManager;

        public DatabaseHelper(Context context, AbstractSQLManager manager, String name, int version) {
//        	this(context, manager , CCPAppManager.getClientUser().getUserId() + "_" + DATABASE_NAME, null, version);
            this(context, manager, name, null, version);
        }

        public DatabaseHelper(Context context, AbstractSQLManager manager, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
            mAbstractSQLManager = manager;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onTriggerUpgrade(db);
            if (oldVersion < 9) {
                onUpdateOld(db);
            }
//            createTables(db);
            if (oldVersion < 9) {
//                onGroupUpgrade(db);
            }
        }

        private void onUpdateOld(SQLiteDatabase db) {
            String[] sqls = new String[]{TABLES_NAME_IM_SESSION,
                    TABLES_NAME_IM_MESSAGE};
            String[] dropTrigger = new String[]{"delete_obsolete_threads_im",
                    "im_update_thread_on_delete",
                    "im_update_thread_on_delete2",
                    "im_update_thread_on_insert",
                    "im_update_thread_on_insert2",
                    "im_update_thread_read_on_update",
                    "im_update_thread_on_update",
                    "thread_update_im_on_delete"};
            for (String sql : sqls) {
                db.execSQL("DROP TABLE IF EXISTS " + sql);
            }

            for (String sql : dropTrigger) {
                db.execSQL("DROP  TRIGGER IF EXISTS " + sql);
            }
        }

        /**
         * 更新触发器
         *
         * @param db
         */
        private void onTriggerUpgrade(SQLiteDatabase db) {
            String sql = "DROP  TRIGGER IF EXISTS 'im_update_thread_read_on_update'";
            String sql1 = "DROP  TRIGGER IF EXISTS 'im_update_thread_on_delete'";
            LogUtil.v(TAG + ":" + sql1);
            db.execSQL(sql);
            db.execSQL(sql1);

            String sql2 = "DROP  TRIGGER IF EXISTS 'im_update_thread_on_insert'";
            LogUtil.v(TAG + ":" + sql2);
            db.execSQL(sql2);
        }

        /**
         * @param db
         */
        private void createTables(SQLiteDatabase db) {
            // 创建联系人表
            createTableForContacts(db);
            // 创建Im信息表
            createTableForIMessage(db);
            // 创建im会话表
            createTableForISession(db);
            // 创建触发器
            createTriggerForIMessage(db);
            // 创建群组表
            createTaleForIMGroups(db);
            /**创建群组成员表结构*/
            createTableGroupMembers(db);

            /**创建群组通知消息表和触发器*/
            createTableSystemNotice(db);
            createTriggerForSystemNotice(db);

            createImgInfoTable(db);
        }

        /**
         * 创建联系人表
         *
         * @param db
         */
        void createTableForContacts(SQLiteDatabase db) {

            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_CONTACT
                    + " ("
                    + ContactsColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ContactsColumn.CONTACT_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + ContactsColumn.type + " INTEGER, "
                    + ContactsColumn.USERNAME + " TEXT, "
                    + ContactsColumn.SUBACCOUNT + " TEXT, "
                    + ContactsColumn.TOKEN + " TEXT, "
                    + ContactsColumn.SUBTOKEN + " TEXT, "
                    + ContactsColumn.REMARK + " TEXT "
                    + ")";
            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }


        /**
         * IM消息表
         *
         * @param db
         */
        void createTableForIMessage(SQLiteDatabase db) {

            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_IM_MESSAGE
                    + " ("
                    + IMessageColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMessageColumn.MESSAGE_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + IMessageColumn.OWN_THREAD_ID + " INTEGER, "
                    + IMessageColumn.CREATE_DATE + " TEXT, "
                    + IMessageColumn.RECEIVE_DATE + " TEXT, "
                    + IMessageColumn.sender + " TEXT, "
                    + IMessageColumn.BODY + " TEXT, "
                    + IMessageColumn.USER_DATA + " TEXT, "
                    + IMessageColumn.FILE_URL + " TEXT, "
                    + IMessageColumn.FILE_PATH + " TEXT, "
                    + IMessageColumn.BOX_TYPE + " INTEGER DEFAULT 0, "
                    + IMessageColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                    + IMessageColumn.MESSAGE_TYPE + " INTEGER DEFAULT 0, "
                    + IMessageColumn.READ_STATUS + "  INTEGER DEFAULT 0, "
                    + IMessageColumn.VERSION + "  INTEGER DEFAULT 0, "
                    + IMessageColumn.DURATION + "  INTEGER DEFAULT 0"
                    + ")";
            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }

        /**
         * IM消息会话
         *
         * @param db
         */
        void createTableForISession(SQLiteDatabase db) {

            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_IM_SESSION
                    + " ("
                    + IThreadColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IThreadColumn.THREAD_ID + " TEXT, "
                    + IThreadColumn.CONTACT_ID + " TEXT , "
                    + IThreadColumn.UNREAD_COUNT + " INTEGER DEFAULT 0, "
                    + IThreadColumn.SNIPPET + "  TEXT, "
                    + IThreadColumn.DATE + "  TEXT, "
                    + IThreadColumn.BOX_TYPE + " INTEGER DEFAULT 0, "
                    + IThreadColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                    + IThreadColumn.MESSAGE_TYPE + " INTEGER DEFAULT 0, "
                    + IThreadColumn.MESSAGE_COUNT + " INTEGER DEFAULT 0"
                    + ")";
            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }

        /**
         * 创建群组
         *
         * @param db
         */
        void createTaleForIMGroups(SQLiteDatabase db) {
            String sql2 = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_GROUPS_2
                    + " ("
                    + GroupColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroupColumn.GROUP_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + GroupColumn.GROUP_NAME + " TEXT, "
                    + GroupColumn.GROUP_OWNER + " TEXT, "
                    + GroupColumn.GROUP_DECLARED + "  TEXT, "
                    + GroupColumn.GROUP_TYPE + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_PERMISSION + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_MEMBER_COUNTS + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_JOINED + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_ISNOTICE + " INTEGER DEFAULT 1, "
                    + GroupColumn.GROUP_DATE_CREATED + "  TEXT"
                    + ")";
            LogUtil.v(TAG + ":" + sql2);
            db.execSQL(sql2);
        }

        /**
         * 创建群组成员数据库
         *
         * @param db
         */
        void createTableGroupMembers(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_GROUP_MEMBERS
                    + " ("
                    + GroupMembersColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroupMembersColumn.OWN_GROUP_ID + " TEXT, "
                    + GroupMembersColumn.BIRTH + " TEXT, "
                    + GroupMembersColumn.MAIL + " TEXT, "
                    + GroupMembersColumn.REMARK + " TEXT, "
                    + GroupMembersColumn.TEL + " TEXT, "
                    + GroupMembersColumn.SIGN + " TEXT, "
                    + GroupMembersColumn.ROLE + "  INTEGER DEFAULT 1, "
                    + GroupMembersColumn.ISBAN + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.RULE + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.SEX + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.VOIPACCOUNT + " TEXT "
                    + ")";
            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }

        /**
         * 创建系统通知表
         *
         * @param db
         */
        void createTableSystemNotice(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_SYSTEM_NOTICE + " (" + SystemNoticeColumn.ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SystemNoticeColumn.NOTICE_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + SystemNoticeColumn.OWN_THREAD_ID + " INTEGER, "
                    + SystemNoticeColumn.ADMIN + " TEXT, "
                    + SystemNoticeColumn.NOTICE_VERIFYMSG + " TEXT, "
                    + SystemNoticeColumn.NOTICE_DECLARED + " TEXT, "
                    + SystemNoticeColumn.NOTICE_GROUPID + " TEXT, "
                    + SystemNoticeColumn.NOTICE_GROUPNAME + " TEXT, "
                    + SystemNoticeColumn.NOTICE_NICKNAME + " TEXT, "
                    + SystemNoticeColumn.NOTICE_OPERATION_STATE + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_VERSION + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_READ_STATUS + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_TYPE + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_DATECREATED + " TEXT, "
                    + SystemNoticeColumn.NOTICE_WHO + " TEXT " + ")";
            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }

        /**
         * 创建IM消息会话表触发器
         *
         * @param db
         */
        void createTriggerForIMessage(SQLiteDatabase db) {

            String sql = "CREATE TRIGGER IF NOT EXISTS delete_obsolete_threads_im AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE
                    + " BEGIN   "
                    + " DELETE FROM " + TABLES_NAME_IM_SESSION + " WHERE id = old." + IMessageColumn.OWN_THREAD_ID + " AND id NOT IN ( SELECT " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ");"
                    + " END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_delete AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_IM_MESSAGE + ".id) FROM " + TABLES_NAME_IM_MESSAGE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + IMessageColumn.OWN_THREAD_ID + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " AND " + TABLES_NAME_IM_MESSAGE + "." + IMessageColumn.BOX_TYPE + " != 3 )   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BODY + " AS " + IThreadColumn.SNIPPET + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " =  (SELECT " + IMessageColumn.CREATE_DATE + " FROM (SELECT " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = (SELECT " + IMessageColumn.BOX_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BOX_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = (SELECT " + IMessageColumn.SEND_STATUS + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.SEND_STATUS + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_TYPE + " = (SELECT " + IMessageColumn.MESSAGE_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.MESSAGE_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "

                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_delete2 AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.CONTACT_ID
                    + " = (SELECT " + IMessageColumn.sender + " FROM (SELECT " + IMessageColumn.sender + ", " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") "
                    + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_insert AFTER INSERT ON " + TABLES_NAME_IM_MESSAGE + " "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET "
                    + IThreadColumn.DATE + " = new." + IMessageColumn.CREATE_DATE + ","
                    + IThreadColumn.SNIPPET + " = new." + IMessageColumn.BODY + ", "
                    + IThreadColumn.BOX_TYPE + "=new." + IMessageColumn.BOX_TYPE + ","
                    + IThreadColumn.SEND_STATUS + "=new." + IMessageColumn.SEND_STATUS + ","
                    + IThreadColumn.MESSAGE_TYPE + "=new." + IMessageColumn.MESSAGE_TYPE + " WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_IM_MESSAGE + ".id) FROM " + TABLES_NAME_IM_MESSAGE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + IMessageColumn.OWN_THREAD_ID + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = new." + IMessageColumn.OWN_THREAD_ID + " AND " + TABLES_NAME_IM_MESSAGE + "." + IMessageColumn.BOX_TYPE + " != 3 )   WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(" +
                    "(SELECT " + IThreadColumn.UNREAD_COUNT + " FROM " + TABLES_NAME_IM_SESSION + " WHERE " + IThreadColumn.ID + " = new." + IMessageColumn.OWN_THREAD_ID + ")+1) "
                    + " WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID + " AND new." + IMessageColumn.BOX_TYPE + " == 1 ;"
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_insert2 AFTER INSERT ON " + TABLES_NAME_IM_MESSAGE + " "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET "
                    + IThreadColumn.CONTACT_ID + " = new." + IMessageColumn.sender + " WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID + "; "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

           /* sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_read_on_update AFTER  UPDATE OF " + IMessageColumn.READ_STATUS + "  ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_IM_MESSAGE + " WHERE " + IMessageColumn.READ_STATUS + " = 0 AND " + IMessageColumn.BOX_TYPE + " = 1 AND " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + "; "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);*/

            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_update AFTER  UPDATE ON " + TABLES_NAME_IM_MESSAGE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = (SELECT " + IMessageColumn.CREATE_DATE + " FROM (SELECT " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BODY + " AS " + IThreadColumn.SNIPPET + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = (SELECT " + IMessageColumn.BOX_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BOX_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = (SELECT " + IMessageColumn.SEND_STATUS + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.SEND_STATUS + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS thread_update_im_on_delete AFTER DELETE ON " + TABLES_NAME_IM_SESSION + " "
                    + "BEGIN DELETE FROM " + TABLES_NAME_IM_MESSAGE + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IThreadColumn.ID + ";END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

        }


        /**
         * 创建系统通知表触发器
         *
         * @param db
         */
        void createTriggerForSystemNotice(SQLiteDatabase db) {
            String sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_delete AFTER DELETE ON " + TABLES_NAME_SYSTEM_NOTICE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_SYSTEM_NOTICE + ".id) FROM " + TABLES_NAME_SYSTEM_NOTICE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + SystemNoticeColumn.OWN_THREAD_ID + " WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS + " = 0 AND " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ")    WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " =    (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ")    WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = " + ECMessage.Direction.RECEIVE.ordinal() + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = " + ECMessage.MessageStatus.SUCCESS.ordinal() + " ;   "
                    // + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_TYPE + " = " + com.speedtong.example.storage.GroupNoticeSqlManager.NOTICE_MSG_TYPE + " ;   "

                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_insert AFTER INSERT ON " + TABLES_NAME_SYSTEM_NOTICE + " "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = new." + SystemNoticeColumn.NOTICE_DATECREATED + "," + IThreadColumn.SNIPPET + " = new." + SystemNoticeColumn.NOTICE_VERIFYMSG + "," + IThreadColumn.BOX_TYPE + "=" + ECMessage.Direction.RECEIVE.ordinal() + "," + IThreadColumn.SEND_STATUS + "=" + ECMessage.MessageStatus.SUCCESS.ordinal() + "," + IThreadColumn.MESSAGE_TYPE + "=" + GroupNoticeSqlManager.NOTICE_MSG_TYPE + " WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_SYSTEM_NOTICE + ".id) FROM " + TABLES_NAME_SYSTEM_NOTICE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + SystemNoticeColumn.OWN_THREAD_ID + " WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID + "  )   WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS + " = 0  AND " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";  "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_read_on_update AFTER  UPDATE OF " + SystemNoticeColumn.NOTICE_READ_STATUS + "  ON " + TABLES_NAME_SYSTEM_NOTICE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS + " = 0 AND " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + "; "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);


            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_update AFTER  UPDATE ON " + TABLES_NAME_SYSTEM_NOTICE + " "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "

                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = " + ECMessage.Direction.RECEIVE.ordinal() + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = " + ECMessage.MessageStatus.SUCCESS.ordinal() + " ;   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            sql = "CREATE TRIGGER IF NOT EXISTS thread_update_system_on_delete AFTER DELETE ON " + TABLES_NAME_IM_SESSION + " "
                    + "BEGIN DELETE FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + IThreadColumn.ID + ";END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);
        }

        public static void createImgInfoTable(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS "
                    + ImgInfoSqlManager.TABLES_NAME_IMGINFO
                    + " ( "
                    + ImgInfoSqlManager.ImgInfoColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ImgInfoSqlManager.ImgInfoColumn.MSGSVR_ID + " TEXT, "

                    + ImgInfoSqlManager.ImgInfoColumn.OFFSET + " INTEGER, "
                    + ImgInfoSqlManager.ImgInfoColumn.TOTALLEN + " INTEGER, "
                    + ImgInfoSqlManager.ImgInfoColumn.BIG_IMGPATH + " TEXT, "
                    + ImgInfoSqlManager.ImgInfoColumn.THUMBIMG_PATH + " TEXT, "
                    + ImgInfoSqlManager.ImgInfoColumn.CREATE_TIME + " TEXT, "
                    + ImgInfoSqlManager.ImgInfoColumn.MSG_LOCAL_ID + " TEXT, "
                    + ImgInfoSqlManager.ImgInfoColumn.STATUS + " INTEGER, "
                    + ImgInfoSqlManager.ImgInfoColumn.NET_TIMES + " TEXT "
                    + ")";

            LogUtil.v(TAG + ":" + sql);
            db.execSQL(sql);
        }
    }

    private static void onGroupUpgrade(SQLiteDatabase db) {
        String sql3 = "insert into groups2 select * , 1 from groups ";
        String sql4 = "delete from groups ";
        db.execSQL(sql3);
        LogUtil.v(TAG + ":" + sql3);
        db.execSQL(sql4);
        LogUtil.v(TAG + ":" + sql4);
    }


    class BaseColumn {
        public static final String ID = "ID";
        public static final String UNREAD_NUM = "unreadCount";
    }


    public class IThreadColumn extends BaseColumn {
        // 会话id
        public static final String THREAD_ID = "sessionId";
        // 总消息数
        public static final String MESSAGE_COUNT = "SumCount";
        // 最后一条消息发送者
        public static final String CONTACT_ID = "contactid";
        // 文本
        public static final String SNIPPET = "text";
        // 消息未读数
        public static final String UNREAD_COUNT = "unreadCount";
        // 消息的发送状态
        public static final String SEND_STATUS = "sendStatus";
        // 信箱类型
        public static final String BOX_TYPE = "boxType";
        // 消息时间
        public static final String DATE = "dateTime";
        // 消息类型
        public static final String MESSAGE_TYPE = "type";

    }

    public class IMessageColumn extends BaseColumn {

        // 消息ID
        public static final String MESSAGE_ID = "msgid";
        // 消息类型
        public static final String MESSAGE_TYPE = "msgType";
        // 会话ID
        public static final String OWN_THREAD_ID = "sid";
        // 消息创建者
        public static final String sender = "sender";
        // 是否已读
        public static final String READ_STATUS = "isRead";
        // 文本
        public static final String BODY = "text";
        // 信箱类型
        public static final String BOX_TYPE = "box_type";
        // 发送状态 -1发送失败 0发送成功 1发送中 2接收成功（默认为0 接收的消息）
        public static final String SEND_STATUS = "state";
        // 服务器时间 毫秒
        public static final String CREATE_DATE = "serverTime";
        // 入库本地时间 毫秒
        public static final String RECEIVE_DATE = "createdTime";
        // 用户自定义数据
        public static final String USER_DATA = "userData";
        // 下载路径
        public static final String FILE_URL = "url";
        // 文件本地路径
        public static final String FILE_PATH = "localPath";
        // 语音时间
        public static final String DURATION = "duration";
        public static final String VERSION = "version";
        // 备注
        public static final String REMARK = "remark";

    }

    /**
     * 联系人表
     */
    public class ContactsColumn extends BaseColumn {
        /**
         * 联系人账号
         */
        public static final String CONTACT_ID = "contact_id";
        /**
         * 联系人昵称
         */
        public static final String USERNAME = "username";
        /**
         * 联系人账号Token
         */
        public static final String TOKEN = "token";
        /**
         * 联系人子账号
         */
        public static final String SUBACCOUNT = "subAccount";
        /**
         * 联系人子账号Token
         */
        public static final String SUBTOKEN = "subToken";
        /**
         * 联系人类型
         */
        public static final String type = "type";
        /**
         * 备注
         */
        public static final String REMARK = "remark";
    }

    class GroupColumn extends BaseColumn {
        /**
         * 群组ID
         */
        public static final String GROUP_ID = "groupid";
        /**
         * 群组名称
         */
        public static final String GROUP_NAME = "name";
        /**
         * 群组创建者
         */
        public static final String GROUP_OWNER = "owner";
        /**
         * 群组类型 0:临时组(上限100人) 1:普通组(上限200人) 2:VIP组(上限500人)
         */
        public static final String GROUP_TYPE = "type";
        /**
         * 群组公告
         */
        public static final String GROUP_DECLARED = "declared";
        /**
         * 群组创建日期
         */
        public static final String GROUP_DATE_CREATED = "create_date";
        /**
         * 群组成员数
         */
        public static final String GROUP_MEMBER_COUNTS = "count";
        /**
         * 群组群组加入权限
         */
        public static final String GROUP_PERMISSION = "permission";
        /**
         * 群组是否加入
         */
        public static final String GROUP_JOINED = "joined";
        public static final String GROUP_ISNOTICE = "isnotice";

    }

    class GroupMembersColumn extends BaseColumn {
        // 所属群组
        public static final String OWN_GROUP_ID = "group_id";
        // 是否禁言
        public static final String ISBAN = "isban";
        // 用户voip账号
        public static final String VOIPACCOUNT = "voipaccount";
        // 性别
        public static final String SEX = "sex";
        // 用户生日
        public static final String BIRTH = "birth";
        // 用户电话
        public static final String TEL = "tel";
        // 用户的签名
        public static final String SIGN = "sign";
        // 用户邮箱
        public static final String MAIL = "mail";
        // 用户的备注
        public static final String ROLE = "role";
        public static final String REMARK = "remark";
        // 是否接收群组消息
        public static final String RULE = "rule";
    }

    /**
     * 群组通知接口字段
     */
    class SystemNoticeColumn extends BaseColumn {
        // 对应话数据库I的
        public static final String OWN_THREAD_ID = "sid";
        // 通知消息ID
        public static final String NOTICE_ID = "notice_id";
        // 通知消息验证理由
        public static final String NOTICE_VERIFYMSG = "verifymsg";
        public static final String NOTICE_DECLARED = "declared";
        // 管理员
        public static final String ADMIN = "admin";
        // 消息类型
        public static final String NOTICE_TYPE = "type";
        // 是否需要确认
        public static final String NOTICE_OPERATION_STATE = "confirm";
        // 群组ID
        public static final String NOTICE_GROUPID = "groupId";
        public static final String NOTICE_GROUPNAME = "groupName";
        // 联系人账号
        public static final String NOTICE_WHO = "member";
        public static final String NOTICE_NICKNAME = "nickName";
        // 已读状态
        public static final String NOTICE_READ_STATUS = "isRead";
        public static final String NOTICE_VERSION = "version";
        // 消息时间
        public static final String NOTICE_DATECREATED = "dateCreated";
    }

    private final MessageObservable mMsgObservable = new MessageObservable();

    protected void registerObserver(OnMessageChange observer) {
        mMsgObservable.registerObserver(observer);
    }

    protected void unregisterObserver(OnMessageChange observer) {
        mMsgObservable.unregisterObserver(observer);
    }

    protected void notifyChanged(String session) {
        mMsgObservable.notifyChanged(session);
    }


    protected void release() {
        destroy();
        closeDB();
        databaseHelper = null;
    }
}
