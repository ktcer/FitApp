package com.cn.fit.ui.patient.others.mement;

import android.content.Context;
import android.content.Intent;
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
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;

public class ActivityPost extends ActivityBasic {


    private TextView myPost;
    private TextView newPost;
    private ListView postLV;
    private String pageType = "0";
    private String ActivityTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);
        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        pageType = getIntent().getExtras().getString(Constant.HEALTH_QA);
        myPost = (TextView) findViewById(R.id.my_post);
        newPost = (TextView) findViewById(R.id.new_post);
        myPost.setOnClickListener(this);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        if (pageType.equals("1"))
            midTV.setText("交流");
        else if (pageType.equals("0"))
            midTV.setText("健康问答");
        newPost.setOnClickListener(this);
        postLV = (ListView) findViewById(R.id.my_moment_post_lv);
        postLV.setAdapter(new MyAdapter(this));
        postLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//				Intent intent = new Intent(PostActivity.this,
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

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.my_post:
                ActivityTag = "MyPostActivity";
                wehtherLogin(ActivityTag, ActivityMyPost.class);
//					Intent intent = new Intent();
//					intent.setClass(PostActivity.this, MyPostActivity.class);
//					startActivity(intent);
                break;
            case R.id.new_post:
//    					Intent intent1 = new Intent();
//    					intent1.setClass(PostActivity.this, NewPostActivity.class);
//    					startActivity(intent1);
                ActivityTag = "NewPostActivity";
                wehtherLogin(ActivityTag, ActivityNewPost.class);
            default:
                break;
        }

    }

    private void wehtherLogin(String ActivityTag, Class<?> cls) {
        UtilsSharedData sharedData = new UtilsSharedData();
        if (sharedData.isEmpty(Constant.LOGIN_STATUS) || sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("0")) {
            Intent intent = new Intent(ActivityPost.this, ActivityLogin.class);
            intent.putExtra("ActivityTag", ActivityTag);
            startActivity(intent);
        } else {
            startActivity(cls);
        }
    }

}
