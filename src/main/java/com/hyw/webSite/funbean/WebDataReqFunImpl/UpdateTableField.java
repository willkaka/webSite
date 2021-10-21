package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.exception.IfThrow;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.Map;

@Service("updateTableField")
@Slf4j
public class UpdateTableField implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
        IfThrow.trueThenThrowMsg(StringUtil.isBlank(dbName),"数据库,不允许为空值!");
        String libName = (String) inputValue.get("libName");
        IfThrow.trueThenThrowMsg(StringUtil.isBlank(libName),"库名,不允许为空值!");
        String tableName = (String) inputValue.get("tableName");
        IfThrow.trueThenThrowMsg(StringUtil.isBlank(tableName),"表名,不允许为空值!");

        Connection connection = null;
        if(Constant.DB_SOURCE_SYS.equals(dbName)){
            connection = dataService.getSpringDatabaseConnection();
        }else {
            ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                    .eq(ConfigDatabaseInfo::getDatabaseName, dbName));
            configDatabaseInfo.setDatabaseLabel(libName);
            connection = DbUtil.getConnection(configDatabaseInfo);
        }

        //设计数据表
        //ALTER TABLE user10 MODIFY email VARCHAR(200) NOT NULL DEFAULT 'a@a.com';
        String fieldName = (String) inputValue.get("modal"+"COLUMN_NAME");
        String fieldType = (String) inputValue.get("modal"+"TYPE_NAME");
        String fieldSize = (String) inputValue.get("modal"+"COLUMN_SIZE");
        String fieldDecimal = (String) inputValue.get("modal"+"DECIMAL_DIGITS");
        String fieldRemarks = (String) inputValue.get("modal"+"REMARKS");

        StringBuffer sql = new StringBuffer();
        sql.append("ALTER").append(" ").append(tableName).append(" ")
                .append("MODIFY").append(" ").append(fieldName).append(" ")
                .append(fieldType).append("(").append(fieldSize);
        if(StringUtil.isBlank(fieldDecimal) || "null".equals(fieldDecimal)){
            sql.append(")");
        }else{
            sql.append(",").append(fieldDecimal).append(")");
        }

        DbUtil.executeSql(connection,sql.toString());

        dataService.closeConnection(connection);


        return returnDto;
    }
}
