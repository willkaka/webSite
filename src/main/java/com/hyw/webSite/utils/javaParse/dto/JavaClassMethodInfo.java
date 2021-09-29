package com.hyw.webSite.utils.javaParse.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JavaClassMethodInfo{
    private String name; // 方法名
    private List<JavaClassMethodParamInfo> inputParamList = new ArrayList<>(); // 方法参数
    private List<JavaClassMethodParamInfo> outputParamList = new ArrayList<>(); // 方法参数
    private List<String> annotationList = new ArrayList<>(); // 方法注解
    private String comment; // 方法注释
    private List<String> variableList = new ArrayList<>(); //方法内定义的变量
}
