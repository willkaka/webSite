package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service("getTableCreateDdl")
@Slf4j
public class GetTableCreateDdl extends RequestFunUnit<String, GetTableCreateDdl.QueryVariable> {

    @Autowired
    private DataService dataService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(GetTableCreateDdl.QueryVariable variable){
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
    public String execLogic(RequestDto requestDto, GetTableCreateDdl.QueryVariable variable){

        //连接数据库，查询数据，关闭数据库
//        Connection connection = DbUtil.getConnection(configDatabaseInfoService.getDatabaseConfig(variable.getDbName()), variable.getLibName());
        Connection connection = DbUtil.getConnection(dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                .eq(ConfigDatabaseInfo::getDatabaseName,variable.getDbName())),variable.getLibName());
        String tableCreatedDdl = DbUtil.getTableCreatedDdl(connection,variable.getTableName());
        DbUtil.closeConnection(connection);

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示
        return tableCreatedDdl;
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
