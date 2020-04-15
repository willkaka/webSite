package com.hyw.webSite.funbean;

import com.hyw.webSite.web.dto.RequestDto;

import java.util.Map;

public interface WebDataReqFun {

    Map<String,Object> execute(RequestDto requestDto);
}
