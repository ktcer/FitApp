package com.cn.fit.ui.patient.main.healthdiary.test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.util.Constant;
import com.cn.fit.util.FButton;
import com.cn.fit.util.Regex;
import com.cn.fit.util.RegexNumber;
import com.cn.fit.util.StringUtil;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.superpicker.CustomDatePicker;
import com.cn.fit.util.superpicker.CustomNumberPickerDouble;

@SuppressWarnings("ucd")
public class ActivityTestPersonInfo extends ActivityBasic implements
        OnClickListener {
    private TextView titleText;
    private EditText et1, et2;
    private CustomNumberPickerDouble et3, et4;
    private FButton btnStartTest;
    private ImageView iv1;

    private String birthdayet;
    private String gender = "男";
    private String height = "";
    private String weight = "";
    private String name = "";
    private int isFirst = 2;
    /**
     * 后台返回的数据
     */

    private Regex reGexNumber;
    /**
     * 用户类型，0表示患者
     */
    private long userId;
    private String telephonenum, password1, macAddress;
    public static boolean isfix = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthpersoninfo);
        initial();
    }

    private void initial() {
        macAddress = getIntent().getStringExtra("macAddress");
        password1 = getIntent().getStringExtra("password1");
        telephonenum = getIntent().getStringExtra("telephonenum");
        // Typeface typeface = Typeface.createFromAsset(getAssets(),
        // "fangzheng.ttf");
        reGexNumber = new RegexNumber();
        isFirst = getIntent().getIntExtra("isFirst", 2);
        findViewById(R.id.left_Btn).setVisibility(View.INVISIBLE);
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("填写基本信息");
//		String tempText = "完善信息并进行测评有助于我们为您量身打造科学的健康日记，并赠送<font color='#FFA500'>30金币</font>哦。";
        // ((TextView)
        // findViewById(R.id.personinfo_tip_tv)).setTypeface(typeface);
//		((TextView) findViewById(R.id.personinfo_tip_tv)).setText(Html
//				.fromHtml(tempText));
        UtilsSharedData.initDataShare(this);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
//		((TextView) findViewById(R.id.right_tv)).setVisibility(View.VISIBLE);
//		((TextView) findViewById(R.id.right_tv)).setText("跳过");
//		((TextView) findViewById(R.id.right_tv)).setOnClickListener(this);
        iv1 = (ImageView) findViewById(R.id.delete_name);
        et1 = (EditText) findViewById(R.id.tv_personinfo_name);
        et2 = (CustomDatePicker) findViewById(R.id.tv_personinfo_birthday);

        et3 = (CustomNumberPickerDouble) findViewById(R.id.tv_personinfo_tall);
        et4 = (CustomNumberPickerDouble) findViewById(R.id.tv_personinfo_weight);
        etClick();
        btnStartTest = (FButton) findViewById(R.id.btn_personinfo_starttest);
        btnStartTest.setOnClickListener(this);
        btnStartTest.setCornerRadius(3);
        btnStartTest.setButtonColor(getResources().getColor(
                R.color.fbutton_color_concrete));
        btnStartTest.setEnabled(false);
        iv1.setOnClickListener(this);
        UtilsSharedData.initDataShare(this);

        // 根据ID找到RadioGroup实例
        RadioGroup group = (RadioGroup) this
                .findViewById(R.id.tv_personinfo_gender_radioGroup);
        // 绑定一个匿名监听器
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                // 获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) ActivityTestPersonInfo.this
                        .findViewById(radioButtonId);
                // 更新文本内容，以符合选中项
                gender = rb.getText().toString();
            }
        });
    }

    private void judgeInfo() {
        name = et1.getText().toString().trim();
        birthdayet = et2.getText().toString().trim();
        height = et3.getText().toString().trim();
        weight = et4.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_personinfo_starttest:
                showProgressBar();
                judgeInfo();
                // **提交注册信息*/
                isfix = true;
                AscyncSubmitPersonalInfo mTask = new AscyncSubmitPersonalInfo(this,
                        userId + "", name, gender, birthdayet, height, weight, isfix) {

                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        if (resultStatus == 1) {
                            // 成功
                            startActivity(TabActivityMain.class);
                        } else {
                            // 失敗
                            ToastUtil.showMessage("您的信息好像没有正确提交哦，" + resultDetail);
                        }
                    }

                };
                mTask.execute();
                break;
            case R.id.right_tv:
//			UtilsSharedData.initDataShare(ActivityTestPersonInfo.this);
                if (isFirst == 0) {
                    startActivity(ActivityEvaluationCenter.class);
                }
                finish();
                break;
            case R.id.delete_name:
                et1.setText("");
                break;
            default:
                break;

        }
    }

    private void fillAllInfo() {
        // 判断是否已经填写所有信息
        if ((!et1.getText().toString().equals(""))
                && (!et2.getText().toString().equals(""))
                && (!et3.getText().toString().equals(""))
                && (!et4.getText().toString().equals(""))) {
            btnStartTest.setEnabled(true);
            btnStartTest.setButtonColor(getResources().getColor(
                    R.color.blue_second));
        } else {
            btnStartTest.setEnabled(false);
            btnStartTest.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_concrete));
        }
    }

    public void etClick() {
        et1.setFocusable(true);
        et1.addTextChangedListener(new CustomTextWacher1(iv1));
        et2.addTextChangedListener(new CustomTextWacher(null));
        et3.addTextChangedListener(new CustomTextWacher(null));
        et4.addTextChangedListener(new CustomTextWacher(null));
        et3.setMaxAndMinValue(50, 250, 0, 9);
        et3.setSelectedValueLeft("160");
        et3.setSelectedValueRight("0");
        et3.setTips("请选择您的身高（厘米）");
        et3.setLittleTips("整数位            小数位");

        et4.setMaxAndMinValue(5, 200, 0, 9);
        et4.setSelectedValueLeft("60");
        et4.setSelectedValueRight("0");
        et4.setTips("请选择您的体重（千克）");
        et4.setLittleTips("整数位            小数位");
    }

    private class CustomTextWacher1 implements TextWatcher {

        ImageView mImageView;

        public CustomTextWacher1(ImageView iv) {
            mImageView = iv;

        }

        private CharSequence temp;
        private int editStart;
        private int editEnd, count;

        @Override
        public void afterTextChanged(Editable arg0) {
            editStart = et1.getSelectionStart();
            editEnd = et1.getSelectionEnd();
            String name1 = temp.toString();
            if (StringUtil.isEmpty(name1)) {
                ToastUtil.showMessage("输入不能为空");
            } else {
                if (StringUtil.isName(name1)) {
                } else {
                    arg0.delete(editEnd - count, editEnd);
                    et1.setText(arg0);
                    et1.setSelection(arg0.toString().length());
                    ToastUtil.showMessage("请输入1-15位字母或数字或者中文或三者组合,请重新输入");
                }

            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
            temp = arg0;
            count = arg3;
            fillAllInfo();
            if (mImageView == null) {
                return;
            }
            if (arg0.toString().equals("")) {
                mImageView.setVisibility(View.GONE);
            } else {
                mImageView.setVisibility(View.VISIBLE);
            }
        }

    }

    private class CustomTextWacher implements TextWatcher {

        ImageView mImageView;

        public CustomTextWacher(ImageView iv) {
            mImageView = iv;
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
            // TODO Auto-generated method stub
            fillAllInfo();
            if (mImageView == null) {
                return;
            }
            if (arg0.toString().equals("")) {
                mImageView.setVisibility(View.GONE);
            } else {
                mImageView.setVisibility(View.VISIBLE);
            }
        }

    }

}
