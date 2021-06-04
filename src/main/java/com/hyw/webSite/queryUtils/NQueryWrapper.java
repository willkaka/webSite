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
public class NQueryWrapper<T> {

    private Map<String,Class<T>> classMap = new HashMap<>();
    private List<Condition> conditionList = new ArrayList<>();
    private List<Field> classFieldList = new ArrayList<>();

    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        int mapIndex=0;
        for(String tableName:classMap.keySet()){
            if(mapIndex>0) sql.append(",");
            sql.append(StringUtil.camelCaseToUnderline(tableName));
            mapIndex++;
        }
        if(CollectionUtil.isNotEmpty(conditionList)) sql.append(" WHERE ");
        int condIndex=0;
        for(Condition condition:conditionList){
            if(condIndex>0) sql.append(" AND ");
            sql.append(StringUtil.camelCaseToUnderline(condition.getColumn().toString()))
                    .append(" ").append(condition.getSymbol())
                    .append(" ").append(condition.getValue());
            condIndex++;
        }
        return sql.toString();
    }

    public <A,B> NQueryWrapper<T> eq(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> ne(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"<>",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> gt(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),">",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> ge(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),">=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> lt(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"<",value);
        return this;
    }

    public <A,B> NQueryWrapper<T> le(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"<=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> in(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"in",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> notIn(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"not in",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> like(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NQueryWrapper<T> likeRight(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"like",
                getValue(String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NQueryWrapper<T> likeLeft(SFunction<A, B> function, Object value) {
        SerializedLambda serializedLambda = putTable(function);
        putCondition(StringUtil.underlineToCamelCase(serializedLambda.getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)));
        return this;
    }

    private void putCondition(String fieldName,String symbol,Object value){
        Condition condition = new Condition();
        condition.setColumn(fieldName).setSymbol(symbol).setValue(value);
        conditionList.add(condition);
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
class Condition{
    private Object column;
    private String symbol;
    private Object value;
}
