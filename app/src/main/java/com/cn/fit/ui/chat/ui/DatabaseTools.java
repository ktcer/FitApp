package com.cn.fit.ui.chat.ui;

/**
 * @author 丑旦
 * @version 1.0
 * @date 创建时间：2015/11/23
 * @parameter 用户端和秘书端的databaseVersion不一样
 * @return databaseVersion
 */
public class DatabaseTools {
    public final static int databaseVersion = 8;

    public static int getDatabaseversion() {
        return databaseVersion;
    }
}


