package com.cn.fit.ui.patient.main.mynurse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanRequestAddInfo;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 保健秘书添加消息
 *
 * @author kuangtiecheng
 */
public class ActivityAddInfo extends ActivityBasicListView {
    private List<BeanRequestAddInfo> nurseList, tempNurseList;
    private DoctorListAdapter doctorListAdapter;
    private TextView Empty_TV;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private long userId;
    protected int pageNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);
        imageInit();
        initView();
    }

    private void imageInit() {
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);
        imageLoader = ImageLoader.getInstance();

    }

    private void initView() {
        UtilsSharedData.initDataShare(this);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);

        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("添加消息");

        Empty_TV = (TextView) findViewById(R.id.empty_tip_tv);
        doctorListAdapter = new DoctorListAdapter(this);
        nurseList = new ArrayList<BeanRequestAddInfo>();
        tempNurseList = new ArrayList<BeanRequestAddInfo>();
        listView = (XListView) findViewById(R.id.expert_list);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setTag("lv");
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//				Intent intent = new Intent();
//				intent.setClass(ActivityAddInfo.this, NursePage.class);
//				intent.putExtra("img", nurseList.get(arg2-1).getImgUrl());//
//				intent.putExtra("nurseID", nurseList.get(arg2-1).getNurseID());
//				intent.putExtra("resume", nurseList.get(arg2-1).getResume());
//				intent.putExtra("diseaseName", diseaseName);
//				intent.putExtra(Constant.PAGE_TYPE, 0);
//				startActivity(intent);
            }
        });
        listView.setAdapter(doctorListAdapter);

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getAssistAddInfo();
    }


    /*
     * 获取保健秘书添加消息
     *
     */
    private void getAssistAddInfo() {
        AscynRequestAddInfo ascynRequestAddInfo = new AscynRequestAddInfo();
        ascynRequestAddInfo.execute();
    }


    private class DoctorListAdapter extends BaseAdapter {
        private Context context;

        public DoctorListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return nurseList.size();
        }

        @Override
        public Object getItem(int position) {
            return nurseList.get(position);
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
                        R.layout.expert_addinfo_list_item, parent, false);
                holder.nurseNameTV = (TextView) convertView
                        .findViewById(R.id.addinfo_name);
                holder.nurseDescTV = (TextView) convertView
                        .findViewById(R.id.addinfo_desc);
                holder.acceptBtn = (FButton) convertView
                        .findViewById(R.id.addinfo_accept);
                holder.nurseAvatarIV = (ImageView) convertView
                        .findViewById(R.id.addinfo_avatar_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.acceptBtn.setCornerRadius(3);
            imageLoader.displayImage(AbsParam.getBaseUrl() + nurseList.get(position).getImgUrl(), holder.nurseAvatarIV, options);
            holder.nurseDescTV.setText("我是" + nurseList.get(position)
                    .getNurseName() + "，请求成为您的保健秘书");
            holder.nurseNameTV.setText(nurseList.get(position).getNurseName());
            holder.acceptBtn.setTag(position);
            holder.acceptBtn.setOnClickListener(new MyAcceptListener(position));
            return convertView;
        }
    }

    /*
     * 接受按钮的点击事件
     */
    private class MyAcceptListener implements OnClickListener {
        int mPosition;

        public MyAcceptListener(int inPosition) {
            mPosition = inPosition;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            PayConfirmAsyncTask confirmExpectAsyncTask = new PayConfirmAsyncTask();
            confirmExpectAsyncTask.execute(mPosition);
        }

    }

    /**
     * 支付确认，开启健康之旅
     *
     * @author kuangtiecheng
     */
    private class PayConfirmAsyncTask extends
            AsyncTask<Integer, Integer, String> {
        String result = "";

        private PayConfirmAsyncTask() {
            showProgressBar();
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", userId + "");
            param.put("expertID", nurseList.get(params[0]).getNurseID() + "");
            Log.i("input", AbsParam.getBaseUrl() + "/member/healthcare/pay"
                    + param.toString());
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/member/healthcare/pay", param, "utf-8");
                Log.i("result", result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            JSONObject json;
            try {
                json = new JSONObject(result);
                result = json.getString("result");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (result != null) {
                if (result.equals("success")) {
                    getAssistAddInfo();
                } else {
                    ToastUtil.showMessage("您的健康之旅貌似没有开启成功哦！");
                }
            }
        }

    }

    private class ViewHolder {
        ImageView nurseAvatarIV;
        TextView nurseNameTV;
        TextView nurseDescTV;
        FButton acceptBtn;
    }

    /*
     * 获取当前的添加消息（包含专家主动添加患者以及患者添加专家的确认消息）
     *
     */
    private class AscynRequestAddInfo extends AsyncTask<Integer, Integer, String> {
        String result = "";
        private String requestAddInfo = "/member/request/addlist";

        public AscynRequestAddInfo() {
            showProgressBar();
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userId", userId + "");
            param.put("userType", "0");
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");

            try {
                String url = AbsParam.getBaseUrl() + requestAddInfo;
                Log.i("input", url + param.toString());
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
                nurseList.clear();
            }
            for (BeanRequestAddInfo tmp : tempNurseList) {
                nurseList.add(tmp);
            }
            if (nurseList.size() == 0) {
                Empty_TV.setVisibility(View.VISIBLE);
            } else {
                Empty_TV.setVisibility(View.GONE);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            doctorListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }

    private void JsonArrayToList(String result) {
        Gson gson = new Gson();
        // 添加我自己的信息
        if (result != null) {
            if (!(result.equals(-1))) {
                tempNurseList.clear();
                tempNurseList = gson.fromJson(result,
                        new TypeToken<List<BeanRequestAddInfo>>() {
                        }.getType());
                if (tempNurseList.size() == 1) {
                    if (tempNurseList.get(0).getIfhave() == 0) {
                        tempNurseList.clear();
                    }
                }
                if (tempNurseList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            default:
                break;
        }

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        getAssistAddInfo();
    }

    private AscynRequestAddInfo ascynRequestAddInfo;

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        if (canLoadMore) {
            if (ascynRequestAddInfo != null && ascynRequestAddInfo.getStatus() == AsyncTask.Status.RUNNING) {
                ascynRequestAddInfo.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            ascynRequestAddInfo = new AscynRequestAddInfo();
            ascynRequestAddInfo.execute();
        }

    }

}
