package com.hyw.webSite.funbean.RequestFunImpl.caes;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
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
import java.util.List;
import java.util.Map;

@Service("queryCodeLibrary")
public class QueryCodeLibrary implements RequestFun {

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
        String codeNo = (String) inputValue.get("codeNo");
        if(StringUtil.isBlank(dbName)){
            throw new BizException("DB不允许为空值!");
        }
        String libName = "caes";

        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dbName);
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        String sql = "SELECT code_no,item_no,item_name,sort_no,item_description,available,item_script,created_by,created_date " +
                "FROM code_library WHERE record_ind='A'";
        if(StringUtil.isNotBlank(codeNo)){
            sql = sql + " AND code_no='"+codeNo+"'";
        }
        List<Map<String,Object>> records = DbUtil.getSqlRecords(connection,sql);
        DbUtil.closeConnection(connection);

        List<String> tableColList = new ArrayList<>();
        tableColList.add("code_no");
        tableColList.add("item_no");
        tableColList.add("item_name");
        tableColList.add("sort_no");
        tableColList.add("item_description");
        tableColList.add("available");
        tableColList.add("item_script");
        tableColList.add("created_by");
        tableColList.add("created_date");

        returnDto.getOutputMap().put("tableColList", tableColList);
        returnDto.getOutputMap().put("tableRecordList", records);

        return returnDto;
    }
}
