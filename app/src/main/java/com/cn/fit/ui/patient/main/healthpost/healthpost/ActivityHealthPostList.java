package com.cn.fit.ui.patient.main.healthpost.healthpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.main.healthpost.healthpost.HealthPostDataLoader.OnCompletedListener;
import com.cn.fit.util.Constant;
import com.cn.fit.util.refreshgridview.FooterView;

public class ActivityHealthPostList extends ActivityBasic implements
        OnCompletedListener {
    private GridView gridview;
    private HealthPostDataLoader loader;
    private int page = 1;
    public final static int PAGE_SIZE = 10; // 每次加载10个item
    private final static int COUNT = 215;// 最多加载215个，确定加载数。
    private AdapterHealthPostItem adapter;
    private List<BeanHealthPost> healthPostList = new ArrayList<BeanHealthPost>();
    //	private View rootView;// 缓存Fragment view	private boolean isLoadFinished;
    private HashMap<String, String> loaderMap = new HashMap<String, String>();
    private boolean isLoadFinished;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_health_post);
        gridview = (GridView) findViewById(R.id.gridview);
        TextView title = (TextView) findViewById(R.id.middle_tv);
        title.setText("健康驿站");
        initGridView();

        loaderMap.put("page", page + "");
        loaderMap.put("page_size", PAGE_SIZE + "");

        loader = new HealthPostDataLoader(this);
        loader.setOnCompletedListerner(this);
        loader.startLoading(loaderMap);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foot_view_layout:
                if (adapter != null
                        && adapter.getFooterView().getStatus() == FooterView.MORE) {
                    loadMoreData();
                }
                break;
            case R.id.footview_button:
                loadMoreData();
                break;
        }

    }

    /**
     * 加载下个页码的数据
     */
    private void loadMoreData() {
        if (loader != null) {

            page = page + 1;
            loaderMap.put("page", page + "");
            if (adapter != null) {
                adapter.setFooterViewStatus(FooterView.LOADING);
            }

            loader.startLoading(loaderMap);
        }
    }

    /**
     * 实例化控件及数据
     */
    private void initGridView() {
        adapter = new AdapterHealthPostItem(this, healthPostList);
        adapter.setOnFooterViewClickListener(this);
        gridview.setAdapter(adapter);
        gridview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滚动到底部自动加载(很重要)
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)
                            && !isLoadFinished
                            && adapter.getFooterView().getStatus() != FooterView.LOADING) {
                        loadMoreData();// 加载数据

                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ActivityHealthPostList.this, ActivityHealthPostDetail.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constant.TOUR_BEAN, healthPostList.get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
            }


        });
    }

    @Override
    public void onCompletedSucceed(List<BeanHealthPost> l) {

        // 在添加数据之前删除最后的伪造item
        if (adapter.isFooterViewEnable()) {
            healthPostList.remove(healthPostList.get(healthPostList.size() - 1));
        }

        // 分页加载
        if (l.size() < PAGE_SIZE || healthPostList.size() + l.size() == COUNT) {
            // 如果加载出来的数目小于指定条数，可视为已全部加载完成
            isLoadFinished = true;
            healthPostList.addAll(l);
            adapter.setFootreViewEnable(false);
            adapter.notifyDataSetChanged();
//			Toast.makeText(this, "没有更多了", 1000).show();
        } else {
            // 还有数据可加载。
            healthPostList.addAll(l);
            // 伪造一个空项来构造一个footerview;
            healthPostList.add(null);
            adapter.setFootreViewEnable(true);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCompletedFailed(String str) {
        Toast.makeText(this, "出错啦！", Toast.LENGTH_LONG).show();

    }

    @Override
    public void getCount(int count) {
        // TODO Auto-generated method stub

    }
}
