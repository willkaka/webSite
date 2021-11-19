package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("deleteRecord")
@Slf4j
public class DeleteRecord implements RequestFun {

    @Autowired
    private DataService dataService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
//        BizException.trueThrow(StringUtil.isBlank(dbName),"数据库,不允许为空值!");
        String libName = (String) inputValue.get("libName");
//        BizException.trueThrow(StringUtil.isBlank(libName),"库名,不允许为空值!");
//
        String tableName = (String) inputValue.get("tableName");
//        BizException.trueThrow(StringUtil.isBlank(tableName),"表名,不允许为空值!");
//

        Connection connection = null;
        if(StringUtils.isNotBlank(dbName)) {
            connection = dataService.getDatabaseConnection(dbName,libName);
        }else{
            tableName = (String) requestDto.getEventInfo().getParamMap().get("tableName");
            connection = dataService.getDatabaseConnection();
        }
        //String sql = SqlUtil.getUpdateSql(tableName,updatedRecordMap,originalRecordMap);
        List<String> keyFields = DbUtil.getTablePrimaryKeys(connection,libName,tableName);
        BizException.trueThrow(CollectionUtil.isEmpty(keyFields),"数据表"+tableName+",无主键,无法更新!");

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE").append(" ").append("FROM").append(" ").append(tableName).append(" ");
        sql.append(" WHERE ");
        int keyFieldCount = 0;
        for(String keyField:keyFields){
            keyFieldCount++;
            if(keyFieldCount != 1) {
                sql.append(" AND ");
            }
            sql.append(keyField).append(" = ").append("\"").append(inputValue.get("modal" + keyField)).append("\" ");
        }
        log.info("准备执行sql:"+sql.toString());
        DbUtil.executeSql(connection,sql.toString());
        log.info("已执行sql:"+sql.toString());

        dataService.closeConnection(connection);

        Map<String,Object> webNextOprMap = new HashMap<>();
        webNextOprMap.put("alert","true");//是否提示成功
        webNextOprMap.put("sucMsg","数据已删除成功！"); //提示信息
        webNextOprMap.put("type","hide");//执行任务类型
        webNextOprMap.put("hideEle","swBackGround"); //更新成功后关闭更新子窗口。
        //{"eventList":[{"event":"click","type":"buttonReq","id":"queryTableRecords"}]}
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEvent("click");
        eventInfo.setType("buttonReq");
        eventInfo.setId("queryTableRecords");
        webNextOprMap.put("callEven",eventInfo);//重新查询数据，实现自动刷新功能
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
