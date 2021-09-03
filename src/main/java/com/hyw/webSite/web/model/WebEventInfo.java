package com.hyw.webSite.web.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 *null
 */
@Data
@Accessors(chain = true)
@TableName("web_event_info")
public class WebEventInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @TableId("web_event_info_id")
    private Integer webEventInfoId;

    /**
     * null
     */
    @TableField("menu")
    private String menu;

    /**
     * null
     */
    @TableField("element")
    private String element;

    /**
     * null
     */
    @TableField("event_type")
    private String eventType;

    /**
     * null
     */
    @TableField("request_type")
    private String requestType;

    /**
     * null
     */
    @TableField("request_no")
    private String requestNo;

    /**
     * null
     */
    @TableField("trigger_type")
    private String triggerType;

    /**
     * null
     */
    @TableField("trigger_element")
    private String triggerElement;

    /**
     * null
     */
    @TableField("trigger_element_type")
    private String triggerElementType;

    /**
     * null
     */
    @TableField("param")
    private String param;

}