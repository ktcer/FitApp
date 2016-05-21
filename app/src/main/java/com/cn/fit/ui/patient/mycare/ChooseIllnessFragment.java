package com.cn.fit.ui.patient.mycare;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanDepartmentInfo;
import com.cn.fit.model.nurse.BeanDiseaseInfo;
import com.cn.fit.model.nurse.BeanSubDepartmentInfo;
import com.cn.fit.ui.patient.main.mynurse.ActivityAddInfo;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用来实现三级科室侧滑的fragment
 *
 * @author kuangtiecheng
 */
public class ChooseIllnessFragment extends Fragment {
    public static String queryAllDepartment = "queryalldepartmentlist";
    public static String querySubDepartment = "queryallsubdepartmentlist";
    public static String queryAllDisease = "queryalldeseaselist";

    private View mView;
    private ListView firstLV;
    private ListView secondLV;
    private ListView thirdLV;

    private List<BeanDepartmentInfo> allList;
    private List<BeanSubDepartmentInfo> subList;
    private List<BeanDiseaseInfo> diseaseList;

    private TranslateAnimation showAnimation = null; // 滑动动画
    private TranslateAnimation hideAnimation = null;

    private FirstListAdapter firstAdapter;
    private SecondAdapter secondAdapter;
    private ThirdAdapter thirdAdapter;

    private int current1st = 0; // 当前选中第几行
    private int current2nd = 0;

    private Boolean isSelect1st = false; // 当前科室是否打开
    private Boolean isSelect2nd = false;

    private CustomProgressDialog progressDialpg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choose_illness, container,
                false);
        initView();
        return mView;
    }

    private void initView() {
        // 初始化进度条
        progressDialpg = new CustomProgressDialog(getActivity(), "正在加载中"
        );
        // 初始化动画
        showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        showAnimation.setDuration(500);
        hideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        hideAnimation.setDuration(500);

        allList = new ArrayList<BeanDepartmentInfo>();
        firstLV = (ListView) mView.findViewById(R.id.illness_first_lv);
        fillFirstList();
        firstLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (isSelect1st && current1st == arg2) {
                    firstAdapter.notifyDataSetInvalidated();
                    secondAdapter.notifyDataSetInvalidated();
                    secondLV.startAnimation(hideAnimation);
                    secondLV.setVisibility(View.GONE);
                    thirdLV.setVisibility(View.GONE);
                    isSelect1st = false;
                    isSelect2nd = false;
                    current1st = arg2;
                } else {
                    fillSecondData(arg2);
                    firstAdapter.notifyDataSetInvalidated();
                    secondAdapter.notifyDataSetInvalidated();
                    secondLV.startAnimation(showAnimation);
                    secondLV.setVisibility(View.VISIBLE);
                    thirdLV.setVisibility(View.GONE);
                    current1st = arg2;
                    isSelect1st = true;
                    isSelect2nd = false;
                }

            }
        });
        secondLV = (ListView) mView.findViewById(R.id.illness_second_lv);
        subList = new ArrayList<BeanSubDepartmentInfo>();
        secondAdapter = new SecondAdapter(getActivity(), subList);
        secondLV.setAdapter(secondAdapter);
        secondLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (isSelect2nd && current2nd == arg2) {
                    secondAdapter.notifyDataSetInvalidated();
                    thirdLV.startAnimation(hideAnimation);
                    thirdLV.setVisibility(View.GONE);
                    isSelect2nd = false;
                    current2nd = arg2;
                } else {
                    fillThirdData(arg2);
                    secondAdapter.notifyDataSetInvalidated();
                    thirdLV.startAnimation(showAnimation);
                    thirdLV.setVisibility(View.VISIBLE);
                    isSelect2nd = true;
                    current2nd = arg2;
                }
            }
        });
        thirdLV = (ListView) mView.findViewById(R.id.illness_third_lv);
        diseaseList = new ArrayList<BeanDiseaseInfo>();
        thirdAdapter = new ThirdAdapter(getActivity(), diseaseList);
        thirdLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("diseaseID", diseaseList.get(arg2)
                        .getDiseaseID());
                intent.putExtra("diseaseName", diseaseList.get(arg2)
                        .getDiseaseName());
                intent.putExtra("subDepartmentID", subList.get(current2nd)
                        .getSubDepartmentID());
                intent.setClass(getActivity(), ActivityAddInfo.class);
                startActivity(intent);
            }
        });
    }

    private void fillFirstList() {
        QueryAllTask allTask = new QueryAllTask();
        allTask.execute();
        progressDialpg.show();
    }

    /**
     * 根据点击第几行填充不同的内容
     *
     * @param position
     */
    private void fillSecondData(int position) {
        subList.clear();
        QuerySubTask subTask = new QuerySubTask();
        subTask.execute(allList.get(position).getDepartmentID() + "");
        progressDialpg.show();
    }

    private void fillThirdData(int position) {
        diseaseList.clear();
        QueryDiseaseTask diseaseTask = new QueryDiseaseTask();
        diseaseTask.execute(subList.get(position).getSubDepartmentID() + "");
        progressDialpg.show();
    }

    private class SecondAdapter extends BaseAdapter {
        List<BeanSubDepartmentInfo> list;
        Context context;

        public SecondAdapter(Context context, List<BeanSubDepartmentInfo> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder3 = null;
            if (convertView == null) {
                holder3 = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.simple_item, null);
                holder3.name = (TextView) convertView
                        .findViewById(R.id.simple_item_tv);
                holder3.layout = (LinearLayout) convertView
                        .findViewById(R.id.simple_item_layout);
                convertView.setTag(holder3);
            } else {
                holder3 = (ViewHolder) convertView.getTag();
            }
            holder3.name.setText(list.get(position).getSubDepartmentName());
            holder3.layout.setBackgroundColor(getResources().getColor(
                    R.color.gray_second));
            holder3.name.setTextColor(getResources().getColor(R.color.black));
            if (isSelect2nd && position == current2nd) {
                holder3.name.setTextColor(getResources().getColor(
                        R.color.blue_second));
            }

            return convertView;
        }
    }

    private class ThirdAdapter extends BaseAdapter {
        List<BeanDiseaseInfo> list;
        Context context;

        public ThirdAdapter(Context context, List<BeanDiseaseInfo> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                        R.layout.simple_item, null);
                holder.name = (TextView) convertView
                        .findViewById(R.id.simple_item_tv);
                holder.layout = (LinearLayout) convertView
                        .findViewById(R.id.simple_item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(list.get(position).getDiseaseName());
            holder.layout.setBackgroundColor(getResources().getColor(
                    R.color.gray_third));
            return convertView;
        }
    }

    private class FirstListAdapter extends BaseAdapter {
        List<BeanDepartmentInfo> list;
        Context context;

        public FirstListAdapter(Context context, List<BeanDepartmentInfo> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderIV holder2 = null;
            if (convertView == null) {
                holder2 = new ViewHolderIV();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.simple_item_with_image_layout, null);
                holder2.departmentLayout = (LinearLayout) convertView
                        .findViewById(R.id.simple_item_with_iv_layout);
                holder2.department = (TextView) convertView
                        .findViewById(R.id.simple_item_with_iv_tv);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolderIV) convertView.getTag();
            }
            holder2.department.setText(list.get(position).getDepartmentName());
            holder2.departmentLayout.setBackgroundColor(getResources()
                    .getColor(R.color.white));
            if (position == current1st && isSelect1st) {
                holder2.departmentLayout.setBackgroundColor(getResources()
                        .getColor(R.color.gray_second));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView name;
        LinearLayout layout;
    }

    private class ViewHolderIV {
        LinearLayout departmentLayout;
        TextView department;
    }

    private class QueryAllTask extends AsyncTask<String, Integer, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/yyzx/app/" + queryAllDepartment, null, "utf-8");
                Log.i("url", AbsParam.getBaseUrl() + queryAllDepartment);
                Log.i("result", result);
                jsonToAllList(result);
            } catch (Exception e) {
                progressDialpg.dismiss();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialpg.dismiss();
            firstAdapter = new FirstListAdapter(getActivity(), allList);
            firstLV.setAdapter(firstAdapter);
        }

    }

    private void jsonToAllList(String result) {
        JSONArray jsArray;
        try {
            jsArray = new JSONArray(result);
            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject jsObject = jsArray.getJSONObject(i);
                BeanDepartmentInfo info = new BeanDepartmentInfo();
                info.setDepartmentID(jsObject.getInt("departmentID"));
                info.setDepartmentName(jsObject.getString("departmentName"));
                allList.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class QuerySubTask extends AsyncTask<String, Integer, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("departmentID", params[0]);
            Log.i("departmentID", params[0]);
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/yyzx/app/" + querySubDepartment, map, "utf-8");
                Log.i("result", result);
                jsonToSubList(result);
            } catch (Exception e) {
                progressDialpg.dismiss();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialpg.dismiss();
            secondLV.setAdapter(secondAdapter);
        }
    }

    private void jsonToSubList(String result) {
        JSONArray jsArray;
        try {
            jsArray = new JSONArray(result);
            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject jsObject = jsArray.getJSONObject(i);
                BeanSubDepartmentInfo subInfo = new BeanSubDepartmentInfo();
                subInfo.setSubDepartmentID(jsObject.getInt("subdepartmentID"));
                subInfo.setSubDepartmentName(jsObject
                        .getString("subdepartmentName"));
                subList.add(subInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class QueryDiseaseTask extends AsyncTask<String, Integer, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("subDepartmentID", params[0]);
            Log.i("subDepartmentID", params[0]);
            try {
                BeanDiseaseInfo allDisease = new BeanDiseaseInfo();
                allDisease.setDiseaseID(0);
                allDisease.setDiseaseName("全部病种");
                diseaseList.add(allDisease);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/yyzx/app/" + queryAllDisease, map, "utf-8");
                Log.i("result", result);
                jsonToDiseaseList(result);
            } catch (Exception e) {
                progressDialpg.dismiss();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialpg.dismiss();
            thirdLV.setAdapter(thirdAdapter);
        }
    }

    private void jsonToDiseaseList(String result) {
        JSONArray jsArray;
        try {
            jsArray = new JSONArray(result);

            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject jsObject = jsArray.getJSONObject(i);
                BeanDiseaseInfo diseaseInfo = new BeanDiseaseInfo();
                diseaseInfo.setDiseaseID(jsObject.getInt("diseaseID"));
                diseaseInfo.setDiseaseName(jsObject.getString("diseaseName"));
                diseaseList.add(diseaseInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
