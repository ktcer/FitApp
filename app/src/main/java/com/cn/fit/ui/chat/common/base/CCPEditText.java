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
package com.cn.fit.ui.chat.common.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.cn.fit.ui.chat.common.utils.EmoticonUtil;


/**
 * @author Jorstin Chan@容联•云通讯
 * @version 4.0
 * @date 2014-12-10
 */
public class CCPEditText extends EditText {


    public InputConnection miInputConnection;

    /**
     * @param context
     */
    public CCPEditText(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CCPEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CCPEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        miInputConnection = super.onCreateInputConnection(outAttrs);
        return miInputConnection;
    }

    public InputConnection getInputConnection() {
        return miInputConnection;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean onTextContextMenuItem = super.onTextContextMenuItem(id);
        if (id == android.R.id.paste) {
            // Gets the position of the cursor
            int selectionStart = getSelectionStart();
            setText(/*EmoticonUtil.emoji2CharSequence(getContext(), */getText()/*.toString(), (int) getTextSize(), false)*/);
            setSelection(selectionStart);
        }

        return onTextContextMenuItem;

    }

    public void setEmojiText(String text) {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        StringBuffer buffer = new StringBuffer(getText());
        String str = buffer.substring(0, selectionStart) + text + buffer.substring(selectionEnd, buffer.length());
        setText(EmoticonUtil.emoji2CharSequence(getContext(), str, (int) getTextSize(), false));
        setSelection(selectionStart + text.length());
    }


    public int getTextSelection(String text, int position) {

        if (TextUtils.isEmpty(text)) {
            return position;
        }
        return 0;
    }

    @Override
    public Bundle getInputExtras(boolean create) {
        return super.getInputExtras(create);
    }
}
