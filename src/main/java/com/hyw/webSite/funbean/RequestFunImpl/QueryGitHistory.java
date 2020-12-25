package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.dto.GitCommitInfoDto;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.service.JGitService;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("queryGitHistory")
@Slf4j
public class QueryGitHistory implements RequestFun {
    @Autowired
    private JGitService jGitService;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","table");//以表格形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String path = (String) inputValue.get("dir");
        if(StringUtil.isBlank(path)){
            throw new BizException("git路径不允许为空值!");
        }
        String ignoreMerge = (String) inputValue.get("ignoreMerge");
        String user = (String) inputValue.get("user");
        String sBegTime = (String) inputValue.get("begTime");
        if(StringUtil.isBlank(sBegTime)){
            throw new BizException("筛选时间不允许为空值!");
        }
        String sEndTime = (String) inputValue.get("endTime");
        if(StringUtil.isBlank(sEndTime)){
            throw new BizException("筛选时间不允许为空值!");
        }
        LocalDateTime begTime = StringUtil.isBlank(sBegTime)?null:LocalDateTime.parse(sBegTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = StringUtil.isBlank(sEndTime)?null:LocalDateTime.parse(sEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String,List<GitCommitInfoDto>> fileCommitList = jGitService.getFileCommintInfo(path,user,begTime,endTime);
        Map<String, List<GitCommitInfoDto>> map = new TreeMap<>(fileCommitList);

        List<Map<String, FieldAttr>> records = new ArrayList<>();
        for(String fileName:map.keySet()){
            int i=0;
            for(GitCommitInfoDto gitCommitInfoDto:map.get(fileName)){
                String message = gitCommitInfoDto.getShortMessage().substring(0,6);
                if("Y".equals(ignoreMerge) && "Merge ".equals(message)){
                    continue;
                }
                Map<String,FieldAttr> record = new LinkedHashMap<>();
                record.put("程序",new FieldAttr().setValue(i==0?fileName:"").setRemarks("程序"));
                record.put("提交时间",new FieldAttr().setValue(gitCommitInfoDto.getCommitDateTime()).setRemarks("提交时间"));
                record.put("提交用户",new FieldAttr().setValue(gitCommitInfoDto.getCommitterIdent().getName()).setRemarks("提交用户"));
                record.put("提交备注信息",new FieldAttr().setValue(gitCommitInfoDto.getShortMessage()).setRemarks("提交备注信息"));
                records.add(record);
                i++;
            }
        }
        returnDto.getOutputMap().put("tableRecordList", records);
        return returnDto;
    }
}
