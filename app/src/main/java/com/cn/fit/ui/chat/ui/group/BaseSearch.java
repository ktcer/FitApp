package com.cn.fit.ui.chat.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.chat.ui.ECSuperActivity;

/**
 * com.cn.aihu.ui.chat.ui.group in ECDemo_Android
 * Created by Jorstin on 2015/4/2.
 */
public class BaseSearch extends ECSuperActivity implements View.OnClickListener {

    public static final int SEARCH_BY_ID = 1;
    public static final int SEARCH_BY_INDISTINCT_NAME = 2;
    public static final String EXTRA_SEARCH_TYPE = "search_type@yuntongxun.com";

    @Override
    protected int getLayoutId() {
        return R.layout.base_search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1, R.string.search_group, this);

        final Intent intent = new Intent(this, SearchGroupActivity.class);
        findViewById(R.id.search_by_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EXTRA_SEARCH_TYPE, SEARCH_BY_ID);
                startActivity(intent);
            }
        });

        findViewById(R.id.search_by_indistinct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EXTRA_SEARCH_TYPE, SEARCH_BY_INDISTINCT_NAME);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
        }
    }
}
