package com.cn.fit.ui.patient.others.mydevice;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyDevice extends ActivityBasic implements OnDismissListener {

    /**
     * @author kuangtiecheng
     */
    private final static int RENT = 0;
    private final static int BIND = 1;
    private ListView deviceLV;
    private Button rentButton, bindButton;

    private TextView search;
    private TextView title;

    private String[] deviceTypeArray = {"绑定设备类型：专用设备", "绑定设备类型：家庭设备",
            "绑定设备类型：公司设备"};
    private String[] deviceUserArray = {"使用人：老旷", "使用人：金海", "使用人：青书"};
    private String[] remainingTimeArray = {"剩余时间：三天", "剩余时间：两小时", "剩余时间：两秒"};

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
        setContentView(R.layout.my_res_my_device);
        initial();
        initSearchView();

    }

    private void initial() {
        deviceLV = (ListView) this.findViewById(R.id.lv_device);
        rentButton = (Button) this.findViewById(R.id.rent_device);
        rentButton.setOnClickListener(new rentButtonOnClickListener());
        bindButton = (Button) this.findViewById(R.id.bind_device);
        bindButton.setOnClickListener(new bindButtonOnClickListener());
        fillList(RENT);
        search = (TextView) findViewById(R.id.right_tv);
        title = (TextView) findViewById(R.id.middle_tv);
        search.setVisibility(View.GONE);
        search.setText("");
        search.setBackgroundResource(R.drawable.search_icon);
        title.setText("我的设备");
        search.setOnClickListener(this);
        deviceLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MyDevice.this, DeviceDetail.class);
                startActivity(intent);

            }
        });
    }

    @SuppressWarnings("deprecation")
    private void initSearchView() {
        // //搜索框部分
        LayoutInflater mInflater = LayoutInflater.from(this);
        mainLayout = (LinearLayout) findViewById(R.id.device_layout);
        titleBarLayout = (RelativeLayout) findViewById(R.id.device_title_bar_layout);
        searchView = mInflater.inflate(R.layout.popup_window_search, null);
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
        for (int i = 0; i < deviceTypeArray.length; i++) {
            HashMap<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("device_type", deviceTypeArray[i]);
            listItem.put("device_user", deviceUserArray[i]);
            listItem.put("remaining_time", remainingTimeArray[i]);
            listItems.add(listItem);
            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItems,
                    R.layout.device_list_item, new String[]{"device_type",
                    "device_user", "remaining_time"}, new int[]{
                    R.id.device_type_tv, R.id.device_user_tv,
                    R.id.remaining_time_tv});
            resultListView.setAdapter(listItemAdapter);
        }
        ;
    }

    private void fillList(int type) {
        List<DeviceContent> deviceList = new ArrayList<DeviceContent>();
        switch (type) {
            case RENT:
                deviceList = getRentList();
                break;
            case BIND:
                deviceList = getBindList();
                break;
        }
        deviceLV.setAdapter(new DeviceListAdapter(this, deviceList));
    }

    private List<DeviceContent> getRentList() {
        List<DeviceContent> list = new ArrayList<DeviceContent>();
        for (int i = 0; i < 5; i++) {
            DeviceContent fakecontent = new DeviceContent();
            fakecontent.setDeviceType("绑定设备类型：专用设备");
            fakecontent.setDeviceUser("设备名称：**");
            fakecontent.setTimeRemaining("剩余时间：2天");
            list.add(fakecontent);
        }
        return list;
    }

    private List<DeviceContent> getBindList() {
        List<DeviceContent> list = new ArrayList<DeviceContent>();
        for (int i = 0; i < 5; i++) {
            DeviceContent fakecontent = new DeviceContent();
            fakecontent.setDeviceType("设备号：GT9128");
            fakecontent.setDeviceUser("设备名称：**");
            fakecontent.setTimeRemaining("绑定设备信息：**");
            fakecontent.setHoldType("持有方式：绑定");
            fakecontent.setOperateState("运行状态：待机");
            list.add(fakecontent);
        }
        return list;
    }

    private class DeviceListAdapter extends BaseAdapter {
        List<DeviceContent> list;
        Context context;

        public DeviceListAdapter(Context context, List<DeviceContent> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                        R.layout.device_list_item, null);
                holder.deviceType = (TextView) convertView
                        .findViewById(R.id.device_type_tv);
                holder.deviceUser = (TextView) convertView
                        .findViewById(R.id.device_user_tv);
                holder.remainingTime = (TextView) convertView
                        .findViewById(R.id.remaining_time_tv);
                holder.holdType = (TextView) convertView
                        .findViewById(R.id.hold_type_tv);
                holder.operateState = (TextView) convertView
                        .findViewById(R.id.operate_state_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.deviceType.setText(list.get(position).getDeviceType());
            holder.deviceUser.setText(list.get(position).getDeviceUser());
            holder.remainingTime.setText(list.get(position).getTimeRemaining());
            holder.holdType.setText(list.get(position).getHoldType());
            holder.operateState.setText(list.get(position).getOperateState());
            return convertView;
        }

    }

    private class ViewHolder {
        TextView deviceType;
        TextView deviceUser;
        TextView remainingTime;
        TextView holdType;
        TextView operateState;
    }

    private class rentButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            fillList(RENT);
            bindButton
                    .setBackgroundColor(getResources().getColor(R.color.white));
            rentButton.setBackgroundColor(getResources().getColor(
                    R.color.trans_30_blue));
            rentButton.setTextColor(getResources().getColor(R.color.blue_second));
            bindButton.setTextColor(getResources().getColor(R.color.black));
        }

    }

    private class bindButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            fillList(BIND);
            rentButton
                    .setBackgroundColor(getResources().getColor(R.color.white));
            bindButton.setBackgroundColor(getResources().getColor(
                    R.color.trans_30_blue));
            bindButton.setTextColor(getResources().getColor(R.color.blue_second));
            rentButton.setTextColor(getResources().getColor(R.color.black));
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
