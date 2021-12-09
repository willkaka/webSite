package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.dbservice.constant.DbConstant;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.SqlUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.dto.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service("updateRecord")
@Slf4j
public class UpdateRecord implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
//        BizException.trueThrow(StringUtil.isBlank(dbName),"数据库,不允许为空值!");
//
        String libName = (String) inputValue.get("libName");
//        BizException.trueThrow(StringUtil.isBlank(libName),"库名,不允许为空值!");
//
        String tableName = (String) inputValue.get("tableName");
//        BizException.trueThrow(StringUtil.isBlank(tableName),"表名,不允许为空值!");
//
        Connection connection = null;
        if(DbConstant.DB_SOURCE_SYS.equals(dbName)) {
            connection = dataService.getDatabaseConnection();
        }else if(StringUtils.isNotBlank(dbName)) {
            ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                    .eq(ConfigDatabaseInfo::getDatabaseName,dbName));
            configDatabaseInfo.setDatabaseLabel(libName);
            connection = DbUtil.getConnection(configDatabaseInfo);
        }else{
            tableName = (String) requestDto.getEventInfo().getParamMap().get("tableName");
            connection = dataService.getDatabaseConnection();
        }

        EventInfo eventInfo = requestDto.getEventInfo();
        Map<String, FieldAttr> recordMap = eventInfo.getRecordMap();
        String updateSql = SqlUtil.getUpdateSql(connection,libName,tableName,recordMap);

        log.info("准备执行sql:"+updateSql);
        DbUtil.executeSql(connection,updateSql);
        log.info("已执行sql:"+updateSql);
        dataService.closeConnection(connection);

        returnDto.setWebNextOpr(eventInfo.getParamMap());//页面在收到响应报文后处理方式
        return returnDto;
    }
}
