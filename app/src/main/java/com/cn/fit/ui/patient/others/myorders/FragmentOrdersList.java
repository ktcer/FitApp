/*
 * 订单列表fragment
 */

package com.cn.fit.ui.patient.others.myorders;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.coach.comment.ActivityAddComment;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.customer.BeanAplayOrderList;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.FragmentBasicListView;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ResourceType")
public class FragmentOrdersList extends FragmentBasicListView {

    private static final String ARG_POSITION = "position";
    private List<BeanAplayOrderList> infoList, tempInfoList;
    private int type;
    private OrdersListAdapter orderListViewAdapter;

    public static FragmentOrdersList newInstance(int position) {
        FragmentOrdersList f = new FragmentOrdersList();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_POSITION) - 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
//				.getDisplayMetrics());
        orderListViewAdapter = new OrdersListAdapter(getActivity());
        listView = new XListView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(R.color.black);
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getActivity(), ActivityMyOrderInfo.class);
                Bundle mBundle = new Bundle();
                mBundle.putString(Constant.ORDER_NO, infoList.get(position - 1).getDdh());
                mBundle.putByte(Constant.ORDER_STATUS, infoList.get(position - 1).getState());
                mBundle.putFloat(Constant.ORDER_PRICE, infoList.get(position - 1).getMoney());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        infoList = new ArrayList<BeanAplayOrderList>();
        tempInfoList = new ArrayList<BeanAplayOrderList>();
        listView.setAdapter(orderListViewAdapter);
        fl.addView(listView);
        return fl;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryMyOrdersTask task = new QueryMyOrdersTask();
        task.execute(type);
    }

    /*
     * 获取订单信息线程
     */
    private class QueryMyOrdersTask extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(getActivity());
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userID", userId + "");
            param.put("state", "" + params[0]);//
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl() + "/ad/app/orderlist";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                jsonToArray(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanAplayOrderList tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            orderListViewAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }


    private void jsonToArray(String json) {
        Gson gson = new Gson();
        tempInfoList = gson.fromJson(json, new TypeToken<List<BeanAplayOrderList>>() {
        }.getType());
    }

    /*
     * 订单列表adapter
     */
    private class OrdersListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public OrdersListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.listitem_myorder, null);
                holder.orderCover = (ImageView) convertView
                        .findViewById(R.id.type_myappointment);
                holder.orderDetail = (TextView) convertView
                        .findViewById(R.id.order_title);
                holder.orderPrice = (TextView) convertView
                        .findViewById(R.id.order_price);
                holder.orderStatus = (TextView) convertView
                        .findViewById(R.id.order_status);
                holder.comment = (TextView) convertView.findViewById(R.id.order_comment);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + infoList.get(position).getCover(),
                    holder.orderCover, AppMain.initImageOptions(R.drawable.default_life_icon, false));

            holder.orderDetail.setText("【" + infoList.get(position).getServeType() + "】" + infoList.get(position).getStartTime() + " " + infoList.get(position).getTitle());
            holder.orderPrice.setText("¥" + infoList.get(position)
                    .getMoney());
            switch (infoList.get(position).getIfComments()) {
                case 0:
                    holder.comment.setVisibility(View.VISIBLE);
                    holder.comment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {//评论
                            Intent intent = new Intent(getActivity(), ActivityAddComment.class);
                            intent.putExtra("coachID", infoList.get(position).getCoachID());
                            intent.putExtra("classID", infoList.get(position).getClassID());
                            intent.putExtra("ddh", infoList.get(position).getDdh());
                            startActivity(intent);

                        }
                    });
                    break;
                case 1:
                    holder.comment.setVisibility(View.VISIBLE);
                    holder.comment.setOnClickListener(null);
                    holder.comment.setText("已评价");
                    holder.comment.setBackgroundColor(getResources().getColor(R.color.white));
                    holder.comment.setTextColor(getResources().getColor(R.color.font_gray));
                    break;
                case -1:
                    holder.comment.setVisibility(View.INVISIBLE);
                    break;
            }

            switch (infoList.get(position).getState()) {
                case 0:
                    holder.orderStatus
                            .setText("待支付");
                    holder.orderStatus.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1:
                    holder.orderStatus
                            .setText("已付款");
                    holder.orderStatus.setTextColor(getResources().getColor(R.color.orangered));
                    break;
                case 4:
                    holder.orderStatus
                            .setText("已取消");
                    holder.orderStatus.setTextColor(getResources().getColor(R.color.lightgray));
                    break;
            }

            return convertView;
        }
    }

    private class ViewHolder {
        /**
         * 订单图片
         */
        ImageView orderCover;
        /**
         * 订单详情
         */
        TextView orderDetail;
        /**
         * 订单价格
         */
        TextView orderPrice;
        /**
         * 订单状态
         */
        TextView orderStatus;
        TextView comment;

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        QueryMyOrdersTask task = new QueryMyOrdersTask();
        task.execute(type);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            QueryMyOrdersTask task = new QueryMyOrdersTask();
            task.execute(type);
        }
    }

}