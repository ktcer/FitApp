package com.cn.fit.ui.patient.main.mynurse;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.R.color;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanAdovisoryListBeen;
import com.cn.fit.model.nurse.BeanEssayInfo;
import com.cn.fit.model.nurse.BeanExpert;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的保健护士
 *
 * @author kuangtiecheng
 */

public class NursePage extends ActivityBasicListView {
    private int pageType = 0;// 0代表普通护士主页，1代表我的保健专家
    private static String queryNurseDetail = "queryreservablenurseinfo";
    private ListView listViewEssay;
    private String[] id = new String[]{"我的设备", "驿站设备"};
    private String[] id2 = new String[]{"我的电话", "驿站电话"};
    private long myNurseID = 2;// 我的保健专家
    private String Id_Message;
    private int appoint_Type;
    private Button video, tel, upline, pic;
    private TextView cancel;
    private Dialog alterDialog, alterDialog1, alterDialog2;
    private CircleImageView imageView;
    private String ActivityTag = "MyCareNurse";
    private List<BeanEssayInfo> essayList;
    private long nurseID;
    private String diseaseName, resume;
    private UtilsSharedData sharedData;
    private String imageUrl1;
    private String imageUrl2;
    private String nurseName;
    private String positionName;
    private String hospitalName;
    private String ability;
    private String office;
    /**
     * 科室名称
     */
    private TextView officeNameTV;
    private TextView nurseNameTV;
    private TextView positionNameTV;
    private TextView hospitalNameTV;
    private TextView abilityTV;

    private TextView experctSummary;
    private TextView dvisory, essay;
    private DisplayImageOptions options, optionsAdvisroy;
    private ImageLoader imageLoader, imageLoaderAdvisroy;

    private AdovisoryListAdapter mAdapter;
    private List<BeanAdovisoryListBeen> tempNurseList;
    private String img;

    private boolean isFavored = false;// 是否已经收藏该护士
    private long userId;
    private BeanExpert beenExpert;
    /**
     * true:页面最上面那个保健专家，false：下面专家列表选着的专家
     */
    private boolean choosenurse = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycarenurse);
        initial();
        listViewEssay.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    protected void initial() {
        beenExpert = new BeanExpert();
        showProgressBar();
        HealthCareExpertTask1 hc = new HealthCareExpertTask1();
        hc.execute();

        UtilsSharedData.initDataShare(NursePage.this);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);

        img = getIntent().getExtras().getString("img");
        pageType = getIntent().getExtras().getInt(Constant.PAGE_TYPE);
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        imageView = (CircleImageView) findViewById(R.id.iv_nurse_activity);
        listViewEssay = (ListView) this.findViewById(R.id.lv_myCareNurse);
        video = (Button) findViewById(R.id.videoAppoint_myCareNurse);
        tel = (Button) findViewById(R.id.telAppoint_myCareNurse);
        upline = (Button) findViewById(R.id.upLine_myCareNurse);
        pic = (Button) findViewById(R.id.pic_myCareNurse);
        cancel = (TextView) findViewById(R.id.right_tv);

        listView = (XListView) this.findViewById(R.id.lv_advisory);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);

        tempNurseList = new ArrayList<BeanAdovisoryListBeen>();
//		for (int i = 0; i < 1; i++) {
//			BeanAdovisoryListBeen adovisoryListBeen = new BeanAdovisoryListBeen();
//			adovisoryListBeen
//					.setAnswer("回复:如果是甲状腺肿大，你现在服用的优甲乐就有一定缩小甲状腺的作用，但剂量可以增加一点。");
//			adovisoryListBeen.setDepartment("骨科");
//			adovisoryListBeen.setHospital("北医三院");
//			adovisoryListBeen.setImagUrl("");
//			adovisoryListBeen.setName("张三");
//			adovisoryListBeen
//					.setQuestion("问： 慢甲炎10多年了，注射过地塞米松，现脖子两侧肌肉肿大突出了，跟注射有关吗？怎么治疗？可以消吗,谢谢");
//			adovisoryListBeen.setStaff("主治医生");
//			adovisoryListBeen.setTime("2014-2-26");
//			tempNurseList.add(adovisoryListBeen);
//		}

        mAdapter = new AdovisoryListAdapter(this, tempNurseList);// tempNurseList还没赋值，明天注意
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.setClass(NursePage.this, AdvisiryHistory.class);
                // intent.putExtra("nurseID",
                // tempNurseList.get(arg2).getNurseID());
                // intent.putExtra("resume",
                // tempNurseList.get(arg2).getResume());
                // intent.putExtra("diseaseName", diseaseName);
                // intent.putExtra(Constant.PAGE_TYPE, 0);
                startActivity(intent);

            }
        });
        listView.setPullLoadEnable(false);
        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

        essayList = new ArrayList<BeanEssayInfo>();
        nurseID = getIntent().getExtras().getLong("nurseID");
        resume = getIntent().getExtras().getString("resume", "暂时没有简介");
        choosenurse = getIntent().getBooleanExtra("choosenurse", false);

        nurseNameTV = (TextView) findViewById(R.id.name_nurse_activity);
        positionNameTV = (TextView) findViewById(R.id.position_nurse_activity);
        hospitalNameTV = (TextView) findViewById(R.id.hospital_nurse_activity);
        officeNameTV = (TextView) findViewById(R.id.office_nurse_activity);
        abilityTV = (TextView) findViewById(R.id.introduction_nurse_activity);
        experctSummary = (TextView) findViewById(R.id.experct_summary);
        dvisory = (TextView) findViewById(R.id.advisory_tv);
        essay = (TextView) findViewById(R.id.essay_tv);
        experctSummary.setText(resume);// 专家简介
        cancel.setVisibility(View.VISIBLE);

        if (pageType == 0) {
            // 普通主页
            cancel.setText("收藏");
            titleText.setText("专家主页");
            diseaseName = getIntent().getStringExtra("diseaseName");
        } else {
            // 保健专家
            titleText.setText("保健专家");
            cancel.setText("申请更换");
            diseaseName = "糖尿病";
        }
        video.setOnClickListener(this);
        tel.setOnClickListener(this);
        upline.setOnClickListener(this);
        pic.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dvisory.setBackgroundColor(getResources().getColor(
                        color.blue_second));
                essay.setBackgroundColor(getResources().getColor(
                        color.trans_30_blue));
                listViewEssay.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        essay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                essay.setBackgroundColor(getResources().getColor(
                        color.blue_second));
                dvisory.setBackgroundColor(getResources().getColor(
                        color.trans_30_blue));
                listViewEssay.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });

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

//		BitmapToCircle bmtc = new BitmapToCircle();
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.default_user_icon);
//		Bitmap bitmap2 = bmtc.toRoundBitmap(bitmap);
//		imageView.setImageBitmap(bitmap2);
        showProgressBar();
        QueryNurseDetailTask task = new QueryNurseDetailTask();
        task.execute();

        dvisory.setBackgroundColor(getResources().getColor(color.blue_second));
    }

    private class ArticleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return essayList.size();
        }

        @Override
        public Object getItem(int position) {
            return essayList.get(position);
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
                convertView = LayoutInflater.from(NursePage.this).inflate(
                        R.layout.article_item_layout, null);
                holder.essayTitle = (TextView) convertView
                        .findViewById(R.id.essay_title_tv);
                holder.essayDate = (TextView) convertView
                        .findViewById(R.id.essay_date_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.essayDate.setText(essayList.get(position).getDateString());
            holder.essayTitle.setText(essayList.get(position).getEssayTitle());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView essayTitle;
        TextView essayDate;
    }


    /**
     * 获取保健庄家ID
     *
     * @author kuangtiecheng
     */

    private class HealthCareExpertTask1 extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();

            param.put("patientID", userId + "");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/member/patient/mynurse",
                        param, "utf-8");
                Log.i("result", result);
                jsonResult(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        private void jsonResult(String result2) throws Exception {
            // TODO Auto-generated method stub
            Gson gson = new Gson();
            beenExpert = gson.fromJson(result2, new TypeToken<BeanExpert>() {
            }.getType());

        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
        }
    }

    private class QueryNurseDetailTask extends
            AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", userId + "");
            if (choosenurse) {
                param.put("nurseID", beenExpert.getCoachID() + "");
            } else {
                param.put("nurseID", nurseID + "");
            }

            Log.i("input", userId + nurseID + ".." + param.toString());
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/yyzx/app/" + queryNurseDetail, param, "utf-8");
                Log.i("result", result);
                JSONObject jsObject = new JSONObject(result);
                nurseName = jsObject.getString("nurseName");
                positionName = jsObject.getString("positionName");
                hospitalName = jsObject.getString("hospitalName");
                // ability = jsObject.getString("ability");
                ability = jsObject.getString("ability");
                // ability="q";
                imageUrl1 = jsObject.getString("imageUrl1");
                imageUrl2 = jsObject.getString("imageUrl2");
                office = jsObject.getString("subDepartmentName");
                JSONArray jsonArray = new JSONArray(
                        jsObject.getString("essayList"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    BeanEssayInfo info = new BeanEssayInfo();
                    info.setEssayID(object.getLong("essayID"));
                    info.setEssayTitle(object.getString("essayTitle"));
                    info.setDateString(object.getString("dateString"));
                    essayList.add(info);
                }
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            nurseNameTV.setText(nurseName);
            positionNameTV.setText(positionName);
            hospitalNameTV.setText(hospitalName);
            abilityTV.setText("擅长:" + ability);
            officeNameTV.setText(office);
            if (choosenurse) {
                imageLoader.displayImage(AbsParam.getBaseUrl() + beenExpert.getPicUrl(),//imageUrl1
                        imageView, options);
            } else {
                imageLoader.displayImage(AbsParam.getBaseUrl() + img,//imageUrl1
                        imageView, options);
            }

            listViewEssay.setAdapter(new ArticleAdapter());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.videoAppoint_myCareNurse:
                // startActivity(telAppointment.class);
                Id_Message = null;
                appoint_Type = 1;
                ActivityTag = "AppointNurse";
                wehtherLogin(ActivityTag, true);
                // pop(true);
                break;
            case R.id.telAppoint_myCareNurse:
                // startActivity(telAppointment.class);
                Id_Message = null;
                appoint_Type = 2;
                ActivityTag = "AppointNurse";
                wehtherLogin(ActivityTag, false);
                // pop(false);
                break;
            case R.id.pic_myCareNurse:
//			Intent intentImage = new Intent(this, PicAdvisiry.class);//图文咨询屏蔽掉了
//			startActivity(intentImage);
                break;
            case R.id.upLine_myCareNurse:
                appoint_Type = 3;
                // startActivity(ClinicAppoint.class);
                UtilsSharedData sharedData = new UtilsSharedData();

                if (sharedData.isEmpty(Constant.LOGIN_STATUS)
                        || sharedData.getValueByKey(Constant.LOGIN_STATUS).equals(
                        "0")) {
                    ActivityTag = "uplineAppoint";
                    Toast.makeText(getApplicationContext(), "请您先登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                    intent.putExtra("ActivityTag", ActivityTag);
                    intent.putExtra("appointType", appoint_Type);
                    intent.putExtra("diseaseName", diseaseName);
                    intent.putExtra("nurseID", nurseID);
                    intent.putExtra("nurseName", nurseName);
                    intent.putExtra("positionName", positionName);
                    intent.putExtra("hospitalName", hospitalName);
                    intent.putExtra("ability", ability);
                    intent.putExtra("office", office);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NursePage.this,
                            AppointmentPage.class);
                    intent.putExtra("appointType", appoint_Type);
                    intent.putExtra("diseaseName", diseaseName);
                    intent.putExtra("nurseID", nurseID);
                    intent.putExtra("nurseName", nurseName);
                    intent.putExtra("positionName", positionName);
                    intent.putExtra("hospitalName", hospitalName);
                    intent.putExtra("ability", ability);
                    intent.putExtra("office", office);
                    startActivity(intent);
                }
                break;
            case R.id.right_tv:
                if (pageType == 0) {
                    if (isFavored) {
                        isFavored = false;
                        cancel.setText("收藏");
                        Toast.makeText(this, "取消收藏", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
                        cancel.setText("取消收藏");
                        isFavored = true;
                    }
                } else if (pageType == 1) {
                    // sharedData = new UtilsSharedData();
                    // if (sharedData.isEmpty(Constant.LOGIN_STATUS)
                    // ||
                    // sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("0"))
                    // {
                    // Intent intent = new Intent(getApplicationContext(),
                    // Login.class);
                    // intent.putExtra("ActivityTag", ActivityTag);
                    // startActivity(intent);
                    // } else {
                    apply_cancel();
                    // }
                }
                break;

            // case R.id.advisory_tv:
            // dvisory.setBackgroundColor(getResources().getColor(color.blue_second));
            // essay.setBackgroundColor(getResources().getColor(color.trans_30_blue));
            // break;
            // case R.id.essay_tv:
            // essay.setBackgroundColor(getResources().getColor(color.blue_second));
            // dvisory.setBackgroundColor(getResources().getColor(color.trans_30_blue));
            // break;
        }
    }

    /**
     * 弹窗，在点击视频预约或电弧预约的时候弹出
     *
     * @author Hubert
     */
    private void pop(boolean i) {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_single_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.indicate_Info);
        PickerView minute_pv = (PickerView) layout.findViewById(R.id.minute_pv);
        List<String> data = new ArrayList<String>();
        for (int a = 0; a < id.length; a++) {
            if (i) {
                title.setText("请选择您的接听设备");
                data.add(id[a]);
            } else {
                title.setText("请选择您的接听电话");
                data.add(id2[a]);
            }
        }
        minute_pv.setData(data);
        Id_Message = minute_pv.getText();
        minute_pv.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                Id_Message = text;
            }
        });
        Id_Message = minute_pv.getText();
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(NursePage.this,
                        AppointmentPage.class);
                intent.putExtra("appointType", appoint_Type);
                intent.putExtra("diseaseName", diseaseName);
                intent.putExtra("nurseID", nurseID);
                intent.putExtra("nurseName", nurseName);
                intent.putExtra("positionName", positionName);
                intent.putExtra("hospitalName", hospitalName);
                intent.putExtra("ability", ability);
                intent.putExtra("office", office);
                startActivity(intent);
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

    /***
     * 申请更换对话框
     *
     * @author kuangtiecheng
     */
    private void apply_cancel() {
        final EditText input = new EditText(this);
        input.setHint("请填写更换保健专家的理由");
        input.setMinLines(8);
        input.setGravity(Gravity.TOP);
        input.setSingleLine(false);
        input.setHorizontalScrollBarEnabled(false);
        alterDialog = new CustomDialog.Builder(this)
                .setTitle("更换保健专家")
                .setContentView(input)
                        // .setView(new EditText(this))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(NursePage.this, "正在申请更换中",
                                Toast.LENGTH_SHORT).show();
                        alterDialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        alterDialog.dismiss();
                    }
                }).create();
        alterDialog.show();
    }

    /**
     * 判断是否已经登录，若已经登录，就弹出dialog，若未登录，则先登录
     */
    private void wehtherLogin(String ActivityTag, Boolean i) {
        UtilsSharedData sharedData = new UtilsSharedData();
        if (sharedData.isEmpty(Constant.LOGIN_STATUS)
                || sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("0")) {
            Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
            intent.putExtra("ActivityTag", ActivityTag);
            startActivity(intent);
        } else {
            pop(i);
        }
    }

}
