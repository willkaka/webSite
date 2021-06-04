package com.hyw.webSite.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
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
public interface DataMapper {
    List<Map<String, Object>> selectAll(@Param("ew") DynamicTableDto dynamicTableDto);

    List<Map<String, Object>> query(@Param("ew") DynamicTableDto dynamicTableDto);

    int insert(DynamicTableDto dynamicTableDto);

    int update(@Param("ew") DynamicTableDto dynamicTableDto);

    int delete(@Param("ew") DynamicTableDto dynamicTableDto);

    //@Param(Constants.WRAPPER) Wrapper<LoanPayPlan> wrapper
    <T> List<Map<String, Object>> query(@Param("tableName") String tableName,@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    List<Map<String, Object>> queryBySql(@Param("sql") String sql);

    int updateBySql(@Param("sql") String sql);

    int saveBySql(@Param("sql") String sql);

    int deleteBySql(@Param("sql") String sql);
}

