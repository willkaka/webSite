package com.hyw.webSite.dbservice;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.dbservice.constant.DbConstant;
import com.hyw.webSite.dbservice.dto.*;
import com.hyw.webSite.dbservice.exception.DbException;
import com.hyw.webSite.dbservice.utils.JdbcUtil;
import com.hyw.webSite.dbservice.utils.QFunction;
import com.hyw.webSite.dbservice.utils.QueryUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;


/**
 * 通用表 服务实现类
 */
@Slf4j
@Service
public class DataService {

    @Autowired
    private DataSource dataSource;

    private static Connection springConnection;

    private static List<TransactionalInfo> transactionalInfoList = new ArrayList<>();

    public static List<TransactionalInfo> getTransactionalInfoList(){
        return transactionalInfoList;
    }

    //************************数据库********************************/
    /**
     * 获取spring数据库连接
     * @return Connection 数据库连接
     */
    private Connection getSpringDatabaseConnection(){
        if(springConnection==null) {
            try {
                springConnection = dataSource.getConnection();
            } catch (Exception e) {
                log.error("取系统数据库连接异常！", e);
                throw new DbException("取系统数据库连接异常！");
            }
        }
        return springConnection;
    }

    public Connection getDatabaseConnection(){
        Connection connection = getSpringDatabaseConnection();
        saveTransactionInfo(connection);
        return connection;
    }

    private void saveTransactionInfo(Connection connection){
        if(QueryUtil.isEmptyList(transactionalInfoList)) return;
        List<Method> methodList = ThreadUtil.getLastCallMethodList(Thread.currentThread().getStackTrace());
        for(TransactionalInfo transactionalInfo:transactionalInfoList){
            Method trxMethod = transactionalInfo.getMethod();
            for(Method method:methodList){
                if(Objects.equals(method,trxMethod)){
                    try {
                        connection.setAutoCommit(false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    transactionalInfo.getConnectionList().add(connection);
                    break;
                }
            }
        }
    }
    /**
     * 按传入的db名称取db连接
     * @param dbName    db名称
     * @param libName   数据库名称
     * @return Connection 数据库连接
     */
    public Connection getDatabaseConnection(String dbName, String libName){
        Connection connection;
        if(DbConstant.DB_SOURCE_SYS.equals(dbName)) {
            connection = getDatabaseConnection();
        }else {
            ConfigDatabaseInfo configDatabaseInfo = this.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                    .eq(ConfigDatabaseInfo::getDatabaseName, dbName));
            configDatabaseInfo.setDatabaseLabel(libName);
            connection = DbUtil.getConnection(configDatabaseInfo);
        }
        saveTransactionInfo(connection);
        return connection;
    }

    public void closeConnection(Connection connection){
        try{
            if(connection != null) connection.close();
        }catch (Exception e){
            log.error("关闭数据库连接出错！",e);
        }
    }

    public String getDatabaseType(){
        String dbType="";
        String driverName="";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            driverName = connection.getMetaData().getDriverName();
        }catch (Exception e){
            throw new DbException("取系统数据库连接异常！");
        }
        if(driverName.toLowerCase().contains("sqlite")) dbType= DbConstant.DB_TYPE_SQLITE;
        if(driverName.toLowerCase().contains("oracle")) dbType= DbConstant.DB_TYPE_ORACLE;
        if(driverName.toLowerCase().contains("mysql")) dbType= DbConstant.DB_TYPE_MYSQL;
        return dbType;
    }

    public String getDatabaseType(Connection connection){
        String dbType="";
        String driverName="";
        try {
            driverName = connection.getMetaData().getDriverName();
        }catch (Exception e){
            throw new DbException("取系统数据库连接异常！");
        }
        if(driverName.toLowerCase().contains("sqlite")) dbType= DbConstant.DB_TYPE_SQLITE;
        if(driverName.toLowerCase().contains("oracle")) dbType= DbConstant.DB_TYPE_ORACLE;
        if(driverName.toLowerCase().contains("mysql")) dbType= DbConstant.DB_TYPE_MYSQL;
        return dbType;
    }

    //************************新增记录********************************/
    /**
     * 新增记录
     * @param object 对象
     * @return 新增记录数
     */
    public <T> int save(T object) {
        return save(getDatabaseConnection(),object);
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
        return save(getDatabaseConnection(),objectList);
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int save(Connection connection, List<T> objectList) {
        return JdbcUtil.updateBySql(connection,JdbcUtil.getInsertSql(objectList));
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int saveBatch(List<T> objectList,int size) {
        int count = 0;
        List<T> newRecords = new ArrayList<>();
        for(T object:objectList){
            newRecords.add(object);
            if(newRecords.size()>=size) {
                count = count + save(getDatabaseConnection(),newRecords);
                newRecords.clear();
            }
        }
        if(newRecords.size()>0)
            count = count + save(getDatabaseConnection(),newRecords);
        return count;
    }


    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    @Transactional
    public <T> int saveBatchCommit(List<T> objectList,int size) {
        int count = 0;
        List<T> newRecords = new ArrayList<>();
        for(T object:objectList){
            newRecords.add(object);
            if(newRecords.size()>=size) {
                count = count + save(getDatabaseConnection(),newRecords);
                newRecords.clear();
            }
        }
        if(newRecords.size()>0)
            count = count + save(getDatabaseConnection(),newRecords);
        return count;
    }

    /**
     * 新增记录
     * @param objectList 对象
     * @return 新增记录数
     */
    public <T> int saveBatch(Connection connection, List<T> objectList,int size) {
        int count = 0;
        List<T> newRecords = new ArrayList<>();
        for(T object:objectList){
            newRecords.add(object);
            if(newRecords.size()>=size) {
                count = count + JdbcUtil.updateBySql(connection,JdbcUtil.getInsertSql(newRecords));
                newRecords.clear();
            }
        }
        if(newRecords.size()>0)
            count = count + JdbcUtil.updateBySql(connection,JdbcUtil.getInsertSql(newRecords));
        return count;
    }

    //************************删除记录********************************/
    public <T,B> int delete(T object, QFunction<T, B> function){
        String keyFieldName = QueryUtil.toUnderlineStr(QueryUtil.getImplMethodName(function).replace("get", ""));
        return delete(getDatabaseConnection(),object,keyFieldName);
    }

    public <T> int delete(T object,String keyFieldName){
        return delete(getDatabaseConnection(),object,keyFieldName);
    }

    public <T> int delete(Connection connection,T object,String keyFieldName){
        return JdbcUtil.updateBySql(connection,JdbcUtil.getDeleteSql(object,keyFieldName));
    }

    public <T> int deleteById(List<T> objectList,String keyFieldName){
        if(QueryUtil.isEmptyList(objectList)) throw new DbException("objectList为空！");
        int deleteCnt = 0;
        for(T object:objectList){
            deleteCnt = deleteCnt + delete(object,keyFieldName);
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
            return JdbcUtil.updateBySql(getDatabaseConnection(),nUpdateWrapper.getSql());
        }
    }

    //************************更新记录********************************/
    public <T,B> int update(T object,QFunction<T, B> function){
        String fieldName = QueryUtil.getImplMethodName(function).replace("get", "");
        return updateById(getDatabaseConnection(),object,fieldName);
    }

    public <T> int updateById(T object,String keyFieldName){
        return updateById(getDatabaseConnection(),object,keyFieldName);
    }

    public <T> int updateById(Connection connection,T object,String keyFieldName){
        return JdbcUtil.updateBySql(connection,JdbcUtil.getUpdateSql(object,keyFieldName));
    }

    public <T> int updateById(List<T> objectList,String keyFieldName){
        if(QueryUtil.isEmptyList(objectList)) throw new DbException("objectList为空！");
        int updateCnt = 0;
        for(T object:objectList){
            updateCnt = updateCnt + updateById(object,keyFieldName);
        }
        return updateCnt;
    }

    public <T> int updateById(Connection connection,List<T> objectList,String keyFieldName){
        List<String> sqlList = JdbcUtil.getUpdateSql(objectList,keyFieldName);
        if(QueryUtil.isEmptyList(sqlList)) throw new DbException("生成的sql为空！");
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
            return JdbcUtil.updateBySql(getDatabaseConnection(),updateWrapper.getSql());
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
            list = JdbcUtil.getSqlRecords(getDatabaseConnection(),sql);
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
        List<Map<String, Object>> list = JdbcUtil.getSqlRecords(queryWrapper.getConnection()==null?
                getDatabaseConnection():queryWrapper.getConnection(),sql);
        if(QueryUtil.isEmptyList(list)) return 0;//throw new DbException("sql("+sql+")查询无记录！");
        return (int) list.get(0).get("COUNT(1)");
    }

    public <T> List<Map<String, Object>> mapList(NQueryWrapper<T> queryWrapper){
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = JdbcUtil.getSqlRecords(queryWrapper.getConnection()==null?
                getDatabaseConnection():queryWrapper.getConnection(),sql);
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(NQueryWrapper<T> queryWrapper){
        List<T> rtnList = new ArrayList();
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = JdbcUtil.getSqlRecords(queryWrapper.getConnection()==null?
                getDatabaseConnection():queryWrapper.getConnection(),sql);
        for(Map<String, Object> map:list){
            try {
                Class<T> clazz = queryWrapper.getTableClass();
                rtnList.add(QueryUtil.map2Object(map,clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        log.info(sql+",查询记录数："+rtnList.size());
        return rtnList;
    }

    public <T> IPage listByPage(NQueryWrapper<T> queryWrapper){
        IPage<T> iPage = new IPage<>();

        int totalCnt = count(queryWrapper);
        List<T> rtnList = new ArrayList();
        String sql = queryWrapper.getSql();
        List<Map<String, Object>> list = JdbcUtil.getSqlRecords(queryWrapper.getConnection()==null?
                getDatabaseConnection():queryWrapper.getConnection(),sql);
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

    //************************查询数据表定义********************************/
//    //数据库清单
//    public List<String> getMysqlDatabase(){
//        return dataMapper.getMysqlDatabase();
//    }
//
//    //数据表清单
//    public List<String> getMysqlTableList(){
//        return dataMapper.getMysqlTableList();
//    }
//
//    //数据表信息
//    public List<MysqlTableInfo> getMysqlTableInfo(String database){
//        return dataMapper.getMysqlTableInfo(database);
//    }
//
//    //数据表结构
//    public List<MysqlColumnInfo> getMysqlColumnInfo(String tableName){
//        return dataMapper.getMysqlColumnInfo(tableName);
//    }
//
//    //create table
//    public <T> String getTableDdlStr(Class<T> clazz){
//        String tableName = QueryUtil.toUnderlineStr(clazz.getSimpleName());
//        String dbType = getDatabaseType();
//        if(dbType.contains("sqlite")) {
//            return dataMapper.getSqliteTableDdl(tableName); //dbType= Constant.DB_TYPE_SQLITE;
//        }else if(dbType.contains("oracle")) {
//            return null;//dbType= Constant.DB_TYPE_ORACLE;
//        }else if(dbType.contains("mysql")) {
//            return dataMapper.getMysqlTableDdl(tableName); //dbType= Constant.DB_TYPE_MYSQL;
//        }else{
//            throw new DbException("暂不支持当前数据库"+dbType);
//        }
//    }

    public <T> List<TableFieldInfo> getTableFieldList(Class<T> clazz){
        Connection connection = getDatabaseConnection();
        return getTableFieldList(clazz,connection);
    }

    public <T> List<TableFieldInfo> getTableFieldList(Class<T> clazz,Connection connection){
        List<TableFieldInfo> tableFieldInfoList = new ArrayList<>();
        String tableName = QueryUtil.toUnderlineStr(clazz.getSimpleName());
        List<Map<String,Object>> fieldsMap = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet set = databaseMetaData.getColumns(
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    null);
            while (set.next()) {
                ResultSetMetaData metaData = set.getMetaData();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                    if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                        String fieldName;
                        if (QueryUtil.isNotBlankStr(metaData.getColumnLabel(fieldNum))) {
                            fieldName = metaData.getColumnLabel(fieldNum);
                        } else {
                            fieldName = metaData.getColumnName(fieldNum);
                        }
                        Object fieldValue = set.getObject(fieldName);
                        map.put(fieldName, fieldValue);
                    }
                }
                fieldsMap.add(map);
            }
        }catch(SQLException e){
            throw new DbException("取数据表结构失败！",e);
        }

        String dbType = getDatabaseType(connection);
        for(Map<String,Object> map:fieldsMap){
            TableFieldInfo tableFieldInfo = new TableFieldInfo();
            tableFieldInfo.setTableName(tableName);
            tableFieldInfo.setFieldName((String) map.get("COLUMN_NAME"));
            String typeNameStr = (String) map.get("TYPE_NAME");
            if("sqlite".equalsIgnoreCase(dbType)) {
                String typeName = typeNameStr.replaceAll("\\(", "#").replaceAll("\\)", "#").replaceAll(",", "#");
                String[] list = typeName.split("#");
                if (list.length == 1) {
                    tableFieldInfo.setFieldType(list[0]);
                    tableFieldInfo.setFieldLength(0);
                    tableFieldInfo.setDecimalDigit(0);
                } else if (list.length == 2) {
                    tableFieldInfo.setFieldType(list[0]);
                    tableFieldInfo.setFieldLength(Integer.parseInt(list[1]));
                    tableFieldInfo.setDecimalDigit(0);
                } else if (list.length == 3) {
                    tableFieldInfo.setFieldType(list[0]);
                    tableFieldInfo.setFieldLength(Integer.parseInt(list[1]));
                    tableFieldInfo.setDecimalDigit(Integer.parseInt(list[2]));
                }
            }else if("mysql".equalsIgnoreCase(dbType)){
                tableFieldInfo.setFieldType(typeNameStr);
                tableFieldInfo.setFieldLength((Integer) map.get("COLUMN_SIZE"));
                tableFieldInfo.setDecimalDigit((Integer) map.get("DECIMAL_DIGITS"));
            }
            tableFieldInfo.setFieldSeq((Integer) map.get("ORDINAL_POSITION"));
            tableFieldInfo.setComment((String) map.get("REMARKS"));
            tableFieldInfo.setDefaultValue((String) map.get("COLUMN_DEF"));
            tableFieldInfo.setIsNullable((String) map.get("IS_NULLABLE"));
            tableFieldInfoList.add(tableFieldInfo);
        }
        List<String> pKeyList = getTablePrimaryKeys(tableName,connection);
        for(String pKey:pKeyList){
            for(TableFieldInfo tableFieldInfo:tableFieldInfoList){
                if(pKey.equalsIgnoreCase(tableFieldInfo.getFieldName())){
                    tableFieldInfo.setKeyType("PRI");
                    break;
                }
            }
        }

        return tableFieldInfoList;
    }

    public List<TableFieldInfo> getTableFieldList(String sql,Connection connection){
        List<TableFieldInfo> tableFieldInfoList = new ArrayList<>();
        List<Map<String,Object>> fieldsMap = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if (set.next()) {
                ResultSetMetaData metaData = set.getMetaData();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                    if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                        TableFieldInfo tableFieldInfo = new TableFieldInfo();
                        String fieldName;
                        if (QueryUtil.isNotBlankStr(metaData.getColumnLabel(fieldNum))) {
                            fieldName = metaData.getColumnLabel(fieldNum);
                        } else {
                            fieldName = metaData.getColumnName(fieldNum);
                        }
//                        Object fieldValue = set.getObject(fieldName);

                        tableFieldInfo.setFieldName(fieldName);
                        tableFieldInfo.setComment(fieldName);
//                        tableFieldInfo.setFieldType("0");
//                        tableFieldInfo.setFieldLength(0);
//                        tableFieldInfo.setDecimalDigit(0);
//                        tableFieldInfo.setFieldSeq((Integer) map.get("ORDINAL_POSITION"));
//                        tableFieldInfo.setComment((String) map.get("REMARKS"));
//                        tableFieldInfo.setDefaultValue((String) map.get("COLUMN_DEF"));
//                        tableFieldInfo.setIsNullable((String) map.get("IS_NULLABLE"));
                        tableFieldInfoList.add(tableFieldInfo);
                    }
                }
                fieldsMap.add(map);
            }
        }catch(SQLException e){
            throw new DbException("取数据表结构失败！",e);
        }
        return tableFieldInfoList;
    }

    public <T> List<String> getTablePrimaryKeys(Class<T> clazz) {
        return getTablePrimaryKeys(QueryUtil.toUnderlineStr(clazz.getSimpleName()),getDatabaseConnection());
    }

    public List<String> getTablePrimaryKeys(String tableName) {
        return getTablePrimaryKeys(tableName,getDatabaseConnection());
    }

    public <T> List<String> getTablePrimaryKeys(Class<T> clazz,Connection connection) {
        return getTablePrimaryKeys(QueryUtil.toUnderlineStr(clazz.getSimpleName()),connection);
    }

    /**
     * 返回表结构显示的内容
     * @param tableName 数据表
     * @param connection 数据库连接
     * @return List<String>
     */
    public List<String> getTablePrimaryKeys(String tableName,Connection connection) {
        List<String> primaryKeys = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet set = databaseMetaData.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);
            if(set == null) return primaryKeys;
            while(set.next()) {
                primaryKeys.add(set.getString(4));
            }
        }catch(SQLException e){
            throw new DbException("取数据表结构失败！",e);
        }
        return primaryKeys;
    }
}

