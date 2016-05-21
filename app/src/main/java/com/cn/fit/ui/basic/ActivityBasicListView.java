package com.cn.fit.ui.basic;

import android.os.Bundle;

import com.cn.fit.util.refreshlistview.XListView;
import com.cn.fit.util.refreshlistview.XListView.IXListViewListener;

public class ActivityBasicListView extends ActivityBasic implements IXListViewListener {

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
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    //友盟的统计接口
    public void onResume() {
        super.onResume();
    }

    //友盟的统计接口
    public void onPause() {
        super.onPause();
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

}