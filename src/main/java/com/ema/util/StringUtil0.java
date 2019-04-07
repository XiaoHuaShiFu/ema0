package com.ema.util;

import java.util.Arrays;
import java.util.List;

/**
 * 描述:把字符串列表/数组转换成字符串
 *      或者解析字符串成字符串列表/数组
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 14:08
 */
public class StringUtil0 {

    /**
     * 把字符串列表转换成字符串，通过,分隔
     *
     * @param stringList 字符串列表
     * @return 字符串
     */
    public static String listToString(List<String> stringList) {
        String s = null;
        for (String s0 : stringList) {
            if (s == null) {
                s = "" + s0;
            } else {
                s = s +  ","  + s0;
            }
        }
        return s;
    }

    /**
     * 把字符串数组转换成字符串，通过,分隔
     *
     * @param strings 字符串数组
     * @return 字符串
     */
    public static String arrayToString(String[] strings) {
        String s = null;
        for (String s0 : strings) {
            if (s == null) {
                s = "" + s0;
            } else {
                s = s +  ","  + s0;
            }
        }
        return s;
    }

    /**
     * 字符串转字符串列表
     *
     * @param string 字符串
     * @return 字符串列表
     */
    public static List<String> stringToList(String string) {
        return Arrays.asList(stringToArray(string));
    }

    /**
     * 字符串转字符串数组
     *
     * @param string 字符串
     * @return 字符串数组
     */
    public static String[] stringToArray(String string) {
        return string.split(",");
    }

}
