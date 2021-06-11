package com.hyw.webSite.dao;

import lombok.Data;

@Data
public class WebElement {
    private int webElementId;
    private String function; // 所属功能
    private String seq; // 序号，显示顺序
    private String id; // 唯一标识
    private String area; // 区域 input/output/modal
    private String window; // 所属窗口
    private String type; // 类型 input/dropDown/button
    private String prompt; // 显示名称
    private String data; // 默认值
    private String event; // 可改变的属性类型
    private String attr; // 可改变的属性类型
}
