package com.cn.fit.dao;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cn.fit.util.AppDisk;
import com.cn.fit.util.LogPrint;

/**
 * 数据库接口
 *
 * @author kuangtiecheng
 */
public class DataBase {

    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "inurse.db";

    /**
     * 日期时间格式化
     */
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    protected SQLiteDatabase mysql;
    protected File dbFile;

    /**
     * 初始化当前数据库文件
     *
     * @param context Context
     */
    public DataBase() {
        dbFile = new File(AppDisk.appInursePath + DATABASE_NAME);
    }

    //TODO 以下是改进后的数据库模式

    protected SQLiteDatabase database;

    /**
     * 初始化当前数据库文件
     *
     * @param context Context
     */
    public DataBase(Context context) {
//		super(new CustomContextWrapper(context, PATH_DB),
//				DATABASE_NAME, null, DATABASE_VERSION);
//		database = getWritableDatabase();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        try {
            if (database.isOpen()) {
                database.close();
            }
        } catch (SQLiteException e) {
            LogPrint.debug("数据库关闭异常", e);
        } finally {
            database = null;
        }
    }

    //	@Override
    public void onCreate(SQLiteDatabase db) {
        String tabNameRece = "DataReceive";
        String tabSqlRece = "CREATE TABLE DataReceive (inTime TEXT PRIMARY KEY NOT NULL, dev INTEGER, content TEXT)";
        createTable(db, tabNameRece, tabSqlRece);

        String tabNameSend = "DataSend";
        String tabSqlSend = "CREATE TABLE DataSend (inTime TEXT PRIMARY KEY NOT NULL, uniqueID TEXT, dev INTEGER, content TEXT, flag INTEGER)";
        createTable(db, tabNameSend, tabSqlSend);

        printTableName(db);
    }

    private void createTable(SQLiteDatabase db, String tabName, String tabSql) {
        if (!existTable(db, tabName)) {
            LogPrint.debug("建表：" + tabSql);
            db.execSQL(tabSql);
        }
    }

    //	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogPrint.warn("[DataBase_onUpgrade] >>> oldVersion=" + oldVersion + " | newVersion=" + newVersion);
    }

    /**
     * 检查表是否存在
     *
     * @param database  SQLiteDatabase
     * @param tableName String
     * @return boolean
     */
    private boolean existTable(SQLiteDatabase database, String tableName) {
        LogPrint.debug("tableName >>> " + tableName);
        Cursor cursor = database.rawQuery(
                "select tbl_name from sqlite_master where type='table' and name='"
                        + tableName + "'", null);
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打印表名
     */
    private void printTableName(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery(
                "select tbl_name from sqlite_master where type='table'", null);
        if (cursor == null) {
            return;
        }
        List<String> tblList = new ArrayList<String>(); // 表名
        while (cursor.moveToNext()) {
            tblList.add(cursor.getString(0));
        }
        cursor.close();
        cursor = null;
        LogPrint.debug("数据库表名：" + tblList);
        for (String tblName : tblList) {
            Cursor curDB = null;
            try {
                curDB = database.query(tblName, new String[]{" * "}, null,
                        null, null, null, null);
                if (null != curDB) {
                    LogPrint.debug("[" + tblName + "] 表含数据 " + curDB.getCount()
                            + " 条...");
                }
            } catch (Exception e) {
                LogPrint.debug("表 [" + tblName + "] 查询异常", e);
            } finally {
                if (null != curDB) {
                    curDB.close();
                    curDB = null;
                }
            }
        }
    }
}