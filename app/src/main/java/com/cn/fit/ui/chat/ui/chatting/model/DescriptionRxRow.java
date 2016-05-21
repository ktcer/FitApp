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
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.cn.fit.ui.chat.ui.chatting.holder.DescriptionViewHolder;
import com.cn.fit.ui.chat.ui.chatting.view.ChattingItemContainer;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityEvaluationSecond;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;


/**
 * <p>Title: DescriptionRxRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public class DescriptionRxRow extends BaseChattingRow {


    public DescriptionRxRow(int type) {
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.chatting_item_from);


            //use the view holder pattern to save of already looked up subviews
            DescriptionViewHolder holder = new DescriptionViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public void buildChattingData(final Context context, BaseHolder baseHolder,
                                  ECMessage detail, int position) {

        DescriptionViewHolder holder = (DescriptionViewHolder) baseHolder;
        ECMessage message = detail;
        if (message != null) {
            ECTextMessageBody textBody = (ECTextMessageBody) message.getBody();
            if (textBody.getMessage().toString().contains("身体状况评估")) {
                holder.getDescTextView().setText(getClickableSpan(context, textBody.getMessage()));
            } else {
                holder.getDescTextView().setText(textBody.getMessage());
            }
            holder.getDescTextView().setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    //设置超链接文字
    private SpannableString getClickableSpan(final Context context, String str) {
        SpannableString spanStr = new SpannableString(str);
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), str.indexOf("身体状况评估"), str.indexOf("身体状况评估") + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                context.startActivity(new Intent(context, ActivityEvaluationSecond.class));
            }
        }, str.indexOf("身体状况评估"), str.indexOf("身体状况评估") + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), str.indexOf("身体状况评估"), str.indexOf("身体状况评估") + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    @Override
    public int getChatViewType() {

        return ChattingRowType.DESCRIPTION_ROW_RECEIVED.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {

        return false;
    }
}
