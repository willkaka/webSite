package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.dto.GitCommitInfoDto;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.service.JGitService;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("queryGitHistory")
@Slf4j
public class QueryGitHistory extends RequestFunUnit<List<Map<String, FieldAttr>>, QueryGitHistory.QueryVariable> {
    @Autowired
    private JGitService jGitService;

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryGitHistory.QueryVariable variable){
        //输入检查
        BizException.trueThrow(StringUtil.isBlank(variable.getDir()),"git路径不允许为空值!");

        BizException.trueThrow(StringUtil.isBlank(variable.getBegTime()),"筛选时间不允许为空值!");

        BizException.trueThrow(StringUtil.isBlank(variable.getEndTime()),"筛选时间不允许为空值!");

    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<Map<String, FieldAttr>> execLogic(RequestDto requestDto, QueryGitHistory.QueryVariable variable){
        List<Map<String, FieldAttr>> records = new ArrayList<>();

        LocalDateTime begTime = StringUtil.isBlank(variable.getBegTime())?null:LocalDateTime.parse(variable.getBegTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = StringUtil.isBlank(variable.getEndTime())?null:LocalDateTime.parse(variable.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if("commitList".equals(variable.getGroupBy())){
            List<GitCommitInfoDto> commitList = jGitService.getCommitList(variable.getDir(),variable.getUser(),begTime,endTime);
            for(GitCommitInfoDto gitCommitInfoDto:commitList){
                if("Y".equals(variable.getIgnoreMerge()) && gitCommitInfoDto.getShortMessage().startsWith("Merge ")){
                    continue;
                }
                Map<String,FieldAttr> record = new LinkedHashMap<>();
                record.put("CommitID",new FieldAttr().setValue(gitCommitInfoDto.getCommitId()).setRemarks("CommitID"));
                record.put("提交时间",new FieldAttr().setValue(gitCommitInfoDto.getCommitDateTime()).setRemarks("提交时间"));
                record.put("提交用户",new FieldAttr().setValue(gitCommitInfoDto.getCommitterIdent().getName()).setRemarks("提交用户"));
                record.put("提交备注信息",new FieldAttr().setValue(gitCommitInfoDto.getShortMessage()).setRemarks("提交备注信息"));
                records.add(record);
            }
        }else if("fileList".equals(variable.getGroupBy())) {
            Map<String, List<GitCommitInfoDto>> fileCommitList = jGitService.getFileCommintInfo(variable.getDir(), variable.getUser(), begTime, endTime);
            Map<String, List<GitCommitInfoDto>> map = new TreeMap<>(fileCommitList);
            for (String fileName : map.keySet()) {
                int i = 0;
                for (GitCommitInfoDto gitCommitInfoDto : map.get(fileName)) {
                    if ("Y".equals(variable.getIgnoreMerge()) && gitCommitInfoDto.getShortMessage().startsWith("Merge ")) {
                        continue;
                    }
                    Map<String, FieldAttr> record = new LinkedHashMap<>();
                    record.put("程序", new FieldAttr().setValue(i == 0 ? fileName : "").setRemarks("程序"));
                    record.put("提交时间", new FieldAttr().setValue(gitCommitInfoDto.getCommitDateTime()).setRemarks("提交时间"));
                    record.put("提交用户", new FieldAttr().setValue(gitCommitInfoDto.getCommitterIdent().getName()).setRemarks("提交用户"));
                    record.put("提交备注信息", new FieldAttr().setValue(gitCommitInfoDto.getShortMessage()).setRemarks("提交备注信息"));
                    records.add(record);
                    i++;
                }
            }
        }

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(false);//表格内容分页显示
        return records;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestPubDto {
        private String dir;
        private String ignoreMerge;
        private String user;
        private String begTime;
        private String endTime;
        private String groupBy; //commitList-提交的事务,fileList-以文件统计
    }
}
