package com.hyw.webSite.web.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors( chain = true )
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
    private Map<String,Object> webNextOpr = new HashMap<>(); //后台执行完请求后，前台的下一操作
}
