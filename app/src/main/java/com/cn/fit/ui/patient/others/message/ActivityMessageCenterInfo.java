package com.cn.fit.ui.patient.others.message;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;

public class ActivityMessageCenterInfo extends ActivityBasic implements OnClickListener {
    private TextView tv1, tv2;
    private TextView delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagecenterinfo);
        initial();
        delete.setOnClickListener(this);
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("消息详情");
        tv1 = (TextView) findViewById(R.id.time_messageCenterInfo);
        tv2 = (TextView) findViewById(R.id.explain_messageCenterInfo);
        delete = (TextView) findViewById(R.id.right_tv);
        delete.setVisibility(View.VISIBLE);
        delete.setText("删除");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        ToastUtil.showMessage("已删除");
//		Intent intent=new Intent(MessageCenterInfo.this,MessageCenter.class);
//		startActivity(intent);
        String old = ActivityMessageCenter.class.getName();
        backTo(old);
    }

}
