package com.cn.fit.ui.patient.main.healthdiary.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.util.CircleImageView;

import java.util.ArrayList;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/4 上午9:09:47
 * @parameter
 * @return
 */
public class DiaryRemindAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<BeanHealthDiaryLocal> list;

    public DiaryRemindAdapter(Context context,
                              ArrayList<BeanHealthDiaryLocal> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.activity_remindlist_item, null);

            holder.account = (TextView) convertView.findViewById(R.id.account);
            holder.tv_remain_period = (TextView) convertView
                    .findViewById(R.id.tv_remain_period);
            holder.avatar_iv = (CircleImageView) convertView
                    .findViewById(R.id.avatar_iv);
            holder.name_tv = (EmojiconTextView) convertView
                    .findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.account.setText("星期" + list.get(position).getDateday().replaceAll("星期", ""));
        holder.tv_remain_period.setText("" + list.get(position).getDaytime());
        if (list.get(position).getContent().startsWith("20"))
            holder.name_tv.setText("爱护，音频提醒");
        else
            holder.name_tv.setText(list.get(position).getContent());
        String path = list.get(position).getPath();
        if (path != null && path.endsWith(".mp4")) {
            holder.avatar_iv.setImageResource(R.drawable.video_remind);
        } else if (path != null && path.endsWith(".wav")) {
            holder.avatar_iv.setImageResource(R.drawable.voice_remind);
        }
        return convertView;
    }
}

class ViewHolder {
    public TextView account, tv_remain_period;
    public CircleImageView avatar_iv;
    public EmojiconTextView name_tv;
}
