package com.cn.fit.ui.patient.others.mement;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class ActivityMyPost extends ActivityBasic {

    private ListView postLV;
    private List<PostContent> postList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_moment_my_post);
        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        postList = new ArrayList<PostContent>();
        postLV = (ListView) findViewById(R.id.my_post_lv);
        postLV.setAdapter(new MyAdapter(this));
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("我的帖子");
        postLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//				Intent intent = new Intent(MyPostActivity.this,
//						FamilyMember.class);
//				startActivity(intent);

            }
        });
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView time;
        public TextView commentBtn;
        public TextView content;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listitem_healthqa, null);
                holder.img = (ImageView) convertView.findViewById(R.id.qa_person_pic);
                holder.name = (TextView) convertView.findViewById(R.id.qa_name);
                holder.time = (TextView) convertView.findViewById(R.id.qa_time);
                holder.commentBtn = (TextView) convertView.findViewById(R.id.qa_comment);
                holder.content = (TextView) convertView.findViewById(R.id.qa_content);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }


//            holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
//            holder.title.setText((String)mData.get(position).get("title"));
//            holder.info.setText((String)mData.get(position).get("info"));
//             
//            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
//                 
//                @Override
//                public void onClick(View v) {
//                    showInfo();                 
//                }
//            });


            return convertView;
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        super.onClick(arg0);
    }
}