package com.cn.fit.ui.chat.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * com.cn.aihu.ui.chat.common.utils in ECDemo_Android
 * Created by Jorstin on 2015/3/30.
 */
public class CheckUtil {
    public static final String[] PHONE_PREFIX = new String[]{"130", "131",
            "132", "133", "134", "135", "136", "137", "138", "139", "150",
            "151", "152", "153", "154", "155", "156", "157", "158", "159",
            "180", "181", "182", "183", "184", "185", "186", "187", "188",
            "189"};

    public static boolean checkLocation(String mdn) {
        return checkMDN(mdn, false);
    }

    public static boolean checkMDN(String mdn) {
        return checkMDN(mdn, true);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 检查手机号码合法性
     *
     * @param mdn
     * @return
     */
    public static boolean checkMDN(String mdn, boolean checkLen) {
        if (mdn == null || mdn.equals("")) {
            return false;
        }
        //modify by zhangyp 去掉号码前边的+86
        if (mdn.startsWith("+86")) {
            mdn = mdn.substring(3);
        }
        if (checkLen && mdn.length() != 11) {
            return false;
        }
        boolean flag = false;
        String p = mdn.length() > 3 ? mdn.substring(0, 3) : mdn;
        for (int i = 0; i < PHONE_PREFIX.length; i++) {
            if (p.equals(PHONE_PREFIX[i])) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return false;
        }
        return true;
    }

    public static final char[] INVALID_CH_CN = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '.', ',', ';', ':', '!', '@', '/', '(', ')', '[', ']', '{',
            '}', '|', '#', '$', '%', '^', '&', '<', '>', '?', '\'', '+',
            '-', '*', '\\', '\"'};

    public static boolean checkCN(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        char[] cArray = str.toCharArray();
        for (int i = 0; i < cArray.length; i++) {
            for (int j = 0; j < INVALID_CH_CN.length; j++) {
                if (cArray[i] == INVALID_CH_CN[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否为新版本, true  为有新版本， 否则；
     *
     * @param mdn
     * @return
     */
    public static boolean versionCompare(String oldversion, String newversion) {
        if (oldversion == null || newversion == null) {
            return false;
        }
        String[] oldstr = oldversion.split("\\.");
        String[] newstr = newversion.split("\\.");

        int[] oldint = new int[oldstr.length];
        int[] newint = new int[newstr.length];

        try {
            for (int i = 0; i < oldstr.length; i++) {
                oldint[i] = Integer.valueOf(oldstr[i]);
            }
            for (int i = 0; i < newstr.length; i++) {
                newint[i] = Integer.valueOf(newstr[i]);
            }
        } catch (Exception e) {
        }

        // 可以简化的逻辑
        int count = oldint.length > newint.length ? newint.length
                : oldint.length;
        for (int temp = 0; temp < count; temp++) {
            if (newint[temp] == oldint[temp]) {
                continue;
            } else if (newint[temp] > oldint[temp]) {
                return true;
            } else {
                return false;
            }
        }
        if (newint.length > oldint.length) {
            return true;
        }
        return false;
    }

    /**
     * 检测邮箱合法性
     *
     * @param email
     * @return
     */
    public static boolean checkEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0)) {
            return false;
        }
        String regEx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(email.trim().toLowerCase());

        return m.find();
    }

    private static final Pattern IPV4_PATTERN =
            Pattern.compile(
                    "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    static String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\:\\d{1,5}$";

    private static final Pattern IP_PORT = Pattern.compile(regex);

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    public static boolean validateIpPort(final String input) {
        return IP_PORT.matcher(input).matches();
    }


    /**
     * @param mobile
     * @return
     */
    public static String formatMobileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        return mobile.replaceAll("[\\.\\-\\ ]", "").trim();
    }
}
