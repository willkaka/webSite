package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service("queryTableRecords")
public class QueryTableRecords extends RequestFunUnit<List<Map<String,FieldAttr>>, QueryTableRecords.QueryVariable> {

    @Autowired
    private DataService dataService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryVariable variable){
        //输入检查
        if(StringUtil.isBlank(variable.getDbName())){
            throw new BizException("DB不允许为空值!");
        }
        if(StringUtil.isBlank(variable.getLibName())){
            throw new BizException("数据库,不允许为空值!");
        }
        if(StringUtil.isBlank(variable.getTableName())){
            throw new BizException("表名,不允许为空值!");
        }
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<Map<String,FieldAttr>> execLogic(RequestDto requestDto, QueryVariable variable){
        //取数逻辑
        int pageNow = requestDto.getEventInfo().getReqPage()==0?1:requestDto.getEventInfo().getReqPage();         //当前的页码
        int pageSize = 10;
        int totalCount;      //表中记录的总行数

        //连接数据库，查询数据，关闭数据库
        Connection connection = DbUtil.getConnection(dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                .eq(ConfigDatabaseInfo::getDatabaseName,variable.getDbName())),variable.getLibName());
        totalCount = DbUtil.getTableRecordCount(connection,variable.getDbName(),variable.getLibName(),variable.getTableName());
        List<Map<String,FieldAttr>> records = DbUtil.getTableRecords(connection,variable.getDbName(),variable.getLibName(),variable.getTableName(),(pageNow-1)*pageSize,pageSize);
        DbUtil.closeConnection(connection);

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(true);//表格内容分页显示
        variable.setTotalCount(totalCount);
        variable.setPageNow(pageNow);
        variable.setPageSize(pageSize); //每页中显示多少条记录

        return records;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestFunUnit.Variable {
        private String dbName;
        private String libName;
        private String tableName;
    }
}
