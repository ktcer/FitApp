package com.cn.fit.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.fit.model.detect.BeanDetectData;

/**
 * 监测数据数据库操作
 *
 * @author kuangtiecheng
 */
public class DaoDetectData extends DataBase {

    public DaoDetectData() {
        super();
    }

    /**
     * 向数据库插入新的监测数据
     *
     * @param
     * @return 如果返回-1插入失败
     */
    public long addNewDetectData(BeanDetectData CallRecord) {
        mysql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        ContentValues values = new ContentValues();
        values.put("type", CallRecord.getType());
        values.put("value", CallRecord.getValue());
        values.put("unit", CallRecord.getUnit());
        values.put("refmaxvalue", CallRecord.getRefmaxvalue());
        values.put("refminvalue", CallRecord.getRefminvalue());
        values.put("subtype", CallRecord.getSubtype());
        values.put("time", CallRecord.getTime());
        values.put("submit_type", CallRecord.getSubmit_type());
        values.put("userid", CallRecord.getUserid());
        long count = mysql.insert("detect_data", null, values);
        mysql.close();// 关闭数据库
        return count;
    }

    /**
     * 根据用户id查询全部历史监测数据
     *
     * @return
     */
    public List<BeanDetectData> queryAllDetectDataByUserId(int id) {
        mysql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor c = null;
        c = mysql.rawQuery("SELECT * FROM detect_data WHERE userid=?  ORDER BY time DESC",
                new String[]{String.valueOf(id)});
        List<BeanDetectData> list = new ArrayList<BeanDetectData>();
        while (c.moveToNext()) { // 从Cursor中获取数据, 封装成OrderBean对象
            int taskNum = c.getInt(0);
            String type = c.getString(1);
            double value = c.getDouble(2);
            String unit = c.getString(3);
            double refmaxvalue = c.getDouble(4);
            double refminvalue = c.getDouble(5);
            int subtype = c.getInt(6);
            String time = c.getString(7);
            int submit_type = c.getInt(8);
            int userid = c.getInt(9);
            list.add(new BeanDetectData(taskNum, type, value, unit,
                    refmaxvalue, refminvalue, subtype, time,
                    submit_type, userid));
        }
        c.close();
        mysql.close();
        return list;
    }


    /**
     * 删除监测数据
     *
     * @param id
     * @return 如果返回-1插入失败
     */
    public int deleteDetectDataByUserId(int userid) {
        mysql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        int count = mysql.delete("detect_data", "userid=?",
                new String[]{String.valueOf(userid)});
        mysql.close();
        return count;
    }

    /**
     * 删除所有监测数据
     */
    public void deleteAllDetectData() {
        mysql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        mysql.execSQL("DELETE FROM detect_data");
        mysql.close();
    }

    /**
     * 查询电话记录条数
     *
     * @return int
     */
    public int queryDetectDataCount() {
        mysql = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor c = mysql.query("detect_data", null, null, null, null, null, null);
        int count = c.getCount();
        c.close(); // 关闭结果集
        mysql.close();
        return count;
    }
}