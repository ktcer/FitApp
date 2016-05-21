package com.cn.fit.ui.chat.storage;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * com.cn.aihu.ui.chat.storage in ECDemo_Android
 * Created by Jorstin on 2015/4/17.
 */
public class _DatabaseManager {
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static _DatabaseManager instance;
    private static AbstractSQLManager.DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new _DatabaseManager();
            mDatabaseHelper = (AbstractSQLManager.DatabaseHelper) helper;
        }
    }

    public static synchronized _DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(_DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }
}
