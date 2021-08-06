package com.hyw.webSite.utils.excel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模板定义
 *
 * @author jobob
 * @since 2021-04-23
 */
@Data
@Accessors(chain = true)
@TableName("template_define")
public class TemplateDefine {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("template_define_id")
    private Integer templateDefineId;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 模板描述
     */
    @TableField("template_desc")
    private String templateDesc;

    /**
     * 定义类型
     */
    @TableField("def_type")
    private String defType;

    /**
     * sheet号
     */
    @TableField("sheet_no")
    private Integer sheetNo;

    /**
     * 行号
     */
    @TableField("pos_row")
    private Integer posRow;

    /**
     * 列号
     */
    @TableField("pos_col")
    private Integer posCol;

    /**
     * 字段
     */
    @TableField("field_name")
    private String fieldName;

    /**
     * 字段描述
     */
    @TableField("field_desc")
    private String fieldDesc;

    /**
     * 字段类型
     */
    @TableField("field_type")
    private String fieldType;

    /**
     * 脱敏方式
     */
    @TableField("data_masking")
    private String dataMasking;
}
