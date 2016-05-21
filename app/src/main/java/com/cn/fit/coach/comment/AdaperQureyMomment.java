package com.cn.fit.coach.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.customer.BeanQureyMomment;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.util.AbsParam;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ktcer on 2016/1/12.
 */
public class AdaperQureyMomment extends BaseAdapter {

    private Context context;
    private List<BeanQureyMomment> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private InterfaceIsfavoriteListener l;
    private boolean flag;

    public AdaperQureyMomment(Context context, List<BeanQureyMomment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setIsfavoriteListener(InterfaceIsfavoriteListener l) {
        this.l = l;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.coach_momment_item, parent, false);
            holder.user_avatar = (ImageView) convertView
                    .findViewById(R.id.avatar_iv);
            holder.prospect_iv = (ImageView) convertView
                    .findViewById(R.id.avatar_prospect_iv);
            holder.nickname_tv = (EmojiconTextView) convertView
                    .findViewById(R.id.nickname_tv);
            holder.tipcnt_tv = (TextView) convertView
                    .findViewById(R.id.tipcnt_tv);
            holder.update_time_tv = (TextView) convertView
                    .findViewById(R.id.update_time_tv);
            holder.last_msg_tv = (EmojiconTextView) convertView
                    .findViewById(R.id.last_msg_tv);
            holder.image_input_text = (ImageView) convertView
                    .findViewById(R.id.image_input_text);
            holder.image_mute = (ImageView) convertView
                    .findViewById(R.id.image_mute);

            convertView.setTag(holder);

        }
//        else {
        holder = (ViewHolder) convertView.getTag();
        holder.nickname_tv.setText(list.get(position).getPhone());
        holder.last_msg_tv
                .setText(list.get(position).getComments());
        holder.image_input_text.setVisibility(View.GONE);
        holder.update_time_tv
                .setText(list.get(position).getTime());
        holder.image_mute
                .setVisibility(View.INVISIBLE);


        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl() + list.get(position).getUserUrl(),
                holder.user_avatar,
                AppMain.initImageOptions(R.drawable.ic_user_default,
                        false));

//        }
        return convertView;

    }

    //public  void  setfavoriteclickListener(){
//
//}
    public interface InterfaceIsfavoriteListener {

        void getDone(int positione);

    }

    public class ViewHolder {
        ImageView user_avatar;
        TextView tipcnt_tv;
        ImageView prospect_iv;
        EmojiconTextView nickname_tv;
        TextView update_time_tv;
        EmojiconTextView last_msg_tv;
        ImageView image_input_text;
        ImageView image_mute;

    }
}
