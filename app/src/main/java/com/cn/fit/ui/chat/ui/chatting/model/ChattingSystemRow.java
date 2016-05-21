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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.SystemViewHolder;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;


/**
 * <p>Title: ChattingSystem.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-5-10
 */
public class ChattingSystemRow extends BaseChattingRow {

    public ChattingSystemRow(int type) {
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        // we have a don't have a converView so we'll have to create a new one
        if (convertView == null || convertView.getTag() == null || ((BaseHolder) convertView.getTag()).getType() != mRowType) {
            convertView = inflater.inflate(R.layout.chatting_item_system, null);

            // use the view holder pattern to save of already looked up subviews
            SystemViewHolder holder = new SystemViewHolder(mRowType);
            holder.setChattingTime((TextView) convertView.findViewById(R.id.chatting_time_tv));
            holder.mSystemView = (TextView) convertView.findViewById(R.id.chatting_content_itv);
            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  ECMessage detail, int position) {

        SystemViewHolder holder = (SystemViewHolder) baseHolder;
        // actually setup the view
        ECMessage iMessage = detail;
        if (iMessage != null) {
            ECTextMessageBody textBody = (ECTextMessageBody) iMessage.getBody();
            holder.mSystemView.setText(textBody.getMessage());
            holder.mSystemView.invalidate();
        }
    }


    @Override
    public int getChatViewType() {

        return ChattingRowType.CHATTING_SYSTEM.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {

        return false;
    }
}
