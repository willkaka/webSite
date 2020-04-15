package com.hyw.webSite.service;

import java.util.List;
import java.util.Map;

import com.hyw.webSite.dto.DynamicTableDto;
import com.hyw.webSite.mapper.DynamicTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 通用表 服务实现类
 * </p>
 */
@Service
public class DynamicTableService
{
    @Autowired
    DynamicTableMapper dynamicTableMapper;

    public List<Map<String, Object>> selectAll(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.selectAll(dynamicTableDto);
    }

    public int insert(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.insert(dynamicTableDto);
    }

    public int update(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.update(dynamicTableDto);
    }

    public int delete(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.delete(dynamicTableDto);
    }
}
