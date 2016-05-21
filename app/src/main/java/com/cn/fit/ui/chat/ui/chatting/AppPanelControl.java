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
package com.cn.fit.ui.chat.ui.chatting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.common.CCPAppManager;


/**
 * 聊天插件功能控制器
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class AppPanelControl {


    private Context mContext;

    public int[] cap = new int[]{R.string.app_panel_pic,
            R.string.app_panel_tackpic,
            R.string.app_panel_file,};


    /**
     *
     */
    public AppPanelControl() {
        mContext = CCPAppManager.getContext();
    }

    /**
     * @return
     */
    public List<Capability> getCapability() {
        List<Capability> capabilities = new ArrayList<Capability>();

        for (int i = 0; i < cap.length; i++) {
            Capability capability = getCapability(cap[i]);
            capabilities.add(capabilities.size(), capability);
        }
        return capabilities;
    }

    /**
     * @param resid
     * @return
     */
    private Capability getCapability(int resid) {
        Capability capability = null;
        switch (resid) {
            case R.string.app_panel_pic:
                capability = new Capability(getContext().getString(R.string.app_panel_pic), R.drawable.image_icon);
                break;
            case R.string.app_panel_tackpic:

                capability = new Capability(getContext().getString(R.string.app_panel_tackpic), R.drawable.photograph_icon);
                break;
            case R.string.app_panel_file:

                capability = new Capability(getContext().getString(R.string.app_panel_file), R.drawable.capability_file_icon);
                break;

            default:
                break;
        }
        capability.setId(resid);
        return capability;
    }

    /**
     * @return
     */
    private Context getContext() {
        if (mContext == null) {
            mContext = AppMain.getInstance().getApplicationContext();
        }
        return mContext;
    }
}
