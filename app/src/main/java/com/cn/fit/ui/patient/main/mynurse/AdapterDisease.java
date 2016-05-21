package com.cn.fit.ui.patient.main.mynurse;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanDiseaseInfo;


public class AdapterDisease extends BaseAdapter {
    private List<BeanDiseaseInfo> mList;
    private Context mContext;

    public AdapterDisease(Context pContext, List<BeanDiseaseInfo> pList) {
        // TODO Auto-generated constructor stub
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
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
        BeanDiseaseInfo diseaseInfo = mList.get(position);
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.imformation_item, null);
        if (convertView != null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.items_info);
        }
        holder.name.setText(diseaseInfo.getDiseaseName());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
