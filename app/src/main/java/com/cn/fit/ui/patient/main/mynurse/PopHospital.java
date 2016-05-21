package com.cn.fit.ui.patient.main.mynurse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.nurse.BeanHospitaList;

public class PopHospital {
    private List<BeanHospitaList> itemList;
    private Context context;
    private PopupWindow mPopupWindow;
    private ListView mListView;
    private LinearLayout transLayout;

    public PopHospital(Context context, List<BeanHospitaList> itemList) {
        this.context = context;
        this.itemList = itemList;
        itemList = new ArrayList<BeanHospitaList>();
        View view = LayoutInflater.from(context).inflate(
                R.layout.pop_menu_layout, null);

        mListView = (ListView) view.findViewById(R.id.pop_menu_lv);
        mListView.setAdapter(new popAdapter());
        transLayout = (LinearLayout) view.findViewById(R.id.transLayout);
        // mPopupWindow = new PopupWindow(view, 300, LayoutParams.WRAP_CONTENT);
        mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListView.setOnItemClickListener(clickListener);
    }

    public void setOnClickListener(OnClickListener clickListerner) {
        transLayout.setOnClickListener(clickListerner);
        mPopupWindow.dismiss();
    }

    // public void addItems(String[] item) {
    // for (String s : item) {
    // itemList.add(s);
    // }
    // }

    public void addItem(BeanHospitaList item) {
        itemList.add(item);
    }

    public void showAsDropDown(View parent, int offsetX, int offsetY) {
        mPopupWindow.showAsDropDown(parent, offsetX, offsetY);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private class popAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHodler holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.pop_menu_item_layout, null);
                holder = new ViewHodler();
                holder.menuItem = (TextView) convertView
                        .findViewById(R.id.pop_menu_item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHodler) convertView.getTag();
            }
            holder.menuItem.setText(itemList.get(position).getHospitalName());
            return convertView;
        }

    }

    private class ViewHodler {
        TextView menuItem;
    }
}
