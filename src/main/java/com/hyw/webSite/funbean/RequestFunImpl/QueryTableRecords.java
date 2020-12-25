package com.hyw.webSite.funbean.RequestFunImpl;

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
        returnDto.getOutputMap().put("withPage",true);//表格内容分页显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = inputValue.get("dbName");
        if(StringUtil.isBlank(dbName)){
            throw new BizException("DB不允许为空值!");
        }
        String libName = inputValue.get("libName");
        if(StringUtil.isBlank(libName)){
            throw new BizException("数据库,不允许为空值!");
        }
        String tableName = inputValue.get("tableName");
        if(StringUtil.isBlank(tableName)){
            throw new BizException("表名,不允许为空值!");
        }

        int pageNow = 0;         //当前的页码
        int pageSize = 10;       //每页中显示多少条记录
        int totalCount = 0;      //表中记录的总行数
        //int pageCount = (totalCount-1)/pageSize+1;       //总页数

        //连接数据库，查询数据，关闭数据库
        Connection connection = DbUtil.getConnection(configDatabaseInfoService.getDatabaseConfig(dbName),libName);
        totalCount = DbUtil.getTableRecordCount(connection,dbName,libName,tableName);
        List<Map<String,FieldAttr>> records = DbUtil.getTableRecords(connection,dbName,libName,tableName,pageNow*pageSize,pageSize);
        DbUtil.closeConnection(connection);

        returnDto.getOutputMap().put("tableRecordList", records);
        returnDto.getOutputMap().put("totalCount", totalCount);
        returnDto.getOutputMap().put("pageNow", pageNow+1);
        returnDto.getOutputMap().put("pageSize", pageSize);
        return returnDto;
    }
}
