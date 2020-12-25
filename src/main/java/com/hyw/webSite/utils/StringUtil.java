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

    public static String String2JsonString(String s)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.toCharArray()[i];
            switch (c) {
                case '\"':
                    sb.append("\\\""); break;
                case '\\':
                    sb.append("\\\\"); break;
                case '/':
                    sb.append("\\/"); break;
                case '\b':
                    sb.append("\\b"); break;
                case '\f':
                    sb.append("\\f"); break;
                case '\n':
                    sb.append("\\n"); break;
                case '\r':
                    sb.append("\\r"); break;
                case '\t':
                    sb.append("\\t"); break;
                default:
                    if ((c >= 0 && c <= 31)||c == 127) {//在ASCⅡ码中，第0～31号及第127号(共33个)是控制字符或通讯专用字符
                    }
                    else {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }
}
