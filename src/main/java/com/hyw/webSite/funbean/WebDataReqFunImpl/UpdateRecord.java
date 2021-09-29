package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.SqlUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service("updateRecord")
@Slf4j
public class UpdateRecord implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
//        IfThrow.trueThenThrowMsg(StringUtil.isBlank(dbName),"数据库,不允许为空值!");
//
        String libName = (String) inputValue.get("libName");
//        IfThrow.trueThenThrowMsg(StringUtil.isBlank(libName),"库名,不允许为空值!");
//
        String tableName = (String) inputValue.get("tableName");
//        IfThrow.trueThenThrowMsg(StringUtil.isBlank(tableName),"表名,不允许为空值!");
//
        Connection connection = null;
        if(StringUtils.isNotBlank(dbName)) {
            ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                    .eq(ConfigDatabaseInfo::getDatabaseName,dbName));
            configDatabaseInfo.setDatabaseLabel(libName);
            connection = DbUtil.getConnection(configDatabaseInfo);
        }else{
            tableName = (String) requestDto.getEventInfo().getParamMap().get("tableName");
            connection = dataService.getSpringDatabaseConnection();
        }

        EventInfo eventInfo = requestDto.getEventInfo();
        Map<String, FieldAttr> recordMap = eventInfo.getRecordMap();
        String updateSql = SqlUtil.getUpdateSql(connection,libName,tableName,recordMap);

        log.info("准备执行sql:"+updateSql);
        DbUtil.executeSql(connection,updateSql);
        log.info("已执行sql:"+updateSql);

        DbUtil.closeConnection(connection);

        Map<String,Object> webNextOprMap = new HashMap<>();
        webNextOprMap.put("type","hide");
        webNextOprMap.put("alert","true");
        webNextOprMap.put("hideEle","swBackGround"); //更新成功后关闭更新子窗口。
        webNextOprMap.put("sucMsg","数据已更新成功！"); //更新成功后关闭更新子窗口。
        //{"eventList":[{"event":"click","type":"buttonReq","id":"queryTableRecords"}]}
        EventInfo eventInfo2 = new EventInfo();
        eventInfo2.setEvent("click");
        eventInfo2.setType("buttonReq");
        eventInfo2.setId("queryTableRecords");
        webNextOprMap.put("callEven",eventInfo2);
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
