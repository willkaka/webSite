package com.hyw.webSite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hyw.webSite.dto.DynamicTableDto;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.mapper.DynamicTableMapper;
import com.hyw.webSite.utils.ClassUtil;
import com.hyw.webSite.utils.SqlUtil;
import com.hyw.webSite.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 通用表 服务实现类
 * </p>
 */
@Slf4j
@Service
public class DynamicTableService
{
    @Autowired
    DynamicTableMapper dynamicTableMapper;

    public List<Map<String, Object>> selectAll(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.selectAll(dynamicTableDto);
    }

    public List<Map<String, Object>> query(DynamicTableDto dynamicTableDto) {
        return dynamicTableMapper.query(dynamicTableDto);
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

    public List<?> readRecords(DynamicTableDto dynamicTableDto) throws Exception {
        //取数据表对象类
        Object tableObj = ClassUtil.getInstance(dynamicTableDto.getClassName());

        List<Object> rtnList = new ArrayList();
        List<Map<String, Object>> list = dynamicTableMapper.query(dynamicTableDto);
        for(Map<String, Object> map:list){
            rtnList.add(ClassUtil.map2Object(map,tableObj.getClass()));
        }
        return rtnList;
    }


    /**
     * 读取多条记录
     * @param tableObj
     * @return
     * @throws Exception
     */
    public List<?> readRecords(Object tableObj) throws Exception {
        List<Object> rtnList = new ArrayList();

        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName(StringUtil.camelCaseToUnderline( tableObj.getClass().getSimpleName() ));
        dynamicTableDto.setSelectFields("*");
        dynamicTableDto.setSelectWhere(SqlUtil.getFieldValueExpress(tableObj));
        List<Map<String, Object>> list = dynamicTableMapper.query(dynamicTableDto);
        for(Map<String, Object> map:list){
            rtnList.add(ClassUtil.map2Object(map,tableObj.getClass()));
        }
        return rtnList;
    }

    public <T> T readRecord(Object tableObj) throws Exception {
        List<Object> rtnList = new ArrayList();

        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName(StringUtil.camelCaseToUnderline( tableObj.getClass().getSimpleName() ));
        dynamicTableDto.setSelectFields("*");
        dynamicTableDto.setSelectWhere(SqlUtil.getFieldValueExpress(tableObj));
        dynamicTableDto.setLimitBegNum(0);
        dynamicTableDto.setLimitCountNum(1);
        List<Map<String, Object>> list = dynamicTableMapper.query(dynamicTableDto);
        for(Map<String, Object> map:list){
            rtnList.add(ClassUtil.map2Object(map,tableObj.getClass()));
        }
        return rtnList==null?null:(T)rtnList.get(0);
    }

    public int insert(Object tableObj){
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName(StringUtil.camelCaseToUnderline( tableObj.getClass().getSimpleName() ));
        dynamicTableDto.setFieldNames(SqlUtil.getFieldNameString(tableObj,","));
        dynamicTableDto.setFieldValues(SqlUtil.getFieldValueString(tableObj,","));
        return dynamicTableMapper.insert(dynamicTableDto);
    }

    public int update(Object tableObj){
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName(StringUtil.camelCaseToUnderline( tableObj.getClass().getSimpleName() ));
        dynamicTableDto.setFieldNames(SqlUtil.getFieldNameString(tableObj,","));
        dynamicTableDto.setFieldValues(SqlUtil.getFieldValueString(tableObj,","));
        return dynamicTableMapper.update(dynamicTableDto);
    }

    public int delete(Object tableObj) throws IllegalAccessException {
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName(StringUtil.camelCaseToUnderline( tableObj.getClass().getSimpleName() ));
        dynamicTableDto.setSelectWhere(SqlUtil.getFieldValueExpress(tableObj));
        return dynamicTableMapper.delete(dynamicTableDto);
    }
}
