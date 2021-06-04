package com.hyw.webSite.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hyw.webSite.mapper.DataMapper;
import com.hyw.webSite.queryUtils.NQueryWrapper;
import com.hyw.webSite.utils.ClassUtil;
import com.hyw.webSite.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * <p>
 * 通用表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class DataService {

    @Autowired
    DataMapper dataMapper;

    //************************新增记录********************************/
    /**
     * 新增记录
     * @param sql sql
     * @return 新增记录数
     */
    public int insert(String sql) {
        return dataMapper.saveBySql(sql);
    }

    /**
     * 新增记录
     * @param object 对象
     * @return 新增记录数
     */
    public int insert(Object object) {
        return dataMapper.saveBySql(SqlUtil.getInsertSql(object));
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int insert(List<T> objectList) {
        return dataMapper.saveBySql(SqlUtil.getInsertSql(objectList));
    }

    //************************删除记录********************************/
    public int delete(Object object,String keyFieldName){
        return dataMapper.deleteBySql(SqlUtil.getDeleteSql(object,keyFieldName));
    }

    public <T> int deleteById(List<T> objectList,String keyFieldName){
        List<String> sqlList = SqlUtil.getDeleteSql(objectList,keyFieldName);
        int deleteCnt = 0;
        for(String sql:sqlList){
            deleteCnt = deleteCnt + dataMapper.deleteBySql(sql);
        }
        return deleteCnt;
    }

    //************************更新记录********************************/
    public int update(UpdateWrapper updateWrapper){
        String sql = updateWrapper.getTargetSql();
        return dataMapper.updateBySql(sql);
    }

    public <T> int updateById(T object,String keyFieldName){
        String sql = SqlUtil.getUpdateSql(object,keyFieldName);
        return dataMapper.updateBySql(sql);
    }

    public <T> int updateById(List<T> objectList,String keyFieldName){
        List<String> sqlList = SqlUtil.getUpdateSql(objectList,keyFieldName);
        int updateCnt = 0;
        for(String sql:sqlList){
            updateCnt = updateCnt + dataMapper.updateBySql(sql);
        }
        return updateCnt;
    }

    //************************查询记录********************************/
    public <T> T getOne(LambdaQueryWrapper<T> queryWrapper){
        String sql = queryWrapper.getCustomSqlSegment();
        List<Map<String, Object>> list = dataMapper.queryBySql(sql);
        for(Map<String, Object> map:list){
            try {
                return (T) ClassUtil.map2Object(map,queryWrapper.getEntity().getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    public <T> List<T> list(QueryWrapper<T> queryWrapper){
//        List<T> rtnList = new ArrayList();
//        if(null==queryWrapper) queryWrapper = new QueryWrapper<T>();
//        if(StringUtil.isBlank(queryWrapper.getSqlSelect())) queryWrapper.select("*");
//        List<Map<String, Object>> list = dataMapper.query(queryWrapper.getEntity().getClass().getSimpleName(),queryWrapper);
//        for(Map<String, Object> map:list){
//            try {
//                rtnList.add((T) ClassUtil.map2Object(map,queryWrapper.getEntityClass().getClass()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtnList;
//    }

    public <T> List<T> list(QueryWrapper<T> queryWrapper){
//        QueryWrapper chldQueryWrapper = new QueryWrapper<T>() { };
//        Class clazz = queryWrapper.getClass();
//        Type type = clazz.getGenericSuperclass();
//        ParameterizedType parameterizedType = (ParameterizedType) type;
//        Type[] types = parameterizedType.getActualTypeArguments();
//        Class<T> clazzName = (Class<T>) types[0];

        List<T> rtnList = new ArrayList();
//        if(null==queryWrapper) queryWrapper = new QueryWrapper<T>();
//        if(StringUtil.isBlank(queryWrapper.getSqlSelect())) queryWrapper.select("*");
//        List<Map<String, Object>> list = dataMapper.query(queryWrapper.getEntity().getClass().getSimpleName(),queryWrapper);
//        for(Map<String, Object> map:list){
//            try {
//                rtnList.add((T) ClassUtil.map2Object(map,queryWrapper.getEntityClass().getClass()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return rtnList;
    }


    public <T> List<T> list(NQueryWrapper<T> queryWrapper){
        List<T> rtnList = new ArrayList();
        System.out.println(queryWrapper.getSql());
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = dataMapper.queryBySql(sql);
        for(Map<String, Object> map:list){
            try {
                Class<T> clazz = queryWrapper.getTableClass();
                rtnList.add(ClassUtil.map2Object(map,clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rtnList;
    }


    public <T> QueryWrapper<T> createQuery() {
        return new QueryWrapper<T>() {

        };
    }


    public <A,B> Function<A, B> createQuery(Function<A, B> function) {
        return function;
    }


}
