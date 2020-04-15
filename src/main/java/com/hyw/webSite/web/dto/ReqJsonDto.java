package com.hyw.webSite.web.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReqJsonDto {
    //报文头部
    private String reqId;//区分请求，避免重复处理
    private String reqMapping;//对应Controller的Mapping
    private String reqName;//请求名称
    private String reqType;//请求类型
    private String curMenu;//当前的菜单

    //报文内容
    private Map<String,Object> reqParm;

    //报文返回信息
    private Map<String,Object> rtnMap;
    private String rtnCode;
    private String rtnMsg;
    //private List<Map<String,Object>> rtnData;
}
