package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service("getFieldFromTab")
@Slf4j
public class GetFieldFromTab implements WebDataReqFun {

    @Autowired
    private DataService dataService;

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息
        Map<String,Object> changedEleMap = new HashMap<>();
        WebElementDto webElementDto = new WebElementDto();
        webElementDto.setId(eventInfo.getRelEleId());
        webElementDto.setType(eventInfo.getRelEleType());
        webElementDto.setChgType(eventInfo.getRelEleChgType());

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String selectedDb = inputValue.get("dbName");
        String selectedLib = inputValue.get("libName");
        String tableName = inputValue.get("tableName");
        if(StringUtil.isBlank(selectedDb) || StringUtil.isBlank(selectedLib) || StringUtil.isBlank(tableName)){
            changedEleMap.put(eventInfo.getRelEleId(), webElementDto);
            return changedEleMap;
        }

//        Connection connection = DbUtil.getConnection(configDatabaseInfoService.getDatabaseConfig(selectedDb), selectedLib);
        Connection connection = DbUtil.getConnection(dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                .eq(ConfigDatabaseInfo::getDatabaseName,selectedDb)), selectedLib);
        Map<String, FieldAttr> fields = DbUtil.getFieldAttrMap(connection,selectedDb,selectedLib,tableName);
        DbUtil.closeConnection(connection);

        Map<String,String> map = new LinkedHashMap<>();
        for(String fieldName:fields.keySet()){
            map.put(fieldName,fieldName);
        }

        webElementDto.setDataMap(map);
        changedEleMap.put(eventInfo.getRelEleId(), webElementDto);
        return changedEleMap;
    }
}
