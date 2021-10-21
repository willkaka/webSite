package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_event_info
public class WebEventInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_event_info_id
    private Integer webEventInfoId;

    /**
     * null
     */
    //menu
    private String menu;

    /**
     * null
     */
    //element
    private String element;

    /**
     * null
     */
    //event_type
    private String eventType;

    /**
     * null
     */
    //request_type
    private String requestType;

    /**
     * null
     */
    //request_no
    private String requestNo;

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