package com.cn.fit.ui.patient.others.myfavor;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.nurse.BeanNurseInfo;
import com.cn.fit.ui.basic.ActivityBasicListView;
import com.cn.fit.ui.patient.main.mynurse.NursePage;
import com.cn.fit.ui.patient.main.mynurse.PopMenu;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityCollectNurses extends ActivityBasicListView implements PopupWindow.OnDismissListener {
    private static String queryNurse = "queryfavornurses";
    private TextView chooseTypeTv;
    private PopMenu popMenuLeft;
    private List<BeanNurseInfo> nurseList, tempnurseList;
    private DoctorListAdapter doctorListAdapter;
    String itemName[] = {"收藏", "关注"};
    //	private ImageView search;
    private TextView search;
    private TextView title;


    private String[] nurseArray = {"李芳芳", "刘芳芳", "王芳芳"};
    private String[] jobArray = {"医生", "护士", "护士长"};
    private String[] illnessArray = {"肺结核", "慢性胃炎", "跌倒预防"};

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
        setContentView(R.layout.my_res_collect_nurses);
        initView();
        initSearchView();
    }

    private void initView() {

//		chooseTypeTv = (TextView) findViewById(R.id.choose_type_tv);
//		chooseTypeTv.setOnClickListener(this);
//		listView = (ListView) findViewById(R.id.nurse_list);
//		listView.setAdapter(new DoctorListAdapter(this));
        search = (TextView) findViewById(R.id.right_tv);
        title = (TextView) findViewById(R.id.middle_tv);
        search.setVisibility(View.GONE);
        search.setText("");
        search.setBackgroundResource(R.drawable.search_icon);
        title.setText("收藏专家");
        search.setOnClickListener(this);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent();
//				intent.setClass(CollectNursesActivity.this, MyCareNurse.class);
//				intent.putExtra("nurseID", 2);
//				intent.putExtra("diseaseName", "糖尿病");
//				intent.putExtra(Constant.PAGE_TYPE, 0);
//				startActivity(intent);
//
//			}
//		});
        doctorListAdapter = new DoctorListAdapter(this);
        nurseList = new ArrayList<BeanNurseInfo>();
        fakeDoctorList();
        tempnurseList = new ArrayList<BeanNurseInfo>();
        listView = (XListView) findViewById(R.id.nurse_list);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.setClass(ActivityCollectNurses.this, NursePage.class);
                intent.putExtra("nurseID", 2);
                intent.putExtra("diseaseName", "糖尿病");
                intent.putExtra(Constant.PAGE_TYPE, 0);
                intent.putExtra("resume", "非常好");
                startActivity(intent);
//				Intent intent = new Intent();
//				intent.setClass(CollectNursesActivity.this, NursePage.class);
//				intent.putExtra("nurseID", nurseList.get(arg2-1).getNurseID());
//				intent.putExtra("resume", nurseList.get(arg2-1).getResume());
//				intent.putExtra("diseaseName", "糖尿病");
//				intent.putExtra(Constant.PAGE_TYPE, 0);
//				startActivity(intent);
            }
        });
        listView.setAdapter(doctorListAdapter);
    }

    @SuppressWarnings("deprecation")
    private void initSearchView() {
        // //搜索框部分
        LayoutInflater mInflater = LayoutInflater.from(this);
        mainLayout = (LinearLayout) findViewById(R.id.linearlayout1);
        titleBarLayout = (RelativeLayout) findViewById(R.id.nurse_title_bar_layout);
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
        for (int i = 0; i < nurseArray.length; i++) {
            HashMap<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("ItemImage", R.drawable.myres_myhelp);
            listItem.put("nurse", nurseArray[i]);
            listItem.put("job", jobArray[i]);
            listItem.put("illness", illnessArray[i]);
            listItems.add(listItem);
            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItems,
                    R.layout.doctor_list_item, new String[]{"ItemImage", "nurse", "job",
                    "illness"}, new int[]{R.id.doctor_avatar, R.id.doctor_name_tv,
                    R.id.doctor_position_tv, R.id.doctor_field_tv});
            resultListView.setAdapter(listItemAdapter);
        }
        ;
    }

    private class QueryExpertTask extends AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", "1");
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String a = AbsParam.getBaseUrl() + "/yyzx/app/" + queryNurse;
                Log.i("result", a);
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/app/healthrecord/" + queryNurse, param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                nurseList.clear();
            }

            for (BeanNurseInfo tmp : tempnurseList) {
                nurseList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            doctorListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        tempnurseList = gson.fromJson(jsonString,
                new TypeToken<List<BeanNurseInfo>>() {
                }.getType());
        if (tempnurseList.size() < 10) {
            canLoadMore = false;
        } else {
            canLoadMore = true;
        }
    }

    private void fakeDoctorList() {
        for (int i = 0; i < 5; i++) {
            BeanNurseInfo content = new BeanNurseInfo();
            content.setNurseName("李芳芳   护士长");
            content.setSubDepartmentName("跌倒预防");
            nurseList.add(content);
        }
    }

    private class DoctorListAdapter extends BaseAdapter {
        private Context context;

        public DoctorListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return nurseList.size();
        }

        @Override
        public Object getItem(int position) {
            return nurseList.get(position);
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
                        R.layout.doctor_list_item, null);
                holder.doctorAvatarIV = (ImageView) convertView
                        .findViewById(R.id.doctor_avatar);
                holder.doctorFieldTV = (TextView) convertView
                        .findViewById(R.id.doctor_field_tv);
                holder.doctorNameTV = (TextView) convertView
                        .findViewById(R.id.doctor_name_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.doctorAvatarIV.setImageBitmap((BitmapFactory.decodeResource(
                    getResources(), R.drawable.myres_myhelp)));
            holder.doctorNameTV.setText(nurseList.get(position)
                    .getNurseName() + "  " + nurseList.get(position).getPositionName());
            holder.doctorFieldTV
                    .setText(nurseList.get(position).getSubDepartmentName());
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView doctorAvatarIV;
        TextView doctorNameTV;
        TextView doctorFieldTV;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//		case R.id.choose_type_tv:
//			popMenuLeft = new PopMenu(this);
//			popMenuLeft.addItems(itemName);
//			popMenuLeft.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					chooseTypeTv.setText(itemName[arg2]);
//					popMenuLeft.dismiss();
//				}
//			});
//			popMenuLeft.showAsDropDown(v);
//			break;

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