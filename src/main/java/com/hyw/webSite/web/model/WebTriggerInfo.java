package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_trigger_info
public class WebTriggerInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_trigger_info_id
    private Integer webTriggerInfoId;

    /**
     * null
     */
    //source_menu
    private String sourceMenu;

    /**
     * null
     */
    //source_element
    private String sourceElement;

    /**
     * null
     */
    //trigger_type
    private String triggerType;

    /**
     * null
     */
    //trigger_element
    private String triggerElement;

    /**
     * null
     */
    //trigger_element_type
    private String triggerElementType;

    /**
     * null
     */
    //param
    private String param;

}
