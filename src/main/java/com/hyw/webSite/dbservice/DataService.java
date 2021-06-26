package com.hyw.webSite.dbservice;

import com.hyw.webSite.mapper.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 通用表 服务实现类
 */
@Slf4j
@Service
public class DataService {

    @Autowired
    DataMapper dataMapper;

    //************************新增记录********************************/
    /**
     * 新增记录
     * @param object 对象
     * @return 新增记录数
     */
    public <T> int save(T object) {
        return dataMapper.saveBySql(JdbcUtil.getInsertSql(object));
    }

    /**
     * 新增记录
     * @param object 对象
     * @return 新增记录数
     */
    public <T> int save(Connection connection, T object) {
        return JdbcUtil.updateBySql(connection,JdbcUtil.getInsertSql(object));
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int save(List<T> objectList) {
        return dataMapper.saveBySql(JdbcUtil.getInsertSql(objectList));
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int save(Connection connection, List<T> objectList) {
        return JdbcUtil.updateBySql(connection,JdbcUtil.getInsertSql(objectList));
    }

    //************************删除记录********************************/
    public <T> int delete(T object,String keyFieldName){
        return dataMapper.deleteBySql(JdbcUtil.getDeleteSql(object,keyFieldName));
    }

    public <T> int delete(Connection connection,T object,String keyFieldName){
        return JdbcUtil.updateBySql(connection,JdbcUtil.getDeleteSql(object,keyFieldName));
    }

    public <T> int deleteById(List<T> objectList,String keyFieldName){
        List<String> sqlList = JdbcUtil.getDeleteSql(objectList,keyFieldName);
        int deleteCnt = 0;
        for(String sql:sqlList){
            deleteCnt = deleteCnt + dataMapper.deleteBySql(sql);
        }
        return deleteCnt;
    }

    public <T> int deleteById(Connection connection,List<T> objectList,String keyFieldName){
        return JdbcUtil.updateBySql(connection,JdbcUtil.getDeleteSql(objectList,keyFieldName));
    }

    public <T> int delete(NUpdateWrapper<T> nUpdateWrapper){
        if(nUpdateWrapper.getConnection() != null){
            return JdbcUtil.updateBySql(nUpdateWrapper.getConnection(),nUpdateWrapper.getSql());
        }else {
            System.out.println(nUpdateWrapper.getSql());
            return dataMapper.deleteBySql(nUpdateWrapper.getSql());
        }
    }

    //************************更新记录********************************/
    public <T> int updateById(T object,String keyFieldName){
        return dataMapper.updateBySql(JdbcUtil.getUpdateSql(object,keyFieldName));
    }

    public <T> int updateById(Connection connection,T object,String keyFieldName){
        return JdbcUtil.updateBySql(connection,JdbcUtil.getUpdateSql(object,keyFieldName));
    }

    public <T> int updateById(List<T> objectList,String keyFieldName){
        List<String> sqlList = JdbcUtil.getUpdateSql(objectList,keyFieldName);
        int updateCnt = 0;
        for(String sql:sqlList){
            updateCnt = updateCnt + dataMapper.updateBySql(sql);
        }
        return updateCnt;
    }

    public <T> int updateById(Connection connection,List<T> objectList,String keyFieldName){
        List<String> sqlList = JdbcUtil.getUpdateSql(objectList,keyFieldName);
        int updateCnt = 0;
        for(String sql:sqlList){
            updateCnt = updateCnt + JdbcUtil.updateBySql(connection,sql);
        }
        return updateCnt;
    }

    public <T> int update(NUpdateWrapper<T> updateWrapper){
        if(updateWrapper.getConnection() != null){
            return JdbcUtil.updateBySql(updateWrapper.getConnection(),updateWrapper.getSql());
        }else {
            System.out.println(updateWrapper.getSql());
            return dataMapper.updateBySql(updateWrapper.getSql());
        }
    }

    //************************查询记录********************************/

    public <T> T getOne(NQueryWrapper<T> queryWrapper){
        List<T> rtnList = new ArrayList<>();
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = new ArrayList<>();
        if(queryWrapper.getConnection() != null){
            list = JdbcUtil.getSqlRecords(queryWrapper.getConnection(),sql);
        }else {
            list = dataMapper.queryBySql(sql);
        }
        for(Map<String, Object> map:list){
            try {
                Class<T> clazz = queryWrapper.getTableClass();
                rtnList.add(QueryUtil.map2Object(map,clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(QueryUtil.isEmptyList(rtnList)){
            return null;
        }else{
            return rtnList.get(0);
        }
    }

    public <T> int count(NQueryWrapper<T> queryWrapper){
        String sql = queryWrapper.getCountSql();
        List<Map<String, Object>> list = new ArrayList<>();
        if(queryWrapper.getConnection() != null){
            list = JdbcUtil.getSqlRecords(queryWrapper.getConnection(),sql);
        }else {
            list = dataMapper.queryBySql(sql);
        }
        return (int) list.get(0).get("COUNT(1)");
    }

    public <T> List<T> list(NQueryWrapper<T> queryWrapper){
        List<T> rtnList = new ArrayList();
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = new ArrayList<>();
        if(queryWrapper.getConnection() != null){
            list = JdbcUtil.getSqlRecords(queryWrapper.getConnection(),sql);
        }else {
            list = dataMapper.queryBySql(sql);
        }
        for(Map<String, Object> map:list){
            try {
                Class<T> clazz = queryWrapper.getTableClass();
                rtnList.add(QueryUtil.map2Object(map,clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rtnList;
    }

    public <T> IPage listByPage(NQueryWrapper<T> queryWrapper){
        IPage<T> iPage = new IPage<>();

        int totalCnt = count(queryWrapper);
        List<T> rtnList = new ArrayList();
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = new ArrayList<>();
        if(queryWrapper.getConnection() != null){
            list = JdbcUtil.getSqlRecords(queryWrapper.getConnection(),sql);
        }else {
            list = dataMapper.queryBySql(sql);
        }
        for(Map<String, Object> map:list){
            try {
                Class<T> clazz = queryWrapper.getTableClass();
                rtnList.add(QueryUtil.map2Object(map,clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        iPage.setRecords(rtnList);
        iPage.setTotalCnt(totalCnt);
        iPage.setCurPage(queryWrapper.getCurPage()+1);
        iPage.setCurRecord(queryWrapper.getCurRecord()*queryWrapper.getPageSize()+rtnList.size());
        iPage.setPageSize(queryWrapper.getPageSize());
        return iPage;
    }
}
