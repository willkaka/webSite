package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("addNewRecord")
@Slf4j
public class AddNewRecord implements RequestFun {

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
        if(StringUtil.isBlank(dbName)){
            throw new BizException("数据库,不允许为空值!");
        }
        String libName = (String) inputValue.get("libName");
        if(StringUtil.isBlank(libName)){
            throw new BizException("库名,不允许为空值!");
        }
        String tableName = (String) inputValue.get("tableName");
        if(StringUtil.isBlank(tableName)){
            throw new BizException("表名,不允许为空值!");
        }

        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dbName);
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);
        List<Map<String,Object>> recordMap = DbUtil.getTableFieldsMapList(connection,tableName);
        DbUtil.closeConnection(connection);

        Map<String,Object> webNextOprMap = new HashMap<>();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setType("webButtonShowModal");
        eventInfo.setRecordMap(recordMap);
        webNextOprMap.put("callEven",eventInfo);
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
