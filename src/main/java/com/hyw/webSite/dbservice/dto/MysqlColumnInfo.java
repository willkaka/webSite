package com.hyw.webSite.dbservice.dto;

import com.baomidou.mybatisplus.annotation.TableField;

public class MysqlColumnInfo {

    @TableField("TABLE_CATALOG")
    private String tableCatalog;

    @TableField("TABLE_SCHEMA")
    private String tableSchema;

    @TableField("TABLE_NAME")
    private String tableName;

    @TableField("COLUMN_NAME")
    private String columnName;

    @TableField("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @TableField("COLUMN_DEFAULT")
    private String columnDefault;

    @TableField("IS_NULLABLE")
    private String isNullable;

    @TableField("DATA_TYPE")
    private String dataType;

    @TableField("CHARACTER_MAXIMUM_LENGTH")
    private Integer characterMaximumLength;

    @TableField("CHARACTER_OCTET_LENGTH")
    private Integer characterOctetLength;

    @TableField("NUMERIC_PRECISION")
    private Integer numericPrecision;

    @TableField("NUMERIC_SCALE")
    private Integer numericScale;

    @TableField("DATETIME_PRECISION")
    private Integer datetimePrecision;

    @TableField("CHARACTER_SET_NAME")
    private String characterSetName;

    @TableField("COLLATION_NAME")
    private String collationName;

    @TableField("COLUMN_TYPE")
    private String columnType;

    @TableField("COLUMN_KEY")
    private String columnKey;

    @TableField("EXTRA")
    private String extra;

    @TableField("PRIVILEGES")
    private String privileges;

    @TableField("COLUMN_COMMENT")
    private String columnComment;

    @TableField("GENERATION_EXPRESSION")
    private String generationExpression;

    @TableField("SRS_ID")
    private String srsId;
}
