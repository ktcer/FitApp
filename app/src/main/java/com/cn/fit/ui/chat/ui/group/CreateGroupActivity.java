/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.group;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.dialog.ECListDialog;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.SDKCoreHelper;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.contact.MobileContactSelectActivity;
import com.cn.fit.util.FButton;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;

/**
 * 群组创建功能
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-27
 */
public class CreateGroupActivity extends ECSuperActivity implements
        View.OnClickListener, ECGroupManager.OnCreateGroupListener, GroupMemberService.OnSynsGroupMemberListener {

    private static final String TAG = "ECDemo.CreateGroupActivity";
    String[] stringArray = null;
    /**
     * 群组名称
     */
    private EditText mNameEdit;
    /**
     * 群组公告
     */
    private EditText mNoticeEdit;
    /**
     * 创建按钮
     */
    private FButton mCreateBtn;
    /**
     * 创建的群组
     */
    private ECGroup group;
    private ECProgressDialog mPostingdialog;
    private Spinner mPermissionSpinner;
    private FButton mSetPermission;
    private int mPermissionModel;

    final private TextWatcher textWatcher = new TextWatcher() {

        private int fliteCounts = 20;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtil.d(LogUtil.getLogUtilsTag(textWatcher.getClass()), "fliteCounts=" + fliteCounts);
            fliteCounts = fliteCounts(s);
            if (fliteCounts < 0) {
                fliteCounts = 0;
            }
            if (checkNameEmpty()) {
                mCreateBtn.setEnabled(true);
                mCreateBtn.setButtonColor(getResources().getColor(
                        R.color.blue_second));
                return;
            }
            mCreateBtn.setEnabled(false);
            mCreateBtn.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_concrete));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stringArray = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.new_group;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringArray = getResources().getStringArray(R.array.group_join_model);
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, R.string.app_title_create_new_group, this);

        initView();
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

    /**
     *
     */
    private void initView() {
        mNameEdit = (EditText) findViewById(R.id.group_name);

        mNoticeEdit = (EditText) findViewById(R.id.group_notice);
        InputFilter[] inputFiltersNotice = new InputFilter[1];
        inputFiltersNotice[0] = new ITextFilter(1);
        mNoticeEdit.setFilters(inputFiltersNotice);

        mCreateBtn = (FButton) findViewById(R.id.create);
        mCreateBtn.setOnClickListener(this);
        mCreateBtn.setCornerRadius(3);
        mCreateBtn.setEnabled(false);
        mCreateBtn.setButtonColor(getResources().getColor(
                R.color.fbutton_color_concrete));

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new ITextFilter();
        mNameEdit.setFilters(inputFilters);
        mNameEdit.addTextChangedListener(textWatcher);

        mPermissionSpinner = (Spinner) findViewById(R.id.str_group_permission_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.group_join_model, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPermissionSpinner.setAdapter(adapter);
        mPermissionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        mPermissionModel = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        mSetPermission = (FButton) findViewById(R.id.str_group_permission_spinner2);
        mSetPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPermissionDialog();
            }
        });
        mSetPermission.setCornerRadius(3);
        initPermissionText();
    }

    /**
     * @return
     */
    private boolean checkNameEmpty() {
        return mNameEdit != null && mNameEdit.getText().toString().trim().length() > 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                v.startAnimation(AnimationUtils.loadAnimation(CreateGroupActivity.this,
                        R.anim.icon_scale));
                hideSoftKeyboard();
                finish();
                break;
            case R.id.create:
                hideSoftKeyboard();
                mPostingdialog = new ECProgressDialog(this, R.string.create_group_posting);
                mPostingdialog.show();
                ECGroupManager ecGroupManager = SDKCoreHelper.getECGroupManager();
                if (!checkNameEmpty() || ecGroupManager == null) {
                    return;
                }
                // 调用API创建群组、处理创建群组接口回调
                ecGroupManager.createGroup(getGroup(), this);
                break;
            default:
                break;
        }
    }

    private boolean showPermissionDialog() {
        ECListDialog dialog = new ECListDialog(this, stringArray, mPermissionModel);
        dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
            @Override
            public void onDialogItemClick(Dialog d, int position) {
                mPermissionModel = position;
                initPermissionText();
            }
        });
        dialog.setTitle(R.string.str_group_permission_spinner);
        dialog.show();
        return true;
    }

    private void initPermissionText() {
        mSetPermission.setText(stringArray[mPermissionModel]);
    }


    /**
     * 创建群组参数
     *
     * @return
     */
    private ECGroup getGroup() {
        ECGroup group = new ECGroup();
        // 设置群组名称
        group.setName(mNameEdit.getText().toString().trim());
        // 设置群组公告
        group.setDeclare(mNoticeEdit.getText().toString().trim());
        // 临时群组（100人）
        group.setScope(ECGroup.Scope.TEMP);
        // 群组验证权限，需要身份验证
        group.setPermission(ECGroup.Permission.values()[mPermissionModel + 1]);
        // 设置群组创建者
        group.setOwner(CCPAppManager.getClientUser().getUserId());
        return group;
    }

    @Override
    public void onResume() {
        super.onResume();
        GroupMemberService.addListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == 0x2a) {
            if (data == null) {
                finish();
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            finish();
            return;
        }

        String[] selectUser = data.getStringArrayExtra("Select_Conv_User");
        if (selectUser != null && selectUser.length > 0) {
            mPostingdialog = new ECProgressDialog(this, R.string.invite_join_group_posting);
            mPostingdialog.show();
            GroupMemberService.inviteMembers(group.getGroupId(), "", 1, selectUser);
        }

    }


    @Override
    public void onCreateGroupComplete(ECError error, ECGroup group) {
        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
            // 创建的群组实例化到数据库
            // 其他的页面跳转逻辑
            group.setIsNotice(true);
            GroupSqlManager.insertGroup(group, true, false);
            this.group = group;
            Intent intent = new Intent(this, MobileContactSelectActivity.class);
            intent.putExtra("group_select_need_result", true);
            startActivityForResult(intent, 0x2a);
        } else {
            ToastUtil.showMessage("创建群组失败[" + error.errorCode + "]");
        }
        dismissPostingDialog();
    }

    @Override
    public void onSynsGroupMember(String groupId) {
        dismissPostingDialog();
        Intent intent = new Intent(CreateGroupActivity.this, ChattingActivity.class);
        intent.putExtra(ChattingActivity.RECIPIENTS, groupId);
        intent.putExtra(ChattingActivity.CONTACT_USER, group.getName());
        startActivity(intent);
        finish();
    }

    /**
     * @param text
     * @return
     */
    public static int fliteCounts(CharSequence text) {
        int count = (30 - Math.round(calculateCounts(text)));
        LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count " + count);
        return count;
    }

    /**
     * @param text
     * @return
     */
    public static float calculateCounts(CharSequence text) {

        float lengh = 0.0F;
        for (int i = 0; i < text.length(); i++) {
            if (!DemoUtils.characterChinese(text.charAt(i))) {
                lengh += 1.0F;
            } else {
                lengh += 0.5F;
            }
        }

        return lengh;
    }


    class ITextFilter implements InputFilter {
        private int limit = 50;

        public ITextFilter() {
            this(0);
        }

        public ITextFilter(int type) {
            if (type == 1) {
                limit = 128;
            }
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            LogUtil.i(LogUtil.getLogUtilsTag(CreateGroupActivity.class), source
                    + " start:" + start + " end:" + end + " " + dest
                    + " dstart:" + dstart + " dend:" + dend);
            float count = calculateCounts(dest);
            int overplus = limit - Math.round(count) - (dend - dstart);
            if (overplus <= 0) {
                if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
                        && (source.length() > 0)
                        && (!(DemoUtils.characterChinese(source.charAt(0))))) {
                    return source.subSequence(0, 1);
                }
                ToastUtil.showMessage("超过最大限制");
                return "";
            }

            if (overplus >= (end - start)) {
                return null;
            }
            int tepmCont = overplus + start;
            if ((Character.isHighSurrogate(source.charAt(tepmCont - 1))) && (--tepmCont == start)) {
                return "";
            }
            return source.subSequence(start, tepmCont);
        }
    }
}
