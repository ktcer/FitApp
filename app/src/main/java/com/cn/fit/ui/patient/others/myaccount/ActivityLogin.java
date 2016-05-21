package com.cn.fit.ui.patient.others.myaccount;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.patient.setting.User;
import com.cn.fit.util.RegexPwd;
import com.cn.fit.util.UtilsLogin;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.progressbutton.iml.ActionProcessButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ActivityLogin extends ECSuperActivity implements OnClickListener,
        OnItemClickListener, OnDismissListener {
    protected static final String TAG = "Register";
    private LinearLayout mLoginLinearLayout; // 登录内容的容器
    private LinearLayout mUserIdLinearLayout; // 将下拉弹出窗口在此容器下方显示
    private Animation mTranslate; // 位移动画
    //	private Dialog mLoginingDlg; // 显示正在登录的Dialog
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private ImageView mMoreUser; // 下拉图标
    private ActionProcessButton mLoginButton; // 登录按钮
    //	private ImageView mLoginMoreUserView; // 弹出下拉弹出窗的按钮
    private String mIdString;
    private String mPwdString;
    private ArrayList<User> mUsers; // 用户列表
    private ListView mUserIdListView; // 下拉弹出窗显示的ListView对象
    private MyAapter mAdapter; // ListView的监听器
    private PopupWindow mPop; // 下拉弹出窗
    private TextView registerButton, titleText, logintxtForgotPwd;
    private UtilsSharedData sharedData;
    private Map<String, String> map;
    private RegexPwd reGexPwd;
    private ProgressGenerator progressGenerator;
    // /**后台返回的数据*/
    // private String retString;
    // /**登录失败*/
    // private static final int Fail_Login=0;
    // /**登录成功*/
    // private static final int Success_Login=1;
    // /**用户类型，0表示患者*/
    // private static final String userType="0";
    /**
     * mac地址
     */
    private static String macAddress;
    private String ActivityTag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reGexPwd = new RegexPwd();
        initView();
        setListener();
        mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动

		/* 获取已经保存好的用户密码 */
        mUsers = UtilsLogin.getUserList(ActivityLogin.this);

        if (mUsers.size() > 0) {
            /* 将列表中的第一个user显示在编辑框 */
            mIdEditText.setText(mUsers.get(0).getId());
            mPwdEditText.setText(mUsers.get(0).getPwd());
        }

        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.userifo_listview, null);
        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        parent.removeView(mUserIdListView); // 必须脱离父子关系,不然会报错
        mUserIdListView.setOnItemClickListener(this); // 设置点击事
        mAdapter = new MyAapter(mUsers);
        mUserIdListView.setAdapter(mAdapter);
        Intent intent = getIntent();
        ActivityTag = intent.getStringExtra("ActivityTag");
        if (ActivityTag == null) {
            ActivityTag = "";
        }
        registerReceiver(new String[]{SDKCoreHelper.ACTION_SDK_CONNECT});
    }

    /* ListView的适配器 */
    class MyAapter extends ArrayAdapter<User> {

        public MyAapter(ArrayList<User> users) {
            super(ActivityLogin.this, 0, users);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
            }

            TextView userIdText = (TextView) convertView
                    .findViewById(R.id.listview_userid);
            userIdText.setText(getItem(position).getId());

            ImageView deleteUser = (ImageView) convertView
                    .findViewById(R.id.login_delete_user);
            deleteUser.setOnClickListener(new OnClickListener() {
                // 点击删除deleteUser时,在mUsers中删除选中的元素
                @Override
                public void onClick(View v) {

                    if (getItem(position).getId().equals(mIdString)) {
                        // 如果要删除的用户Id和Id编辑框当前值相等，则清空
                        mIdString = "";
                        mPwdString = "";
                        mIdEditText.setText(mIdString);
                        mPwdEditText.setText(mPwdString);
                    }
                    mUsers.remove(getItem(position));
                    mAdapter.notifyDataSetChanged(); // 更新ListView
                }
            });
            return convertView;
        }

    }

    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

                int length = mIdEditText.getText().toString().length();
                if (length > 11) {
                    int editEnd = mIdEditText.getSelectionEnd();
                    s.delete(length - 1, editEnd);
                } else if (length < 11) {
                    mPwdEditText.setText("");
                } else {
                    for (User user : mUsers) {
                        if (user.getId().equals(s.toString())) {
                            mPwdString = user.getPwd();
                            mPwdEditText.setText(mPwdString);
                        }
                    }
                }
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                int length = mPwdEditText.getText().toString().length();
                if ((length > 64)
                        || !reGexPwd.judge(mPwdEditText.getText().toString())) {
                    int editEnd = mPwdEditText.getSelectionEnd();
                    if (length > 0) {
                        s.delete(length - 1, editEnd);
                        ToastUtil.showMessage("密码只能" + reGexPwd.getRule());
                    }
                }
            }
        });
        mLoginButton.setOnClickListener(this);
        mMoreUser.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void initView() {
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mMoreUser = (ImageView) findViewById(R.id.login_more_user);
        mLoginButton = (ActionProcessButton) findViewById(R.id.login_btnLogin);
        mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator = new ProgressGenerator(mLoginButton);
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("登录");
        registerButton = (TextView) findViewById(R.id.right_tv);
        logintxtForgotPwd = (TextView) findViewById(R.id.login_txtForgotPwd);
        logintxtForgotPwd.setOnClickListener(this);
        registerButton.setVisibility(View.VISIBLE);
        registerButton.setText("注册");
        findViewById(R.id.left_Btn).setVisibility(View.INVISIBLE);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
//		initLoginingDlg();
        sharedData = new UtilsSharedData();
        sharedData.initDataShare(ActivityLogin.this);
    }

    public void initPop() {
        int width = mUserIdLinearLayout.getWidth() - 4;
        int height = LayoutParams.WRAP_CONTENT;
        mPop = new PopupWindow(mUserIdListView, width, height, true);
        mPop.setOnDismissListener(this);// 设置弹出窗口消失时监听器

        // 注意要加这句代码，点击弹出窗口其它区域才会让窗口消失
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
    }

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
            mLoginButton.setEnabled(true);
            mIdEditText.setEnabled(true);
            mPwdEditText.setEnabled(true);
        } else {
            mLoginButton.setEnabled(false);
            mIdEditText.setEnabled(false);
            mPwdEditText.setEnabled(false);
        }
    }
//	/* 初始化正在登录对话框 */
//	private void initLoginingDlg() {
//
//		mLoginingDlg = new Dialog(this, R.style.loginingDlg);
//		mLoginingDlg.setContentView(R.layout.logining_dlg);
//
//		Window window = mLoginingDlg.getWindow();
//		WindowManager.LayoutParams params = window.getAttributes();
//		// 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置
//
//		// 获取屏幕的高宽
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int cxScreen = dm.widthPixels;
//		int cyScreen = dm.heightPixels;
//
//		int height = (int) getResources().getDimension(
//				R.dimen.loginingdlg_height);// 高42dp
//		int lrMargin = (int) getResources().getDimension(
//				R.dimen.loginingdlg_lr_margin); // 左右边沿10dp
//		int topMargin = (int) getResources().getDimension(
//				R.dimen.loginingdlg_top_margin); // 上沿20dp
//
//		params.y = (-(cyScreen - height) / 2) + topMargin; // -199
//		/* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */
//
//		params.width = cxScreen;
//		params.height = height;
//		// width,height表示mLoginingDlg的实际大小
//
//		mLoginingDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
//	}
//
//	/* 显示正在登录对话框 */
//	private void showLoginingDlg() {
//		if (mLoginingDlg != null)
//			mLoginingDlg.show();
//	}
//
//	/* 关闭正在登录对话框 */
//	private void closeLoginingDlg() {
//		if (mLoginingDlg != null && mLoginingDlg.isShowing())
//			mLoginingDlg.dismiss();
//	}

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_btnLogin:
                // 启动登录
                // showLoginingDlg(); //
                // 显示"正在登录"对话框,因为此Demo没有登录到web服务器,所以效果可能看不出.可以结合情况使用
                Log.i(TAG, mIdString + "  " + mPwdString);
                if (mIdString == null || mIdString.equals("")
                        || (mIdString.length() != 11)) { // 账号为空时
                    Toast.makeText(ActivityLogin.this, "用户名格式不正确",
                            Toast.LENGTH_SHORT).show();
                } else if (mPwdString == null || mPwdString.equals("")
                        || (!reGexPwd.judge(mPwdString))) {// 密码为空时
                    Toast.makeText(ActivityLogin.this, "密码格式不正确",
                            Toast.LENGTH_SHORT).show();
                } else if (mPwdString.length() < 6 || mPwdString.length() > 64) {
                    Toast.makeText(ActivityLogin.this, "密码长度必须在6-64个字符之间", Toast.LENGTH_SHORT).show();
                } else {// 账号和密码都不为空时
                    boolean mIsSave = true;
                    try {
                        Log.i(TAG, "保存用户列表");
                        // for (User user : mUsers) { // 判断本地文档是否有此ID用户
                        // if (user.getId().equals(mIdString)) {
                        // mIsSave = false;
                        // break;
                        // }
                        // }
                        for (int i = 0; i < mUsers.size(); i++) {
                            if (mUsers.get(i).getId().equals(mIdString)) {
                                mIsSave = false;
                                // 重新将密码植入
                                mUsers.get(i).setPwd(mPwdString);
                                break;
                            }
                        }
                        if (mIsSave) { // 将新用户加入users
                            User user = new User(mIdString, mPwdString, mUsers.size() + 1);
                            mUsers.add(user);
                            Collections.sort(mUsers);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//				closeLoginingDlg();// 关闭对话框
                    macAddress = getLocalMacAddress();
                    // Log.i("MacAddress",macAddress);
//				showProgressBar();
                    progressGenerator.start();
                    setInputEnabled(false);
                    AscyncLogin async = new AscyncLogin(this, mIdString,
                            mPwdString, macAddress);
                    async.execute();
                }
                break;
            case R.id.login_more_user: // 当点击下拉栏
                if (mPop == null) {
                    initPop();
                }
                if (!mPop.isShowing() && mUsers.size() > 0) {
                    // Log.i(TAG, "切换为角向上图标");
                    mMoreUser.setImageResource(R.drawable.login_more_down); // 切换图标
                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
                }
                break;
            case R.id.right_tv:
                startActivity(ActivityRegister.class);
                break;
            case R.id.login_txtForgotPwd:
                startActivity(ActivityResetPassword.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mIdEditText.setText(mUsers.get(position).getId());
        mPwdEditText.setText(mUsers.get(position).getPwd());
        List<User> tempUserList = new ArrayList<User>();
        tempUserList.add(mUsers.get(position));
        for (User user : mUsers) {
            if (!user.getId().equals(mUsers.get(position).getId())) {
                tempUserList.add(user);
            }
        }
        mUsers.clear();
        int index = 0;
        for (User user : tempUserList) {
            user.setAddOrder(index);
            mUsers.add(user);
            index++;
        }
        mPop.dismiss();
    }

    /* PopupWindow对象dismiss时的事件 */
    @Override
    public void onDismiss() {
        // Log.i(TAG, "切换为角向下图标");
        mMoreUser.setImageResource(R.drawable.login_more_up);
    }

    /* 退出此Activity时保存users */
    @Override
    public void onPause() {
        super.onPause();
        try {
            UtilsLogin.saveUserList(ActivityLogin.this, mUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取设备的mac地址
     */
    public String getLocalMacAddress() {
        // WifiManager wifi = (WifiManager)
        // getSystemService(Context.WIFI_SERVICE);
        // WifiInfo info = wifi.getConnectionInfo();
        // return info.getMacAddress();
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

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_login;
    }
}
