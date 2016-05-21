package com.cn.fit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式，匹配用户名
 * （只有字母、数字和下划线且不能以下划线开头和结尾）
 *
 * @author kuangtiecheng
 */
public class RegexUser implements Regex {

    /**
     * 只有字母、数字和下划线且不能以下划线开头和结尾
     */
    private static final String ExUsername = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$";

    /**
     * 用于剔除非法字符
     */
    private static final String ExReplay = "[^a-zA-Z0-9_]";

    public boolean judge(String value) {
        Pattern pattern = Pattern.compile(ExUsername);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    public String correct(String value) {
        value = value.replaceAll(ExReplay, "");
        return value;
    }

    public String getRule() {
        return "由字母、数字和下划线组成，且不能以下划线开头和结尾";
    }
}
