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
package com.cn.fit.ui.chat.ui.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.ui.ECSuperActivity;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.group.GroupService;
import com.yuntongxun.ecsdk.im.ECGroup;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-29
 */
public class GroupCardSelectUI extends ECSuperActivity implements View.OnClickListener {

    private static final String TAG = "ECDemo.GroupCardSelectUI";
    /**
     * 群组列表
     */
    private ListView mListView;
    /**
     * 群组列表适配器
     */
    private GroupSelectAdapter mGroupSelectAdapter;
    /**
     * 群组列表
     */
    private List<ECGroup> mGroups;
    private TextView mEmptyView;

    private final AdapterView.OnItemClickListener mItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            ECGroup group = (ECGroup) mGroupSelectAdapter.getItem(position);
            if (group == null) {
                LogUtil.e(TAG, "onItemClick contact null");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(ChattingActivity.RECIPIENTS, group.getGroupId());
            intent.putExtra(ChattingActivity.CONTACT_USER, group.getName());
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.group_card_select;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, R.string.address_select_group_card, this);

        mGroups = GroupSqlManager.getJoinGroups();
        for (ECGroup group : mGroups) {
            if (group.getName() != null && group.getName().endsWith(GroupService.PRICATE_CHATROOM)) {
                ArrayList<String> member = GroupMemberSqlManager.getGroupMemberID(group.getGroupId());
                if (member != null) {
                    ArrayList<String> contactName = ContactSqlManager.getContactName(member.toArray(new String[]{}));
                    String chatroomName = DemoUtils.listToString(contactName, ",");
                    group.setName(chatroomName);
                }
            }
        }
        initView();
    }

    /**
     * 初始化布局参数
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.group_card_select_list);
        mGroupSelectAdapter = new GroupSelectAdapter();
        mListView.setAdapter(mGroupSelectAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
        mEmptyView = (TextView) findViewById(R.id.group_card_empty_tip_tv);
        mEmptyView.setVisibility((mGroups.size() <= 0) ? View.VISIBLE : View.GONE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGroups != null) {
            mGroups.clear();
            mGroups = null;
        }
    }


    public class GroupSelectAdapter extends BaseAdapter {
        int padding;

        public GroupSelectAdapter() {
            padding = getResources().getDimensionPixelSize(R.dimen.OneDPPadding);
        }

        @Override
        public int getCount() {
            return mGroups.size();
        }

        @Override
        public Object getItem(int position) {
            return mGroups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder mViewHolder = null;
            if (convertView == null || convertView.getTag() == null) {
                view = View.inflate(GroupCardSelectUI.this, R.layout.group_card_select_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.head = (ImageView) view.findViewById(R.id.group_card_item_avatar_iv);
                mViewHolder.nick = (TextView) view.findViewById(R.id.group_card_item_nick);
                mViewHolder.count = (TextView) view.findViewById(R.id.group_card_item_count_tv);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }
            ECGroup item = (ECGroup) getItem(position);
            Bitmap bitmap = ContactLogic.getChatroomPhoto(item.getGroupId());
            if (bitmap != null) {
                mViewHolder.head.setImageBitmap(bitmap);
                mViewHolder.head.setPadding(padding, padding, padding, padding);

            } else {
                mViewHolder.head.setImageResource(R.drawable.group_head);
                mViewHolder.head.setPadding(0, 0, 0, 0);
            }
            mViewHolder.nick.setText(item.getName());

            return view;
        }

        class ViewHolder {
            ImageView head;
            TextView nick;
            TextView count;
        }
    }
}
