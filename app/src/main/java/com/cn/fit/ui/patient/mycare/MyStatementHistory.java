package com.cn.fit.ui.patient.mycare;

import android.os.Bundle;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

/**
 * 我的病情自述详情
 *
 * @author kuangtiecheng
 */
public class MyStatementHistory extends ActivityBasic {
    private TextView tv1, tv2;
    private String str = "2011.10心脏病发作住院，在常规检查中心脏病症状不显著，"
            + "心电图，24小时动态监测，血液检测（尽管只在中心医院时略高），"
            + "心脏彩超，听诊器都是正常的，只有心脏冠状动脉CT检查显示冠状动脉狭窄；做造影发现两处80%狭窄并做支架。"
            + "手术出院后，长时间出现强烈的心脏不适症状，卧床2个月左右，一年多才基本康复。";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mystatementhistory);
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("病情自述详情");
        tv1 = (TextView) findViewById(R.id.date_myStatementHistory);
        tv2 = (TextView) findViewById(R.id.statemnet_myStatementHistory);
        tv2.setText(str);
    }
}
