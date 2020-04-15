package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service("getFieldList")
@Slf4j
public class GetFieldList implements WebDataReqFun {

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        Map<String,Object> changedEleMap = new HashMap<>();
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String selectedDb = inputValue.get("dbName");
        String selectedLib = inputValue.get("libName");
        String tableName = inputValue.get("tableName");

        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(selectedDb);
        if(!"sqlite".equals(configDatabaseInfo.getDatabaseType().toLowerCase())) {
            configDatabaseInfo.setDatabaseLabel(selectedLib);
        }
        Connection connection = DbUtil.getConnection(configDatabaseInfo);
        List<Map<String,Object>> fields = DbUtil.getFieldInfo(connection,selectedDb,tableName);
        DbUtil.closeConnection(connection);

        changedEleMap.put("recordMap",fields);

        WebElement webElement = new WebElement();
        webElement.setId(eventInfo.getRelEleId());
        webElement.setType(eventInfo.getRelEleType());
        webElement.setChgType(eventInfo.getRelEleChgType());
        //webElement.setDataMap(map);
        changedEleMap.put(eventInfo.getRelEleId(),webElement);

        return changedEleMap;
    }
}
