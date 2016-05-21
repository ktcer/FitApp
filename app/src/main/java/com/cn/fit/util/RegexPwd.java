package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式，匹配密码
 * （只有字母、数字）
 *
 * @author kuangtiecheng
 */
public class RegexPwd implements Regex {

    /**
     * 只有字母、数字
     */
    private static final String ExPwd = "[^a-zA-Z0-9]";

    @Override
    public boolean judge(String value) {
        Pattern pattern = Pattern.compile(ExPwd);
        Matcher matcher = pattern.matcher(value);
        return !matcher.find();
    }

    @Override
    public String correct(String value) {
        value = value.replaceAll(ExPwd, "");
        return value;
    }

    @Override
    public String getRule() {
        return "由字母和数字组成";
    }
}
