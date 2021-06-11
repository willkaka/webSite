package com.hyw.webSite.service;

import com.hyw.webSite.mapper.DataMapper;
import com.hyw.webSite.queryUtils.NQueryWrapper;
import com.hyw.webSite.queryUtils.NUpdateWrapper;
import com.hyw.webSite.utils.ClassUtil;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Jdbc数据操作服务
 */
@Slf4j
@Service
public class JDataService {
//
//    //************************新增记录********************************/
//    /**
//     * 新增记录
//     * @param object 对象
//     * @return 新增记录数
//     */
//    public <T> int save(Connection connection, T object) {
//        DbUtil.executeSql(connection,SqlUtil.getInsertSql(object));
//        return 1;
//    }
//
//    /**
//     * 新增记录
//     * @param objectList 对象
//     * @return 新增记录数
//     */
//    public <T> int save(Connection connection,List<T> objectList) {
//        DbUtil.executeSql(connection,SqlUtil.getInsertSql(objectList));
//        return objectList.size();
//    }
//
//    //************************删除记录********************************/
//    public <T> int delete(Connection connection,T object,String keyFieldName){
//        DbUtil.executeSql(connection,SqlUtil.getDeleteSql(object,keyFieldName));
//        return 1;
//    }
//
//    public <T> int deleteById(List<T> objectList,String keyFieldName){
//        List<String> sqlList = SqlUtil.getDeleteSql(objectList,keyFieldName);
//        int deleteCnt = 0;
//        for(String sql:sqlList){
//            deleteCnt = deleteCnt + dataMapper.deleteBySql(sql);
//        }
//        return deleteCnt;
//    }
//
//    //************************更新记录********************************/
//    public <T> int updateById(T object,String keyFieldName){
//        String sql = SqlUtil.getUpdateSql(object,keyFieldName);
//        return dataMapper.updateBySql(sql);
//    }
//
//    public <T> int updateById(List<T> objectList,String keyFieldName){
//        List<String> sqlList = SqlUtil.getUpdateSql(objectList,keyFieldName);
//        int updateCnt = 0;
//        for(String sql:sqlList){
//            updateCnt = updateCnt + dataMapper.updateBySql(sql);
//        }
//        return updateCnt;
//    }
//
//    public <T> int update(NUpdateWrapper<T> updateWrapper){
//        return dataMapper.updateBySql(updateWrapper.getSql());
//    }
//
//    //************************查询记录********************************/
//
//    public <T> T getOne(NQueryWrapper<T> queryWrapper){
//        List<T> rtnList = new ArrayList<>();
//        String sql = queryWrapper.getSql();
//        List<Map<String, Object>> list = dataMapper.queryBySql(sql);
//        for(Map<String, Object> map:list){
//            try {
//                Class<T> clazz = queryWrapper.getTableClass();
//                rtnList.add(ClassUtil.map2Object(map,clazz));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if(CollectionUtil.isEmpty(rtnList)){
//            return null;
//        }else{
//            return rtnList.get(0);
//        }
//    }
//
//    public <T> List<T> list(NQueryWrapper<T> queryWrapper){
//        List<T> rtnList = new ArrayList();
//        System.out.println(queryWrapper.getSql());
//        String sql = queryWrapper.getSql();
//        List<Map<String, Object>> list = dataMapper.queryBySql(sql);
//        for(Map<String, Object> map:list){
//            try {
//                Class<T> clazz = queryWrapper.getTableClass();
//                rtnList.add(ClassUtil.map2Object(map,clazz));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtnList;
//    }
}
