package com.hyw.webSite.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("web_menu_group_info")
public class WebMenuGroupInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    //web_menu_group_info_id
    private Integer webMenuGroupInfoId;

    /**
     * null
     */
    //menu_group
    private String menuGroup;

    /**
     * null
     */
    //group_desc
    private String groupDesc;

}
