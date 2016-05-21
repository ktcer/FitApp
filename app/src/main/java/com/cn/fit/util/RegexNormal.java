package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通常对输入框的处理，匹配汉字、字母、数字 足够了，其他字符不作为关键字查询，可以从结果中选择
 * 正则表达式，匹配汉字
 * （只有汉字、BASKaCRTPM（-）、数字）
 *
 * @author kuangtiecheng
 */
public class RegexNormal implements Regex {

    /**
     * 只有汉字、BASKaCRTPM、数字
     */
    private static final String ExCharacter = "[^A-Za-z0-9\u4e00-\u9fa5]";

    /**
     * 用于剔除非法字符
     */
    private static final String ExReplay = "[^A-Za-z0-9\u4e00-\u9fa5]";

    @Override
    public boolean judge(String value) {
        Pattern pattern = Pattern.compile(ExCharacter);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    @Override
    public String correct(String value) {
        value = value.replaceAll(ExReplay, "");
        return value;
    }

    @Override
    public String getRule() {
        return "只能输入汉字";
    }
}
