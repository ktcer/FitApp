/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
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

import android.view.KeyEvent;

import com.cn.fit.util.refreshlistview.XListView;
import com.cn.fit.util.refreshlistview.XListView.IXListViewListener;

/**
 * 自定义三个TabView 的Fragment ,将三个TabView共同属性方法
 * 统一处理
 * 三个TabView 滑动页面需要继承该基类，
 * Created by Jorstin on 2015/3/18.
 */
public abstract class TabFragmentListView extends BaseFragment implements IXListViewListener {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        onReleaseTabUI();
        super.onDestroy();
    }

    protected int pageNum = 1;
    protected boolean canLoadMore = true;
    protected XListView listView;

    /**
     * 停止刷新，
     */
    protected void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        pageNum = 1;
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        if (canLoadMore) {
            pageNum++;
        }
    }

    /**
     * 当前TabFragment被点击
     */
    public abstract void onTabFragmentClick();

    /**
     * 当前TabFragment被释放
     */
    protected abstract void onReleaseTabUI();

}

