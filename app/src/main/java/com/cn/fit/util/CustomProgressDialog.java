package com.cn.fit.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.cn.fit.R;
import com.cn.fit.util.progressWheel.ProgressWheel;

/**
 * @author kuangtiecheng
 * @Description:加载动画
 */
public class CustomProgressDialog extends Dialog {

    private Context mContext;
    private String mLoadingTip;
    private ProgressWheel progressWheel;

    public CustomProgressDialog(Context context, String content) {
        super(context);
        this.mContext = context;
        this.mLoadingTip = content;
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setContentView(R.layout.progress_dialog);
//		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
    }

}
