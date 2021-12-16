package com.hyw.webSite.web.dto;

import com.hyw.webSite.model.FieldAttr;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors( chain = true )
public class WebTableInfo{
    private boolean isWithPage;//是否分页
    private int totalCount;//总记录数
    private int pageNow;//当前页码
    private int pageSize;//每页记录数
    private List<Map<String, FieldAttr>> recordList;//页面表格显示的数据记录
    private List<WebElementDto> formatInfoList; //保存表格记录中要
}
