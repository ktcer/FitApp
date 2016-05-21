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
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.FileRowViewHolder;
import com.cn.fit.ui.chat.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;


/**
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public class FileTxRow extends BaseChattingRow {

    public FileTxRow(int type) {
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null || convertView.getTag() == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.chatting_item_file_to);

            //use the view holder pattern to save of already looked up subviews
            FileRowViewHolder holder = new FileRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }


    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  ECMessage detail, int position) {

        FileRowViewHolder holder = (FileRowViewHolder) baseHolder;
        if (detail != null) {
            ECMessage message = detail;
            String userData = message.getUserData();
            ECFileMessageBody fileBody = (ECFileMessageBody) message.getBody();
            if (TextUtils.isEmpty(userData)) {
                holder.contentTv.setText(fileBody.getFileName());
            } else {
                String fileName = userData.substring(userData.indexOf("fileName=") + "fileName=".length(), userData.length());
                holder.contentTv.setText(fileName);
            }
            OnClickListener onClickListener = ((ChattingActivity) context).getChattingAdapter().getOnClickListener();
            ViewHolderTag holderTag = ViewHolderTag.createTag(detail, ViewHolderTag.TagType.TAG_VIEW_FILE, position);
            holder.contentTv.setTag(holderTag);
            holder.contentTv.setOnClickListener(onClickListener);
            getMsgStateResId(position, holder, detail, onClickListener);

        }
    }

    @Override
    public int getChatViewType() {

        return ChattingRowType.FILE_ROW_TRANSMIT.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {
        // TODO Auto-generated method stub
        return false;
    }

}
