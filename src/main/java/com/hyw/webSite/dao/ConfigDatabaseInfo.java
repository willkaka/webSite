package com.hyw.webSite.dao;

//import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//@TableName("config_database_info")
public class ConfigDatabaseInfo {
    private int configDatabaseInfoId;
    private String databaseName;
    private String databaseType;
    private String databaseDriver;
    private String databaseAddr;
    private String databaseAttr;
    private String databaseLabel;
    private String loginName;
    private String loginPassword;
}
