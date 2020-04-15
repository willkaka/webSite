package com.hyw.webSite.utils;

import org.springframework.lang.Nullable;

import java.util.*;

public class CollectionUtil {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !(collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !(map == null || map.isEmpty());
    }


    /**
     * 获取map中第一个数据值
     *
     * @param map 数据源
     * @return 第一个数据值
     */
    public static String getMapFirstOrNull(Map<String, String> map) {
        String obj = null;
        if(null == map) return obj;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getValue();
            break;
        }
        return obj;
    }

    public static void sort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null || o2 == null) {
                    return -1;
                }
                if (o1.length() > o2.length()) {
                    return 1;
                }
                if (o1.length() < o2.length()) {
                    return -1;
                }
                if (o1.compareTo(o2) > 0) {
                    return 1;
                }
                if (o1.compareTo(o2) < 0) {
                    return -1;
                }
                if (o1.compareTo(o2) == 0) {
                    return 0;
                }
                return 0;
            }
        });
    }
}
