package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_element_info
public class WebElementInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_element_info_id
    private Integer webElementInfoId;

    /**
     * null
     */
    //menu
    private String menu;

    /**
     * null
     */
    //element_seq
    private Integer elementSeq;

    /**
     * null
     */
    //element
    private String element;

    /**
     * null
     */
    //area
    private String area;

    /**
     * null
     */
    //sub_area
    private String subArea;

    /**
     * null
     */
    //element_type
    private String elementType;

    /**
     * null
     */
    //element_desc
    private String elementDesc;

}
