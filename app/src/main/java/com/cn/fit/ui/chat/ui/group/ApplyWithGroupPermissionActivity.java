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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.chat.ui.settings.EditConfigureActivity;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECGroup;


/**
 * 申请加入群组界面
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-31
 */
public class ApplyWithGroupPermissionActivity extends ECSuperActivity implements
        GroupService.Callback, View.OnClickListener, GroupService.OnApplyGroupCallbackListener {

    private static final String TAG = "ECDemo.ApplyWithGroupPermissionActivity";
    /**
     * 群组ID
     */
    private ECGroup mGroup;
    /**
     * 群组公告
     */
    private EditText mNotice;
    String appTitle;
    /**
     * 群组基本信息
     */
    private GroupProfileView mGroupProfileView;
    private ECProgressDialog mPostingdialog;

    @Override
    protected int getLayoutId() {
        return R.layout.apply_group_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String groupId = getIntent().getStringExtra(GroupInfoActivity.GROUP_ID);
        if (TextUtils.isEmpty(groupId)) {
            ToastUtil.showMessage("群组ID为空");
            finish();
            return;
        }

        initView();
        mGroup = GroupSqlManager.getECGroup(groupId);
        syncGroupInf(groupId);
        GroupService.syncGroupInfo(groupId);
    }

    /**
     * 初始化
     */
    private void initView() {
        mGroupProfileView = (GroupProfileView) findViewById(R.id.group_file);
        mNotice = (EditText) findViewById(R.id.group_notice);
        mNotice.setEnabled(false);

        TextView view = (TextView) findViewById(R.id.red_btn);
        view.setBackgroundResource(R.drawable.btn_style_red);
        view.setOnClickListener(this);
        view.setText(R.string.group_apply_btn);
    }

    private void syncGroupInf(String groupId) {
        mGroup = GroupSqlManager.getECGroup(groupId);
        if (mGroup == null) {
            return;
        }

        if (!TextUtils.isEmpty(mGroup.getName())) {
            mGroupProfileView.setNameText(appTitle = mGroup.getName());
            getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, mGroup.getName(), this);
        }
        if (!TextUtils.isEmpty(mGroup.getOwner())) {
            ECContacts contact = ContactSqlManager.getContact(mGroup.getOwner());
            if (contact != null) {
                mGroupProfileView.setOwnerText(contact.getNickname());
            }
        }
        mGroupProfileView.setGroupIdText(DemoUtils.getGroupShortId(mGroup.getGroupId()));


        mNotice.setText(mGroup.getDeclare());
        mNotice.setSelection(mNotice.getText().length());


    }

    @Override
    public void onResume() {
        super.onResume();
        GroupService.addListener(this);
    }

    @Override
    public void onSyncGroup() {

    }

    @Override
    public void onSyncGroupInfo(String groupId) {
        syncGroupInf(groupId);
    }

    @Override
    public void onGroupDel(String groupId) {

    }

    @Override
    public void onError(ECError error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.red_btn:

                if (mGroup.getPermission() == ECGroup.Permission.NEED_AUTH) {
                    // 需要权限
                    Intent intent = new Intent(ApplyWithGroupPermissionActivity.this, EditConfigureActivity.class);
                    intent.putExtra("setting_type", -1);
                    intent.putExtra("edit_title", "申请理由");
                    startActivityForResult(intent, 0x2a);
                    return;
                }
                applyGroup("");
                break;
            default:
                break;
        }
    }

    private void applyGroup(String declare) {
        mPostingdialog = new ECProgressDialog(this, R.string.loading_press);
        mPostingdialog.show();
        if (TextUtils.isEmpty(declare)) {
            String userName = CCPAppManager.getClientUser().getUserName();
            declare = getString(R.string.group_apply_reason, userName);
        }
        GroupService.applyGroup(mGroup.getGroupId(), declare, this);
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
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            return;
        }

        if (requestCode == 0x2a) {
            applyGroup(data.getStringExtra("result_data"));
        }
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

    @Override
    public void onApplyGroup(boolean success) {
        dismissPostingDialog();
        if (!success) {
            return;
        }

        if (mGroup.getPermission() == ECGroup.Permission.AUTO_JOIN) {
            GroupSqlManager.updateJoinStatus(mGroup.getGroupId(), true);
            // 直接加入
            Intent intent = new Intent(ApplyWithGroupPermissionActivity.this, ChattingActivity.class);
            intent.putExtra(ChattingActivity.RECIPIENTS, mGroup.getGroupId());
            intent.putExtra(ChattingActivity.CONTACT_USER, mGroup.getName());
            startActivity(intent);
        } else {
            ToastUtil.showMessage("申请加入群组" + appTitle + "成功，请等待管理员审核");
        }
        finish();
    }

}
