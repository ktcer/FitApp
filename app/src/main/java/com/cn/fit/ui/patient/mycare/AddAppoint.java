package com.cn.fit.ui.patient.mycare;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

/**
 * @author kuangtiecheng
 */
public class AddAppoint extends ActivityBasic {
    private ImageView button;
    private String msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmyappoint);
        initial();
    }

    private void initial() {
//		button = (ImageView) this.findViewById(R.id.add_my_appoint_search);
//		button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String old = MyCarePage.class.getName();
//				backTo(old);
//			}
//		});
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("新增预约咨询");
    }

}
