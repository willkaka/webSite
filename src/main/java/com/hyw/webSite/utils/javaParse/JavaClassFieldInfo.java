package com.hyw.webSite.utils.javaParse;

import lombok.Data;

@Data
public class JavaClassFieldInfo {
    private String argString;
    private String fieldName;
    private String fieldType;
    private String fieldTypeFullName;
    private String fieldAttr;// private/public static final
    private String fieldDesc;
    private String fieldInit;
}
