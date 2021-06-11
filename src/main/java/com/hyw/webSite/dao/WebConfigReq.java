package com.hyw.webSite.dao;

import lombok.Data;

@Data
public class WebConfigReq {
    private int webConfigReqId;
    private String menu;
    private String reqType;
    private String reqName;
    private String beanName;
}
