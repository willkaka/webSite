package com.hyw.webSite.web.req;

import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.WebElementDto;
import com.hyw.webSite.web.resp.NextOprDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors( chain = true )
public class PublicReq {
    //报文头部
    private String reqId;//区分请求，避免重复处理
    private String reqMapping;//对应Controller的Mapping
    private String reqName;//请求名称
    private String reqType;//请求类型
    private String curMenu;//当前的菜单

    //报文内容
    private EventInfo eventInfo; //事件信息
    private WebValueDto webValueDto; //页面当前输入的值
}


