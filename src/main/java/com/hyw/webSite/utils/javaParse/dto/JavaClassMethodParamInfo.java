package com.hyw.webSite.utils.javaParse.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JavaClassMethodParamInfo{
    private String name;
    private String type;
    private String typeFullName;
    private List<JavaClassTypeInfo> subTypeList = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
}
