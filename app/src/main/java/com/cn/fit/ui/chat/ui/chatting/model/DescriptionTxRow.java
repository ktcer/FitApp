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
import android.text.method.LinkMovementMethod;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.DescriptionViewHolder;
import com.cn.fit.ui.chat.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;


/**
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public class DescriptionTxRow extends BaseChattingRow {

    public DescriptionTxRow(int type) {
        super(type);
    }

    /* (non-Javadoc)
     * @see com.hisun.cas.model.im.ChattingRow#buildChatView(android.view.LayoutInflater, android.view.View)
     */
    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null || ((BaseHolder) convertView.getTag()).getType() != mRowType) {

            convertView = new ChattingItemContainer(inflater, R.layout.chatting_item_to);

            //use the view holder pattern to save of already looked up subviews
            DescriptionViewHolder holder = new DescriptionViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  ECMessage msg, int position) {
        DescriptionViewHolder holder = (DescriptionViewHolder) baseHolder;
        if (msg != null) {
            ECTextMessageBody textBody = (ECTextMessageBody) msg.getBody();
            holder.getDescTextView().setText(textBody.getMessage());
            holder.getDescTextView().setMovementMethod(LinkMovementMethod.getInstance());
            OnClickListener onClickListener = ((ChattingActivity) context).getChattingAdapter().getOnClickListener();
            getMsgStateResId(position, holder, msg, onClickListener);

            // ((ChattingActivity) context).registerForContextMenu(holder.getDescTextView());
        }
    }


    @Override
    public int getChatViewType() {
        return ChattingRowType.DESCRIPTION_ROW_TRANSMIT.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {

        return false;
    }


}
