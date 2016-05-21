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
package com.cn.fit.ui.chat.storage;

import java.util.ArrayList;

/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-13
 */
public abstract class ECObservable<T> {

    protected final ArrayList<T> mObservers = new ArrayList<T>();

    /**
     * 注册观察者
     *
     * @param observer
     */
    public void registerObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                throw new IllegalStateException("ECObservable " + observer + " is already registered.");
            }
            mObservers.add(observer);
        }
    }

    /**
     * 移除观察
     *
     * @param observer
     */
    public void unregisterObserver(T observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index == -1) {
                throw new IllegalStateException("ECObservable " + observer + " was not registered.");
            }
            mObservers.remove(index);
        }
    }

    /**
     * 移除所有观察着
     */
    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }
}
