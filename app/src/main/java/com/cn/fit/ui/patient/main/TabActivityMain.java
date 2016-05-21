package com.cn.fit.ui.patient.main;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.customer.discovery.ActivityCoachDiscoveryList;
import com.cn.fit.model.healthdiary.BeanHealthDiaryLocal;
import com.cn.fit.service.ServiceGps;
import com.cn.fit.ui.AppPool;
import com.cn.fit.ui.chat.ui.ActivityHealthAssist;
import com.cn.fit.ui.patient.main.detectioncenter.ActivityDetectionCenter;
import com.cn.fit.ui.patient.main.healthdiary.DataBaseHelper;
import com.cn.fit.ui.patient.main.healthpost.ActivityPost;
import com.cn.fit.ui.patient.others.myaccount.ActivityMyAccountCenter;
import com.cn.fit.ui.scancode.ActivityCapture;
import com.cn.fit.ui.welcome.AscycUpData;
import com.cn.fit.util.Constant;
import com.cn.fit.util.CustomDialog;
import com.cn.fit.util.SystemBarTintManager;
import com.cn.fit.util.UtilsSharedData;

public class TabActivityMain extends TabActivity implements
        View.OnClickListener {
    private SystemBarTintManager tintManager;
    private final int SCANER_CODE = 1;
    //	private ResideMenu resideMenu;
//	private int[] image = { R.drawable.leftbar_myfamily,R.drawable.leftbar_myorders};
//	private String[] array = new String[] // 创建一个字符串数组，存textview字符
//	{"我的家庭","我的订单"};//, "我的咨询", "健康档案","健康商城" , "我的好友", "我的设备" , "我的收藏" , "我的测评" };
//	private ListView menuList;
//	private DisplayImageOptions options;
//	private ImageLoader imageLoader;
//	private ResideMenuInfo info;
    private static LinearLayout layoutTop;
    //	private boolean is_closed = true;
    private long mExitTime;

    private Button leftMenu;
    private Button rightMenu;
    private static TabHost mTabHost;
    //	public static int selectDetetcMember = 0;
//	public static int selectNurse = 0;
    private String userID, userAccount;
    //	,userName,userGender;
    private Intent intentLocation;
    //	public static String mCurrentTab;
    private Intent iDiary;
    private Intent iAssist;
    private Intent iPost;
    private Intent iMe;
    private static ImageButton mBt0;
    private static ImageButton mBt2;
    //	private static ImageButton mBt3;
    private static View mBt1;
    private static View mBt3;
    public static View mRedMark; // 秘书消息的红点
    public static View mRedMark_tab3;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPool.createActivity(this);
//		type = getIntent().getIntExtra("type", 0); 
        //第一次使用需要弹出这个界面
        UtilsSharedData.initDataShare(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);


        AscycUpData ascycUpdate = new AscycUpData(this);
        ascycUpdate.execute();
        setContentView(R.layout.activity_main);

//       options = AppMain.initImageOptions(R.drawable.default_user_icon, true);
//		imageLoader = ImageLoader.getInstance();

        getData();
        setUpMenu();
        setupTabs(savedInstanceState);
        setListener();
//	    startLocationService();
//		initDiaryRemind(getApplicationContext());
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getData() {
        UtilsSharedData.initDataShare(this);// ////////
        userID = UtilsSharedData.getLong(Constant.USER_ID, 1) + "";
        userAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
//		userName = UtilsSharedData.getValueByKey(Constant.USER_NAME).equals("")?
//				UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT):UtilsSharedData.getValueByKey(Constant.USER_NAME);
//		userGender = UtilsSharedData.getValueByKey(Constant.USER_GENDER);
//		if(userGender.equals("00070002")){
//			userGender = "男";
//		}else if(userGender.equals("00070003")){
//			userGender = "女";
//		}else{
//			userGender = "未知";
//		}

    }

    private void startLocationService() {
        initGPS();
        intentLocation = new Intent(this, ServiceGps.class);
        intentLocation.putExtra(Constant.USER_ID, userID);
        intentLocation.putExtra(Constant.USER_ACCOUNT, userAccount);
        startService(intentLocation);
    }


    private void setupTabs(Bundle savedInstanceState) {
        mTabHost = getTabHost();
        iDiary = new Intent(this, ActivityCoachDiscoveryList.class);
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_DIARY)
                .setIndicator(getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iDiary));
        iAssist = new Intent(this, ActivityHealthAssist.class);
        iAssist.putExtra("launcher_from", 1);
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_CONSULT)
                .setIndicator(getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iAssist));
        iPost = new Intent(this, ActivityPost.class);//ActivityHealthPostList
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_ACTIVITY)
                .setIndicator(getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iPost));
        iMe = new Intent(this, ActivityMyAccountCenter.class);
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_SETTING)
                .setIndicator(getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iMe));


        mBt0 = (ImageButton) findViewById(R.id.tab_iv_0);
        mBt1 = findViewById(R.id.tab_iv_1);
        mBt2 = (ImageButton) findViewById(R.id.tab_iv_2);
        mBt3 = findViewById(R.id.tab_iv_3);
        mRedMark = findViewById(R.id.red_mark);
        mRedMark_tab3 = findViewById(R.id.red_mark_tab3);
        mBt0.setSelected(true);
        mBt1.setSelected(false);
        mBt2.setSelected(false);
        mBt3.setSelected(false);
        mBt0.setOnClickListener(this);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);
    }

    private static void switchTabChoosed(int tab) {
//        mCurrntTabInt = tab;
        switch (tab) {
            case 0:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(true);
                mBt1.setSelected(false);
                mBt2.setSelected(false);
                mBt3.setSelected(false);
                break;
            case 1:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(false);
                mBt1.setSelected(true);
                mBt2.setSelected(false);
                mBt3.setSelected(false);
                break;
            case 2:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(false);
                mBt1.setSelected(false);
                mBt2.setSelected(true);
                mBt3.setSelected(false);
                break;
            case 3:
                layoutTop.setVisibility(View.GONE);
                mBt0.setSelected(false);
                mBt1.setSelected(false);
                mBt2.setSelected(false);
                mBt3.setSelected(true);
                break;

            default:
                break;
        }
    }


      /* Might be useful if we want to switch tab programmatically, from inside any of the fragment.*/
//      public void setCurrentTab(int val){
//            mTabHost.setCurrentTab(val);
//      }


    @SuppressWarnings("deprecation")
    private void setUpMenu() {
        layoutTop = (LinearLayout) findViewById(R.id.layout_top);
        leftMenu = (Button) findViewById(R.id.title_bar_left_menu);
        rightMenu = (Button) findViewById(R.id.title_bar_right_menu);
//		resideMenu = new ResideMenu(this);
//		resideMenu.setBackground(R.drawable.menu_background);
//		resideMenu.attachToActivity(this);
//		resideMenu.setMenuListener(menuListener);
        // valid scale factor is between 0.0f and 1.0f. leftmenu'width is
        // 150dip.
//		resideMenu.setScaleValue(0.8f);
        // 禁止使用右侧菜单
//		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
//		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        // create menu items;
//		menuList = new ListView(this);
//		menuList.setDivider(null);
//		menuList.setPadding(0, 20, 0, 0);
//		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		TitleListAdapter adapter = new TitleListAdapter(this);
//		menuList.setAdapter(adapter);
//		menuList.setSelector(R.color.transparent);
//		resideMenu.addMenuList(menuList, ResideMenu.DIRECTION_LEFT);
//		info = new ResideMenuInfo(this, R.drawable.icon_profile, userName,
//				userGender);

    }

    private void setListener() {
//		resideMenu.addMenuInfo(info);
//		resideMenu.layoutSetting.setOnClickListener(this);
//		info.setOnClickListener(this);
        leftMenu.setOnClickListener(this);
        rightMenu.setOnClickListener(this);
//		menuList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				arg1.startAnimation(AnimationUtils.loadAnimation(TabActivityMain.this,
//						R.anim.icon_scale));
//				Intent intent;
//				switch (arg2) {
//				case 0:
//					intent = new Intent();
//					intent.setClass(getApplicationContext(), ActivityMyFamily.class);
//					startActivity(intent);
//					break;
//				case 1:
//					intent = new Intent();//我的订单
//					intent.setClass(getApplicationContext(), ActivityMyOrdersList.class);
//					startActivity(intent);
//					break;
//				default:
//					break;
//				}
//			}
//			
//		});
    }
//	
//	private class TitleListAdapter extends BaseAdapter {
//		private Context context;
//
//		public TitleListAdapter(Context context) {
//			this.context = context;
//		}
//
//		@Override
//		public int getCount() {
//			return array.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return array[position];
//		}

//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				holder = new ViewHolder();
//				convertView = LayoutInflater.from(context).inflate(
//						R.layout.list_item_menu, null);
//				holder.title = (TextView) convertView
//						.findViewById(R.id.list_item_title);
//				holder.image = (ImageView) convertView
//						.findViewById(R.id.list_item_image);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.image.setImageResource(image[position]);
//			holder.title.setTextColor(getResources().getColor(R.color.white));
//			holder.title.setTextSize(16.0f);
//			holder.title.setText(array[position]);
//			return convertView;
//		}
//	}
//
//	private class ViewHolder {
//		ImageView image;
//		TextView title;
//	}


    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.icon_scale));
//		if (view == info) {// 我的账户
//			Intent intent = new Intent();
//			intent.setClass(getApplicationContext(), ActivityMyAccountCenter.class);
//			startActivity(intent);
//		} else {
        changePage(view.getId());
        switch (view.getId()) {

//            case R.id.tab_iv_0:
//            	this.mTabHost.setCurrentTabByTag(Constant.TAB_DIARY);
//                switchTabChoosed(0);
//                break;
//            case R.id.tab_iv_1:
//            	this.mTabHost.setCurrentTabByTag(Constant.TAB_CONSULT);
//                switchTabChoosed(1);
//                break;
//            case R.id.tab_iv_2:
//            	this.mTabHost.setCurrentTabByTag(Constant.TAB_ACTIVITY);
//                switchTabChoosed(2);
//                break;
//            case R.id.tab_iv_3:
//            	this.mTabHost.setCurrentTabByTag(Constant.TAB_SETTING);
//                switchTabChoosed(3);
//                break;

//            case R.id.layout_setting:
//				Intent intent = new Intent();
//				intent.putExtra("flog", "设置");
//				intent.setClass(getApplicationContext(), ActivitySetting.class);
//				startActivity(intent);
//            	break;
            case R.id.title_bar_right_menu:
                ActivityDetectionCenter.whetherFromDiary = false;
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), ActivityDetectionCenter.class);
                startActivity(intent1);
                break;
            case R.id.title_bar_left_menu:
                //进行扫码
                Intent intent = new Intent(
                        TabActivityMain.this,
                        ActivityCapture.class);
                startActivityForResult(intent, SCANER_CODE);
                break;

            default:
//                Log.e(TAG, "tabs 5 or -1");
                break;
        }


//		}
    }

    public static void changePage(int id) {
        switch (id) {

            case R.id.tab_iv_0:
                mTabHost.setCurrentTabByTag(Constant.TAB_DIARY);
                switchTabChoosed(0);
                break;
            case R.id.tab_iv_1:
                mTabHost.setCurrentTabByTag(Constant.TAB_CONSULT);
                switchTabChoosed(1);
                break;
            case R.id.tab_iv_2:
                mTabHost.setCurrentTabByTag(Constant.TAB_ACTIVITY);
                switchTabChoosed(2);
                break;
            case R.id.tab_iv_3:
                mTabHost.setCurrentTabByTag(Constant.TAB_SETTING);
                switchTabChoosed(3);
                break;
            default:
                break;
        }
    }

//	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
//		@Override
//		public void openMenu() {
//			is_closed = false;
////			tintManager.setStatusBarTintResource(R.color.trans_statusbar_bg);
//			leftMenu.setVisibility(View.GONE);
//		}
//
//		@Override
//		public void closeMenu() {
//			is_closed = true;
////			tintManager.setStatusBarTintResource(R.color.statusbar_bg);
//			leftMenu.setVisibility(View.VISIBLE);
//		}
//	};

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        imageLoader.displayImage(AbsParam.getBaseUrl()+UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL), info.getIcon(), options);
//		if(((ActivityDetectionCenter)mStacks.get(Constant.TAB_DETECTION).lastElement()).adapter!=null){
//			((ActivityDetectionCenter)mStacks.get(Constant.TAB_DETECTION).lastElement()).adapter.notifyDataSetChanged();
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					((ActivityDetectionCenter)mStacks.get(Constant.TAB_DETECTION).lastElement()).adapter.notifyDataSetChanged();
//				}
//			}, 100);
//		}
    }


    // What good method is to access resideMenu？
//	public ResideMenu getResideMenu() {
//		return resideMenu;
//	}


//	// 监听手机上的BACK键


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                // 判断菜单是否关闭
//        		if (is_closed) {
                // 判断两次点击的时间间隔（默认设置为2秒）
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();

                    mExitTime = System.currentTimeMillis();
                } else {
//        				stopService(intentLocation);
//        				MakeCall.logout(getApplicationContext());
                    AppPool.finishAllAct();
                    finish();
                    System.exit(0);
                    super.onBackPressed();
                }
//        		} 
//        		else {
//        			resideMenu.closeMenu();
//        		}
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 回调接口
     *
     * @author kuangtiecheng
     */
//	public interface MyTouchListener
//	{
//	        public void onTouchEvent(MotionEvent event);
//	}
//	public interface MyTouchListener
//	{
//			public boolean onTouch(View v, MotionEvent event);
//			public void onTouchEvent(MotionEvent event);
//	}
    /*
	 * 保存MyTouchListener接口的列表
	 */
//	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<TabActivityMain.MyTouchListener>();
//
//	/**
//	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
//	 * @param listener
//	 */
//	public void registerMyTouchListener(MyTouchListener listener)
//	{
//	        myTouchListeners.add( listener );
//	}
//
//	/**
//	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
//	 * @param listener
//	 */
//	public void unRegisterMyTouchListener(MyTouchListener listener)
//	{
//	        myTouchListeners.remove( listener );
//	}
//	/**
//	 * 分发触摸事件给所有注册了MyTouchListener的接口
//	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//	        // TODO Auto-generated method stub 
//	        for (MyTouchListener listener : myTouchListeners) {
//	                       listener.onTouchEvent(ev);
//	        }
//	        return super.dispatchTouchEvent(ev);
//	}
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("为了我们能为您提供更好的服务，您需要打开设备的定位功能，是否继续？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    dialog.dismiss();

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
    }

    /**
     * 初始化提醒记录,应对VERSION=1之前的两种情况
     */
    public void initDiaryRemind(Context context) {
        BeanHealthDiaryLocal beanHealthDiaryLocal = new BeanHealthDiaryLocal();
        beanHealthDiaryLocal.setUserid((int) UtilsSharedData.getLong(Constant.USER_ID, 0));
        beanHealthDiaryLocal.setValid(DataBaseHelper.VALID_FLAG);
        DataBaseHelper.initDatabase(context, beanHealthDiaryLocal);
    }
}
