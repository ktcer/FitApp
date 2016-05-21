package com.cn.fit.ui.patient.others.myhealthrecord;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthrecord.BaseInfoBeen;
import com.cn.fit.model.healthrecord.BeanHealthRecordOneBeen;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.patient.mycare.HealthRecord;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.refreshlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人健康档案
 *
 * @author kuangtiecheng
 */
public class PersonnalData extends ActivityBasicListView {
    private static String queryNurse = "baseinformation";
    private XListView listView1;
    private Button button, button1, button2;
    private SimpleAdapter adapter;
    private TextView name, sex, birthday, province, nation, people, family,
            occupytion, phone, height, weight;
    // private LinearLayout layout;
    private ViewPager mPager;// 页卡内容
    private List<View> listViews; // Tab页面列表
    private String[] name3, time;// ,name4,name5,name6;
    /**
     * 2 基本信息，0 健康档案，1,干预方案
     */
    private int flag = 0;
    private List<BeanHealthRecordOneBeen> healthList, tempHealthList;
    private DoctorListAdapter doctorListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnal_data);
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("健康档案");
        baseInfoBeen = new BaseInfoBeen();
        initTopButtons();
        initViewPager();
        basicInformation();
        Health();
        intervene();
    }

    /**
     * 初始化头标
     */
    private void initTopButtons() {
        button = (Button) this.findViewById(R.id.basicInfo_personlaData);
        button1 = (Button) this.findViewById(R.id.HealthData_personalData);
        button2 = (Button) this.findViewById(R.id.intervene_scheme);
        button.setOnClickListener(new MyOnClickListener(0));
        button1.setOnClickListener(new MyOnClickListener(1));
        button2.setOnClickListener(new MyOnClickListener(2));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View view1 = mInflater.inflate(R.layout.slide_personnal_data, null);
        View view2 = mInflater.inflate(R.layout.slide_listview, null);
        View view3 = mInflater.inflate(R.layout.intervene_scheme, null);

        name = (TextView) view1.findViewById(R.id.name_personalData);
        sex = (TextView) view1.findViewById(R.id.sex_PersonalData);
        birthday = (TextView) view1.findViewById(R.id.brithday_PersonalData);
        province = (TextView) view1.findViewById(R.id.province_PersonalData);
        nation = (TextView) view1.findViewById(R.id.nation_PersonalData);
        people = (TextView) view1.findViewById(R.id.people_PersonalData);
        family = (TextView) view1.findViewById(R.id.family_PersonalData);
        occupytion = (TextView) view1
                .findViewById(R.id.occupytion_PersonalData);
        phone = (TextView) view1.findViewById(R.id.phone_num);
        height = (TextView) view1.findViewById(R.id.height_tv);
        weight = (TextView) view1.findViewById(R.id.weight_tv);

        doctorListAdapter = new DoctorListAdapter(this);
        healthList = new ArrayList<BeanHealthRecordOneBeen>();
        tempHealthList = new ArrayList<BeanHealthRecordOneBeen>();
        listView = (XListView) view2.findViewById(R.id.lv_basicData);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.setClass(PersonnalData.this, HealthRecord.class);
                intent.putExtra("healthStateItemName", healthList.get(arg2 - 1)
                        .getHealthStateItemName());
                intent.putExtra("healthStateID", healthList.get(arg2 - 1)
                        .getHealthStateID());
                startActivity(intent);
            }
        });
        listView1 = (XListView) view3.findViewById(R.id.lv_myintervene_history);
        listView1.setPullLoadEnable(true);
        listView1.setXListViewListener(this);
        listView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

//				Intent intent = new Intent();
//				intent.setClass(PersonnalData.this, HealthDiaryActivity.class);
////				intent.putExtra("healthStateItemName", healthList.get(arg2 - 1)
////						.getHealthStateItemName());
////				intent.putExtra("healthStateID", healthList.get(arg2 - 1)
////						.getHealthStateID());
//				startActivity(intent);


            }
        });
        listViews.add(view1);
        listViews.add(view2);
        listViews.add(view3);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            switch (index) {
                case 0:
                    button.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button.setTextColor(getResources()
                            .getColor(R.color.blue_second));
                    button1.setTextColor(getResources().getColor(R.color.black));
                    button2.setTextColor(getResources().getColor(R.color.black));
                    flag = 2;
                    // basicInformation();
                    break;
                case 1:
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button1.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    button.setTextColor(getResources().getColor(R.color.black));
                    button2.setTextColor(getResources().getColor(R.color.black));
                    flag = 0;
                    QueryHealthTask healthTask = new QueryHealthTask();
                    healthTask.execute();
                    // Health();
                    break;
                case 2:
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button2.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    button.setTextColor(getResources().getColor(R.color.black));
                    button1.setTextColor(getResources().getColor(R.color.black));
                    flag = 1;
                    break;

            }
        }
    }

    ;

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    button.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button.setTextColor(getResources()
                            .getColor(R.color.blue_second));
                    button1.setTextColor(getResources().getColor(R.color.black));
                    button2.setTextColor(getResources().getColor(R.color.black));
                    flag = 2;
                    // basicInformation();
                    break;
                case 1:
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button1.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    button.setTextColor(getResources().getColor(R.color.black));
                    button2.setTextColor(getResources().getColor(R.color.black));
                    flag = 0;
                    QueryHealthTask healthTask = new QueryHealthTask();
                    healthTask.execute();
                    // Health();
                    break;
                case 2:
                    button2.setBackgroundColor(getResources().getColor(
                            R.color.trans_30_blue));
                    button.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                    button1.setBackgroundColor(getResources().getColor(
                            R.color.white));
                    button2.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    button.setTextColor(getResources().getColor(R.color.black));
                    button1.setTextColor(getResources().getColor(R.color.black));
                    flag = 1;
                    break;

            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private class DoctorListAdapter extends BaseAdapter {
        private Context context;

        public DoctorListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return healthList.size();
        }

        @Override
        public Object getItem(int position) {
            return healthList.get(position);
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
                        R.layout.list_item, null);
                holder.healthTitle = (TextView) convertView
                        .findViewById(R.id.list_item_title);
                holder.healthImg = (ImageView) convertView
                        .findViewById(R.id.list_item_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//			BitmapToCircle bmtc = new BitmapToCircle();
//			Bitmap bitmap = null;
//			if (healthList.get(position).getHealthStateItemName().equals("疾病史")) {
//				bitmap = BitmapFactory.decodeResource(getResources(),
//						R.drawable.setting_myaccount);
//			} else if (healthList.get(position).getHealthStateItemName()
//					.equals("家族史")) {
//				bitmap = BitmapFactory.decodeResource(getResources(),
//						R.drawable.setting_helpcenter);
//			}
//			if (bitmap != null) {
//				Bitmap bitmap2 = bmtc.toRoundBitmap(bitmap);
//				holder.healthImg.setImageBitmap(bitmap2);
//			}

            holder.healthTitle.setText(healthList.get(position)
                    .getHealthStateItemName());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView healthTitle;
        ImageView healthImg;
    }

    public void setInfo(String str1, String str2, String str3, String str4,
                        String str5, String str6, String str7, String str8, String str9,
                        String str10, String str11) {
        name.setText("姓名:" + str1);
        sex.setText("性别:" + str2);
        birthday.setText("出生日期:" + str3);
        province.setText("籍贯:" + str4);
        nation.setText("国籍:" + str5);
        people.setText("民族:" + str6);
        family.setText("婚姻状况:" + str7);
        occupytion.setText("职业:" + str8);
        phone.setText("电话:" + str9);
        height.setText("身高" + str10);
        weight.setText("体重" + str11);
    }

    private BaseInfoBeen baseInfoBeen;

    private void basicInformation() {

        setInfo("李明", "男", "1998-02-04", "北京", "中国", "汉族", "已婚", "教师", "110",
                "175cm", "60kg");
        setInfo(baseInfoBeen.getPatientName(), baseInfoBeen.getPatientGender(),
                baseInfoBeen.getPatientBornData(),
                baseInfoBeen.getPatientPosition(),
                baseInfoBeen.getPatientNationality(),
                baseInfoBeen.getPatientNation(),
                baseInfoBeen.getPatientMaritalState(),
                baseInfoBeen.getPatientProfession(),
                baseInfoBeen.getPatientTelephone(),
                baseInfoBeen.getPatientHeight(),
                baseInfoBeen.getPatientWeight());
    }

    private void Health() {
        name3 = new String[]{"疾病史", "家族史", "预防接种史", "过敏史", "药物过敏/禁忌",
                "危险环境接触史"};
        int[] pic = new int[]{R.drawable.setting_myaccount,
                R.drawable.setting_helpcenter, R.drawable.setting_aboutus,
                R.drawable.testcenter, R.drawable.setting_familyremind,
                R.drawable.setting_infocenter};
        List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name3.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title2", name3[i]);
            item.put("pic", pic[i]);
            Items.add(item);
            adapter = new SimpleAdapter(this, Items, R.layout.list_item,
                    new String[]{"title2", "pic"}, new int[]{
                    R.id.list_item_title, R.id.list_item_image});

            listView.setAdapter(adapter);
        }
    }

    private void Health1() {
        if (flag == 0) {
            if (pageNum == 1) {
                healthList.clear();
            }

            for (BeanHealthRecordOneBeen tmp : tempHealthList) {
                healthList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            doctorListAdapter.notifyDataSetChanged();
        }

    }

    private void intervene() {
        time = new String[]{"2013-5-15", "2014-5-15", "2015-5-15",
                "2020-2-12", "2014-2-15", "2090-15-32"};
        String[] title = new String[]{"膝关节置换方案", "膝关节置换方案", "膝关节置换方案",
                "膝关节置换方案", "膝关节置换方案", "膝关节置换方案",};
        List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < time.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("time", time[i]);
            item.put("title", title[i]);
            Items.add(item);
            adapter = new SimpleAdapter(this, Items,
                    R.layout.listitem_intervene_histroy, new String[]{"time",
                    "title"}, new int[]{
                    R.id.list_item_intervene_time,
                    R.id.list_item_intervene_title});
            listView1.setAdapter(adapter);
        }
    }

    private class QueryBaseInfoTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            try {
                String a = AbsParam.getBaseUrl() + "/app/healthrecord/"
                        + queryNurse;
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/app/healthrecord/" + queryNurse, param, "utf-8");
                Log.i("result", result);
                healthJson(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            basicInformation();
            hideProgressBar();
            onLoad();
        }
    }

    private class QueryHealthTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            param.put("pageSize", "10");
            param.put("pageNu", pageNum + "");
            try {
                String a = AbsParam.getBaseUrl() + "/app/healthrecord/"
                        + "healthstate";
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/app/healthrecord/" + "healthstate", param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
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
        // private void JsonArrayToList(String jsonString) throws Exception {
        // Gson gson = new Gson();
        // tempHealthList.clear();
        // tempHealthList = gson.fromJson(jsonString,
        // new TypeToken<List<BeanHealthRecordOneBeen>>() {
        // }.getType());
        // }
        private void JsonArrayToList(String result) {
            JSONArray jsonArray;
            try {
                tempHealthList.clear();
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BeanHealthRecordOneBeen healthRecordOneBeen = new BeanHealthRecordOneBeen();
                    healthRecordOneBeen.setHealthStateID(jsonObject
                            .getString("healthStateID"));
                    healthRecordOneBeen.setHealthStateImgUrl(jsonObject
                            .getString("healthStateImgUrl"));
                    healthRecordOneBeen.setHealthStateItemName(jsonObject
                            .getString("healthStateItemName"));
                    tempHealthList.add(healthRecordOneBeen);
                }
                if (tempHealthList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            listView.setAdapter(doctorListAdapter);
            Health1();
            hideProgressBar();
            onLoad();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        showProgressBar();
        QueryBaseInfoTask baseInfoTask = new QueryBaseInfoTask();
        baseInfoTask.execute();
    }

    private void healthJson(String result) {
        // JSONArray jsonArray;
        try {
            // jsonArray = new JSONArray(result);
            // for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(result);

            baseInfoBeen.setPatientBornData(jsonObject
                    .getString("patientBornData"));
            baseInfoBeen
                    .setPatientGender(jsonObject.getString("patientGender"));
            baseInfoBeen
                    .setPatientHeight(jsonObject.getString("patientHeight"));
            baseInfoBeen.setPatientMaritalState(jsonObject
                    .getString("patientMaritalState"));
            baseInfoBeen.setPatientName(jsonObject.getString("patientName"));
            baseInfoBeen
                    .setPatientNation(jsonObject.getString("patientNation"));
            baseInfoBeen.setPatientNationality(jsonObject
                    .getString("patientNationality"));
            baseInfoBeen.setPatientPosition(jsonObject
                    .getString("patientPosition"));
            baseInfoBeen.setPatientTelephone(jsonObject
                    .getString("patientTelephone"));
            baseInfoBeen
                    .setPatientWeight(jsonObject.getString("patientWeight"));
            baseInfoBeen.setPatientProfession(jsonObject
                    .getString("patientProfession"));
            // }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        switch (flag) {
            case 0:
                onLoad();
                break;
            case 1:
                onLoadintervene();
                break;

            default:
                break;
        }

    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
        switch (flag) {
            case 0:
                onLoad();
                break;
            case 1:
                onLoadintervene();
                break;

            default:
                break;
        }
    }

    protected void onLoadintervene() {
        listView1.stopRefresh();
        listView1.stopLoadMore();
        listView1.setRefreshTime("刚刚");
    }
}