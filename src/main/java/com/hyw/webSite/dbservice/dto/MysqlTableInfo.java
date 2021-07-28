package com.hyw.webSite.dbservice.dto;

import com.baomidou.mybatisplus.annotation.TableField;

public class MysqlTableInfo {
    @TableField("TABLE_CATALOG")
    private String tableCatalog;

    @TableField("TABLE_SCHEMA")
    private String tableSchema;

    @TableField("TABLE_NAME")
    private String tableName;

    @TableField("TABLE_TYPE")
    private String tableType;

    @TableField("ENGINE")
    private String engine;

    @TableField("VERSION")
    private String version;

    @TableField("ROW_FORMAT")
    private String rowFormat;

    @TableField("TABLE_ROWS")
    private String tableRows;

    @TableField("AVG_ROW_LENGTH")
    private String avgRowLength;

    @TableField("DATA_LENGTH")
    private String dataLength;

    @TableField("MAX_DATA_LENGTH")
    private String maxDataLength;

    @TableField("INDEX_LENGTH")
    private String indexLength;

    @TableField("DATA_FREE")
    private String dataFree;

    @TableField("AUTO_INCREMENT")
    private String autoIncrement;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_TIME")
    private String updateTime;

    @TableField("CHECK_TIME")
    private String checkTime;

    @TableField("TABLE_COLLATION")
    private String tableCollation;

    @TableField("CHECKSUM")
    private String checkSum;

    @TableField("CREATE_OPTIONS")
    private String createOptions;

    @TableField("TABLE_COMMENT")
    private String tableComment;
}
