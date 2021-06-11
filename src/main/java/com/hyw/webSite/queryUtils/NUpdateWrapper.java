package com.hyw.webSite.queryUtils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.ObjectUtil;
import com.hyw.webSite.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Accessors(chain = true)
public class NUpdateWrapper<T> {

    private Map<String,Class<T>> classMap = new HashMap<>();
    private Map<String,Object> setMap = new HashMap<>();
    private List<UpdCondition> updConditionList = new ArrayList<>();
    private List<Field> classFieldList = new ArrayList<>();

    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        int mapIndex=0;
        for(String tableName:classMap.keySet()){
            if(mapIndex>0) sql.append(",");
            sql.append(StringUtil.camelCaseToUnderline(tableName));
            mapIndex++;
        }
        mapIndex=0;
        if(CollectionUtil.isNotEmpty(setMap)){
            sql.append(" SET ");
            for(String setFieldName:setMap.keySet()){
                if(mapIndex>0) sql.append(",");
                sql.append(StringUtil.camelCaseToUnderline(setFieldName)).append("=")
                .append(setMap.get(setFieldName));
                mapIndex++;
            }
        }
        if(CollectionUtil.isNotEmpty(updConditionList)) sql.append(" WHERE ");
        int condIndex=0;
        for(UpdCondition updCondition : updConditionList){
            if(condIndex>0) sql.append(" AND ");
            sql.append(StringUtil.camelCaseToUnderline(updCondition.getColumn().toString()))
                    .append(" ").append(updCondition.getSymbol())
                    .append(" ").append(updCondition.getValue());
            condIndex++;
        }
        return sql.toString();
    }

    public <A,B> NUpdateWrapper<T> set(SFunction<A, B> function, Object value) {
        setMap.put(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),
                getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> set(SFunction<A, B> function1, SFunction<A, B> function2) {
        setMap.put(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),
                StringUtil.camelCaseToUnderline(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> eq(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"=",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> eq(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"=",
                StringUtil.camelCaseToUnderline(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> ne(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<>",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> ne(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<>",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> gt(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),">",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> gt(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),">",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> ge(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),">=",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> ge(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),">=",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> lt(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<",value);
        return this;
    }

    public <A,B> NUpdateWrapper<T> lt(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> le(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<=",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> le(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<=",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NUpdateWrapper<T> in(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"in",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> notIn(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"not in",getValue(value));
        return this;
    }

    public <A,B> NUpdateWrapper<T> like(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NUpdateWrapper<T> likeRight(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue(String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NUpdateWrapper<T> likeLeft(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)));
        return this;
    }

    private void putCondition(String fieldName,String symbol,Object value){
        UpdCondition updCondition = new UpdCondition();
        updCondition.setColumn(fieldName).setSymbol(symbol).setValue(value);
        updConditionList.add(updCondition);
    }

    private <A,B> SerializedLambda putTable(SFunction<A, B> function){
        SerializedLambda serializedLambda = ClassUtils.resolve(function);
        Class clazz = serializedLambda.getImplClass();
        String clazzName = clazz.getSimpleName();
        if(!classMap.containsKey(clazzName)){
            classMap.put(clazzName,clazz);
        }
        return serializedLambda;
    }

    private void getClassFieldInfo(){
        classMap.forEach((tableName,clazz)->{
            classFieldList.addAll(ObjectUtil.getAllFieldList(clazz));
        });
    }


    private String getValue(Object value){
        if(value instanceof Integer){
            return String.valueOf(value);
        }else if(value instanceof String){
            return "'"+String.valueOf(value)+"'";
        }else if(value instanceof BigDecimal){
            BigDecimal num = (BigDecimal) value;
            return String.valueOf(value);
        }else if(value instanceof Date){
            return "'"+String.valueOf(value)+"'";
        }else if(value instanceof LocalDate){
            return "'"+String.valueOf(value)+"'";
        }else if(value instanceof LocalDateTime){
            return "'"+String.valueOf(value)+"'";
        }else{
            return "'"+String.valueOf(value)+"'";
        }
    }

    public Class<T> getTableClass(){
        for(String name:classMap.keySet()){
            return classMap.get(name);
        }
        return null;
    }
}

@Setter
@Getter
@Accessors(chain = true)
class UpdCondition {
    private Object column;
    private String symbol;
    private Object value;
}
