package com.cn.fit.ui.patient.others.myfavor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class CollectPaperActicle extends ActivityBasic {

    /**
     * 2015-4-9
     *
     * @author kuangtiecheng
     */
    private Button cancleCollect;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_collect_article);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("文章详情");
    }

}
