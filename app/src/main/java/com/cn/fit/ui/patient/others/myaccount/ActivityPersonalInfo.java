package com.cn.fit.ui.patient.others.myaccount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.patient.main.healthdiary.test.AscyncSubmitPersonalInfo;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FileUtils;
import com.cn.fit.util.StringUtil;
import com.cn.fit.util.UtilsImage;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.customgallery.GalleryHelper;
import com.cn.fit.util.customgallery.model.PhotoInfo;
import com.cn.fit.util.superpicker.CustomDatePicker;
import com.cn.fit.util.superpicker.CustomNumberPicker;
import com.cn.fit.util.superpicker.CustomNumberPickerDouble;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 会员信息修改页面
 *
 * @author kuangtiecheng
 */

@SuppressLint("ResourceAsColor")
public class ActivityPersonalInfo extends ActivityBasic implements
        OnClickListener {
    private ImageView iv;
    private String userName, userAccount, userGender, userBirth, userTall,
            userWeight;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    //	private Bitmap head;// 头像Bitmap
    private int resultdata;
    //	private static String path = AppDisk.diskLocal+"myHead/";// sd路径
    private RelativeLayout mMyPic;
    private SettingItem mMyAccount;
    private EditText mMyName;
    private CustomNumberPicker mMySex;
    private CustomDatePicker mMyBirth;
    private CustomNumberPickerDouble mMyTall;

    private CustomNumberPickerDouble mMyWeight;
    public static int type1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        setdata();
        initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    private void initial() {
        ((TextView) findViewById(R.id.middle_tv)).setText("编辑信息");
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        userAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
        userGender = UtilsSharedData.getValueByKey(Constant.USER_GENDER);
        userBirth = UtilsSharedData.getValueByKey(Constant.USER_BIRTHDAY);
        userTall = UtilsSharedData.getValueByKey(Constant.USER_HEIGHT);
        userWeight = UtilsSharedData.getValueByKey(Constant.USER_WEIGHT);
        iv = (ImageView) findViewById(R.id.pic);
        iv.setOnClickListener(this);

        mMyPic = (RelativeLayout) findViewById(R.id.item_pic);
        mMyPic.setOnClickListener(this);

        mMyAccount = (SettingItem) findViewById(R.id.item_mytel);
        mMyAccount.setCheckText(userAccount);

        mMyName = (EditText) findViewById(R.id.item_myname);
        mMyName.addTextChangedListener(new CustomTextWacher(0));
        mMyName.setHint(userName);
        mMyName.setHintTextColor(getResources().getColor(R.color.font_gray));
        mMyName.setOnClickListener(this);

        mMySex = (CustomNumberPicker) findViewById(R.id.item_mysex);
        mMySex.addTextChangedListener(new CustomTextWacher(1));
        mMySex.setHint(userGender);
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.gender));
        List<String> arrayList = new ArrayList<String>(list);
        mMySex.setList(arrayList);
        mMySex.setTips("请选择您的性别");

        mMyBirth = (CustomDatePicker) findViewById(R.id.item_mybirth);
        mMyBirth.addTextChangedListener(new CustomTextWacher(2));
        mMyBirth.setHint(userBirth);

        mMyTall = (CustomNumberPickerDouble) findViewById(R.id.item_mytall);
        mMyTall.addTextChangedListener(new CustomTextWacher(3));
        mMyTall.setHint(userTall);
        mMyTall.setMaxAndMinValue(50, 250, 0, 9);
        mMyTall.setTips("请选择您的身高（厘米）");
        mMyTall.setLittleTips("整数位            小数位");
        if (userTall.equals("")) {//首次注册没有填写信息
        } else {
            String[] high = userTall.split("\\.");
            mMyTall.setSelectedValueLeft(high[0]);
            mMyTall.setSelectedValueRight(high[1]);
        }


        mMyWeight = (CustomNumberPickerDouble) findViewById(R.id.item_myweight);
        mMyWeight.addTextChangedListener(new CustomTextWacher(4));
        mMyWeight.setHint(userWeight);
        mMyWeight.setMaxAndMinValue(5, 200, 0, 9);
        mMyWeight.setLittleTips("整数位            小数位");
        mMyWeight.setTips("请选择您的体重（千克）");
        if (userTall.equals("")) {//首次注册没有填写信息
        } else {
            String[] weight = userWeight.split("\\.");
            mMyWeight.setSelectedValueLeft(weight[0]);
            mMyWeight.setSelectedValueRight(weight[1]);
        }

//		path = AppDisk.appInursePath + userAccount
//				+ File.separator + AppDisk.MYHEAD;
        imageUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        imageLoader.displayImage(AbsParam.getBaseUrl() + imageUrl, iv, options);

    }


    private void changeNameDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setText(userName);
        inputServer.setMinLines(8);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(getResources().getColor(R.color.font_gray));
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        inputServer.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd, count;

            @Override
            public void afterTextChanged(Editable arg0) {
                editStart = inputServer.getSelectionStart();
                editEnd = inputServer.getSelectionEnd();
                String name1 = temp.toString();
                if (StringUtil.isEmpty(name1)) {
                    ToastUtil.showMessage("输入不能为空");
                } else {
                    if (StringUtil.isName(name1)) {
                    } else {
                        arg0.delete(editEnd - count, editEnd);
                        inputServer.setText(arg0);
                        inputServer.setSelection(arg0.toString().length());
                        ToastUtil.showMessage("请输入1-15位字母或数字或者中文或三者组合,请重新输入");
                    }

                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                temp = arg0;
                count = arg3;


            }

        });

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = inputServer.getText().toString().trim();
                if (StringUtil.isEmpty(name)) {
                    ToastUtil.showMessage("输入不能为空");
                } else {
                    if (StringUtil.isName(name)) {
                        mMyName.setText(inputServer.getText().toString());

                    } else {
                        ToastUtil.showMessage("请输入1-15位字母或数字或者中文或三者组合");
                    }

                }

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

    private class CustomTextWacher implements TextWatcher {
        int type;


        public CustomTextWacher(int type) {
            this.type = type;

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            type1 = type;
            // TODO Auto-generated method stub
            switch (type) {
                case 0:
                    userName = arg0.toString();
                    break;
                case 1:
                    userGender = arg0.toString();
                    break;
                case 2:
                    userBirth = arg0.toString();
                    break;
                case 3:
                    userTall = arg0.toString();
                    break;
                case 4:
                    userWeight = arg0.toString();
                    break;
                default:
                    break;
            }
            updateData();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.item_pic: /* 设置 */
                GalleryHelper.openGallerySingle(ActivityPersonalInfo.this, true, new GalleryImageLoader());
//			showDialog();
                break;
            case R.id.item_myname: /* 修改名称 */
                changeNameDialog();
                break;

            case R.id.pic:
                UtilsImage.displayBigPic(ActivityPersonalInfo.this, imageUrl);
                break;
            default:
                break;
        }
    }

    /**
     * 提交修改的信息数据
     */
    private void updateData() {
        // **提交注册信息*/
        AscyncSubmitPersonalInfo mTask = new AscyncSubmitPersonalInfo(this,
                UtilsSharedData.getLong(Constant.USER_ID, 1) + "", userName,
                userGender, userBirth, userTall, userWeight, false);
        mTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
                PhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if (photoInfo != null) {
                    //修改头像上传服务器
                    ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), iv);
                    UpPhotoTask upt = new UpPhotoTask(photoInfo.getPhotoPath());
                    upt.execute();
                }

                if (photoInfoList != null) {
                    Toast.makeText(this, "选择了" + photoInfoList.size() + "张", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private String img;

    private class UpPhotoTask extends AsyncTask<Integer, Integer, String> {
        String result;
        String path;

        public UpPhotoTask(String path) {
            this.path = path;
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userid", userId + "");
//			param.put("userType", 0 + "");//
            Log.i("input",
                    AbsParam.getBaseUrl()
                            + param.toString());
            try {
                result = NetTool.uploadFile(AbsParam.getBaseUrl()
                                + "/base/app/uploadpicture", path, param,
                        null);
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
            hideProgressBar();
            if (resultdata == 1) {
                UtilsSharedData.saveKeyMustValue(Constant.USER_IMAGEURL, img);
                FileUtils.deleteDir();
            } else {
                showToastDialogLongTime("修改头像失败");
            }

        }
    }

    /**
     * @param result
     */
    private void JsonArrayToList(String result) {
        try {
            JSONObject jso = new JSONObject(result);
            resultdata = jso.getInt("result");
            jso.getString("detail");
            img = jso.getString("fileurl");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
