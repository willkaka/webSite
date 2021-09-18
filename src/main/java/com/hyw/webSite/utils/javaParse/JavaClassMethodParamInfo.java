package com.hyw.webSite.utils.javaParse;

import lombok.Data;

import java.util.List;

@Data
public class JavaClassMethodParamInfo{
    private String name;
    private String type;
    private String typeFullName;
    private List<String> annotations;
}
