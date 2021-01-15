package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("genClass")
@Slf4j
public class GenClass implements RequestFun {

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","textArea");//以表格形式显示
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

        //连接数据库，查询数据，关闭数据库
        Connection connection = DbUtil.getConnection(configDatabaseInfoService.getDatabaseConfig(dbName),libName);
        List<FieldAttr> fields = DbUtil.getFieldAttr(connection,dbName,libName,tableName);
        List<String> keys = DbUtil.getTablePrimaryKeys(connection,libName,tableName);
        String tableComment = DbUtil.getTableComment(connection,tableName);
        DbUtil.closeConnection(connection);

        List<String> importJar = new ArrayList<>();
        importJar.add("com.baomidou.mybatisplus.annotation.TableName");
        importJar.add("lombok.Data");
        importJar.add("lombok.experimental.Accessors");

        StringBuilder s = new StringBuilder();
        s.append("/**").append("\n").append(" *").append(tableComment).append("\n").append("*/").append("\n")
                .append("@Data").append("\n")
                .append("@Accessors(chain = true)").append("\n")
                .append("@TableName(\"").append(tableName).append("\")").append("\n")
                .append("public class ").append(StringUtil.underlineToCamelCase(tableName).substring(0,1).toUpperCase())
                .append(StringUtil.underlineToCamelCase(tableName).substring(1))
                //.append(" extends BaseEntity")
                .append(" {").append("\n\n")
                .append("\t").append("private static final long serialVersionUID = 1L;").append("\n\n");
        for(FieldAttr fieldAttr:fields){
            s.append("\t").append("/**").append("\n")
                    .append("\t").append(" * ").append(fieldAttr.getRemarks()).append("\n")
                    .append("\t").append("*/").append("\n");
            if(keys.contains(fieldAttr.getColumnName())){
                s.append("\t").append("@TableId");
                if(!importJar.contains("com.baomidou.mybatisplus.annotation.TableId"))importJar.add("com.baomidou.mybatisplus.annotation.TableId");
            }else{
                s.append("\t").append("@TableField");
                if(!importJar.contains("com.baomidou.mybatisplus.annotation.TableField"))importJar.add("com.baomidou.mybatisplus.annotation.TableField");
            }
            s.append("(\"").append(fieldAttr.getColumnName()).append("\")").append("\n")
                    .append("\t").append("private ");
            switch (fieldAttr.getTypeName()){
                case "VARCHAR": s.append("String "); break;
                case "CHAR"   : s.append("char "); break;
                case "INT": s.append("Integer "); break;
                case "DATETIME": s.append("LocalDateTime "); if(!importJar.contains("java.time.LocalDateTime"))importJar.add("java.time.LocalDateTime");break;
                case "DATE": s.append("LocalDate "); if(!importJar.contains("java.time.LocalDate"))importJar.add("java.time.LocalDate");break;
                case "DECIMAL": s.append("BigDecimal "); if(!importJar.contains("java.math.BigDecimal"))importJar.add("java.math.BigDecimal");break;
                default:log.error("未知数据类型({})",fieldAttr.getTypeName());
            }
            s.append(StringUtil.underlineToCamelCase(fieldAttr.getColumnName())).append(";").append("\n\n");
        }
        s.append("}");

        StringBuilder importString = new StringBuilder();
        for(String jar:importJar){
            importString.append("import ").append(jar).append(";").append("\n");
        }

        String value = importString.toString() + "\n" + s.toString();
        returnDto.getOutputMap().put("textAreaValue", value);
        return returnDto;
    }

}