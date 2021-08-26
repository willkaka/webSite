package com.hyw.webSite.funbean.RequestFunImpl;

import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Service("uploadFile")
@Slf4j
public class UploadFile extends RequestFunUnit<String, UploadFile.QryVariable> {

    @Autowired
    private ExcelTemplateUtil excelTemplateUtil;

    @Autowired
    private DataService dataService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(UploadFile.QryVariable variable){
        //输入检查
        if(Objects.isNull(variable.getFileList())){
            throw new BizException("导入文件不允许为空值!");
        }
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, UploadFile.QryVariable variable) {
        StringBuffer outString = new StringBuffer();
        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示

        int cnt = 0;
        for(MultipartFile multipartFile:variable.getFileList()) {
            File file = FileUtil.multipartFileToFile(multipartFile);

            if("SPD_Claim".equalsIgnoreCase(variable.getFileType())){
                outString.append(spdClaimSql(excelTemplateUtil.getExcelRecords("SPD_Claim",file)));
            }
        }

        return outString.toString();
    }

    private String spdClaimSql(List<JSONObject> jsonObjectList){
        StringBuilder rtnStr = new StringBuilder();
        StringBuilder loanNoStr = new StringBuilder();
        StringBuilder updLbrSql = new StringBuilder();
        for(JSONObject jsonObject:jsonObjectList){
            String loanNo = jsonObject.getString("loanNo");
            String claimDate = new SimpleDateFormat("yyyy-MM-dd").format(jsonObject.getDate("claimDate"));
            int overdueDays = jsonObject.getInteger("overdueDays");

            updLbrSql.append("UPDATE loanbalance_relative SET current_overdue_date='").append(claimDate)
                    .append("',corp_current_overdue_date='").append(claimDate)
                    .append("',fee_current_overdue_date='").append(claimDate)
                    .append("',current_overdue_days=").append(overdueDays)
                    .append(",corp_current_overdue_days=").append(overdueDays)
                    .append(",fee_current_overdue_days=").append(overdueDays)
                    .append(" WHERE loan_no='").append(loanNo).append("';").append("\n");
            if(loanNoStr.length()<=0){
                loanNoStr.append("'").append(loanNo).append("'");
            }else{
                loanNoStr.append(",").append("'").append(loanNo).append("'");
            }
        }

        rtnStr.append("-- 更新loanbalance_relative逾期日期/逾期天数，共 ").append(jsonObjectList.size()).append(" 条记录.\n")
              .append(updLbrSql).append("\n\n")
              .append("-- 更新loanbalance_relative最近逾期天数，共 ").append(jsonObjectList.size()).append(" 条记录.\n")
              .append("update loanbalance_relative \n" +
                      "set last_overdue_days = current_overdue_days,\n" +
                      "corp_last_overdue_days = corp_current_overdue_days,\n" +
                      "fee_last_overdue_days = fee_current_overdue_days\n" +
                      "where loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative贷款逾期次数若为0则更新为1.\n")
              .append("update loanbalance_relative\n" +
                      "set total_overdue_cnt=1\n" +
                      "where total_overdue_cnt=0 \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative本息逾期次数若为0则更新为1.\n")
              .append("update loanbalance_relative\n" +
                      "set corp_total_overdue_cnt=1\n" +
                      "where corp_total_overdue_cnt=0 \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative费用逾期次数若为0则更新为1.\n")
              .append("update loanbalance_relative\n" +
                      "set fee_total_overdue_cnt=1\n" +
                      "where fee_total_overdue_cnt=0 \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative贷款总逾期天数小于当前逾期天数的，更新为当前逾期天数.\n")
              .append("update loanbalance_relative\n" +
                      "set total_overdue_days = current_overdue_days\n" +
                      "where (total_overdue_days < current_overdue_days or total_overdue_cnt<=1) \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative本息总逾期天数小于当前逾期天数的，更新为当前逾期天数.\n")
              .append("update loanbalance_relative\n" +
                      "set corp_total_overdue_days = corp_current_overdue_days\n" +
                      "where (corp_total_overdue_days < corp_current_overdue_days or corp_total_overdue_cnt<=1) \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative费用总逾期天数小于当前逾期天数的，更新为当前逾期天数.\n")
              .append("update loanbalance_relative\n" +
                      "set fee_total_overdue_days = fee_current_overdue_days\n" +
                      "where (fee_total_overdue_days < fee_current_overdue_days or fee_total_overdue_cnt<=1) \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative贷款首次逾期日期为空的，更新为当前逾期日期.\n")
              .append("update loanbalance_relative\n" +
                      "set first_overdue_date = current_overdue_date\n" +
                      "where first_overdue_date is null \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative本息首次逾期日期为空的，更新为当前逾期日期.\n")
              .append("update loanbalance_relative\n" +
                      "set corp_first_overdue_date = corp_current_overdue_date\n" +
                      "where corp_first_overdue_date is null \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
              .append("-- 更新loanbalance_relative费用首次逾期日期为空的，更新为当前逾期日期.\n")
              .append("update loanbalance_relative\n" +
                      "set fee_first_overdue_date = fee_current_overdue_date\n" +
                      "where fee_first_overdue_date is null \n" +
                      "and loan_no in (").append(loanNoStr).append(");").append("\n\n")
        ;

        return rtnStr.toString();
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
        private List<MultipartFile> fileList;
        private String fileType;
    }
}
