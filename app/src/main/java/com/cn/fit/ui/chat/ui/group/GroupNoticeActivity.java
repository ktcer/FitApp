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

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.DateUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.storage.GroupNoticeSqlManager;
import com.cn.fit.ui.chat.ui.CCPListAdapter;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECAckType;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;


/**
 * 群组通知列表接口
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-31
 */
public class GroupNoticeActivity extends ECSuperActivity implements
        View.OnClickListener, GroupService.Callback {

    private static final String TAG = "ECDemo.GroupNoticeActivity";

    /**
     * 会话消息列表ListView
     */
    private ListView mListView;
    private GroupNoticeAdapter mAdapter;
    private ECProgressDialog mPostingdialog;

    @Override
    protected int getLayoutId() {
        return R.layout.group_notice_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, getString(R.string.app_clear), getString(R.string.app_title_notice), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        GroupService.addListener(this);
        GroupNoticeSqlManager.setAllSessionRead();
        GroupNoticeSqlManager.registerMsgObserver(mAdapter);
        mAdapter.notifyChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        GroupNoticeSqlManager.unregisterMsgObserver(mAdapter);
    }


    /**
     *
     */
    private void initView() {
        if (mListView != null) {
            mListView.setAdapter(null);
        }

        mListView = (ListView) findViewById(R.id.group_notice_lv);
        View mCallEmptyView = findViewById(R.id.empty_conversation_tv);
        mListView.setEmptyView(mCallEmptyView);
        mListView.setDrawingCacheEnabled(false);
        mListView.setScrollingCacheEnabled(false);

        mListView.setOnItemClickListener(null);

        mAdapter = new GroupNoticeAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onSyncGroup() {

    }

    @Override
    public void onSyncGroupInfo(String groupId) {

    }

    @Override
    public void onGroupDel(String groupId) {

    }

    @Override
    public void onError(ECError error) {
        dismissPostingDialog();
    }


    public class GroupNoticeAdapter extends CCPListAdapter<DemoGroupNotice> {

        /**
         * @param ctx
         */
        public GroupNoticeAdapter(Context ctx) {
            super(ctx, new DemoGroupNotice());
        }

        @Override
        protected void initCursor() {
            notifyChange();
        }

        @Override
        protected DemoGroupNotice getItem(DemoGroupNotice t,
                                          Cursor cursor) {
            DemoGroupNotice message = new DemoGroupNotice();
            message.setCursor(cursor);
            return message;
        }

        public final CharSequence getContent(NoticeSystemMessage message) {
            if (message.getType() == ECGroupNoticeMessage.ECGroupMessageType.QUIT) {

            }
            return message.getContent();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if (convertView == null || convertView.getTag() == null) {
                view = View.inflate(mContext, R.layout.group_notice_list_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.msgType = (TextView) view.findViewById(R.id.msg_type);
                mViewHolder.nickname = (TextView) view.findViewById(R.id.user_nickname);
                mViewHolder.ImageViewHeader = (ImageView) view.findViewById(R.id.ImageViewHeader);
                mViewHolder.msgTime = (TextView) view.findViewById(R.id.msg_time);
                mViewHolder.sysMsgFrom = (TextView) view.findViewById(R.id.sysMsg_from);
                mViewHolder.resultShow = (TextView) view.findViewById(R.id.result_show);
                mViewHolder.resultSummary = (TextView) view.findViewById(R.id.result_summary);
                mViewHolder.acceptBtn = (Button) view.findViewById(R.id.accept_btn);
                mViewHolder.refuseBtn = (Button) view.findViewById(R.id.Refuse_btn);
                mViewHolder.operationLy = (LinearLayout) view.findViewById(R.id.operation_ly);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            final DemoGroupNotice item = getItem(position);
            mViewHolder.nickname.setText(item.getGroupName());
            mViewHolder.resultSummary.setText(item.getContent());
            mViewHolder.sysMsgFrom.setText(getString(R.string.str_system_come_from, item.getGroupName()));
            mViewHolder.sysMsgFrom.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(item.getDeclared())) {
                mViewHolder.sysMsgFrom.setText("附加消息：" + item.getDeclared());
                mViewHolder.sysMsgFrom.setVisibility(View.VISIBLE);
            }
            if (item.getDateCreate() > 0) {
                mViewHolder.msgTime.setText(DateUtil.getDateString(item.getDateCreate(), DateUtil.SHOW_TYPE_CALL_LOG));
            } else {
                mViewHolder.msgTime.setText("");
            }

            if (item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_NEED_REPLAY) {

                // System information about the invitation to join the group
                // or join the group needs to operate, Whether is it right? Read or unread,
                // as long as the state has not operation can display the operating button
                mViewHolder.operationLy.setVisibility(View.VISIBLE);
                mViewHolder.resultShow.setVisibility(View.GONE);

            } else {
                // Other notice about information, only need to display
                // without the need to have relevant operation
                mViewHolder.operationLy.setVisibility(View.GONE);
                mViewHolder.resultShow.setVisibility(View.VISIBLE);
                if (item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_REFUSE) {
                    mViewHolder.resultShow.setText(R.string.str_system_message_operation_result_refuse);
                } else if (item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_THROUGH) {
                    mViewHolder.resultShow.setText(R.string.str_system_message_operation_result_through);

                } else {
                    mViewHolder.resultShow.setVisibility(View.GONE);
                }
            }


            mViewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //
                    OperationGroupSystemMsg(true, item);
                }
            });
            mViewHolder.refuseBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OperationGroupSystemMsg(false, item);
                }
            });
            return view;
        }

        @Override
        protected void notifyChange() {
            Cursor cursor = GroupNoticeSqlManager.getCursor();
            setCursor(cursor);
            super.notifyDataSetChanged();
        }


        /**
         * 处理接受或者拒绝邀请
         *
         * @param isAccept
         * @param imSystemMessage
         */
        protected void OperationGroupSystemMsg(final boolean isAccept, final DemoGroupNotice imSystemMessage) {
            showProcessDialog(getString(R.string.login_posting_submit));
            synchronized (GroupNoticeActivity.class) {

                boolean isInvite = imSystemMessage.getAuditType() == ECGroupNoticeMessage.ECGroupMessageType.INVITE.ordinal();
                ECAckType ackType = isAccept ? ECAckType.AGREE : ECAckType.REJECT;
                GroupService.operationGroupApplyOrInvite(isInvite, imSystemMessage.getGroupId(), isInvite ? imSystemMessage.getAdmin() : imSystemMessage.getMember(), ackType, new GroupService.OnAckGroupServiceListener() {
                    @Override
                    public void onAckGroupService(boolean success) {
                        long rows = GroupNoticeSqlManager.updateNoticeOperation(imSystemMessage.getId(), isAccept);
                        LogUtil.d(TAG, "[OperationGroupSystemMsg] result :" + rows + " ,");
                        notifyChange();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissPostingDialog();
                            }
                        });
                    }
                });
            }
        }

    }


    static class ViewHolder {
        LinearLayout operationLy;
        TextView msgType;
        TextView resultShow;
        TextView nickname;
        TextView sysMsgFrom;
        TextView msgTime;
        ImageView ImageViewHeader;
        TextView resultSummary;
        Button acceptBtn; // accetp
        Button refuseBtn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.closeCursor();
        }
        GroupService.addListener(null);
        System.gc();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.text_right:
                GroupNoticeSqlManager.delSessions();
                mAdapter.notifyChange();
                break;
            default:
                break;
        }
    }


    void showProcessDialog(String tips) {
        mPostingdialog = new ECProgressDialog(GroupNoticeActivity.this, R.string.login_posting_submit);
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
}
