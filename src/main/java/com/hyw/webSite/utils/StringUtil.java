package com.hyw.webSite.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class StringUtil {

    public static boolean isBlank(String s){
        return null==s || "".equals(s);
    }

    public static boolean isNotBlank(String s){
        return !(null==s || "".equals(s));
    }

}
