package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.ui.chat.ui.TabFragmentListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.refreshlistview.XListView;
import com.cn.fit.utils.listviewwithgridview.ListViewItem;
import com.cn.fit.utils.listviewwithgridview.ListWithGridViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentHealthPost extends TabFragmentListView {

    private int page = 1;
    public final static int PAGE_SIZE = 4; // 每次加载4个item
    private View rootView;// 缓存Fragment view
    private List<ListViewItem> listItem = new ArrayList<ListViewItem>();
    private ListWithGridViewAdapter mAdapter;
    private List<BeanHealthPost> infoList, tempInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_onlyaxlistview, null);
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        rootView.findViewById(R.id.titlebar).setVisibility(View.GONE);
        listView = (XListView) rootView.findViewById(R.id.xlist);
        // 设置Adapter
        mAdapter = new ListWithGridViewAdapter(getActivity(), listItem);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        infoList = new ArrayList<BeanHealthPost>();
        tempInfoList = new ArrayList<BeanHealthPost>();
        listView.setAdapter(mAdapter);
        // 点击事件
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Toast.makeText(getActivity(),
//						"listview position = " + position, Toast.LENGTH_SHORT)
//						.show();
//			}
//		});
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        showProgressBar();
        QueryHealthPostList taskQNL = new QueryHealthPostList();
        taskQNL.execute();
    }

    /**
     * 箱item中增加数据
     *
     * @return
     */
    private List<ListViewItem> mockData(List<BeanHealthPost> bean) {
        List<ListViewItem> items = new ArrayList<ListViewItem>();
        ListViewItem item = new ListViewItem();
        item.mTitle = "高血糖健康之旅";
        item.listBeanHealthPost = bean;
        items.add(item);

        item = new ListViewItem();
        item.mTitle = "高血压健康之旅";
        item.listBeanHealthPost = bean;
        items.add(item);

        return items;

    }


    /**
     * 查询护士列表
     *
     * @author kuangtiecheng
     */
    private class QueryHealthPostList extends
            AsyncTask<Integer, Integer, String> {

        public QueryHealthPostList() {
            super();
        }

        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("pageSize", PAGE_SIZE + "");
            param.put("pageNum", page + "");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/travel/getlist", param,
                        "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                JsonArrayToList(result);
                if (tempInfoList.size() < 5) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                e.printStackTrace();
                hideProgressBar();

            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void JsonArrayToList(String jsonString) throws Exception {
            Gson gson = new Gson();
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    tempInfoList = gson.fromJson(jsonString,
                            new TypeToken<List<BeanHealthPost>>() {
                            }.getType());

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanHealthPost tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            listItem.clear();
            listItem.addAll(mockData(infoList));
            mAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();

        }
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        // showProgressBar();
        QueryHealthPostList taskQNL = new QueryHealthPostList();
        taskQNL.execute();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            QueryHealthPostList taskQNL = new QueryHealthPostList();
            taskQNL.execute();
        }
    }


    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_onlyaxlistview;
    }

    @Override
    public void onTabFragmentClick() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onReleaseTabUI() {
        // TODO Auto-generated method stub

    }

}
