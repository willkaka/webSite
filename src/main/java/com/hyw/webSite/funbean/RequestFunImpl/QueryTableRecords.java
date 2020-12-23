package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.service.DynamicTableService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("queryTableRecords")
public class QueryTableRecords implements RequestFun {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","table");//以表格形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
        if(StringUtil.isBlank(dbName)){
            throw new BizException("DB不允许为空值!");
        }
        String libName = (String) inputValue.get("libName");
        if(StringUtil.isBlank(libName)){
            throw new BizException("数据库,不允许为空值!");
        }
        String tableName = (String) inputValue.get("tableName");
        if(StringUtil.isBlank(tableName)){
            throw new BizException("表名,不允许为空值!");
        }

        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dbName);
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        List<String> tableColList = DbUtil.getFieldNameList(connection,libName,tableName);
        //List<Map<String,Object>> records = DbUtil.getTableRecords(connection,tableName);
        List<Map<String,FieldAttr>> records = DbUtil.getTableRecords(connection,dbName,libName,tableName);

        DbUtil.closeConnection(connection);

        //returnDto.getOutputMap().put("tableColList", tableColList);
//        for(Map<String,FieldAttr> record:records){
//            List<Map.Entry<String, FieldAttr>> list2 = new ArrayList<>();
//            list2.addAll(record.entrySet());
//            Collections.sort(list2, (o1, o2) -> o1.getValue().getOrdinalPosition()-o2.getValue().getOrdinalPosition());
//        }

        returnDto.getOutputMap().put("tableRecordList", records);

        return returnDto;
    }
}
