package com.hyw.webSite.web.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ReturnDto {
    //报文返回信息
    private String rtnCode = "0000";
    private String rtnMsg = "Success";
    private Map<String,Object> titleInfoMap = new HashMap<>();
    private Map<String,Object> menuMap = new HashMap<>();
    private Map<String,Object> navMap = new HashMap<>();
    private Map<String,Object> formatInfoMap = new HashMap<>(); //菜单输入输出格式信息
    private Map<String,Object> inputMap = new HashMap<>();
    private Map<String,Object> outputMap = new HashMap<>();
    private Map<String,Object> changedMap = new HashMap<>(); //key:element id,value: info
}
