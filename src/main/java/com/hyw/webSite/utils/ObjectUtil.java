package com.hyw.webSite.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class ObjectUtil {

    /**
     * 判断是否为字符串
     * @param object 输入对象
     * @return 是字符串为true
     */
    public static boolean isString(Object object){
        return object instanceof String;
    }

    public static boolean isBigDecimal(Object object){
        return object instanceof BigDecimal;
    }

    public static boolean isLocalDate(Object object){
        return object instanceof LocalDate;
    }

    public static boolean isInteger(Object object){
        return object instanceof Integer;
    }

    public static boolean isDate(Object object){
        return object instanceof Date;
    }
}
