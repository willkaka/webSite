package com.hyw.webSite.utils.javaParse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JavaClassInfo {
    private String packageStr; //
    private List<String> importList = new ArrayList<>(); // 引入包
    private String name; // 类名
    private String fullName; // 包含包路径类名
    private List<String> argList = new ArrayList<>(); // 类变量
    private List<String> annotationList = new ArrayList<>(); // 注解
    private String comment; // 类注释
    private List<JavaClassFieldInfo> javaClassFieldInfoList = new ArrayList<>(); // 类成员变量

    private List<JavaClassMethodInfo> methodInfoList = new ArrayList<>();
}

