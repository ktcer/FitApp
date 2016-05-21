package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式，匹配激活码
 * （只有数字）
 *
 * @author kuangtiecheng
 */
public class RegexNumber implements Regex {

    /**
     * 只有数字
     */
    private static final String ExNumber = "[^0-9]";

    @Override
    public boolean judge(String value) {
        Pattern pattern = Pattern.compile(ExNumber);
        Matcher matcher = pattern.matcher(value);
        return !matcher.find();
    }

    @Override
    public String correct(String value) {
        value = value.replaceAll(ExNumber, "");
        return value;
    }

    @Override
    public String getRule() {
        return "只能是数字";
    }
}
