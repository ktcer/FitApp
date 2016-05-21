package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.model.nurse.BeanTravelDetail;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.UtilsImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ActivityHealthPostDetail extends ActivityBasic {
    private TextView title, introduction, actualPay, Pay, customService;
    private ImageView iv;
    private FButton buy;
    private DisplayImageOptions options;
    private BeanHealthPost beanHealthPost;
    private ArrayList<String> picList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthpostdetail);
        getData();
        initView();
        setData();
    }

    private void getData() {
        beanHealthPost = (BeanHealthPost) getIntent().getSerializableExtra(
                Constant.TOUR_BEAN);
        gerUrls();
    }

    private void gerUrls() {
        AscyncGetTravelDetail GTD = new AscyncGetTravelDetail(beanHealthPost.getTravelID() + "") {
            @Override
            protected void onPostExecute(BeanTravelDetail result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if ((result != null) && (result.getShowurl() != null)) {
                    String[] picUrls = result.getShowurl().split("&");
                    if (!result.getShowurl().trim().equals("")) {
                        picList = new ArrayList<String>();
                        for (String url : picUrls) {
                            picList.add(url);
                        }
                    }
                }

            }

        };
        GTD.execute();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.middle_tv);
        findViewById(R.id.txt_postdetail).setOnClickListener(this);
        findViewById(R.id.txt_postpic).setOnClickListener(this);
        findViewById(R.id.txt_postsummarize).setOnClickListener(this);
        findViewById(R.id.txt_postsummarize).setVisibility(View.GONE);
        introduction = (TextView) findViewById(R.id.introduction_activity);
        actualPay = (TextView) findViewById(R.id.actPay_activity);
        Pay = (TextView) findViewById(R.id.Pay_activity);
        customService = (TextView) findViewById(R.id.customService_activity);
        iv = (ImageView) findViewById(R.id.image_activity);
        buy = (FButton) findViewById(R.id.buy_activity);
        Pay.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        title.setText("活动详情");
        options = AppMain.initImageOptions(R.drawable.default_life_icon, true);// 构建完成
        buy.setCornerRadius(3);
        buy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ActivityHealthPostDetail.this,
                        ActivityCocahSubmitOrder.class);
                intent.putExtra("img", beanHealthPost.getCover());
                intent.putExtra(
                        "title",
                        "【" + beanHealthPost.getTag() + "】"
                                + beanHealthPost.getActivetime() + " "
                                + beanHealthPost.getTitle());
                intent.putExtra("price", "¥" + beanHealthPost.getFinalprice());
                intent.putExtra("travelID", beanHealthPost.getTravelID());
                startActivity(intent);
            }
        });
    }


    private void setData() {
        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl() + beanHealthPost.getCover(), iv, options);
        introduction.setText("【" + beanHealthPost.getTag() + "】"
                + beanHealthPost.getActivetime() + " "
                + beanHealthPost.getTitle());
        actualPay.setText("¥" + beanHealthPost.getFinalprice());
        Pay.setText("¥" + beanHealthPost.getOriprice());

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_postpic:
                if (picList != null) {
                    UtilsImage.displayBigPics(ActivityHealthPostDetail.this, picList, 0);
                } else {
                    ToastUtil.showMessage("好像这个活动没有放图片哦~");
                }

                break;
            case R.id.txt_postdetail:
                String URL = AbsParam.getBaseUrl() + "/web/travel/detail?travelID="
                        + beanHealthPost.getTravelID() + "&platform=app.html";
                intent = new Intent(ActivityHealthPostDetail.this,
                        ActivityDetailWebService.class);
                intent.putExtra("url", URL);
                intent.putExtra("title", "活动详情");
                startActivity(intent);
                break;
            case R.id.txt_postsummarize:
                //页面是写死的，暂时不要
//			intent = new Intent(ActivityHealthPostDetail.this,
//					ActivitySummary.class);
//			// intent.putExtra("title", "活动概要");
//			startActivity(intent);
                break;
            default:
                break;
        }
    }


}
