package com.hyw.webSite.dao;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *null
 */
@Data
@Accessors(chain = true)
//@TableName("sys_param")
public class SysParam {
    /**
     * null
     */
    private long sysParamId;

    /**
     * null
     */
    private String paramRoot;

    /**
     * null
     */
    private String paramSub;

    /**
     * null
     */
    private String paramName;

    /**
     * null
     */
    private String paramValue1;

    /**
     * null
     */
    private String paramValue2;

    /**
     * null
     */
    private String paramValue3;

    /**
     * null
     */
    private String paramDesc;

}
