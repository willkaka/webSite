package com.hyw.webSite.funbean.RequestFunImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import com.hyw.webSite.utils.excel.TemplateDefine;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("spdbClaimOverdue")
@Slf4j
public class SpdbClaimOverdue extends RequestFunUnit<String, SpdbClaimOverdue.QryVariable> {

    @Autowired
    private ExcelTemplateUtil excelTemplateUtil;

    @Autowired
    private DataService dataService;

    @Value("${upload.file.local.path}")
    private String uploadFileLocalPath;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QryVariable variable){
        //输入检查
        BizException.trueThrow(Objects.isNull(variable.getFileList()),"导入文件不允许为空值!");

    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, QryVariable variable) {
        StringBuffer outString = new StringBuffer();
        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示

        String fieldInfoListStr = variable.getFieldInfoList();
        JSONObject jsonObject = JSONObject.parseObject(fieldInfoListStr);
        //{"list":[{"fieldName":"loanNo","posCol":9,"fieldType":"string"},{"fieldName":"overdueDate","posCol":17,"fieldType":"date"},{"fieldName":"putoutDate","posCol":15,"fieldType":"date"},{"fieldName":"overdueDays","posCol":16,"fieldType":"number"}]}
        //{"list":[{"fieldName":"loanNo","posCol":"J","fieldType":"string"},{"fieldName":"overdueDate","posCol":"S","fieldType":"date"},{"fieldName":"putoutDate","posCol":"Q","fieldType":"date"},{"fieldName":"overdueDays","posCol":"R","fieldType":"number"}]}
        JSONArray list = (JSONArray) jsonObject.get("list");
        //posCol字母转数字
        for(int i=0;i<list.size();i++){
            JSONObject object = list.getJSONObject(i);
            for(String key:object.keySet()){
                if("posCol".equalsIgnoreCase(key)){
                    String value = object.getString(key);
                    object.replace(key,getExcelColNo(value));
                }
            }
        }
        //转为TemplateDefine
        List<TemplateDefine> fieldList = new ArrayList<>();
        for(Object key:list){
            TemplateDefine templateDefine = new TemplateDefine();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                templateDefine = objectMapper.readValue(key.toString(), TemplateDefine.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            fieldList.add(templateDefine);
        }
        fieldList.forEach(field -> field.setPosRow(variable.getBegLineNo()));

        for(MultipartFile multipartFile:variable.getFileList()) {
            File file = multipartFileToFile(multipartFile);
            outString.append(spdClaimSql(
                    excelTemplateUtil.getExcelRecordsWithJsonFieldInfo(file,variable.getSheetNo(),
                            variable.getBegLineNo(),fieldList)));
        }

        return outString.toString();
    }

    private int getExcelColNo(String c){
        if(StringUtils.isNumeric(c)) return Integer.parseInt(c);
        char ch = c.toUpperCase().charAt(c.length()-1);
        return ch-'A';
    }
    /**
     * 浦发理赔更新逾期天数sql生成。
     * @param jsonObjectList
     * @return
     */
    private String spdClaimSql(List<JSONObject> jsonObjectList){
        StringBuilder rtnStr = new StringBuilder();
        StringBuilder loanNoStr = new StringBuilder();
        StringBuilder updLbrSql = new StringBuilder();
        for(JSONObject jsonObject:jsonObjectList){
            String loanNo = jsonObject.containsKey("loanNo")?jsonObject.getString("loanNo"):"";
            String overdueDate = jsonObject.containsKey("overdueDate")?new SimpleDateFormat("yyyy-MM-dd").format(jsonObject.getDate("overdueDate")):"";
            String putoutDate = jsonObject.containsKey("putoutDate")?new SimpleDateFormat("yyyy-MM-dd").format(jsonObject.getDate("putoutDate")):"";
            long overdueDays = jsonObject.containsKey("overdueDays")?jsonObject.getInteger("overdueDays"):0L;
            LocalDate dOverdueDate = LocalDate.parse(overdueDate);
            LocalDate dPutoutDate = LocalDate.parse(putoutDate);
            LocalDate curOverdueDate = dOverdueDate.minusDays(overdueDays);
            if(curOverdueDate.isBefore(dPutoutDate)){
                curOverdueDate = dPutoutDate;
            }
            //逾期天数按 逾期日期-今天 计算
            overdueDays = LocalDate.now().toEpochDay() - curOverdueDate.toEpochDay();

            updLbrSql.append("UPDATE loanbalance_relative SET current_overdue_date='").append(curOverdueDate)
                    .append("',corp_current_overdue_date='").append(curOverdueDate)
                    .append("',fee_current_overdue_date='").append(curOverdueDate)
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
     * 读取上传的文件
     * @param file MultipartFile文件
     * @return File
     */
    public File multipartFileToFile(MultipartFile file){
        String location = System.getProperty("user.dir") + uploadFileLocalPath;
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            try {
                ins = file.getInputStream();
                toFile = new File( (location.endsWith("/")||location.endsWith("\\")?location:(location + "/"))
                        + file.getOriginalFilename());
                FileUtil.inputStreamToFile(ins, toFile);
                ins.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return toFile;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
        private List<MultipartFile> fileList;
        private Integer sheetNo;
        private Integer begLineNo;//开始行号
        private String fieldInfoList;
    }
}