package com.cn.fit.ui.patient.main.detectioncenter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.detect.BeanSubmitIntervItem;
import com.cn.fit.model.healthdiary.BeanIntervItem;
import com.cn.fit.model.healthdiary.BeanIntervNode;
import com.cn.fit.model.healthdiary.BeanNodeComplete;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.patient.main.healthdiary.ActivityHealthDiary;
import com.cn.fit.ui.patient.main.healthdiary.AscynNodeComplete;
import com.cn.fit.ui.patient.mycare.historicDetectionList;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.CircleImageView;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.HorizontalPicker;
import com.cn.fit.util.HorizontalPicker.OnItemClicked;
import com.cn.fit.util.HorizontalPicker.OnItemSelected;
import com.cn.fit.util.PickerView;
import com.cn.fit.util.PickerView.onSelectListener;
import com.cn.fit.util.StringUtil;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.segmentbutton.AndroidSegmentedControlView;
import com.cn.fit.util.segmentbutton.AndroidSegmentedControlView.OnSelectionChangedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

@SuppressLint("NewApi")
public class ActivityDetectionCenter extends ActivityBasic {
    private boolean isStart = true;
    private FButton button;
    private TextView date1, date2, date3;
    // private int person = 0;

    /**
     * 测量值
     */
    private long monitorDataId;
    /**
     * 体温或者搜收缩压的最大值
     */
    private long refValueMax_TWorSSY;
    /**
     * 体温或者收缩压的最小值
     */
    private long refValueMin_TWorSSY;
    private long refValueMax_SZY;
    private long refValueMin_SZY;
    /**
     * 单位
     */
    private String unit;
    private String dataName;

    private static final long TW = 1l;
    private static final long SSY = 2l;
    private static final long SZY = 3l;
    private static final long HR = 4l;
    private static final long XT = 5l;
    private static final long XY = 6l;
    private static final long TZ = 7l;
    private static final long TIZHONG = 8l;

    private static final long KFXT = 13l;
    private static final long ZCQXT = 14l;
    private static final long ZCHXT = 15;
    private static final long WUCQXT = 16l;
    private static final long WUCHXT = 17l;
    private static final long WANCQXT = 18l;
    private static final long WANCHXT = 19l;
    private static final long SQXT = 20l;
    private String DetectionValue, DetectionValue1;
    private String addResult = "false";
    private String addMessage = "false";
    private List<historicDetectionList> historicList;
    private String dataCode = "BP";
    private String nowChooseType = Constant.DETECTION_XUEYA;
    private String dataNum = "7";// 获取历史数据个数
    private BeanIntervNode beanIntervNode;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private CircleImageView myPhoto;
    /****************************************************
     * 图表相关
     */
    private LineChartView chart;
    private LineChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private int numberOfPoints = 7;
    private LinearLayout indicateLayout;
    private TextView indicate1, indicate2, indicate3, indicate4, historyCenter;
    /****************************************************
     * 修改测量类型选项相关
     */
    private HorizontalPicker picker;
    private CharSequence[] detectType;

    /**
     * whetherFromDiary=false:表示是一次普通的测量，true表示是一次来自健康日记的测量。
     */
    public static boolean whetherFromDiary = false;
    private boolean isFromAlter;

    private String detectionType;
    private ArrayList<String> detectionTypeList = new ArrayList<String>();
    private ArrayList<String> detectionTypeList1 = new ArrayList<String>();
    private static int pageType;
    private LinearLayout alterData;
    /**
     * 上一次选择的MonitorDataId
     */
    private long LastMonitorDataId;
    private String leftDefaultVale;
    private String rightDefaultVale;

//	private int state;

    /**
     * 修改前选择的时间段
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycare_detectioncenter);
        beanIntervNode = (BeanIntervNode) getIntent().getSerializableExtra(Constant.DETECTION_NODE);
        if (whetherFromDiary) {
            detectionTypeList = getIntent().getStringArrayListExtra("detectionType");
            if (detectionTypeList.size() != 0) {
                detectionType = detectionTypeList.get(0);
            }
        }

        if (detectionType == null) {
            detectionType = "SSY";
        }
        UtilsSharedData.initDataShare(this);
        try {
            initial();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * 重置图表
     */
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
            v.bottom = 35;
            v.top = 43;
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            v.bottom = 0;
            v.top = 300;
        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            v.bottom = 1;
            v.top = 35;
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
            v.bottom = 50;
            v.top = 100;
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
            v.bottom = 1;
            v.top = 50;
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
            v.bottom = 1;
            v.top = 400;
        }

        v.left = 0;
        if (numberOfPoints == 1) {
            v.right = 1;
        } else {
            v.right = numberOfPoints - 1;
        }
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        if (data != null) {
            data.getLines().clear();
            chart.setLineChartData(data);
        } else {
            //TODO很奇怪的处理方案
            //第一次进入，为了不让图表区域空白， 新建一张空白表
            ArrayList<Line> lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; j++) {
                values.add(new PointValue(Float.parseFloat(j + ""), -100));
            }
            initLines(lines, values, ChartUtils.COLORS[0]);
            data = new LineChartData(lines);
            data.setBaseValue(Float.NEGATIVE_INFINITY);
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            chart.setLineChartData(data);

        }
    }

    private void initial() throws Exception {
        // 将上次的旋转值缓存下来
        ((TextView) findViewById(R.id.middle_tv)).setText("监测中心");
        chart = (LineChartView) findViewById(R.id.menulist);
        chart.setViewportCalculationEnabled(false);
        chart.setValueSelectionEnabled(hasLabelForSelected);
        if (hasLabelForSelected) {
            hasLabels = false;
        }
        historyCenter = (TextView) findViewById(R.id.history_detectioncenter);
        indicateLayout = (LinearLayout) findViewById(R.id.indicate);
        indicate1 = (TextView) findViewById(R.id.indicate1);
        indicate2 = (TextView) findViewById(R.id.indicate2);
        indicate3 = (TextView) findViewById(R.id.indicate3);
        indicate4 = (TextView) findViewById(R.id.indicate4);
        indicate1.setOnClickListener(this);
        indicate2.setOnClickListener(this);
        indicate3.setOnClickListener(this);
        indicate4.setOnClickListener(this);
        // listView = (ListView) findViewById(R.id.lv_bloodPressure);
        date1 = (TextView) findViewById(R.id.date1_bloodPressure);
        date2 = (TextView) findViewById(R.id.date2_bloodPressure);
        date3 = (TextView) findViewById(R.id.date3_heartRate);

        button = (FButton) findViewById(R.id.receiveData);
        alterData = (LinearLayout) findViewById(R.id.alterData);
        alterData.setOnClickListener(this);
        button.setOnClickListener(this);
        historyCenter.setOnClickListener(this);
        button.setCornerRadius(3);
        historicList = new ArrayList<historicDetectionList>();
        // 人物选择滑动
        choosePerson();
        // 滑动选择测量类型
        chooseType();
        // 周选择
        choosePeriod();

    }

    // private String selectPerson = "我";
    // 人物选择滑动
    private void choosePerson() {

        options = AppMain.initImageOptions(R.drawable.default_user_icon, false);
        imageLoader = ImageLoader.getInstance();
        // adapter = new AdapterImage(this, options, imageLoader);
        myPhoto = (CircleImageView) findViewById(R.id.iv_myphoto);
        imageLoader
                .displayImage(
                        AbsParam.getBaseUrl()
                                + UtilsSharedData
                                .getValueByKey(Constant.USER_IMAGEURL),
                        myPhoto, options);

    }

    private OnTouchListener onTouchListener;

    /**
     * true 不滑动，false 滑动
     *
     * @param t
     * @return
     */
    private OnTouchListener onTouchEvent(boolean t) {
        final boolean on = t;
        onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return on;
            }
        };
        return onTouchListener;
    }

    private void chooseType() throws Exception {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        picker = (HorizontalPicker) findViewById(R.id.picker);
        detectType = getResources().getTextArray(R.array.values);
        picker.setScreenHeight(display.getHeight());
        picker.setScreenWidth(display.getWidth());
        picker.setValues(detectType);
        picker.setSideItems(2, display.getWidth(), display.getHeight());
        picker.setSelectedItem(0);
        picker.setOnTouchListener(onTouchEvent(false));
        if (whetherFromDiary) {
            if (StringUtil.isItEquals(detectionTypeList, "SSY")) {
                detectionTypeList1.add("血压");
            }
            if (StringUtil.isItEquals(detectionTypeList, "TW")) {
                detectionTypeList1.add("体温");
            }
            if (StringUtil.isItEquals(detectionTypeList, "XT")) {
                detectionTypeList1.add("血糖");
            }
            if (StringUtil.isItEquals(detectionTypeList, "XY")) {
                detectionTypeList1.add("血氧");
            }
            if (StringUtil.isItEquals(detectionTypeList, "TIZHI")) {
                detectionTypeList1.add("体脂");
            }
            if (StringUtil.isItEquals(detectionTypeList, "TIZHONG")) {
                detectionTypeList1.add("体重");
            }
            initPicker(0, "血压", display);
        } else {
            showProgressBar();
            getHistoricDetection task_hisD = new getHistoricDetection(false);
            task_hisD.execute();
        }
        // }
        picker.setOnItemClickedListener(new OnItemClicked() {

            @Override
            public void onItemClicked(int index) {
                // TODO Auto-generated method stub
                judgeChooseType(index, whetherFromDiary);
            }
        });
        picker.setOnItemSelectedListener(new OnItemSelected() {

            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                judgeChooseType(index, whetherFromDiary);
            }
        });
    }

    /**
     * 从日记界面跳转过来，用于判断当前监测类型选择项可以是哪些
     *
     * @param id
     * @param item
     */
    private void initPicker(int id, String item, Display display) {
        detectType = new CharSequence[detectionTypeList1.size()];
        detectionTypeList1.toArray(detectType);
        picker.setScreenHeight(display.getHeight());
        picker.setScreenWidth(display.getWidth());
        picker.setValues(detectType);
        picker.setSideItems(2, display.getWidth(), display.getHeight());
        picker.setOnTouchListener(onTouchEvent(false));
        picker.setSelectedItem(0);
        judgeChooseType(0, whetherFromDiary);
    }

    private void choosePeriod() {
        AndroidSegmentedControlView datePicker = (AndroidSegmentedControlView) findViewById(R.id.choosePeriod);
        datePicker
                .setOnSelectionChangedListener(new OnSelectionChangedListener() {

                    @Override
                    public void newSelection(String identifier, String value) {
                        // TODO Auto-generated method stub
                        ToastUtil.showMessage(value);
                    }
                });
    }

    // 修改测试类型
    private void judgeChooseType(int index, boolean choose) {
        if (choose) {
            String[] str = new String[]{"SSY", "TW", "XT", "XY", "TIZHI",
                    "TIZHONG"};
            for (int i = 0; i < detectType.length; i++) {
                if (str[i] == detectionType.toString()) {
                    index = i;
                    return;
                }
            }

            if (detectType[index].toString().equals("血压")) {
                date1.setText("收缩压："
                        + ((beanIntervNode.getState() != 7) ? ""
                        : (getSuitableItem("SSY").getValue() + "mmHg")));
                date2.setVisibility(View.VISIBLE);
                date2.setText("舒张压："
                        + ((beanIntervNode.getState() != 7) ? ""
                        : (getSuitableItem("SZY").getValue() + "mmHg")));
                date3.setVisibility(View.VISIBLE);
                date3.setText("心    率："
                        + ((beanIntervNode.getState() != 7) ? ""
                        : (getSuitableItem("XL").getValue() + "次/分")));
                nowChooseType = Constant.DETECTION_XUEYA;
                dataCode = "BP";
            } else {
                date2.setVisibility(View.GONE);
                date3.setVisibility(View.GONE);
                if (detectType[index].toString().equals("体温")) {
                    dataCode = "TW";
                    date1.setText("体温："
                            + ((beanIntervNode.getState() != 7) ? ""
                            : (getSuitableItem(dataCode).getValue() + "℃")));
                    nowChooseType = Constant.DETECTION_TIWEN;

                } else if (detectType[index].toString().equals("血糖")) {
                    dataCode = "XT";
                    date1.setText("血糖："
                            + ((beanIntervNode.getState() != 7) ? ""
                            : (getSuitableItem(dataCode).getValue() + "毫摩尔/升")));
                    nowChooseType = Constant.DETECTION_XUETANG;

                } else if (detectType[index].toString().equals("血氧")) {
                    dataCode = "XY";
                    date1.setText("血氧饱和度："
                            + ((beanIntervNode.getState() != 7) ? ""
                            : (getSuitableItem(dataCode).getValue() + "%")));
                    nowChooseType = Constant.DETECTION_XUEYANG;

                } else if (detectType[index].toString().equals("体脂")) {
                    dataCode = "TIZHI";
                    date1.setText("体脂率："
                            + ((beanIntervNode.getState() != 7) ? ""
                            : (getSuitableItem(dataCode).getValue() + "%")));
                    nowChooseType = Constant.DETECTION_TIZHI;

                } else if (detectType[index].toString().equals("体重")) {
                    dataCode = "TIZHONG";
                    date1.setText("体重："
                            + ((beanIntervNode.getState() != 7) ? ""
                            : (getSuitableItem(dataCode).getValue() + "千克")));
                    nowChooseType = Constant.DETECTION_TIZHONG;

                } else {
                    return;
                }
            }
        } else {
            if (detectType[index].toString().equals("血压")) {
                date1.setText("收缩压：");
                date2.setVisibility(View.VISIBLE);
                date2.setText("舒张压：");
                date3.setVisibility(View.VISIBLE);
                date3.setText("心    率：");
                nowChooseType = Constant.DETECTION_XUEYA;
                dataCode = "BP";
            } else {
                date2.setVisibility(View.GONE);
                date3.setVisibility(View.GONE);
                if (detectType[index].toString().equals("体温")) {
                    dataCode = "TW";
                    date1.setText("体温：");
                    nowChooseType = Constant.DETECTION_TIWEN;

                } else if (detectType[index].toString().equals("血糖")) {
                    dataCode = "XT";
                    date1.setText("血糖：");
                    nowChooseType = Constant.DETECTION_XUETANG;

                } else if (detectType[index].toString().equals("血氧")) {
                    dataCode = "XY";
                    date1.setText("血氧饱和度：");
                    nowChooseType = Constant.DETECTION_XUEYANG;

                } else if (detectType[index].toString().equals("体脂")) {
                    dataCode = "TIZHI";
                    date1.setText("体脂率：");
                    nowChooseType = Constant.DETECTION_TIZHI;

                } else if (detectType[index].toString().equals("体重")) {
                    dataCode = "TIZHONG";
                    date1.setText("体重：");
                    nowChooseType = Constant.DETECTION_TIZHONG;

                } else {
                    return;
                }
            }
        }


        showProgressBar();
        getHistoricDetection task_hisD = new getHistoricDetection(
                whetherFromDiary);
        task_hisD.execute();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {

            case R.id.alterData:
                if (button.getText() == "保存提交") {
                    isFromAlter = true;
                    showChooseDialog();
                }
                break;
            case R.id.receiveData:
                if (!isStart) {
                    isStart = true;
                    button.setButtonColor(getResources().getColor(
                            R.color.lightgreen));
                    button.setText("开始测量");
                    showProgressBar();
                    DetectionTask detectionTask = new DetectionTask(
                            whetherFromDiary);
                    detectionTask.execute();
                } else {
                    showChooseDialog();
                }
                break;
            case R.id.history_detectioncenter:
                Intent intent = new Intent(ActivityDetectionCenter.this,
                        ActivityHistrionicDataCenter.class);
                intent.putExtra("dataCode", dataCode);
                startActivity(intent);
                break;
        }
    }

    private ArrayList<String> xTimeList = new ArrayList<String>();

    // 更新图表数据
    private void ChartData() {
        // resetViewport();
        if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            indicate1.setText("收缩压(mmHg)");
            indicate2.setText("舒张压(mmHg)");
            indicate3.setText("心率(次/分)");
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.VISIBLE);
            indicate3.setVisibility(View.VISIBLE);
            indicate4.setVisibility(View.GONE);
        } else if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体温(摄氏度)");
        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.VISIBLE);
            indicate3.setVisibility(View.VISIBLE);
            indicate4.setVisibility(View.VISIBLE);
            indicate1.setText("凌晨");
            indicate2.setText("餐前");
            indicate3.setText("餐后");
            indicate4.setText("睡前");
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("血氧(百分比)");
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体重(千克)");
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体脂(百分比)");
        }
        if (historicList.size() == 0) {
            resetViewport();
            return;
        }
        xTimeList.clear();
        List<Line> lines = null;
        if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getValueTWorSSY().length() != 0) {
                    values.add(new PointValue(Float.parseFloat(j + ""), Float
                            .parseFloat(historicList.get(
                                    historicList.size() - j - 1)
                                    .getValueTWorSSY())));
                    xTimeList.add(historicList.get(historicList.size() - j - 1)
                            .getTime().substring(0, 5));
                }

            }
            numberOfPoints = 7;// values.size();
            resetViewport();
            initLines(lines, values, ChartUtils.COLORS[0]);

            List<PointValue> values1 = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                values1.add(new PointValue(Float.parseFloat(j + ""), Float
                        .parseFloat(historicList.get(
                                historicList.size() - j - 1).getValueSZY())));
            }
            initLines(lines, values1, ChartUtils.COLORS[2]);

            List<PointValue> values2 = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1).getValueHR()
                        .length() != 0) {
                    values2.add(new PointValue(Float.parseFloat(j + ""), Float
                            .parseFloat(historicList.get(
                                    historicList.size() - j - 1).getValueHR())));
                }

            }
            initLines(lines, values2, ChartUtils.COLORS[4]);

        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            int m1 = 0;
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getMonitorDataId() == KFXT) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values.add(new PointValue(
                                    Float.parseFloat(m1 + ""), Float
                                    .parseFloat(historicList
                                            .get(historicList.size()
                                                    - j - 1)
                                            .getValueTWorSSY())));
                        }
                    }

                    m1 += 3;
                    xTimeList.add("");
                    xTimeList.add("");
                    xTimeList.add(historicList.get(historicList.size() - j - 1)
                            .getDate().substring(5));
                }
            }
            numberOfPoints = 21;
            resetViewport();
            initLines(lines, values, ChartUtils.COLORS[0]);
            long[] tempZCQ = {ZCQXT, WUCQXT, WANCQXT};
            List<PointValue> values1 = new ArrayList<PointValue>();
            for (int n = 0; n < 3; n++) {
                int m2 = n;
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getMonitorDataId() == tempZCQ[n]) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values1.add(new PointValue(Float
                                    .parseFloat(m2 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));
                        }
                        m2 += 3;
                    }
                }
            }

            initLines(lines, values1, ChartUtils.COLORS[2]);

            List<PointValue> values2 = new ArrayList<PointValue>();
            long[] tempZCH = {ZCHXT, WUCHXT, WANCHXT};
            for (int n = 0; n < 3; n++) {
                int m3 = n;
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getMonitorDataId() == tempZCH[n]) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values2.add(new PointValue(Float
                                    .parseFloat(m3 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));

                        }
                        m3 += 3;
                    }
                }
            }
            initLines(lines, values2, ChartUtils.COLORS[4]);

            List<PointValue> values3 = new ArrayList<PointValue>();
            int m4 = 0;
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getMonitorDataId() == SQXT) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values3.add(new PointValue(Float
                                    .parseFloat(m4 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));
                        }
                    }

                    m4 += 3;
                }
            }

            initLines(lines, values3, ChartUtils.COLORS[3]);
        } else {
            // 其他值获取的方式一致
            lines = new ArrayList<Line>();
            for (int i = 0; i < 1; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        values.add(new PointValue(Float.parseFloat(j + ""),
                                Float.parseFloat(historicList.get(
                                        historicList.size() - j - 1)
                                        .getValueTWorSSY())));
                        xTimeList.add(historicList
                                .get(historicList.size() - j - 1).getTime()
                                .substring(0, 5));
                    }

                }
                numberOfPoints = 7;// values.size();
                resetViewport();
                initLines(lines, values, ChartUtils.COLORS[i]);
            }

        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < xTimeList.size(); ++i) {
            axisValues.add(new AxisValue(i).setLabel(xTimeList.get(i)));
        }
        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));

        chart.setLineChartData(data);
    }

    /**
     * 初始化每一个表曲线
     *
     * @param lines
     * @param values
     * @param color
     */
    private void initLines(List<Line> lines, List<PointValue> values, int color) {
        Line line = new Line(values);
        line.setColor(color);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        lines.add(line);
    }

    private String pickerLeftValue, pickerRightValue, pickerRightMostValue;

    // 弹出选择弹窗
    private void showChooseDialog() {
        monitorDataId = KFXT;
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(formChooseDialog());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
                    date1.setText("体温：" + pickerLeftValue + "."
                            + pickerRightValue + "℃");
                    if (whetherFromDiary) {
                        getSuitableItem("TW").setValue(Double.parseDouble(pickerLeftValue + "." + pickerRightValue));
                        beanIntervNode.setState(7);
                    }

                    monitorDataId = TW;
                } else if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
                    date1.setText("收缩压：" + pickerLeftValue + "mmHg");
                    date2.setText("舒张压：" + pickerRightValue + "mmHg");
                    date3.setText("心率：" + pickerRightMostValue + "次/分");
                    if (whetherFromDiary) {
                        getSuitableItem("SSY").setValue(Double.parseDouble(pickerLeftValue));
                        getSuitableItem("SZY").setValue(Double.parseDouble(pickerRightValue));
                        getSuitableItem("XL").setValue(Double.parseDouble(pickerRightMostValue));
                        beanIntervNode.setState(7);
                        beanIntervNode.setState(7);
                    }
                    monitorDataId = SZY;
                } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
                    String descXT = "凌晨";
                    if (monitorDataId == KFXT) {
                        descXT = "凌晨";
                    } else if (monitorDataId == ZCQXT) {
                        descXT = "空腹";
                    } else if (monitorDataId == ZCHXT) {
                        descXT = "早餐后";
                    } else if (monitorDataId == WUCQXT) {
                        descXT = "午餐前";
                    } else if (monitorDataId == WUCHXT) {
                        descXT = "午餐后";
                    } else if (monitorDataId == WANCQXT) {
                        descXT = "晚餐前";
                    } else if (monitorDataId == WANCHXT) {
                        descXT = "晚餐后";
                    } else if (monitorDataId == SQXT) {
                        descXT = "睡前";
                    }
                    LastMonitorDataId = monitorDataId;
                    date1.setText(descXT + "血糖：" + pickerLeftValue + "."
                            + pickerRightValue + "毫摩尔/升");
                    if (whetherFromDiary) {
                        getSuitableItem("XT").setValue(Double.parseDouble(pickerLeftValue + "." + pickerRightValue));
                        beanIntervNode.setState(7);
                    }
                    // monitorDataId=KFXT;
                } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
                    date1.setText("血氧饱和度：" + pickerLeftValue + "."
                            + pickerRightValue + "%");
                    if (whetherFromDiary) {
                        getSuitableItem("XY").setValue(Double.parseDouble(pickerLeftValue + "." + pickerRightValue));
                        beanIntervNode.setState(7);
                    }
                    monitorDataId = XY;
                } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
                    date1.setText("体脂率：" + pickerLeftValue + "."
                            + pickerRightValue + "%");
                    if (whetherFromDiary) {
                        getSuitableItem("TIZHI").setValue(Double.parseDouble(pickerLeftValue + "." + pickerRightValue));
                        beanIntervNode.setState(7);
                    }
                    monitorDataId = TZ;
                } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
                    date1.setText("体重：" + pickerLeftValue + "."
                            + pickerRightValue + "千克");
                    if (whetherFromDiary) {
                        getSuitableItem("TIZHONG").setValue(Double.parseDouble(pickerLeftValue + "." + pickerRightValue));
                        beanIntervNode.setState(7);
                    }
                    monitorDataId = TIZHONG;
                }
                if (isStart) {
                    isStart = false;
                    button.setButtonColor(getResources().getColor(
                            R.color.fbutton_color_orange));
                    button.setText("保存提交");
                    picker.setEnabled(false);
                }

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

    // 构建选择弹窗
    private LinearLayout formChooseDialog() {
        int leftNumMin = 0;
        int leftNumMax = 0;
        int rightNumMin = 0;
        int rightNumMax = 0;
        int rightMostMin = 0;
        int rightMostMax = 0;
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.choosepicker_double_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.indicate_Info);
        TextView type = (TextView) layout.findViewById(R.id.info_Type);

        if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
            title.setText("请选择体温数据");
            type.setText("度                         分");
            leftNumMin = 35;
            leftNumMax = 43;
            rightNumMin = 0;
            rightNumMax = 10;
            leftDefaultVale = "36";
            rightDefaultVale = "0";

        } else if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            title.setText("请选择血压数据");
            type.setText("收缩压       舒张压        心率");
            leftNumMin = 30;
            leftNumMax = 300;
            rightNumMin = 10;
            rightNumMax = 200;
            rightMostMin = 0;
            rightMostMax = 250;
            leftDefaultVale = "120";
            rightDefaultVale = "80";
            PickerView pickerView3 = (PickerView) layout
                    .findViewById(R.id.choose_Picker3);
            pickerView3.setVisibility(View.VISIBLE);
            List<String> data3 = new ArrayList<String>();
            for (int a = rightMostMin; a < rightMostMax; a++) {
                data3.add(a + "");
            }
            pickerView3.setData(data3, "75");// 心率默认75
            pickerRightMostValue = "75";// String.valueOf((rightMostMax +
            // rightMostMin) / 2);
            pickerView3.setOnSelectListener(new onSelectListener() {

                @Override
                public void onSelect(String text) {
                    // TODO Auto-generated method stub
                    pickerRightMostValue = text;
                }
            });
            pickerRightMostValue = pickerView3.getText();

        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            setSugarSegments(layout);
            title.setText("请选择血糖数据\n(单位：毫摩尔/升)");
            type.setText("整数位                小数位");
            leftNumMin = 1;
            leftNumMax = 35;
            rightNumMin = 0;
            rightNumMax = 10;
            leftDefaultVale = "4";
            rightDefaultVale = "0";
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
            title.setText("请选择血氧饱和度\n(单位：百分比)");
            type.setText("整数位                小数位");
            leftNumMin = 50;
            leftNumMax = 100;
            rightNumMin = 0;
            rightNumMax = 10;
            leftDefaultVale = "96";
            rightDefaultVale = "0";
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
            title.setText("请选择体重数据\n(单位：千克)");
            type.setText("整数位                小数位");
            leftNumMin = 1;
            leftNumMax = 399;
            rightNumMin = 0;
            rightNumMax = 10;
            leftDefaultVale = "80";
            rightDefaultVale = "0";
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
            title.setText("请选择体脂率\n(单位：百分比)");
            type.setText("整数位                小数位");
            leftNumMin = 1;
            leftNumMax = 49;
            rightNumMin = 0;
            rightNumMax = 10;
            leftDefaultVale = "23";
            rightDefaultVale = "0";
        }
        PickerView pickerView1 = (PickerView) layout
                .findViewById(R.id.choose_Picker1);
        List<String> data1 = new ArrayList<String>();
        for (int a = leftNumMin; a < leftNumMax; a++) {
            data1.add(a + "");
        }
        pickerView1.setData(data1, leftDefaultVale);

        PickerView pickerView2 = (PickerView) layout
                .findViewById(R.id.choose_Picker2);
        List<String> data2 = new ArrayList<String>();
        for (int a = rightNumMin; a < rightNumMax; a++) {
            data2.add(a + "");
        }
        pickerView2.setData(data2, rightDefaultVale);
        // 设置选择初始值
        pickerLeftValue = leftDefaultVale;// String.valueOf(((leftNumMin +
        // leftNumMax) / 2));
        pickerRightValue = rightDefaultVale; // String.valueOf(((rightNumMin +
        // rightNumMax) / 2));

        pickerView1.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                pickerLeftValue = text;
            }
        });

        pickerView2.setOnSelectListener(new onSelectListener() {

            @Override
            public void onSelect(String text) {
                pickerRightValue = text;
            }
        });
        pickerLeftValue = pickerView1.getText();
        pickerRightValue = pickerView2.getText();
        return layout;
    }

    private void setSugarSegments(LinearLayout layout) {
        final AndroidSegmentedControlView chooseSugarPicker1 = (AndroidSegmentedControlView) layout
                .findViewById(R.id.chooseSugar1);
        final AndroidSegmentedControlView chooseSugarPicker2 = (AndroidSegmentedControlView) layout
                .findViewById(R.id.chooseSugar2);
        chooseSugarPicker1.setVisibility(View.VISIBLE);
        chooseSugarPicker2.setVisibility(View.VISIBLE);
        chooseSugarPicker1
                .setOnSelectionChangedListener(new OnSelectionChangedListener() {

                    @Override
                    public void newSelection(String identifier, String value) {
                        try {
                            chooseSugarPicker2.setDefaultSelection(-1);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // TODO Auto-generated method stub
                        if (value.equals("凌晨")) {
                            monitorDataId = KFXT;
                        } else if (value.equals("空腹")) {
                            monitorDataId = ZCQXT;
                        } else if (value.equals("早餐后")) {
                            monitorDataId = ZCHXT;
                        } else if (value.equals("午餐前")) {
                            monitorDataId = WUCQXT;
                        }
                    }
                });
        chooseSugarPicker2
                .setOnSelectionChangedListener(new OnSelectionChangedListener() {

                    @Override
                    public void newSelection(String identifier, String value) {
                        // TODO Auto-generated method stub
                        try {
                            chooseSugarPicker1.setDefaultSelection(-1);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (value.equals("午餐后")) {
                            monitorDataId = WUCHXT;
                        } else if (value.equals("晚餐前")) {
                            monitorDataId = WANCQXT;
                        } else if (value.equals("晚餐后")) {
                            monitorDataId = WANCHXT;
                        } else if (value.equals("睡前")) {
                            monitorDataId = SQXT;
                        }
                    }
                });
        int top, bottom;
        if (isFromAlter) {
            if (LastMonitorDataId == KFXT) {
                top = 0;
                bottom = -1;
            } else if (LastMonitorDataId == ZCQXT) {
                top = 1;
                bottom = -1;
            } else if (LastMonitorDataId == ZCHXT) {
                top = 2;
                bottom = -1;
            } else if (LastMonitorDataId == WUCQXT) {
                top = 3;
                bottom = -1;
            } else if (LastMonitorDataId == WUCHXT) {
                top = -1;
                bottom = 0;
            } else if (LastMonitorDataId == WANCQXT) {
                top = -1;
                bottom = 1;
            } else if (LastMonitorDataId == WANCHXT) {
                top = -1;
                bottom = 2;
            } else {
                top = -1;
                bottom = 3;
            }
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String[] nowtime = formatter.format(curDate).split("\\:");
            if (Integer.parseInt(nowtime[0]) < 6) {
                top = 0;
                bottom = -1;
                monitorDataId = KFXT;
            } else if (Integer.parseInt(nowtime[0]) < 8
                    && Integer.parseInt(nowtime[0]) >= 6) {
                top = 1;
                bottom = -1;
                monitorDataId = ZCQXT;
            } else if (Integer.parseInt(nowtime[0]) < 10
                    && Integer.parseInt(nowtime[0]) >= 8) {
                top = 2;
                bottom = -1;
                monitorDataId = ZCHXT;
            } else if (Integer.parseInt(nowtime[0]) < 12
                    && Integer.parseInt(nowtime[0]) >= 10) {
                top = 3;
                bottom = -1;
                monitorDataId = WUCQXT;
            } else if (Integer.parseInt(nowtime[0]) < 15
                    && Integer.parseInt(nowtime[0]) >= 12) {
                top = -1;
                bottom = 0;
                monitorDataId = WUCHXT;
            } else if (Integer.parseInt(nowtime[0]) < 18
                    && Integer.parseInt(nowtime[0]) >= 15) {
                top = -1;
                bottom = 1;
                monitorDataId = WANCQXT;
            } else if (Integer.parseInt(nowtime[0]) < 20
                    && Integer.parseInt(nowtime[0]) >= 18) {
                top = -1;
                bottom = 2;
                monitorDataId = WANCHXT;
            } else {
                top = -1;
                bottom = 3;
                monitorDataId = SQXT;
            }
            // LastMonitorDataId = monitorDataId;
        }
        try {
            chooseSugarPicker1.setDefaultSelection(top);
            chooseSugarPicker2.setDefaultSelection(bottom);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据节点名称查询出节点对象
     *
     * @return
     */
    private BeanIntervItem getSuitableItem(String nodeName) {
        for (BeanIntervItem beanIntervItem : beanIntervNode.getIntervItemsList()) {
            if (beanIntervItem.getCode().equals(nodeName)) {
                return beanIntervItem;
            }
        }
        return null;
    }

    /**
     * 提交检测数据的类
     *
     * @author kuangtiecheng
     */
    private class DetectionTask extends AsyncTask<Integer, Integer, String> {
        String result = "";
        boolean choose;// true代表从日记界面进入，false代表普通进入
        String resultCode = "";
        String msg = "";

        public DetectionTask(boolean choose) {
            // TODO Auto-generated constructor stub
            this.choose = choose;
        }

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            if (choose) {
                param.put("patientPlanId",
                        ActivityHealthDiary.healthDiaryBean.getPatientPlanId()
                                + "");
                // 从康复日记跳转过来进行测量值上传的时候，采用以下方式
                if (monitorDataId == SZY) { // 如果传的是收缩压，先传收缩压，再传舒张压
                    DetectionValue = pickerLeftValue;
                    DetectionValue1 = pickerRightValue;
//					if (whetherInput == 2) {
//						pickerRightMostValue = String.valueOf(HeartRate);
//					}
                    ArrayList<BeanSubmitIntervItem> listBean = new ArrayList<BeanSubmitIntervItem>();
                    listBean.add(new BeanSubmitIntervItem(DetectionValue,
                            getSuitableItem("SSY").getNodeItemId(), getSuitableItem("SSY").getId()));
                    listBean.add(new BeanSubmitIntervItem(DetectionValue1,
                            getSuitableItem("SZY").getNodeItemId(), getSuitableItem("SZY").getId()));
                    listBean.add(new BeanSubmitIntervItem(pickerRightMostValue,
                            getSuitableItem("XL").getNodeItemId(), getSuitableItem("XL").getId()));
                    param.put("values", resultToJsonInterv(listBean));
                    getNetWork(result, param, true);

                } else {
                    // 其他数据都是用这个方式提交
                    DetectionValue = String.valueOf(Integer
                            .parseInt(pickerLeftValue)
                            + Integer.parseInt(pickerRightValue) * 0.1);// 温度值
                    ArrayList<BeanSubmitIntervItem> listBean = new ArrayList<BeanSubmitIntervItem>();
                    listBean.add(new BeanSubmitIntervItem(DetectionValue,
                            getSuitableItem(dataCode).getNodeItemId(), getSuitableItem(dataCode).getId()));
                    param.put("values", resultToJsonInterv(listBean));
                    getNetWork(result, param, true);
                }
            } else {
                // 如果是从顶栏右侧进入检测中心时
                param.put("patientId", userId + "");
                param.put("values", resultToJson());
                getNetWork(result, param, true);
            }
            return null;
        }

        /**
         * 正常提交的情况下，将数据组包
         *
         * @return
         */
        private String resultToJson() {
            String retString;
            if (monitorDataId == 2 || monitorDataId == 3 || monitorDataId == 4) {
                // pickerLeftValue:收缩压 pickerRightValue:舒张压
//				if (whetherInput == 2) {
//					pickerRightMostValue = String.valueOf(HeartRate);
//				}
                retString = "{{monitorDataId:2,value:" + pickerLeftValue
                        + "},{monitorDataId:3,value:" + pickerRightValue
                        + "},{monitorDataId:4,value:" + pickerRightMostValue
                        + "}}";
            } else {
                DetectionValue = String.valueOf(Integer
                        .parseInt(pickerLeftValue)
                        + Integer.parseInt(pickerRightValue) * 0.1);// 温度值
                retString = "{{monitorDataId:" + monitorDataId + ",value:"
                        + DetectionValue + "}}";
            }
            return retString;
        }

        /**
         * 日记提交的情况下，将数据组包
         *
         * @return
         */
        private String resultToJsonInterv(
                ArrayList<BeanSubmitIntervItem> listBean) {
            String retString = "{";
            int size = listBean.size();
            for (int i = 0; i < size; i++) {
                retString += "{valueId:" + listBean.get(i).getValueId()
                        + ",nodeItemId:" + listBean.get(i).getNodeItemId()
                        + ",value:" + listBean.get(i).getValue() + "}";
                if (i != size - 1) {
                    // 不是最后一个数据，则在末尾需要添加逗号
                    retString += ",";
                }
            }
            retString += "}";
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//			hideProgressBar();
            if (choose) {
                if (resultCode.equals("-1")) {
//					showProgressBar();
                    AscynNodeComplete ncTask = new AscynNodeComplete(
                            ActivityDetectionCenter.this,
                            ActivityHealthDiary.healthDiaryBean
                                    .getPatientPlanId(), beanIntervNode.getId()) {

                        @Override
                        protected void onPostExecute(
                                BeanNodeComplete beanNodeComplete) {
                            super.onPostExecute(beanNodeComplete);
                            hideProgressBar();
                            if (beanNodeComplete != null) {
                                if (beanNodeComplete.getSuccess()
                                        .equals("true")) {
                                    getHistoricDetection task_hisD = new getHistoricDetection(
                                            false);
                                    task_hisD.execute();
                                } else {
                                    ToastUtil.showMessage(beanNodeComplete
                                            .getMsg());
                                }
                            }
                        }

                    };
                    ncTask.execute();
                } else {
                    hideProgressBar();
                    ToastUtil.showMessage("测量数据提交失败，请稍后重试");
                }

            } else {
                if (addResult.equals("true")) {
//					showProgressBar();
                    getHistoricDetection task_hisD = new getHistoricDetection(
                            false);
                    task_hisD.execute();
                } else {
                    hideProgressBar();
                    ToastUtil.showMessage("增加数据未成功");
                    picker.setEnabled(true);
                }
            }
        }

        private void getNetWork(String result, Map<String, String> param,
                                boolean whetherGo) {
            try {
                if (choose) {
                    result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                            + "/interv/item/edit2", param, "utf-8");
                    Log.i("result", result);
                    JSONObject json = new JSONObject(result);
                    if (whetherGo) {
                        resultCode = json.getString("statusCode");
                    } else {
                        resultCode = " ";
                    }

                    // msg=json.getString("detail");
                } else {
                    result = NetTool.sendHttpClientPost(AbsParam.getBaseUrl()
                            + "/interv/monitordata/add2", param, "utf-8");
                    Log.i("result", result);
                    JSONObject addObject = new JSONObject(result);
                    JSONObject addObjectSon = new JSONObject(
                            addObject.getString("data"));
                    addResult = addObjectSon.getString("success");
                    addMessage = addObjectSon.getString("msg");
                }
            } catch (Exception e) {
                hideProgressBar();
            }
        }

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        whetherFromDiary = false;
        super.onDestroy();
    }

    /**
     * 获得历史数据的类
     */
    private class getHistoricDetection extends
            AsyncTask<Integer, Integer, String> {
        String result = "";
        boolean choose;

        public getHistoricDetection(boolean choose) {
            this.choose = choose;
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            HashMap<String, String> param = new HashMap<String, String>();
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("patientId", userId + "");
            param.put("dataCode", dataCode);
            param.put("num", dataNum);
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/monitordata/record/history", param, "utf-8");
                Log.i("result", result);
                JSONObject data = new JSONObject(result);
                JSONObject historicDection = new JSONObject(
                        data.getString("data"));
                if (dataCode.equals("BP")) {
                    JSONObject historicDection_SSY = new JSONObject(
                            historicDection.getString("SSY"));
                    JSONObject historicDection_SZY = new JSONObject(
                            historicDection.getString("SZY"));
                    JSONObject historicDection_HR = new JSONObject(
                            historicDection.getString("XL"));
                    monitorDataId = historicDection_SSY
                            .getLong("monitorDataId");
                    unit = historicDection_SSY.getString("unit");
                    // dataCode=historicDection_SSY.getString("dataCode");
                    dataName = historicDection_SSY.getString("dataName");
                    refValueMax_TWorSSY = historicDection_SSY
                            .getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_SSY
                            .getLong("refValueMin");
                    refValueMax_SZY = historicDection_SZY
                            .getLong("refValueMax");
                    refValueMin_SZY = historicDection_SZY
                            .getLong("refValueMin");
                    JSONArray array_SSY = new JSONArray(
                            historicDection_SSY.getString("value"));
                    JSONArray array_SZY = new JSONArray(
                            historicDection_SZY.getString("value"));
                    JSONArray array_HR = new JSONArray(
                            historicDection_HR.getString("value"));
                    historicList.clear();
                    for (int i = 0; i < array_SSY.length(); i++) {
                        JSONObject object_SSY = array_SSY.getJSONObject(i);
                        JSONObject object_SZY = array_SZY.getJSONObject(i);
                        JSONObject object_HR = array_HR.getJSONObject(i);
                        historicDetectionList hDList = new historicDetectionList();
                        hDList.setDate(object_SSY.getString("date"));
                        hDList.setTime(object_SSY.getString("time"));
                        hDList.setValueTWorSSY(object_SSY.getString("value"));
                        hDList.setValueSZY(object_SZY.getString("value"));
                        hDList.setValueHR(object_HR.getString("value"));
                        historicList.add(hDList);
                    }
                } else if (dataCode.equals("XT")) {
                    ArrayList<JSONObject> historicDection_XT = new ArrayList<JSONObject>();
                    String[] tempXtType = {"KFXT", "ZCQXT", "ZCHXT", "WUCQXT",
                            "WUCHXT", "WANCQXT", "WANCHXT", "SQXT"};
                    for (int m = 0; m < tempXtType.length; m++) {
                        if (!historicDection.getString(tempXtType[m]).equals(
                                "null")) {
                            historicDection_XT.add(new JSONObject(
                                    historicDection.getString(tempXtType[m])));
                        }
                    }

                    ArrayList<JSONArray> historicDection_XTData = new ArrayList<JSONArray>();
                    for (int i = 0; i < historicDection_XT.size(); i++) {
                        historicDection_XTData.add(new JSONArray(
                                historicDection_XT.get(i).getString("value")));
                    }

                    historicList.clear();
                    for (int j = 0; j < historicDection_XTData.size(); j++) {
                        for (int k = 0; k < historicDection_XTData.get(j)
                                .length(); k++) {
                            JSONObject object_XT = historicDection_XTData
                                    .get(j).getJSONObject(k);
                            historicDetectionList hDList = new historicDetectionList();
                            hDList.setDate(object_XT.getString("date"));
                            hDList.setTime(object_XT.getString("time"));
                            hDList.setMonitorDataId(KFXT + j);
                            hDList.setValueTWorSSY(object_XT.getString("value"));
                            historicList.add(hDList);
                        }
                    }

                } else {
                    // 其他的值获取历史的方式相同
                    JSONObject historicDection_TW = new JSONObject(
                            historicDection.getString(dataCode));
                    JSONArray array_tw = new JSONArray(
                            historicDection_TW.getString("value"));
                    monitorDataId = historicDection_TW.getLong("monitorDataId");
                    unit = historicDection_TW.getString("unit");
                    // dataCode=historicDection_TW.getString("dataCode");
                    dataName = historicDection_TW.getString("dataName");
                    refValueMax_TWorSSY = historicDection_TW
                            .getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_TW
                            .getLong("refValueMin");
                    historicList.clear();
                    for (int i = 0; i < array_tw.length(); i++) {
                        JSONObject object_tw = array_tw.getJSONObject(i);
                        historicDetectionList hDList_tw = new historicDetectionList();
                        hDList_tw.setDate(object_tw.getString("date"));
                        hDList_tw.setTime(object_tw.getString("time"));
                        // days[6-i]=object_tw.getString("time").substring(0,
                        // 5);
                        hDList_tw.setValueTWorSSY(object_tw.getString("value"));
                        historicList.add(hDList_tw);
                    }
                }

            } catch (Exception e) {
                hideProgressBar();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            ChartData();
            picker.setEnabled(true);
        }

    }

}
