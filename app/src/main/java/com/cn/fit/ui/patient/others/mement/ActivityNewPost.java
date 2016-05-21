package com.cn.fit.ui.patient.others.mement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class ActivityNewPost extends ActivityBasic {
    /**
     * 2015-4-9
     *
     * @author kuangtiecheng
     */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_moment_new_post);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        TextView rightTV = (TextView) findViewById(R.id.right_tv);
        rightTV.setVisibility(View.VISIBLE);
        rightTV.setText("提交");
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("发表新帖");
        rightTV.setOnClickListener(this);
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.right_tv:
                previousActivity(v);
                break;

            default:
                break;
        }

    }

}

	
	

