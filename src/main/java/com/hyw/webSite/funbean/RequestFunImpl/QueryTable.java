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
import java.util.*;

@Service("queryTable")
public class QueryTable extends RequestFunUnit<List<Map<String, FieldAttr>>, QueryTable.QueryVariable> {

    @Autowired
    private DataService dataService;

    private static Map<String,String> showCols = new LinkedHashMap<>();
    static{
        showCols.put("COLUMN_NAME", "字段名");
        showCols.put("TYPE_NAME", "类型");
        showCols.put("COLUMN_SIZE", "长度");
        showCols.put("DECIMAL_DIGITS", "小数位");
        showCols.put("REMARKS", "描述");
    };

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryTable.QueryVariable variable){
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
    public List<Map<String, FieldAttr>> execLogic(RequestDto requestDto, QueryTable.QueryVariable variable){

        ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                .eq(ConfigDatabaseInfo::getDatabaseName,variable.getDbName()));
        configDatabaseInfo.setDatabaseLabel(variable.getLibName());
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        //表格定义字段
        List<Map<String,Object>> fields = DbUtil.getFieldInfo(connection,variable.getDbName(),variable.getLibName(),variable.getTableName());
        DbUtil.closeConnection(connection);
        delNeedlessRecords(fields);

        List<Map<String, FieldAttr>> records = new ArrayList<>();
        for(Map<String,Object> field:fields){
            Map<String, FieldAttr> record = new LinkedHashMap<>();
            for(String fieldName:showCols.keySet()){
                FieldAttr fieldAttr = new FieldAttr();
                fieldAttr.setRemarks(showCols.get(fieldName));
                fieldAttr.setColumnName(fieldName);
                fieldAttr.setValue(field.get(fieldName));
                record.put(fieldName,fieldAttr);
            }
            records.add(record);
        }
        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(false);//表格内容分页显示
        return records;
    }

    private void delNeedlessRecords(List<Map<String,Object>> records){
        List<String> fieldList = new ArrayList<>();
        showCols.forEach((key,value) ->fieldList.add(key));
        for (Map<String, Object> record : records) {
            //使用迭代器的remove()方法删除元素
            record.entrySet().removeIf(entry -> !fieldList.contains(entry.getKey()));
        }
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
