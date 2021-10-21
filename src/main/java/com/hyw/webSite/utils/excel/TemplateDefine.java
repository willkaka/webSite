package com.hyw.webSite.utils.excel;

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
//@TableName("template_define
public class TemplateDefine {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    //template_define_id
    private Integer templateDefineId;

    /**
     * 模板名称
     */
    //template_name
    private String templateName;

    /**
     * 模板描述
     */
    //template_desc
    private String templateDesc;

    /**
     * 定义类型
     */
    //def_type
    private String defType;

    /**
     * sheet号
     */
    //sheet_no
    private Integer sheetNo;

    /**
     * 行号
     */
    //pos_row
    private Integer posRow;

    /**
     * 列号
     */
    //pos_col
    private Integer posCol;

    /**
     * 字段
     */
    //field_name
    private String fieldName;

    /**
     * 字段描述
     */
    //field_desc
    private String fieldDesc;

    /**
     * 字段类型
     */
    //field_type
    private String fieldType;

    /**
     * 脱敏方式
     */
    //data_masking
    private String dataMasking;
}
