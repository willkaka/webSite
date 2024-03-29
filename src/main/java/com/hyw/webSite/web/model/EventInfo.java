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

    private String RelEleId;
    private String RelEleType;
    private String RelEleChgType;

    private String triggerType;
    private String triggerElement;
    private String triggerElementType; //改变的类型：value-改变值

    private String selectedValue;
    private boolean withPage; //是否为分页按钮的请求事件
    private int reqPage; //请求页码
    private Map<String, FieldAttr> recordMap;
    private Map<String,Object> paramMap; //放置参数
}
