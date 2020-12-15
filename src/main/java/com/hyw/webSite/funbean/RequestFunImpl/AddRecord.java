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

@Service("addRecord")
@Slf4j
public class AddRecord implements RequestFun {

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

        //生成insert语句
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");
        StringBuilder sqlFields = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        for(String fieldName:inputValue.keySet()){
            if("modal".equals( fieldName.substring(0,5)) ){
                if(StringUtil.isNotBlank(sqlFields.toString())) sqlFields.append(",");
                sqlFields.append(fieldName.substring(5,fieldName.length()));
                if(StringUtil.isNotBlank(sqlValues.toString())) sqlValues.append(",");
                if(StringUtil.isBlank(inputValue.get(fieldName))){
                    sqlValues.append("null");
                }else {
                    sqlValues.append("\"").append(inputValue.get(fieldName)).append("\" ");
                }
            }
        }
        sql.append(sqlFields).append(") VALUES(").append(sqlValues).append(")");

        //建立数据库连接，并执行写入操作
        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dbName);
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        log.info("准备执行sql:"+sql.toString());
        DbUtil.executeSql(connection,sql.toString());
        log.info("已执行sql:"+sql.toString());

        DbUtil.closeConnection(connection);

        //设置成功写入后的自动刷新
        Map<String,Object> webNextOprMap = new HashMap<>();
        webNextOprMap.put("type","hide");
        webNextOprMap.put("alert","true");
        webNextOprMap.put("hideEle","swBackGround"); //更新成功后关闭更新子窗口。
        webNextOprMap.put("sucMsg","数据成功写入！"); //更新成功后关闭更新子窗口。
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEvent("click");
        eventInfo.setType("buttonReq");
        eventInfo.setId("queryTableRecords");
        webNextOprMap.put("callEven",eventInfo);
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
