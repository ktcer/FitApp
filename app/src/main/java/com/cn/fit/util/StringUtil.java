package com.cn.fit.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;

import com.cn.fit.model.family.BeanFamilyMemberInfo;

public class StringUtil {
    public static boolean isItEquals(ArrayList list, String str) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i).toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * **手机号匹正则表达式
     *
     * @param input
     * @return
     */
    public static boolean isPhone(String input) {
        String reg = "\\b(1)[358][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\\b";
        Pattern pattern = Pattern.compile(reg);
        boolean tf = pattern.matcher(input).matches();
        return tf;
    }

    /**
     * **密码匹正则表达式
     *
     * @param input
     * @return
     */
    public static boolean isPassword(String input) {
        String reg = "^[A-Za-z0-9]{6,29}$";
        Pattern pattern = Pattern.compile(reg);
        boolean tf = pattern.matcher(input).matches();
        return tf;
    }

    /**
     * **匹配非表情符号的正则表达式 不能输入表情
     *
     * @param input
     * @return
     */
    public static boolean isText(String input) {
        String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]|[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}\n｛｝【】［］‘；：”“’。，、？]){0,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
        Pattern pattern = Pattern.compile(reg);
        boolean tf = pattern.matcher(input).matches();
        return tf;
    }

    /**
     * **此正则表达式将上面二者结合起来进行判断，中文、大小写字母和数字，{1，10}字符的长度为2-10**
     *
     * @param input
     * @return
     */
    public static boolean isName(String input) {
        /** 此正则表达式将上面二者结合起来进行判断，中文、大小写字母和数字，{1，10}字符的长度为2-10 **/
        String all = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{1,15}$";
        Pattern pattern = Pattern.compile(all);
        boolean tf = pattern.matcher(input).matches();
        return tf;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || input.equals("null"))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }

        return true;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断数组是否包含某个字符串
     *
     * @param arry
     * @param str
     * @return
     */
    public static boolean containsLikeStr(String[] arry, String str) {
        for (String string : arry) {
            if (string.contains(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数组是否包含某个字符串
     *
     * @param arry
     * @param str
     * @return
     */
    public static boolean containsStr(String[] arry, String str) {
        for (String string : arry) {
            if (string.equals(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Unicode转汉字
     *
     * @param data
     * @return
     */
    public static String convertUnicode(String data) {
        try {
            return new String(data.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(List list) {

        if (list == null || list.size() == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean containsLikeStr(List<String> list, String str) {
        for (String string : list) {
            if (string.equals(str.trim())) {
                return true;
            }
        }
        return false;
    }

    public static String getHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        String regex = "^(https?)://[-a-zA-Z0-9.:]+[-a-zA-Z0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    public static String displayName(BeanFamilyMemberInfo bean) {
        if (bean.getRemarkName() != null) {
            if (!bean.getRemarkName().equals("")) {
                return bean.getRemarkName();
            }
        }
        if (bean.getMemberName() != null) {
            if (!bean.getMemberName().equals("")) {
                return bean.getMemberName();
            }
        }
        if (bean.getMemberLoginName() != null) {
            if (!bean.getMemberLoginName().equals("")) {
                return bean.getMemberLoginName();
            }
        }
        return "";
    }

    /**
     * 用于textview图文混排
     */
    public static ImageGetter getImageGetterInstance(final Context mContext) {
        ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int fontH = (int) (16 * 1.5);
                int id = Integer.parseInt(source);
                Drawable d = mContext.getResources().getDrawable(id);
                int height = fontH;
                int width = (int) ((float) d.getIntrinsicWidth() / (float) d
                        .getIntrinsicHeight()) * fontH;
                if (width == 0) {
                    width = d.getIntrinsicWidth();
                }
                d.setBounds(0, 0, width, height);
                return d;
            }
        };
        return imgGetter;
    }
}
