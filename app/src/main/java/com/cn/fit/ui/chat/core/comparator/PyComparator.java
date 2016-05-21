package com.cn.fit.ui.chat.core.comparator;

/**
 * com.cn.aihu.ui.chat.core.comparator in ECDemo_Android
 * Created by Jorstin on 2015/3/21.
 */

import java.io.Serializable;
import java.util.Comparator;

import com.cn.fit.ui.chat.ui.contact.ECContacts;

/**
 * 用于按拼音排序的比较器
 *
 * @author zhangyp
 */
public class PyComparator implements Comparator<ECContacts>, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4812257326087439258L;

    @Override
    public int compare(ECContacts contact1, ECContacts contact2) {
        String name1 = contact1.getQpNameStr();
        String name2 = contact2.getQpNameStr();
        if ((name1 == null || name1.length() == 0) && (name2 == null || name2.length() == 0)) {
            return 0;
        } else if (name1 == null || name1.length() == 0) {
            return 1;
        } else if (name2 == null || name2.length() == 0) {
            return -1;
        } else {
            if (compare(contact1.getQpName()[0].substring(0, 1), contact2.getQpName()[0].substring(0, 1)) != 0) {//如果第一个字的第一个字母不相等
                return compare(contact1.getQpName()[0].substring(0, 1), contact2.getQpName()[0].substring(0, 1)); //按第一个字的第一个字母排序
            } else {
                boolean firstIsChar = false;
                boolean secondIsChar = false;
                if (('a' <= contact1.getNickname().charAt(0) && contact1.getNickname().charAt(0) <= 'z') || ('A' <= contact1.getNickname().charAt(0) && contact1.getNickname().charAt(0) <= 'Z')) {
                    firstIsChar = true;
                }
                if (('a' <= contact2.getNickname().charAt(0) && contact2.getNickname().charAt(0) <= 'z') || ('A' <= contact2.getNickname().charAt(0) && contact2.getNickname().charAt(0) <= 'Z')) {
                    secondIsChar = true;
                }
                if (firstIsChar && secondIsChar) { //如果都是英文，比较全拼
                    return compare(name1, name2);
                } else if (firstIsChar && !secondIsChar) { //一方是英文
                    return -1;
                } else if (!firstIsChar && secondIsChar) { //一方是英文
                    return 1;
                } else {
                    if (compare(contact1.getQpName()[0], contact2.getQpName()[0]) == 0) { //都是汉字
                        if (compare(Integer.toHexString(contact1.getNickname().charAt(0)), Integer.toHexString(contact2.getNickname().charAt(0))) == 0) {
                            return compare(name1, name2);
                        } else {
                            return compare(Integer.toHexString(contact1.getNickname().charAt(0)), Integer.toHexString(contact2.getNickname().charAt(0)));
                        }
                    } else {
                        return compare(contact1.getQpName()[0], contact2.getQpName()[0]);
                    }
                }
            }
        }
    }

    /**
     * 重写的String比较方法，设置大小写排序一样，符号数字在字母后边
     *
     * @param name1
     * @param name2
     * @return
     */
    private int compare(String name1, String name2) {
        char[] value1 = name1.toCharArray();
        char[] value2 = name2.toCharArray();
        int len1 = value1.length;
        int len2 = value2.length;
        int n = Math.min(len1, len2);
        char v1[] = value1;
        char v2[] = value2;
        int i = 0;
        int j = 0;
        if (i == j) {
            int k = i;
            int lim = n + i;
            while (k < lim) {
                char c1 = changeASCII(v1[k]);
                char c2 = changeASCII(v2[k]);
                if (c1 != c2) {
                    return c1 - c2;
                }
                k++;
            }
        } else {
            while (n-- != 0) {
                char c1 = changeASCII(v1[i++]);
                char c2 = changeASCII(v2[j++]);
                if (c1 != c2) {
                    return c1 - c2;
                }
            }
        }
        return len1 - len2;
    }

    /**
     * 改变ascii的顺序，设置大小写排序一样，符号数字在字母后边
     *
     * @param c
     * @return
     */
    private char changeASCII(char c) {
        if (65 <= c && c <= 90) {
            c = (char) (c - 33);
        } else if (97 <= c && c <= 122) {
            c = (char) (c - 65);
        } else if (32 <= c && c <= 64) {
            c = (char) (c + 26);
        }
        return c;
    }
}

