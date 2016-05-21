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
package com.cn.fit.ui.chat.ui.chatting;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.cn.fit.ui.chat.common.CCPAppManager;
import com.cn.fit.ui.chat.common.utils.MediaPlayTools;
import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.storage.ImgInfoSqlManager;
import com.cn.fit.ui.chat.ui.chatting.model.ViewHolderTag;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

/**
 * 处理聊天消息点击事件响应
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class ChattingListClickListener implements View.OnClickListener {

    /**
     * 聊天界面
     */
    private ChattingActivity mContext;

    public ChattingListClickListener(ChattingActivity activity, String userName) {
        mContext = activity;
    }

    @Override
    public void onClick(View v) {
        ViewHolderTag holder = (ViewHolderTag) v.getTag();
        ECMessage iMessage = holder.detail;

        switch (holder.type) {
            case ViewHolderTag.TagType.TAG_VIEW_FILE:
                ECFileMessageBody body = (ECFileMessageBody) holder.detail.getBody();
                CCPAppManager.doViewFilePrevieIntent(mContext, body.getLocalUrl());
                break;

            case ViewHolderTag.TagType.TAG_VOICE:
                if (iMessage == null) {
                    return;
                }
                MediaPlayTools instance = MediaPlayTools.getInstance();
                final ChattingListAdapter adapterForce = mContext.getChattingAdapter();
                if (instance.isPlaying()) {
                    instance.stop();
                }
                if (adapterForce.mVoicePosition == holder.position) {
                    adapterForce.mVoicePosition = -1;
                    adapterForce.notifyDataSetChanged();
                    return;
                }

                instance.setOnVoicePlayCompletionListener(new MediaPlayTools.OnVoicePlayCompletionListener() {

                    @Override
                    public void OnVoicePlayCompletion() {
                        adapterForce.mVoicePosition = -1;
                        adapterForce.notifyDataSetChanged();
                    }
                });
                ECVoiceMessageBody voiceBody = (ECVoiceMessageBody) holder.detail.getBody();
                String fileLocalPath = voiceBody.getLocalUrl();
                instance.playVoice(fileLocalPath, false);
                adapterForce.setVoicePosition(holder.position);
                adapterForce.notifyDataSetChanged();

                break;

            case ViewHolderTag.TagType.TAG_VIEW_PICTURE:
                if (iMessage != null) {

                    List<String> msgids = IMessageSqlManager.getImageMessageIdSession(mContext.getmThread());
                    if (msgids == null || msgids.isEmpty()) {
                        return;
                    }
                    int position = 0;
                    ArrayList<ViewImageInfo> urls = (ArrayList<ViewImageInfo>) ImgInfoSqlManager.getInstance().getViewImageInfos(msgids);
                    msgids.clear();
                    if (urls == null || urls.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < urls.size(); i++) {
                        if (urls.get(i) != null && urls.get(i).getMsgLocalId().equals(iMessage.getMsgId())) {
                            position = i;
                            break;
                        }
                    }
                    CCPAppManager.startChattingImageViewAction(mContext, position, urls);
                }
                break;

            case ViewHolderTag.TagType.TAG_RESEND_MSG:

                mContext.doResendMsgRetryTips(iMessage, holder.position);
                break;
            default:
                break;
        }
    }


}
