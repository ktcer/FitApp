/**
 * created by Mr.Simple, Sep 1, 20142:17:05 PM.
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
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthpost.BeanHealthPost;
import com.cn.fit.ui.AppMain;
import com.cn.fit.util.AbsParam;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author kuangtiecheng
 */
public class GridViewAdapter extends BaseAdapter {

    Context mContext;
    List<BeanHealthPost> mListBeanHealthPost;

    public GridViewAdapter(Context context,
                           List<BeanHealthPost> listBeanHealthPost) {
        mContext = context;
        mListBeanHealthPost = listBeanHealthPost;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BeanHealthPost getItem(int position) {
        return mListBeanHealthPost.get(position);
    }

    @Override
    public int getCount() {
        return mListBeanHealthPost.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gridview_content, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            holder.decs = (TextView) convertView.findViewById(R.id.desc);
            holder.newPrice = (TextView) convertView
                    .findViewById(R.id.newprice);
            holder.oldPrice = (TextView) convertView
                    .findViewById(R.id.oldprice);
            // holder.linearLayoutpost = (LinearLayout) convertView
            // .findViewById(R.id.item_post);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.decs.setSingleLine(false);
        holder.decs.setLines(2);
        holder.decs.setTextSize(14);
        holder.decs.setText("【" + getItem(position).getTag() + "】"
                + getItem(position).getActivetime() + " "
                + getItem(position).getTitle());
        holder.newPrice.setText("¥" + getItem(position).getFinalprice());
        holder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.oldPrice.setText("¥" + getItem(position).getOriprice());
        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl() + getItem(position).getCover(),
                holder.imageView,
                AppMain.initImageOptions(R.drawable.default_life_icon, true));

        return convertView;
    }

    private class ViewHolder {
        public ImageView imageView;
        public TextView decs;
        public TextView newPrice;
        public TextView oldPrice;

    }

}
