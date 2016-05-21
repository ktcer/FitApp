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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.dialog.ECAlertDialog;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.common.view.SettingItem;
import com.cn.fit.ui.chat.core.ClientUser;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.ui.chat.ui.contact.ContactDetailActivity;
import com.cn.fit.ui.chat.ui.contact.ContactLogic;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.chat.ui.contact.MobileContactSelectActivity;
import com.cn.fit.ui.patient.setting.ActivitySettings;
import com.cn.fit.ui.chat.ui.settings.EditConfigureActivity;
import com.cn.fit.util.FButton;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMember;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;


/**
 * 群组详情界面
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-29
 */
public class GroupInfoActivity extends ECSuperActivity implements
        View.OnClickListener, GroupMemberService.OnSynsGroupMemberListener, GroupService.Callback {

    private static final String TAG = "ECDemo.GroupInfoActivity";
    public final static String GROUP_ID = "group_id";
    public static final String EXTRA_RELOAD = "com.cn.aihu.ui.chat_reload";
    public static final String EXTRA_QUEIT = "com.cn.aihu.ui.chat_quit";

    private ScrollView mScrollView;
    private TextView mGroupCountTv;
    /**
     * 群组ID
     */
    private ECGroup mGroup;
    /**
     * 群组公告
     */
    private EditText mNotice;
    /**
     * 群组成员列表
     */
    private ListView mListView;
    /**
     * 群组成员适配器
     */
    private GroupInfoAdapter mAdapter;
    private ECProgressDialog mPostingdialog;
    private boolean mClearChatmsg = false;
    private SettingItem mNoticeItem;
    private SettingItem mNameItem;
    private SettingItem mNewMsgNotify;
    private SettingItem mRoomDisplayname;
    private int mEditMode = -1;

    private final AdapterView.OnItemClickListener mItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            ECGroupMember item = mAdapter.getItem(position);
            if (item == null) {
                return;
            }

            if ("add@yuntongxun.com".equals(item.getVoipAccount())) {
                Intent intent = new Intent(GroupInfoActivity.this, MobileContactSelectActivity.class);
                intent.putExtra("group_select_need_result", true);
                intent.putExtra("select_type", false);
                startActivityForResult(intent, 0x2a);
                return;
            }
            //GroupMemberService.queryGroupMemberCard(mGroup.getGroupId() , item.getVoipAccount());
            //GroupMemberService.modifyGroupMemberCard(mGroup.getGroupId(), item.getVoipAccount());
            ECContacts contact = ContactSqlManager.getContact(item.getVoipAccount());
            if (contact == null || contact.getId() == -1) {
                ToastUtil.showMessage(R.string.contact_none);
                return;
            }
            Intent intent = new Intent(GroupInfoActivity.this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.RAW_ID, contact.getId());
            startActivity(intent);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.group_info_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String groupId = getIntent().getStringExtra(GROUP_ID);
        mGroup = GroupSqlManager.getECGroup(groupId);
        if (mGroup == null) {
            finish();
            return;
        }
        initView();
        refeshGroupInfo();

        GroupService.syncGroupInfo(mGroup.getGroupId());
        GroupMemberService.synsGroupMember(mGroup.getGroupId());

        registerReceiver(new String[]{IMessageSqlManager.ACTION_GROUP_DEL});
    }

    @Override
    public void onResume() {
        super.onResume();
        GroupService.addListener(this);
        GroupMemberService.addListener(this);
    }

    /**
     *
     */
    private void initView() {
        mNotice = (EditText) findViewById(R.id.group_notice);
        mGroupCountTv = (TextView) findViewById(R.id.group_count);
        mNoticeItem = (SettingItem) findViewById(R.id.group_notice2);
        mNoticeItem.getCheckedTextView().setSingleLine(false);
        //mNoticeItem.getCheckedTextView().setMaxLines(5);
        mNameItem = (SettingItem) findViewById(R.id.group_name);
        mNameItem.getCheckedTextView().setSingleLine(false);
        mListView = (ListView) findViewById(R.id.member_lv);
        mListView.setOnItemClickListener(mItemClickListener);
        mScrollView = (ScrollView) findViewById(R.id.info_content);
        mAdapter = new GroupInfoAdapter(this);
        mListView.setAdapter(mAdapter);

        mNewMsgNotify = (SettingItem) findViewById(R.id.settings_new_msg_notify);
        mRoomDisplayname = (SettingItem) findViewById(R.id.settings_room_my_displayname);
        mNewMsgNotify.getCheckedTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGroupNewMsgNotify();
            }
        });


        findViewById(R.id.btn_free_group).setOnClickListener(this);
        findViewById(R.id.clear_msg).setOnClickListener(this);
        mNotice.setEnabled(isOwner() ? true : false);
        FButton button = (FButton) findViewById(R.id.btn_free_group);
        button.setText(isOwner() ? R.string.str_group_dissolution : R.string.str_group_quit);
        button.setCornerRadius(3);
        onSynsGroupMember(mGroup.getGroupId());
    }

    private void updateGroupNewMsgNotify() {
        if (mGroup == null || mGroup.getGroupId() == null) {
            return;
        }
        // 处理消息免打扰
        try {
            if (mNewMsgNotify == null) {
                return;
            }
            mNewMsgNotify.toggle();
            boolean checked = mNewMsgNotify.isChecked();
            showProcessDialog(getString(R.string.login_posting_submit));
            ECGroupOption option = new ECGroupOption();
            option.setGroupId(mGroup.getGroupId());
            option.setRule(checked ? ECGroupOption.Rule.SILENCE : ECGroupOption.Rule.NORMAL);
            GroupService.setGroupMessageOption(option);
            LogUtil.d(TAG, "updateGroupNewMsgNotify: " + checked);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void refeshGroupInfo() {
        if (mGroup == null) {
            return;
        }
        mNotice.setText(mGroup.getDeclare());
        mNoticeItem.setCheckText(mGroup.getDeclare());
        mNotice.setSelection(mNotice.getText().length());
        if (mGroup.getName() != null && mGroup.getName().endsWith("@priategroup.com")) {
            ArrayList<String> member = GroupMemberSqlManager.getGroupMemberID(mGroup.getGroupId());
            if (member != null) {
                ArrayList<String> contactName = ContactSqlManager.getContactName(member.toArray(new String[]{}));
                String chatroomName = DemoUtils.listToString(contactName, ",");
                mGroup.setName(chatroomName);
            }
        }
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, mGroup.getName(), this);
        mNameItem.setCheckText(mGroup.getName());

        if (isOwner()) {
            mNameItem.setOnClickListener(new OnItemClickListener(ActivitySettings.CONFIG_TYPE_GROUP_NAME));
            mNoticeItem.setOnClickListener(new OnItemClickListener(ActivitySettings.CONFIG_TYPE_GROUP_NOTICE));
        }
        mNewMsgNotify.setChecked(!mGroup.isNotice());
    }

    /**
     * 是否是群组创建者
     *
     * @return
     */
    private boolean isOwner() {
        return CCPAppManager.getClientUser().getUserId().equals(mGroup.getOwner());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                v.startAnimation(AnimationUtils.loadAnimation(GroupInfoActivity.this,
                        R.anim.icon_scale));
                goBack();
                break;
            case R.id.btn_free_group:
                mPostingdialog = new ECProgressDialog(this, R.string.group_exit_posting);
                mPostingdialog.show();
                if (isOwner()) {
                    GroupService.disGroup(mGroup.getGroupId());
                    return;
                }
                GroupService.quitGroup(mGroup.getGroupId());
                break;
            case R.id.clear_msg:
                ECAlertDialog buildAlert = ECAlertDialog.buildAlert(GroupInfoActivity.this, R.string.fmt_delcontactmsg_confirm_group, null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPostingdialog = new ECProgressDialog(GroupInfoActivity.this, R.string.clear_chat);
                        mPostingdialog.show();
                        ECHandlerHelper handlerHelper = new ECHandlerHelper();
                        handlerHelper.postRunnOnThead(new Runnable() {
                            @Override
                            public void run() {
                                IMessageSqlManager.deleteChattingMessage(mGroup.getGroupId());
                                ToastUtil.showMessage(R.string.clear_msg_success);
                                mClearChatmsg = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissPostingDialog();
                                    }
                                });
                            }
                        });

                    }

                });
                buildAlert.setTitle(R.string.app_tip);
                buildAlert.show();
                break;
            case R.id.btn_middle:
                if (mScrollView != null) {
                    getTopBarView().post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    void showProcessDialog(String tips) {
        mPostingdialog = new ECProgressDialog(GroupInfoActivity.this, R.string.login_posting_submit);
        mPostingdialog.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == 0x2a || requestCode == 0xa) {
            if (data == null) {
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            return;
        }
        if (requestCode == 0x2a) {
            String[] selectUser = data.getStringArrayExtra("Select_Conv_User");
            if (selectUser != null && selectUser.length > 0) {
                mPostingdialog = new ECProgressDialog(this, R.string.invite_join_group_posting);
                mPostingdialog.show();
                String reason = getString(R.string.group_invite_reason, CCPAppManager.getClientUser().getUserName(), mGroup.getName());
                GroupMemberService.inviteMembers(mGroup.getGroupId(), reason, 2, selectUser);
            }
        } else if (requestCode == 0xa) {
            String result_data = data.getStringExtra("result_data");
            if (mGroup == null) {
                return;
            }
            if (TextUtils.isEmpty(result_data)) {
                ToastUtil.showMessage("不允许为空");
                return;
            }
            if (mEditMode == ActivitySettings.CONFIG_TYPE_GROUP_NAME) {
                mGroup.setName(result_data);
            } else {
                mGroup.setDeclare(result_data);
            }

            doModifyGroup();
        }
    }

    private void doModifyGroup() {
        // 修改群组信息请求
        showProcessDialog(getString(R.string.login_posting_submit));
        GroupService.modifyGroup(mGroup);
    }

    @Override
    public void onSynsGroupMember(String groupId) {
        dismissPostingDialog();
        if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
            return;
        }
        int count = mAdapter.getCount();
        ArrayList<ECGroupMember> members = GroupMemberSqlManager.getGroupMemberWithName(mGroup.getGroupId());
        if (members == null) {
            members = new ArrayList<ECGroupMember>();
        }
        boolean hasSelf = false;
        ClientUser clientUser = CCPAppManager.getClientUser();
        for (ECGroupMember member : members) {
            if (clientUser.getUserId().equals(member.getVoipAccount())) {
                hasSelf = true;
                break;
            }
        }
        if (!hasSelf) {
            ECContacts contact = ContactSqlManager.getContact(clientUser.getUserId());
            if (contact != null) {
                ECGroupMember member = new ECGroupMember();
                member.setVoipAccount(contact.getContactid());
                member.setRemark(contact.getRemark());
                member.setDisplayName(contact.getNickname());
                members.add(member);
            }
        }

        mAdapter.setData(members);
        int memCount = isOwner() ? mAdapter.getCount() - 1 : mAdapter.getCount();
        mGroupCountTv.setText(getString(R.string.str_group_members_tips, memCount));
        if (members != null && count <= members.size()) {
            setListViewHeightBasedOnChildren(mListView);
        }

        getTopBarView().setTitle(mGroup.getName() + getString(R.string.str_group_members_titletips, memCount));
        //mRoomDisplayname.setCheckText(mGroup.getDeclare());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra(EXTRA_RELOAD, mClearChatmsg);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.setData(null);
            mAdapter.clear();
            mAdapter = null;
        }
        GroupMemberService.addListener(null);
        mGroup = null;
        mPostingdialog = null;
        System.gc();
    }

    /**
     * 动态改变ListView 高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 2));
        ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);

        getActivityLayoutView().invalidate();
    }

    public class GroupInfoAdapter extends ArrayAdapter<ECGroupMember> {
        Context mContext;

        public GroupInfoAdapter(Context context) {
            super(context, 0);
            mContext = context;
        }

        public void setData(List<ECGroupMember> data) {
            clear();
            if (data != null) {
                for (ECGroupMember appEntry : data) {
                    add(appEntry);
                }
            }

            if (isOwner()) {
                ECGroupMember add = new ECGroupMember();
                add.setVoipAccount("add@yuntongxun.com");
                add(add);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if (convertView == null || convertView.getTag() == null) {
                view = View.inflate(mContext, R.layout.group_member_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.mAvatar = (ImageView) view.findViewById(R.id.group_card_item_avatar_iv);
                mViewHolder.name_tv = (EmojiconTextView) view.findViewById(R.id.group_card_item_nick);
                mViewHolder.account = (TextView) view.findViewById(R.id.account);
                mViewHolder.operationRm = (Button) view.findViewById(R.id.remove_btn);
                mViewHolder.operationSpeak = (Button) view.findViewById(R.id.speak_btn);
                mViewHolder.operationLy = (LinearLayout) view.findViewById(R.id.operation_ly);

                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            final ECGroupMember item = getItem(position);
            if (item != null) {
                item.setDisplayName(TextUtils.isEmpty(item.getDisplayName()) ? item.getVoipAccount() : item.getDisplayName());
                mViewHolder.operationSpeak.setText(item.isBan() ? R.string.str_group_speak_enable : R.string.str_group_speak_disenable);
                mViewHolder.operationSpeak.setTextColor(item.isBan() ? Color.parseColor("#ffff5454") : Color.parseColor("#ff00B486"));
                mViewHolder.account.setText(item.getVoipAccount());
                if (item.getVoipAccount().equals("add@yuntongxun.com") && position == getCount() - 1) {
                    mViewHolder.mAvatar.setImageResource(R.drawable.add_contact_selector);
                    mViewHolder.name_tv.setText(R.string.str_group_invite);
                    mViewHolder.operationLy.setVisibility(View.INVISIBLE);
                    mViewHolder.account.setVisibility(View.GONE);
                } else {
                    mViewHolder.account.setVisibility(View.VISIBLE);
                    mViewHolder.mAvatar.setImageBitmap(ContactLogic.getPhoto(item.getRemark()));
                    String creator;
                    if (item.getRole() == 1) {
                        creator = "[创建者]";
                    } else if (item.getRole() == 2) {
                        creator = "[管理员]";
                    } else {
                        creator = "[成员]";
                    }
                    mViewHolder.name_tv.setText(item.getDisplayName() + creator);
                    if (isOwner() && !CCPAppManager.getClientUser().getUserId().equals(item.getVoipAccount())) {
                        mViewHolder.operationLy.setVisibility(View.VISIBLE);
                        mViewHolder.operationRm.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 处理成员移除
                                doRemoveMember(item);
                            }
                        });

                        mViewHolder.operationSpeak.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 处理成员禁言
                                doSetMemberSpeakStatus(item);
                            }
                        });


                    } else {
                        mViewHolder.operationLy.setVisibility(View.INVISIBLE);
                    }
                }

            }

            return view;
        }

        class ViewHolder {
            /**
             * 头像
             */
            ImageView mAvatar;
            /**
             * 名称
             */
            EmojiconTextView name_tv;
            TextView account;
            /**
             * 踢出按钮
             */
            Button operationRm;
            /**
             * 禁言
             */
            Button operationSpeak;
            LinearLayout operationLy;

        }
    }

    /**
     * 设置群组成员禁言状态
     *
     * @param item
     */
    private void doSetMemberSpeakStatus(final ECGroupMember item) {
        String msg = getString(R.string.str_group_member_speak_tips, item.getDisplayName());
        if (item.isBan()) {
            msg = getString(R.string.str_group_member_unspeak_tips, item.getDisplayName());
        }
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(this, msg, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProcessDialog(getString(R.string.login_posting_submit));
                GroupMemberService.forbidMemberSpeakStatus(mGroup.getGroupId(), item.getVoipAccount(), !item.isBan());
            }
        });

        buildAlert.setTitle(R.string.app_tip);
        buildAlert.show();
    }

    /**
     * 移除群组成员
     *
     * @param item
     */
    private void doRemoveMember(final ECGroupMember item) {
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(this, getString(R.string.str_group_member_remove_tips, item.getDisplayName()), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProcessDialog(getString(R.string.group_remove_member_posting));
                GroupMemberService.removerMember(mGroup.getGroupId(), item.getVoipAccount());
                mClearChatmsg = true;
            }
        });

        buildAlert.setTitle(R.string.app_tip);
        buildAlert.show();
    }

    @Override
    public void onSyncGroup() {

    }

    @Override
    public void onSyncGroupInfo(String groupId) {
        dismissPostingDialog();
        if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
            return;
        }
        mGroup = GroupSqlManager.getECGroup(groupId);
        refeshGroupInfo();
    }

    @Override
    public void onGroupDel(String groupId) {
        if (mGroup == null || !mGroup.getGroupId().equals(groupId)) {
            return;
        }
        dismissPostingDialog();
        ECGroup ecGroup = GroupSqlManager.getECGroup(mGroup.getGroupId());
        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra(EXTRA_QUEIT, true);
        setResult(RESULT_OK, intent);
        if (ecGroup == null) {
            // 群组被解散
            finish();
            return;
        }
        finish();
        // 更新群组界面 已经退出群组
    }

    @Override
    public void onError(ECError error) {
        dismissPostingDialog();
    }


    private final class OnItemClickListener implements View.OnClickListener {

        private int mType;

        public OnItemClickListener(int type) {
            this.mType = type;
        }

        @Override
        public void onClick(View v) {
            mEditMode = this.mType;
            Intent intent = new Intent(GroupInfoActivity.this, EditConfigureActivity.class);
            if (mEditMode == ActivitySettings.CONFIG_TYPE_GROUP_NAME) {
                intent.putExtra("edit_title", getString(R.string.edit_group_name));
                intent.putExtra("edit_default_data", mGroup.getName());
            } else {
                intent.putExtra("edit_title", getString(R.string.edit_group_notice));
                intent.putExtra("edit_default_data", mGroup.getDeclare());
            }
            startActivityForResult(intent, 0xa);
        }
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if (IMessageSqlManager.ACTION_GROUP_DEL.equals(intent.getAction()) && intent.hasExtra("group_id")) {
            String id = intent.getStringExtra("group_id");
            if (id != null && id.equals(mGroup.getGroupId())) {
                ToastUtil.showMessage("群组已经被解散");
                finish();
            }
        }
    }
}
