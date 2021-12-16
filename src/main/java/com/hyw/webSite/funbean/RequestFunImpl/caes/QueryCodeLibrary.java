package com.hyw.webSite.funbean.RequestFunImpl.caes;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
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

@Service("queryCodeLibrary")
public class QueryCodeLibrary extends RequestFunUnit<List<Map<String,FieldAttr>>, QueryCodeLibrary.QueryVariable> {

    @Autowired
    private DataService dataService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryVariable variable){
        //输入检查
        BizException.trueThrow(StringUtil.isBlank(variable.getDbName()),"DB不允许为空值!");

        BizException.trueThrow(StringUtil.isBlank(variable.getCodeNo()),"codeNo,不允许为空值!");

    }

    @Override
    public List<Map<String,FieldAttr>> execLogic(RequestDto requestDto, QueryVariable variable){
        String libName = "caes";
        ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                .eq(ConfigDatabaseInfo::getDatabaseName,variable.getDbName()));
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        String sql = "SELECT code_no,item_no,item_name,sort_no,item_description,available,item_script,created_by,created_date " +
                "FROM code_library WHERE record_ind='A'";
        if(StringUtil.isNotBlank(variable.getCodeNo())){
            sql = sql + " AND code_no='"+variable.getCodeNo()+"'";
        }
        List<Map<String, FieldAttr>> records = DbUtil.getSqlRecordsWithFieldAttr(connection,sql);
        DbUtil.closeConnection(connection);

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示

        return records;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestPubDto {
        private String dbName;
        private String codeNo;
    }
}
