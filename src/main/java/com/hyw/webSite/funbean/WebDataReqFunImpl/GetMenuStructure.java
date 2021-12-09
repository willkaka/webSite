package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
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

/**
 * 取新建菜单，需要录入的数据结构
 */
@Service("getMenuStructure")
@Slf4j
public class GetMenuStructure implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){


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

        //输出
        Map<String,Object> webNextOprMap = new HashMap<>();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setId("addNewRecord");
        eventInfo.setType("webButtonShowModal");//显示modal
        eventInfo.setRecordMap(recordMap);//显示
        webNextOprMap.put("callEven",eventInfo);//返回后页面的下一步处理
        ReturnDto returnDto = new ReturnDto();
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
