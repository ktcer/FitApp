/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui.chatting.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.chat.common.utils.DensityUtil;
import com.cn.fit.ui.chat.ui.chatting.Capability;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class AppGrid extends GridView implements
        AdapterView.OnItemClickListener {

    /**
     *
     */
    private Context mContext;

    private List<Capability> mCapabilities;

    /**
     *
     */
    private AppGirdApapter mGirdApapter;

    /**
     *
     */
    private OnCapabilityItemClickListener mOnCapabilityItemClickListener;

    /**
     * The current panel tag No.
     */
    private int mPanelIndex;

    /**
     * The total count of Panel members
     */
    private int mItems;

    /**
     * The count of single Panel members
     */
    private int mPageItems;

    /**
     * The count of Panel index.
     */
    private int mPanelPageCount;

    /**
     * ccp capability items that reserve.
     */
    private int mReserveItems;


    public AppGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public AppGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public AppGrid(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * @param position
     * @param items
     * @param pageItems
     * @param pageCount
     * @param column
     * @param reserve
     */
    public void setAppPanelBase(int position, int items, int pageItems, int pageCount, int column, int reserve) {
        mPanelIndex = position;
        mItems = items;
        mPageItems = pageItems;
        mPanelPageCount = pageCount;
        mReserveItems = reserve;
        setNumColumns(column);
    }

    /**
     * @param capabilities
     */
    public void setAppPanelItems(List<Capability> capabilities) {

        mGirdApapter = new AppGirdApapter(getContext(), capabilities);
        setBackgroundResource(0);
        setAdapter(mGirdApapter);
        setOnItemClickListener(this);

        int left = DensityUtil.getMetricsDensity(mContext, 10.0F);
        int top = DensityUtil.getMetricsDensity(mContext, 6.0F);
        setPadding(left, top, left, 0);
    }

    public int getCount() {
        return this.mGirdApapter.getCount() - 1;
    }

    /**
     * @param verticalSpacing
     */
    public final void setPanelVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing <= 0) {
            return;
        }
        int padding = DensityUtil.getMetricsDensity(mContext, 10.0F);
        setPadding(padding, verticalSpacing, padding, 0);
        setVerticalSpacing(verticalSpacing / 2);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (mGirdApapter != null && position <= mGirdApapter.getCount() - 1) {
            Capability capability = (Capability) mGirdApapter.getItem(position);
            if (mOnCapabilityItemClickListener != null) {
                mOnCapabilityItemClickListener.onPanleItemClick(position, capability.getId(), capability.getCapabilityName());
            }
        }
    }

    /**
     * Register a callback to be invoked when an item in this {@link com.cn.fit.ui.chat.ui.chatting.view.AppGrid} View has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnCapabilityItemClickListener(OnCapabilityItemClickListener listener) {
        mOnCapabilityItemClickListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    public interface OnCapabilityItemClickListener {

        /**
         * Callback method to be invoked when an item in this EmojiGird View has
         * been clicked.
         *
         * @param index
         * @param capabilityName
         */
        void onPanleItemClick(int index, int capabilityId, String capabilityName);

    }

    private class AppGirdApapter extends BaseAdapter {

        private List<Capability> mLists;
        private int width;
        private int height;

        private LayoutInflater mLayoutInflater;

        public AppGirdApapter(Context context, List<Capability> lists) {

            mLists = lists;
            width = DensityUtil.getMetricsDensity(context, 64.0F);
            height = DensityUtil.getMetricsDensity(context, 53.299999F);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return mItems;
        }

        @Override
        public Object getItem(int position) {

            return mLists.get(position);
        }


        @Override
        public long getItemId(int position) {

            return 0L;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            ViewHolder mViewHolder = null;
            if (convertView == null || convertView.getTag() == null) {
                view = mLayoutInflater.inflate(R.layout.app_grid_item, parent, false);

                mViewHolder = new ViewHolder();
                mViewHolder.mCapabilityName = (TextView) view.findViewById(R.id.app_grid_item_name);
                mViewHolder.mCapabilityIcon = (ImageView) view.findViewById(R.id.app_grid_item_icon);
                mViewHolder.mCapabilityMask = (ImageView) view.findViewById(R.id.app_grid_item_icon_mask);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            Capability item = (Capability) getItem(position);
            if (item != null) {

                mViewHolder.mCapabilityName.setText(item.getCapabilityName());

                if (item.getIcon() > 0) {
                    mViewHolder.mCapabilityIcon.setImageResource(item.getIcon());
                }
                mViewHolder.mCapabilityMask.setVisibility(View.VISIBLE);
            }

            return view;
        }

        class ViewHolder {
            TextView mCapabilityName;
            ImageView mCapabilityIcon;
            ImageView mCapabilityMask;
        }
    }
}
