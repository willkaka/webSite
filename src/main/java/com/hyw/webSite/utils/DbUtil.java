package com.hyw.webSite.utils;

import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DbUtil {

    /**
     * 获取db的所有数据库名称
     * @param connection 连接
     * @return 所有数据库名称List<String>
     */
    public static List<String> getLibraryNames(Connection connection){
        List<String> dbLibraryList=new ArrayList<String>();
        try {
            DatabaseMetaData dmd = connection.getMetaData();

            if("sqlite".equals(dmd.getDatabaseProductName().toLowerCase())){
                dbLibraryList.add("main");
                return dbLibraryList;
            }

            ResultSet rs = dmd.getCatalogs();
            //List<Map<String,Object>> maplist = getRcdMapFromResultSet(rs);
            while (rs.next()) {
                dbLibraryList.add(rs.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dbLibraryList;
    }

    public static List<String> getTableNames(Connection con,String libName) {
        String catalog           = libName;  //表所在的编目
        String schemaPattern     = null;  //表所在的模式
        String tableNamePattern  = null; //匹配表名
        //指出返回何种表的数组("TABLE"、"VIEW"、"SYSTEM TABLE"， "GLOBAL TEMPORARY"，"LOCAL  TEMPORARY"，"ALIAS"，"SYSNONYM")
        String[] typePattern = new String[] { "TABLE" };

        List<String> tableList = new ArrayList<String>();
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(catalog, schemaPattern, tableNamePattern, typePattern);
            //List<Map<String,Object>> maplist = getRcdMapFromResultSet(rs);
            while (rs.next()) {
                tableList.add(rs.getString(3)); //rs.getString(3)表名，rs.getString(2)表所属用户名
            }
        } catch (Exception e) {
            throw new BizException("取数据库的所有表格名称出错！");
        }
        return tableList;
    }

    public static List<Map<String,Object>> getFieldInfo(Connection connection,String dbName, String libName,String tableName) {
        String   catalog           = libName;  //表所在的编目
        String   schemaPattern     = dbName;  //表所在的模式
        String   tableNamePattern  = tableName; //匹配表名
        String   columnNamePattern = null; //

        List<Map<String,Object>> fieldsMap = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet result = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            fieldsMap = getRcdMapFromResultSet(result);
        }catch(SQLException e){
            log.error("取数据表结构失败！",e);
            throw new BizException("取数据表结构失败！");
        }
        return fieldsMap;
    }

    public static List<String> getFieldNameList(Connection connection,String dbName, String tableName) {
        String   catalog           = dbName;  //表所在的编目
        String   schemaPattern     = null;  //表所在的模式
        String   tableNamePattern  = tableName; //匹配表名
        String   columnNamePattern = null; //

        List<Map<String,Object>> fieldsMap = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet result = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            fieldsMap = getRcdMapFromResultSet(result);
        }catch(SQLException e){
            log.error("取数据表结构失败！",e);
            throw new BizException("取数据表结构失败！");
        }

        List<String> fieldsList = new ArrayList<>();
        for(Map<String,Object> map:fieldsMap){
            fieldsList.add((String) map.get("COLUMN_NAME"));
        }

        return fieldsList;
    }

    /**
     * 返回表结构显示的内容
     * @param connection
     * @param dbName
     * @param tableName
     * @return
     */
    public static List<String> getFieldInfoShowCol(Connection connection,String dbName, String tableName) {
        String   catalog           = null;  //表所在的编目
        String   schemaPattern     = dbName;  //表所在的模式
        String   tableNamePattern  = tableName; //匹配表名
        String   columnNamePattern = null; //

        List<String> showColsMap = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet set = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            if(set == null) return showColsMap;
            ResultSetMetaData metaData = set.getMetaData();
            for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                    String fieldName;
                    if (StringUtils.isNotBlank(metaData.getColumnLabel(fieldNum))) {
                        fieldName = metaData.getColumnLabel(fieldNum);
                    } else {
                        fieldName = metaData.getColumnName(fieldNum);
                    }
                    showColsMap.add(fieldName);
                }
            }
        }catch(SQLException e){
            log.error("取数据表结构失败！",e);
            throw new BizException("取数据表结构失败！");
        }
        return showColsMap;
    }

    /**
     * 返回表结构显示的内容
     * @param connection
     * @param dbName
     * @param tableName
     * @return
     */
    public static List<String> getTablePrimaryKeys(Connection connection,String dbName, String tableName) {
        String   catalog           = dbName;  //表所在的编目
        String   schemaPattern     = null;  //表所在的模式
        String   tableNamePattern  = tableName; //匹配表名

        List<String> primaryKeys = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet set = databaseMetaData.getPrimaryKeys(catalog, schemaPattern, tableNamePattern);
            if(set == null) return primaryKeys;
            while(set.next()) {
                primaryKeys.add(set.getString(4));
            }
        }catch(SQLException e){
            log.error("取数据表结构失败！",e);
            throw new BizException("取数据表结构失败！");
        }
        return primaryKeys;
    }



    public static void main(String[] args) throws SQLException {
        //'com.mysql.cj.jdbc.Driver','jdbc:mysql://localhost:3306/hlhome?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC',
        // 'hlhome', 'root', 'Root#98e'),
        String driver="com.mysql.cj.jdbc.Driver";
        String url="jdbc:mysql://10.21.16.49:7004";
        String user="caesopr";
        String password="Dfs@3K3#r3";
        Connection connection = DbUtil.getConnection("mysql",driver,url,null,null,user,password);
        String type = getConnectionType(connection);
        List<String> libs = DbUtil.getLibraryNames(connection);
        List<String> tables = DbUtil.getTableNames(connection,"caes");
        List<Map<String,Object>> fields = getTableFieldsMapList(connection,"test1");
        fields = getFieldInfo(connection,"caes","caes","test1");
        DbUtil.closeConnection(connection);

        driver="org.sqlite.JDBC";
        url="jdbc:sqlite::resource:sqlitedb/hlhome.db";
        user="";
        password="";
        connection = DbUtil.getConnection("sqlite",driver,url,null,null,user,password);
        List<String> keyFields = DbUtil.getTablePrimaryKeys(connection,null,"web_element");
        type = getConnectionType(connection);
        DbUtil.getLibraryNames(connection);
        fields = getTableFieldsMapList(connection,"web_config_element");
        DbUtil.closeConnection(connection);
    }


    /**
     * 取DB数据库连接
     * @param configDatabaseInfo
     * @return Connection
     */
    public static Connection getConnection(ConfigDatabaseInfo configDatabaseInfo) {
        String dbType = configDatabaseInfo.getDatabaseType();
        String driver = configDatabaseInfo.getDatabaseDriver();
        String url = configDatabaseInfo.getDatabaseAddr();
        String attr = configDatabaseInfo.getDatabaseAttr();
        String lib = configDatabaseInfo.getDatabaseLabel();
        String user = configDatabaseInfo.getLoginName();
        String password = configDatabaseInfo.getLoginPassword();

        return getConnection(dbType,driver,url,attr,lib,user,password);
    }

    /**
     * 取DB数据库连接
     * @param driver 驱动
     * @param url 地址
     * @param user 用户名
     * @param password 密码
     * @return Connection
     */
    public static Connection getConnection(String dbType,String driver,String url,String attr,String lib,String user,String password) {
        String linkSign = dbType.equals(Constant.DB_TYPE_MYSQL)?"?":
                        ( dbType.equals(Constant.DB_TYPE_ORACLE)?":":
                        ( dbType.equals(Constant.DB_TYPE_SQLITE)?":":"") );

        String dbUrl ="";
        if(dbType.equals(Constant.DB_TYPE_MYSQL)) {
            dbUrl = url
                + (StringUtil.isNotBlank(lib) ? "/" + lib : "")
                + (StringUtil.isNotBlank(attr) ? "?" + attr : "");
        }else if(dbType.equals(Constant.DB_TYPE_ORACLE)){
            dbUrl = url
                    + (StringUtil.isNotBlank(lib) ? ":" + lib : "");
        }if(dbType.equals(Constant.DB_TYPE_SQLITE)){
            if("main".equals(lib)) {
                dbUrl = url;
            }else{
                dbUrl = url
                        + (StringUtil.isNotBlank(lib) ? ":" + lib : "");
            }
        }

        try {
            Class.forName(driver);
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            log.error("数据库连接出错！driver({}),url({}),user({}),password({})",driver,dbUrl,user,password,e);
            throw new BizException("数据库连接出错！");
        }
    }

    /**
     * 关闭数据库连接
     * @param connection 数据库连接
     */
    public static void closeConnection(Connection connection){
        try{
            if(connection != null) connection.close();
        }catch (Exception e){
            log.error("关闭数据库连接出错！",e);
        }
    }

    /**
     * 执行sql返回记录集
     * @param connection 数据库连接
     * @param sql 脚本
     * @return 记录集
     */
    public static List<Map<String,Object>> getSqlRecords(Connection connection,String sql){
        if(connection == null){
            log.error("数据库连接不允许为null！");
            return null;
        }
        if(StringUtil.isBlank(sql)){
            log.error("输入sql语句不允为空！");
            return null;
        }

        List<Map<String,Object>> listMap = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if(set != null) {
                //取记录
                while(set.next()) {
                    ResultSetMetaData metaData = set.getMetaData();
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                        if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                            String fieldName;
                            if (StringUtils.isNotBlank(metaData.getColumnLabel(fieldNum))) {
                                fieldName = metaData.getColumnLabel(fieldNum);
                            } else {
                                fieldName = metaData.getColumnName(fieldNum);
                            }
                            Object fieldValue = set.getObject(fieldName);
                            map.put(fieldName, fieldValue);
                        }
                    }
                    listMap.add(map);
                }
            }
            if(set != null) set.close();
            statement.close();
        }catch(Exception e){
            log.error("执行SQL({})出错！",sql,e);
        }finally {
            DbUtil.closeConnection(connection);
        }
        return listMap;
    }


    /**
     * 查询数据表返回记录集
     * @param connection 数据库连接
     * @param table 查询数据表
     * @return 记录集
     */
    public static List<Map<String,Object>> getTableRecords(Connection connection,String table){
        if(connection == null){
            log.error("数据库连接不允许为null！");
            return null;
        }
        if(StringUtil.isBlank(table)){
            log.error("查询数据表不允为空！");
            return null;
        }

        List<Map<String,Object>> listMap = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select * from " + table);
            if(set != null) {
                //取记录
                while(set.next()) {
                    ResultSetMetaData metaData = set.getMetaData();
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                        if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                            String fieldName;
                            if (StringUtils.isNotBlank(metaData.getColumnLabel(fieldNum))) {
                                fieldName = metaData.getColumnLabel(fieldNum);
                            } else {
                                fieldName = metaData.getColumnName(fieldNum);
                            }
                            Object fieldValue = set.getObject(fieldName);
                            map.put(fieldName, fieldValue);
                        }
                    }
                    listMap.add(map);
                }
            }
            if(set != null) set.close();
            statement.close();
        }catch(Exception e){
            log.error("查询数据表({})出错！",table,e);
        }finally {
            DbUtil.closeConnection(connection);
        }
        return listMap;
    }

    /**
     * 查询数据表返回记录集
     * @param connection 数据库连接
     * @param table 查询数据表
     */
    public static List<Map<String,Object>> getTableFieldsMapList(Connection connection,String table){
        if(connection == null){
            log.error("数据库连接不允许为null！");
            return null;
        }
        if(StringUtil.isBlank(table)){
            log.error("查询数据表不允为空！");
            return null;
        }

        String sqlSelect = "";
        if(Constant.DB_TYPE_ORACLE.equals(getConnectionType(connection).toLowerCase())){
            sqlSelect = "SELECT * FROM " + table + " WHERE rownum=1";
        }else{
            sqlSelect = "SELECT * FROM " + table + " limit 1";
        }

        List<Map<String,Object>> fieldList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sqlSelect);
            if(set != null) {
                //取字段名
                ResultSetMetaData metaData1 = set.getMetaData();
                for (int fieldNum = 1; fieldNum <= metaData1.getColumnCount(); fieldNum++) {
                    Map<String,Object> field = new HashMap<>();
                    field.put("name",metaData1.getColumnLabel(fieldNum));
                    field.put("type",metaData1.getColumnTypeName(fieldNum));
                    //field.put("length",metaData1.getColumnDisplaySize(fieldNum));
                    field.put("length",metaData1.getPrecision(fieldNum));
                    fieldList.add(field);
                }
            }
            if(set != null) set.close();
            statement.close();
        }catch(Exception e){
            log.error("查询数据表({})出错！",table,e);
        }finally {
            //DbUtil.closeConnection(connection);
        }
        return fieldList;
    }

    /**
     * 执行insert/update语句
     * @param connection 数据库连接
     * @param sql insert/update语句
     * @return boolean成功为真
     */
    public static boolean executeSql(Connection connection,String sql){
        if(connection == null){
            log.error("数据库连接不允许为null！");
            return false;
        }
        if(StringUtil.isBlank(sql)){
            log.error("sql语句不允为空！");
            return false;
        }

        boolean rtnFlag = false;
        try {
            Statement statement = connection.createStatement();
            rtnFlag = statement.execute(sql);
            statement.close();
        }catch(Exception e){
            log.error("执行sql语句({})出错！",sql,e);
        }finally {
            DbUtil.closeConnection(connection);
        }
        return rtnFlag;
    }

    public static String getConnectionType(Connection connection){
        String type = "";
        try {
            type = connection.getMetaData().getDatabaseProductName().trim();
        }catch (SQLException e){
            log.error("取数据类型出错！",e);
        }
        return type;
    }

    private static List<Map<String,Object>> getRcdMapFromResultSet(ResultSet set){
        List<Map<String,Object>> listMap = new ArrayList<>();

        if(set == null) return listMap;

        //取记录
        try {
            while (set.next()) {
                ResultSetMetaData metaData = set.getMetaData();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int fieldNum = 1; fieldNum <= metaData.getColumnCount(); fieldNum++) {
                    if (metaData.getColumnName(fieldNum) != null && !"".equals(metaData.getColumnName(fieldNum))) {
                        String fieldName;
                        if (StringUtils.isNotBlank(metaData.getColumnLabel(fieldNum))) {
                            fieldName = metaData.getColumnLabel(fieldNum);
                        } else {
                            fieldName = metaData.getColumnName(fieldNum);
                        }
                        Object fieldValue = set.getObject(fieldName);
                        map.put(fieldName, fieldValue);
                    }
                }
                listMap.add(map);
            }
        }catch (SQLException e){
            log.error("读取resultset出错！",e);
            throw new BizException("读取resultset出错！");
        }
        return listMap;
    }
}
