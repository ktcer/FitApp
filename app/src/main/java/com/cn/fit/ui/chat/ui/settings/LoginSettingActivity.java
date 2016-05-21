package com.cn.fit.ui.chat.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.patient.setting.ActivitySettings;

public class LoginSettingActivity extends ECSuperActivity implements View.OnClickListener {

    private SettingItem mSettingServerIp;
    private SettingItem mSettingAppkey;
    private SettingItem mSettingToken;

    private final class OnConfigClickListener implements View.OnClickListener {

        private int type;

        public OnConfigClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginSettingActivity.this, EditConfigureActivity.class);
            intent.putExtra("setting_type", type);
            startActivityForResult(intent, 0xa);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, R.string.app_server_view, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_setting;
    }

    @Override
    public void onResume() {
        super.onResume();
        initConfigValue();
    }

    /**
     * 加载页面布局
     */
    private void initView() {

        mSettingServerIp = (SettingItem) findViewById(R.id.settings_serverIP);
        mSettingAppkey = (SettingItem) findViewById(R.id.settings_appkey);
        mSettingToken = (SettingItem) findViewById(R.id.settings_token);

        mSettingServerIp.setOnClickListener(new OnConfigClickListener(ActivitySettings.CONFIG_TYPE_SERVERIP));
        mSettingAppkey.setOnClickListener(new OnConfigClickListener(ActivitySettings.CONFIG_TYPE_APPKEY));
        mSettingToken.setOnClickListener(new OnConfigClickListener(ActivitySettings.CONFIG_TYPE_TOKEN));
        initConfigValue();
    }

    private void initConfigValue() {
        // mSettingServerIp.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_SERVERIP));
        mSettingAppkey.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_APPKEY));
        mSettingToken.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_TOKEN));
    }


    private String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId(), (String) settings.getDefaultValue());
        return value;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;

            default:
                break;
        }
    }
}
