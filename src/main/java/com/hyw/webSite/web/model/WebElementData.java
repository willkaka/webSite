package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_element_data
public class WebElementData {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_element_data_id
    private Integer webElementDataId;

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
    //data_type
    private String dataType;

    /**
     * null
     */
    //data_attr
    private String dataAttr;

    /**
     * null
     */
    //express
    private String express;

}