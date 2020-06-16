package com.hyw.webSite.dto;


import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 贷款余额表
 * </p>
 *
 * @author jobob
 * @since 2018-12-18
 */
@Getter
@Setter
@Accessors( chain = true )
public class DynamicTableDto{

    private static final long serialVersionUID = 1L;

    /**
     * 数据表
     */
    private String tableName;

    /**
     * 对象类名
     */
    private String className;

    /**
     * 选取字段
     */
    private String selectFields;

    /**
     * 取数条件
     */
    private String selectWhere;

    /**
     * 集合条件
     */
    private String selectGroupby;

    /**
     * 排序条件
     */
    private String selectOrderby;

    /**
     * 字段名集合(insert字段名)
     */
    private String fieldNames;

    /**
     * 字段值集合(insert字段值)
     */
    private String fieldValues;

    /**
     * 字段值集合(insert字段/值，update字段/值)
     */
    private Map<String,Object> fieldValueMap;

    /**
     * where条件字段值集合()
     */
    private Map<String,Object> whereFieldValueMap;

    /**
     * 取出的数据清单
     */
    private List<Map<String,String>> dataList;

    /**
     * 每次读取开始记录号
     */
    private long limitBegNum;

    /**
     * 每次读取记录数
     */
    private long limitCountNum;
}

