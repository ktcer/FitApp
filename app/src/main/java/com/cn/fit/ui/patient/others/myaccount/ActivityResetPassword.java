package com.cn.fit.ui.patient.others.myaccount;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.FButton;
import com.cn.fit.util.Regex;
import com.cn.fit.util.RegexNumber;
import com.cn.fit.util.RegexPwd;
import com.cn.fit.util.SmsContent;
import com.cn.fit.util.progressbutton.iml.ActionProcessButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ResourceAsColor")
public class ActivityResetPassword extends ActivityBasic implements OnClickListener {
    private static String macAddress;
    private TextView titleText, forgetPSD, register;
    private EditText et1, et2, et3, et4;
    private FButton getCode_btn;
    private ActionProcessButton btnChangePassword;
    private ImageView iv1, iv2, iv3, iv4;
    /**
     * 获取验证码接口
     */
    private String interface_name;
    /**
     * 注册使用的电话号码
     */
    private String telephonenum;
//	private String name;
    /**
     * 注册时获取的验证码
     */
    private String identifyingCode;
    /**
     * 输入的密码
     */
    private String password1, password2;
    private int CodeResultBack;
    private int RegisterResultBack;
    private String CodeDetialBack;
    private String RegisterDetailBack;
    private TimeCount time;
    private Map<String, String> map;
    private Regex reGexPhone, reGexPwd;
    /**
     * 后台返回的数据
     */
    private String retutrnStr;
    private Message msg;
    private static final int NoCode = 0;
    private static final int GetCode = 1;
    private static final int RESTPASSWORDFAIL = 2;
    private static final int RESTPASSWORDSUCCESS = 3;
    private static final int IDNTIFINGCODEFAIL = 5;//
    private static final int NOTUSER = 6;
    private static final int PSWnoMatch = 4;
    private ProgressGenerator progressGenerator;
    /**
     * 用户类型，0表示患者
     */
    private static final String userType = "1";

    //	private TimeCount time;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetting_password);
        initial();
    }

    private void initial() {
        reGexPhone = new RegexNumber();
        reGexPwd = new RegexPwd();
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("修改密码");
        iv1 = (ImageView) findViewById(R.id.delete_Login);
        iv2 = (ImageView) findViewById(R.id.delete1_Login);
        iv3 = (ImageView) findViewById(R.id.delete2_Login);
        iv4 = (ImageView) findViewById(R.id.delete3_Login);
        et1 = (EditText) findViewById(R.id.phone_Register);
        et2 = (EditText) findViewById(R.id.code_Register);
        et3 = (EditText) findViewById(R.id.password_Register);
        et4 = (EditText) findViewById(R.id.password2_Register);

        etClick();
        getCode_btn = (FButton) findViewById(R.id.getCode_Register);
        getCode_btn.setOnClickListener(this);
        btnChangePassword = (ActionProcessButton) findViewById(R.id.change_password);
        btnChangePassword.setOnClickListener(this);
        btnChangePassword.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator = new ProgressGenerator(btnChangePassword);
        getCode_btn.setCornerRadius(3);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.change_password:                                            /**提交注册信息*/
                interface_name = new String();
                interface_name = "/base/app/findapppsd";
                telephonenum = et1.getText().toString();
                identifyingCode = et2.getText().toString();
                password1 = et3.getText().toString();
                password2 = et4.getText().toString();
//			name = et5.getText().toString();
//			if(!name.equals("")&!telephonenum.equals("")&!identifyingCode.equals("")&!password1.equals("")&!password2.equals("")){
                if (!telephonenum.equals("") & !identifyingCode.equals("") & !password1.equals("") & !password2.equals("")) {
                    if (password1.length() < 6 || password1.length() > 64) {
                        ToastUtil.showMessage("密码长度必须在6-64个字符之间");
                    } else {
                        if (password1.equals(password2)) {
                            if (reGexPwd.judge(password1)) {
                                //跳转到完善信息页面
//						Intent intent = new Intent(this, RegisterInfo.class);
//						intent.putExtra(Constant.USER_ACCOUNT, telephonenum);
//						intent.putExtra(Constant.USER_PASS, password1);
//						intent.putExtra(Constant.USER_IDENTIFY, identifyingCode);
//						startActivity(intent);
//						showProgressBar();
                                progressGenerator.start();
                                setInputEnabled(false);
                                new Thread(runnableRegister).start();
                            } else {
                                showToastDialog("密码只能" + reGexPwd.getRule());
                            }
                        } else {
                            showToastDialog("两次密码输入不一致");
                        }
                    }
                } else {
                    showToastDialog("请填写所有信息");
                }
                break;
            case R.id.getCode_Register:
                telephonenum = et1.getText().toString();
                Log.i("电话", telephonenum);
                if (reGexPhone.judge(telephonenum)) {
                    if (telephonenum.length() == 11) {
                        AsyncJudgeNumberExsits mTask = new AsyncJudgeNumberExsits(telephonenum) {

                            @Override
                            protected void onPostExecute(String result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result.equals("1")) {
                                    ToastUtil.showMessage("该用户不存在");
                                } else if (result.equals("0")) {
                                    time = new TimeCount(60000, 1000);
                                    time.start();
                                    interface_name = "/base/app/getidentifycode";
                                    map = new HashMap<String, String>();
                                    map.put("telephoneNum", telephonenum);
                                    map.put("terminal", "ANDROID");
                                    getCode_btn.setEnabled(false);
                                    getCode_btn.setButtonColor(R.color.fbutton_color_concrete);
                                    new Thread(runnableRegister).start();
                                    // 注册短信监听
                                    SmsContent content = new SmsContent(ActivityResetPassword.this,
                                            new Handler(), et2);
                                    // 注册短信变化监听
                                    ActivityResetPassword.this.getContentResolver().registerContentObserver(
                                            content.SMS_URI_INBOX, true, content);
                                }
                            }

                        };
                        mTask.execute();

//					time = new TimeCount(60000, 1000);
//					time.start();
//					interface_name = new String();
//					interface_name = "/base/app/getidentifycode";
//					map = new HashMap<String, String>();
//					map.put("telephoneNum", telephonenum);
//					new Thread(runnableRegister).start();
//					// 注册短信监听
//					SmsContent content = new SmsContent(this,
//							new Handler(), et2);
//					// 注册短信变化监听
//					this.getContentResolver().registerContentObserver(
//							Uri.parse("content://sms/"), true, content);
                    } else {
                        showToastDialog("手机号长度不对");
                    }
                } else {
                    showToastDialog("手机号必须都为数字");
                }
                break;
            case R.id.delete_Login:
                et1.setText("");
                break;
            case R.id.delete1_Login:
                et2.setText("");
                break;
            case R.id.delete2_Login:
                et3.setText("");
                break;
            case R.id.delete3_Login:
                et4.setText("");
                break;
        }
    }

    public void etClick() {
        et1.setFocusable(true);
        et2.setFocusable(true);
        et3.setFocusable(true);
        et4.setFocusable(true);
        et1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv1.setVisibility(View.GONE);
                } else {
                    iv1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et1.getText().toString().length();
                if (length > 11) {
                    int editEnd = et1.getSelectionEnd();
                    arg0.delete(length - 1, editEnd);
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv2.setVisibility(View.GONE);
                } else {
                    iv2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                int length = et2.getText().toString().length();
                if (length > 4) {
                    int editEnd = et2.getSelectionEnd();
                    arg0.delete(length - 1, editEnd);
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv3.setVisibility(View.GONE);
                } else {
                    iv3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et3.getText().toString().length();
                if ((length > 64) || !reGexPwd.judge(et3.getText().toString())) {
                    int editEnd = et3.getSelectionEnd();
                    if (length > 0) {
                        arg0.delete(length - 1, editEnd);
                        ToastUtil.showMessage("密码只能" + reGexPwd.getRule());
                    }
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv4.setVisibility(View.GONE);
                } else {
                    iv4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et4.getText().toString().length();
                if ((length > 64) || !reGexPwd.judge(et4.getText().toString())) {
                    int editEnd = et4.getSelectionEnd();
                    if (length > 0) {
                        arg0.delete(length - 1, editEnd);
                        ToastUtil.showMessage("密码只能" + reGexPwd.getRule());
                    }
                }
            }
        });

    }

    @Override
    public void setInputEnabled(boolean enabled) {
        // TODO Auto-generated method stub
        super.setInputEnabled(enabled);
        if (enabled) {
            progressGenerator.stop();
            getCode_btn.setEnabled(true);
            btnChangePassword.setEnabled(true);
            et1.setEnabled(true);
            et2.setEnabled(true);
            et3.setEnabled(true);
            et4.setEnabled(true);
        } else {
            getCode_btn.setEnabled(false);
            btnChangePassword.setEnabled(false);
            et1.setEnabled(false);
            et2.setEnabled(false);
            et3.setEnabled(false);
            et4.setEnabled(false);
        }
    }

    Runnable runnableRegister = new Runnable() {

        @Override
        public void run() {
            String nubeNumber = "";
//			String url="http://192.168.1.106:8080/serviceplatform//base/app/"+interface_name;
            String url = AbsParam.getBaseUrl() + interface_name;
            if (interface_name.equals("/base/app/findapppsd")) {
                if (password1.equals(password2)) {
                    //先注册获取视频号码
//					nubeNumber = MakeCall.register(Register.this, telephonenum);
                    if (!isNumeric(nubeNumber)) {
                        nubeNumber = "0";
                    }
                    map = new HashMap<String, String>();
                    map = new HashMap<String, String>();
                    map.put("oldusername", telephonenum);
                    map.put("newpassword1", password1);
                    map.put("newpassword", password2);
                    map.put("persontype", "0");
                    map.put("id", "0");
                    map.put("code", identifyingCode);

                } else {
                    msg = new Message();
                    msg.what = PSWnoMatch;
                    RegisterHandler.sendMessage(msg);
                }
            }
            Log.i("input", url + ">>>>>>>>><<<<<<<<" + map.toString() + nubeNumber);
            try {
                retutrnStr = NetTool.sendHttpClientPost(url, map, "utf-8");

                Log.i("datas", retutrnStr);
                if (interface_name.equals("/base/app/getidentifycode")) {
                    JSONObject getCodeResult = new JSONObject(retutrnStr);
                    CodeResultBack = getCodeResult.getInt("result");
//					CodeDetialBack=getCodeResult.getString("detail");
                    msg = new Message();
                    if (CodeResultBack == 0) {
                        msg.what = NoCode;
                    } else if (CodeResultBack == 1) {
                        msg.what = GetCode;
                    }
                    RegisterHandler.sendMessage(msg);
                } else if (interface_name.equals("/base/app/findapppsd")) {
                    JSONObject RegisterResult = new JSONObject(retutrnStr);
                    RegisterResultBack = RegisterResult.getInt("result");
                    RegisterDetailBack = RegisterResult.getString("detail");
                    msg = new Message();
                    if (RegisterResultBack == 0) {
                        msg.what = RESTPASSWORDFAIL;
                    } else if (RegisterResultBack == 1) {
                        msg.what = RESTPASSWORDSUCCESS;
                    } else if (RegisterResultBack == -1) {
                        msg.what = IDNTIFINGCODEFAIL;
                    } else if (RegisterResultBack == 2) {
                        msg.what = NOTUSER;
                    }
                    RegisterHandler.sendMessage(msg);
                }
                ;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };
    private Handler RegisterHandler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case NoCode:
                    setInputEnabled(true);
                    ToastUtil.showMessage("没有获得验证码");
                    break;
                case IDNTIFINGCODEFAIL:
                    setInputEnabled(true);
                    ToastUtil.showMessage("验证码错误");
                    break;
                case NOTUSER:
                    setInputEnabled(true);
                    ToastUtil.showMessage("没有该用户");
                    //			time = new TimeCount(60000, 1000);
                    break;
                case GetCode:
                    hideProgressBar();

                    break;
                case RESTPASSWORDFAIL:
                    ToastUtil.showMessage(RegisterDetailBack);
                    setInputEnabled(true);
                    break;
                case RESTPASSWORDSUCCESS:
//				Toast.makeText(getApplicationContext(), "注册成功", 1).show();
//				startActivity(FillInformation.class);
//				MakeCall.register(getApplicationContext(), telephonenum);
//				String old=Setting.class.getName();
//				backTo(old);
//				startActivity(MyAccountCenter.class);
//				Intent intent = new Intent(Register.this, MainActivityFragme.class);
//				intent.putExtra(Constant.USER_ID, userID+"");
//				intent.putExtra(Constant.USER_ACCOUNT, mIdString);
//                startActivity(intent);
                    ToastUtil.showMessage("修改密码" + RegisterDetailBack);
                    startActivity(ActivityLogin.class);
//				macAddress=getLocalMacAddress();
//				AscyncLogin async = new AscyncLogin(ActivityResetPassword.this,telephonenum,password1,macAddress);
//				async.execute();
                    break;
                case PSWnoMatch:
                    setInputEnabled(true);
                    ToastUtil.showMessage("您输入的密码不一致，请重新输入");
                    et4.setText("");
                    break;
                default:
                    setInputEnabled(true);
                    break;
            }
        }
    };

    public String getLocalMacAddress() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");

        if (result == null) {
            return "网络出错，请检查网络";
        }
        if (result.length() > 0 && result.contains("HWaddr")) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.toLowerCase();
            }
        }
        return result.trim();
    }

    private String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                // result += line;
                Log.i("test", "line: " + line);
            }

            result = line;
            Log.i("test", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            getCode_btn.setText("重新发送");
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.blue_second));
            getCode_btn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            getCode_btn.setEnabled(false);
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_concrete));
            getCode_btn.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    private boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
