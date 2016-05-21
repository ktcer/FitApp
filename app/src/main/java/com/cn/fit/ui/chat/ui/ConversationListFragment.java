/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
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
package com.cn.fit.ui.chat.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.dialog.ECListDialog;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.common.view.NetWarnBannerView;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.storage.GroupNoticeSqlManager;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.model.Conversation;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.chat.ui.group.GroupNoticeActivity;
import com.cn.fit.ui.chat.ui.group.GroupService;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

/**
 * 会话界面
 * Created by Jorstin on 2015/3/18.
 */
public class ConversationListFragment extends TabFragment implements CCPListAdapter.OnListAdapterCallBackListener {

    private static final String TAG = "Aihu.ConversationListFragment";


    /**
     * 会话消息列表ListView
     */
    private ListView mListView;
    private NetWarnBannerView mBannerView;
    public ConversationAdapter mAdapter;
    private OnUpdateMsgUnreadCountsListener mAttachListener;
    private ECProgressDialog mPostingdialog;

    final private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View visew, int position,
                                long id) {

            if (mAdapter != null) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return;
                }
                int _position = position - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return;
                }

                Conversation conversation = mAdapter.getItem(_position);
                if (GroupNoticeSqlManager.CONTACT_ID.equals(conversation.getSessionId())) {
                    Intent intent = new Intent(getActivity(), GroupNoticeActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra(ChattingActivity.RECIPIENTS, conversation.getSessionId());
                ECContacts tempECContacts = null;
                for (int i = 0; i < ContactsCache.getInstance().getContacts().size(); i++) {
                    if (ContactsCache.getInstance().getContacts().get(i).getContactid().equals(conversation.getSessionId() + "")) {
                        tempECContacts = ContactsCache.getInstance().getContacts().get(i);
                    }
                }
                if (tempECContacts != null) {
                    intent.putExtra(ChattingActivity.CONTACT_USER, tempECContacts.getNickname());
                    intent.putExtra(ChattingActivity.CONTACT_IMGURL, tempECContacts.getImgUrl());
                    intent.putExtra(ChattingActivity.CONTACT_REMAINTIME, tempECContacts.getRemainServeTime());
                } else {
                    intent.putExtra(ChattingActivity.CONTACT_USER, conversation.getUsername());
                    intent.putExtra(ChattingActivity.CONTACT_IMGURL, "");
                    intent.putExtra(ChattingActivity.CONTACT_REMAINTIME, 0);
                }
                startActivity(intent);
            }
        }
    };

    private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mAdapter != null) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return false;
                }
                int _position = position - headerViewsCount;

                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return false;
                }
                Conversation conversation = mAdapter.getItem(_position);
                final int itemPosition = position;
                String[] menu = buildMenu(conversation);
                ECListDialog dialog = new ECListDialog(getActivity(), /*new String[]{getString(R.string.main_delete)}*/menu);
                dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                    @Override
                    public void onDialogItemClick(Dialog d, int position) {
                        handleContentMenuClick(itemPosition, position);
                    }
                });
                dialog.setTitle(conversation.getUsername());
                dialog.show();
                return true;
            }
            return false;
        }
    };

    private String[] buildMenu(Conversation conversation) {
        if (conversation != null && conversation.getSessionId() != null) {
            if (conversation.getSessionId().toLowerCase().startsWith("g")) {
                ECGroup ecGroup = GroupSqlManager.getECGroup(conversation.getSessionId());
                if (ecGroup == null || !GroupSqlManager.getJoinState(ecGroup.getGroupId())) {
                    return new String[]{getString(R.string.main_delete)};
                }
                if (ecGroup.isNotice()) {
                    return new String[]{getString(R.string.main_delete), getString(R.string.menu_mute_notify)};
                }
                return new String[]{getString(R.string.main_delete), getString(R.string.menu_notify)};
            }
        }
        return new String[]{getString(R.string.main_delete)};
    }

    @Override
    public void onTabFragmentClick() {

    }

    @Override
    protected void onReleaseTabUI() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        registerReceiver(new String[]{GroupService.ACTION_SYNC_GROUP, IMessageSqlManager.ACTION_SESSION_DEL});
    }


    @Override
    public void onResume() {
        super.onResume();
        reTryConnect();
        updateConnectState();
        IMessageSqlManager.registerMsgObserver(mAdapter);
        mAdapter.notifyChange();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAttachListener = (OnUpdateMsgUnreadCountsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUpdateMsgUnreadCountsListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        IMessageSqlManager.unregisterMsgObserver(mAdapter);
    }

    /**
     *
     */
    private void initView() {
        if (mListView != null) {
            mListView.setAdapter(null);

            if (mBannerView != null) {
                mListView.removeHeaderView(mBannerView);
            }
        }

        mListView = (ListView) findViewById(R.id.main_chatting_lv);
        View mEmptyView = findViewById(R.id.empty_conversation_tv);
        mListView.setEmptyView(mEmptyView);
        mListView.setDrawingCacheEnabled(false);
        mListView.setScrollingCacheEnabled(false);

        mListView.setOnItemLongClickListener(mOnLongClickListener);
        mListView.setOnItemClickListener(mItemClickListener);
        mBannerView = new NetWarnBannerView(getActivity());
        mBannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTryConnect();
            }
        });
        mListView.addHeaderView(mBannerView);
        mAdapter = new ConversationAdapter(getActivity(), this);
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);
    }

    private void reTryConnect() {
        ECDevice.ECConnectState connectState = SDKCoreHelper.getConnectState();
        if (connectState == null || connectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            SDKCoreHelper.init(getActivity());
            SDKCoreHelper.getSDKInstance().setOnConnectListener(new SDKCoreHelper.OnConnectInterface() {
                @Override
                public void connected() {
                    updateConnectState();
                }
            });
        }
    }

    public void updateConnectState() {
        if (!isAdded()) {
            return;
        }
        ECDevice.ECConnectState connect = SDKCoreHelper.getConnectState();
        if (connect == ECDevice.ECConnectState.CONNECTING) {
            mBannerView.setNetWarnText(getString(R.string.connecting_server));
            mBannerView.reconnect(true);
        } else if (connect == ECDevice.ECConnectState.CONNECT_FAILED) {
            mBannerView.setNetWarnText(getString(R.string.connect_server_error));
            mBannerView.reconnect(false);
        } else if (connect == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            mBannerView.hideWarnBannerView();
        }
        Log.i("updateConnectState", connect.name());
        LogUtil.d(TAG, "updateConnectState connect :" + connect.name());
    }

    private Boolean handleContentMenuClick(int convresion, int position) {
        if (mAdapter != null) {
            int headerViewsCount = mListView.getHeaderViewsCount();
            if (convresion < headerViewsCount) {
                return false;
            }
            int _position = convresion - headerViewsCount;

            if (mAdapter == null || mAdapter.getItem(_position) == null) {
                return false;
            }
            final Conversation conversation = mAdapter.getItem(_position);
            switch (position) {
                case 0:
                    showProcessDialog();
                    ECHandlerHelper handlerHelper = new ECHandlerHelper();
                    handlerHelper.postRunnOnThead(new Runnable() {
                        @Override
                        public void run() {
                            IMessageSqlManager.deleteChattingMessage(conversation.getSessionId());
                            ToastUtil.showMessage(R.string.clear_msg_success);
                            ConversationListFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissPostingDialog();
                                    mAdapter.notifyChange();
                                }
                            });
                        }
                    });
                    break;
                case 1:
                    showProcessDialog();
                    final boolean notify = GroupSqlManager.isGroupNotify(conversation.getSessionId());
                    ECGroupOption option = new ECGroupOption();
                    option.setGroupId(conversation.getSessionId());
                    option.setRule(notify ? ECGroupOption.Rule.SILENCE : ECGroupOption.Rule.NORMAL);
                    GroupService.setGroupMessageOption(option, new GroupService.GroupOptionCallback() {
                        @Override
                        public void onComplete(String groupId) {
                            if (mAdapter != null) {
                                mAdapter.notifyChange();
                            }
                            ToastUtil.showMessage(notify ? R.string.new_msg_mute_notify : R.string.new_msg_notify);
                            dismissPostingDialog();
                        }

                        @Override
                        public void onError(ECError error) {
                            dismissPostingDialog();
                            ToastUtil.showMessage("设置失败");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.conversation;
    }

    @Override
    public void OnListAdapterCallBack() {
        if (mAttachListener != null) {
            mAttachListener.OnUpdateMsgUnreadCounts();
        }
    }

    public interface OnUpdateMsgUnreadCountsListener {
        void OnUpdateMsgUnreadCounts();
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if (GroupService.ACTION_SYNC_GROUP.equals(intent.getAction())
                || IMessageSqlManager.ACTION_SESSION_DEL.equals(intent.getAction())) {
            if (mAdapter != null) {
                mAdapter.notifyChange();
            }
        }
    }

    void showProcessDialog() {
        mPostingdialog = new ECProgressDialog(ConversationListFragment.this.getActivity(), R.string.login_posting_submit);
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

