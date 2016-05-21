package com.cn.fit.ui.chat.ui.settings;

import java.io.InvalidClassException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.base.CCPFormInputView;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DateUtil;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.core.ClientUser;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.chat.ui.chatting.IMChattingHelper;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.PersonInfo;
import com.yuntongxun.ecsdk.SdkErrorCode;

public class SettingPersionInfoActivity extends ECSuperActivity implements View.OnClickListener {

    private static final int TIME_SETTINGS_DIALOG = 1;

    private EditText nicknameEt;
    private RadioGroup mSexRg;
    private TextView mBirthTv;
    private Button mSignbtn;

    private int mExpirationTimeStartYear;
    private int mExpirationTimeStartMonth;
    private int mExpirationTimeStartDay;
    private ECProgressDialog mPostingdialog;
    private ClientUser clientUser;
    private boolean mFromRegist = false;
    private final DatePickerDialog.OnDateSetListener mDateSetListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mExpirationTimeStartYear = year;
            mExpirationTimeStartMonth = monthOfYear;
            mExpirationTimeStartDay = dayOfMonth;
            initExpirationTime();
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted,
                R.drawable.btn_style_green, null,
                getString(R.string.dialog_ok_skip),
                getString(R.string.app_title_setting_persioninfo), null, this);

        mFromRegist = getIntent().getBooleanExtra("from_regist", false);

        initView();

        if (mFromRegist) {
            getTopBarView().setTopBarToStatus(1, -1, -1, R.string.app_title_setting_persioninfo, this);
        } else {
            getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted,
                    -1, null,
                    null,
                    getString(R.string.app_title_setting_persioninfo), null, this);
        }
    }

    private void initView() {
        CCPFormInputView formInputView = (CCPFormInputView) findViewById(R.id.nickname_tv);
        nicknameEt = formInputView.getFormInputEditView();
        mSexRg = (RadioGroup) findViewById(R.id.sex_rg);
        mSignbtn = (Button) findViewById(R.id.sign_in_button);

        mSignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                handlePersionInfo();
            }
        });


        clientUser = CCPAppManager.getClientUser();

        if (clientUser == null) {
            return;
        }
        initExpirationTimeView();
        nicknameEt.setText(clientUser.getUserName());
        nicknameEt.setSelection(nicknameEt.getText().length());

        if (clientUser.getSex() == 2) {
            ((RadioButton) mSexRg.getChildAt(0)).setChecked(false);
            ((RadioButton) mSexRg.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) mSexRg.getChildAt(0)).setChecked(true);
            ((RadioButton) mSexRg.getChildAt(1)).setChecked(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mFromRegist) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        clientUser = CCPAppManager.getClientUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (clientUser != null) {
            saveClientUser();
        }
    }

    private void saveClientUser() {
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, clientUser.toString(), true);
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    private void handlePersionInfo() {
        if (SDKCoreHelper.getECChatManager() == null) {
            return;
        }

        String nickname = nicknameEt.getText().toString().trim();
        int checkedRadioButtonId = mSexRg.getCheckedRadioButtonId();
        PersonInfo.Sex sex = PersonInfo.Sex.MALE;
        if (checkedRadioButtonId == R.id.female) {
            sex = PersonInfo.Sex.FEMALE;
        }

        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showMessage("请设置用户昵称");
            return;
        }
        mPostingdialog = new ECProgressDialog(this, R.string.login_posting_submit);
        mPostingdialog.show();
        final PersonInfo.Sex clientSex = sex;
        String birth = mBirthTv.getText().toString();
        SDKCoreHelper.getECChatManager().setPersonInfo(nickname, sex, birth, new ECChatManager.OnSetPersonInfoListener() {
            @Override
            public void onSetPersonInfoComplete(ECError e, int version) {
                IMChattingHelper.getInstance().mServicePersonVersion = version;
                dismissPostingDialog();
                if (SdkErrorCode.REQUEST_SUCCESS == e.errorCode) {
                    try {
                        ClientUser clientUser = CCPAppManager.getClientUser();
                        if (clientUser != null) {
                            clientUser.setUserName(nicknameEt.getText().toString());
//                            clientUser.setSex(clientSex);
                            clientUser.setBirth(getActiveTimelong());
                            clientUser.setpVersion(version);
                            CCPAppManager.setClientUser(clientUser);
                            ECContacts contacts = new ECContacts();
                            contacts.setClientUser(clientUser);
                            ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, clientUser.toString(), true);
                            ContactSqlManager.insertContact(contacts, clientUser.getSex());
                        }

                        setResult(RESULT_OK);
                        finish();
                    } catch (InvalidClassException e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                ToastUtil.showMessage("设置失败,请稍后重试");
            }
        });


/*        // 获得SDK消息管理类
        ECChatManager manager = ECDevice.getECChatManager();
        // 调用查询个人信息接口，设置结果回调
        manager.getPersonInfo(new ECChatManager.OnGetPersonInfoListener() {
            @Override
            public void onGetPersonInfoComplete(ECError e, PersonInfo p) {
                if ("000000".equals(e.errorCode)) {
                    // 个人信息获取成功
                    // 更新个人信息到本地数据库，通知UI刷新
                    return;
                }
                Log.e("Aihu", "get person info fail  " +
                        ", errorCode=" + e.errorCode);
            }
        });*/

/*        // 设置昵称
        String nickName ="$Tony";
        // 设置性别（男：1 ， 女：2）
        int sex = 1;
        // 设置生日（格式：2015-5-13）;
        String birth = "2015-5-13";
        // 获得SDK消息管理类
        ECChatManager manager = ECDevice.getECChatManager();
        // 调用设置个人信息接口，设置结果回调
        manager.setPersonInfo(nickName, sex, birth
                , new ECChatManager.OnSetPersonInfoListener() {
            @Override
            public void onSetPersonInfoComplete(ECError e, int version) {
                if ("000000".equals(e.errorCode)) {
                    // 个人信息设置成功，version最新的个人信息版本号
                    // 更新个人信息到本地数据库，通知UI刷新
                    return;
                }
                Log.e("Aihu", "set person info fail  " +
                        ", errorCode=" + e.errorCode);
            }
        });*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_SETTINGS_DIALOG:
                return new DatePickerDialog(SettingPersionInfoActivity.this, mDateSetListener, mExpirationTimeStartYear, mExpirationTimeStartMonth, mExpirationTimeStartDay);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case TIME_SETTINGS_DIALOG:
                ((DatePickerDialog) dialog).updateDate(mExpirationTimeStartYear, mExpirationTimeStartMonth, mExpirationTimeStartDay);
            default:
                break;
        }
        super.onPrepareDialog(id, dialog);
    }

    /**
     *
     */
    private void initExpirationTimeView() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 获取当前日期Date对象
        Date date = new Date();
        if (clientUser != null && clientUser.getBirth() != 0) {
            date.setTime(clientUser.getBirth());
        }
        //为Calendar对象设置时间为当前日期
        calendar.setTime(date);
        // 获取Calendar对象中的年
        mExpirationTimeStartYear = calendar.get(Calendar.YEAR);
        // 获取Calendar对象中的月
        mExpirationTimeStartMonth = calendar.get(Calendar.MONTH);
        //获取这个月的第几天
        mExpirationTimeStartDay = calendar.get(Calendar.DATE);

        mBirthTv = (TextView) findViewById(R.id.birth_tv);
        mBirthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_SETTINGS_DIALOG);
            }
        });

        initExpirationTime();

    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if (mPostingdialog == null || !mPostingdialog.isShowing()) {
            return;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }

    private void initExpirationTime() {
        mBirthTv.setText(getActiveTimeString(mExpirationTimeStartYear, mExpirationTimeStartMonth, mExpirationTimeStartDay));
    }

    public static String getActiveTimeString(int year, int month, int day) {
        return DateUtil.formatDate(year, month, day);
    }

    public long getActiveTimelong() {
        return DateUtil.getDateMills(mExpirationTimeStartYear, mExpirationTimeStartMonth, mExpirationTimeStartDay);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_persion_info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
            case R.id.text_right:
                hideSoftKeyboard();
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
    }
}
