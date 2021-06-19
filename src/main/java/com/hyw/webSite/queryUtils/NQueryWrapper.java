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
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Accessors(chain = true)
public class NQueryWrapper<T> {

    private Connection connection;
    private Map<String,Class<T>> classMap = new HashMap<>();
    private List<String> selectFieldList = new ArrayList<>();
    private List<UpdCondition> conditionList = new ArrayList<>();
    private List<Field> classFieldList = new ArrayList<>();
    private List<GroupInfo> groupInfoList = new ArrayList<>();
    private List<OrderInfo> orderInfoList = new ArrayList<>();

    private int totalCnt=0;  //总记录
    private int pageSize=0;  //每页记录数
    private int totalPage=0; //总页数
    private int curRecord=0; //当前记录号
    private int curPage=0;   //当前页数

    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        int condIndex=0;
        if(CollectionUtil.isNotEmpty(selectFieldList)) {
            for (String fieldName : selectFieldList) {
                if (condIndex > 0) sql.append(", ");
                sql.append(fieldName);
                condIndex ++;
            }
        }else {
            sql.append("*");
        }
        sql.append(" FROM ");

        int mapIndex=0;
        // From tables
        for(String tableName:classMap.keySet()){
            if(mapIndex>0) sql.append(",");
            sql.append(StringUtil.camelCaseToUnderline(tableName));
            mapIndex++;
        }

        // Where conditions
        if(CollectionUtil.isNotEmpty(conditionList)) sql.append(" WHERE ");
        condIndex=0;
        for(UpdCondition condition:conditionList){
            if(condIndex>0) sql.append(" AND ");
            sql.append(StringUtil.camelCaseToUnderline(condition.getColumn().toString()))
                    .append(" ").append(condition.getSymbol())
                    .append(" ").append(condition.getValue());
            condIndex++;
        }

        // Group By
        condIndex=0;
        if(CollectionUtil.isNotEmpty(groupInfoList)) sql.append(" GROUP BY ");
        for(GroupInfo orderInfo:groupInfoList){
            if(condIndex>0) sql.append(", ");
            sql.append(orderInfo.getColumn().toString());
            condIndex++;
        }

        // Order By
        condIndex=0;
        if(CollectionUtil.isNotEmpty(orderInfoList)) sql.append(" ORDER BY ");
        for(OrderInfo orderInfo:orderInfoList){
            if(condIndex>0) sql.append(", ");
            sql.append(orderInfo.getColumn().toString())
                    .append(" ").append(orderInfo.getOrderKey());
            condIndex++;
        }

        if(pageSize>0){
            sql.append(" LIMIT ").append((curPage==0?1:curPage-1)*pageSize).append(",").append(pageSize);
        }

        return sql.toString();
    }

    public String getCountSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("COUNT(1)");
        sql.append(" FROM ");

        int mapIndex=0;
        // From tables
        for(String tableName:classMap.keySet()){
            if(mapIndex>0) sql.append(",");
            sql.append(StringUtil.camelCaseToUnderline(tableName));
            mapIndex++;
        }

        // Where conditions
        if(CollectionUtil.isNotEmpty(conditionList)) sql.append(" WHERE ");
        int condIndex=0;
        for(UpdCondition condition:conditionList){
            if(condIndex>0) sql.append(" AND ");
            sql.append(StringUtil.camelCaseToUnderline(condition.getColumn().toString()))
                    .append(" ").append(condition.getSymbol())
                    .append(" ").append(condition.getValue());
            condIndex++;
        }

        // Group By
        condIndex=0;
        if(CollectionUtil.isNotEmpty(groupInfoList)) sql.append(" GROUP BY ");
        for(GroupInfo orderInfo:groupInfoList){
            if(condIndex>0) sql.append(", ");
            sql.append(orderInfo.getColumn().toString());
            condIndex++;
        }
        return sql.toString();
    }

    @SafeVarargs
    public final <A,B> NQueryWrapper<T> selectFields(SFunction<A, B>... functions) {
        for (SFunction<A, B> function : functions) {
            SerializedLambda serializedLambda = ClassUtils.resolve(function);
            selectFieldList.add(StringUtil.camelCaseToUnderline(serializedLambda.getImplMethodName().replace("get", "")));
        }
        return this;
    }

    public final <A,B> NQueryWrapper<T> selectFields(String userDefinedField) {
        selectFieldList.add(userDefinedField);
        return this;
    }

    public <A,B> NQueryWrapper<T> eq(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> eq(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.camelCaseToUnderline(putTable(function1).getImplMethodName().replace("get","")),"=",
                StringUtil.camelCaseToUnderline(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> ne(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<>",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> ne(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<>",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> gt(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),">",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> gt(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),">",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> ge(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),">=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> ge(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),">=",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> lt(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<",value);
        return this;
    }

    public <A,B> NQueryWrapper<T> lt(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> le(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"<=",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> le(SFunction<A, B> function1, SFunction<A, B> function2) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function1).getImplMethodName().replace("get","")),"<=",
                StringUtil.underlineToCamelCase(putTable(function2).getImplMethodName().replace("get","")));
        return this;
    }

    public <A,B> NQueryWrapper<T> in(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"in",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> notIn(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"not in",getValue(value));
        return this;
    }

    public <A,B> NQueryWrapper<T> like(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NQueryWrapper<T> likeRight(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue(String.valueOf(value)+"%"));
        return this;
    }

    public <A,B> NQueryWrapper<T> likeLeft(SFunction<A, B> function, Object value) {
        putCondition(StringUtil.underlineToCamelCase(putTable(function).getImplMethodName().replace("get","")),"like",
                getValue("%"+String.valueOf(value)));
        return this;
    }

    @SafeVarargs
    public final <A,B> NQueryWrapper<T> groupBy(SFunction<A, B>... functions) {
        for (SFunction<A, B> function : functions) {
            SerializedLambda serializedLambda = ClassUtils.resolve(function);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setSeq(groupInfoList.size() + 1);
            groupInfo.setColumn(StringUtil.camelCaseToUnderline(serializedLambda.getImplMethodName().replace("get", "")));
            groupInfoList.add(groupInfo);
        }
        return this;
    }

    public <A,B> NQueryWrapper<T> orderByAsc(SFunction<A, B> function) {
        SerializedLambda serializedLambda = ClassUtils.resolve(function);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setSeq(orderInfoList.size()+1);
        orderInfo.setOrderKey("Asc");
        orderInfo.setColumn(StringUtil.camelCaseToUnderline(serializedLambda.getImplMethodName().replace("get","")));
        orderInfoList.add(orderInfo);
        return this;
    }

    public <A,B> NQueryWrapper<T> orderByDesc(SFunction<A, B> function) {
        SerializedLambda serializedLambda = ClassUtils.resolve(function);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setSeq(orderInfoList.size()+1);
        orderInfo.setOrderKey("Desc");
        orderInfo.setColumn(StringUtil.camelCaseToUnderline(serializedLambda.getImplMethodName().replace("get","")));
        orderInfoList.add(orderInfo);
        return this;
    }

    public NQueryWrapper<T> setTable(String tableName){
        classMap.put(tableName,null);
        return this;
    }




    private void putCondition(String fieldName,String symbol,Object value){
        UpdCondition condition = new UpdCondition();
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

@Setter
@Getter
@Accessors(chain = true)
class GroupInfo{
    private int seq;
    private Object column;
}

@Setter
@Getter
@Accessors(chain = true)
class OrderInfo{
    private int seq;
    private Object column;
    private String orderKey; //Asc/Desc
}