package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_menu_info
public class WebMenuInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_menu_info_id
    private Integer webMenuInfoId;

    /**
     * null
     */
    //menu_group
    private String menuGroup;

    /**
     * null
     */
    //menu_seq
    private Integer menuSeq;

    /**
     * null
     */
    //menu
    private String menu;

    /**
     * null
     */
    //menu_desc
    private String menuDesc;

}
