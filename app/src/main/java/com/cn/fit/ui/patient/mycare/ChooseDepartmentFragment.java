package com.cn.fit.ui.patient.mycare;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuangtiecheng 用来实现二级菜单侧滑的fragment
 */
public class ChooseDepartmentFragment extends Fragment {
    private View Mview;
    private ListView firstLV;
    private ListView secondLV;
    private List<String> departmentList;
    private TranslateAnimation showAnimation = null; // 滑动动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mview = inflater.inflate(R.layout.fragment_choose_department,
                container, false);
        initView();
        return Mview;
    }

    private void initView() {
        showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        showAnimation.setDuration(500);
        firstLV = (ListView) Mview.findViewById(R.id.department_first_lv);
        fillFirstList();
        firstLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                fillSecondData(arg2);
                secondLV.startAnimation(showAnimation);
                secondLV.setVisibility(View.VISIBLE);
            }
        });
        secondLV = (ListView) Mview.findViewById(R.id.department_second_lv);
        secondLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }
        });
    }

    private void fillFirstList() {
        departmentList = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            departmentList.add("第一级菜单");
        }
        firstLV.setAdapter(new CommonAdapter(getActivity(), departmentList));
    }

    private void fillSecondData(int position) {
        List<String> secondList = new ArrayList<String>();
        switch (position) {
            case 0:
                for (int i = 0; i < 5; i++) {
                    secondList.add("二级菜单1");
                }
                break;
            case 1:
                for (int i = 0; i < 6; i++) {
                    secondList.add("二级菜单2");
                }
                break;
            case 2:
                for (int i = 0; i < 7; i++) {
                    secondList.add("二级菜单3");
                }
                break;
            case 3:
                for (int i = 0; i < 8; i++) {
                    secondList.add("二级菜单4");
                }
                break;
        }
        secondLV.setAdapter(new CommonAdapter(getActivity(), secondList));
    }

    private class CommonAdapter extends BaseAdapter {
        List<String> list;
        Context context;

        public CommonAdapter(Context context, List<String> list) {
            this.list = list;
            this.context = context;
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.simple_item, null);
                holder.name = (TextView) convertView
                        .findViewById(R.id.simple_item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(list.get(position));
            return convertView;
        }

    }

    private class ViewHolder {
        TextView name;
    }
}
