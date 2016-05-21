/**
 * created by Mr.Simple, Sep 1, 20142:23:04 PM.
 * Copyright (c) 2014, hehonghui@umeng.com All Rights Reserved.
 * <p/>
 * #####################################################
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 * #####################################################
 */

package com.cn.fit.utils.listviewwithgridview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.patient.main.healthpost.healthpost.ActivityHealthPostDetail;
import com.cn.fit.ui.patient.main.healthpost.healthpost.ActivityHealthPostList;
import com.cn.fit.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuangtiecheng
 */
public class ListWithGridViewAdapter extends BaseAdapter {

    List<ListViewItem> mListViewItems = new ArrayList<ListViewItem>();
    LayoutInflater mInflater;
    Context mContext;

    public ListWithGridViewAdapter(Context context, List<ListViewItem> items) {
        mContext = context;
        mListViewItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListViewItems.size();
    }

    @Override
    public ListViewItem getItem(int position) {
        return mListViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.title_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mGridView = (MGridView) convertView.findViewById(R.id.my_gridview);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.mMore = (TextView) convertView.findViewById(R.id.much_post_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ListViewItem item = getItem(position);
        // 设置GridView的Adapter
        viewHolder.mGridView.setAdapter(new GridViewAdapter(mContext, item.listBeanHealthPost));
        // 计算GridView宽度, 设置默认为numColumns为3.
        viewHolder.mTextView.setText(item.mTitle);
        viewHolder.mMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, ActivityHealthPostList.class);
                mContext.startActivity(intent);
            }
        });
        viewHolder.mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Intent intent = new Intent(mContext, ActivityHealthPostDetail.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(Constant.TOUR_BEAN, mListViewItems.get(position).listBeanHealthPost.get(i));
                intent.putExtras(mBundle);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

    /**
     * @author kuangtiecheng
     */
    static class ViewHolder {
        MGridView mGridView;
        TextView mTextView;
        TextView mMore;
    }

}
