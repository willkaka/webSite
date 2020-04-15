package com.hyw.webSite.funbean;

import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;

public interface RequestFun {

    ReturnDto execute(RequestDto requestDto);
}
