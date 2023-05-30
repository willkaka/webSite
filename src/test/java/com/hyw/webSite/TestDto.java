package com.hyw.webSite;

import com.github.crab2died.annotation.ExcelField;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class TestDto implements Serializable {

    /**
     * 关联交易流水号/批次号
     */
    @ExcelField(title = "批次号", order = 1)
    private String relationTransNo;

    /**
     * 出账编号
     */
    @ExcelField(title = "出账编号", order = 2)
    @NotBlank(message = "流程编号不能为空")
    private String loanNo;// loanNo贷款编号

    /**
     * 费用集合
     */
    List<Map<String, Object>> listFee = new ArrayList<>();
}
