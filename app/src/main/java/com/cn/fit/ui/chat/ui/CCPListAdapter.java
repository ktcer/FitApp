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
package com.cn.fit.ui.chat.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import com.cn.fit.ui.chat.storage.OnMessageChange;

/**
 * 定义抽象适配器，继承扩展不同的消息
 * Created by Jorstin on 2015/3/18.
 */
public abstract class CCPListAdapter<T> extends BaseAdapter implements OnMessageChange {

    /**
     * 数据Cursor
     */
    private Cursor mCursor;
    /**
     * 数据缓存
     */
    private Map<Integer, T> mData;
    /**
     * 适配器使用数据类型
     */
    protected T t;
    /**
     * 上下文对象
     */
    protected Context mContext;
    /**
     * 数据总数
     */
    protected int mCount;
    /**
     * 数据改变回调接口
     */
    protected OnCursorChangeListener mOnCursorChangeListener;

    /**
     * 构造方法
     *
     * @param ctx
     * @param t
     */
    public CCPListAdapter(Context ctx, T t) {
        mContext = ctx;
        this.t = t;
        this.mCount = -1;
    }

    protected void setCursor(Cursor cursor) {
        mCursor = cursor;
        this.mCount = -1;
    }

    public void initCache() {
        if (mData != null) {
            return;
        }
        mData = new HashMap<Integer, T>();
    }

    /**
     * 返回一个数据类型Cursor
     *
     * @return
     */
    protected Cursor getCursor() {
        if (mCursor == null) {
            initCursor();
//            try {
//            	Assert.assertNotNull(mCursor);
//			} catch (Exception e) {
//				System.out.println(e.toString());
//			}

        }
        return mCursor;
    }

    public void setOnCursorChangeListener(OnCursorChangeListener listener) {
        this.mOnCursorChangeListener = listener;
    }

    public void resetListener() {
        this.mOnCursorChangeListener = null;
    }

    /***
     * 关闭数据库
     */
    public void closeCursor() {
        if (mData != null) {
            mData.clear();
        }
        if (mCursor != null) {
            mCursor.close();
        }
        mCount = -1;
    }

    @Override
    public int getCount() {
        if (mCount < 0) {
            mCount = getCursor().getCount();
        }
        return mCount;
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || !getCursor().moveToPosition(position)) {
            return null;
        }

        if (mData == null) {
            return getItem(this.t, getCursor());
        }

        T _t = mData.get(Integer.valueOf(position));
        if (_t == null) {
            _t = getItem(null, getCursor());
        }
        mData.put(Integer.valueOf(position), _t);
        return _t;
    }

    @Override
    public long getItemId(int position) {

        return 0;

    }

    @Override
    public void onChanged(String sessionId) {
        if (mOnCursorChangeListener != null) {
            mOnCursorChangeListener.onCursorChangeBefore();
        }
        closeCursor();
        notifyChange();

        if (mOnCursorChangeListener == null) {
            return;
        }
        mOnCursorChangeListener.onCursorChangeAfter();
    }

    protected abstract void notifyChange();

    protected abstract void initCursor();

    protected abstract T getItem(T t, Cursor cursor);


    public interface OnListAdapterCallBackListener {
        void OnListAdapterCallBack();
    }


    public interface OnCursorChangeListener {
        void onCursorChangeBefore();

        void onCursorChangeAfter();
    }
}

