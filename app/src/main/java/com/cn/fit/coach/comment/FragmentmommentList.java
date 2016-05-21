/*
 * 订单列表fragment
 */

package com.cn.fit.coach.comment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanQureyMomment;
import com.cn.fit.ui.basic.FragmentBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ResourceType")
public class FragmentmommentList extends FragmentBasicListView {

    private static final String ARG_POSITION = "position";
    private List<BeanQureyMomment> infoList, tempInfoList;
    private int type;
    private int pageNum = 1;
    private AdaperQureyMomment listViewAdapter;
    private long coachID;

    public static FragmentmommentList newInstance(int position, long coachID) {
        FragmentmommentList f = new FragmentmommentList();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putLong("coachID", coachID);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_POSITION);
        coachID = getArguments().getLong("coachID");
        infoList = new ArrayList<BeanQureyMomment>();
        tempInfoList = new ArrayList<BeanQureyMomment>();
        switch (type) {
            case 0:
                type = 2;
                break;
            case 1:
                type = 1;
                break;
            default:
                type = 0;
                break;

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
//				.getDisplayMetrics());
        listViewAdapter = new AdaperQureyMomment(getActivity().getApplicationContext(), infoList);
        listView = new XListView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(R.color.black);
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                Intent intent = new Intent(getActivity(), ActivityCoachApply.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable(Constant.BEAN_DISCOVERY, infoList.get(position-1));
//                intent.putExtras(mBundle);
//                intent.putExtra("buyflag",true);
//                if(l.length==2){
//                    intent.putExtra(Constant.LATITUDE, l[0]);
//                    intent.putExtra(Constant.LONGITUDE, l[1]);
//                }else{
//                    intent.putExtra(Constant.LATITUDE, latitude);
//                    intent.putExtra(Constant.LONGITUDE, longitude);
//                }
//                startActivity(intent);
//            }
//        });

        listView.setAdapter(listViewAdapter);
        fl.addView(listView);
        return fl;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryMomment task = new QueryMomment();
        task.execute(type);
    }

    private class QueryMomment extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(getActivity());
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("coachID", coachID + "");
            param.put("state", "" + params[0]);//
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl() + "/home/app/getcomments";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                jsonToArray(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanQureyMomment tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            listViewAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }


    private void jsonToArray(String json) {
        Gson gson = new Gson();
        tempInfoList = gson.fromJson(json, new TypeToken<List<BeanQureyMomment>>() {
        }.getType());
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        QueryMomment task = new QueryMomment();
        task.execute(type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    QueryMomment task;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            if (task != null
                    && task.getStatus() == AsyncTask.Status.RUNNING) {
                task.cancel(true); // 如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            task = new QueryMomment();
            task.execute(type);
        }
    }

}