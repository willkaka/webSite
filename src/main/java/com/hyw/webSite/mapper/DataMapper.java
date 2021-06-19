package com.hyw.webSite.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}

