package com.cn.fit.ui.chat.ui.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.view.TopBarView;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.ui.chat.ui.ContactListFragment;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.group.GroupService;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;

/**
 * 联系人选择页面
 * Created by Jorstin on 2015/3/18.
 */
public class ContactSelectListActivity extends ECSuperActivity implements
        View.OnClickListener, ContactListFragment.OnContactClickListener {
    private ECProgressDialog mPostingdialog;
    private static final String TAG = "Aihu.ContactSelectListActivity";
    /**
     * 查看群组
     */
    public static final int REQUEST_CODE_VIEW_GROUP_OWN = 0x2a;
    private TopBarView mTopBarView;
    private boolean mNeedResult;
    private boolean mShowGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_contact_select;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        mNeedResult = getIntent().getBooleanExtra("group_select_need_result", false);
        mShowGroup = getIntent().getBooleanExtra("select_type", true);
        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(R.id.contact_container) == null) {
            ContactListFragment list = ContactListFragment.newInstance(mShowGroup ? ContactListFragment.TYPE_SELECT : ContactListFragment.TYPE_NON_GROUP);
            fm.beginTransaction().add(R.id.contact_container, list).commit();
        }
        mTopBarView = getTopBarView();
        String actionBtn = getString(R.string.radar_ok_count, getString(R.string.dialog_ok_button), 0);
        mTopBarView.setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, R.drawable.btn_style_green, null, actionBtn, getString(R.string.select_contacts), null, this);
        mTopBarView.setRightBtnEnable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.text_right:
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                if (fragments.get(0) instanceof ContactListFragment) {
                    String chatuser = ((ContactListFragment) fragments.get(0)).getChatuser();
                    String[] split = chatuser.split(",");
                    if (split.length == 1 && !mNeedResult) {
                        Intent intent = new Intent(ContactSelectListActivity.this, ChattingActivity.class);
                        intent.putExtra(ChattingActivity.RECIPIENTS, split[0]);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    if (mNeedResult) {
                        Intent intent = new Intent();
                        intent.putExtra("Select_Conv_User", split);
                        setResult(-1, intent);
                        finish();
                        return;
                    }

                    if (split.length > 0) {
                        postCreatePrivateChatroom(split);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 创建一个私有群组
     *
     * @param split
     */
    private void postCreatePrivateChatroom(String[] split) {
        mPostingdialog = new ECProgressDialog(this, R.string.create_chatroom_posting);
        mPostingdialog.show();
        GroupService.doCreateGroup(split, new ECGroupManager.OnInviteJoinGroupListener() {


            @Override
            public void onInviteJoinGroupComplete(ECError error, String groupId,
                                                  String[] members) {
                if ("000000".equals(error.errorCode)) {
                    GroupMemberSqlManager.insertGroupMembers(groupId, members);
                    ArrayList<String> contactName = ContactSqlManager.getContactName(members);
                    String users = DemoUtils.listToString(contactName, ",");
                    Intent intent = new Intent(ContactSelectListActivity.this, ChattingActivity.class);
                    intent.putExtra(ChattingActivity.RECIPIENTS, groupId);
                    intent.putExtra(ChattingActivity.CONTACT_USER, users);
                    startActivity(intent);
                }
                dismissPostingDialog();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == REQUEST_CODE_VIEW_GROUP_OWN) {
            if (data == null) {
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            return;
        }

        String contactId = data.getStringExtra(ChattingActivity.RECIPIENTS);
        String contactUser = data.getStringExtra(ChattingActivity.CONTACT_USER);
        if (contactId != null && contactId.length() > 0) {
            Intent intent = new Intent(this, ChattingActivity.class);
            intent.putExtra(ChattingActivity.RECIPIENTS, contactId);
            intent.putExtra(ChattingActivity.CONTACT_USER, contactUser);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onContactClick(int count) {
        mTopBarView.setRightBtnEnable(count > 0 ? true : false);
        mTopBarView.setRightButtonText(getString(R.string.radar_ok_count, getString(R.string.dialog_ok_button), count));
    }

    @Override
    public void onSelectGroupClick() {
        Intent intent = new Intent(this, GroupCardSelectUI.class);
        startActivityForResult(intent, REQUEST_CODE_VIEW_GROUP_OWN);
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
}
