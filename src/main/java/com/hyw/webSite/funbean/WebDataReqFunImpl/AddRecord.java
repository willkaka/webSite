package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.exception.IfThrow;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.SqlUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service("addRecord")
@Slf4j
public class AddRecord implements RequestFun {

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


        //建立数据库连接，并执行写入操作
        Connection connection = dataService.getDatabaseConnection(dbName,libName);

        //取数据表字段定义信息
        Map<String,FieldAttr> fieldAttrMap = DbUtil.getFieldAttrMap(connection,dbName,libName,tableName);
        for(String alterFieldName:inputValue.keySet()){
            if(StringUtil.isNotBlank(alterFieldName) && alterFieldName.length()>5 &&
                    "modal".equals( alterFieldName.substring(0,5)) ){
                String fieldName = alterFieldName.substring(5,alterFieldName.length());
                FieldAttr fieldAttr = fieldAttrMap.get(fieldName);
                if(null!=fieldAttr) fieldAttr.setValue(inputValue.get(alterFieldName));
            }
        }
        //生成insert语句
        String sql = SqlUtil.getInsertSql(connection,libName,tableName,fieldAttrMap);

        log.info("准备执行sql:"+sql);
        DbUtil.executeSql(connection,sql);
        log.info("已执行sql:"+sql);

        dataService.closeConnection(connection);

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
