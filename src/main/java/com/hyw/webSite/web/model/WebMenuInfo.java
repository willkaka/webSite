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
@TableName("web_menu_info")
public class WebMenuInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @TableId("web_menu_info_id")
    private Integer webMenuInfoId;

    /**
     * null
     */
    @TableField("menu_group")
    private String menuGroup;

    /**
     * null
     */
    @TableField("menu_seq")
    private Integer menuSeq;

    /**
     * null
     */
    @TableField("menu")
    private String menu;

    /**
     * null
     */
    @TableField("menu_desc")
    private String menuDesc;

}
