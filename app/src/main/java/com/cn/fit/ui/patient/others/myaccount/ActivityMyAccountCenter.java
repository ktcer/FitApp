package com.cn.fit.ui.patient.others.myaccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.customer.my.ActivityMyCollect;
import com.cn.fit.customer.my.ActivityMyCoursrList;
import com.cn.fit.model.personinfo.BeanPersonInfo;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityTestList;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityTestReportList;
import com.cn.fit.ui.patient.others.message.ActivityMessageCenter;
import com.cn.fit.ui.patient.others.myorders.ActivityMyOrdersList;
import com.cn.fit.ui.patient.setting.ActivitySettings;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMyAccountCenter extends ActivityBasic implements
        OnClickListener {
    private ImageView iv;
    private TextView tv_name, tv_score, tv_money, tv_mymoney;
    private String userName, userAccount;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    private Bitmap head;// 头像Bitmap
    public static float myCoins;
    public static float money;
    private FButton btnRemark;
    private boolean isRemark;
    private ImageView testReadMark;
    private ImageView infoReadMark;
    private SettingItem mMyMsg;
    private SettingItem mMyFamily;
    private SettingItem mMyOrders;
    private SettingItem myCollect;
    private SettingItem mMsgRemind;
    private LinearLayout myMoney;
    private LinearLayout myscore;
    private SettingItem mStartTest;
    private SettingItem mTestResult;
    private SettingItem diaryRemind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccountcenter);
        // setdata();
        // initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    private void initial() {

        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        userAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
        tv_name = (TextView) findViewById(R.id.name_informationManage);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_money = (TextView) findViewById(R.id.name_informationManage);
        tv_mymoney = (TextView) findViewById(R.id.tv_mymoney);
        iv = (ImageView) findViewById(R.id.image_informationManage);
        testReadMark = (ImageView) findViewById(R.id.red_mark_healthTest);
        infoReadMark = (ImageView) findViewById(R.id.red_mark_BasicInfo);
        tv_name.setText(userName.equals("") ? userAccount : userName);
        imageLoader.displayImage(imageUrl, iv, options);
        btnRemark = (FButton) findViewById(R.id.btn_remark);
        btnRemark.setCornerRadius(3);
        btnRemark.setOnClickListener(this);

        mMyMsg = (SettingItem) findViewById(R.id.txt_mymsg);
        mMyMsg.setOnClickListener(this);
        mMyMsg.setNewUpdateVisibility(true);
        mMyFamily = (SettingItem) findViewById(R.id.txt_myfamily);
        mMyFamily.setOnClickListener(this);

        myscore = (LinearLayout) findViewById(R.id.layout_myscore);
        myscore.setOnClickListener(this);

        myMoney = (LinearLayout) findViewById(R.id.layout_mymoney);
        myMoney.setOnClickListener(this);

        mMyOrders = (SettingItem) findViewById(R.id.txt_myorders);
        mMyOrders.setOnClickListener(this);
        myCollect = (SettingItem) findViewById(R.id.my_collect);
        myCollect.setOnClickListener(this);
        mMsgRemind = (SettingItem) findViewById(R.id.txt_remind_settings);
        mMsgRemind.setOnClickListener(this);

        mStartTest = (SettingItem) findViewById(R.id.item_startTest);
        mStartTest.setOnClickListener(this);
        mTestResult = (SettingItem) findViewById(R.id.item_TestResult);
        mTestResult.setOnClickListener(this);

        diaryRemind = (SettingItem) findViewById(R.id.txt_my_cours);
        diaryRemind.setOnClickListener(this);

        iv.setOnClickListener(this);

        // 查询是否签到
//		AsyncQueryRemarkState asyncRemark = new AsyncQueryRemarkState(this,
//				btnRemark);
//		asyncRemark.execute();
    }

    /**
     * @author kuangtiecheng 此处可能有问题，AsyncGetMyCoins会重复加载
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        setdata();
        initial();
        // TODO Auto-generated method stub
        super.onResume();
        TabActivityMain.changePage(R.id.tab_iv_3);
        Log.i("test", "i have chang to tab3");
        imageUrl = AbsParam.getBaseUrl()
                + UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        imageLoader.displayImage(imageUrl, iv, options);
        showProgressBar();
        AsyGetUserInfo userTask = new AsyGetUserInfo(this) {

            @Override
            protected void onPostExecute(BeanPersonInfo result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
//				userName = result.getUserName();
                tv_name.setText(userName);
//				imageUrl = AbsParam.getBaseUrl() + result.getImgUrl();
                imageLoader.displayImage(imageUrl, iv, options);
            }
        };
        userTask.execute();
//		// 我的金币
//		AsyncGetMyCoins mTask = new AsyncGetMyCoins(this) {
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				tv_score.setText((long) myCoins + "个");
//			}
//		};
//		mTask.execute();
//		// 我的余额
//		AsyncGetMoney gmTask = new AsyncGetMoney(this) {
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				tv_mymoney.setText(money + "元");
//			}
//		};
//		gmTask.execute();
        hasUnfinished();
    }

    private void hasUnfinished() {

        if (UtilsSharedData.getLong("HabbitTestState", -1) * UtilsSharedData.getLong("BodyTestState", -1) == 0) {
            testReadMark.setVisibility(View.VISIBLE);
        } else {
            testReadMark.setVisibility(View.GONE);
        }
        if (UtilsSharedData.getLong("BaseInfoState", -1) == 0) {
            infoReadMark.setVisibility(View.VISIBLE);
        } else {
            infoReadMark.setVisibility(View.GONE);
        }
        if (UtilsSharedData.getLong("HabbitTestState", -1) * UtilsSharedData.getLong("BodyTestState", -1) * UtilsSharedData.getLong("BaseInfoState", -1) == 0) {
            TabActivityMain.mRedMark_tab3.setVisibility(View.VISIBLE);
        } else {
            TabActivityMain.mRedMark_tab3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_mymsg: /* 消息中心 */
                intent = new Intent();
                intent.setClass(this, ActivityMessageCenter.class);
                startActivity(intent);
                break;
            case R.id.txt_myfamily: /* 设置 */
                // intent = new Intent();
                // intent.setClass(this, ActivityMyFamily.class);
                // startActivity(intent);
                break;
            case R.id.txt_myorders: /* 设置 */
                intent = new Intent();// 我的订单
                intent.setClass(this, ActivityMyOrdersList.class);
                startActivity(intent);
                break;

            case R.id.image_informationManage:
                intent = new Intent();// 个人信息
                intent.setClass(this, ActivityPersonalInfo.class);
                startActivity(intent);
                break;
            case R.id.my_collect:
                intent = new Intent();// 我的收藏
                intent.setClass(this, ActivityMyCollect.class);
                startActivity(intent);
                break;
            case R.id.txt_remind_settings:
                intent = new Intent();// 提醒设置
                intent.setClass(this, ActivitySettings.class);
                startActivity(intent);
                break;
            case R.id.layout_mymoney:
                intent = new Intent();// 我的余额
                intent.setClass(this, ActivityMyBalance.class);
                startActivity(intent);
                break;
            case R.id.layout_myscore:
                intent = new Intent();// 我的金币
                intent.setClass(this, ActivityMyConis.class);
                startActivity(intent);
                break;
            case R.id.btn_remark:
                // //与后台交互...
                remarkData();
                break;
            case R.id.item_startTest:
                intent = new Intent(this, ActivityTestList.class);
                startActivity(intent);
                break;
            case R.id.item_TestResult:
                intent = new Intent(this, ActivityTestReportList.class);
                startActivity(intent);

                break;
            case R.id.txt_my_cours:
                // 从本地数据库读取提醒内容
                intent = new Intent(this, ActivityMyCoursrList.class);//ActivityDiaryRemindList
                startActivity(intent);

                break;

            default:
                break;
        }
    }

    public synchronized void remarkData() {
        AsyncRemark asyncRemark = new AsyncRemark(this) {
            @Override
            protected void onPostExecute(JSONObject result) {
                // TODO Auto-generated method stub
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "网络出问题了",
                            Toast.LENGTH_SHORT).show();
                    setBtnRemark(
                            false,
                            getApplicationContext().getString(
                                    R.string.remark_ready));
                } else {
                    try {
                        if (result.getInt("result") == 1) {
                            setBtnRemark(true, getApplicationContext()
                                    .getString(R.string.remark_finish));
                            int count = remarkUi(3, tv_score);
                            tv_score.setText(""
                                    + (Long.parseLong(tv_score.getText()
                                    .toString().replace("个", "")) + (long) count)
                                    + "个");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        };
        asyncRemark.execute();
    }

    /**
     * @param isRemark 是否已经签到
     * @author kuangtiecheng 签到，设置btnRemark状态
     */
    public void setBtnRemark(boolean isRemark, String state) {
        if (isRemark) {
            btnRemark.setEnabled(false);
            btnRemark.setBackgroundColor(this.getResources().getColor(
                    R.color.fbutton_color_concrete));
        }
        btnRemark.setText(state);
    }

    /**
     * @param count 一次签到送的金币数 textView 动画终点落在的控件
     * @author kuangtiecheng 签到效果，待修改
     */
    public int remarkUi(int count, TextView textView) {
        AnimationSet mAnimationSet;
        ImageView image = (ImageView) findViewById(R.id.image);
        image.setVisibility(View.VISIBLE);
        mAnimationSet = getAnimationSet(textView);
        image.startAnimation(mAnimationSet);

        TextView add_num = (TextView) findViewById(R.id.add_num);
        mAnimationSet = getAddAnimationSet();
        mAnimationSet.setStartOffset(1000);
        add_num.setVisibility(View.VISIBLE);
        add_num.setText("+" + count);
        add_num.startAnimation(mAnimationSet);
        image.setVisibility(View.INVISIBLE);
        add_num.setVisibility(View.INVISIBLE);

        return count;
    }

    /**
     * @param textView 动画终点落在的控件
     * @author kuangtiecheng 签到金币动画，待修改
     */
    public AnimationSet getAnimationSet(TextView textView) {
        AnimationSet mAnimationSet = new AnimationSet(false);

        int[] location = new int[2];
        textView.getLocationOnScreen(location);
        System.out.println("location[0]: " + location[0] + " location[1]： "
                + location[1]);
        int height = textView.getHeight();
        int width = textView.getWidth();
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, location[0]
                + width, Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, location[1] - height);

        mTranslateAnimation.setInterpolator(new AccelerateInterpolator());
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, 0);

        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 0.05f, 1f,
                0.05f, Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f);
        mScaleAnimation.setInterpolator(new LinearInterpolator());

        RotateAnimation mRotateAnimation = new RotateAnimation(0.0f, 15.0f,
                Animation.ABSOLUTE, location[0], Animation.ABSOLUTE,
                location[1] - height);
        ;
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.addAnimation(mTranslateAnimation);
        mAnimationSet.addAnimation(mRotateAnimation);

        mAnimationSet.setDuration(2000);
        return mAnimationSet;
    }

    /**
     * @author kuangtiecheng 签到加积分动画，待修改
     */
    public AnimationSet getAddAnimationSet() {
        AnimationSet mAnimationSet = new AnimationSet(true);
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, 0);
        ScaleAnimation mScaleAnimation = new ScaleAnimation(2f, 0.25f, 2f,
                0.25f, Animation.RELATIVE_TO_SELF, 0.25f,
                Animation.RELATIVE_TO_SELF, 0.25f);

        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);

        mAnimationSet.setDuration(1000);
        return mAnimationSet;
    }

}
