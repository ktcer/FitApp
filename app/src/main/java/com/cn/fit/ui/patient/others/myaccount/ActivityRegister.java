package com.cn.fit.ui.patient.others.myaccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.patient.setting.User;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.FButton;
import com.cn.fit.util.Regex;
import com.cn.fit.util.RegexNumber;
import com.cn.fit.util.RegexPwd;
import com.cn.fit.util.SmsContent;
import com.cn.fit.util.UtilsLogin;
import com.cn.fit.util.progressbutton.iml.ActionProcessButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("ucd")
public class ActivityRegister extends ECSuperActivity implements
        OnClickListener {
    private static String macAddress;
    private TextView titleText;
    private EditText et1, et2, et3, et4;// ,et5;
    private FButton getCode_btn;
    private ActionProcessButton register_btn;
    private ImageView iv1, iv2, ivVisible, ivInVisible, iv4;// ,iv5,iv6;
    /**
     * 获取验证码接口
     */
    private String interface_name;
    /**
     * 注册使用的电话号码
     */
    private String telephonenum;
    /**
     * 注册时获取的验证码
     */
    private String identifyingCode;
//    /**
//     * 注册时获取的邀请码
//     */
//    private String inviteCode;
    /**
     * 输入的密码
     */
    private String password1;
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
    private static final int FailToRegister = 2;
    private static final int SuccessToResgister = 3;
    /**
     * 用户类型，0表示患者
     */
    private static final String userType = "0";
    private ProgressGenerator progressGenerator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initial();
        /**
         * 注册云通讯登录回调
         */
        registerReceiver(new String[]{SDKCoreHelper.ACTION_SDK_CONNECT});
    }

    private void initial() {
        reGexPhone = new RegexNumber();
        reGexPwd = new RegexPwd();
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("注册");
        iv1 = (ImageView) findViewById(R.id.delete_PhoneNumber);
        iv2 = (ImageView) findViewById(R.id.delete_identifyCode);
        ivVisible = (ImageView) findViewById(R.id.PasswordVisible);
        ivInVisible = (ImageView) findViewById(R.id.PasswordInVisible);
        iv4 = (ImageView) findViewById(R.id.delete_InvitationCode);
        et1 = (EditText) findViewById(R.id.phone_Register);
        et2 = (EditText) findViewById(R.id.code_Register);
        et3 = (EditText) findViewById(R.id.password_Register);
        et4 = (EditText) findViewById(R.id.invitationCode_Register);
        et4.setVisibility(View.INVISIBLE);
        etClick();
        getCode_btn = (FButton) findViewById(R.id.getCode_Register);
        getCode_btn.setOnClickListener(this);
        register_btn = (ActionProcessButton) findViewById(R.id.submit_Register);
        register_btn.setOnClickListener(this);
        getCode_btn.setCornerRadius(3);
        register_btn.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator = new ProgressGenerator(register_btn);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        ivVisible.setOnClickListener(this);
        ivInVisible.setOnClickListener(this);
        iv4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.submit_Register:
                /** 提交注册信息 */
                interface_name = "regist";
                telephonenum = et1.getText().toString();
                identifyingCode = et2.getText().toString();
                password1 = et3.getText().toString();
//                inviteCode = et4.getText().toString();
                if (!telephonenum.equals("") & !password1.equals("")) {
                    if (password1.length() < 6 || password1.length() > 64) {
                        ToastUtil.showMessage("密码长度必须在6-64个字符之间");
                    } else {
                        if (reGexPwd.judge(password1)) {
                            if (telephonenum.length() == 11) {
//                                if ((inviteCode.length() != 0)
//                                        && (inviteCode.length() != 11)) {
//                                    // 已经输入了邀请码，但是邀请码格式不正确
//                                    ToastUtil.showMessage("邀请码必须为11位手机号码");
//                                } else {
                                progressGenerator.start();
                                setInputEnabled(false);
                                new Thread(runnableRegister).start();
//                                }
                            } else {
                                ToastUtil.showMessage("手机号长度只能为11位数字");
                            }
                        } else {
                            ToastUtil.showMessage("密码只能" + reGexPwd.getRule());
                        }
                    }

                } else {
                    ToastUtil.showMessage("请填写所有信息");
                }
                break;
            case R.id.getCode_Register:
                telephonenum = et1.getText().toString();
                if (reGexPhone.judge(telephonenum)) {
                    if (telephonenum.length() == 11) {
                        AsyncJudgeNumberExsits mTask = new AsyncJudgeNumberExsits(
                                telephonenum) {

                            @Override
                            protected void onPostExecute(String result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result.equals("0")) {
                                    ToastUtil.showMessage("该手机号已存在");
                                } else if (result.equals("1")) {
                                    time = new TimeCount(60000, 1000);
                                    time.start();
                                    interface_name = "getidentifycode";
                                    map = new HashMap<String, String>();
                                    map.put("telephoneNum", telephonenum);
                                    getCode_btn.setEnabled(false);
                                    getCode_btn
                                            .setButtonColor(R.color.fbutton_color_concrete);
                                    new Thread(runnableRegister).start();
                                    // 注册短信监听
                                    SmsContent content = new SmsContent(
                                            ActivityRegister.this, new Handler(),
                                            et2);
                                    // 注册短信变化监听
                                    ActivityRegister.this.getContentResolver()
                                            .registerContentObserver(
                                                    content.SMS_URI_INBOX, true,
                                                    content);
                                }
                            }

                        };
                        mTask.execute();

                    } else {
                        ToastUtil.showMessage("手机号长度不对");
                    }
                } else {
                    ToastUtil.showMessage("手机号必须都为数字");
                }
                break;
            case R.id.delete_PhoneNumber:
                et1.setText("");
                break;
            case R.id.delete_identifyCode:
                et2.setText("");
                break;
            case R.id.delete_InvitationCode:
                et4.setText("");
                break;
            case R.id.PasswordVisible:
                ivVisible.setVisibility(View.INVISIBLE);
                ivInVisible.setVisibility(View.VISIBLE);
                // 文本以密码形式显示
                et3.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.PasswordInVisible:
                et3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivVisible.setVisibility(View.VISIBLE);
                ivInVisible.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void etClick() {
        et1.setFocusable(true);
        et2.setFocusable(true);
        et3.setFocusable(true);

        et1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv1.setVisibility(View.GONE);
                } else {
                    iv1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
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
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv2.setVisibility(View.GONE);
                } else {
                    iv2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
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
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
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
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv4.setVisibility(View.GONE);
                } else {
                    iv4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et4.getText().toString().length();
                if (length > 11) {
                    int editEnd = et4.getSelectionEnd();
                    arg0.delete(length - 1, editEnd);
                }

            }
        });

    }

    Runnable runnableRegister = new Runnable() {

        @Override
        public void run() {
            String url = AbsParam.getBaseUrl() + "/base/app/" + interface_name;
            if (interface_name.equals("regist")) {
                map = new HashMap<String, String>();
                map.put("telephoneNum", telephonenum);
                map.put("password", password1);
                map.put("identifyingCode", identifyingCode);
                map.put("userType", userType);
            }
            try {
                retutrnStr = NetTool.sendHttpClientPost(url, map, "utf-8");
                Log.i("input", retutrnStr);
                if (interface_name.equals("getidentifycode")) {
                    JSONObject getCodeResult = new JSONObject(retutrnStr);
                    CodeResultBack = getCodeResult.getInt("result");
                    CodeDetialBack = getCodeResult.getString("detail");
                    msg = new Message();
                    if (CodeResultBack == 0) {
                        msg.what = NoCode;
                    } else if (CodeResultBack == 1) {
                        msg.what = GetCode;
                    }
                    RegisterHandler.sendMessage(msg);
                } else if (interface_name.equals("regist")) {
                    JSONObject RegisterResult = new JSONObject(retutrnStr);
                    RegisterResultBack = RegisterResult.getInt("result");
                    RegisterDetailBack = RegisterResult.getString("detail");
                    msg = new Message();
                    if (RegisterResultBack == 0) {
                        msg.what = FailToRegister;
                    }
                    if (RegisterResultBack == 1) {
                        msg.what = SuccessToResgister;
                    }
                    RegisterHandler.sendMessage(msg);
                }
                ;
            } catch (Exception e) {
                msg = new Message();
                msg.what = FailToRegister;
                RegisterHandler.sendMessage(msg);
                Log.i("datas", "异常" + e.toString());
            }
        }
    };

    /**
     * 设置界面是否使能输入
     *
     * @param enabled
     */

    @Override
    public void setInputEnabled(boolean enabled) {
        // TODO Auto-generated method stub
        super.setInputEnabled(enabled);
        if (enabled) {
            progressGenerator.stop();
            getCode_btn.setEnabled(true);
            register_btn.setEnabled(true);
            et1.setEnabled(true);
            et2.setEnabled(true);
            et3.setEnabled(true);
        } else {
            getCode_btn.setEnabled(false);
            register_btn.setEnabled(false);
            et1.setEnabled(false);
            et2.setEnabled(false);
            et3.setEnabled(false);
        }
    }

    private Handler RegisterHandler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case NoCode:
                    ToastUtil.showMessage("没有获得验证码");
                    break;
                case GetCode:

                    break;
                case FailToRegister:
                    ToastUtil.showMessage("后台返回失败，请重新试一次" + RegisterDetailBack);
                    setInputEnabled(true);
                    break;
                case SuccessToResgister:
                    ToastUtil.showMessage(RegisterDetailBack);
                    macAddress = getLocalMacAddress();
                    List<User> userList = UtilsLogin
                            .getUserList(ActivityRegister.this);
                    User newUser = new User(telephonenum, password1, 0);
                    ArrayList<User> tempList = new ArrayList<User>();
                    tempList.add(newUser);
                    int i = 1;
                    for (User user : userList) {
                        user.setAddOrder(i);
                        tempList.add(user);
                        i++;
                    }
                    try {
                        UtilsLogin.saveUserList(ActivityRegister.this, tempList);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    AscyncLogin async = new AscyncLogin(ActivityRegister.this,
                            telephonenum, password1, macAddress);
                    async.execute();
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
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCode_btn.setText("重新发送");
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.blue_second));
            getCode_btn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getCode_btn.setEnabled(false);
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_concrete));
            getCode_btn.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_register;
    }

}
