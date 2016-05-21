/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.fit.ui.chat.common.utils;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;

/**
 * <p>Title: ClipboardUtils.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-6-12
 */
public class ClipboardUtils {

    /**
     * @param context
     * @param label   User-visible label for the clip data.
     * @param text    The actual text in the clip.
     */
    public static void copyFromEdit(Context context, CharSequence label, CharSequence text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText(label, text));
            return;
        }

        ((android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(text);
    }

    /**
     * @param context
     * @return
     */
    public static CharSequence pasteToResult(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipData clipData = ((android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE))
                    .getPrimaryClip();
            if ((clipData == null) || (clipData.getItemCount() <= 0)) {
                return null;
            }

            ClipData.Item item = clipData.getItemAt(0);
            if (item == null) {
                return null;
            }
            return item.getText();
        }

        return ((android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).getText();
    }
}
