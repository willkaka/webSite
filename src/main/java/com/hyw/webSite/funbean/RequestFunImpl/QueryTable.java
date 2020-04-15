package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service("queryTable")
public class QueryTable implements RequestFun {

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    private String[] cols = new String[]{"COLUMN_NAME","TYPE_NAME","COLUMN_SIZE","DECIMAL_DIGITS","REMARKS"};

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","table");//以表格形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

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

        //表格列信息
        List<String> tableColList = DbUtil.getFieldInfoShowCol(connection,dbName,tableName);
        delNeedlessFields(tableColList);
        returnDto.getOutputMap().put("tableColList", tableColList);

        //表格记录数据
        List<Map<String,Object>> fields = DbUtil.getFieldInfo(connection,dbName,tableName);
        DbUtil.closeConnection(connection);
        delNeedlessRecords(fields);
        returnDto.getOutputMap().put("tableRecordList", fields);

        return returnDto;
    }

    private void delNeedlessFields(List<String> fields){
        List<String> fieldList = Arrays.asList(cols);
        fields.removeIf(field -> !fieldList.contains(field));
    }

    private void delNeedlessRecords(List<Map<String,Object>> records){
        List<String> fieldList = Arrays.asList(cols);
        for (Map<String, Object> record : records) {
            //使用迭代器的remove()方法删除元素
            record.entrySet().removeIf(entry -> !fieldList.contains(entry.getKey()));
        }
    }
}
