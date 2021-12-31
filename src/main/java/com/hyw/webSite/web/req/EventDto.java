package com.hyw.webSite.web.req;

import com.hyw.webSite.model.FieldAttr;
import lombok.Data;

import java.util.Map;

/**
 * 事件信息
 *
 * 该数据会在触发事件后的请求中带回
 */
@Data
public class EventDto {
    // {"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName"}]}
    private String eventType; // click/change/....
    private String type; // menuReq/buttonReq
    private String id; // MR001/BR001
    private String sourceMenu;
    private String sourceArea;
    private String sourceElement;

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
    private Map<String,Object> triggerParamMap; //放置参数
}
