package com.cn.fit.ui.patient.others.myaccount;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.personinfo.BeanOverage;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ActivityMyConis extends ActivityBasicListView {
    private List<BeanOverage> infoList, tempInfoList;
    private AdapterMyCoins adapter;
    protected int pageNum = 1;
    private float coinUnitprice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyaxlistview);
        initView();
    }

    private void initView() {
        infoList = new ArrayList<BeanOverage>();
        tempInfoList = new ArrayList<BeanOverage>();
        adapter = new AdapterMyCoins(infoList, this);
        ((TextView) this.findViewById(R.id.middle_tv)).setText("我的金币");
        listView = (XListView) findViewById(R.id.xlist);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // String url = infoList.get(position-1).getVedioUrl();
                // Intent intent =new Intent(getActivity(),VideoPlay.class);
                // Bundle mBundle = new Bundle();
                // mBundle.putString(Constant.VEDIO_URL, url);
                // intent.putExtras(mBundle);
                // startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        adapter.setHistoryShow(new AdapterMyCoins.HistoryShow() {
            @Override
            public void showHistory() {
                System.out.println("跳转到金币历史查询接口");
                Intent i = new Intent(ActivityMyConis.this, CoinsHistory.class);
                startActivity(i);
            }
        });
        showProgressBar();
        AscynCoinHistory achTask = new AscynCoinHistory(this, pageNum);
        achTask.execute();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // getMyCoins();

        showProgressBar();
        AscynCoinHistory achTask = new AscynCoinHistory(this, pageNum);
        achTask.execute();
    }

    private void getMyCoins() {
        // showProgressBar();
        AsyncGetMyCoins mTask = new AsyncGetMyCoins(this) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
                // adapter.notifyDataSetChanged();
            }

        };
        mTask.execute();

    }

    private class AscynCoinHistory extends AsyncTask<Integer, Integer, String> {

        String result = "";
        private ActivityBasic act;
        private String requestAddInfo = "/goldcoins/coinhistory";
        private long userId;
        private int pageNum;
        private List<BeanOverage> beanOveragelist;

        public AscynCoinHistory(ActivityBasic act, int pageNum) {
            this.act = act;
            this.pageNum = pageNum;
            UtilsSharedData.initDataShare(act);// ////////
            userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            beanOveragelist = new ArrayList<BeanOverage>();
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", userId + "");
            param.put("userType", 0 + "");
            param.put("pageNum", pageNum + "");
            if (pageNum == 1) {
                param.put("pageSize", 9 + "");
            } else {
                param.put("pageSize", 10 + "");
            }
            ;

            try {
                String url = AbsParam.getBaseUrl() + requestAddInfo;
                Log.i("input", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                // result = result.substring(1, result.length()-4);
                Log.i("result", result);
                tempInfoList.clear();
                if (pageNum == 1) {
                    BeanOverage bean = new BeanOverage();
                    bean.setItem("d");
                    tempInfoList.add(bean);

                } else {

                }
                JsonArrayToList(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }

            } catch (Exception e) {
                canLoadMore = false;
                hideProgressBar();
                // ((ActivityBasic) act).hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }

            for (BeanOverage tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            getMyCoins();
            adapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
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
            // List<BeanOverage> beanOveragelist = new ArrayList<BeanOverage>();

            // 添加我自己的信息
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    tempInfoList
                            .addAll((Collection<? extends BeanOverage>) gson
                                    .fromJson(jsonString,
                                            new TypeToken<List<BeanOverage>>() {
                                            }.getType()));
                }
            }
        }

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        // getMyCoins();
        // showProgressBar();
        pageNum = 1;
        AscynCoinHistory achTask = new AscynCoinHistory(this, pageNum);
        achTask.execute();
    }

    private AscynCoinHistory achTask;

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        // getMyCoins();
        if (canLoadMore) {
            if (achTask != null
                    && achTask.getStatus() == AsyncTask.Status.RUNNING) {
                achTask.cancel(true); // 如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            // showProgressBar();
            achTask = new AscynCoinHistory(this, pageNum);
            achTask.execute();
        }
    }

}
