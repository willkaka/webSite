package com.hyw.webSite.mapper;

import com.hyw.webSite.dto.DynamicTableDto;
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
public interface DynamicTableMapper{
    List<Map<String, Object>> selectAll(@Param( "ew" ) DynamicTableDto dynamicTableDto);

    List<Map<String, Object>> query(@Param( "ew" ) DynamicTableDto dynamicTableDto);

    int insert(DynamicTableDto dynamicTableDto);

    int update(@Param( "ew" ) DynamicTableDto dynamicTableDto);

    int delete(@Param( "ew" ) DynamicTableDto dynamicTableDto);
}

