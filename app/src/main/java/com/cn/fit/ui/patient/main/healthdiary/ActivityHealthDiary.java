package com.cn.fit.ui.patient.main.healthdiary;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.broadcast.DiaryReceiver;
import com.cn.fit.http.NetTool;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.model.healthdiary.BeanIntervItem;
import com.cn.fit.model.healthdiary.BeanIntervNode;
import com.cn.fit.model.healthdiary.BeanInterventionLogResult;
import com.cn.fit.model.healthdiary.BeanMyPlan;
import com.cn.fit.model.healthdiary.BeanPatientState;
import com.cn.fit.model.healthdiary.BeanResultOfEvaluation;
import com.cn.fit.model.healthdiary.BeanSummaryResult;
import com.cn.fit.ui.AppPool;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.basic.IfaceDialog;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.ToastUtil;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.main.detectioncenter.ActivityDetectionCenter;
import com.cn.fit.ui.patient.main.healthdiary.alarm.ActivitySetNotificationTime;
import com.cn.fit.ui.patient.main.healthdiary.alarm.AlarmUtils;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityEvaluationCenter;
import com.cn.fit.ui.patient.main.healthdiary.test.ActivityTestPersonInfo;
import com.cn.fit.customer.coach.ActivityCoachsList;
import com.cn.fit.ui.record.audio.ActivityRecordAudio;
import com.cn.fit.ui.record.video.ActivityVideoRecord;
import com.cn.fit.util.AbsParam;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.FButton;
import com.cn.fit.util.HorizontalPickerHealthDiary;
import com.cn.fit.util.StringUtil;
import com.cn.fit.util.UtilsSharedData;
import com.cn.fit.util.dropdownmenu.DropdownButton;
import com.cn.fit.util.dropdownmenu.DropdownButtonsController;
import com.cn.fit.util.dropdownmenu.DropdownItemObject;
import com.cn.fit.util.dropdownmenu.DropdownListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityHealthDiary extends ActivityBasic implements IfaceDialog,
        OnClickListener {
    //    Calendar mInitialTime;
    Calendar minTime, maxTime;
    //    static SliderContainer mContainer;
    TextView date;
    ListView contentList;
    MyAdapter adapter;
    ImageView todaySummarize, todayRemind, todayLocation;
    int minuteInterval = 1;
    String dataData = "2015-04-24";
    String tomorrowData = "";
    boolean firstIn = true;
    private String summary;
    private ImageView increaseMonthBtn, decreaseMonthBtn;
    //    private ImageView increaseMember, decreaseMember;
    private DropdownButton choosePlan;// 选择方案下拉按钮
    private View mask;//下拉黑色背景
    private DropdownListView dropdownPlans;
    private DropdownButtonsController dropdownButtonsController;
    private List<DropdownItemObject> listPlanItems;
    private EmojiconTextView tv_Communication, aihuTips;
    private LinearLayout noDiaryLayout;// ,noDiaryLayoutContent;
    private LinearLayout noDiaryLayoutContent;
    private LinearLayout noDiaryHasInternet;
    private LinearLayout sonOfScorllView;
    //    private FButton btnStartTest;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private Long valueId, valueId1, nodeItemId, valueId_DBP, valueId_HR,
            nodeItemId_DBP, nodeItemId_HR;
    private String value, value_DBP, value_HR;
    private ArrayList<String> detectionType = new ArrayList<String>();
    private boolean isHaveSummarize = false;
    private static boolean isRemind = true;//开关变量，控制app没有被杀死后，进入该界面后控制通知只设置一次。
    private List<BeanMyPlan> MyPlanList = new ArrayList<BeanMyPlan>();
    //输入表情前EditText中的文本
    private static String tmp1;
    /** true表示选择成功，false表示选择失败 */
    /**
     * 只有在第一次启动的时候让最近的时间的节点移动到最上方的判断符
     */
    private byte onlyStart = 0;
    private HorizontalPickerHealthDiary DatePicker;
    private CharSequence[] dates, datesToShow;
    private int chosedDayIndex = 0;
    private boolean isInitial = true;
    private boolean NetWorkIsWrongForHealthDiary = false;
    private TextView noDiaryTitle, basicInformation, livingHabitEvalution, choseExpert;
    private ImageView fillBasicInfo, fillLivingHabitEva, gotoChoseExpert;
    private LinearLayout infoLayout, testLayout, chooseLayout;
    private boolean canInfo = false;
    private boolean canTest = false;
    private boolean canChoose = false;
    private FButton gotoHealthPost, gotoSecretary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub]
        enableBanner = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_healthdiary);
        imageInit();
        initView();
    }

    /**
     * 初始化在没有康复日记时显示的布局文件。
     */
    private void initNoDiaryLayout() {
        sonOfScorllView = (LinearLayout) findViewById(R.id.sonOfScrollView);
        noDiaryHasInternet = (LinearLayout) findViewById(R.id.noDiaryHasInternet);
        noDiaryTitle = (TextView) findViewById(R.id.nodiaryTitle_healthdiary);
        basicInformation = (TextView) findViewById(R.id.fillBasicInformation_healthdiary);
        livingHabitEvalution = (TextView) findViewById(R.id.livingHabitEvaluation_healthdiary);
        choseExpert = (TextView) findViewById(R.id.chooseExpert_healthdiary);
        fillBasicInfo = (ImageView) findViewById(R.id.gotoFillInformation);
        fillLivingHabitEva = (ImageView) findViewById(R.id.gotoTestLivingHabit);
        gotoChoseExpert = (ImageView) findViewById(R.id.gotoChooseExpert);
        infoLayout = (LinearLayout) findViewById(R.id.infoLinearLayout);
        testLayout = (LinearLayout) findViewById(R.id.testLinearLayout);
        chooseLayout = (LinearLayout) findViewById(R.id.chooseLinearLayout);
        gotoHealthPost = (FButton) findViewById(R.id.gotoHealthPost);
        gotoHealthPost.setCornerRadius(3);
        gotoSecretary = (FButton) findViewById(R.id.gotoSecretary);
        gotoSecretary.setCornerRadius(3);
        basicInformation.setOnClickListener(this);
        livingHabitEvalution.setOnClickListener(this);
        choseExpert.setOnClickListener(this);
        fillBasicInfo.setOnClickListener(this);
        fillLivingHabitEva.setOnClickListener(this);
        gotoChoseExpert.setOnClickListener(this);
        gotoHealthPost.setOnClickListener(this);
        gotoSecretary.setOnClickListener(this);
    }

    private void imageInit() {
        options = new DisplayImageOptions.Builder()
                // .showStubImage(R.drawable.my_videos)
                // .showImageForEmptyUri(R.drawable.my_videos)
                // .showImageOnFail(R.drawable.my_videos)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        // new Thread(runnableHealthDiary).start();
        GetMyStausAsyncTask mTask = new GetMyStausAsyncTask();
        mTask.execute();
    }

    private void initView() {
        // typeface = Typeface.createFromAsset(getAssets(), "fangzheng.ttf");
        UtilsSharedData.initDataShare(this);
        AppPool.createActivity(ActivityHealthDiary.this);
        tv_Communication = (EmojiconTextView) findViewById(R.id.CommunicationWithSecretary);
        aihuTips = (EmojiconTextView) findViewById(R.id.aihuTips);
        aihuTips.setOnClickListener(this);
        tv_Communication.setOnClickListener(this);
        // tv_Communication.setTypeface(typeface);
        // 设置今天的日期
//        btnStartTest = (FButton) findViewById(R.id.add_diary_btn);
//        btnStartTest.setCornerRadius(3);
//        btnStartTest.setOnClickListener(this);
        noDiaryLayout = (LinearLayout) findViewById(R.id.no_diary_layout);
        noDiaryLayoutContent = (LinearLayout) findViewById(R.id.noDiaryNoInternet);
        initNoDiaryLayout();
        date = (TextView) findViewById(R.id.health_date);
        initDropDownMenu();
        increaseMonthBtn = (ImageView) findViewById(R.id.increasemonth);
        decreaseMonthBtn = (ImageView) findViewById(R.id.decreasemonth);
        increaseMonthBtn.setOnClickListener(this);
        decreaseMonthBtn.setOnClickListener(this);
        todaySummarize = (ImageView) findViewById(R.id.healthy_dairy_summarize);
        todaySummarize.setOnClickListener(this);
        todayRemind = (ImageView) findViewById(R.id.healthy_dairy_remind);
        todayRemind.setOnClickListener(this);
        contentList = (ListView) findViewById(R.id.health_content);
        adapter = new MyAdapter(getApplicationContext());
        contentList.setAdapter(adapter);

        contentList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.startAnimation(AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.icon_scale));
                // if(intervNodeList.get(position).getContentList().size()!=0){
                // Intent intent = new
                // Intent(HealthDiaryActivity.this,ActivityHealthTraningGuide.class);
                // intent.putExtra(Constant.PAGE_TYPE, position);
                // startActivity(intent);
                // }else
                // Toast.makeText(getActivity(), position, 1).show();
                if (intervNodeList == null)
                    return;
                if (intervNodeList.get(position).getIntervItemsList().size() != 0) {
//                    if ((Calendar.getInstance().getTimeInMillis() / (24 * 60 * 1000 * 60)) == (mContainer
//                            .getTime().getTimeInMillis() / (24 * 60 * 1000 * 60))) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    if (String.valueOf(df.format(Calendar.getInstance().getTime())).equals(dates[chosedDayIndex].toString())) {
                        ActivityDetectionCenter.whetherFromDiary = true;
                        detectionType.clear();
                        for (int i = 0; i < intervNodeList.get(position)
                                .getIntervItemsList().size(); i++) {
                            detectionType.add(intervNodeList.get(position)
                                    .getIntervItemsList().get(i).getCode());
                        }

                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.DETECTION_NODE, intervNodeList.get(position));
                        intent1.putStringArrayListExtra("detectionType", (ArrayList<String>) detectionType);
                        intent1.setClass(getApplicationContext(),
                                ActivityDetectionCenter.class);
                        startActivity(intent1);
                    } else if (intervNodeList.get(position).getContentList()
                            .size() > 0) {
                        Intent intent = new Intent(getApplicationContext(),
                                ActivityHealthTraningGuide.class);
                        intent.putExtra(Constant.PAGE_TYPE, position);
                        startActivity(intent);
                    }
                } else if (intervNodeList.get(position).getContentList().size() != 0) {
                    Intent intent = new Intent(getApplicationContext(),
                            ActivityHealthTraningGuide.class);
                    intent.putExtra(Constant.PAGE_TYPE, position);
                    startActivity(intent);
                } else {

                }
            }
        });
        init_datepicker();
//		listTips = getResources().getStringArray(
//				R.array.health_diary_random_tips);
//		random = new Random(listTips.length - 1);
    }

    private void init_datepicker() {
        String todayDate;
        Date DateToday = new Date(System.currentTimeMillis());
        Calendar calendarToaday = Calendar.getInstance();
        calendarToaday.setTime(DateToday);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(DateToday);
        dates = new CharSequence[]{todayDate};
        datesToShow = new CharSequence[]{todayDate.substring(8) + "\n" + getWeekOfDay(calendarToaday)};
        DatePicker = (HorizontalPickerHealthDiary) findViewById(R.id.datepicker_healthdiary);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DatePicker.setScreenHeight(display.getHeight());
        DatePicker.setScreenWidth(display.getWidth());
        DatePicker.setSelectedItem(0);
        DatePicker.setValues(datesToShow);
        DatePicker.setSideItems(3, display.getWidth(), display.getHeight());
        DatePicker.setOnTouchListener(onTouchEvent(false));
        DatePicker.setEnabled(true);
    }

    private View.OnTouchListener onTouchListener;

    private View.OnTouchListener onTouchEvent(boolean t) {
        final boolean on = t;
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return on;
            }
        };
        return onTouchListener;
    }

    private void initDropDownMenu() {
        choosePlan = (DropdownButton) findViewById(R.id.choosePlan);
        mask = findViewById(R.id.mask);
        dropdownPlans = (DropdownListView) findViewById(R.id.dropdownPlans);
        listPlanItems = new ArrayList<DropdownItemObject>();
        dropdownButtonsController = new DropdownButtonsController(this, listPlanItems, mask, choosePlan, dropdownPlans);
        dropdownButtonsController.init(0l);
        dropdownButtonsController.setOnPageSelectedListener(new DropdownButtonsController.DropDownSelectListener() {
            @Override
            public void onPageSelected(DropdownListView view) {
                //这儿处理选中的操作
//                view.getCurrent().id;
                planId = view.getCurrent().id;
                UtilsSharedData.saveKeyMustValue(Constant.LAST_CHOOSE_PLAN, planId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dataData = sdf.format(new Date(System.currentTimeMillis()));
                isInitial = true;
                onlyStart = 0;
                showProgressBar();
                new Thread(runnableHealthDiary).start();
            }

            @Override
            public void onListViewsShow() {

            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
            }
        });
    }

    private void judgeCanSummaryOrNot() {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
        if (String.valueOf(SDF.format(Calendar.getInstance().getTime())).compareTo(dates[chosedDayIndex].toString()) < 0) {
            todaySummarize.setVisibility(View.INVISIBLE);
        } else {
            todaySummarize.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.healthy_dairy_summarize:
                showSummarizeDialog();
                isHaveSummarize = true;
                break;
            case R.id.healthy_dairy_remind:
                showRemindDialog();
                break;
            case R.id.increasemonth:// 增加月份
                HandlerOfMonthChanged(true);
                break;
            case R.id.decreasemonth:// 减少月份
                HandlerOfMonthChanged(false);
                break;
            case R.id.fillBasicInformation_healthdiary:
            case R.id.infoLinearLayout:
            case R.id.gotoFillInformation:
                if (canInfo) {
                    intent = new Intent(ActivityHealthDiary.this, ActivityTestPersonInfo.class);
                    intent.putExtra("isFirst", 1);
                    startActivity(intent);
                }
                break;
            case R.id.livingHabitEvaluation_healthdiary:
            case R.id.testLinearLayout:
            case R.id.gotoTestLivingHabit:
                if (canTest) {
                    intent = new Intent(ActivityHealthDiary.this, ActivityEvaluationCenter.class);
                    startActivity(intent);
                }
                break;
            case R.id.chooseExpert_healthdiary:
            case R.id.chooseLinearLayout:
            case R.id.gotoChooseExpert:
                if (canChoose) {
                    intent = new Intent(ActivityHealthDiary.this, ActivityCoachsList.class);
                    startActivity(intent);
                }
                break;
            case R.id.aihuTips:
            case R.id.CommunicationWithSecretary:
                if (NetWorkIsWrongForHealthDiary) {
                    showProgressBar();
                    new Thread(runnableHealthDiary).start();
                }
                break;
            case R.id.gotoHealthPost:
                TabActivityMain.changePage(R.id.tab_iv_2);
                break;
            case R.id.gotoSecretary:
                TabActivityMain.changePage(R.id.tab_iv_1);
                break;
            default:
                break;
        }
    }

    /**
     * @param increaseMonth 当月份发生变化时，对日记内容进行对应的更新。如果月份变化以后，日期以及超出康复日记日期范围内的，则不变化。
     * @Author kuangtiecheng
     **/
    private void HandlerOfMonthChanged(boolean increaseMonth) {
        int changedMonth = 0;
        Calendar comparedCalendar;
        if (increaseMonth) {
            if (calendarEnd != null) {
                comparedCalendar = calendarEnd;
                changedMonth = 1;
            } else
                return;
        } else {
            if (calendarStart != null) {
                comparedCalendar = calendarStart;
                changedMonth = -1;
            } else
                return;
        }
        SimpleDateFormat SDF_MC = new SimpleDateFormat("yyyy-MM-dd");
        Date changedDateBecauseOfMonth = new Date();
        try {
            changedDateBecauseOfMonth = SDF_MC.parse(dates[chosedDayIndex].toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarMonthChanged = Calendar.getInstance();
        calendarMonthChanged.setTime(changedDateBecauseOfMonth);
        calendarMonthChanged.add(Calendar.MONTH, changedMonth);
        if (calendarMonthChanged.compareTo(comparedCalendar) == changedMonth) {
            calendarMonthChanged.add(Calendar.MONTH, -changedMonth);
            return;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        dataData = sdf1.format(calendarMonthChanged.getTime());
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].toString().equals(dataData)) {
                chosedDayIndex = i;
            }
        }
        judgeCanSummaryOrNot();
        showProgressBar();
        isInitial = false;
        new Thread(runnableHealthDiary).start();
    }

    // 弹出总结弹窗
    private EditText inputServer;

    @SuppressLint("ResourceAsColor")
    private void showSummarizeDialog() {
        inputServer = new EditText(this);
        inputServer.setHint("请填写今日小结");
        inputServer.setMinLines(8);
        if (healthDiaryBean != null) {

            if (healthDiaryBean.getNodes().size() != 0) {
                inputServer.setText(healthDiaryBean.getNodes().get(0)
                        .getSummary());
                if (isHaveSummarize) {
                    inputServer.setText(tmp1);
                }
            }
        }
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(getResources().getColor(R.color.font_gray));
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        inputServer.addTextChangedListener(new TextWatcher() {
            //匹配非表情符号的正则表达式
//			 private String reg = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>"; 
//			 private String reg ="^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]|[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){0,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";   
//			 private String reg ="[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:‘\"<,>\\.?/\\\\*)]";
//			private String reg =  "<:([[-]\\d*[,]]+):>";
            //输入表情前的光标位置
            private int cursorPos = 0;
            //			  //输入表情前EditText中的文本
            private String tmp;
            //是否重置了EditText的内容
            private boolean resetText = false;
            private int editStart;
            private int editEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editEnd = inputServer.getSelectionEnd();
                if (!resetText) {
                    if (count >= 0) {//表情符号的字符长度最小为3
                        //提取输入的长度大于3的文本
                        CharSequence input = s.subSequence(editEnd - count, editEnd);
                        //正则匹配是否是表情符号
                        String in = input.toString();
                        if (!StringUtil.isText(in)) {
                            resetText = true;
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            inputServer.setText(tmp);
                            inputServer.setSelection(tmp.length());
                            Toast.makeText(ActivityHealthDiary.this,
                                    "不支持输入", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!resetText) {
                    cursorPos = inputServer.getSelectionEnd();
                    tmp = s.toString();//这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
//				  tmp1 = s.toString();
            }
        });

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tmp1 = inputServer.getText().toString();
                summary = inputServer.getText().toString();
                new Thread(runnableSummaryResult).start();
                showProgressBar();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tmp1 = inputServer.getText().toString();
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /*******************************************************
     * 获取当天康复日记内容
     */
    private long planId;
    private Map<String, String> map;
    private String retStr;// 获取到返回的json数据
    public static BeanInterventionLogResult healthDiaryBean;
    // public static BeanIntervItem detectionNodeBean;
    // 网络请求
    Runnable runnableHealthDiary = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/interv/plan/show";

        @Override
        public void run() {
            try {
                long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
                map = new HashMap<String, String>();
                map.put("patientId", userId + "");// 表示患者用户id
                map.put("planId", planId + "");// 查找最新的干预方案
                map.put("days", "1");// 查询一天的干预方案
                map.put("startDate", dataData);// 查询开始日期
                retStr = NetTool.sendPostRequest(url, map, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getHealthDiaryInfo(retStr);
                Message msg = new Message();
                if (retStr == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                healthDiaryHandler.sendMessage(msg);

            } catch (Exception e) {
                Message msg = new Message();
                msg.what = Constant.FAIL;
                healthDiaryHandler.sendMessage(msg);
                healthDiaryHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hideProgressBar();
                        showNetErrorInfo();
                    }
                }, 1000);
            }
        }
    };

    private Handler healthDiaryHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    // 展示获取的数据bean
                    hideProgressBar();
                    firstIn = false;
                    if (healthDiaryBean != null) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                        String end = healthDiaryBean.getPlanEndDate();
                        String request = healthDiaryBean.getStartDate();
                        long compare = 0;
                        try {
                            compare = sf.parse(end).getTime()
                                    - sf.parse(request).getTime();
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (compare < 0) {
                            // 请求时间大于结束时间也不显示数据界面
                            NetWorkIsWrongForHealthDiary = true;
                            displayOrNotIndicate(true, true);
                            String tempText = "\u3000\u3000亲，您的健康方案好像有一点问题！请点击文字区域刷新或者联系供应商咨询一下吧！";
//                            String tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的健康方案好像有一点问题！<br>请点击文字区域刷新或者联系供应商咨询一下吧！";
//                            btnStartTest.setVisibility(View.GONE);
                            aihuTips.setText(Html.fromHtml("<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"));
                            aihuTips.setVisibility(View.VISIBLE);
                            tv_Communication.setText(Html.fromHtml(tempText,
                                    StringUtil.getImageGetterInstance(ActivityHealthDiary.this), null));
                        } else {
                            // 有健康档案数据，则显示健康日记界面
                            NetWorkIsWrongForHealthDiary = false;
                            displayOrNotIndicate(false, true);
                            updateUI();
                            //设置提醒
                            if (intervNodeList != null) {
                                System.out.println("=-=-=intervNodeList.get(0)=-=-=" + intervNodeList.get(0));
                                if (isRemind)
                                    setNotify(getApplicationContext(), intervNodeList);
                            }

                        }
                    } else {
                        NetWorkIsWrongForHealthDiary = true;
                        displayOrNotIndicate(true, true);
                        String tempText = "\u3000\u3000亲，您的健康方案好像有一点问题！请点击文字区域刷新或者联系供应商咨询一下吧！";
//                        String tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的健康方案好像有一点问题！<br>请点击文字区域刷新或者联系供应商咨询一下吧！";
//                        btnStartTest.setVisibility(View.GONE);
                        aihuTips.setText(Html.fromHtml("<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"));
                        aihuTips.setVisibility(View.VISIBLE);
                        tv_Communication.setText(Html.fromHtml(tempText,
                                StringUtil.getImageGetterInstance(ActivityHealthDiary.this), null));
                    }
                    break;
                case Constant.FAIL:
                    NetWorkIsWrongForHealthDiary = true;
                    displayOrNotIndicate(true, true);
                    String tempText = "\u3000\u3000亲，现在网络有点问题哦！请点击文字区域刷新或者检查网络之后重试！";
//                    btnStartTest.setVisibility(View.GONE);
                    aihuTips.setText(Html.fromHtml("<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"));
                    aihuTips.setVisibility(View.VISIBLE);
                    tv_Communication.setText(Html.fromHtml(tempText,
                            StringUtil.getImageGetterInstance(ActivityHealthDiary.this), null));
                    break;
                default:

                    break;
            }
        }
    };

    private void displayOrNotIndicate(boolean noDiary, boolean noInternet) {
        if (noDiary) {
            noDiaryLayout.setVisibility(View.VISIBLE);
            sonOfScorllView.setVisibility(View.VISIBLE);
            if (noInternet) {
                noDiaryLayoutContent.setVisibility(View.VISIBLE);
                noDiaryHasInternet.setVisibility(View.GONE);
            } else {
                noDiaryLayoutContent.setVisibility(View.GONE);
                noDiaryHasInternet.setVisibility(View.VISIBLE);
            }
            date.setVisibility(View.INVISIBLE);
            choosePlan.setVisibility(View.INVISIBLE);
            increaseMonthBtn.setVisibility(View.INVISIBLE);
            decreaseMonthBtn.setVisibility(View.INVISIBLE);
//            btnStartTest.setVisibility(View.VISIBLE);
        } else {
            sonOfScorllView.setVisibility(View.GONE);
            noDiaryLayout.setVisibility(View.GONE);
            noDiaryLayoutContent.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            choosePlan.setVisibility(View.VISIBLE);
            increaseMonthBtn.setVisibility(View.VISIBLE);
            decreaseMonthBtn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getHealthDiaryInfo(String jsonString) throws Exception {
        Gson gson = new Gson();
        healthDiaryBean = gson.fromJson(jsonString,
                BeanInterventionLogResult.class);
//		System.out.println("=-=-=jsonString=-=-="+jsonString);	
    }

    Calendar calendarStart, calendarEnd;
    Calendar addCalendar;

    private void updateDatePicker() {
        String startDate, endDate, addDate, todayDate, dayOfWeek;
        Date dateToday = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = healthDiaryBean.getPlanStartDate();
        endDate = healthDiaryBean.getPlanEndDate();
        Date dateStart = null;
        Date dateEnd = null;
        todayDate = sdf.format(dateToday);
        try {
            dateStart = sdf.parse(startDate);
            dateEnd = sdf.parse(endDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (endDate.compareTo(startDate) > 0) {
            dates = new CharSequence[(int) ((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60 * 60 * 24) + 1)];
            datesToShow = new CharSequence[(int) ((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60 * 60 * 24) + 1)];
            addCalendar = Calendar.getInstance();
            addCalendar.setTime(dateStart);
            dayOfWeek = getWeekOfDay(addCalendar);
            addDate = startDate;
            int index = 0;
            while (endDate.compareTo(addDate) >= 0) {
                dates[index] = addDate;
                datesToShow[index] = addDate.substring(8) + "\n " + dayOfWeek;
                int i = datesToShow[index].length();
                if (addDate.equals(todayDate) && isInitial) {
                    chosedDayIndex = index;
                }
                addCalendar.add(calendarEnd.DATE, 1);
                Date date_temp = addCalendar.getTime();
                dayOfWeek = getWeekOfDay(addCalendar);
                addDate = sdf.format(date_temp);
                index++;
            }
            calendarStart = Calendar.getInstance();
            calendarStart.setTime(dateStart);
            calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(dateEnd);
            DatePicker.setSelectedItem(chosedDayIndex);
            DatePicker.setValues(datesToShow);
            dataData = dates[chosedDayIndex].toString();
            date.setText(dataData.substring(0, 4) + "年" + dataData.substring(5, 7) + "月");
            DatePicker.setOnTouchListener(onTouchEvent(false));
            DatePicker.setEnabled(true);
            DatePicker.setOnItemClickedListener(new HorizontalPickerHealthDiary.OnItemClicked() {

                @Override
                public void onItemClicked(int index) {
                    // TODO Auto-generated method stub
                    if (index != chosedDayIndex) {
                        dataData = dates[index].toString();
                        chosedDayIndex = index;
                        date.setText(dataData.substring(0, 4) + "年" + dataData.substring(5, 7) + "月");
                        isInitial = false;
                        onlyStart = 0;
                        showProgressBar();
                        new Thread(runnableHealthDiary).start();
                    }
                }
            });
            DatePicker.setOnItemSelectedListener(new HorizontalPickerHealthDiary.OnItemSelected() {

                @Override
                public void onItemSelected(int index) {
                    // TODO Auto-generated method stub
                    if (index != chosedDayIndex) {
                        dataData = dates[index].toString();
                        chosedDayIndex = index;
                        date.setText(dataData.substring(0, 4) + "年" + dataData.substring(5, 7) + "月");
                        isInitial = false;
                        onlyStart = 0;
                        showProgressBar();
                        new Thread(runnableHealthDiary).start();
                    }
                }
            });
        }

    }

    /***
     * @param c
     * @return 周几
     * @author kuangtiecheng
     * @version 1.0
     */
    private String getWeekOfDay(Calendar c) {
        String dayOfWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if (dayOfWeek.equals("1")) {
            return "星期天";
        } else if (dayOfWeek.equals("2")) {
            return "星期一";
        } else if (dayOfWeek.equals("3")) {
            return "星期二";
        } else if (dayOfWeek.equals("4")) {
            return "星期三";
        } else if (dayOfWeek.equals("5")) {
            return "星期四";
        } else if (dayOfWeek.equals("6")) {
            return "星期五";
        }
        return "星期六";
    }

    /**
     * 更新界面
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    List<BeanIntervNode> intervNodeList;

    private void updateUI() {
//        updateTimePicker();
        updateDatePicker();
        judgeCanSummaryOrNot();
        if (healthDiaryBean.getNodes().size() != 0) {
            intervNodeList = healthDiaryBean.getNodes().get(0).getNodes();

//			contentList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            intervNodeList = null;
//			contentList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if (onlyStart == 0) {
            if (intervNodeList != null && intervNodeList.size() > 0) {
                if (getLeastTime(intervNodeList) > 0) {
                    contentList.setSelectionFromTop(getLeastTime(intervNodeList) - 1, 0);
                } else {
                    contentList.setSelectionFromTop(getLeastTime(intervNodeList), 0);
                }
//				Log.i("test", ""+intervNodeList.get(getLeastTime(intervNodeList)).getTime());
            }
        }
        onlyStart = 1;
    }

    private int getLeastTime(List<BeanIntervNode> list) {
        int position = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String[] nowtime = formatter.format(curDate).split("\\:");
        int now = Integer.parseInt(nowtime[0]) * 60 + Integer.parseInt(nowtime[1]);
        int min = now;
        for (int i = 0; i < list.size(); i++) {
            String[] str = list.get(i).getTime().split("\\:");
            int time = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
            int tempMin = Math.abs(now - time);
            if (tempMin <= min) {
                min = tempMin;
                position = i;
            }
        }
        return position;
    }

    /*******************************************************
     * 提交当日总结
     */
    private Map<String, String> map1;
    private String retStr1;// 获取到返回的json数据
    private BeanSummaryResult beanSummaryResult;
    // 网络请求
    Runnable runnableSummaryResult = new Runnable() {
        private String url = AbsParam.getBaseUrl()
                + "/interv/plan/dailysummary";

        // private String
        // url="http://192.168.12.112:8080/serviceplatform/interv/plan/dailysummary";
        @Override
        public void run() {
            try {
                map1 = new HashMap<String, String>();
                map1.put("patientPlanId",
                        String.valueOf(healthDiaryBean.getPatientPlanId()));// 干预方案Id
                map1.put("summary", summary);// 查询一天的干预方案
                retStr1 = NetTool.sendPostRequest(url, map1, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getSummaryResult(retStr1);

                Message msg = new Message();
                if (retStr1 == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                summaryHandler.sendMessage(msg);

            } catch (Exception e) {
                summaryHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hideProgressBar();
                        showNetErrorInfo();
                    }
                }, 1000);
            }
        }
    };

    private Handler summaryHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    // 展示获取的数据bean
                    hideProgressBar();
                    ToastUtil.showMessage("提交成功");
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
    private void getSummaryResult(String jsonString) throws Exception {
        Gson gson = new Gson();
        beanSummaryResult = gson.fromJson(jsonString, BeanSummaryResult.class);
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView time;
        public TextView title;
        public TextView tips;
        private FrameLayout pic_layout;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context healthDiaryFragment) {
            this.mInflater = LayoutInflater.from(healthDiaryFragment);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = 0;
            if (intervNodeList != null) {
                count = intervNodeList.size();
            }
            return count;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            // if (convertView == null) {
            holder = new ViewHolder();
            if (position % 2 == 0) {
                convertView = mInflater.inflate(
                        R.layout.listitem_historydiary_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.listitem_historydiary_right, null);

            }
            holder.img = (ImageView) convertView.findViewById(R.id.health_pic);
            holder.time = (TextView) convertView.findViewById(R.id.health_time);
            holder.title = (TextView) convertView
                    .findViewById(R.id.health_desc);
            holder.tips = (TextView) convertView.findViewById(R.id.health_tips);
            holder.pic_layout = (FrameLayout) convertView
                    .findViewById(R.id.pic_layout);
            holder.img.setScaleType(ScaleType.FIT_XY);
            if (intervNodeList != null) {
                if (intervNodeList.get(position).getState() == 7) {
                    holder.title.setText("今日训练(已完成)");
                }
                if (intervNodeList.get(position).getContentList().size() != 0) {
                    String imgURL = AbsParam.getBaseUrl()
                            + intervNodeList.get(position).getContentList()
                            .get(0).getThumbnail();
                    imageLoader.displayImage(imgURL, holder.img, options);
                } else if (intervNodeList.get(position).getIntervItemsList()
                        .size() != 0) {
                    List<BeanIntervItem> intervItemsList = intervNodeList.get(
                            position).getIntervItemsList();
                    // 隐藏视频界面
                    holder.pic_layout.setVisibility(View.GONE);
                    String display = "";
                    int val = 0;
                    for (int i = 0; i < intervItemsList.size(); i++) {
                        if (intervItemsList.get(i).getValue() != 0) {
                            display += intervItemsList.get(i).getName()
                                    + ":"
                                    + intervItemsList.get(i).getValue()
                                    + intervItemsList.get(i).getUnit()
                                    + ((i == intervItemsList.size() - 1) ? ""
                                    : "\n");
                            val++;
                        }

                    }
                    if (val > 0) {
                        holder.title.setText(display);
                    } else {
                        holder.title.setText(intervNodeList.get(position)
                                .getContentStr());
                    }

                } else {
                    holder.pic_layout.setVisibility(View.GONE);
                    holder.title.setText(intervNodeList.get(position)
                            .getContentStr());
                }
                holder.time.setText(intervNodeList.get(position).getTime());
                holder.tips
                        .setText(Html
                                .fromHtml("<h5><font color='#33cccc'>温馨提示</font></h5><h9><font color='#888888'>"
                                        + ((intervNodeList.get(position).getRemindContent() == null) ? "" : intervNodeList.get(position).getRemindContent())
                                        + "</font></h9>"));

                convertView.setTag(holder);
            }
            return convertView;
        }

    }

	/*
     * 提示用户选择提醒方式
	 */
    /**
     * 0:音频，1：视频，2：文字
     */
    private int chooseFalg = 0;

    private void showRemindDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
                null);
        Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        buttonEvent(dialog, view);

    }

    private void buttonEvent(final Dialog dialog, View view) {
        view.findViewById(R.id.videoremind).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(ActivityHealthDiary.this,
                                ActivityVideoRecord.class);
                        intent.putExtra(Constant.REMIND_TYPE, 1);
                        startActivity(intent);
                    }
                });

        view.findViewById(R.id.audioremind).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent1 = new Intent(ActivityHealthDiary.this,
                                ActivityRecordAudio.class);
                        intent1.putExtra(Constant.REMIND_TYPE, 0);
                        startActivity(intent1);
                    }
                });

        view.findViewById(R.id.textremind).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        characterRecord();
                    }
                });

        view.findViewById(R.id.cancelremind).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });

    }

    @SuppressLint("ResourceAsColor")
    private void characterRecord() {

        final EditText inputServer = new EditText(this);
        inputServer.setMinLines(8);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(getResources().getColor(R.color.font_gray));
        inputServer.setHint("爱护，文字提醒");
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("请填写您要设置的提醒文字");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String textremind = inputServer.getText().toString();
                if (textremind == null || textremind.length() == 0) {
                    textremind = "爱护，文字提醒";
                }
                UtilsSharedData.saveKeyMustValue("textremind", textremind);
                Intent intent2 = new Intent(ActivityHealthDiary.this,
                        ActivitySetNotificationTime.class);
//				intent2.putExtra("choose", chooseFalg);
                intent2.putExtra(Constant.REMIND_TYPE, 2);
                startActivity(intent2);
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

    /**
     * 更新日记，本地存储
     *
     * @author 丑旦
     */
    public void updateDiary(int pageType) {
        if (pageType == 1) {
            System.out.println("=-=-=日记状态正常=-=-=pageType: " + pageType);
        } else {//删除该用户的所有提醒
            BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
            beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(Constant.USER_ID, 0));
            beanHealthDiaryLocal.setId(null);//查询该用户的所有有效的闹铃记录
            try {
                ArrayList<BeanHealthDiaryLocal> list = DataBaseHelper.onQuery(getApplicationContext(), beanHealthDiaryLocal);
                AlarmUtils.cancelAlarms(getApplicationContext(), list);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DataBaseHelper.onDelete(getApplicationContext(), beanHealthDiaryLocal);
        }
    }

    /**
     * 获取当前的用户状态， 0 没有康复日记 1 有康复日记 2 康复日记已过期
     *
     * @author kuangtiecheng
     */
    private class GetMyStausAsyncTask extends
            AsyncTask<Integer, Integer, BeanPatientState> {
        String result = "";
        BeanPatientState BPS;

        private GetMyStausAsyncTask() {
            // showProgressBar();
        }

        @Override
        protected BeanPatientState doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientId", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            try {
//                result = NetTool.sendPostRequest(AbsParam.getBaseUrl() + "/base/app/getcpstatus", param, "utf-8");
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl() + "/interv/get_patient_state", param, "utf-8");
                Log.i("result", result);
                BPS = getBeanPatientState(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return BPS;
        }

        private BeanPatientState getBeanPatientState(String str) {
            BeanPatientState bps = null;
            Gson gson = new Gson();
            if (!str.equals("sendText error!") && str.length() > 0) {
                bps = gson.fromJson(str, new TypeToken<BeanPatientState>() {
                }.getType());
            }
            return bps;
        }

        @Override
        protected void onPostExecute(BeanPatientState result) {
            hideProgressBar();
            if (result != null) {
                updateDiary(result.getPlanState());
                UtilsSharedData.saveKeyMustValue("BaseInfoState", result.getBaseInfoState());
                UtilsSharedData.saveKeyMustValue("HabbitTestState", result.getHabbitTestState());
                UtilsSharedData.saveKeyMustValue("ExpertState", result.getExpertState());
                UtilsSharedData.saveKeyMustValue("BodyTestState", result.getBodyTestState());
                if (result.getPlanState() == 1) {
                    //如果有日记，则请求康复日记列表
                    displayOrNotIndicate(false, false);
                    getHealthdiaryList();
                } else {
                    handlerOfNoDiary(result);
                }
            } else {

                NetWorkIsWrongForHealthDiary = true;
                displayOrNotIndicate(true, true);
                String tempText = "\u3000\u3000亲，您的健康方案好像有一点问题！请点击文字区域刷新或者联系供应商咨询一下吧！";
                //                            String tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的健康方案好像有一点问题！<br>请点击文字区域刷新或者联系供应商咨询一下吧！";
                //                            btnStartTest.setVisibility(View.GONE);
                aihuTips.setText(Html.fromHtml("<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"));
                aihuTips.setVisibility(View.VISIBLE);
                tv_Communication.setText(Html.fromHtml(tempText,
                        StringUtil.getImageGetterInstance(ActivityHealthDiary.this), null));
            }
            if (UtilsSharedData.getLong("BaseInfoState", -1) * UtilsSharedData.getLong("HabbitTestState", -1) * UtilsSharedData.getLong("BodyTestState", -1) == 0) {
                TabActivityMain.mRedMark_tab3.setVisibility(View.VISIBLE);
            } else {
                TabActivityMain.mRedMark_tab3.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 从后台请求到的状态位不为1时的界面处理
     */
    private void handlerOfNoDiary(BeanPatientState bean) {
        displayOrNotIndicate(true, false);
        //如果没有日记，在第一次使用app以及没有基本信息的条件下，直接跳转到填写基本信息页面进行填写。
        if (!UtilsSharedData.getBoolean(Constant.HASUSED) && bean.getBaseInfoState() == 0) {
            Intent intent1 = new Intent(getApplicationContext(), ActivityTestPersonInfo.class);
            intent1.putExtra("isFirst", 0);
            startActivity(intent1);
            UtilsSharedData.saveKeyMustValue(Constant.HASUSED, true);
        }
        //如果没有日记，且不符合上一个条件时，显示没有完善的项目
        String textContent_FillInfo;
        String textContent_LivingHabit;
        String textContent_Chose;
        gotoSecretary.setVisibility(View.GONE);
        gotoHealthPost.setVisibility(View.GONE);
        if (bean.getPlanState() == 0) {
            if (bean.getExpertState() == 1) {
                gotoSecretary.setVisibility(View.VISIBLE);
                gotoHealthPost.setVisibility(View.VISIBLE);
                noDiaryTitle.setText(Html.fromHtml("<font color='#000000'>您的健康秘书正在为您制定康复日记，您可以进行咨询，也可以先去驿站逛逛哦。</font>"));
            } else {
                noDiaryTitle.setText(Html.fromHtml("<font color='#000000'>您还没有制定日记哦，选择一个健康秘书为您制定吧</font>"));
            }

        } else {
            gotoSecretary.setVisibility(View.VISIBLE);
            gotoHealthPost.setVisibility(View.VISIBLE);
            noDiaryTitle.setText(Html.fromHtml("<font color='#000000'>您的日记已经过期了哦，快联系健康秘书为您制定吧，也可以先去驿站逛逛哦</font>"));
        }
        if (bean.getBaseInfoState() == 1) {
            canInfo = false;
            textContent_FillInfo = "<font color='#000000'>完善基本信息</font>" + "<font color='#FCBF04'>\u3000\u3000+10金币</font>";
            fillBasicInfo.setBackgroundResource(R.drawable.icon_yiwancheng);
        } else {
            canInfo = true;
            textContent_FillInfo = "<font color='#000000'>完善基本信息</font>" + "<font color='#FCBF04'>\u3000\u3000+10金币</font>";
            fillBasicInfo.setBackgroundResource(R.drawable.icon_quzhixing);
        }
        if (bean.getHabbitTestState() == 1) {
            canTest = false;
            fillLivingHabitEva.setBackgroundResource(R.drawable.icon_yiwancheng);
            GetMyTestResultAsyncTask getMyResult = new GetMyTestResultAsyncTask() {
                @Override
                protected void onPostExecute(BeanResultOfEvaluation result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        if (result.getDetail().equals("高危")) {
                            livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>"
                                    + "<font color='#FF4500'>\u3000\u3000高危</font>"));
                        } else if (result.getDetail().equals("极高危")) {
                            livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>"
                                    + "<font color='#FF0000'>\u3000\u3000极高危</font>"));
                        } else if (result.getDetail().equals("中危")) {
                            livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>"
                                    + "<font color='#FFA500'>\u3000\u3000中危</font>"));
                        } else if (result.getDetail().equals("低危")) {
                            livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>"
                                    + "<font color='#FFFF00'>\u3000\u3000低危</font>"));
                        } else if (result.getDetail().equals("健康")) {
                            livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>"
                                    + "<font color='#45c01a'>\u3000\u3000健康</font>"));
                        }

                    } else {
                        livingHabitEvalution.setText(Html.fromHtml("<font color='#000000'>生活习惯评估</font>" + "<font color='#e6e5e5'>\u3000\u3000+10金币</font>"));
                    }
                }
            };
            getMyResult.execute();
        } else {
            canTest = true;
            textContent_LivingHabit = "<font color='#000000'>生活习惯评估</font>" + "<font color='#FCBF04'>\u3000\u3000+10金币</font>";
            fillLivingHabitEva.setBackgroundResource(R.drawable.icon_quzhixing);
            livingHabitEvalution.setText(Html.fromHtml(textContent_LivingHabit));
        }
        if (bean.getExpertState() == 1) {
            canChoose = false;
            textContent_Chose = "<font color='#000000'>选择健康秘书</font>" + "<font color='#FCBF04'>\u3000\u3000+10金币</font>";
            gotoChoseExpert.setBackgroundResource(R.drawable.icon_yiwancheng);

        } else {
            canChoose = true;
            textContent_Chose = "<font color='#000000'>选择健康秘书</font>" + "<font color='#FCBF04'>\u3000\u3000+10金币</font>";
            gotoChoseExpert.setBackgroundResource(R.drawable.icon_quzhixing);
        }
        basicInformation.setText(Html.fromHtml(textContent_FillInfo));
        choseExpert.setText(Html.fromHtml(textContent_Chose));

    }

    /**
     * 获取康复日记列表
     */
    private void getHealthdiaryList() {
        AsyncGetMyPlans task = new AsyncGetMyPlans(ActivityHealthDiary.this) {
            @Override
            protected void onPostExecute(List<BeanMyPlan> beanMyPlans) {
                super.onPostExecute(beanMyPlans);
                if (beanMyPlans != null) {
                    if (beanMyPlans.size() > 0) {
                        MyPlanList = beanMyPlans;
                        listPlanItems.clear();
                        for (int i = 0; i < beanMyPlans.size(); i++) {
                            listPlanItems.add(new DropdownItemObject(beanMyPlans.get(i).getExpertName() + "制定的方案", beanMyPlans.get(i).getPlanId(), beanMyPlans.get(i).getPlanId() + ""));
                            //                                        listPlanItems.add(new DropdownItemObject(beanMyPlans.get(i).getExpertName(),i,beanMyPlans.get(i).getPlanId()+""));
                        }
                        if ((UtilsSharedData.getLong(Constant.LAST_CHOOSE_PLAN, -1) == -1)
                                || !judgeContainsPlanId(UtilsSharedData.getLong(Constant.LAST_CHOOSE_PLAN, -1))) {
                            if (listPlanItems.size() > 0) {
                                planId = listPlanItems.get(0).id;
                            }
                        } else {
                            planId = UtilsSharedData.getLong(Constant.LAST_CHOOSE_PLAN, -1);
                        }
                        dropdownButtonsController.init(planId);
                        Date dateToday = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        dataData = sdf.format(dateToday);
                        date.setText(dataData.substring(0, 4) + "年" + dataData.substring(5, 7) + "月");
                        new Thread(runnableHealthDiary).start();
                    } else {
                        hideProgressBar();
                    }
                } else {
                    hideProgressBar();
                }
            }
        };
        task.execute();
    }

    /**
     * 判断当前存储的id是否存在于方案列表数组中
     *
     * @param Id
     * @return
     */
    private boolean judgeContainsPlanId(long Id) {
        for (DropdownItemObject bean : listPlanItems) {
            if (bean.id == Id) {
                //找到id了
                return true;
            }
        }
        return false;
    }
    /*
     * 根据状态判断当前需要显示什么内容
     */
//    private void disPlayResult(int status, BeanResultOfEvaluation result) {
//        String tempText = "";
//        switch (status) {
//            case 0:
//                // 完成注册
//                tempText = "\u3000\u3000《我的秘书》帮您选择多个专科的资深护士,"
//                        + "再由他们为您量身定制《健康日记》,"
//                        + "在《健康驿站》您和著名专家欢聚一堂。<br>\u3000\u3000亲,点击“开始”,让我们为您的健康之旅保驾护航。";
////                tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"
////                        + "<big><big>·</big></big> 《我的秘书》帮您选择多个专科的资深护士<br>"
////                        + "<big><big>·</big></big> 再由他们为您量身定制《健康日记》<br>"
////                        + "<big><big>·</big></big> 在《健康驿站》您和著名专家欢聚一堂<br>亲，点击“开始”，让我们为您的健康之旅保驾护航";
////                btnStartTest.setText("开始");
//                aihuTips.setVisibility(View.VISIBLE);
//                if (!UtilsSharedData.getBoolean(Constant.HASUSED)) {
//                    Intent intent1 = new Intent(getApplicationContext(),
//                            ActivityTestPersonInfo.class);
//                    startActivity(intent1);
//                    UtilsSharedData.saveKeyMustValue(Constant.HASUSED, true);
//                }
//                break;
//            case 1:
////                tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，我们已经收到您填写的基本信息<br>现在您可以进行一次身体状况的测评<br>让我们可以为您提供更为贴心的服务<br>还可以赢取 <font color='#B8860B'>30金币</font>哦<br>快点击“开始测评”进行测评吧！";
//                tempText = "\u3000\u3000亲，我们已经收到您填写的基本信息。现在您可以进行一次身体状况的测评，让我们可以为您提供更为贴心的服务，还可以赢取 <font color='#B8860B'>100金币</font>哦！快点击“开始测评”进行测评吧！";
////                btnStartTest.setText("开始测评");
//                aihuTips.setVisibility(View.VISIBLE);
//                // 完成个人信息填写
//                break;
//            case 2:
//                // 完成初始测评
//                // 需要继续请求测评结果
//                if (result != null) {
//                    String textResult = "";
//                    if (result.getDetail().equals("高危")) {
//                        textResult = "<font color='#FF4500'><big><big>高危</big></big></font>";
//                    } else if (result.getDetail().equals("极高危")) {
//                        textResult = "<font color='#FF0000'><big><big>极高危</big></big></font>";
//                    } else if (result.getDetail().equals("中危")) {
//                        textResult = "<font color='#FFA500'><big><big>中危</big></big></font>";
//                    } else if (result.getDetail().equals("低危")) {
//                        textResult = "<font color='#FFFF00'><big><big>低危</big></big></font>";
//                    } else if (result.getDetail().equals("健康")) {
//                        textResult = "<font color='#45c01a'><big><big>健康</big></big></font>";
//                    }
//                    tempText = "<font color='#e6e5e5'>————————   测评结果       ————————</font><br>"
//                            + "\u3000\u3000您的测评结果为：" + textResult
//                            + "<br><font color='#e6e5e5'>————————   温馨提示       ————————</font><br>"
//                            + "\u3000\u3000" + result.getWarming();
////                    btnStartTest.setText("选择健康秘书");
//                    aihuTips.setVisibility(View.GONE);
//                }
//                break;
//            case 3:
//                // //选完专家
//                // tempText =
//                // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，你已经有私人健康秘书为您服务啦！<br>为了您的私人健康秘书能够更全面的了解您的身体状况<br>请完善一下您的健康状况信息吧！";
//                // btnStartTest.setText("完善健康信息");
//                // break;
//            case 4:
//                tempText = "\u3000\u3000亲，您的私人健康秘书正在为您制定健康方案！请您耐心等待。您也可以到“驿站”逛逛哦！";
////                tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的私人健康秘书正在为您制定健康方案！<br>请您耐心等待<br>你也可以到“驿站”逛逛哦！";
////                btnStartTest.setText("去驿站逛逛");
//                aihuTips.setVisibility(View.VISIBLE);
//                // 专家测评完
//                break;
//            case 5:
//                // 方案开始执行
//                tempText = "\u3000\u3000亲，您的私人健康秘书已经为您制定好了健康方案！点击“开启健康之旅”来查看吧！";
////                tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的私人健康秘书已经为您制定好了健康方案！<br>点击“开启健康之旅”来查看吧！";
////                btnStartTest.setText("开启健康之旅");
//                aihuTips.setVisibility(View.VISIBLE);
//                break;
//
//            default:
//                break;
//        }
//        aihuTips.setText(Html.fromHtml("<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>"));
//        tv_Communication.setText(Html.fromHtml(tempText,
//                StringUtil.getImageGetterInstance(this), null));
//    }

    /**
     * 请求当前测评结果
     *
     * @author kuangtiecheng
     */
    private class GetMyTestResultAsyncTask extends
            AsyncTask<Integer, Integer, BeanResultOfEvaluation> {
        String result = "";
        private BeanResultOfEvaluation beanResultOfEvaluation;

        private GetMyTestResultAsyncTask() {
            // showProgressBar();
        }

        @Override
        protected BeanResultOfEvaluation doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            try {
                result = NetTool
                        .sendPostRequest(AbsParam.getBaseUrl()
                                        + "/webapp/questionnaire/getquesresult", param,
                                "utf-8");
                Log.i("result", result);
                beanResultOfEvaluation = jsonToBean(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
                Message msg = new Message();
                msg.what = Constant.FAIL;
                healthDiaryHandler.sendMessage(msg);
            }
            return beanResultOfEvaluation;
        }

        @Override
        protected void onPostExecute(BeanResultOfEvaluation result) {
            super.onPostExecute(result);
            hideProgressBar();
            if (result == null) {
                return;
            }
            if (result.getStatus().equals("success")) {
                displayOrNotIndicate(true, false);
//                disPlayResult(2, result);
                // Toast.makeText(getBaseContext(), "submit success!",
                // 1).show();
            } else {
                ToastUtil.showMessage("获取身体评测结果失败！");
            }
        }

        private BeanResultOfEvaluation jsonToBean(String str) {
            BeanResultOfEvaluation beanROE = new BeanResultOfEvaluation();
            Gson gson = new Gson();
            if (str != null && !str.equals("-1")) {
                beanROE = gson.fromJson(str,
                        new TypeToken<BeanResultOfEvaluation>() {
                        }.getType());
            }
            return beanROE;
        }

    }

    /**
     * 设置通知
     *
     * @author kuangtiecheng
     */
    public void setNotify(Context context, List<BeanIntervNode> intervNodeList) {
        boolean value = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_DIARY_REMIND_SHAKE.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_DIARY_REMIND_SHAKE.getDefaultValue());
//		System.out.println("=-=-=提醒的开关是否打开value=-=-="+ value);
        if (value) {
            ArrayList<String> listTime = new ArrayList<String>();
            ArrayList<String> listContent = new ArrayList<String>();
            if (intervNodeList != null && intervNodeList.size() > 0) {
                for (BeanIntervNode beanIntervNode : intervNodeList) {
                    if (!nowCompareTime(beanIntervNode.getTime())) {
                        listTime.add(beanIntervNode.getTime());
//						System.out.println("=-=-=beanIntervNode.getTime()=-=-="+ beanIntervNode.getTime());
                        listContent.add(beanIntervNode.getContentStr());
//						System.out.println("=-=-=beanIntervNode.getContentStr()=-=-="+ beanIntervNode.getContentStr());
                    }
                }
            }
            if (listTime != null && listTime.size() > 0) {
                for (int i = 0; i < listTime.size(); i++) {
                    Intent intent = new Intent(context, DiaryReceiver.class);
                    intent.putExtra(Constant.NOTIFY_CONTENT, listContent.get(i));
                    intent.putExtra(Constant.NOTIFY_ID, i);
                    PendingIntent pi = PendingIntent.getBroadcast(context, i, intent, 0);
                    int[] hour = AlarmUtils.getHourMinute(listTime.get(i));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
//					System.out.println("=-=-=设置的系统通知=-=-="+hour[0]+":"+hour[1]);
                    calendar.set(Calendar.HOUR_OF_DAY, hour[0]);
                    calendar.set(Calendar.MINUTE, hour[1]);
                    AlarmManager alarmManager = AlarmUtils.getAlarmManager(context);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                }
                isRemind = false;
            } else {
                System.out.println("=-=-=时间超时，不设提醒=-=-=");
            }
        } else
            System.out.println("=-=-=开关关闭，不设提醒=-=-=");
    }

    /**
     * true表示过了当天过了当前时间的节点，不能再设置通知
     *
     * @param time
     * @author kuangtiecheng
     */
    public boolean nowCompareTime(String time) {
        boolean flag = false;
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        try {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
//			System.out.println("=-=-=now: "+now);
            Date date = sf.parse(time);
            calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
            calendar.set(Calendar.MINUTE, date.getMinutes());
            long past = calendar.getTimeInMillis();
//			System.out.println("=-=-=past: "+past);
            if (now > past)
                flag = true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }
}
