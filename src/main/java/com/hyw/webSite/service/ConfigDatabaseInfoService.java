package com.hyw.webSite.service;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.dto.DynamicTableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConfigDatabaseInfoService {

    @Autowired
    private DynamicTableService dynamicTableService;


    /**
     * 取配置的所有数据库信息
     * @return List<ConfigDatabaseInfo>
     */
    public List<ConfigDatabaseInfo> getDatabaseConfigList(){
        List<ConfigDatabaseInfo> configDatabaseInfos = new ArrayList<>();
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName("config_database_info");
        dynamicTableDto.setSelectFields("database_name,database_type,database_driver,database_addr,database_attr,database_label,login_name,login_password ");
        List<Map<String,Object>> rtnListMap = dynamicTableService.selectAll(dynamicTableDto);
        for(Map<String,Object> rtnMap:rtnListMap){
            ConfigDatabaseInfo configDatabaseInfo = new ConfigDatabaseInfo();
            configDatabaseInfo.setDatabaseName((String) rtnMap.get("database_name"));
            configDatabaseInfo.setDatabaseType((String) rtnMap.get("database_type"));
            configDatabaseInfo.setDatabaseDriver((String) rtnMap.get("database_driver"));
            configDatabaseInfo.setDatabaseAddr((String) rtnMap.get("database_addr"));
            configDatabaseInfo.setDatabaseAttr((String) rtnMap.get("database_attr"));
            configDatabaseInfo.setDatabaseLabel((String) rtnMap.get("database_label"));
            configDatabaseInfo.setLoginName((String) rtnMap.get("login_name"));
            configDatabaseInfo.setLoginPassword((String) rtnMap.get("login_password"));

            configDatabaseInfos.add(configDatabaseInfo);
        }
        return configDatabaseInfos;
    }

    /**
     * 由数据库名称取数据库信息
     * @param databaseName 数据库名称
     * @return ConfigDatabaseInfo
     */
    public ConfigDatabaseInfo getDatabaseConfig(String databaseName){
        List<ConfigDatabaseInfo> configDatabaseInfos = new ArrayList<>();
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName("config_database_info");
        dynamicTableDto.setSelectFields("database_name,database_type,database_driver,database_addr,database_attr,database_label,login_name,login_password ");
        dynamicTableDto.setSelectWhere(" database_name = '"+databaseName+"' ");
        List<Map<String,Object>> rtnListMap = dynamicTableService.selectAll(dynamicTableDto);
        for(Map<String,Object> rtnMap:rtnListMap){
            ConfigDatabaseInfo configDatabaseInfo = new ConfigDatabaseInfo();
            configDatabaseInfo.setDatabaseName((String) rtnMap.get("database_name"));
            configDatabaseInfo.setDatabaseType((String) rtnMap.get("database_type"));
            configDatabaseInfo.setDatabaseDriver((String) rtnMap.get("database_driver"));
            configDatabaseInfo.setDatabaseAddr((String) rtnMap.get("database_addr"));
            configDatabaseInfo.setDatabaseAttr((String) rtnMap.get("database_attr"));
            configDatabaseInfo.setDatabaseLabel((String) rtnMap.get("database_label"));
            configDatabaseInfo.setLoginName((String) rtnMap.get("login_name"));
            configDatabaseInfo.setLoginPassword((String) rtnMap.get("login_password"));

            configDatabaseInfos.add(configDatabaseInfo);
        }
        return CollectionUtils.isEmpty(configDatabaseInfos)?null:configDatabaseInfos.get(0);
    }
}
