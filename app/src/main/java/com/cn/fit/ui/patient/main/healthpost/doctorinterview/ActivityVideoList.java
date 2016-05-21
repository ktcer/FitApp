package com.cn.fit.ui.patient.main.healthpost.doctorinterview;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import com.cn.fit.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class ActivityVideoList extends ActivityBasic {
    private final static int ONDEMOND = 0;
    private final static int LIVE = 1;
    private ListView videoLV;
    private String[] title = new String[]{"代谢综合征康复运动示范", "老年人跌倒测评示范教学", "老年人跌倒风险因素的认知与管理", "人工膝关节置换术后康复指南", "探索基于云平台的延伸护理新模式", "血糖仪应用标准操作流程"};
    private int[] pic = new int[]{R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow};
    //	private Button liveBtn;
//	private Button onDemondBtn;
    private String[] titleString = {"探索基于云平台的延伸护理新模式"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        initView();
    }

    private void initView() {
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("专家访谈");
        videoLV = (ListView) findViewById(R.id.video_list);
//		liveBtn = (Button) findViewById(R.id.live_btn);
//		liveBtn.setOnClickListener(new LiveBtnOnClickListener());
//		onDemondBtn = (Button) findViewById(R.id.on_demond_btn);
//		onDemondBtn.setOnClickListener(new OnDemondOnClickListener());
        videoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String url;
                switch (arg2) {
                    case 0:
                        url = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/03/vts_01_3.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;
                    case 1:
                        url = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/02/vts_01_2.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;
                    case 2:
                        url = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/01/vts_01_1.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;
                    case 3:
                        url = "http://114.112.74.20/www.imediciner.com.cn/rgcgjzhshkfzn/rgcgjzhshkfzn/rgcgjzhshkfzn.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;
                    case 4:
                        url = "http://114.112.74.20/www.imediciner.com.cn/tsjyyptdyshlxms/tsjyyptdyshlxms.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;
                    case 5:
                        url = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/04/vts_01_4.m3u8";
                        startActivity(VideoPlay.class, Constant.VEDIO_URL, url);
                        break;

                }

            }

        });
        fillList(ONDEMOND);
    }

    private void fillList(int type) {
        List<VideoContent> videoList = new ArrayList<VideoContent>();
        switch (type) {
            case ONDEMOND:
                videoList = getOnDemodList(title, pic);
                break;
            case LIVE:
                videoList = getLiveList();
                break;
        }
        videoLV.setAdapter(new VideoListAdapter(this, videoList));
    }

    private List<VideoContent> getOnDemodList(String[] str, int[] adb) {
        List<VideoContent> list = new ArrayList<VideoContent>();
        for (int i = 0; i < str.length; i++) {
            VideoContent fakecontent = new VideoContent();
            fakecontent.setVideoTitle(str[i]);
            fakecontent.setVideoPicture(BitmapFactory.decodeResource(getResources(), adb[i]));
//			fakecontent.setVideoTitle("春季如何保健");
//			fakecontent.setLecturer("讲座人：梁丽");
//			fakecontent.setDuration("时长：50分钟");
//			fakecontent.setDate("2015-03-08");
//			fakecontent.setVideoPicture(BitmapFactory.decodeResource(
//					getResources(), R.drawable.video_default));
            list.add(fakecontent);
        }
        return list;
    }

    private List<VideoContent> getLiveList() {
        List<VideoContent> list = new ArrayList<VideoContent>();
        for (int i = 0; i < 5; i++) {
            VideoContent fakecontent = new VideoContent();
            fakecontent.setVideoTitle("春季如何保健");
//			fakecontent.setLecturer("讲座人：梁丽");
//			fakecontent.setDuration("时长：50分钟");
//			fakecontent.setDate("2015-03-08");
            fakecontent.setVideoPicture(BitmapFactory.decodeResource(
                    getResources(), R.drawable.arrow));
            list.add(fakecontent);
        }
        return list;
    }

    private class VideoListAdapter extends BaseAdapter {
        List<VideoContent> list;
        Context context;

        public VideoListAdapter(Context context, List<VideoContent> list) {
            this.context = context;
            this.list = list;
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
                        R.layout.video_list_item, null);
                holder.videoTitle = (TextView) convertView
                        .findViewById(R.id.video_title_tv);
                holder.videoPic = (ImageView) convertView
                        .findViewById(R.id.video_picture_iv);
//				holder.videoLecturer = (TextView) convertView
//						.findViewById(R.id.video_lecturer_tv);
//				holder.videoDuration = (TextView) convertView
//						.findViewById(R.id.video_duration_tv);
//				holder.videoDate = (TextView) convertView
//						.findViewById(R.id.video_date_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.videoTitle.setText(list.get(position).getVideoTitle());
            holder.videoPic
                    .setImageBitmap(list.get(position).getVideoPicture());
//			holder.videoDuration.setText(list.get(position).getDuration());
//			holder.videoLecturer.setText(list.get(position).getLecturer());
//			holder.videoDate.setText(list.get(position).getDate());
            return convertView;
        }

    }

    private class ViewHolder {
        ImageView videoPic;
        TextView videoTitle;
//		TextView videoLecturer;
//		TextView videoDuration;
//		TextView videoDate;
    }

//	private class LiveBtnOnClickListener implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			fillList(LIVE);
//			onDemondBtn.setBackgroundColor(getResources().getColor(
//					R.color.gray));
//			liveBtn.setBackgroundColor(getResources().getColor(R.color.blueLight));
//		}
//
//	}
//
//	private class OnDemondOnClickListener implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			fillList(ONDEMOND);
//			onDemondBtn.setBackgroundColor(getResources().getColor(
//					R.color.blueLight));
//			liveBtn.setBackgroundColor(getResources().getColor(R.color.gray));
//		}
//
//	}
}
