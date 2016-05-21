/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.contact.ContactDetailActivity;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.HashMap;


/**
 * <p>Title: BaseChattingRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public abstract class BaseChattingRow implements IChattingRow {

    public static final String TAG = LogUtil.getLogUtilsTag(BaseChattingRow.class);
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    int mRowType;

    public BaseChattingRow(int type) {
        mRowType = type;
    }

    /**
     * 处理消息的发送状态设置
     *
     * @param position 消息的列表所在位置
     * @param holder   消息ViewHolder
     * @param l
     */
    protected static void getMsgStateResId(int position, BaseHolder holder, ECMessage msg, View.OnClickListener l) {
        if (msg != null && msg.getDirection() == ECMessage.Direction.SEND) {
            ECMessage.MessageStatus msgStatus = msg.getMsgStatus();
            if (msgStatus == ECMessage.MessageStatus.FAILED) {
                holder.getUploadState().setImageResource(R.drawable.msg_state_failed_resend);
                holder.getUploadState().setVisibility(View.VISIBLE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
            } else if (msgStatus == ECMessage.MessageStatus.SUCCESS || msgStatus == ECMessage.MessageStatus.RECEIVE) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }

            } else if (msgStatus == ECMessage.MessageStatus.SENDING) {
                holder.getUploadState().setImageResource(0);
                holder.getUploadState().setVisibility(View.GONE);
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.VISIBLE);
                }

            } else {
                if (holder.getUploadProgressBar() != null) {
                    holder.getUploadProgressBar().setVisibility(View.GONE);
                }
                LogUtil.d(TAG, "getMsgStateResId: not found this state");
            }

            ViewHolderTag holderTag = ViewHolderTag.createTag(msg, ViewHolderTag.TagType.TAG_RESEND_MSG, position);
            holder.getUploadState().setTag(holderTag);
            holder.getUploadState().setOnClickListener(l);
        }
    }

    /**
     * @param contextMenu
     * @param targetView
     * @param detail
     * @return
     */
    public abstract boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, ECMessage detail);


    /**
     * @param baseHolder
     * @param displayName
     */
    public static void setDisplayName(BaseHolder baseHolder, String displayName) {
        if (baseHolder == null || baseHolder.getChattingUser() == null) {
            return;
        }

        if (TextUtils.isEmpty(displayName)) {
            baseHolder.getChattingUser().setVisibility(View.GONE);
            return;
        }
        baseHolder.getChattingUser().setText(displayName);
        baseHolder.getChattingUser().setVisibility(View.VISIBLE);
    }

    protected abstract void buildChattingData(Context context, BaseHolder baseHolder, ECMessage detail, int position);

    @Override
    public void buildChattingBaseData(Context context, BaseHolder baseHolder, ECMessage detail, int position) {

        // 处理其他使用逻辑
        buildChattingData(context, baseHolder, detail, position);
        setContactPhoto(context, baseHolder, detail);
        if (((ChattingActivity) context).isPeerChat() && detail.getDirection() == ECMessage.Direction.RECEIVE) {
            ECContacts contact = ContactSqlManager.getContact(detail.getForm());
            if (contact != null) {
                if (TextUtils.isEmpty(contact.getNickname())) {
                    contact.setNickname(contact.getContactid());
                }
                setDisplayName(baseHolder, contact.getNickname());
            } else {
                setDisplayName(baseHolder, detail.getForm());
            }
        }
        setContactPhotoClickListener(context, baseHolder, detail);
    }

    private void setContactPhotoClickListener(final Context context, BaseHolder baseHolder, final ECMessage detail) {
        if (baseHolder.getChattingAvatar() != null && detail != null) {
            baseHolder.getChattingAvatar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(CCPAppManager.getClientUser().getUserId().equals(detail.getForm())) {
                        context.startActivity(new Intent(context, ActivitySettings.class));
                        return ;
                    }*/
                    ECContacts contact = ContactSqlManager.getContact(detail.getForm());
                    if (contact == null || contact.getId() == -1) {
                        return;
                    }
//                    Intent intent = new Intent(context, ContactDetailActivity.class);
//                    intent.putExtra(ContactDetailActivity.RAW_ID, contact.getId());
//                    context.startActivity(intent);
                    UtilsSharedData.initDataShare(context);
                    long userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
                    Intent intent = new Intent(context, ContactDetailActivity.class);
                    if ((userId + "0").equals(detail.getForm())) {
                        //传递本机用户的数据
                        //自己的头像不能点击！！！！
//                        intent.putExtra(ContactDetailActivity.MOBILE, detail.getForm());
//                        intent.putExtra(ContactDetailActivity.DISPLAY_NAME, UtilsSharedData.getValueByKey(Constant.USER_NAME));
                    } else {
                        //传递保健秘书的相关数据
                        intent.putExtra(ContactDetailActivity.MOBILE, ((ChattingActivity) context).getmRecipients());
                        intent.putExtra(ContactDetailActivity.DISPLAY_NAME, ((ChattingActivity) context).getmUsername());
                        intent.putExtra(ContactDetailActivity.IMAGEURL, ((ChattingActivity) context).getmUserImgUrl());
                        intent.putExtra(ContactDetailActivity.REMAIN_TIME, ((ChattingActivity) context).getmRemainTime());
                        context.startActivity(intent);
                    }

                }
            });
        }
    }


    /**
     * 添加用户头像
     *
     * @param baseHolder
     * @param detail
     */
    private void setContactPhoto(Context mContext, BaseHolder baseHolder, ECMessage detail) {
        if (baseHolder.getChattingAvatar() != null) {
            try {
                if (TextUtils.isEmpty(detail.getForm())) {
                    return;
                }
                UtilsSharedData.initDataShare(mContext);
                long userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
//                String userUin = "";
//                if (hashMap.containsKey(detail.getForm())) {
//                    userUin = hashMap.get(detail.getForm());
//                } else {
//                    userUin = ContactSqlManager.getContact(detail.getForm())
//                            .getRemark();
//                }

                String imgUrl = "";
                if ((userId + "0").equals(detail.getForm())) {
                    //显示本地用户的头像
                    imgUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
                } else {
                    //显示秘书用户的头像
                    imgUrl = ((ChattingActivity) mContext).getmUserImgUrl();
                }
                ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + imgUrl,
                        baseHolder.getChattingAvatar(),
                        AppMain.initImageOptions(R.drawable.default_user_icon, false));
//                baseHolder.getChattingAvatar().setImageBitmap(
//                        ContactLogic.getPhoto(userUin));
            } catch (Exception e) {
            }
        }
    }

}
