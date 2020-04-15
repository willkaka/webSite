package com.hyw.webSite.utils;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class SqlUtil {

    /**
     * 拼接单表查询sql语句
     * @param tableName 表名
     * @param selectFields 选择字段
     * @param selectWhere 条件
     * @param selectGroupby 集合
     * @param selectOrderby 排序
     * @return sql字符串
     */
    public static String getSelectSql(String tableName, String selectFields,String selectWhere,String selectGroupby,String selectOrderby){
        return "SELECT " +
                (StringUtil.isNotBlank(selectFields) ? selectFields : "* ") +
                " FROM " + tableName +
                (StringUtil.isNotBlank(selectWhere) ? " WHERE " + selectWhere : "") +
                (StringUtil.isNotBlank(selectGroupby) ? " GROUP BY " + selectGroupby : "") +
                (StringUtil.isNotBlank(selectOrderby) ? " ORDER BY " + selectOrderby : "");
    }

    /**
     * 拼接单表查询sql语句
     * @param tableName 表名
     * @param selectFields 选择字段list
     * @param whereFieldValueMap 条件map
     * @return sql字符串
     */
    public static String getSelectSql(String tableName, List<String> selectFields, Map<String,Object> whereFieldValueMap){
        StringBuilder selectFieldsString = new StringBuilder();
        StringBuilder whereConditionString = new StringBuilder();

        if(StringUtil.isBlank(tableName)) return null;

        //select fields
        if(!CollectionUtils.isEmpty(selectFields)) {
            boolean firstField = true;
            for (String fieldName : selectFields) {
                if (firstField) {
                    selectFieldsString.append(fieldName);
                    firstField = false;
                } else {
                    selectFieldsString.append(",").append(fieldName);
                }
            }
        }else{
            selectFieldsString.append("*");
        }

        //where conditions
        if(whereFieldValueMap != null && whereFieldValueMap.size()>0){
            for (String field : whereFieldValueMap.keySet()) {
                if(whereConditionString.length()>0)
                    whereConditionString.append(" AND ");
                whereConditionString.append(field).append("=");

                if(ObjectUtil.isString(whereFieldValueMap.get(field)))
                    whereConditionString.append("'").append(whereFieldValueMap.get(field)).append("'");
                if(ObjectUtil.isInteger(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
                if(ObjectUtil.isBigDecimal(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
            }
        }

        return whereConditionString.length()>0?
                "SELECT " + selectFieldsString + " FROM " + tableName + " WHERE (" + whereConditionString + ")":
                "SELECT " + selectFieldsString + " FROM " + tableName;
    }


    /**
     * 拼接单表新增单记录sql语句
     * @param tableName 表名
     * @param fieldValueMap 记录字段及值map
     * @return sql字符串
     */
    public static String getInsertSql(String tableName, Map<String,Object> fieldValueMap){
        if(StringUtil.isBlank(tableName)) return null;
        if(CollectionUtils.isEmpty(fieldValueMap)) return null;

        StringBuilder insertString = new StringBuilder();
        insertString.append("INSERT INTO ").append(tableName).append(" (");

        boolean firstKey = true;
        for (String field : fieldValueMap.keySet()) {
            if(!firstKey) {
                insertString.append(",");
            }else{
                firstKey=false;
            }
            insertString.append(field);
        }
        insertString.append(") VALUES(");

        firstKey = true;
        for (String field : fieldValueMap.keySet()) {
            if(!firstKey) {
                insertString.append(",");
            }else{
                firstKey=false;
            }
            insertString.append(ObjectUtil.isString(fieldValueMap.get(field))?"'"+fieldValueMap.get(field)+"'":fieldValueMap.get(field));
        }
        insertString.append(")");

        return insertString.toString();
    }

    /**
     * 拼接单表单记录更新sql语句
     * @param tableName 表名
     * @param fieldValueMap 更新字段及值map
     * @param whereFieldValueMap 条件map
     * @return sql字符串
     */
    public static String getUpdateSql(String tableName, Map<String,Object> fieldValueMap, Map<String,Object> whereFieldValueMap){
        StringBuilder setFieldsString = new StringBuilder();
        StringBuilder whereConditionString = new StringBuilder();

        if(StringUtil.isBlank(tableName)) return null;
        if(CollectionUtils.isEmpty(fieldValueMap)) return null;

        //set fields
        for (String field : fieldValueMap.keySet()) {
            if(setFieldsString.length()>0)
                setFieldsString.append(" , ");
            setFieldsString.append(field).append("=");

            if(ObjectUtil.isString(fieldValueMap.get(field)))
                setFieldsString.append("'").append(fieldValueMap.get(field)).append("'");
            if(ObjectUtil.isInteger(fieldValueMap.get(field)))
                setFieldsString.append(fieldValueMap.get(field));
            if(ObjectUtil.isBigDecimal(fieldValueMap.get(field)))
                setFieldsString.append(fieldValueMap.get(field));
        }

        //where conditions
        if(!CollectionUtils.isEmpty(whereFieldValueMap)){
            for (String field : whereFieldValueMap.keySet()) {
                if(whereConditionString.length()>0)
                    whereConditionString.append(" AND ");
                whereConditionString.append(field).append("=");

                if(ObjectUtil.isString(whereFieldValueMap.get(field)))
                    whereConditionString.append("'").append(whereFieldValueMap.get(field)).append("'");
                if(ObjectUtil.isInteger(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
                if(ObjectUtil.isBigDecimal(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
            }
        }

        return whereConditionString.length()>0?
                "UPDATE " + tableName + " SET " + setFieldsString + " WHERE (" + whereConditionString + ")":
                "UPDATE " + tableName + " SET " + setFieldsString;
    }


    /**
     * 拼接单表记录删除sql语句
     * @param tableName 表名
     * @param whereFieldValueMap 条件map
     * @return sql字符串
     */
    public static String getDeleteSql(String tableName, Map<String,Object> whereFieldValueMap){
        StringBuilder setFieldsString = new StringBuilder();
        StringBuilder whereConditionString = new StringBuilder();

        if(StringUtil.isBlank(tableName)) return null;

        //where conditions
        if(!CollectionUtils.isEmpty(whereFieldValueMap)){
            for (String field : whereFieldValueMap.keySet()) {
                if(whereConditionString.length()>0)
                    whereConditionString.append(" AND ");
                whereConditionString.append(field).append("=");

                if(ObjectUtil.isString(whereFieldValueMap.get(field)))
                    whereConditionString.append("'").append(whereFieldValueMap.get(field)).append("'");
                if(ObjectUtil.isInteger(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
                if(ObjectUtil.isBigDecimal(whereFieldValueMap.get(field)))
                    whereConditionString.append(whereFieldValueMap.get(field));
            }
        }

        return whereConditionString.length()>0?
                "DELETE FROM " + tableName + " WHERE (" + whereConditionString + ")":
                "DELETE FROM " + tableName;
    }
}
