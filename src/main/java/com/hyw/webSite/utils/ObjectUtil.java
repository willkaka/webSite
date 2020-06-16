package com.hyw.webSite.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class ObjectUtil {

    /**
     * 判断是否为字符串
     * @param object 输入对象
     * @return 是字符串为true
     */
    public static boolean isString(Object object){
        return object instanceof String;
    }

    public static boolean isBigDecimal(Object object){
        return object instanceof BigDecimal;
    }

    public static boolean isLocalDate(Object object){
        return object instanceof LocalDate;
    }

    public static boolean isInteger(Object object){
        return object instanceof Integer;
    }

    public static boolean isDate(Object object){
        return object instanceof Date;
    }



    /**
     * 取对象的所有属性（包括父类属性）
     * @param clazz 对象
     * @return Map<String, Field>
     */
    public static Map<String, Field> getAllFieldMap(Class<?> clazz){
        Map<String, Field> fieldMap = new HashMap<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {//向上循环  遍历父类
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                fieldMap.put(field.getName(),field);
            }
        }
        return fieldMap;
    }

    /**
     * 取对象的所有属性（包括父类属性）
     * @param clazz 对象
     * @return List<Field>
     */
    public static List<Field> getAllFieldList(Class<?> clazz){
        List<Field> fieldList = new ArrayList<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {//向上循环  遍历父类
            Field[] fields = clazz.getDeclaredFields();
            Collections.addAll(fieldList, fields);
        }
        return fieldList;
    }

    /**
     * 将传入object转为同类型的对象
     * @param object 对象
     * @param clazz 对象类型
     * @return 转换后的对象
     */
    public static Object dataTypeConvert(Object object,Class<?> clazz){
        if(null != object && object.getClass().equals(clazz)) return object;

        //Date 转为 LocalDate
        if (object instanceof Date && clazz.getName().equals("java.time.LocalDate")) {
            Date date = (Date) object;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            //Timestamp 转为 LocalDateTime
        }else if (object instanceof Timestamp && clazz.getName().equals("java.time.LocalDateTime")) {
            Timestamp timestamp = (Timestamp) object;
            return timestamp.toLocalDateTime();
        }
        return object;
    }

    /**
     * @Description: 获取对象 属性值
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        if(object == null) {
            return null;
        }
        Class<? extends Object> class1 = object.getClass();
        Object objectValue = null;
        Field field = null;
        try {
            field = class1.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            objectValue = field.get(object);
        }catch (Exception e) {
            log.error("对象{} 获取属性值：{} 失败", object,fieldName);
        }
        return objectValue;
    }

    /**
     * Object转为Map
     * @param object 对象
     * @return Map<String,Object>
     */
    public static Map<String,Object> object2Map(Object object){
        Map<String,Object> map = new HashMap<>();
        if (Objects.isNull(object)) { return map; }

        List<Field> fields = ObjectUtil.getAllFieldList(object.getClass());
        for(Field field:fields){
            Object value = ObjectUtil.getFieldValue(object,field.getName());
            map.put(field.getName(),value);
        }
        return map;
    }
}
