package com.hyw.webSite.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SqliteUtil {

    public static List<Map<String,String>> getTableFields(Connection connection, String table){
        List<Map<String,String>> fields = new ArrayList<>();

        if(connection == null){
            log.error("数据库连接不允许为null！");
            return null;
        }

        String sql = "pragma table_info(" + table + ")";
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if(res != null) {
                while (res.next()) {        //如果当前语句不是最后一条，则进入循环
                    Map<String,String> field = new HashMap<>();
                    field.put("cid",res.getString("cid"));
                    field.put("name",res.getString("name"));  //名称
                    field.put("type",res.getString("type"));   //类型
                    field.put("notnull",res.getString("notnull"));   //notnull
                    field.put("dflt_value",res.getString("dflt_value"));   //默认值
                    field.put("pk",res.getString("pk"));   //pk
                    fields.add(field);
                }
            }
            if(res != null) res.close();
            statement.close();
        }catch(Exception e){
            log.error("执行SQL({})出错！",sql,e);
        }finally {
            //DbUtil.closeConnection(connection);
        }

        return fields;
    }
}
