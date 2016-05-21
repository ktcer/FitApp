package com.cn.fit.ui.patient.mycare;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 病情自述
 *
 * @author kuangtiecheng
 */
public class MyStatement extends ActivityBasic implements
        PopupWindow.OnDismissListener, OnScrollListener, OnItemClickListener {
    private ListView listView;
    private SimpleAdapter adapter;
    private List<StatementList> statementList;
    private TextView search_btn;
    /*
     *搜索部分
     */
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

    // 网络请求列表刷新部分
    private Thread mThread;
    private StatementListAdapter lvAdapter;
    private StatementListAdapter resultListViewAdapter;
    // private ListViewAdapter resultAdapter;
    private View loadingLayout;
    private int listTotal = 24;// 列表项总数(后台传来),不然不能提前取消载入动画

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mystatement);
        initial();
        initSearchView();
        load();
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("病情自述");
        listView = (ListView) this.findViewById(R.id.lv_myStatement);
        statementList = new ArrayList<StatementList>();
        search_btn = (TextView) findViewById(R.id.right_tv);
        search_btn.setVisibility(View.GONE);
        //	search_btn.setVisibility(View.VISIBLE);
        search_btn.setBackgroundResource(R.drawable.search_icon);
        search_btn.setText("");
        search_btn.setOnClickListener(this);
    }

    private void write() {
        for (int i = 0; i < 10; i++) {
            StatementList content = new StatementList();
            content.setStatementList("膝关节置换术之后，经过一系列锻炼，感觉有点...");
            statementList.add(content);
        }
    }

    private void load() {
        write();
        listView.setAdapter(new StatementListAdapter(this));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                startActivity(MyStatementHistory.class);
            }

        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                showSearchBar();
                break;
            case R.id.cancel_edittext_iv:
                searchEditText.setText("");
                onDismiss();
            case R.id.popup_window_tv_search:
                setResultList();
                alphaView.setVisibility(View.GONE);
                resultListView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private class StatementListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public StatementListAdapter(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        @Override
        public int getCount() {
            return statementList.size();
        }

        @Override
        public Object getItem(int position) {
            return statementList.get(position);
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
                        R.layout.listitem_mystatement, null);
                holder.Statement_Item = (TextView) convertView
                        .findViewById(R.id.tv1_mystatement);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.Statement_Item.setText(statementList.get(position).getStatementList());
            return convertView;
        }

    }

    private class ViewHolder {
        TextView Statement_Item;/**病情自述*/
    }

    /**
     * 搜索部分初始化
     */

    private void initSearchView() {
        ////搜索框部分
        LayoutInflater mInflater = LayoutInflater.from(this);
        mainLayout = (LinearLayout) findViewById(R.id.linearLayout_myStatement);
        titleBarLayout = (RelativeLayout) findViewById(R.id.title_myStatement);
        searchView = mInflater.inflate(R.layout.popup_window_search, null);
        cancelEditTextIV = (ImageView) searchView.findViewById(R.id.cancel_edittext_iv);
        cancelEditTextIV.setOnClickListener(this);
        searchEditText = (EditText) searchView.findViewById(R.id.popup_window_et_search);
        searchEditText.setFocusable(true);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    alphaView.setVisibility(View.VISIBLE);
                    resultListView.setVisibility(View.GONE);
                    cancelEditTextIV.setVisibility(View.GONE);
                } else {
                    cancelEditTextIV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchTextView = (TextView) searchView.findViewById(R.id.popup_window_tv_search);
        searchTextView.setOnClickListener(this);
        resultListView = (ListView) searchView.findViewById(R.id.popup_window_lv);
        resultListView.setTag("resultListView");
        //阴影View
        alphaView = searchView.findViewById(R.id.popup_window_v_alpha);
        alphaView.setOnClickListener(this);

        popupWindow = new PopupWindow(searchView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(this);

        loadingLayout = mInflater.inflate(R.layout.loading, null);
    }

    /**
     * 健康方案列表填充数据及设置点击、下拉刷新事件
     */
    private void setList() {

        // 添加到脚页显示
        listView.addFooterView(loadingLayout);
        // 给ListView添加适配器
        lvAdapter = new StatementListAdapter(this);
        listView.setAdapter(lvAdapter);
        // 给ListView注册滚动监听
        listView.setOnScrollListener(this);
        // 给ListView注册点击监听
        listView.setOnItemClickListener(this);
    }

    private void setResultList() {

        // 添加到脚页显示
        resultListView.addFooterView(loadingLayout);
        // 给ListView添加适配器
        resultListViewAdapter = new StatementListAdapter(this);
        resultListView.setAdapter(resultListViewAdapter);
        // 给ListView注册滚动监听
        resultListView.setOnScrollListener(this);
        // 给ListView注册点击监听
        resultListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

//		Intent intent = new Intent(MyTest.this, ActivityMyOrderInfo.class);
//		intent.putExtra("state", State[position]);
//		startActivity(intent);

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
        Animation translateAnimation = new TranslateAnimation(0, 0, -moveHeight, 0);
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


    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        if (view.getTag().equals("listView")) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                // 开线程去下载网络数据
                if (mThread == null || !mThread.isAlive()) {
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                // 这里放你网络数据请求的方法，我在这里用线程休眠5秒方法来处理
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    };
                    mThread.start();
                }
            }
        } else if (view.getTag().equals("resultListView")) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                // 开线程去下载网络数据
                if (mThread == null || !mThread.isAlive()) {
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                // 这里放你网络数据请求的方法，我在这里用线程休眠5秒方法来处理
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    };
                    mThread.start();
                }
            }
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    if (lvAdapter.count <= 20) {
                        lvAdapter.count += 10;
                        int currentPage = lvAdapter.count / 10;
                        Toast.makeText(getApplicationContext(),
                                "第" + currentPage + "页", Toast.LENGTH_LONG).show();
                    } else {
                        listView.removeFooterView(loadingLayout);
                    }
                    // 重新刷新Listview的adapter里面数据
                    lvAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (resultListViewAdapter.count <= 20) {
                        resultListViewAdapter.count += 10;
                        int currentPage = resultListViewAdapter.count / 10;
                        Toast.makeText(getApplicationContext(),
                                "第" + currentPage + "页", Toast.LENGTH_LONG).show();
                    } else {
                        resultListView.removeFooterView(loadingLayout);
                    }
                    // 重新刷新Listview的adapter里面数据
                    resultListViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
