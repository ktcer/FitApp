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
package com.cn.fit.ui.chat.common.utils;

import java.io.InvalidClassException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cn.fit.ui.AppMain;


/**
 * 参数配置序列号
 *
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class ECPreferences {


    /**
     * The name of the CCP Demo settings file.
     */
    public static final String CCP_PREFERENCE = getDefaultSharedPreferencesFileName();

    /**
     * Constructor of <code>CcpPreferences</code>.
     */
    private ECPreferences() {
        super();
    }

    /**
     * Method that initializes the defaults preferences of the application.
     */
    public static void loadDefaults() {
        //Sets the default preferences if no value is set yet
        try {
            Map<ECPreferenceSettings, Object> defaultPrefs =
                    new HashMap<ECPreferenceSettings, Object>();
            ECPreferenceSettings[] values = ECPreferenceSettings.values();
            int cc = values.length;
            for (int i = 0; i < cc; i++) {
                defaultPrefs.put(values[i], values[i].getDefaultValue());
            }
            savePreferences(defaultPrefs, false, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.e("Save default settings fails");
        }
    }


    /**
     * Method that returns the shared preferences of the application.
     *
     * @return SharedPreferences The shared preferences of the application
     * @hide
     */
    public static SharedPreferences getSharedPreferences() {
        return AppMain.getInstance().getSharedPreferences(
                getDefaultSharedPreferencesFileName(), Context.MODE_MULTI_PROCESS);
    }

    /**
     * @return SharedPreferences file name
     */
    public static String getDefaultSharedPreferencesFileName() {
        return "com.cn.aihu.ui.chat_preferences";
    }

    /**
     * To obtain the system preferences to save the file to edit the object
     *
     * @return
     */
    public static Editor getSharedPreferencesEditor() {
        SharedPreferences cCPreferences = getSharedPreferences();
        Editor edit = cCPreferences.edit();
        edit.remove("");
        return edit;
    }

    /**
     * Method that saves a preference.
     *
     * @param pref    The preference identifier
     * @param value   The value of the preference
     * @param applied If the preference was applied
     * @throws java.io.InvalidClassException If the value of the preference is not of the
     *                                       type of the preference
     */
    public static void savePreference(ECPreferenceSettings pref, Object value, boolean applied)
            throws InvalidClassException {
        Map<ECPreferenceSettings, Object> prefs =
                new HashMap<ECPreferenceSettings, Object>();
        prefs.put(pref, value);
        savePreferences(prefs, applied);
    }

    /**
     * Method that saves the preferences passed as argument.
     *
     * @param prefs   The preferences to be saved
     * @param applied If the preference was applied
     * @throws java.io.InvalidClassException If the value of a preference is not of the
     *                                       type of the preference
     */
    public static void savePreferences(Map<ECPreferenceSettings, Object> prefs, boolean applied)
            throws InvalidClassException {
        savePreferences(prefs, true, applied);
    }

    /**
     * Method that saves the preferences passed as argument.
     *
     * @param prefs          The preferences to be saved
     * @param noSaveIfExists No saves if the preference if has a value
     * @param applied        If the preference was applied
     * @throws java.io.InvalidClassException If the value of a preference is not of the
     *                                       type of the preference
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    private static void savePreferences(
            Map<ECPreferenceSettings, Object> prefs, boolean noSaveIfExists, boolean applied)
            throws InvalidClassException {
        //Get the preferences editor
        SharedPreferences sp = getSharedPreferences();
        Editor editor = sp.edit();

        //Save all settings
        Iterator<ECPreferenceSettings> it = prefs.keySet().iterator();
        while (it.hasNext()) {
            ECPreferenceSettings pref = it.next();
            if (!noSaveIfExists && sp.contains(pref.getId())) {
                //The preference already has a value
                continue;
            }

            //Known and valid types
            Object value = prefs.get(pref);
            if (value == null) {
                return;
            }
            if (value instanceof Boolean && pref.getDefaultValue() instanceof Boolean) {
                editor.putBoolean(pref.getId(), ((Boolean) value).booleanValue());
            } else if (value instanceof String && pref.getDefaultValue() instanceof String) {
                editor.putString(pref.getId(), (String) value);
            } else if (value instanceof Integer && pref.getDefaultValue() instanceof Integer) {
                editor.putInt(pref.getId(), (Integer) value);
            } else if (value instanceof Long && pref.getDefaultValue() instanceof Long) {
                editor.putLong(pref.getId(), (Long) value);
            } else if (value instanceof Set && pref.getDefaultValue() instanceof Set) {
                //editor.putStringSet(pref.getId(), (Set<String>)value);
            } else if (value instanceof ObjectStringIdentifier
                    && pref.getDefaultValue() instanceof ObjectStringIdentifier) {
                editor.putString(pref.getId(), ((ObjectStringIdentifier) value).getId());
            } else {
                //The object is not of the appropriate type
                String msg = String.format(
                        "%s: %s",
                        pref.getId(),
                        value.getClass().getName());
                LogUtil.e(String.format(
                        "Configuration error. InvalidClassException: %s",
                        msg));
                throw new InvalidClassException(msg);
            }

        }

        //Commit settings
        editor.commit();

    }
}
