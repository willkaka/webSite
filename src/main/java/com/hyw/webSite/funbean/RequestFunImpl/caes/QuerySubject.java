package com.hyw.webSite.funbean.RequestFunImpl.caes;

import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.service.DynamicTableService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("querySubject")
public class QuerySubject implements RequestFun {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","table");//以表格形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dbName = (String) inputValue.get("dbName");
        if(StringUtil.isBlank(dbName)){
            throw new BizException("DB不允许为空值!");
        }
        String modelId = (String) inputValue.get("modelId");
        String orgId = (String) inputValue.get("orgId");
        String owner = (String) inputValue.get("owner");
        String channel = (String) inputValue.get("channel");
        String freeTax = (String) inputValue.get("freeTax");
        String transId = (String) inputValue.get("transId");
        String libName = "caes";

        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dbName);
        configDatabaseInfo.setDatabaseLabel(libName);
        Connection connection = DbUtil.getConnection(configDatabaseInfo);

        //取trans_entry
        String sql = "select trans_id 交易码,sort_id 序号,direction 方向,subject_no 核算科目,expression 表达式,digest 科目名称 from trans_entry WHERE record_ind='A'";
        if(StringUtil.isNotBlank(modelId)){
            sql = sql + " AND model_id='"+modelId+"'";
        }
        if(StringUtil.isNotBlank(transId) && !"all".equals(transId)){
            sql = sql + " AND trans_id='" + transId + "'";
        }
        sql = sql +  "order by trans_id,sort_id";
        List<Map<String,Object>> transEntryList = DbUtil.getSqlRecords(connection,sql);
        //取subject_map_eas
        sql = "select subject_no,business_channel,line_id,org_id,company_num,account_num,asstact_first_num,tax_type from subject_map_eas WHERE record_ind='A'";
        List<Map<String,Object>> subjectMapList = DbUtil.getSqlRecords(connection,sql);
        DbUtil.closeConnection(connection);

        String company;
        if("DSRD".equals(owner)) company = "02.100";
        else if("RSRD".equals(owner)) company = "02.103";
        else if("100500".equals(orgId)) company = "03.001";
        else if("100200".equals(orgId)) company = "03.002";
        else if("100300".equals(orgId)) company = "03.003";
        else if("100600".equals(orgId)) company = "03.004";
        else company = "01";


        for(Map<String,Object> transEntry:transEntryList){
            String subjectNo = (String) transEntry.get("核算科目");

            List<Map<String,Object>> subjectMapList2 = new ArrayList<>();
            for(Map<String,Object> subjectMap:subjectMapList){
                if(subjectNo.equals(subjectMap.get("subject_no"))){
                    subjectMapList2.add(subjectMap);
                }
            }
            Map<String,Object> subjectMap = getMatchModel(subjectMapList2,channel,modelId,orgId,company);
            transEntry.put("金蝶科目号",subjectMap.get("account_num"));
            transEntry.put("金蝶辅助科目",subjectMap.get("asstact_first_num"));
        }
        List<String> tableColList = new ArrayList<>();
        tableColList.add("交易码");
        tableColList.add("序号");
        tableColList.add("方向");
        tableColList.add("核算科目");
        tableColList.add("科目名称");
        tableColList.add("金蝶科目号");
        tableColList.add("金蝶辅助科目");
        tableColList.add("表达式");

        returnDto.getOutputMap().put("tableColList", tableColList);
        returnDto.getOutputMap().put("tableRecordList", transEntryList);

        return returnDto;
    }

    public static final double WEIGH_FIRST = 0.5;
    public static final double WEIGH_SECOND = 0.25;
    public static final double WEIGH_THIRD = 0.125;
    public static final double WEIGH_FOURTH = 0.0625;
    public static final double WEIGH_FIFTH = 0.03125;
    private static Map<String,Object> getMatchModel(List<Map<String,Object>> list,
                                                    String businessChannel, String lineId, String orgId, String companyNum) {
        double max = 0;
        int index = 0;
        for (int i=0;i<list.size();i++){
            double temp = 0;
            Map<String,Object> result = list.get(i);
            if(result.get("business_channel").equals(businessChannel)) temp = temp + WEIGH_FIRST;
            if(result.get("line_id").equals(lineId)) temp = temp + WEIGH_SECOND;
            if(result.get("org_id").equals(orgId)) temp = temp + WEIGH_THIRD;
            if(result.get("company_num").equals(companyNum)) temp = temp + WEIGH_FOURTH;
            if(max<temp) {
                max = temp;
                index = i;
            }
        }
        return list.get(index);
    }
}
