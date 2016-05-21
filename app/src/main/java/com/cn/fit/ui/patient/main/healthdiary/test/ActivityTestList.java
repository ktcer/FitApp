package com.cn.fit.ui.patient.main.healthdiary.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.main.healthpost.doctorinterview.VideoPlay;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

/**
 * 测试选择类型列表
 */
public class ActivityTestList extends ActivityBasic {
    private final static int ONDEMOND = 0;
    private final static int LIVE = 1;
    private ListView videoLV;
    private String[] title = new String[]{"生活习惯评估", "身体指标评估"};
    private int[] pic = new int[]{R.drawable.iconfont_shenghuoxiguan, R.drawable.iconfont_shentizhibiao};
    private String[] titleString = {"对您的生活习惯进行评估指导", "对您的身体指标进行评估指导"};
    private Boolean[] showMark = new Boolean[]{false, false};
    private VideoListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyalistview);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsSharedData.initDataShare(ActivityTestList.this);
        if (UtilsSharedData.getLong("HabbitTestState", -1) == 0) {
            showMark[0] = true;
        } else {
            showMark[0] = false;
        }
        if (UtilsSharedData.getLong("BodyTestState", -1) == 0) {
            showMark[1] = true;
        } else {
            showMark[1] = false;
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("身体评估");
        videoLV = (ListView) findViewById(R.id.list);
        adapter = new VideoListAdapter();
        videoLV.setAdapter(adapter);
        videoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String url;
                switch (arg2) {
                    case 0:
                        startActivity(ActivityEvaluationCenter.class);
                        break;
                    case 1:
                        startActivity(ActivityEvaluationSecond.class);
                        break;
                    case 2:
                        url = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/01/vts_01_1.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;

                }

            }

        });

    }


    private class VideoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return title[position];
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
                convertView = LayoutInflater.from(ActivityTestList.this).inflate(
                        R.layout.list_item_testtype, null);
                holder.testImage = (ImageView) convertView
                        .findViewById(R.id.vido_circle_img);
                holder.testName = (TextView) convertView
                        .findViewById(R.id.vido_name);
                holder.testDescription = (TextView) convertView
                        .findViewById(R.id.vido_description);
                holder.redmaker = (ImageView) convertView.findViewById(R.id.red_mark_Test);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.testImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.testImage.setImageResource(pic[position]);
            holder.testName.setText(title[position]);
            holder.testDescription.setText(titleString[position]);
            if (showMark[position]) {
                holder.redmaker.setVisibility(View.VISIBLE);
            } else {
                holder.redmaker.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    private class ViewHolder {
        ImageView testImage;
        TextView testName;
        TextView testDescription;
        ImageView redmaker;
    }

}
