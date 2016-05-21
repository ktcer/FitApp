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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.cn.fit.R;
import com.cn.fit.ui.AppMain;
import com.cn.fit.ui.chat.ui.chatting.base.EmojiconHandler;
import com.cn.fit.ui.chat.ui.chatting.base.emoji.Emojicon;
import com.cn.fit.ui.chat.ui.chatting.base.emoji.People;
import com.cn.fit.ui.chat.ui.chatting.model.CCPEmoji;


/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class EmoticonUtil {

    private static HashMap<String, SpannableString> hashMap = new HashMap<String, SpannableString>();

    private final HashMap<String, SoftReference<Bitmap>> emojiCache = new HashMap<String, SoftReference<Bitmap>>();

    private final HashMap<String, Integer> emojiKeyValue = new HashMap<String, Integer>();

    private static EmoticonUtil mEmojiUtils;

    public static EmoticonUtil getInstace() {
        if (mEmojiUtils == null) {
            mEmojiUtils = new EmoticonUtil();
        }
        return mEmojiUtils;
    }

    /**
     * @param emojiUnicode
     * @param c
     * @return
     */
    public static Integer getEmoticonResId(String emojiUnicode, Context c) {
        return c.getResources().getIdentifier("emoji_" + emojiUnicode,
                "drawable", c.getPackageName());
    }

    /**
     * @param emojiName
     * @return
     */
    public static String formatFaces(String emojiName) {
        StringBuffer sb = new StringBuffer();
        sb.append("<img src=\"emoji_");
        sb.append(emojiName);
        sb.append("\">");
        return sb.toString();
    }

    /**
     * @param c
     * @return
     */
    public static ImageGetter getImageGetter(final Context c) {
        return new ImageGetter() {

            public Drawable getDrawable(String source) {
                System.out.println("source =" + source);
                Integer resID = c.getResources().getIdentifier(source, "drawable", c.getPackageName());
                Drawable d = c.getResources().getDrawable(resID);
                d.setBounds(0, 0, 24, 24);
                return d;
            }
        };
    }

    public static void initEmoji() {
        // HashMap<String, ArrayList<String>> emoMap =
        // EmojiParser.getInstance().getEmoMap();
        /*String[] stringArray = ECApplication.getInstance()
				.getApplicationContext().getResources()
				.getStringArray(R.array.emoji_code_file);
		if (stringArray != null) {
			initEmojiIcons(stringArray);
		}*/

        // initEmojiIcons();

        Emojicon[] data = People.DATA;
        if (data == null) {
            return;
        }
        for (Emojicon emojicon : data) {
            if (emojicon.getEmoji() != null) {
                CCPEmoji emoji = new CCPEmoji();
                emoji.setEmojiName(emojicon.getEmoji());
                emoji.setEmojiDesc(emojicon.getEmoji());
                int unicode = Character.codePointAt(emojicon.getEmoji(), 0);
                if (unicode > 0xff) {
                    emoji.setId(EmojiconHandler.getEmojiResource(AppMain.getInstance().getApplicationContext(), unicode));
                    emojis.add(emoji);
                }
            }
        }
    }

    private static ArrayList<CCPEmoji> emojis = new ArrayList<CCPEmoji>();

    private static HashMap<String, String> emojiMap = new HashMap<String, String>();

    public boolean emojiFilter(String filter) {
        if (TextUtils.isEmpty(filter)) {
            return false;
        }

        if ("emoji_custom_bg".equals(filter)
                || "emoji_del_selector".equals(filter)
                || "emoji_icon_selector".equals(filter)
                || "emoji_item_selector".equals(filter)
                || "emoji_press".equals(filter)) {
            return false;
        }

        return true;
    }

    public ArrayList<CCPEmoji> getEmojiCache() {

        return emojis;
    }

    /**
     * @return
     */
    public static int getEmojiSize() {
        return emojis.size();
    }

    /**
     *
     */
    public void release() {
        if (emojiCache != null) {
            Iterator<SoftReference<Bitmap>> iter = emojiCache.values()
                    .iterator();
            while (iter.hasNext()) {
                SoftReference<Bitmap> sr = iter.next();
                if (sr != null && sr.get() != null) {
                    sr.get().recycle();
                }
            }
            emojiCache.clear();
        }

        emojis.clear();
    }

    /**
     * The emo (local expression file name) into the corresponding Emoji
     * expression Unicode coding, each UTF-16 code takes two bytes
     *
     * @param emo The local expression of file name for example: emoji_e415.png
     * @return
     */
    public static String convertUnicode(String emo) {// e403
        // 1f615
        emo = emo.substring(emo.indexOf("_") + 1);
        if (emo.length() < 6) {
            String d = new String(Character.toChars(Integer.parseInt(emo, 16)));
            return d;
        }
        String[] emos = emo.split("_");

        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        for (int i = 0; i < char0.length; i++) {
            emoji[i] = char0[i];
        }
        for (int i = char0.length; i < emoji.length; i++) {
            emoji[i] = char1[i - char0.length];
        }
        String s = new String(emoji);
        LogUtil.d(emo + "1: " + s);
        return s;
    }

    /**
     * The Emoji expressions into Unicode code
     *
     * @param context
     * @param str
     * @param textSize
     * @param isReplaceLine
     * @return
     */
    public static SpannableString emoji2CharSequence(Context context, String str, int textSize, boolean isReplaceLine) {

        SpannableString object = null;
        String key = null;
        if (TextUtils.isEmpty(str)) {
            return new SpannableString("");
        }

        if (textSize == -1) {
            textSize = context.getResources().getDimensionPixelSize(R.dimen.ccp_button_text_size);
        } else if (textSize == -2) {
            textSize = context.getResources().getDimensionPixelSize(R.dimen.primary_text_size);
        }

        key = str + "@" + textSize;
        object = hashMap.get(key);
        if (object != null) {
            return object;
        }
        String source = str;
        if (isReplaceLine) {
            source = replaceLinebreak(str).toString();
        }

        source = matchEmojiUnicode(source);

        object = new SpannableString(source);

        boolean containsKeyEmoji = containsKeyEmoji(context, object, textSize);
        if (containsKeyEmoji) {
            hashMap.put(key, object);
        }

        return object;
    }


    /**
     * @param str
     * @return
     */
    private static CharSequence replaceLinebreak(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.toString().contains("\n")) {
            return str.toString().replace("\n", " ");
        }

        return str;
    }

    public static boolean containsKeyEmoji(Context context, SpannableString spannableString, int textSize) {

        if (TextUtils.isEmpty(spannableString)) {
            return false;
        }

        boolean isEmoji = false;
        char[] charArray = spannableString.toString().toCharArray();
        int i = 0;

        while (i < charArray.length) {

            int emojiId = getEmojiId(charArray[i]);

            if (emojiId != -1) {
                Drawable drawable = EmoticonUtil.getEmoticonDrawable(context, emojiId);

                if (drawable != null) {
                    drawable.setBounds(0, 0, (int) (1.3D * textSize), (int) (1.3D * textSize));
                    spannableString.setSpan(new ImageSpan(drawable, 0), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    isEmoji = true;
                }
            }
            i++;
        }

        return isEmoji;
    }

    /**
     * @param context
     * @param emojiId
     * @return
     */
    private static Drawable getEmoticonDrawable(Context context, int emojiId) {

        Drawable drawable = null;
        if (context == null || emojiId == -1) {
            return drawable;
        }

        int identifier = context.getResources().getIdentifier(
                "emoji_" + emojiId, "drawable", context.getPackageName());

        if (identifier != 0) {
            drawable = context.getResources().getDrawable(identifier);
        }
        return drawable;
    }

    /**
     * Replace the not support emoji.
     *
     * @param str
     * @return
     */
    public static String matchEmojiUnicode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] charArray = str.toCharArray();
        try {
            for (int i = 0; i < charArray.length - 1; i++) {
                int _index = charArray[i];
                int _index_inc = charArray[i + 1];

                if (_index == 55356) {
                    if ((_index_inc < 56324) || (_index_inc > 57320)) {
                        continue;
                    }
                    charArray[i] = '.';
                    charArray[(i + 1)] = '.';
                    continue;
                }

                if ((_index != 55357) || (_index_inc < 56343) || (_index_inc > 57024)) {
                    continue;
                }

                charArray[i] = '.';
                charArray[(i + 1)] = '.';
            }
        } catch (Exception e) {
        }

        return new String(charArray);
    }

    /**
     * @param charStr
     * @return
     */
    private static int getEmojiId(char charStr) {
        int i = -1;
        if ((charStr < 57345) || (charStr > 58679)) {
            return i;
        }
        if ((charStr >= 57345) && (charStr <= 57434)) {
            i = charStr - 57345;
        } else if ((charStr >= 57601) && (charStr <= 57690)) {
            i = charStr + 'Z' - 57601;
        } else if ((charStr >= 57857) && (charStr <= 57939)) {
            i = charStr + '´' - 57857;
        } else if ((charStr >= 58113) && (charStr <= 58189)) {
            i = charStr + 'ć' - 58113;
        } else if ((charStr >= 58369) && (charStr <= 58444)) {
            i = charStr + 'Ŕ' - 58369;
        } else if ((charStr >= 58625) && (charStr <= 58679)) {
            i = charStr + 'Ơ' - 58625;
        }
        return i;
    }

    public static void main(String[] args) {
        String convertUnicode = convertUnicode("e415");
        char[] charArray = convertUnicode.toCharArray();
        int emojiId = getEmojiId(charArray[0]);
        System.err.println(emojiId);
    }


    /**
     *
     */
    public static SpannableString getTextFormat(Context context, String format, int paddingSize) {
        if (TextUtils.isEmpty(format)) {
            return new SpannableString("");
        }
        String strValue = replaceLinebreak(format).toString();
        switch (paddingSize) {
            case -1:
                paddingSize = context.getResources().getDimensionPixelSize(R.dimen.ccp_button_text_size);
                break;
            case -2:
                paddingSize = context.getResources().getDimensionPixelSize(R.dimen.primary_text_size);
                break;
            default:
                break;
        }
        String key = strValue + "@" + paddingSize;
        String source = matchEmojiUnicode(strValue);
        SpannableString object = hashMap.get(key);
        if (object == null) {
            object = new SpannableString(source);
        }
        boolean containsKeyEmoji = containsKeyEmoji(context, object, paddingSize);
        if (containsKeyEmoji) {
            hashMap.put(key, object);
        }
        return object;

    }
}
