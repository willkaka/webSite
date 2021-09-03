package com.hyw.webSite.web.dto;

import com.hyw.webSite.web.model.EventInfo;
import lombok.Data;

import java.util.Map;

@Data
public class RequestDto {
    //报文头部
    private String reqId;//区分请求，避免重复处理
    private String reqMapping;//对应Controller的Mapping
    private String reqName;//请求名称
    private String reqType;//请求类型

    private String curMenu;//当前的菜单

    //报文内容
    private EventInfo eventInfo; //事件信息
    private Map<String,Object> reqParm;
}
