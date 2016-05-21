package com.cn.fit.util;

/**
 * 正则表达式接口
 *
 * @author kuangtiecheng
 */
public interface Regex {

    /**
     * 正则表达式，错误字符
     */
    public static final String ExErrorChar = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&;*——+|{}【】‘；：”“’。，、？]";

    /**
     * 判断是否合法
     *
     * @param value String 待验证的字符串
     * @return boolean 合法返回 TRUE
     */
    public boolean judge(String value);

    /**
     * 剔除非法字符
     *
     * @param value String 要替换的字符串
     * @return String 替换后的字符串
     */
    public String correct(String value);

    /**
     * 规则描述
     *
     * @return String
     */
    public String getRule();
}
