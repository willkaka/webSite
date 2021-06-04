package com.hyw.webSite.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
public class AsyncService {


    /**
     * 异步执行任务
     * @param jobConfig jobConfig
     */
    @Async
    @Transactional
    public void asyncExecJob(JobConfig jobConfig) {
        //初始作业执行状态
        jobConfig.setJobStatus("I");
        jobConfig.setLastExecMsg("");
        jobConfig.setLastExecTime(LocalDateTime.now());

        //执行参数
        String execStatus,execMessage;
        //异步执行
        log.info("开始执行{}",jobConfig.getJobName());
        try {
            //jobProcessor.process(jobConfig.getTaskXmlFile(), shardingContext);
            execStatus = "S";
            execMessage = "success";
        }catch (Exception e){
            execStatus = "F";
            execMessage = e.getMessage();
            log.error("执行{}异常！",jobConfig.getJobName(),e);
        }
        log.info("完成执行{}",jobConfig.getJobName());

        //更新执行结果状态
        jobConfig.setJobStatus(execStatus);
        jobConfig.setLastExecMsg(execMessage);
    }
}
