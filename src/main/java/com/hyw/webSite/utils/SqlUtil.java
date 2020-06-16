package com.hyw.webSite.utils;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static String getFieldValueExpress(Object object) throws IllegalAccessException {
        StringBuffer s = new StringBuffer();
        List<Field> fields = ObjectUtil.getAllFieldList(object.getClass());

        boolean addAnd = false;
        for(Field field:fields){
            //不处理静态变量和常量
            if ( Modifier.isStatic( field.getModifiers() ) || Modifier.isFinal( field.getModifiers() ) ){
                continue;
            }
            String express = getFieldValueExpress(field,object);
            if(addAnd && StringUtil.isNotBlank(express)) {
                s.append(" AND ");
            }else if(StringUtil.isNotBlank(express)){
                addAnd = true;
            }
            s.append(express);
        }
        return s.toString();
    }

    /**
     * 取对象属性值并转为字符串（用于sql_where拼接）
     *   字符串加单引号
     * @param field 字段
     * @param object 对象
     * @return 属性值字符串
     */
    public static String getFieldValueExpress(Field field,Object object){
        if(null == field){
            //如果数据表定义字段比对象类字段多，则多出来的不赋值，不抛错
            return null;
        }
        Object value = null;
        field.setAccessible(true);//设置可见性为true
        try {
            value = field.get(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(null == value) return "";

        //转为字符串
        DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if( value instanceof String){
            return StringUtil.camelCaseToUnderline(field.getName())+"="+"'"+value+"'";
        }else if(value instanceof LocalDate){
            LocalDate date = (LocalDate) value;
            return StringUtil.camelCaseToUnderline(field.getName())+"="+"'"+ date.format(fmtDate) +"'";
        }else if(value instanceof LocalDateTime){
            LocalDateTime date = (LocalDateTime) value;
            return StringUtil.camelCaseToUnderline(field.getName())+"="+"'"+ date.format(fmtDateTime) +"'";
        }else if(value instanceof Integer){
            return (Integer)value==0?"":StringUtil.camelCaseToUnderline(field.getName()) + "=" + String.valueOf(value);
        }else if(value instanceof Long){
            return (long)value==0?"":StringUtil.camelCaseToUnderline(field.getName()) + "=" + String.valueOf(value);
        }else if(value instanceof BigDecimal){
            return BigDecimal.ZERO.compareTo((BigDecimal)value)==0?"":StringUtil.camelCaseToUnderline(field.getName())+"="+String.valueOf(value);
        }
        return (String) StringUtil.camelCaseToUnderline(field.getName())+"="+value;
    }

    /**
     * 取对象属性名并转为字符串（用于sql_insert拼接）
     * @param object 对象
     * @param splitSign 分隔符
     * @return 属性值字符串
     */
    public static String getFieldNameString(Object object,String splitSign){
        StringBuffer s = new StringBuffer();

        List<Field> fields = ObjectUtil.getAllFieldList(object.getClass());
        boolean addSplitSign = false;
        for(Field field:fields) {
            //不处理静态变量和常量
            if ( Modifier.isStatic( field.getModifiers() ) || Modifier.isFinal( field.getModifiers() ) ){
                continue;
            }
            String fieldName = StringUtil.camelCaseToUnderline( field.getName() );
            if(addSplitSign && StringUtil.isNotBlank(fieldName)) {
                s.append(splitSign).append(" ");
            }else if(StringUtil.isNotBlank(fieldName)){
                addSplitSign = true;
            }
            s.append(fieldName);
        }
        return s.toString();
    }

    /**
     * 取对象属性值并转为字符串（用于sql_insert拼接）
     * @param object 对象
     * @param splitSign 分隔符
     * @return String
     */
    public static String getFieldValueString(Object object,String splitSign){
        StringBuffer s = new StringBuffer();

        List<Field> fields = ObjectUtil.getAllFieldList(object.getClass());
        boolean addSplitSign = false;
        for(Field field:fields) {
            //不处理静态变量和常量
            if ( Modifier.isStatic( field.getModifiers() ) || Modifier.isFinal( field.getModifiers() ) ){
                continue;
            }

            Object value = null;
            field.setAccessible(true);//设置可见性为true
            try {
                value = field.get(object);
            }catch (Exception e){
                e.printStackTrace();
            }

            //转为字符串
            String sValue = null;
            DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(null == value) {
                sValue = "null";
            }else if( value instanceof String){
                sValue = "'"+value+"'";
            }else if(value instanceof LocalDate){
                LocalDate date = (LocalDate) value;
                sValue = "'"+ date.format(fmtDate) +"'";
            }else if(value instanceof LocalDateTime){
                LocalDateTime date = (LocalDateTime) value;
                sValue = "'"+ date.format(fmtDateTime) +"'";
            }else if(value instanceof Integer){
                sValue = String.valueOf(value);
            }else if(value instanceof Long){
                sValue = String.valueOf(value);
            }else if(value instanceof BigDecimal){
                sValue = String.valueOf(value);
            }else {
                sValue = (String) value;
            }

            if(addSplitSign && StringUtil.isNotBlank(sValue.toString())) {
                s.append(" ").append(splitSign);
            }else if(StringUtil.isNotBlank(sValue.toString())){
                addSplitSign = true;
            }
            s.append( sValue );
        }
        return s.toString();
    }


}
