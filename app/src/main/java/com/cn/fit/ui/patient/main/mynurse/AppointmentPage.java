package com.cn.fit.ui.patient.main.mynurse;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.consult.BeanConsultResult;
import com.cn.fit.model.consult.BeanOtherConsult;
import com.cn.fit.model.consult.BeanVideoConsult;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;
import com.cn.fit.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppointmentPage extends ActivityBasic {
    private ScrollView scrollView;
    private long nurseID = 1;
    private String selectDate = "";
    private String diseaseName = "糖尿病";
    private ListView lv_date, lv_morning, lv_afternoon;
    private TextView tv_name, tv_hosptial, tv_office, tv_introduction, tv_position;
    private ImageView iv;
    private static Boolean changeColor1 = false, changeColor2 = false;
    private Dialog alterDialog2;
    private SimpleAdapter adapter;
    private MyAdapter adapter1, adapter2;
    private int Position;
    List<Map<String, Object>> Items;
    private int appoint_Type;
    private String isItemClickable = "", isItemClickable1 = "", appoint_Time;
    private String hour, minute;

    private String nurseName;
    private String positionName;
    private String hospitalName;
    private String ability;
    private String office;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity);
        Intent intent = getIntent();
        appoint_Type = intent.getIntExtra("appointType", 1);
        diseaseName = intent.getStringExtra("diseaseName");
        nurseID = intent.getLongExtra("nurseID", 1);

        nurseName = intent.getStringExtra("nurseName");
        positionName = intent.getStringExtra("positionName");
        hospitalName = intent.getStringExtra("hospitalName");
        ability = intent.getStringExtra("ability");
        office = intent.getStringExtra("office");

        initView();
        showProgressBar();
        new Thread(runnableBeanConsult).start();
    }

    private void initView() {
        // TODO Auto-generated method stub
        String title = "";
        switch (appoint_Type) {
            case 1:
                title = "视频预约";
                break;
            case 2:
                title = "电话预约";
                break;
            case 3:
                title = "门诊预约";
                break;
            default:
                break;
        }
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText(title);
        lv_date = (ListView) findViewById(R.id.date_nurse_activity);
        lv_date.setVerticalScrollBarEnabled(false);
        lv_morning = (ListView) findViewById(R.id.morning_nurse_activity);
        lv_morning.setVerticalScrollBarEnabled(false);
        lv_afternoon = (ListView) findViewById(R.id.afternoon_nurse_activity);
        lv_afternoon.setVerticalScrollBarEnabled(false);
        tv_name = (TextView) findViewById(R.id.name_nurse_activity);
        tv_hosptial = (TextView) findViewById(R.id.hospital_nurse_activity);
        tv_position = (TextView) findViewById(R.id.position_nurse_activity);
        tv_office = (TextView) findViewById(R.id.office_nurse_activity);
        tv_introduction = (TextView) findViewById(R.id.introduction_nurse_activity);
        scrollView = (ScrollView) findViewById(R.id.scrollView_telAppointment);
        scrollView.scrollTo(0, scrollView.getBottom());
        iv = (ImageView) findViewById(R.id.iv_nurse_activity);

        tv_name.setText(nurseName);
        tv_hosptial.setText(hospitalName);
        tv_position.setText(positionName);
        tv_office.setText(office);
        tv_introduction.setText("擅长:" + ability);
    }


    //LOAD在执行完成网络数据请求之后执行
    private void updateUI() {
        Items = new ArrayList<Map<String, Object>>();
        if (appoint_Type == 1) {
            for (int i = 0; i < beanVideoConsultList.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("date", beanVideoConsultList.get(i).getDateString());
                if (beanVideoConsultList.get(i).getWorkTime() == null) {
                    continue;
                }
                String availableAM = beanVideoConsultList.get(i).getWorkTime().get(0).getAvilable();
                item.put("appoint", availableAM.equals("YES") ? "预约" : "已满");
                String availablePM = beanVideoConsultList.get(i).getWorkTime().get(1).getAvilable();
                item.put("appoint2", availablePM.equals("YES") ? "预约" : "已满");
                Items.add(item);
            }
        } else {
            for (int i = 0; i < beanOtherConsultList.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("date", beanOtherConsultList.get(i).getDateString());
                long foreNoon = beanOtherConsultList.get(i).getForeNoon();
                long foreNoonNum = beanOtherConsultList.get(i).getForeNoonNum();
                item.put("appoint", foreNoon == 1 ? "可预约：" + foreNoonNum : "已满");
                long afterNoon = beanOtherConsultList.get(i).getAfterNoon();
                long afterNoonNum = beanOtherConsultList.get(i).getAfterNoonNum();
                item.put("appoint2", afterNoon == 1 ? "可预约：" + afterNoonNum : "已满");
                Items.add(item);
            }
        }
        adapter = new SimpleAdapter(AppointmentPage.this, Items, R.layout.list_item_black, new String[]{"date"}, new int[]{R.id.list_item_title_black});
        lv_date.setAdapter(adapter);
        adapter1 = new MyAdapter(this, true);
        adapter2 = new MyAdapter(this, false);
        lv_morning.setAdapter(adapter1);
        lv_afternoon.setAdapter(adapter2);
        lv_morning.setOnItemClickListener(mLeftListOnItemClick);
        lv_afternoon.setOnItemClickListener(mRightListOnItemClick);
        banListViewScroll();
    }

    //请求护士排班信息
    private Map<String, String> map;
    private String retStr;//获取到返回的json数据
    private List<BeanVideoConsult> beanVideoConsultList;
    private List<BeanOtherConsult> beanOtherConsultList;
    //网络请求
    Runnable runnableBeanConsult = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/yyzx/app/queryworkperiodbynurse";

        @Override
        public void run() {
            try {
                map = new HashMap<String, String>();
                map.put("nurseID", String.valueOf(nurseID));
                map.put("reserveType", String.valueOf(appoint_Type));// 表示专家用户id
                retStr = NetTool.sendPostRequest(url, map, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getVisitTime(retStr);

                Message msg = new Message();
                if (retStr == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                consultInfoHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Handler consultInfoHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    //展示获取的数据bean
                    hideProgressBar();
                    updateUI();
                    break;

                default:

                    break;
            }
        }
    };

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getVisitTime(String jsonString) throws Exception {
        Gson gson = new Gson();
        if (appoint_Type == 1) {
            //视频预约
            beanVideoConsultList = gson.fromJson(jsonString, new TypeToken<List<BeanVideoConsult>>() {
            }.getType());
        } else {
            //其他预约
            beanOtherConsultList = gson.fromJson(jsonString, new TypeToken<List<BeanOtherConsult>>() {
            }.getType());
        }
    }

    //提交预约咨询信息
    private Map<String, String> map1;
    private String retStr1;//获取到返回的json数据
    private BeanConsultResult beanConsultResult;
    //网络请求
    Runnable runnableBeanSubmit = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/yyzx/app/reservenurse";

        @Override
        public void run() {
            try {
                UtilsSharedData.initDataShare(AppointmentPage.this);
                long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
                map1 = new HashMap<String, String>();
                map1.put("patientID", userId + "");// 表示用户id
                map1.put("nurseID", String.valueOf(nurseID));// 表示专家用户id
                map1.put("dataString", selectDate);// 表示专家用户id
                map1.put("periodList", convertInt(appoint_Time));// 表示专家用户id
                map1.put("diseaseType", diseaseName);
                map1.put("adviceType", appoint_Type + "");
                retStr1 = NetTool.sendPostRequest(url, map1, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getSubmitReturn(retStr1);

                Message msg = new Message();
                if (retStr1 == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                submitHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Handler submitHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    //展示获取的数据bean
                    hideProgressBar();
                    jumpToSelfstatement();
                    Toast.makeText(getApplicationContext(), beanConsultResult.getDetail(), Toast.LENGTH_SHORT).show();
                    break;

                default:

                    break;
            }
        }
    };

    //跳转到新增病情自述界面
    private void jumpToSelfstatement() {
        startActivity(AddMyStatement.class, Constant.RESERVE_ID, beanConsultResult.getReserveID());
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getSubmitReturn(String jsonString) throws Exception {
        Gson gson = new Gson();
        beanConsultResult = gson.fromJson(jsonString, BeanConsultResult.class);
    }


    /**
     * 禁止listview滑动
     *
     * @author kuangtiecheng
     */
    private void banListViewScroll() {
        lv_date.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        lv_morning.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        lv_afternoon.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    AdapterView.OnItemClickListener mLeftListOnItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            changeColor1 = !changeColor1;
            String str = isItemClickable.substring(6 - arg2, 7 - arg2);//
            if (str.equals("1")) {
                if (appoint_Type == 1) {
                    selectDate = beanVideoConsultList.get(arg2).getDateString();
                    popup(true, convertTime(beanVideoConsultList.get(arg2).getWorkTime().get(0).getPeriodList()));
                } else {
                    selectDate = beanOtherConsultList.get(arg2).getDateString();
                    appoint_Time = "上午";
                    show();
                }
                adapter1.setSelectItem(arg2);
                adapter1.notifyDataSetInvalidated();
            } else {
//            	Toast.makeText(getApplicationContext(), "对不起，这个时间段已预约满，请更换",Toast.LENGTH_SHORT).show();
            }
        }

    };
    AdapterView.OnItemClickListener mRightListOnItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            adapter2.setSelectItem(arg2);
            adapter2.notifyDataSetInvalidated();
            changeColor2 = !changeColor2;
            String str1 = isItemClickable1.substring(6 - arg2, 7 - arg2);
            if (str1.equals("1")) {
                if (appoint_Type == 1) {
                    selectDate = beanVideoConsultList.get(arg2).getDateString();
                    popup(true, convertTime(beanVideoConsultList.get(arg2).getWorkTime().get(1).getPeriodList()));
                } else {
                    selectDate = beanOtherConsultList.get(arg2).getDateString();
                    appoint_Time = "下午";
                    show();
                }
                adapter2.setSelectItem(arg2);
                adapter2.notifyDataSetInvalidated();
            } else {
//            	Toast.makeText(getApplicationContext(), "对不起，这个时间段已预约满，请更换", 1).show();
            }
        }

    };

    //将后台传输过来的时间int转化为string类型的时间
    private List<String> convertTime(List<Integer> time) {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < time.size(); i++) {
            temp.add(Constant.timeList[time.get(i) - 1]);
        }
        return temp;
    }

    //将时间值转换为后台可以识别的intlist
    private String convertInt(String time) {
        if (time.equals("上午")) {
            time = "0:00-0:15";
        } else if (time.equals("下午")) {
            time = "23:45-0:00";
        }
        List lista = new ArrayList<String>();
        for (int i = 0; i < Constant.timeList.length; i++) {
            lista.add(Constant.timeList[i]);
        }
        return String.valueOf((lista.indexOf(time) + 1));
    }

    private void popup(Boolean i, List<String> id) {
//		String title;
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_single_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.indicate_Info);
        PickerView minute_pv = (PickerView) layout.findViewById(R.id.minute_pv);
        if (i) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) minute_pv.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.width = 430;// 控件的宽强制设成30
            minute_pv.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            title.setText("请选择视频预约时间");
        } else {
            title.setText("请选择电话预约时间");
        }
        List<String> data = new ArrayList<String>();
        for (int a = 0; a < id.size(); a++) {
            data.add(id.get(a));
        }
        minute_pv.setData(data);
        appoint_Time = minute_pv.getText();
        minute_pv.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                appoint_Time = text;
            }
        });
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void show() {
        String title = "";
        switch (appoint_Type) {
            case 1:
                title = "视频咨询";
                break;
            case 2:
                title = "电话咨询";
                break;
            case 3:
                title = "门诊咨询";
                break;
            default:
                break;
        }
        String content = "您确定预约 " + hospitalName + " " + office + " " + nurseName + " " + selectDate + " " + appoint_Time + " " + title;
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(Color.RED), 5, content.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alterDialog2 = new CustomDialog.Builder(this).setTitle("提示").setMessage(style).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
//					startActivity(AddMyStatement.class);
                alterDialog2.dismiss();
                new Thread(runnableBeanSubmit).start();
                showProgressBar();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                alterDialog2.dismiss();
            }
        })
                .create();
        alterDialog2.show();

//		}else if()

    }

    public final class ViewHolder {
        public TextView dateText;
        public TextView morningText;
        public TextView afternoonText;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Boolean i;

        public MyAdapter(Context context, Boolean i) {
            this.mInflater = LayoutInflater.from(context);
            this.i = i;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return Items.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return Items.get(arg0);
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_white, null);
                holder.morningText = (TextView) convertView.findViewById(R.id.list_item_title_white);
                holder.afternoonText = (TextView) convertView.findViewById(R.id.list_item_title_white2);
                if (i) {
                    holder.afternoonText.setVisibility(View.GONE);
                } else {
                    holder.morningText.setVisibility(View.GONE);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.morningText.setText((String) Items.get(position).get("appoint"));
            holder.afternoonText.setText((String) Items.get(position).get("appoint2"));
            if (Items.get(position).get("appoint").equals("已满")) {
                holder.morningText.setBackgroundColor(getResources().getColor(R.color.lightgray));
                isItemClickable = "0" + isItemClickable;
            } else {
                //	holder.morningText.setClickable(false);
                holder.morningText.setBackgroundColor(getResources().getColor(R.color.blue_second));
                isItemClickable = "1" + isItemClickable;
            }
            if (Items.get(position).get("appoint2").equals("已满")) {
                holder.afternoonText.setBackgroundColor(getResources().getColor(R.color.lightgray));
                isItemClickable1 = "0" + isItemClickable1;
            } else {
                holder.afternoonText.setBackgroundColor(getResources().getColor(R.color.blue_second));
                isItemClickable1 = "1" + isItemClickable1;
            }
            if (position == selectItem) {
                Position = position;

            }
            return convertView;
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        private int selectItem = -1;
    }
}
