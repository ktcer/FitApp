/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.contact;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.customer.coach.ActivityCoachPage;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsImage;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jorstin on 2015/3/18.
 */
public class ContactDetailActivity extends ECSuperActivity implements View.OnClickListener {

    public final static String RAW_ID = "raw_id";
    public final static String MOBILE = "mobile";
    public final static String DISPLAY_NAME = "display_name";
    public final static String IMAGEURL = "imageurl";
    public final static String REMAIN_TIME = "remain_time";
    public final static String ISMASTER = "ismaster";
    private CircleImageView mPhotoView;
    private EmojiconTextView mUsername;
    private TextView mNumber, mRemainTime;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private ECContacts mContacts;
    private FButton bindMasterBtn;
    private int isMaster = 1;
    private String expertId;
    private View.OnClickListener onClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mContacts == null) {
                return;
            }
            Intent intent = new Intent(ContactDetailActivity.this, ChattingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(ChattingActivity.RECIPIENTS, mContacts.getContactid());
            intent.putExtra(ChattingActivity.CONTACT_USER, mContacts.getNickname());
            intent.putExtra(ChattingActivity.CONTACT_IMGURL, mContacts.getImgUrl());
            intent.putExtra(ChattingActivity.CONTACT_REMAINTIME, mContacts.getRemainServeTime());
            startActivity(intent);
            setResult(RESULT_OK);
            finish();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.layout_contact_detail;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initActivityState(savedInstanceState);
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, R.string.contact_contactDetail, this);
    }


    /**
     * @param savedInstanceState
     */
    String imgUrl = "";

    @SuppressLint("ResourceAsColor")
    private void initActivityState(Bundle savedInstanceState) {
        long rawId = getIntent().getLongExtra(RAW_ID, -1);
        if (rawId == -1) {
            String mobile = getIntent().getStringExtra(MOBILE);
            String displayname = getIntent().getStringExtra(DISPLAY_NAME);
            imgUrl = getIntent().getStringExtra(IMAGEURL);
            int remainTime = getIntent().getIntExtra(REMAIN_TIME, 0);
            mContacts = ContactSqlManager.getCacheContact(mobile);
//            isMaster = getIntent().getIntExtra(ISMASTER, 2);
            if (mContacts == null) {
                mContacts = new ECContacts(mobile);
                mContacts.setNickname(displayname);
                mContacts.setImgUrl(imgUrl);
                mContacts.setRemainServeTime(remainTime);
//                mContacts.setIsmaster(isMaster);
            }
        }

        if (mContacts == null && rawId != -1) {
            mContacts = ContactSqlManager.getContact(rawId);
        }

        if (mContacts == null) {
            ToastUtil.showMessage(R.string.contact_none);
            finish();
            return;
        }
        mImageLoader.displayImage(AbsParam.getBaseUrl() + mContacts.getImgUrl(), mPhotoView, options);
        mUsername.setText(TextUtils.isEmpty(mContacts.getNickname()) ? mContacts.getContactid() : mContacts.getNickname());
        mNumber.setText("教练工号：" + mContacts.getContactid());
        if (mContacts.getRemainServeTime() < 5) {
            if (mContacts.getRemainServeTime() <= 0) {
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
                mPhotoView.setColorFilter(cf);
                mUsername.setTextColor(getResources().getColor(R.color.font_gray));
            }
            //显示红色
            mRemainTime.setTextColor(getResources().getColor(R.color.red));
        } else if ((mContacts.getRemainServeTime() > 5) && (mContacts.getRemainServeTime() < 10)) {
            //显示橙色
            mRemainTime.setTextColor(getResources().getColor(R.color.orange));
        } else if (mContacts.getRemainServeTime() > 10) {
            //显示绿色
            mRemainTime.setTextColor(getResources().getColor(R.color.lightgreen));
        }
        mRemainTime.setText(mContacts.getRemainServeTime() + "天");
        bindMasterBtn.setOnClickListener(this);
        if (isMaster == 1) {
            bindMasterBtn.setVisibility(View.GONE);
        }
    }


    /**
     *
     */
    private void initView() {
        UtilsSharedData.initDataShare(ContactDetailActivity.this);
        mPhotoView = (CircleImageView) findViewById(R.id.desc);
        mPhotoView.setOnClickListener(this);
        mUsername = (EmojiconTextView) findViewById(R.id.contact_nameTv);
        mNumber = (TextView) findViewById(R.id.contact_numer);
        mRemainTime = (TextView) findViewById(R.id.contact_remaintime);
        findViewById(R.id.txt_assist_page).setOnClickListener(this);
        FButton chat = (FButton) findViewById(R.id.entrance_chat);
        chat.setOnClickListener(onClickListener);
        chat.setCornerRadius(3);
        bindMasterBtn = (FButton) findViewById(R.id.ismaster);
        bindMasterBtn.setCornerRadius(3);
        mImageLoader = ImageLoader.getInstance();
        options = AppMain.initImageOptions(R.drawable.icon_touxiang_persion_gray, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhotoView != null) {
            mPhotoView.setImageDrawable(null);
        }
        onClickListener = null;
        mContacts = null;

    }

    private void bindMaster() {
        CustomDialog.Builder builder = new CustomDialog.Builder(ContactDetailActivity.this);
        builder.setTitle("提示");
        builder.setMessage("主管秘书只能有一位，您确定要将主管健康秘书修改为" + mContacts.getNickname() + "?");
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                AnscynBindMaster BM = new AnscynBindMaster();
                BM.execute();
                arg0.dismiss();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        builder.create().show();
    }

    private class AnscynBindMaster extends AsyncTask<Integer, Integer, String> {
        String detail;

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            String patientId = UtilsSharedData.getLong(Constant.USER_ID, 0) + "";
            expertId = mContacts.getContactid().substring(0, mContacts.getContactid().length() - 1);
            param.put("expertId", expertId + "");
            param.put("patientId", patientId);
            Log.i("test", "expertId:" + expertId + "patientId:" + patientId);
            try {
                String url = AbsParam.getBaseUrl() + "/member/request/setMasterExpert";
                String result = NetTool.sendHttpClientPost(url, param, "utf-8");
                Log.i("result", result);
                JSONObject json = new JSONObject(result);
                detail = json.getString("detail");
            } catch (Exception e) {
                // TODO: handle exception
            }
            return detail;
        }

        @Override
        protected void onPostExecute(String detail) {
            // TODO Auto-generated method stub
            if (detail.equals("success")) {
                Toast.makeText(ContactDetailActivity.this, "您已经成功将" + mContacts.getNickname() + "设置为您的主管秘书", Toast.LENGTH_SHORT).show();
                bindMasterBtn.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                v.startAnimation(AnimationUtils.loadAnimation(ContactDetailActivity.this,
                        R.anim.icon_scale));
                hideSoftKeyboard();
                finish();
                break;
            case R.id.desc:
                UtilsImage.displayBigPic(ContactDetailActivity.this, imgUrl);
                break;
            case R.id.txt_assist_page:
//				//专家主页
                Intent intent = new Intent(this, ActivityCoachPage.class);
                intent.putExtra("ExpertId", Long.parseLong(mContacts.getContactid().substring(0, mContacts.getContactid().length() - 1)));
                intent.putExtra("image", mContacts.getImgUrl());
                startActivity(intent);
                break;
            case R.id.ismaster:
                if (isMaster == 0) {
                    bindMaster();
                }
                break;
            default:
                break;
        }
    }
}
