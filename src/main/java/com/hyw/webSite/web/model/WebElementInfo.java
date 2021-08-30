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
@TableName("web_element_info")
public class WebElementInfo {

    private static final long serialVersionUID = 1L;

    /**
     * null
     */
    @TableId("web_element_info_id")
    private Integer webElementInfoId;

    /**
     * null
     */
    @TableField("menu")
    private String menu;

    /**
     * null
     */
    @TableField("element_seq")
    private Integer elementSeq;

    /**
     * null
     */
    @TableField("element")
    private String element;

    /**
     * null
     */
    @TableField("area")
    private String area;

    /**
     * null
     */
    @TableField("sub_area")
    private String subArea;

    /**
     * null
     */
    @TableField("element_type")
    private String elementType;

    /**
     * null
     */
    @TableField("element_desc")
    private String elementDesc;

}
