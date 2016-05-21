package com.cn.fit.coach.comment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanGetStar;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.pagertable.PagerSlidingTabStrip;

/**
 * Created by ktcer on 2016/1/12.
 */
public class ActivityQueryCoachMomment extends ActivityBasic {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private long coachID;
    private long classID;
    private RatingBar rbCourseStar, rbCoachStar;
    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_comment);
        initial();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
    }

    /**
     * 基本组件初始化
     */
    private void initial() {
        dm = getResources().getDisplayMetrics();
        coachID = getIntent().getLongExtra("coachID", 0);
        classID = getIntent().getLongExtra("classID", 0);
        ((TextView) this.findViewById(R.id.middle_tv)).setText("评价详情");
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

//		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
//				.getDisplayMetrics());
//		pager.setPageMargin(pageMargin);
        tabs.setIndicatorColor(getResources().getColor(R.color.blue_second));
        tabs.setViewPager(pager);
        setTabsValue();

        rbCourseStar = (RatingBar) findViewById(R.id.rb_course_star);
        rbCourseStar.setNumStars(5);
        rbCourseStar.setRating(2);
        rbCourseStar.setIsIndicator(true);
        rbCoachStar = (RatingBar) findViewById(R.id.rb_coach_star);
        rbCoachStar.setNumStars(5);
        rbCoachStar.setIsIndicator(true);
        rbCoachStar.setRating(3);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
//		tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#33CCCC"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#33CCCC"));
        // 取消点击Tab时的背景色
        tabs.setTextColor(getResources().getColor(R.color.launcher_tab_text_selector));
        tabs.setTabBackground(0);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        AscynGetStar task = new AscynGetStar(this, coachID) {
            @Override
            protected void onPostExecute(BeanGetStar beanGetStar) {
                super.onPostExecute(beanGetStar);
                hideProgressBar();
                if (beanGetStar == null) {
                    return;
                } else {
                    rbCoachStar.setRating(beanGetStar.getStar());
                }
            }
        };
        task.execute();
        showProgressBar();
        AscynGetCourseStar taskc = new AscynGetCourseStar(this, classID) {
            @Override
            protected void onPostExecute(BeanGetStar beanGetStar) {
                super.onPostExecute(beanGetStar);
                hideProgressBar();
                if (beanGetStar == null) {
                    return;
                } else {
                    rbCourseStar.setRating(beanGetStar.getStar());
                }
            }
        };
        taskc.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
    }

    /*
     * pagerview adapter
     * @see com.cn.aihu.ui.basic.ActivityBasicListView#onRefresh()
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"好评", "中评", "差评"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentmommentList.newInstance(position, coachID);
        }

    }

}