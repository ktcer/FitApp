package com.cn.fit.ui.patient.main.healthpost.healthpost;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

public class ActivityDetailWebService extends ActivityBasic {

    private WebView webservice;
    private String TravelID;
    private ProgressBar progress;
    private TextView Title;
    private String title;
    private String url = "http://test.inurse.com.cn/web/travel/detail?travelID=10&platform=app.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webservice);
        Title = (TextView) findViewById(R.id.middle_tv);
        webservice = (WebView) findViewById(R.id.webview_webservice);
        progress = (ProgressBar) findViewById(R.id.myProgressBar_webservice);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        Title.setText(title);
        webservice.getSettings().setJavaScriptEnabled(true);
        webservice.loadUrl(url);
        webservice.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    progress.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == progress.getVisibility()) {
                        progress.setVisibility(View.VISIBLE);
                    }
                    progress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }
}
