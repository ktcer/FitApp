/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.chat.common.base.CCPCustomViewPager;
import com.cn.fit.ui.chat.common.base.CCPLauncherUITabView;
import com.cn.fit.ui.chat.common.dialog.ECAlertDialog;
import com.cn.fit.ui.chat.common.dialog.ECProgressDialog;
import com.cn.fit.ui.chat.common.utils.ECNotificationManager;
import com.cn.fit.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.fit.ui.chat.common.utils.ECPreferences;
import com.cn.fit.ui.chat.common.utils.LogUtil;
import com.cn.fit.ui.chat.core.ContactsCache;
import com.cn.fit.ui.chat.core.ContactsCache.InterfaceGetContactsListener;
import com.cn.fit.ui.chat.storage.ContactSqlManager;
import com.cn.fit.ui.chat.storage.GroupNoticeSqlManager;
import com.cn.fit.ui.chat.storage.GroupSqlManager;
import com.cn.fit.ui.chat.storage.IMessageSqlManager;
import com.cn.fit.ui.chat.ui.chatting.ChattingActivity;
import com.cn.fit.ui.chat.ui.chatting.IMChattingHelper;
import com.cn.fit.ui.chat.ui.contact.ECContacts;
import com.cn.fit.ui.chat.ui.contact.MobileContactActivity;
import com.cn.fit.ui.chat.ui.contact.MobileContactActivity.MobileContactFragment;
import com.cn.fit.ui.chat.ui.group.GroupNoticeActivity;
import com.cn.fit.ui.patient.main.TabActivityMain;
import com.cn.fit.ui.patient.others.myaccount.ActivityLogin;
import com.cn.fit.util.Constant;
import com.cn.fit.util.UtilsSharedData;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主界面（消息会话界面、联系人界面、群组界面）
 */
public class ActivityHealthAssist extends ActivityBasic implements
        View.OnClickListener,
        ConversationListFragment.OnUpdateMsgUnreadCountsListener,
        InterfaceGetContactsListener {

    private static final String TAG = "ActivityHealthAssist";
    /**
     * 当前ECLauncherUI 实例
     */
    public static ActivityHealthAssist mLauncherUI;

    /**
     * 当前ECLauncherUI实例产生个数
     */
    public static int mLauncherInstanceCount = 0;

    /**
     * 当前主界面RootView
     */
    public View mLauncherView;

    /**
     * LauncherUI 主界面导航控制View ,包含三个View Tab按钮
     */
    private CCPLauncherUITabView mLauncherUITabView;
    /**
     * 三个TabView所对应的三个页面的适配器
     */
    private CCPCustomViewPager mCustomViewPager;

    /**
     * 联系人列表
     */
    // public List<ECContacts> contacts = new ArrayList<ECContacts>();
    // public HashMap<String,ECContacts> contacts = new HashMap<String,
    // ECContacts>();

    /**
     * 沟通、联系人、群组适配器
     */
    public LauncherViewPagerAdapter mLauncherViewPagerAdapter;

    // private OverflowHelper mOverflowHelper;

    /**
     * 当前显示的TabView Fragment
     */
    private int mCurrentItemPosition = -1;

    /**
     * 会话界面(沟通)
     */
    private static final int TAB_CONVERSATION = 0;

    /**
     * 通讯录界面(联系人)
     */
    private static final int TAB_ADDRESS = 1;

    /**
     * 群组界面
     */
    // private static final int TAB_GROUP = 2;

    /**
     * {@link CCPLauncherUITabView} 是否已经被初始化
     */
    private boolean mTabViewInit = false;

    /**
     * 缓存三个TabView
     */
    private final HashMap<Integer, Fragment> mTabViewCache = new HashMap<Integer, Fragment>();

    // private OverflowAdapter.OverflowItem[] mItems = new
    // OverflowAdapter.OverflowItem[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBanner = false;
        int pid = android.os.Process.myPid();
        UtilsSharedData.initDataShare(this);
        if (mLauncherUI != null) {
            LogUtil.i(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
                    "finish last LauncherUI");
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        mLauncherInstanceCount++;
        super.onCreate(savedInstanceState);
        // initWelcome();
        // mOverflowHelper = new OverflowHelper(this);

        // 设置页面默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // ECContentObservers.getInstance().initContentObserver();
        mLauncherView = getLayoutInflater().inflate(R.layout.main_tab, null);
        setContentView(mLauncherView);

        if (mLauncherUITabView != null) {
            mLauncherUITabView.setOnUITabViewClickListener(null);
            mLauncherUITabView.setVisibility(View.VISIBLE);
        }
        mLauncherUITabView = (CCPLauncherUITabView) findViewById(R.id.laucher_tab_top);
        List<Integer> tabTile = new ArrayList<Integer>();
        tabTile.add(R.string.lovenurse);
        tabTile.add(R.string.person);
        mLauncherUITabView.setTabViewText(tabTile, 2);

        ContactsCache.getInstance().setOnGetContactsDoneListener(this);
        if (!mTabViewInit) {
            initLauncherUIView();
        }
    }

    /**
     * 初始化主界面UI视图
     */
    private void initLauncherUIView() {
        // mLauncherView = getLayoutInflater().inflate(R.layout.main_tab, null);
        // setContentView(mLauncherView);

        mTabViewInit = true;
        // initOverflowItems();
        mCustomViewPager = (CCPCustomViewPager) findViewById(R.id.pager);
        mCustomViewPager.setOffscreenPageLimit(2);

        // if (mLauncherUITabView != null) {
        // mLauncherUITabView.setOnUITabViewClickListener(null);
        // mLauncherUITabView.setVisibility(View.VISIBLE);
        // }
        // mLauncherUITabView = (CCPLauncherUITabView)
        // findViewById(R.id.laucher_tab_top);
        mCustomViewPager.setSlideEnabled(true);
        mLauncherViewPagerAdapter = new LauncherViewPagerAdapter(this,
                mCustomViewPager);
        // List<Integer> tabTile = new ArrayList<Integer>();
        // tabTile.add(R.string.lovenurse);
        // tabTile.add(R.string.person);
        // mLauncherUITabView.setTabViewText(tabTile,2);
        mLauncherUITabView
                .setOnUITabViewClickListener(mLauncherViewPagerAdapter);

        // findViewById(R.id.btn_plus).setOnClickListener(this);
        ctrlViewTab(0);

        Intent intent = getIntent();
        if (intent != null && intent.getIntExtra("launcher_from", -1) == 1) {
            // 检测从登陆界面过来，判断是不是第一次安装使用
            checkFirstUse();
        }

        // 如果是登陆过来的
        doInitAction();
        hideProgressBar();
    }

    // private void settingPersionInfo() {
    // if(IMChattingHelper.getInstance().mServicePersonVersion == 0 &&
    // CCPAppManager.getClientUser().getpVersion() == 0) {
    // Intent settingAction = new Intent(this,
    // SettingPersionInfoActivity.class);
    // settingAction.putExtra("from_regist" , true);
    // startActivityForResult(settingAction, 0x2a);
    // return ;
    // }
    // }

    /**
     * 检测离线消息
     */
    private void checkOffineMessage() {
        if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS) {
            return;
        }
        ECHandlerHelper handlerHelper = new ECHandlerHelper();
        handlerHelper.postDelayedRunnOnThead(new Runnable() {
            @Override
            public void run() {
                boolean result = IMChattingHelper.isSyncOffline();
                if (!result) {
                    ECHandlerHelper.postRunnOnUI(new Runnable() {
                        @Override
                        public void run() {
                            disPostingLoading();
                        }
                    });
                    IMChattingHelper.checkDownFailMsg();
                }
            }
        }, 1000);
    }

    private boolean isFirstUse() {
        boolean firstUse = ECPreferences.getSharedPreferences().getBoolean(
                ECPreferenceSettings.SETTINGS_FIRST_USE.getId(),
                ((Boolean) ECPreferenceSettings.SETTINGS_FIRST_USE
                        .getDefaultValue()).booleanValue());
        return firstUse;
    }

    private ECProgressDialog mPostingdialog;

    private void checkFirstUse() {
        boolean firstUse = isFirstUse();

        // Display the welcome message?
        if (firstUse) {
            if (IMChattingHelper.isSyncOffline()) {
                mPostingdialog = new ECProgressDialog(this,
                        R.string.tab_loading);
                mPostingdialog.setCanceledOnTouchOutside(false);
                mPostingdialog.setCancelable(false);
                mPostingdialog.show();
            }
            // Don't display again this dialog
            try {
                ECPreferences.savePreference(
                        ECPreferenceSettings.SETTINGS_FIRST_USE, Boolean.FALSE,
                        true);
            } catch (Exception e) {
                /** NON BLOCK **/
            }
        }
    }

    /**
     * 根据TabFragment Index 查找Fragment
     *
     * @param tabIndex
     * @return
     */
    public final BaseFragment getTabView(int tabIndex) {
        LogUtil.d(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
                "get tab index " + tabIndex);
        if (tabIndex < 0) {
            return null;
        }

        if (mTabViewCache.containsKey(Integer.valueOf(tabIndex))) {
            return (BaseFragment) mTabViewCache.get(Integer.valueOf(tabIndex));
        }

        BaseFragment mFragment = null;
        switch (tabIndex) {
            case TAB_CONVERSATION:
                mFragment = (TabFragment) Fragment.instantiate(this,
                        ConversationListFragment.class.getName(), null);
                break;
            case TAB_ADDRESS:
                mFragment = (TabFragment) Fragment
                        .instantiate(this,
                                MobileContactActivity.MobileContactFragment.class
                                        .getName(), null);
                break;
            // case TAB_GROUP:
            // mFragment = (TabFragment) Fragment.instantiate(this,
            // GroupListFragment.class.getName(), null);
            // break;

            default:
                break;
        }

        if (mFragment != null) {
            mFragment.setActionBarActivity(this);
        }
        mTabViewCache.put(Integer.valueOf(tabIndex), mFragment);
        return mFragment;
    }

    /**
     * 根据提供的子Fragment index 切换到对应的页面
     *
     * @param index 子Fragment对应的index
     */
    public void ctrlViewTab(int index) {

        LogUtil.d(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
                "change tab to " + index + ", cur tab " + mCurrentItemPosition
                        + ", has init tab " + mTabViewInit
                        + ", tab cache size " + mTabViewCache.size());
        if ((!mTabViewInit || index < 0)
                || (mLauncherViewPagerAdapter != null && index > mLauncherViewPagerAdapter
                .getCount() - 1)) {
            return;
        }

        if (mCurrentItemPosition == index) {
            return;
        }
        mCurrentItemPosition = index;

        if (mLauncherUITabView != null) {
            mLauncherUITabView.doChangeTabViewDisplay(mCurrentItemPosition);
        }

        if (mCustomViewPager != null) {
            mCustomViewPager.setCurrentItem(mCurrentItemPosition, false);
        }

    }

    // void initOverflowItems() {
    // if (mItems == null) {
    // mItems = new OverflowAdapter.OverflowItem[3];
    // }
    // //mItems[0] = new
    // OverflowAdapter.OverflowItem(getString(R.string.main_plus_chat));
    // mItems[0] = new
    // OverflowAdapter.OverflowItem(getString(R.string.main_plus_groupchat));
    // mItems[1] = new
    // OverflowAdapter.OverflowItem(getString(R.string.main_plus_querygroup));
    // mItems[2] = new
    // OverflowAdapter.OverflowItem(getString(R.string.main_plus_settings));
    // }

    // @Override
    // public boolean onMenuOpened(int featureId, Menu menu) {
    // controlPlusSubMenu();
    // return false;
    // }

    // @Override
    // public boolean dispatchKeyEvent(KeyEvent event) {
    // LogUtil.d(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
    // " onKeyDown");
    //
    // if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
    // && event.getAction() == KeyEvent.ACTION_UP) {
    // // dismiss PlusSubMenuHelper
    // if (mOverflowHelper != null && mOverflowHelper.isOverflowShowing()) {
    // mOverflowHelper.dismiss();
    // return true;
    // }
    // }
    //
    // // 这里可以进行设置全局性的menu菜单的判断
    // if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
    // && event.getAction() == KeyEvent.ACTION_DOWN) {
    // doTaskToBackEvent();
    // }
    //
    // try {
    //
    // return super.dispatchKeyEvent(event);
    // } catch (Exception e) {
    // LogUtil.e(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
    // "dispatch key event catch exception " + e.getMessage());
    // }
    //
    // return false;
    // }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (internalReceiver != null) {
            unregisterReceiver(internalReceiver);
        }
    }

    @Override
    public void onResume() {
        LogUtil.i(LogUtil.getLogUtilsTag(ActivityHealthAssist.class),
                "onResume start");
        super.onResume();
        TabActivityMain.changePage(R.id.tab_iv_1);
        Log.i("test", "tab is right?");
        ContactsCache.getInstance().load();
        // 统计时长
        // MobclickAgent.onResume(this);

        // boolean fullExit =
        // ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_FULLY_EXIT.getId(),
        // false);
        // if (fullExit) {
        // try {
        // ECHandlerHelper.removeCallbacksRunnOnUI(initRunnable);
        // ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT,
        // false, true);
        // ContactsCache.getInstance().stop();
        // CCPAppManager.setClientUser(null);
        // ECDevice.unInitial();
        // finish();
        // android.os.Process.killProcess(android.os.Process.myPid());
        // return;
        // } catch (InvalidClassException e) {
        // e.printStackTrace();
        // }
        // }
        // getExperts();

        if (mLauncherUITabView == null) {
            // String account = getAutoRegistAccount();
            // if (TextUtils.isEmpty(account)) {
            // startActivity(new Intent(this, LoginActivity.class));
            // finish();
            // return;
            // }
            // 注册第一次登陆同步消息
            registerReceiver(new String[]{
                    IMChattingHelper.INTENT_ACTION_SYNC_MESSAGE,
                    SDKCoreHelper.ACTION_SDK_CONNECT});
            // ClientUser user = new ClientUser("").from(account);
            // CCPAppManager.setClientUser(user);
            // if(!ContactSqlManager.hasContact(user.getUserId())) {
            // ECContacts contacts = new ECContacts();
            // contacts.setClientUser(user);
            // ContactSqlManager.insertContact(contacts);
            // }

            if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS
                    && !SDKCoreHelper.isKickOff()) {
                // ContactsCache.getInstance().load();
                SDKCoreHelper.init(this);
            }

        }
        OnUpdateMsgUnreadCounts();
    }

    public void handlerKickOff(String kickoffText) {
        if (isFinishing()) {
            return;
        }
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(this, kickoffText,
                getString(R.string.dialog_btn_confim),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ECNotificationManager.getInstance()
                                .forceCancelNotification();
                        restartAPP();
                    }
                });
        buildAlert.setTitle("异地登陆");
        buildAlert.setCanceledOnTouchOutside(false);
        buildAlert.setCancelable(false);
        buildAlert.show();
    }

    public void restartAPP() {
        ECDevice.unInitial();
        try {
            ECPreferences.savePreference(
                    ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
        } catch (InvalidClassException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
        finishAllAct();
        startActivity(ActivityLogin.class);

    }

    /**
     * 检查是否需要自动登录
     *
     * @return
     */
    private String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences
                .getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(),
                (String) registAuto.getDefaultValue());
        return registAccount;
    }

    // private void controlPlusSubMenu() {
    // if (mOverflowHelper == null) {
    // return;
    // }
    //
    // if (mOverflowHelper.isOverflowShowing()) {
    // mOverflowHelper.dismiss();
    // return;
    // }
    //
    // mOverflowHelper.setOverflowItems(mItems);
    // mOverflowHelper.setOnOverflowItemClickListener(mOverflowItemCliclListener);
    // mOverflowHelper.showAsDropDown(findViewById(R.id.btn_plus));
    // }

    @Override
    public void onPause() {
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "KEVIN Launcher onPause");
        super.onPause();
        // 友盟统计API
        // MobclickAgent.onPause(this);
    }

    /**
     * 返回隐藏到后台
     */
    // public void doTaskToBackEvent() {
    // moveTaskToBack(true);
    //
    // }
    @Override
    public void OnUpdateMsgUnreadCounts() {
        int unreadCount = IMessageSqlManager.qureyAllSessionUnreadCount();
        int notifyUnreadCount = IMessageSqlManager.getUnNotifyUnreadCount();
        int count = unreadCount;
        if (unreadCount >= notifyUnreadCount) {
            count = unreadCount - notifyUnreadCount;
        }
        if (mLauncherUITabView != null) {
            mLauncherUITabView.updateMainTabUnread(count);
        }
        if (count > 0) {
            TabActivityMain.mRedMark.setVisibility(View.VISIBLE);
        } else {
            TabActivityMain.mRedMark.setVisibility(View.GONE);
        }
    }

    /**
     * TabView 页面适配器
     *
     * @author 容联•云通讯
     * @version 4.0
     * @date 2014-12-4
     */
    private class LauncherViewPagerAdapter extends FragmentStatePagerAdapter
            implements ViewPager.OnPageChangeListener,
            CCPLauncherUITabView.OnUITabViewClickListener {
        /**
         *
         */
        private int mClickTabCounts;
        private ContactListFragment mContactUI;
        // private GroupListFragment mGroupListFragment;

        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        public LauncherViewPagerAdapter(FragmentActivity fm, ViewPager pager) {
            super(fm.getSupportFragmentManager());
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(String tabSpec, Class<?> clss, Bundle args) {
            String tag = tabSpec;

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return mLauncherUI.getTabView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageScrollStateChanged state = " + state);

            // if (state != ViewPager.SCROLL_STATE_IDLE || mGroupListFragment ==
            // null) {
            // return;
            // }
            // if(mGroupListFragment != null) {
            // mGroupListFragment.onGroupFragmentVisible(true);
            // mGroupListFragment = null;
            // }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageScrolled " + position + " " + positionOffset + " "
                            + positionOffsetPixels);
            if (mLauncherUITabView != null) {
                mLauncherUITabView.doTranslateImageMatrix(position,
                        positionOffset);
            }
            if (positionOffset != 0.0F) {
                // if (mGroupListFragment == null) {
                // mGroupListFragment = (GroupListFragment)
                // getTabView(CCPLauncherUITabView.TAB_VIEW_THIRD);
                // mGroupListFragment.onGroupFragmentVisible(false);
                // }
                return;
            }
        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageSelected");
            if (mLauncherUITabView != null) {
                mLauncherUITabView.doChangeTabViewDisplay(position);
                mCurrentItemPosition = position;
            }
        }

        @Override
        public void onTabClick(int tabIndex) {
            if (tabIndex == mCurrentItemPosition) {
                LogUtil.d(
                        LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                        "on click same index " + tabIndex);
                // Perform a rolling
                TabFragment item = (TabFragment) getItem(tabIndex);
                item.onTabFragmentClick();
                return;
            }

            mClickTabCounts += mClickTabCounts;
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onUITabView Click count " + mClickTabCounts);
            mViewPager.setCurrentItem(tabIndex);
        }

    }

    /**
     * 网络注册状态改变
     *
     * @param connect
     */
    public void onNetWorkNotify(ECDevice.ECConnectState connect) {
        BaseFragment tabView = getTabView(TAB_CONVERSATION);
        if (tabView instanceof ConversationListFragment && tabView.isAdded()) {
            ((ConversationListFragment) tabView).updateConnectState();
        }
    }

    // @Override
    // public void onClick(View v) {
    // if (v.getId() == R.id.btn_plus) {
    // controlPlusSubMenu();
    // }
    // }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Intent actionIntent = intent;
        String userName = actionIntent.getStringExtra("Main_FromUserName");
        String mSession = actionIntent.getStringExtra("Main_Session");
        ECContacts contacts = ContactSqlManager
                .getContactLikeUsername(userName);
        if (contacts != null) {
            LogUtil.d(LogUtil.getLogUtilsTag(getClass()),
                    "[onNewIntent] userName = " + userName + " , contact_id "
                            + contacts.getContactid());

            if (GroupNoticeSqlManager.CONTACT_ID
                    .equals(contacts.getContactid())) {
                Intent noticeintent = new Intent(this,
                        GroupNoticeActivity.class);
                startActivity(noticeintent);
                return;
            }

            Intent chatIntent = new Intent(this, ChattingActivity.class);
            if (!TextUtils.isEmpty(mSession) && mSession.startsWith("g")) {
                ECGroup ecGroup = GroupSqlManager.getECGroup(mSession);
                if (ecGroup == null) {
                    return;
                }
                chatIntent.putExtra(ChattingActivity.RECIPIENTS, mSession);
                chatIntent.putExtra(ChattingActivity.CONTACT_USER,
                        ecGroup.getName());
            } else {
                chatIntent.putExtra(ChattingActivity.RECIPIENTS,
                        contacts.getContactid());
                chatIntent.putExtra(ChattingActivity.CONTACT_USER,
                        contacts.getNickname());
            }
            startActivity(chatIntent);
            return;
        }
    }

    /**************************************************************************
     * 注意看这儿哦
     */

    // private final AdapterView.OnItemClickListener mOverflowItemCliclListener
    // = new AdapterView.OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view,
    // int position, long id) {
    // controlPlusSubMenu();
    // if (position == 3) {
    // startActivity(new Intent(ActivityHealthAssist.this,
    // ContactSelectListActivity.class));
    // } else if (position == 0) {
    // startActivity(new Intent(ActivityHealthAssist.this,
    // CreateGroupActivity.class));
    // } else if (position == 1) {
    // startActivity(new Intent(ActivityHealthAssist.this, BaseSearch.class));
    // } else if (position == 2) {
    // startActivity(new Intent(ActivityHealthAssist.this,
    // ActivitySettings.class));
    // }
    // }
    //
    // };

    private InternalReceiver internalReceiver;

    /**
     * 注册广播
     *
     * @param actionArray
     */
    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        registerReceiver(internalReceiver, intentfilter);
    }

    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            LogUtil.d(TAG, "[onReceive] action:" + intent.getAction());
            if (IMChattingHelper.INTENT_ACTION_SYNC_MESSAGE.equals(intent
                    .getAction())) {
                disPostingLoading();
            } else if (SDKCoreHelper.ACTION_SDK_CONNECT.equals(intent
                    .getAction())) {
                doInitAction();
                // tetstMesge();
                BaseFragment tabView = getTabView(TAB_CONVERSATION);
                if (tabView != null
                        && tabView instanceof ConversationListFragment) {
                    ((ConversationListFragment) tabView).updateConnectState();
                }
            } else if (SDKCoreHelper.ACTION_KICK_OFF.equals(intent.getAction())) {
                String kickoffText = intent.getStringExtra("kickoffText");
                handlerKickOff(kickoffText);
            }
        }
    }

    private boolean mInitActionFlag;

    /**
     * 处理一些初始化操作
     */
    private void doInitAction() {
        if (SDKCoreHelper.getConnectState() == ECDevice.ECConnectState.CONNECT_SUCCESS
                && !mInitActionFlag) {

            // 检测当前的版本
            // SDKCoreHelper.SoftUpdate mSoftUpdate = SDKCoreHelper.mSoftUpdate;
            // if(mSoftUpdate != null) {
            // if(DemoUtils.checkUpdater(mSoftUpdate.version)) {
            // boolean force = mSoftUpdate.mode == 2;
            // showUpdaterTips(force);
            // if(force) {
            // return ;
            // }
            // }
            // }

            IMChattingHelper.getInstance().getPersonInfo();
            // settingPersionInfo();
            // 检测离线消息
            checkOffineMessage();
            mInitActionFlag = true;
        }
    }

    private void disPostingLoading() {
        if (mPostingdialog != null && mPostingdialog.isShowing()) {
            mPostingdialog.dismiss();
        }
    }

    @Override
    public void getContactsDone() {
        // TODO Auto-generated method stub
        // if(!mTabViewInit){
        // initLauncherUIView();
        // }
        BaseFragment tabView = getTabView(TAB_CONVERSATION);
        if (tabView instanceof ConversationListFragment && tabView.isAdded()) {
            ((ConversationListFragment) tabView).mAdapter.notifyChange();
        }

        BaseFragment tabView1 = getTabView(TAB_ADDRESS);
        if (tabView1 instanceof MobileContactFragment && tabView1.isAdded()) {
            ((MobileContactFragment) tabView1).refreshContacts();
        }
    }

    // ECAlertDialog showUpdaterTipsDialog = null;
    // private void showUpdaterTips(final boolean force) {
    // if(showUpdaterTipsDialog != null) {
    // return ;
    // }
    // String negativeText =
    // getString(force?R.string.settings_logout:R.string.update_next);
    // String msg = getString(R.string.new_update_version);
    // showUpdaterTipsDialog = ECAlertDialog.buildAlert(this, msg,
    // negativeText,
    // getString(R.string.app_update),
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // dialog.dismiss();
    // showUpdaterTipsDialog = null;
    // if(force) {
    // try {
    // ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT,
    // true, true);
    // } catch (InvalidClassException e) {
    // e.printStackTrace();
    // }
    // restartAPP();
    // }
    // }
    // },
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // CCPAppManager.startUpdater(ActivityHealthAssist.this);
    // // restartAPP();
    // showUpdaterTipsDialog = null;
    // }
    // });
    //
    // showUpdaterTipsDialog.setTitle(R.string.app_tip);
    // showUpdaterTipsDialog.setDismissFalse();
    // showUpdaterTipsDialog.setCanceledOnTouchOutside(false);
    // showUpdaterTipsDialog.setCancelable(false);
    // showUpdaterTipsDialog.show();
    // }
}
