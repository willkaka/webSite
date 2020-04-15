package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
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

        DbUtil.closeConnection(connection);


        return returnDto;
    }
}
