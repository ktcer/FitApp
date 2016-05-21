package com.cn.fit.ui.patient.others.myfamily;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.family.BeanFamilyMemberInfo;
import com.cn.fit.model.family.BeanFamilyResult;
import com.cn.fit.model.family.BeanResultUtils;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityAddNews extends ActivityBasicListView {
    private static String family = "/family/request/";
    private TextView title;
    private List<BeanFamilyMemberInfo> newsList, tempnewsList;
    private NewsListAdapter newsListAdapter;
    private String operation = "";
    private BeanFamilyResult beanFamilyResult;
    private int selectMember = 0;
    protected int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_add_news);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("添加消息");
//
        newsListAdapter = new NewsListAdapter(this);
        newsList = new ArrayList<BeanFamilyMemberInfo>();
        tempnewsList = new ArrayList<BeanFamilyMemberInfo>();
        listView = (XListView) findViewById(R.id.add_news_lv);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectMember = arg2 - 1;
//				memberName = newsList.get(arg2-1).getMemberName();
//				memberID = newsList.get(arg2-1).getMemberID();
                showDialog();
            }
        });
        listView.setAdapter(newsListAdapter);
    }


    public void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(
                ActivityAddNews.this);
        builder.setTitle("提示");
        String content = "您是否同意用户" + " " + newsList.get(selectMember).getOwnerName() + " " + "添加您为家庭成员？";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(Color.RED), 7, newsList.get(selectMember).getOwnerName().length() + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(style);
        builder.setPositiveButton("同意",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        operation = "agree";
                        showProgressBar();
                        ConfirmFamilyAddInfoTask confirmFamilyAddInfoTask = new ConfirmFamilyAddInfoTask();
                        confirmFamilyAddInfoTask.execute();

//						FamilyAddInfoTask familyAddInfoTask = new FamilyAddInfoTask();
//						familyAddInfoTask.execute();
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        operation = "disagree";
                        showProgressBar();
                        ConfirmFamilyAddInfoTask confirmFamilyAddInfoTask = new ConfirmFamilyAddInfoTask();
                        confirmFamilyAddInfoTask.execute();

//						FamilyAddInfoTask familyAddInfoTask = new FamilyAddInfoTask();
//						familyAddInfoTask.execute();
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }

    /**
     * 9.2.4确认添加消息
     * 提交内容post
     * 接口名： confirmfamilyaddinfo
     *
     * @author kuangtiecheng
     */
    private class ConfirmFamilyAddInfoTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(ActivityAddNews.this);// ////////
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userId", userId + "");
            param.put("relationId", newsList.get(selectMember).getRelationId() + "");
            param.put("type", operation);
            try {
                String url = AbsParam.getBaseUrl() + family + "process";
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                JsonResult(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            if (beanFamilyResult != null) {
                if (beanFamilyResult.getStatusCode() == -1) {
                    Toast.makeText(ActivityAddNews.this, "确认成功", 1000).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    }, 1000);

                } else {
                    Toast.makeText(ActivityAddNews.this, BeanResultUtils.getPropertyFromResult(beanFamilyResult, "msg"), 1000).show();
                }

            }
        }
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonResult(String jsonString) throws Exception {
        beanFamilyResult = BeanResultUtils.parseResult(jsonString);
    }

    /**
     * 9.2.3查询我的添加消息提交内容post
     * 接口名： queryfamilyaddinfo
     *
     * @author kuangtiecheng
     */
    private class FamilyAddInfoTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(ActivityAddNews.this);// ////////
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userId", userId + "");
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl() + family + "list";//"/yyzx/app/" + queryNurse;
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                newsList.clear();
            }

            for (BeanFamilyMemberInfo tmp : tempnewsList) {
                if (tmp.getState() == 1) {
                    newsList.add(tmp);
                }
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            newsListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
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
        BeanFamilyResult bean = BeanResultUtils.parseResult(jsonString);
        if (bean != null) {
            if (bean.getStatusCode() == -1) {
                tempnewsList = gson.fromJson(bean.getData().toString(), new TypeToken<List<BeanFamilyMemberInfo>>() {
                }.getType());
            }
        }

        if (tempnewsList.size() < 10) {
            canLoadMore = false;
        } else {
            canLoadMore = true;
        }
    }

    private class NewsListAdapter extends BaseAdapter {
        private Context context;

        public NewsListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public BeanFamilyMemberInfo getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.news_list_item, null);
                holder.newsItemTV = (TextView) convertView
                        .findViewById(R.id.news_item_tv);
                holder.timeTV = (TextView) convertView
                        .findViewById(R.id.time_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.newsItemTV.setText(newsList.get(position).getOwnerName() + " 请求添加您为家庭成员");
            holder.timeTV.setText("电话:" + newsList.get(position).getPhone());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView newsItemTV;
        TextView timeTV;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        FamilyAddInfoTask familyAddInfoTask = new FamilyAddInfoTask();
        familyAddInfoTask.execute();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        FamilyAddInfoTask familyAddInfoTask = new FamilyAddInfoTask();
        familyAddInfoTask.execute();
    }

    FamilyAddInfoTask familyAddInfoTask;

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        if (canLoadMore) {
            if (familyAddInfoTask != null && familyAddInfoTask.getStatus() == AsyncTask.Status.RUNNING) {
                familyAddInfoTask.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            familyAddInfoTask = new FamilyAddInfoTask();
            familyAddInfoTask.execute();
        }
    }


}
