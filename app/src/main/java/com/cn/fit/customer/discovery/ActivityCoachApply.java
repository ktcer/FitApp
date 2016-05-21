package com.cn.fit.customer.discovery;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.customer.baidulocation.BaiduLacationUtil;
import com.cn.fit.customer.baidumapgeocoder.GeoCoderDemo;
import com.cn.fit.customer.coach.ActivityCoachPage;
import com.cn.fit.customer.my.SharaSocia;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanDiscovery;
import com.cn.fit.model.customer.BeanDiscoveryDetail;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.AppPool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthpost.healthpost.ActivityCocahSubmitOrder;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/2.
 */
public class ActivityCoachApply extends ActivityBasic {
    private TextView titleImae;
    private TextView functionText;
    private TextView styleText;
    private TextView money;
    private ImageView collectimage, bigTitleImage;
    private TextView applyNum;
    private TextView locationText, submit;
    private TextView locationDetail, programName;
    private TextView applayTime, distance, text_goal1, text_goal3, text_goal2, detailText, coachName, goodAt, studentsNumbers;
    private CircleImageView coachImage;
    private boolean buyFlag = false;

    private BeanDiscovery beanDiscovery;
    private double latitude;
    private double longitude;
    private BaiduLacationUtil location;
    private LinearLayout mapll, coachll, buyll;
    private static BeanDiscoveryDetail beanDiscoveryDetail1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_apply);
        AppPool.createActivity(this);
        setTitle("   ");
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        beanDiscovery = (BeanDiscovery) getIntent().getSerializableExtra(Constant.BEAN_DISCOVERY);
        latitude = getIntent().getDoubleExtra(Constant.LATITUDE, 39.959833);
        longitude = getIntent().getDoubleExtra(Constant.LONGITUDE, 116.355626);
        buyFlag = getIntent().getBooleanExtra("buyflag", false);
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("活动详情");
        TextView rigthText = (TextView) findViewById(R.id.right_tv);
        rigthText.setVisibility(View.VISIBLE);
        rigthText.setText("分享");
        rigthText.setOnClickListener(this);
        titleImae = (TextView) findViewById(R.id.coach_text_apply);
        functionText = (TextView) findViewById(R.id.function_textView3_apply);
        styleText = (TextView) findViewById(R.id.text_title);
        money = (TextView) findViewById(R.id.text_money);
        collectimage = (ImageView) findViewById(R.id.collectimage);
        collectimage.setOnClickListener(this);
        bigTitleImage = (ImageView) findViewById(R.id.half_artist_image);
        applyNum = (TextView) findViewById(R.id.num_text);
        locationText = (TextView) findViewById(R.id.location_textView7);
        locationText.setVisibility(View.INVISIBLE);
        locationDetail = (TextView) findViewById(R.id.location_detail);
        applayTime = (TextView) findViewById(R.id.time_apply_text);
        distance = (TextView) findViewById(R.id.distance_textView6);
        text_goal1 = (TextView) findViewById(R.id.text_goal1);
        text_goal2 = (TextView) findViewById(R.id.text_goal2);
        text_goal3 = (TextView) findViewById(R.id.text_goal3);
        detailText = (TextView) findViewById(R.id.detail_text);
        coachImage = (CircleImageView) findViewById(R.id.coach_image);
        coachName = (TextView) findViewById(R.id.coach_name);
        goodAt = (TextView) findViewById(R.id.coach_good_at);
        studentsNumbers = (TextView) findViewById(R.id.student_number);
        programName = (TextView) findViewById(R.id._text);
        submit = (TextView) findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        mapll = (LinearLayout) findViewById(R.id.ll_third);
        mapll.setOnClickListener(this);
        coachll = (LinearLayout) findViewById(R.id.ll_seven);
        coachll.setOnClickListener(this);
        buyll = (LinearLayout) findViewById(R.id.ll_eight);

//页面传过来的数据显示
        titleImae.setText(beanDiscovery.getTitle());
        goodAt.setText(beanDiscovery.getTitle());
        functionText.setText(beanDiscovery.getTag());
        styleText.setText(beanDiscovery.getServeType());
        money.setText("¥" + beanDiscovery.getFinalPrice() + "");
        applyNum.setText(beanDiscovery.getNums() + "/" + beanDiscovery.getHaveNums());
        programName.setText(beanDiscovery.getProgramName());
        if (buyFlag) {
            buyll.setVisibility(View.INVISIBLE);
        } else {
            buyll.setVisibility(View.VISIBLE);

        }
        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl() + beanDiscovery.getCover(), bigTitleImage,
                AppMain.initImageOptions(R.drawable.ic_user_default,
                        false));


    }

    SharaSocia sharaSocia;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                sharaSocia = new SharaSocia();
                sharaSocia.init(this, AbsParam.getBaseUrl() + beanDiscoveryDetail1.getCover(), beanDiscovery.getTitle(), "详情" + beanDiscovery.getTag(), "http://101.200.90.103/ad/web/detail?classID=" + beanDiscoveryDetail1.getClassID());//http://101.200.90.103:8086/ad/web/detail?classID="+beanDiscoveryDetail1.getClassID()
                break;
            case R.id.collectimage:
                showProgressBar();
                AscyncFollowclass ascyfc = new AscyncFollowclass();
                ascyfc.execute();
                break;
            case R.id.ll_third:
                if (beanDiscoveryDetail1 != null) {
                    Intent intent = new Intent(this, GeoCoderDemo.class);
//                intent.putExtra("location",beanDiscoveryDetail1.getLocation());
                    intent.putExtra("longitude", beanDiscoveryDetail1.getLongitude());
                    intent.putExtra("latitude", beanDiscoveryDetail1.getLatitude());
                    startActivity(intent);
                }
                break;
            case R.id.submit_button:
                Intent intent = new Intent(this, ActivityCocahSubmitOrder.class);
                intent.putExtra("name", beanDiscoveryDetail1.getCoachName());
                intent.putExtra("classname", beanDiscovery.getTitle() + "," + beanDiscovery.getTag() + "," + beanDiscovery.getServeType());
                intent.putExtra("image", beanDiscoveryDetail1.getCover());
                intent.putExtra("address", beanDiscoveryDetail1.getLocation());
                intent.putExtra("time", beanDiscoveryDetail1.getStartTime());
                intent.putExtra("unitprice", beanDiscovery.getFinalPrice());
                intent.putExtra("classID", beanDiscoveryDetail1.getClassID());
                startActivity(intent);
                break;
            case R.id.ll_seven:
                Intent intents = new Intent(this, ActivityCoachPage.class);
                intents.putExtra("ExpertId", beanDiscoveryDetail1.getCoachID());
                intents.putExtra("image", beanDiscoveryDetail1.getCoachPic());
                intents.putExtra("classID", beanDiscoveryDetail1.getClassID());
                intents.putExtra("pageType", "1");
                intents.putExtra("latitude", beanDiscoveryDetail1.getLatitude());
                intents.putExtra("longitude", beanDiscoveryDetail1.getLongitude());
                startActivity(intents);

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sharaSocia.destroy();
//        location.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressBar();
        QureyDiscoveryDetail qureyDiscoveryDetail = new QureyDiscoveryDetail(this, beanDiscovery.getClassID(), latitude, longitude) {
            @Override
            protected void onPostExecute(BeanDiscoveryDetail beanDiscoveryDetail) {
                super.onPostExecute(beanDiscoveryDetail);
                hideProgressBar();
                showProgressBar();
                AscyncIfCollet ascyncIfCollet = new AscyncIfCollet();
                ascyncIfCollet.execute();
                beanDiscoveryDetail1 = beanDiscoveryDetail;
                if (beanDiscoveryDetail != null) {
                    ImageLoader.getInstance().displayImage(
                            AbsParam.getBaseUrl() + beanDiscoveryDetail.getCoachPic(), coachImage,
                            AppMain.initImageOptions(R.drawable.ic_user_default,
                                    false));
                    locationText.setText(beanDiscoveryDetail.getLocation());
                    locationDetail.setText(beanDiscoveryDetail.getLocation());
                    applayTime.setText(beanDiscoveryDetail.getStartTime());
                    String dis = "";
                    if (beanDiscoveryDetail.getDistance() > 1000) {
                        dis = beanDiscoveryDetail.getDistance() / 1000 + "km";
                    } else {
                        dis = beanDiscoveryDetail.getDistance() + "m";
                    }
                    distance.setText(dis);
                    String goalStr = beanDiscoveryDetail.getGoal();
                    if (goalStr == null || goalStr.length() == 0) {
                        text_goal1.setVisibility(View.INVISIBLE);
                        text_goal2.setVisibility(View.INVISIBLE);
                        text_goal2.setVisibility(View.INVISIBLE);
                    } else {
                        String[] goal = goalStr.split(",");
                        switch (goal.length) {
                            case 1:
                                text_goal1.setVisibility(View.VISIBLE);
                                text_goal2.setVisibility(View.INVISIBLE);
                                text_goal3.setVisibility(View.INVISIBLE);
                                text_goal1.setText(goal[0]);
                                break;
                            case 2:
                                text_goal1.setVisibility(View.VISIBLE);
                                text_goal2.setVisibility(View.VISIBLE);
                                text_goal3.setVisibility(View.INVISIBLE);
                                text_goal1.setText(goal[0]);
                                text_goal2.setText(goal[1]);
                                break;
                            case 3:
                            default:
                                text_goal1.setVisibility(View.VISIBLE);
                                text_goal2.setVisibility(View.VISIBLE);
                                text_goal2.setVisibility(View.VISIBLE);
                                text_goal1.setText(goal[0]);
                                text_goal2.setText(goal[1]);
                                text_goal3.setText(goal[2]);
                                break;
                        }

                    }
                    detailText.setText(Html.fromHtml("<font color='#000000'>课程简介:</font>") + "\n" + beanDiscoveryDetail.getDetail());
                    coachName.setText(beanDiscoveryDetail.getCoachName());
                    studentsNumbers.setText(beanDiscoveryDetail.getCoachMembers() + "");
//                    goodAt.setText();
                }

            }
        };
        qureyDiscoveryDetail.execute();

    }

    private class AscyncIfCollet extends AsyncTask<Integer, Integer, String> {

        private String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            String retString = "";
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            param.put("classID", beanDiscoveryDetail1.getClassID() + "");

            try {
                retString = NetTool.sendPostRequest(
                        AbsParam.getBaseUrl() + "/ad/app/iffollow",
                        param, "utf-8");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                hideProgressBar();
            }
            return retString;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            int operate = 0;
            if (result == null) {
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                operate = json.getInt("operate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (operate == 0) {
                collectimage.setBackground(getResources().getDrawable(R.drawable.ic_favorite_info_n));
                hideProgressBar();
            } else {
                collectimage.setBackground(getResources().getDrawable(R.drawable.ic_favorite_info_p));
                hideProgressBar();

            }

        }
    }

    private class AscyncFollowclass extends AsyncTask<Integer, Integer, String> {

        private String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            String retString = "";
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            param.put("classID", beanDiscoveryDetail1.getClassID() + "");

            try {
                retString = NetTool.sendPostRequest(
                        AbsParam.getBaseUrl() + "/ad/app/followclass",
                        param, "utf-8");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                hideProgressBar();
            }
            return retString;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            int operate = 0;
            if (result == null) {
                return;
            }
            try {
                JSONObject json = new JSONObject(result);
                operate = json.getInt("operate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (operate == 0) {
                ToastUtil.showMessage("取消关注成功");
                collectimage.setBackground(getResources().getDrawable(R.drawable.ic_favorite_info_n));
                hideProgressBar();
            } else {
                ToastUtil.showMessage("关注成功，可以在我的收藏里面查看");
                collectimage.setBackground(getResources().getDrawable(R.drawable.ic_favorite_info_p));
                hideProgressBar();

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            sharaSocia.onsharaActivityResult(requestCode, resultCode, data);
        }

    }
}
