package com.hyw.webSite.funbean.abs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 自定义变量
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestPubDto {
    private String outputShowType; //输出显示格式类型
    private boolean isWithPage; //是否分页
    private int totalCount;
    private int pageNow;
    private int pageSize;
}
