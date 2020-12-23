package com.hyw.webSite.utils;

import com.hyw.webSite.model.FieldAttr;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CollectionUtil {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !(collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !(map == null || map.isEmpty());
    }


    /**
     * 获取map中第一个数据值
     *
     * @param map 数据源
     * @return 第一个数据值
     */
    public static String getMapFirstOrNull(Map<String, String> map) {
        String obj = null;
        if(null == map) return obj;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getValue();
            break;
        }
        return obj;
    }

    /**
     * 复制独立的map
     * @param mapA
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Map<Object,T> cloneMap(Map<Object,T> mapA, Class<T> clazz) throws Exception {
        Map<Object,T> mapB = new HashMap<>();
        for(Object key:mapA.keySet()){
            T b = clazz.newInstance();
            T a = mapA.get(key);
            Field[] fields = clazz.getDeclaredFields();        //获取实体类的所有属性，返回Field数组
            for (Field field : fields) {     //遍历所有属性
                String fieldName = field.getName();    //获取属性的名字
                Field declaredField = clazz.getDeclaredField(fieldName);

                String getFieldName = "get" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1); //将属性的首字符大写，方便构造get，set方法
                String type = field.getGenericType().toString();    //获取属性的类型
                Method m;
                if("class java.lang.Boolean".equals(type) || "boolean".equals(type)){
                    m = clazz.getMethod(fieldName);
                }else{
                    m = clazz.getMethod(getFieldName);
                }

                //设置可见性为true
                declaredField.setAccessible(true);
                //setter
                declaredField.set(b, m.invoke(a));
            }
            mapB.put(key,b);
        }
        return mapB;
    }

    /**
     * 排序
     * @param list
     */
    public static void sort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null || o2 == null) { return -1; }
                if (o1.length() > o2.length()) { return 1; }
                if (o1.length() < o2.length()) { return -1; }
                if (o1.compareTo(o2) > 0) { return 1; }
                if (o1.compareTo(o2) < 0) { return -1; }
                if (o1.compareTo(o2) == 0) { return 0; }
                return 0;
            }
        });
    }

    /**
     * map转object
     * @param map 字段值map
     * @param clazz 对象类
     * @return 对象
     * @throws Exception
     */
    public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) throws Exception {
        T t = clazz.newInstance();
        Map<String, Field> fieldMap = ObjectUtil.getAllFieldMap(clazz);
        //map的key
        for (String s : map.keySet() ) {
            //map的key对应的属性
            Field declaredField = fieldMap.get(StringUtil.underlineToCamelCase(s));
            if(null == declaredField){
                //如果数据表定义字段比对象类字段多，则多出来的不赋值，不抛错
                continue;
            }
            declaredField.setAccessible(true);//设置可见性为true
            declaredField.set(t, ObjectUtil.dataTypeConvert(map.get(s),declaredField.getType()));//setter
        }

        return t;
    }

}
