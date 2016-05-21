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

import java.io.File;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.VoiceRowViewHolder;
import com.cn.fit.ui.chat.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;

/**
 * <p>Title: VoiceTxRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public class VoiceTxRow extends BaseChattingRow {

    public VoiceTxRow(int type) {
        super(type);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.model.im.ChattingRow#buildChatView(android.view.LayoutInflater, android.view.View)
     */
    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.chatting_item_to_voice);

            //use the view holder pattern to save of already looked up subviews
            VoiceRowViewHolder holder = new VoiceRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  final ECMessage detail, int position) {

        final VoiceRowViewHolder holder = (VoiceRowViewHolder) baseHolder;
        holder.voiceAnim.setVoiceFrom(false);
        if (detail != null) {
            if (detail.getMsgStatus() == ECMessage.MessageStatus.SENDING) {
                holder.voiceSending.setVisibility(View.VISIBLE);
            } else {
                holder.voiceSending.setVisibility(View.GONE);
            }

            File file = new File(((ECFileMessageBody) detail.getBody()).getLocalUrl());
            long length = file.length();

            VoiceRowViewHolder.initVoiceRow(holder, detail, position, (ChattingActivity) context, false);
            OnClickListener onClickListener = ((ChattingActivity) context).getChattingAdapter().getOnClickListener();
            getMsgStateResId(position, holder, detail, onClickListener);
        }
    }


    @Override
    public int getChatViewType() {
        // return type
        return ChattingRowType.VOICE_ROW_TRANSMIT.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {
        // TODO Auto-generated method stub
        return false;
    }

}
