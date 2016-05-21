package com.cn.fit.ui;

/**
 * 推送消息给Activity
 *
 * @author kuangtiecheng
 */
public interface IfacePush {

    /**
     * 推送当前应用占用内存的数据
     *
     * @param value String
     */
    public void pushMemoryInfo(String value);
}
