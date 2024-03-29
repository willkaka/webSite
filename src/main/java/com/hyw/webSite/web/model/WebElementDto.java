package com.hyw.webSite.web.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WebElementDto {
    private int webElementId;
    private String id; // 唯一标识
    private String seq; // 序号，显示顺序
    private String func; // 所属功能
    private String area; // 区域 input/output/modal
    private String window; // 所属窗口
    private String type; // 类型 input/dropDown/button
    private String prompt; // 显示名称
    private String defaultValue; // 默认值
    private String chgType; // 可改变的属性类型
    private Map<String,String> dataMap;
    private Map<String,String> attrMap; // 属性 eg. class="xxx",width=100px,height=200px
    private List<EventInfo> eventInfoList; // 事件类型，事件处理ID；数据库按："onclick:clickEvent001#onload:clickEvent002"
    private List<WebElementDto> subElements; //子数据项，例如：子菜单等

}
