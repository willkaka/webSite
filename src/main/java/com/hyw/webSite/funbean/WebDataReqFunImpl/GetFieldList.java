package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.WebElementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service("getFieldList")
@Slf4j
public class GetFieldList implements WebDataReqFun {

    @Autowired
    private DataService dataService;

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        Map<String,Object> changedEleMap = new HashMap<>();
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String selectedDb = inputValue.get("dbName");
        String selectedLib = inputValue.get("libName");
        String tableName = inputValue.get("tableName");

        Connection connection = dataService.getDatabaseConnection(selectedDb,selectedLib);
        List<Map<String,Object>> fields = DbUtil.getFieldInfo(connection,selectedDb,selectedLib,tableName);
        dataService.closeConnection(connection);

        changedEleMap.put("recordMap",fields);

        WebElementDto webElementDto = new WebElementDto();
        webElementDto.setId(eventInfo.getRelEleId());
        webElementDto.setType(eventInfo.getRelEleType());
        webElementDto.setChgType(eventInfo.getRelEleChgType());
        //webElement.setDataMap(map);
        changedEleMap.put(eventInfo.getRelEleId(), webElementDto);

        return changedEleMap;
    }
}
