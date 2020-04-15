package com.hyw.webSite.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebInfoUtil {



    /**
     * 取导航路径
     * @return
     */
    public static List<String> getNavPathList(){
        List<String> navPathList = new ArrayList<>();
        navPathList.add("1");
        navPathList.add("2");
        navPathList.add("3");
        return navPathList;
    }

    /**
     * 取数据列名
     * @return
     */
    public static List<String> getTableColList(){
        List<String> tableColList = new ArrayList<>();
        tableColList.add("col1");
        tableColList.add("col2");
        tableColList.add("col3");
        return tableColList;
    }

    /**
     * 取数据表记录
     * @return
     */
    public static List<Map<String,Object>> getTableRecords(){
        List<Map<String,Object>> tableRecList = new ArrayList<>();
        Map<String,Object> rec1 = new HashMap<>();
        rec1.put("col1","11");
        rec1.put("col2","12");
        rec1.put("col3","13");

        Map<String,Object> rec2 = new HashMap<>();
        rec2.put("col1","21");
        rec2.put("col2","22");
        rec2.put("col3","23");

        Map<String,Object> rec3 = new HashMap<>();
        rec3.put("col1","31");
        rec3.put("col2","32");
        rec3.put("col3","33");

        tableRecList.add(rec1);
        tableRecList.add(rec2);
        tableRecList.add(rec3);
        return tableRecList;
    }
}
