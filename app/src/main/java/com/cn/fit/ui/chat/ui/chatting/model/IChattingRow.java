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
import android.view.LayoutInflater;
import android.view.View;

import com.cn.fit.ui.chat.ui.chatting.holder.BaseHolder;
import com.yuntongxun.ecsdk.ECMessage;


/**
 * <p>Title: ChattingRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-16
 */
public interface IChattingRow {

    /**
     * Get a View that displays the data at the specified position in the data set
     *
     * @param convertView
     * @return
     */
    View buildChatView(LayoutInflater inflater, View convertView);

    /**
     * @param context
     * @param detail
     */
    void buildChattingBaseData(Context context, BaseHolder baseHolder, ECMessage detail, int position);

    /**
     * @return
     */
    int getChatViewType();

}
