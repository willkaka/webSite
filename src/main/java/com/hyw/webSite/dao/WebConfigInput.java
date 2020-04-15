package com.hyw.webSite.dao;

import lombok.Data;

@Data
public class WebConfigInput {
    private String web;
    private int seq;
    private String inputId;
    private String inputName;
    private String inputClass;
    private String inputType;
    private String inputPrompt;
    private String inputRtnVar;
}
