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
package com.cn.fit.ui.chat.ui.chatting.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.EmoticonUtil;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.ui.chatting.model.CCPEmoji;


/**
 * <p>Title: EmojiApapter.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class EmojiApapter extends BaseAdapter {


    ArrayList<CCPEmoji> emojis;

    LayoutInflater mInflater;

    public EmojiApapter(Context context) {

        mInflater = LayoutInflater.from(context);
        ;
    }


    @Override
    public int getCount() {

        if (emojis != null && emojis.size() > 0) {
            return this.emojis.size() + 1;
        }

        return 0;
    }


    @Override
    public Object getItem(int position) {

        if (emojis != null && (position <= (emojis.size() - 1))) {
            return emojis.get(position);
        }

        return null;
    }


    @Override
    public long getItemId(int position) {

        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.emoji_item, null);
            viewHolder.emoji_icon = (ImageView) convertView.findViewById(R.id.emoji_id);
            //viewHolder.emoji_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (getCount() - 1 == position) {
            viewHolder.emoji_icon.setImageResource(R.drawable.emoji_del_selector);
        } else {

            CCPEmoji emoji = (CCPEmoji) getItem(position);
            if (emoji != null) {
                if (emoji.getId() == R.drawable.emoji_del_selector) {
                    convertView.setBackgroundDrawable(null);
                    viewHolder.emoji_icon.setImageResource(emoji.getId());
                } else if (TextUtils.isEmpty(emoji.getEmojiDesc())) {
                    convertView.setBackgroundDrawable(null);
                    viewHolder.emoji_icon.setImageDrawable(null);
                } else {
                    viewHolder.emoji_icon.setTag(emoji);
                    viewHolder.emoji_icon.setImageResource(emoji.getId());
                }
            }
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView emoji_icon;
    }

    public void release() {
        if (emojis != null) {
            emojis.clear();
            emojis = null;
        }
        mInflater = null;
    }

    /**
     * @param emojis
     */
    public void updateEmoji(ArrayList<CCPEmoji> emojis) {
        this.emojis = emojis;
        if (this.emojis == null) {
            emojis = new ArrayList<CCPEmoji>();
            LogUtil.e(LogUtil.getLogUtilsTag(EmoticonUtil.class), "EmojiApapter.updateEmoji get emoji list fail, new one");
        }
        notifyDataSetChanged();
    }
}