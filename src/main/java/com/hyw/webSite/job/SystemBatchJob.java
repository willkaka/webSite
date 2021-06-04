package com.hyw.webSite.job;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Order(value=2) //优先级
public class SystemBatchJob implements CommandLineRunner {

    @Autowired
    private AsyncService asyncService;

    private LocalDateTime lastFreshTime; //作业配置上次刷新时间
    private long jobFreshFrequencySecond = 600; //作业配置刷新频率(秒)

    @Override
    public void run(String... args) throws Exception {
        log.info("已启动批次执行监控任务。");
        lastFreshTime = LocalDateTime.now();
        Map<String,String> jobExecTimeMap = new HashMap<>();
        JobConfig jobConfig = new JobConfig();
        jobConfig.setJobName("BatchJob");
        // 系统启动时执行
//        while (true) {
//            try {
//                //log.info("-------------------1111------------------");
//                CronExpression exp = new CronExpression("0 0 23 * * ? ");
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                String nowTime = LocalDateTime.now().format(dateTimeFormatter);
//                boolean cronCheck = exp.isSatisfiedBy(simpleDateFormat.parse(nowTime));
//                //cron时间检查通过且按秒记录的上次执行时间与当前不一样，或手工执行标志为Y,时执行
//                if (cronCheck){
//                    if(!jobExecTimeMap.containsKey(jobConfig.getJobName())
//                        || jobExecTimeMap.containsKey(jobConfig.getJobName()) &&
//                             !jobExecTimeMap.get(jobConfig.getJobName()).equals(nowTime)
//                        ) {
//                        //异步执行
//                        asyncService.asyncExecJob(jobConfig);
//                        jobExecTimeMap.put(jobConfig.getJobName(), nowTime);
//                        jobConfig.setJobStatus("I");
//                    }
//                }
//            }catch (Exception e){
//                log.error("System Job Error!",e);
//            }
//        }
    }
}

@Getter
@Setter
class JobConfig {
    private String jobName;
    private String jobStatus;
    private String classPath;
    private String lastExecMsg;
    private LocalDateTime lastExecTime;
}