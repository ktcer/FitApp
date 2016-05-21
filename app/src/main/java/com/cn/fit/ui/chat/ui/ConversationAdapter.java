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
package com.cn.fit.ui.chat.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.utils.DateUtil;
import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.ConversationSqlManager;
import com.cn.fit.ui.chat.storage.GroupMemberSqlManager;
import com.cn.fit.ui.chat.storage.GroupNoticeSqlManager;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.ui.chat.ui.chatting.model.Conversation;
import com.cn.fit.ui.chat.ui.contact.ContactLogic;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.chat.ui.group.GroupNoticeHelper;
import com.cn.fit.util.AbsParam;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.ArrayList;

/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-8
 */
public class ConversationAdapter extends CCPListAdapter<Conversation> {

    private OnListAdapterCallBackListener mCallBackListener;
    int padding;
    private Context mContext;

    /**
     * @param ctx
     */
    public ConversationAdapter(Context ctx,
                               OnListAdapterCallBackListener listener) {
        super(ctx, new Conversation());
        mContext = ctx;
        mCallBackListener = listener;
        padding = ctx.getResources()
                .getDimensionPixelSize(R.dimen.OneDPPadding);
    }

    @Override
    protected Conversation getItem(Conversation t, Cursor cursor) {
        Conversation conversation = new Conversation();
        conversation.setCursor(cursor);
        if (conversation.getUsername() != null
                && conversation.getUsername().endsWith("@priategroup.com")) {
            ArrayList<String> member = GroupMemberSqlManager
                    .getGroupMemberID(conversation.getSessionId());
            if (member != null) {
                ArrayList<String> contactName = ContactSqlManager
                        .getContactName(member.toArray(new String[]{}));
                if (contactName != null && !contactName.isEmpty()) {
                    String chatroomName = DemoUtils.listToString(contactName,
                            ",");
                    conversation.setUsername(chatroomName);
                }
            }
        }
        return conversation;
    }

    /**
     * 会话时间
     *
     * @param conversation
     * @return
     */
    protected final CharSequence getConversationTime(Conversation conversation) {
        if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING
                .ordinal()) {
            return mContext.getString(R.string.conv_msg_sending);
        }
        if (conversation.getDateTime() <= 0) {
            return "";
        }
        return DateUtil.getDateString(conversation.getDateTime(),
                DateUtil.SHOW_TYPE_CALL_LOG).trim();
    }

    /**
     * 根据消息类型返回相应的主题描述
     *
     * @param conversation
     * @return
     */
    protected final CharSequence getConversationSnippet(
            Conversation conversation) {
        if (conversation == null) {
            return "";
        }
        if (GroupNoticeSqlManager.CONTACT_ID
                .equals(conversation.getSessionId())) {
            return GroupNoticeHelper
                    .getNoticeContent(conversation.getContent());
        }

        String fromNickName = "";
        if (conversation.getSessionId().toUpperCase().startsWith("G")) {
            if (conversation.getContactId() != null
                    && CCPAppManager.getClientUser() != null
                    && !conversation.getContactId().equals(
                    CCPAppManager.getClientUser().getUserId())) {
                ECContacts contact = ContactSqlManager.getContact(conversation
                        .getContactId());
                if (contact != null && contact.getNickname() != null) {
                    fromNickName = contact.getNickname() + ": ";
                } else {
                    fromNickName = conversation.getContactId() + ": ";
                }
            }
        }

        if (conversation.getMsgType() == ECMessage.Type.VOICE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_voice);
        } else if (conversation.getMsgType() == ECMessage.Type.FILE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_file);
        } else if (conversation.getMsgType() == ECMessage.Type.IMAGE.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_pic);
        } else if (conversation.getMsgType() == ECMessage.Type.VIDEO.ordinal()) {
            return fromNickName + mContext.getString(R.string.app_video);
        }
        return fromNickName + conversation.getContent();
    }

    /**
     * 根据消息发送状态处理
     *
     * @param context
     * @param conversation
     * @return
     */
    public static Drawable getChattingSnippentCompoundDrawables(
            Context context, Conversation conversation) {
        if (conversation.getSendStatus() == ECMessage.MessageStatus.FAILED
                .ordinal()) {
            return DemoUtils.getDrawables(context, R.drawable.msg_state_failed);
        } else if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING
                .ordinal()) {
            return DemoUtils
                    .getDrawables(context, R.drawable.msg_state_sending);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder mViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            view = View.inflate(mContext, R.layout.conversation_item, null);

            mViewHolder = new ViewHolder();
            mViewHolder.user_avatar = (ImageView) view
                    .findViewById(R.id.avatar_iv);
            mViewHolder.prospect_iv = (ImageView) view
                    .findViewById(R.id.avatar_prospect_iv);
            mViewHolder.nickname_tv = (EmojiconTextView) view
                    .findViewById(R.id.nickname_tv);
            mViewHolder.tipcnt_tv = (TextView) view
                    .findViewById(R.id.tipcnt_tv);
            mViewHolder.update_time_tv = (TextView) view
                    .findViewById(R.id.update_time_tv);
            mViewHolder.last_msg_tv = (EmojiconTextView) view
                    .findViewById(R.id.last_msg_tv);
            mViewHolder.image_input_text = (ImageView) view
                    .findViewById(R.id.image_input_text);
            mViewHolder.image_mute = (ImageView) view
                    .findViewById(R.id.image_mute);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }

        Conversation conversation = getItem(position);
        if (conversation != null) {
            // if(TextUtils.isEmpty(conversation.getUsername())) {
            // // mViewHolder.nickname_tv.setText(conversation.getSessionId());
            // } else {
            // mViewHolder.nickname_tv.setText(conversation.getUsername());
            // }
            ECContacts tempECContacts = null;
            for (int i = 0; i < ContactsCache.getInstance().getContacts()
                    .size(); i++) {
                if (ContactsCache.getInstance().getContacts().get(i)
                        .getContactid()
                        .equals(conversation.getSessionId() + "")) {
                    tempECContacts = ContactsCache.getInstance().getContacts()
                            .get(i);
                }
            }
            if (tempECContacts != null) {
                mViewHolder.nickname_tv.setText(tempECContacts.getNickname());
            } else {
                mViewHolder.nickname_tv.setText(conversation.getUsername());
                // view.setVisibility(View.GONE);
            }
            mViewHolder.last_msg_tv
                    .setText(getConversationSnippet(conversation));
            mViewHolder.last_msg_tv
                    .setCompoundDrawables(
                            getChattingSnippentCompoundDrawables(mContext,
                                    conversation), null, null, null);
            // 未读提醒设置
            setConversationUnread(mViewHolder, conversation);
            mViewHolder.image_input_text.setVisibility(View.GONE);
            mViewHolder.update_time_tv
                    .setText(getConversationTime(conversation));
            if (conversation.getSessionId().toUpperCase().startsWith("G")) {
                Bitmap bitmap = ContactLogic.getChatroomPhoto(conversation
                        .getSessionId());
                if (bitmap != null) {
                    mViewHolder.user_avatar.setImageBitmap(bitmap);
                    mViewHolder.user_avatar.setPadding(padding, padding,
                            padding, padding);
                    mViewHolder.user_avatar.setBackgroundColor(Color
                            .parseColor("#d5d5d5"));
                } else {
                    mViewHolder.user_avatar
                            .setImageResource(R.drawable.group_head);
                    mViewHolder.user_avatar.setPadding(0, 0, 0, 0);
                    mViewHolder.user_avatar.setBackgroundDrawable(null);
                }
            } else {
                mViewHolder.user_avatar.setBackgroundDrawable(null);
                if (conversation.getSessionId().equals(
                        GroupNoticeSqlManager.CONTACT_ID)) {
                    mViewHolder.user_avatar
                            .setImageResource(R.drawable.default_user_icon);
                } else {
                    ECContacts contact = ContactSqlManager
                            .getContact(conversation.getSessionId());
                    if (tempECContacts != null) {
                        ImageLoader.getInstance().displayImage(
                                AbsParam.getBaseUrl()
                                        + tempECContacts.getImgUrl(),
                                mViewHolder.user_avatar,
                                AppMain.initImageOptions(
                                        R.drawable.icon_touxiang_persion_gray,
                                        false));
                    } else {
                        mViewHolder.user_avatar.setImageBitmap(ContactLogic
                                .getPhoto(contact.getRemark()));
                    }
                }
            }
            mViewHolder.image_mute
                    .setVisibility(isNotice(conversation) ? View.GONE
                            : View.VISIBLE);
        }

        return view;
    }

    /**
     * 设置未读图片显示规则
     *
     * @param mViewHolder
     * @param conversation
     */
    private void setConversationUnread(ViewHolder mViewHolder,
                                       Conversation conversation) {
        String msgCount = conversation.getUnreadCount() > 100 ? "..." : String
                .valueOf(conversation.getUnreadCount());
        mViewHolder.tipcnt_tv.setText(msgCount);
        if (conversation.getUnreadCount() == 0) {
            mViewHolder.tipcnt_tv.setVisibility(View.GONE);
            mViewHolder.prospect_iv.setVisibility(View.GONE);
        } else if (conversation.isNotice()) {
            mViewHolder.tipcnt_tv.setVisibility(View.VISIBLE);
            mViewHolder.prospect_iv.setVisibility(View.GONE);
        } else {
            mViewHolder.prospect_iv.setVisibility(View.VISIBLE);
            mViewHolder.tipcnt_tv.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        ImageView user_avatar;
        TextView tipcnt_tv;
        ImageView prospect_iv;
        EmojiconTextView nickname_tv;
        TextView update_time_tv;
        EmojiconTextView last_msg_tv;
        ImageView image_input_text;
        ImageView image_mute;
    }

    @Override
    protected void initCursor() {
        notifyChange();
    }

    @Override
    protected void notifyChange() {
        if (mCallBackListener != null) {
            mCallBackListener.OnListAdapterCallBack();
        }
        Cursor cursor = ConversationSqlManager.getConversationCursor();
        setCursor(cursor);
        super.notifyDataSetChanged();
    }

    private boolean isNotice(Conversation conversation) {
        if (conversation.getSessionId().toLowerCase().startsWith("g")) {
            return conversation.isNotice();
        }
        return true;
    }

}
