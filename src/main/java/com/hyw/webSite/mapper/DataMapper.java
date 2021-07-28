package com.hyw.webSite.mapper;

import com.hyw.webSite.dbservice.dto.MysqlColumnInfo;
import com.hyw.webSite.dbservice.dto.MysqlTableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用表 Mapper 接口
 * </p>
 */
@Mapper
public interface DataMapper {

    List<Map<String, Object>> queryBySql(@Param("sql") String sql);

    int updateBySql(@Param("sql") String sql);

    int saveBySql(@Param("sql") String sql);

    int deleteBySql(@Param("sql") String sql);


    /* Mysql数据表信息 */
    //数据库清单
    @Select("show databases")
    List<String> getMysqlDatabase();

    //数据表清单
    @Select("show tables")
    List<String> getMysqlTableList();

    //数据表信息
    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=#{database}")
    List<MysqlTableInfo> getMysqlTableInfo(@Param("database") String database);

    //数据表结构
    @Select("select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<MysqlColumnInfo> getMysqlColumnInfo(@Param("tableName") String tableName);

    //数据表创建ddl语句
    @Select("show create table #{tableName}")
    String getMysqlTableDdl(@Param("tableName") String tableName);


    /* Sqlite数据表信息 */
    //数据表创建ddl语句
    @Select("SELECT * FROM sqlite_master WHERE type='table' AND name = #{tableName}")
    String getSqliteTableDdl(@Param("tableName") String tableName);

}

