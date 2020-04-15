package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.service.DynamicTableService;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("insertRecord")
@Slf4j
public class InsertRecord implements RequestFun {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        Map<String,Object> rtnMap = new HashMap<>();
        //rtnMap.put("showType","table");//以表格形式显示
//        rtnMap.put("showType","table-edit");//以表格形式显示
//
//        Map<String,Object> inputValueMap = (Map<String,Object>) reqMap.get("inputValue");
//        Map<String,Object> newRecordMap = (Map<String,Object>) reqMap.get("newRecordMap");
//
//        String databaseName = (String) inputValueMap.get("dbName");
//        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(databaseName);
//        Connection connection = DbUtil.getConnection(configDatabaseInfo.getDatabaseType(),configDatabaseInfo.getDatabaseDriver(),
//                configDatabaseInfo.getDatabaseAddr(),configDatabaseInfo.getDatabaseAttr(),configDatabaseInfo.getDatabaseLabel(),
//                configDatabaseInfo.getLoginName(),configDatabaseInfo.getLoginPassword());
//
//        String tableName = (String) inputValueMap.get("tableName");
//        String sql = SqlUtil.getInsertSql(tableName,newRecordMap);
//        log.info(sql);
//        Boolean rtnFlag = DbUtil.executeSql(connection,sql);
//        DbUtil.closeConnection(connection);
//
//        rtnMap.put("updateRecordCount",1);//返回更新记录数
//        rtnMap.put("updateRecordResult",rtnFlag);//返回执行结果true/false

        return returnDto;
    }
}
