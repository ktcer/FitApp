package com.cn.fit.ui.patient.main.healthdiary;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanCompleteNode;
import com.cn.fit.model.healthdiary.BeanContentNode;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthpost.doctorinterview.VideoPlay;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 训练指南
 *
 * @author kuangtiecheng
 */

public class ActivityHealthTraningGuide extends ActivityBasic {
    private LinearLayout groupViewLl;
    private ViewPager viewPager;
    private int pageType = 0;
    //	private long pageType=0;
    private TextView health_Summarize;
    private List<View> viewList = new ArrayList<View>();
    //滚动小圆点
    private int selectPage = 0;
    private ImageView[] imageViews;
    private ImageView imageView;
    private DisplayImageOptions options;

    //	private ImageLoader imageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageType = getIntent().getExtras().getInt(Constant.PAGE_TYPE);
        setContentView(R.layout.home_healthtrainingguide);
        imageInit();
        initView();
    }

    private boolean canPerform() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(calendar.getTime());
        if (dateStr.equals(ActivityHealthDiary.healthDiaryBean.getNodes().get(0).getDate())) {//HealthDiaryActivity 用activity的时候换回来，下面全是HealthDiaryFragment
            return true;
        }
        return false;
    }

    private void imageInit() {
        options = new DisplayImageOptions.Builder()
//	    	.showImageOnLoading(R.drawable.default_user_icon) //设置图片在下载期间显示的图片  
//	    	.showImageForEmptyUri(R.drawable.default_user_icon)//设置图片Uri为空或是错误的时候显示的图片  
//	    	.showImageOnFail(R.drawable.default_user_icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    private void initView() {
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("训练指南");
        TextView completeTV = (TextView) findViewById(R.id.right_tv);
        completeTV.setText("完成");
        if (ActivityHealthDiary.healthDiaryBean.getNodes().get(0).getNodes().get(pageType).getState() == 7) {
            completeTV.setVisibility(View.INVISIBLE);
        } else {
            completeTV.setVisibility(View.VISIBLE);
        }
        if (canPerform()) {
            completeTV.setVisibility(View.VISIBLE);
        } else {
            completeTV.setVisibility(View.INVISIBLE);
        }
        completeTV.setOnClickListener(this);
        groupViewLl = (LinearLayout) findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //设置每日训练说明
        String content = ActivityHealthDiary.healthDiaryBean.getNodes().get(0).getNodes().get(pageType).getContentStr();
        health_Summarize = (TextView) findViewById(R.id.health_summarize);
        health_Summarize.setText(content.replace("<video>", "").replace("</video>", ""));
        /**
         * 将需要滑动的View加入到viewList
         */
        final List<BeanContentNode> contentList = ActivityHealthDiary.healthDiaryBean.getNodes().get(0).getNodes().get(pageType).getContentList();
        for (int i = 0; i < contentList.size(); i++) {
            FrameLayout layout = new FrameLayout(this);//为本Activity创建一个线性布局对象
            //并且设置它的属性 android:layout_width 与 android:layout_height 都为 FILL_PARENT
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ImageView imageView1 = new ImageView(this);
            imageView1.setScaleType(ScaleType.FIT_XY);
            String imgURL = AbsParam.getBaseUrl() + contentList.get(i).getThumbnail();
            ImageLoader.getInstance().displayImage(imgURL, imageView1, options);
            imageView1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //打开对应该项的数据
                    arg0.startAnimation(AnimationUtils.loadAnimation(ActivityHealthTraningGuide.this,
                            R.anim.icon_scale));
                    String url = contentList.get(selectPage).getUrl();
                    startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                }
            });
            layout.addView(imageView1);

            ImageView imageView2 = new ImageView(this);
            imageView2.setPadding(10, 10, 10, 10);
            imageView2.setImageResource(R.drawable.icn_play_big);
            imageView2.setScaleType(ScaleType.CENTER_INSIDE);
            LinearLayout.LayoutParams picviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(imageView2, picviewParams);//加入的同时，也就设置了TextView相对于布局对象的布局属性 android:layout_width 与 android:layout_height

            viewList.add(layout);
        }

        /**
         * 定义个圆点滑动导航ImageView，根据View的个数而定
         */
        imageViews = new ImageView[viewList.size()];
        for (int i = 0; i < viewList.size(); i++) {
            imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            imageView.setPadding(20, 0, 20, 0);

            imageViews[i] = imageView;

            if (i == 0) {
                // 默认选中第一张图片
                imageViews[i]
                        .setBackgroundResource(R.drawable.ball);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.ball1);
            }

            groupViewLl.addView(imageViews[i]);
        }
        viewPager.setAdapter(new AdapterMyPager(viewList));
        viewPager.setOnPageChangeListener(new SwitchPageChangeListener());
    }

    // 指引页面更改事件监听器
    class SwitchPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.ball);
                selectPage = arg0;

                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.ball1);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.right_tv:
                showComfirmCompleteDialog();
                break;

            default:
                break;
        }
    }

    //弹出总结弹窗
    private void showComfirmCompleteDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否完成今日训练？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new Thread(runnableCompleteNode).start();
                showProgressBar();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    /*******************************************************
     * 完成当前节点
     */
    private Map<String, String> map;
    private String retStr;//获取到返回的json数据
    private BeanCompleteNode beanCompleteNode;
    //网络请求
    Runnable runnableCompleteNode = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/interv/node/complete";

        @Override
        public void run() {
            try {
                map = new HashMap<String, String>();
                map.put("patientPlanId", ActivityHealthDiary.healthDiaryBean.getPatientPlanId() + "");// 干预方案Id
                map.put("nodeId", pageType + "");// 干预方案节点id
                retStr = NetTool.sendPostRequest(url, map, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getSummaryResult(retStr);

                Message msg = new Message();
                if (retStr == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                completeNodeHandler.sendMessage(msg);

            } catch (Exception e) {
                completeNodeHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hideProgressBar();
                        showNetErrorInfo();
                    }
                }, 1000);
            }
        }
    };

    private Handler completeNodeHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    //展示获取的数据bean
                    hideProgressBar();
                    ToastUtil.showMessage("提交成功");
                    finish();
                    break;
                default:

                    break;
            }
        }
    };

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getSummaryResult(String jsonString) throws Exception {
        Gson gson = new Gson();
        beanCompleteNode = gson.fromJson(jsonString, BeanCompleteNode.class);
    }

}
