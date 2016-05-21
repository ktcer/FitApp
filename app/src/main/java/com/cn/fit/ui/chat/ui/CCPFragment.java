/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
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
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cn.fit.R;
import com.cn.fit.ui.basic.FragmentBasic;
import com.cn.fit.ui.chat.common.utils.DensityUtil;

/**
 * 应用页面View基类，每个View 必须要继承与该类并实现相应的方法
 * Created by Jorstin on 2015/3/18.
 */
public abstract class CCPFragment extends FragmentBasic {

    /**
     * 自定义页面根布局，获取页面改变之后的大小值
     */
    private View mLayoutListenerView;
    /**
     * 页面内容装载器,加载页面的标题（如果设置）和主界面
     */
    private LinearLayout mCCProotView;
    /**
     * 根据getLayoutId()加载所得的页面布局
     */
    private ViewGroup mContentView;
    /**
     * 广播拦截器
     */
    private InternalReceiver internalReceiver;
    /**
     * 当前页面是否可以销毁
     */
    private boolean isFinish = false;
    ;

    final Handler mSupperHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            super.dispatchMessage(msg);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mLayoutListenerView == null) {
            mLayoutListenerView = inflater.inflate(R.layout.ccp_fragment, null);
            mCCProotView = (LinearLayout) mLayoutListenerView.findViewById(R.id.ccp_root_view);
            if (getTitleLayoutId() != -1) {
                // 加载页面的标题（如果不需要使用ActionBar）可以使用自定义的页面标题布局
                mCCProotView.addView(inflater.inflate(getTitleLayoutId(), null),
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        DensityUtil.getMetricsDensity(getActivity(), 50.0F));

            }
            // 加载主界面资源（标题除外）
            if (getLayoutId() != -1) {
                mContentView = (ViewGroup) inflater.inflate(getLayoutId(), null);
                mCCProotView.addView(mContentView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
            }
        }

        ViewGroup mViewRoot = (ViewGroup) mLayoutListenerView.getParent();
        if (mViewRoot != null) {
            mViewRoot.removeView(mLayoutListenerView);
        }

        return mLayoutListenerView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            // 注销广播监听器
            getActivity().unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    /**
     * 处理按钮按下事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return false;
    }

    /**
     * 注册广播Action，子类如果需要监听广播可以调用
     * 该方法传入相应事件的Action
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
        //intentfilter.addAction(CASIntent.ACTION_SERVICE_DESTORY);
        //intentfilter.addAction(CASIntent.ACTION_FORCE_DEACTIVE);
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        getActivity().registerReceiver(internalReceiver, intentfilter);
    }

    /**
     * 返回一个Handler 主线程
     *
     * @return
     */
    public android.os.Handler getSupperHandler() {
        return mSupperHandler;
    }

    /**
     * 重载页面关闭方法
     */
    public void finish() {
        if (getActivity() == null) {
            return;
        }
        if (isFinish) {
            getActivity().finish();
            return;
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("MainScreen");
    }

    /**
     * 查找	View
     *
     * @param paramInt
     * @return
     */
    public final View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     *
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
        // 广播处理
    }

    /**
     * 每个页面需要实现该方法返回一个该页面所对应的资源ID
     *
     * @return 页面资源ID
     */
    protected abstract int getLayoutId();

    /**
     * 如果需要自定义页面标题，则需要重载该方法
     *
     * @return
     */
    public int getTitleLayoutId() {
        return -1;
    }

    /**
     * 自定义应用全局广播处理器，方便全局拦截广播并进行分发
     *
     * @author 容联•云通讯
     * @version 4.0
     * @date 2014-12-4
     */
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }

    }
}
