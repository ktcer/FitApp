package com.cn.fit.ui.patient.others.myaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.personinfo.BeanOverage;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by junwei on 2015/12/15.
 */
public class AdapterCoinsHistory extends BaseAdapter {
    private List<BeanOverage> list;
    private Context mContext;
    private LayoutInflater inflater;
    private int positions;

    public AdapterCoinsHistory(List<BeanOverage> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(mContext);
        viewHolder holderList = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_overage, null);
            holderList = new viewHolder();
            holderList.consumptionName = (TextView) convertView
                    .findViewById(R.id.consumption_name);
            holderList.overages = (TextView) convertView
                    .findViewById(R.id.overage);
            holderList.detailedMoney = (TextView) convertView
                    .findViewById(R.id.detailed_money);
            holderList.consumptionTime = (TextView) convertView
                    .findViewById(R.id.consumption_time);
            convertView.setTag(holderList);
        }
        holderList = (viewHolder) convertView.getTag();
        BeanOverage beanOverage = list.get(position);
        holderList.consumptionName.setText(beanOverage.getItem());
        holderList.overages.setText(beanOverage.getDetail());
        holderList.consumptionTime.setText(beanOverage.getCreatetime());
        String detail = beanOverage.getDetail();
        // 匹配找到“支出“字符串
        Pattern p = Pattern.compile("支出");
        Matcher m = p.matcher(detail);
        if (!m.find()) {
            holderList.detailedMoney.setTextColor(mContext.getResources()
                    .getColor(R.color.lightgreen));
            holderList.detailedMoney.setText("+" + beanOverage.getAmount()
                    + "");
        } else {
            holderList.detailedMoney.setTextColor(mContext.getResources()
                    .getColor(R.color.orange));
            holderList.detailedMoney.setText("-" + beanOverage.getAmount()
                    + "");
        }
        return convertView;
    }

    class viewHolder implements Serializable {
        TextView consumptionName;
        TextView consumptionTime;
        TextView overages;
        TextView detailedMoney;
    }

}
