package com.hyw.webSite.utils;

import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class ClassUtil {

    public static Object getInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException({})",className, e);
            throw new BizException("");
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException({})",className, e);
            throw new BizException("");
        } catch (InstantiationException e) {
            log.error("InstantiationException({})",className, e);
            throw new BizException("");
        }
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
        //map的key
        for (String o : map.keySet() ) {
            //map的key对应的属性
            Field declaredField = clazz.getDeclaredField( StringUtil.underlineToCamelCase(o) );
            //设置可见性为true
            declaredField.setAccessible(true);
            //setter
            declaredField.set(t, map.get(o));
        }

        return t;
    }
}
