package com.cn.fit.ui.patient.main.healthdiary;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.family.BeanFamilyResult;
import com.cn.fit.model.family.BeanResultUtils;
import com.cn.fit.model.healthdiary.BeanChoosHealthDiary;
import com.cn.fit.model.healthdiary.BeanHealthDiaryInfo;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ActivityHealthDiaryDetail extends ActivityBasic {
    private TextView userNum, detail, right;
    private CircleImageView detailImage;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private BeanHealthDiaryInfo healthDiary;
    private BeanChoosHealthDiary choosHealthDiary;
    private long healthDiaryID;
    private String title;
    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_diary_detail);
        imageInit();
        initView();
    }

    private void initView() {
        //获取存储的用户ID
        UtilsSharedData.initDataShare(ActivityHealthDiaryDetail.this);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);

        title = getIntent().getExtras().getString("title");
        healthDiaryID = getIntent().getExtras().getLong("healthDiaryID");
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText(title);
        right = (TextView) findViewById(R.id.right_tv);
        right.setVisibility(View.VISIBLE);
        right.setText("选择");
        right.setOnClickListener(this);

        healthDiary = new BeanHealthDiaryInfo();
        choosHealthDiary = new BeanChoosHealthDiary();
        userNum = (TextView) findViewById(R.id.users_num);
        detail = (TextView) findViewById(R.id.health_detail);
        detailImage = (CircleImageView) findViewById(R.id.heath_diray_pic);

    }

    private void imageInit() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_user_icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_user_icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_user_icon)  //设置图片加载/解码过程中错误时候显示的图片
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
        imageLoader = ImageLoader.getInstance();

    }

    /**
     * 健康日记详情
     *
     * @author kuangtiecheng
     */
    private class QueryHealthDairyTask extends
            AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("healthDiaryID", healthDiaryID + "");
            Log.i("input", AbsParam.getBaseUrl()
                    + "/interv/plan/detail/show" + param.toString());
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/interv/plan/detail/show", param, "utf-8");
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
            userNum.setText(healthDiary.getUsersNum() + "");
            detail.setText(healthDiary.getHealthDiaryDetail());
            imageLoader.displayImage(
                    AbsParam.getBaseUrl() + healthDiary.getImgUrl(),
                    detailImage, options);
            hideProgressBar();
        }
    }

    /**
     * 解析健康日記詳情
     *
     * @param result
     */
    private void JsonArrayToList(String result) {
        JSONArray jsonArray;
        try {
            Gson gson = new Gson();
            BeanFamilyResult bean = BeanResultUtils.parseResult(result);
            if (bean != null) {
                if (bean.getStatusCode() == -1) {
                    healthDiary = gson.fromJson(bean.getData().toString(),
                            new TypeToken<BeanHealthDiaryInfo>() {
                            }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public String GetNowDate() {
        String temp_str = "";
        Date dt = new Date();
        // 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
        temp_str = sdf.format(dt);
        return temp_str;
    }

    /**
     * 选着当前健康日记
     *
     * @author kuangtiecheng
     */
    private class ChooseHealthDairyTask extends
            AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("healthDiaryID", healthDiaryID + "");
            param.put("patientID", userId + "");
            param.put("stationID", "");
            param.put("startTime", GetNowDate());
            Log.i("input", AbsParam.getBaseUrl()
                    + "/interv/plan/patient/choose" + param.toString());
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/interv/plan/patient/choose", param, "utf-8");
                Log.i("result", result);
                JsonArray(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            if (choosHealthDiary.isSuccess()) {
                showToastDialogLongTime(choosHealthDiary.getMsg());
                finish();
            } else {
                showToastDialog("选着失败:原因" + choosHealthDiary.getMsg());
            }

        }
    }

    /**
     * 解析選著監看日記
     *
     * @param result
     */
    private void JsonArray(String result) {
        JSONArray jsonArray;
        try {
            Gson gson = new Gson();
            BeanFamilyResult bean = BeanResultUtils.parseResult(result);
            if (bean != null) {
                if (bean.getStatusCode() == -1) {
                    choosHealthDiary = gson.fromJson(bean.getData().toString(),
                            new TypeToken<BeanChoosHealthDiary>() {
                            }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryHealthDairyTask healthDiaryTask = new QueryHealthDairyTask();
        healthDiaryTask.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                showDialog();
                break;

            default:
                break;
        }
    }

    /**
     * 选着当前健康日记弹窗
     */
    private void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您要选择当前健康日记吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ChooseHealthDairyTask chdt = new ChooseHealthDairyTask();
                chdt.execute();
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

}
