package com.hyw.webSite.web.model;

import com.hyw.webSite.model.FieldAttr;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EventInfo {
    // {"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName"}]}
    private String event; // click
    private String type; // menuReq/buttonReq
    private String id; // MR001/BR001
    private String relEleId;
    private String relEleType;
    private String relEleChgType; //改变的类型：value-改变值
    private String selectedValue;
    private Map<String, FieldAttr> recordMap;
    private Map<String,Object> paramMap; //放置参数
}
