package com.hyw.webSite.web.resp;

import com.hyw.webSite.web.dto.EventInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class NextOprDto {

    private List<EventInfo> eventInfoList;
}
