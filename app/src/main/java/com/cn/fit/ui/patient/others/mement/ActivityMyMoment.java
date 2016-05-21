package com.cn.fit.ui.patient.others.mement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.main.mynurse.NursePage;
import com.cn.fit.ui.patient.main.mynurse.PopMenu;
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

/**
 * 我的朋友圈Activity
 *
 * @author kuangtiecheng
 */
public class ActivityMyMoment extends ActivityBasic {
    private ListView lv1, lv2;
    private TextView selectIleeness;
    private TextView selectArea;
    private TextView doctorList;// 醫生列表
    private TextView illnessFriends;// 病友列表
    private ImageView communication;// 聊天室》改
    private String queryResult = "关注：200 |文章：5";
    private String illName = "脑血栓";
    private String[] illNumbers = {"高血压", "肺结核", "骨质疏松", "脑血栓"};
    private String[] areaNumbers = {"所属驿站", "所属城市"};
    private PopMenu popLeft;
    private PopMenu popRight;
    private View doctorUnderline;
    private View illnessUnderline;
    private LinearLayout Login, notLogin;
    private String ActivityTag = "MyMoment";
    private Button toLogin_btn;
    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_listview);
        initView();
//		wehtherLogin(ActivityTag);
        initTopButtons();
        initViewPager();
        setListener();
        listViewWork();
        listViewIllness();
    }

    /**
     * 初始化头标
     */
    private void initTopButtons() {
        doctorList = (TextView) findViewById(R.id.doctor_list_button);
        illnessFriends = (TextView) findViewById(R.id.illness_friends_button);
        doctorUnderline = (View) findViewById(R.id.doctor_underline_v);
        illnessUnderline = (View) findViewById(R.id.illness_friends_underline_v);
        doctorList.setOnClickListener(new MyOnClickListener(0));
        illnessFriends.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View view1 = mInflater.inflate(R.layout.slide_listview, null);
        View view2 = mInflater.inflate(R.layout.slide_listview, null);

        lv1 = (ListView) view1.findViewById(R.id.lv_basicData);
        lv2 = (ListView) view2.findViewById(R.id.lv_basicData);

        listViews.add(view1);
        listViews.add(view2);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initView() {
        Login = (LinearLayout) findViewById(R.id.login_MyMoment);
        notLogin = (LinearLayout) findViewById(R.id.notlogin_MyMoment);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("朋友圈");
        //隐藏返回按键
        findViewById(R.id.left_tv).setVisibility(View.GONE);
        selectIleeness = (TextView) findViewById(R.id.select_illness);
        selectArea = (TextView) findViewById(R.id.select_area);

        communication = (ImageView) findViewById(R.id.communication);

        toLogin_btn = (Button) findViewById(R.id.toLogin_MyMoment);
        toLogin_btn.setOnClickListener(this);
    }

    private void setListener() {
        selectIleeness.setOnClickListener(this);
        selectArea.setOnClickListener(this);
        communication.setOnClickListener(this);
    }

    /**
     * 医生列表
     */
    private void listViewWork() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 7; i++) {
            map.put("name", queryResult); // 关注 文章
            listItem.add(map);
        }

        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
                R.layout.friend_search_view,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[]{"name"},
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.myStatusText});
        // 添加并且显示
        lv1.setAdapter(listItemAdapter);
        lv1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ActivityMyMoment.this, NursePage.class);
                intent.putExtra("nurseID", 2);
                intent.putExtra("diseaseName", "糖尿病");
                intent.putExtra(Constant.PAGE_TYPE, 0);
                intent.putExtra("resume", "非常好");
                startActivity(intent);
            }
        });

    }

    private void listViewIllness() {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 7; i++) {
            map.put("name", illName); // 关注 文章
            listItem.add(map);
        }

        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
                R.layout.illness_list_view,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[]{"name"},
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.ill_name});
        // 添加并且显示
        lv2.setAdapter(listItemAdapter);
        lv2.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // HashMap<String, String> map = (HashMap<String, String>)
                // lv.getItemAtPosition(position);
                // final String name = map.get("name");
//				Toast.makeText(MyMoment.this, "message", Toast.LENGTH_SHORT)
//						.show();
            }
        });

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
                    doctorList.setTextColor(getResources().getColor(R.color.blue_second));
                    doctorUnderline.setBackgroundColor(getResources().getColor(
                            R.color.blue_second));
                    illnessFriends.setTextColor(getResources().getColor(R.color.font_gray));
                    illnessUnderline.setBackgroundColor(getResources().getColor(
                            R.color.font_gray));
//				listViewWork();
                    break;
                case 1:
                    doctorList.setTextColor(getResources().getColor(R.color.font_gray));
                    doctorUnderline.setBackgroundColor(getResources().getColor(
                            R.color.font_gray));
                    illnessFriends.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    illnessUnderline.setBackgroundColor(getResources().getColor(
                            R.color.blue_second));
//				listViewIllness();
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
                    doctorList.setTextColor(getResources().getColor(R.color.blue_second));
                    doctorUnderline.setBackgroundColor(getResources().getColor(
                            R.color.blue_second));
                    illnessFriends.setTextColor(getResources().getColor(R.color.font_gray));
                    illnessUnderline.setBackgroundColor(getResources().getColor(
                            R.color.font_gray));
//				listViewWork();
                    break;
                case 1:
                    doctorList.setTextColor(getResources().getColor(R.color.font_gray));
                    doctorUnderline.setBackgroundColor(getResources().getColor(
                            R.color.font_gray));
                    illnessFriends.setTextColor(getResources().getColor(
                            R.color.blue_second));
                    illnessUnderline.setBackgroundColor(getResources().getColor(
                            R.color.blue_second));
//				listViewIllness();
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

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.select_illness:
                popLeft = new PopMenu(this);
                popLeft.addItems(illNumbers);
                popLeft.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        selectIleeness.setText(illNumbers[arg2] + "▼");
                        popLeft.dismiss();
                    }
                });
                popLeft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        popLeft.dismiss();
                    }
                });
                popLeft.showAsDropDown(v, 50, 0);
                break;
            case R.id.select_area:
                popRight = new PopMenu(this);
                popRight.addItems(areaNumbers);
                popRight.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        selectArea.setText(areaNumbers[arg2] + "▼");
                        popRight.dismiss();
                    }
                });
                popRight.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        popRight.dismiss();
                    }
                });
                popRight.showAsDropDown(v, 20, 0);
                break;

            case R.id.communication:
                Intent intent = new Intent();
                intent.putExtra(Constant.HEALTH_QA, "1");
                intent.setClass(ActivityMyMoment.this, ActivityPost.class);
                startActivity(intent);
                break;
            case R.id.toLogin_MyMoment:
                wehtherLogin(ActivityTag);
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		if(Constant.firstEnterLoginFromMoment){
        wehtherLogin(ActivityTag);
//			Constant.firstEnterLoginFromMoment=false;
//		}
    }

    private void wehtherLogin(String ActivityTag) {
        UtilsSharedData sharedData = new UtilsSharedData();
        if (sharedData.isEmpty(Constant.LOGIN_STATUS) || sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("0")) {
            Login.setVisibility(View.GONE);
            notLogin.setVisibility(View.VISIBLE);
            if (Constant.firstEnterLoginFromMoment) {
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                intent.putExtra("ActivityTag", ActivityTag);
                startActivity(intent);
                Constant.firstEnterLoginFromMoment = false;
            } else {
                Constant.firstEnterLoginFromMoment = true;
            }
        } else {
            Login.setVisibility(View.VISIBLE);
            notLogin.setVisibility(View.GONE);
//			Constant.firstEnterLoginFromMoment=true;
        }
    }
}
