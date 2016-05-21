/**
 * @Title: MessageAdapter.java
 * @Package com.test.testim
 * @Description: TODO(鐢ㄤ竴鍙ヨ瘽鎻忚堪璇ユ枃浠跺仛浠?箞)
 * @author kuangtiecheng.Z
 * @date 2013-2-27 涓嬪崍12:05:50
 * @version V1.0
 */
package com.cn.fit.ui.patient.myres.mycustomer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    //	protected static final String TAG = "MessageAdapter";
    private Context context;
    private List<MessageVo> messageVo;


    public MessageAdapter(Context context, List<MessageVo> messageVo) {
        super();
        this.context = context;
        this.messageVo = messageVo;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messageVo.size();
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return messageVo.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        MessageVo message = messageVo.get(position);
        if (convertView == null || (holder = (ViewHolder) convertView.getTag()).flag != message.getDirection()) {
            holder = new ViewHolder();
            if (message.getDirection() == MessageVo.MESSAGE_FROM) {
                holder.flag = MessageVo.MESSAGE_FROM;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_from, null);
            } else {
                holder.flag = MessageVo.MESSAGE_TO;
                convertView = LayoutInflater.from(context).inflate(R.layout.item_to, null);
            }
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }
        holder.content.setText(message.getContent());
        holder.time.setText(message.getTime());
        return convertView;
    }

    static class ViewHolder {
        int flag;
        TextView content;
        TextView time;
    }

}
