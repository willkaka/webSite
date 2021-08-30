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
@TableName("web_menu_group_info")
public class WebMenuGroupInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @TableId("web_menu_group_info_id")
    private Integer webMenuGroupInfoId;

    /**
     * null
     */
    @TableField("menu_group")
    private String menuGroup;

    /**
     * null
     */
    @TableField("group_desc")
    private String groupDesc;

}
