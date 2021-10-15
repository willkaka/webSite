package com.hyw.webSite.web.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WebDivDto {
    private int webElementId;
    private String id; // 唯一标识
    private String seq; // 序号，显示顺序
    private String func; // 所属功能
    private String area; // 区域 input/output/modal
    private String window; // 所属窗口
    private String type; // 类型 input/dropDown/button
    private String prompt; // 显示名称
    private List<WebElementDto> subElements; //子数据项，例如：子菜单等

}
