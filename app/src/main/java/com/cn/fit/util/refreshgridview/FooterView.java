package com.cn.fit.util.refreshgridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.fit.R;

/**
 * gridview底部加载
 *
 * @author kuangtiecheng
 */
public class FooterView extends LinearLayout {
    private Context mContext;

    public static final int HIDE = 0;
    public static final int MORE = 1;
    public static final int LOADING = 2;
    public static final int BADNETWORK = 3;

    private ProgressBar progressBar;
    private TextView textView;
    private Button btn;

    private int curStatus;

    private OnClickListener ml;

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FooterView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.gridview_footer,
                this, true);
        progressBar = (ProgressBar) findViewById(R.id.footer_loading);
        textView = (TextView) findViewById(R.id.footview_text);
        btn = (Button) findViewById(R.id.footview_button);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ml != null) {
                    ml.onClick(v);
                }

            }
        });

        setStatus(MORE);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        ml = l;
        super.setOnClickListener(ml);
    }

    public void setStatus(int status) {
        curStatus = status;
        switch (status) {
            case HIDE:
                setVisibility(View.GONE);
                break;
            case MORE:
                progressBar.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("点击加载更多");
                this.setVisibility(View.VISIBLE);
                break;
            case LOADING:

                progressBar.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("正在加载...");
                this.setVisibility(View.VISIBLE);
                break;
            case BADNETWORK:
                progressBar.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("网络连接有问题");
                this.setVisibility(View.VISIBLE);
                break;

        }
    }

    public int getStatus() {
        return curStatus;
    }

}
