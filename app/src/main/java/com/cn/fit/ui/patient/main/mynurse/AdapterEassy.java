package com.cn.fit.ui.patient.main.mynurse;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanEassy;

public class AdapterEassy extends BaseAdapter {
    private Context context;
    private List<BeanEassy> eassyList;//BeanEassy

    public AdapterEassy(Context context, List<BeanEassy> eassyLis) {
        super();
        this.context = context;
        this.eassyList = eassyLis;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eassyList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return eassyList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.eassy_item, null);
            holder.Title = (TextView) convertView
                    .findViewById(R.id.eassy_title_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Title.setText(eassyList.get(position).getEssayTitle());
        return convertView;
    }

    private class ViewHolder {
        TextView Title;
    }

}
