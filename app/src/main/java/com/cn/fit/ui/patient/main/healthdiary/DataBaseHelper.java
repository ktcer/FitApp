package com.cn.fit.ui.patient.main.healthdiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.cn.fit.model.healthdiary.BeanAlarm;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.model.personinfo.BeanPersonInfo;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kuangtiecheng
 * @date 创建时间�?015/10/23 下午5:32:43
 * @version 1.0
 * @parameter
 * @return
 */

/**
 * @author kuangtiecheng
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    // 版本
    public static final int VERSION = 2;
    // 数据库名
    public static final String NAME = "aihu";
    // 表名
    public static final String TABLE_DIARY_REMIND = "diary_remind";
    // 字段名
    public static final String ID = "id";
    public static final String USER_ID = "userid";
    public static final String DATE_DAY = "dateday";
    public static final String DAY_TIME = "daytime";
    public static final String CONTENT = "content";
    public static final String VALID = "valid";
    //操作
    public static final int INSERT = 0;
    public static final int DELETE = 1;
    public static final int QUERY = 2;
    public static final int UPDATE = 3;
    //删除标志
    public static final int VALID_FLAG = 1;
    public static final int INVALID_FLAG = 0;

    public static DataBaseHelper instance = null;

    public DataBaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    public DataBaseHelper(Context context, String name) {
        this(context, name, VERSION);
        // TODO Auto-generated constructor stub
    }

    public DataBaseHelper(Context context) {
        this(context, NAME, VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqlitedatabase) {
        // TODO Auto-generated method stub
        sqlitedatabase.execSQL(DatabaseUtils.CREAT_DIARY);
        sqlitedatabase.execSQL(DatabaseUtils.CREAT_USER);
        sqlitedatabase.execSQL(DatabaseUtils.CREAT_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        System.out.println("update a database  操作时间：  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        switch (oldVersion) {
            case 1:
                sqlitedatabase.execSQL(DatabaseUtils.CREAT_ALARM);
        }
    }

    /**
     * 获取DataBaseHelper
     *
     */
    public static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

    /**
     * 获取数据库
     *
     */
    public static SQLiteDatabase getDatabase(Context context) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(context);
        return dataBaseHelper.getReadableDatabase();
    }

    /**
     * 插入闹铃记录
     *
     */
    public static void insertAlarm(Context context, BeanAlarm beanAlarm) {
        String sql = "insert into alarm (id,dateday,daytime,valid) values (?,?,?,?)";
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        sqlitedatabase.execSQL(sql, new Object[]{beanAlarm.getId(), beanAlarm.getDateday(), beanAlarm.getDaytime(), beanAlarm.getValid()});
        sqlitedatabase.close();
    }

    /**
     * 查询闹铃记录
     * 外键查询(diary_remind查询alarm)
     */
    public static List<BeanAlarm> queryAlarm(Context context, Integer id) {
        List<BeanAlarm> list = new ArrayList<BeanAlarm>();
        String sql = "";
        if (id == null) {
            sql = "select * from alarm";
        } else {
            sql = "select * from alarm where id = " + id + " and valid = "
                    + VALID_FLAG;
        }
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        sqlitedatabase.rawQuery(sql, null);
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BeanAlarm bean = new BeanAlarm();
            bean.setAlarmid(cursor.getInt(0));
            bean.setId(cursor.getInt(1));
            bean.setDateday(cursor.getString(2));
            bean.setDaytime(cursor.getString(3));
            bean.setValid(cursor.getInt(6));
//			Log.e("=-=-=queryUser  BeanAlarm=-=-=", ""+bean);
            list.add(bean);
        }
        sqlitedatabase.close();
        return list;
    }

    /**
     * 删除闹铃记录
     *
     */
    public static void deleteAlarm(Context context, BeanAlarm beanAlarm) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = "update alarm set valid = " + INVALID_FLAG
                + " where alarmid = " + beanAlarm.getAlarmid();
//		Log.e("=-=-=updateUser sql=-=-=", sql);
        sqlitedatabase.execSQL(sql);
        sqlitedatabase.close();
    }

    /**
     * 更新闹铃记录
     *
     */
    public static void updateAlarm(Context context, BeanAlarm beanAlarm) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = "update alarm set dateday ='" + beanAlarm.getDateday() + "' , daytime = '"
                + beanAlarm.getDaytime() + "' where alarmid = "
                + beanAlarm.getAlarmid() + " and valid = " + VALID_FLAG;
//		Log.e("=-=-=updateUser sql=-=-=", sql);
        sqlitedatabase.execSQL(sql);
        sqlitedatabase.close();
    }

    /**
     * 插入个人信息记录
     *
     */
    public static void insertUser(Context context, BeanPersonInfo jsonInfo) {
        String sql = "insert into user (userid,userinfo) values (?,?)";
        String info = beanToJson(jsonInfo);
        SQLiteDatabase sqlitedatabase = getDatabase(context);
//		sqlitedatabase.execSQL(sql, new Object[]{jsonInfo.getUserID(),info});
        sqlitedatabase.close();
    }

    /**
     * 更改个人信息记录
     *
     */
    public static void updateUser(Context context, BeanPersonInfo jsonInfo) {
//		SQLiteDatabase sqlitedatabase = getDatabase(context);
//		String info = beanToJson(jsonInfo);
//		String sql = "update user set userinfo = '"+info+"' where userid="+jsonInfo.getUserID();
//		Log.e("=-=-=updateUser sql=-=-=", sql);
//		sqlitedatabase.execSQL(sql, new Object[]{jsonInfo.getUserID(),info});
//		sqlitedatabase.close();
    }

    /**
     * 查询个人信息记录
     *
     */
    public static List<BeanPersonInfo> queryUser(Context context, long userId) {
        List<BeanPersonInfo> list = new ArrayList<BeanPersonInfo>();
        String sql = "select * from user where userid=" + userId;
//		Log.e("=-=-=updateUser sql=-=-=", sql);
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        sqlitedatabase.rawQuery(sql, null);
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String info = cursor.getString(2);
            BeanPersonInfo bean = jsonToBean(info);
//			bean.setUserID(userId);
//			Log.e("=-=-=queryUser  bean=-=-=", ""+bean);
            list.add(bean);
        }
        sqlitedatabase.close();
        return list;
    }

    /**
     * json字符串转化为BeanPersonInfo
     *
     */
    public static BeanPersonInfo jsonToBean(String jsonInfo) {
        Gson gson = new Gson();
        BeanPersonInfo bean = gson.fromJson(jsonInfo, BeanPersonInfo.class);
        return bean;
    }

    /**
     * BeanPersonInfo转化为json字符串
     *
     */
    public static String beanToJson(BeanPersonInfo jsonInfo) {
        JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("userID", jsonInfo.getUserID());
//			jsonObject.put("userName", jsonInfo.getUserName());
//			jsonObject.put("videoNumber", jsonInfo.getVideoNumber());
//			jsonObject.put("height", jsonInfo.getHeight());
//			jsonObject.put("weight", jsonInfo.getWeight());
//			jsonObject.put("birth", jsonInfo.getBirth());
//			jsonObject.put("sex", jsonInfo.getSex());
//			jsonObject.put("gkNumber", jsonInfo.getGkNumber());
//			jsonObject.put("imgUrl", jsonInfo.getImgUrl());
//			Log.e("=-=-=beanToJson=-=-=", jsonObject.toString());
//			return jsonObject.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
        return null;
    }

    /**
     * 插入提醒记录
     *
     */
    public static void onInsert(Context context,
                                BeanHealthDiaryLocal beanHealthDiaryLocal) {
        String dateDay = beanHealthDiaryLocal.getDateday();
        String sql = generateSql(INSERT, beanHealthDiaryLocal);
        System.out.println("=-=-=onInsert sql=-=-=" + sql);
        SQLiteDatabase sqlitedatabase = getDatabase(context);

        beanHealthDiaryLocal.setDateday(dateDay);
        sqlitedatabase.execSQL(sql,
                new Object[]{beanHealthDiaryLocal.getUserid(),
                        beanHealthDiaryLocal.getDateday(),
                        beanHealthDiaryLocal.getDaytime(),
                        beanHealthDiaryLocal.getContent(),
                        beanHealthDiaryLocal.getPath(),
                        beanHealthDiaryLocal.getDiaryid(), VALID_FLAG});
        sqlitedatabase.close();
    }

    /**
     * 更新提醒记录
     *
     */
    public static void onUpdate(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = generateSql(UPDATE, beanHealthDiaryLocal);
        System.out.println("=-=-=onUpdate sql=-=-=" + sql);
        sqlitedatabase.execSQL(sql);
        sqlitedatabase.close();
    }

    /**
     * 删除提醒记录
     *
     */
    public static void onDelete(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = generateSql(DELETE, beanHealthDiaryLocal);
        System.out.println("=-=-=onDelete sql=-=-=" + sql);
        sqlitedatabase.execSQL(sql);
        sqlitedatabase.close();
    }

    /**
     * 查询当天的某一条(所有)提醒
     * beanHealthDiaryLocal的dayTime是否为空
     * @return
     *
     */
    public static ArrayList<BeanHealthDiaryLocal> onQuery(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        ArrayList<BeanHealthDiaryLocal> list = new ArrayList<BeanHealthDiaryLocal>();
        String sql = generateSql(QUERY, beanHealthDiaryLocal);
        System.out.println("=-=-=onQuery sql=-=-=" + sql);
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BeanHealthDiaryLocal bean = new BeanHealthDiaryLocal();
            bean.setId(cursor.getInt(0));
            bean.setUserid(cursor.getInt(1));
            bean.setDateday(cursor.getString(2));
            bean.setDaytime(cursor.getString(3));
            bean.setContent(cursor.getString(4));
            bean.setPath(cursor.getString(5));
            bean.setDiaryid(cursor.getInt(6));
            bean.setValid(cursor.getInt(7));
            list.add(bean);
        }
        sqlitedatabase.close();
        return list;
    }

    /**
     * 更改数据表结构
     *
     */
    public void onAlter() {

    }

    /**
     * 生成sql type=INSERT,DELETE,QUERY;增删查
     */
    public static String generateSql(int type, BeanHealthDiaryLocal beanHealthDiaryLocal) {
        StringBuilder builder = new StringBuilder();
        switch (type) {
            case INSERT:
                builder.append("insert into diary_remind (userid,dateday,daytime,content,path,diaryid,valid) values (?,?,?,?,?,?,?)");
                break;
            case DELETE:
                if (beanHealthDiaryLocal.getDaytime() != null)// 按时间点删除
                    builder.append("update diary_remind set valid = "
                            + INVALID_FLAG + " where userid="
                            + beanHealthDiaryLocal.getUserid() + " and daytime = '"
                            + beanHealthDiaryLocal.getDaytime() + "'");
                else if (beanHealthDiaryLocal.getId() != null)// 按提醒id删除
                    builder.append("update diary_remind set valid = "
                            + INVALID_FLAG + " where id ="
                            + beanHealthDiaryLocal.getId());
                else // 过期删除
                    builder.append("update diary_remind set valid = "
                            + INVALID_FLAG + " where userid="
                            + beanHealthDiaryLocal.getUserid());
                break;
            case QUERY:
                if (beanHealthDiaryLocal.getId() != null) {// 按提醒id查
                    builder.append("select * from diary_remind where id = "
                            + beanHealthDiaryLocal.getId() + " and valid= "
                            + VALID_FLAG);
                } else {// 所有有效的
                    builder.append("select * from diary_remind where userid = "
                            + beanHealthDiaryLocal.getUserid() + " and valid= "
                            + VALID_FLAG);
                }

//			if (beanHealthDiaryLocal.getDateday() != null)// 按天查
//				builder.append("select * from diary_remind where userid = "
//						+ beanHealthDiaryLocal.getUserid() + " and dateday='"
//						+ beanHealthDiaryLocal.getDateday() + "' and valid= "
//						+ VALID_FLAG);
//			else if (beanHealthDiaryLocal.getDaytime() != null) // 按时间点查
//				builder.append("select * from diary_remind where userid = "
//						+ beanHealthDiaryLocal.getUserid() + " and daytime ='"
//						+ beanHealthDiaryLocal.getDaytime() + "' and valid= "
//						+ VALID_FLAG);
//			else if (beanHealthDiaryLocal.getId() != null)// 按日记id查
//				builder.append("select * from diary_remind where id = "				
//						+ beanHealthDiaryLocal.getId() + " and valid= "
//						+ VALID_FLAG);							
//			else  // 所有有效的
//				builder.append("select * from diary_remind where userid = "
//						+ beanHealthDiaryLocal.getUserid() + " and valid= "
//						+ VALID_FLAG);
                break;
            case UPDATE:
                if (beanHealthDiaryLocal.getContent().length() > 0 || beanHealthDiaryLocal.getContent() != null) {
                    builder.append("update diary_remind set dateday = '"
                            + beanHealthDiaryLocal.getDateday() + "' , daytime = '"
                            + beanHealthDiaryLocal.getDaytime() + "' , content = '"
                            + beanHealthDiaryLocal.getContent() + "' where id="
                            + beanHealthDiaryLocal.getId());
                } else {
                    builder.append("update diary_remind set dateday = '"
                            + beanHealthDiaryLocal.getDateday() + "' , daytime = '"
                            + beanHealthDiaryLocal.getDaytime() + "' where id="
                            + beanHealthDiaryLocal.getId());
                }
                break;
            default:
                break;
        }
        return builder.toString();
    }

    /**
     * 初始化,录入原有表的内容
     * 主要用来升级alarm表
     * 应对版本1到版本2，业务逻辑
     */
    public static void initDatabase(Context context, BeanHealthDiaryLocal beanHealthDiaryLocal) {
        List<BeanAlarm> list = DataBaseHelper.queryAlarm(context, null);
        if (list.size() == 0) {//alarm表为空，执行
            ArrayList<BeanHealthDiaryLocal> listBeanHealthDiaryLocal = DataBaseHelper.onQuery(context, beanHealthDiaryLocal);
            if (listBeanHealthDiaryLocal.size() > 0) {
                for (int i = 0; i < listBeanHealthDiaryLocal.size(); i++) {
                    BeanHealthDiaryLocal bean = listBeanHealthDiaryLocal.get(i);
                    String[] array = bean.getDateday().split(",");
                    for (int j = 0; j < array.length; j++) {
                        BeanAlarm beanAlarm = new BeanAlarm();
                        beanAlarm.setDateday(array[j]);
                        beanAlarm.setDaytime(bean.getDaytime());
                        beanAlarm.setId(bean.getId());
                        beanAlarm.setValid(DataBaseHelper.VALID_FLAG);
                        DataBaseHelper.insertAlarm(context, beanAlarm);
                    }
                    DataBaseHelper.printAlarm(context, "alarm");
                    try {
                        AlarmUtils.setAlarm(context, bean);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 输出diary_remind表的内容
     */
    public static void print(Context context, String tableName) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = "select * from " + tableName;
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int userId = cursor.getInt(1);
            String day = cursor.getString(2);
            String time = cursor.getString(3);
            String content = cursor.getString(4);
            String path = cursor.getString(5);
            int diaryid = cursor.getInt(6);
            int valid = cursor.getInt(7);
            System.out.println(tableName + "内容  id: " + id + " userId: " + userId
                    + " day: " + day + " time: " + time + " content: "
                    + content + " path: " + path + " diaryid: " + diaryid
                    + " valid: " + valid);
        }
        sqlitedatabase.close();
    }

    /**
     * 输出alarm表的内容
     */
    public static void printAlarm(Context context, String tableName) {
        SQLiteDatabase sqlitedatabase = getDatabase(context);
        String sql = "select * from " + tableName;
        Cursor cursor = sqlitedatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int alarmid = cursor.getInt(0);
            int id = cursor.getInt(1);
            String day = cursor.getString(2);
            String time = cursor.getString(3);
            int valid = cursor.getInt(6);
            System.out.println(tableName + "内容  alarmid: " + alarmid + " id: " + id
                    + " day: " + day + " time: " + time
                    + " valid: " + valid);
        }
        sqlitedatabase.close();
    }
}
