package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service("editRecord")
@Slf4j
public class EditRecord implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
        BizException.trueThrow(StringUtil.isBlank(dbName),"数据库,不允许为空值!");
        String libName = (String) inputValue.get("libName");
        BizException.trueThrow(StringUtil.isBlank(libName),"库名,不允许为空值!");
        String tableName = (String) inputValue.get("tableName");
        BizException.trueThrow(StringUtil.isBlank(tableName),"表名,不允许为空值!");

        Connection connection = dataService.getDatabaseConnection(dbName,libName);

        Map<String,FieldAttr> recordMap = DbUtil.getFieldAttrMap(connection,dbName,libName,tableName);
        dataService.closeConnection(connection);

        Map<String,Object> valueMap = requestDto.getEventInfo().getParamMap();
        if(CollectionUtil.isNotEmpty(valueMap)){
            for(String key:recordMap.keySet()){
                ((FieldAttr)recordMap.get(key)).setValue(valueMap.get(key)).setCurValue(valueMap.get(key));
            }
        }
        Map<String,Object> webNextOprMap = new HashMap<>();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setId("editRecord");
        eventInfo.setType("webButtonShowModal");
        eventInfo.setRecordMap(recordMap);
        webNextOprMap.put("callEven",eventInfo);
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
