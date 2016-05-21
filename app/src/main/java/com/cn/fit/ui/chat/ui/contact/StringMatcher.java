package com.cn.fit.ui.chat.ui.contact;

/**
 * 字符串匹配工具类
 * com.cn.aihu.ui.chat.ui.contact in ECDemo_Android
 * Created by Jorstin on 2015/3/21.
 */
public class StringMatcher {
    // 这些变量是韩文，小巫也不知道是什么意思，有谁懂的马上联系我啊
    private final static char KOREAN_UNICODE_START = '가'; // 韩文字符编码开始？
    private final static char KOREAN_UNICODE_END = '힣';   // 韩文字符编码结束？
    private final static char KOREAN_UNIT = '까' - '가';    // 不知道是啥？
    // 韩文的一些字符初始化
    private final static char[] KOREAN_INITIAL = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
            'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ',
            'ㅎ'};


    /**
     * 字符匹配
     *
     * @param value   需要keyword匹配的字符串
     * @param keyword #ABCDEFGHIJKLMNOPQRSTUVWXYZ中的一个
     * @return
     */
    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null)
            return false;
        if (keyword.length() > value.length())
            return false;

        int i = 0, j = 0;
        do {
            // 如果是韩文字符并且在韩文初始数组里面
            if (isKorean(value.charAt(i)) && isInitialSound(keyword.charAt(j))) {
                if (keyword.charAt(j) == getInitialSound(value.charAt(i))) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            } else {
                // 逐个字符匹配
                if (keyword.charAt(j) == value.charAt(i)) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            }
        } while (i < value.length() && j < keyword.length());
        // 如果最后j等于keyword的长度说明匹配成功
        return (j == keyword.length()) ? true : false;
    }

    // 判断字符是否在韩文字符编码范围内
    private static boolean isKorean(char c) {
        if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
            return true;
        return false;
    }

    // 判断是否在韩文字符里面
    private static boolean isInitialSound(char c) {
        for (char i : KOREAN_INITIAL) {
            if (c == i)
                return true;
        }
        return false;
    }

    // 获得韩文初始化字符数组里面的一个字符
    private static char getInitialSound(char c) {
        return KOREAN_INITIAL[(c - KOREAN_UNICODE_START) / KOREAN_UNIT];
    }
}
