package com.hyw.webSite.web.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors( chain = true )
public class WebValueDto {

    private String curMenu;//当前的菜单

    private Map<String,Object> webInputValueMap;//页面输入值
}


