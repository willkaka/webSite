package com.hyw.webSite.utils;

public class StringUtil {

    public static boolean isBlank(String s){
        return null==s || "".equals(s);
    }

    public static boolean isNotBlank(String s){
        return !(null==s || "".equals(s));
    }

    /***
     * 下划线命名转为驼峰命名
     * @param para 下划线命名的字符串
     */
    public static String underlineToCamelCase(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /**
     * 功能：驼峰命名转下划线命名
     * 小写和大写紧挨一起的地方,加上分隔符,然后全部转小写
     */
    public static String camelCaseToUnderline(String c) {
        String separator = "_";
        c = c.replaceAll("([a-z])([A-Z])", "$1" + separator + "$2").toLowerCase();
        return c;
    }
}
