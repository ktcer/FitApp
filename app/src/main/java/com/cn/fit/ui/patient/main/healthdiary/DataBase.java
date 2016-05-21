package com.cn.fit.ui.patient.main.healthdiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/23 下午5:49:18
 * @parameter
 * @return
 */
public class DataBase {
    public Context context;
    public DataBase instance;

    private DataBase(Context context) {
        super();
        this.context = context;
    }

    public DataBase getInstance() {
        if (instance == null)
            return new DataBase(context);
        else
            return instance;
    }

    public void init() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        database.close();
    }
}
