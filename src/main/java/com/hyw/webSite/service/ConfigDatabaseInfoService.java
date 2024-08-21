package com.hyw.webSite.service;

import com.hyw.webSite.constant.DbConstant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.gdata.DataService;
import com.hyw.gdata.NQueryWrapper;
import com.hyw.webSite.utils.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class ConfigDatabaseInfoService {

    @Autowired
    private DataService dataService;

    /**
     * 按传入的db名称取db连接
     * @param dbName    db名称
     * @param libName   数据库名称
     * @return Connection 数据库连接
     */
    public Connection getDatabaseConnection(String dbName, String libName){
        Connection connection;
        if(DbConstant.DB_SOURCE_SYS.equals(dbName)) {
            connection = dataService.getDatabaseConnection();
        }else {
            ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                    .eq(ConfigDatabaseInfo::getDatabaseName, dbName));
            configDatabaseInfo.setDatabaseLabel(libName);
            connection = DbUtil.getConnection(configDatabaseInfo);
        }
        return connection;
    }
}
