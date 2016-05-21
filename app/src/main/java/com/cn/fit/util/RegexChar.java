package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式，匹配激活码
 * （只有字母）
 *
 * @author kuangtiecheng
 */
public class RegexChar implements Regex {

    /**
     * 只有字母、数字
     */
    private static final String ExChar = "[^a-zA-Z]";

    @Override
    public boolean judge(String value) {
        Pattern pattern = Pattern.compile(ExChar);
        Matcher matcher = pattern.matcher(value);
        return !matcher.find();
    }

    @Override
    public String correct(String value) {
        value = value.replaceAll(ExChar, "");
        return value;
    }

    @Override
    public String getRule() {
        return "只能输入字母（不区分大小写）";
    }
}
