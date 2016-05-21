package com.cn.fit.ui.chat.ui.chatting;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.ui.CCPListAdapter;
import com.yuntongxun.ecsdk.ECMessage;

/**
 * com.cn.aihu.ui.chat.ui.chatting in ECDemo_Android
 * Created by Jorstin on 2015/4/8.
 */
public class ChattingListAdapter2 extends CCPListAdapter<ECMessage> {

    /**
     * 构造方法
     *
     * @param ctx
     * @param ecMessage
     */
    public ChattingListAdapter2(Context ctx, ECMessage ecMessage) {
        super(ctx, ecMessage);
    }

    @Override
    protected void notifyChange() {

    }

    @Override
    protected void initCursor() {
        // 初始化一个空的数据列表
        setCursor(IMessageSqlManager.getNullCursor());
    }

    @Override
    protected ECMessage getItem(ECMessage ecMessage, Cursor cursor) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
