package com.hyw.webSite.utils.javaParse.dto;

import lombok.Data;

import java.util.List;

@Data
public class JavaClassFieldInfo {
    private String argString;
    private String fieldName;
    private String fieldType;
    private String fieldTypeFullName;
    private List<JavaClassTypeInfo> subTypeList;
    private String fieldAttr;// private/public static final
    private String fieldDesc;
    private String fieldInit;
    private String fieldValue;
}
