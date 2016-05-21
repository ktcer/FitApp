package com.cn.fit.ui.patient.myres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.main.mynurse.PopMenu;
import com.cn.fit.ui.patient.others.myfavor.CollectPaperActicle;
import com.cn.fit.ui.patient.others.myfavor.CollectPaperContent;

public class ActivitySubscribePaper extends ActivityBasic implements OnDismissListener {
    private TextView chooseType1Tv;
    private PopMenu popMenuLeft;
    private ListView collectPaperLv;
    private List<CollectPaperContent> collectPaperList;
    String itemName[] = {"高血压", "心血管", "白血病"};

    private TextView search, title;

    private String[] healthArray = {"高血压患者注意合理膳食", "强迫症患者注意合理膳食", "更年期患者注意合理膳食"};
    private String[] publisherArray = {"吃蔬菜，水果......", "吃蔬菜，水果......", "吃蔬菜，水果......"};
    private String[] collectTimeArray = {"昨天", "上周", "上个月"};

    // 搜索框部分
    private LinearLayout mainLayout;
    private RelativeLayout titleBarLayout;
    private int moveHeight;
    private int statusBarHeight;
    private PopupWindow popupWindow;
    private View searchView;
    private EditText searchEditText;
    private TextView searchTextView;
    private ListView resultListView;
    private View alphaView;
    private ImageView cancelEditTextIV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_subscribe_paper);
        initView();
        initSearchView();
    }

    private void initView() {
        collectPaperList = new ArrayList<CollectPaperContent>();
        chooseType1Tv = (TextView) findViewById(R.id.choose_type1_tv);
        chooseType1Tv.setOnClickListener(this);
        fakeCollectPaperList();
        collectPaperLv = (ListView) findViewById(R.id.subscribe_paper_list_lv);
        collectPaperLv.setAdapter(new CollectPaperListAdapter(this));
        search = (TextView) findViewById(R.id.right_tv);
        search.setOnClickListener(this);
        title = (TextView) findViewById(R.id.middle_tv);
        search.setVisibility(View.GONE);
        search.setText("");
        search.setBackgroundResource(R.drawable.search_icon);
        title.setText("订阅文章");
        collectPaperLv.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(ActivitySubscribePaper.this, CollectPaperActicle.class);
                startActivity(intent);

            }
        });
    }

    @SuppressWarnings("deprecation")
    private void initSearchView() {
        // //搜索框部分
        LayoutInflater mInflater = LayoutInflater.from(this);
        mainLayout = (LinearLayout) findViewById(R.id.subscribe_layout);
        searchView = mInflater.inflate(R.layout.popup_window_search, null);
        titleBarLayout = (RelativeLayout) findViewById(R.id.paper_title_bar_layout);
        cancelEditTextIV = (ImageView) searchView
                .findViewById(R.id.cancel_edittext_iv);
        cancelEditTextIV.setOnClickListener(this);
        searchEditText = (EditText) searchView
                .findViewById(R.id.popup_window_et_search);
        searchEditText.setFocusable(true);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().equals("")) {
                    alphaView.setVisibility(View.VISIBLE);
                    resultListView.setVisibility(View.GONE);
                    cancelEditTextIV.setVisibility(View.GONE);
                } else {
                    cancelEditTextIV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        searchTextView = (TextView) searchView
                .findViewById(R.id.popup_window_tv_search);
        searchTextView.setOnClickListener(this);
        resultListView = (ListView) searchView
                .findViewById(R.id.popup_window_lv);
        // 阴影View
        alphaView = searchView.findViewById(R.id.popup_window_v_alpha);
        alphaView.setOnClickListener(this);

        popupWindow = new PopupWindow(searchView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(this);
    }

    /**
     * 搜索结果列表填充数据及设置点击事件
     */
    private void setResultList() {

        final ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < healthArray.length; i++) {
            HashMap<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("health", healthArray[i]);
            listItem.put("publisher", publisherArray[i]);
            listItem.put("collect_time", collectTimeArray[i]);
            listItems.add(listItem);
            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItems,
                    R.layout.collect_paper_list_item, new String[]{"health", "publisher",
                    "collect_time"}, new int[]{R.id.keep_health_tv, R.id.collect_time_tv});
            resultListView.setAdapter(listItemAdapter);
        }
        ;
    }

    private void fakeCollectPaperList() {
        for (int i = 0; i < 4; i++) {
            CollectPaperContent content = new CollectPaperContent();
            content.setKeepHealth("高血压患者注意合理膳食");
            content.setPublisher("吃蔬菜，水果......");
            content.setCollectTime("收藏时间：昨天");
            collectPaperList.add(content);
        }
    }

    private class CollectPaperListAdapter extends BaseAdapter {
        private Context context;

        public CollectPaperListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return collectPaperList.size();
        }

        @Override
        public Object getItem(int position) {
            return collectPaperList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.collect_paper_list_item, null);
                holder.KeepHealthTV = (TextView) convertView
                        .findViewById(R.id.keep_health_tv);
                holder.CollectTimeTV = (TextView) convertView
                        .findViewById(R.id.collect_time_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.KeepHealthTV.setText(collectPaperList.get(position)
                    .getKeepHealth());
            holder.CollectTimeTV.setText(collectPaperList.get(position)
                    .getCollectTime());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView KeepHealthTV;
        TextView CollectTimeTV;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.choose_type1_tv:
                popMenuLeft = new PopMenu(this);
                popMenuLeft.addItems(itemName);
                popMenuLeft.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        chooseType1Tv.setText(itemName[arg2] + "▼");
                        popMenuLeft.dismiss();
                    }
                });
                popMenuLeft.showAsDropDown(v, 200, 0);
                break;

            case R.id.right_tv:
                showSearchBar();
                break;
            case R.id.popup_window_tv_search:
                setResultList();
                alphaView.setVisibility(View.GONE);
                resultListView.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_edittext_iv:
                searchEditText.setText("");
                break;
            default:
                break;
        }
    }

    private void showSearchBar() {
        getStatusBarHeight();
        moveHeight = titleBarLayout.getHeight();
        Animation translateAnimation = new TranslateAnimation(0, 0, 0,
                -moveHeight);
        translateAnimation.setDuration(300);
        mainLayout.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                mainLayout.setAnimation(anim);
                titleBarLayout.setVisibility(View.GONE);
                titleBarLayout.setPadding(0, -moveHeight, 0, 0);

                popupWindow.showAtLocation(mainLayout, Gravity.CLIP_VERTICAL,
                        0, statusBarHeight);
                openKeyboard();
            }
        });
    }

    private void getStatusBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
    }

    private void openKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    public void onDismiss() {
        resetUI();
    }

    private void resetUI() {
        searchEditText.setText("");
        titleBarLayout.setPadding(0, 0, 0, 0);
        titleBarLayout.setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(0, 0,
                -moveHeight, 0);
        translateAnimation.setDuration(300);
        mainLayout.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                mainLayout.setAnimation(anim);
                // titleBarLayout.setPadding(0, 0, 0, 0);

            }
        });
    }
}