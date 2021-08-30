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
@TableName("web_trigger_info")
public class WebTriggerInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @TableId("web_trigger_info_id")
    private Integer webTriggerInfoId;

    /**
     * null
     */
    @TableField("source_menu")
    private String sourceMenu;

    /**
     * null
     */
    @TableField("source_element")
    private String sourceElement;

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
