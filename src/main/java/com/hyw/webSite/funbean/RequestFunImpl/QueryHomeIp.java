package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.RequestFunImpl.checkIP.CheckIPConnect;
import com.hyw.webSite.funbean.RequestFunImpl.checkIP.CheckIPDto;
import com.hyw.webSite.funbean.RequestFunImpl.checkIP.CheckIPandPortThread;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("queryHomeIp")
@Slf4j
public class QueryHomeIp extends RequestFunUnit<String, QueryHomeIp.QueryVariable> {

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryHomeIp.QueryVariable variable){

    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, QueryHomeIp.QueryVariable variable){

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TEXT); //以表格形式显示

        if(CheckIPConnect.isHttpConnectable("http://" + CheckIPandPortThread.getIp() +":"+"81"+"/hyw.html")){
            return CheckIPandPortThread.getIp();
        }else{
            CheckIPandPortThread.setIp("");
        }

        int threadCount = 256;  // 64-11min;32-22min;128-6min;256-3.5min
        int port = 81; // 81   3389
        int timeount = 3000;

        System.out.println("线程数："+threadCount+",开始时间："+ LocalDateTime.now());
        List<CheckIPDto> checkIPdtos = new ArrayList<CheckIPDto>();
        add(checkIPdtos,"113.116.87.0", "113.116.87.255", port, timeount);
        add(checkIPdtos,"113.116.88.0", "113.116.88.255", port, timeount);
        add(checkIPdtos,"113.116.89.0", "113.116.89.255", port, timeount);
        add(checkIPdtos,"113.116.90.0", "113.116.90.255", port, timeount);
        add(checkIPdtos,"113.116.91.0", "113.116.91.255", port, timeount);
        add(checkIPdtos,"113.116.92.0", "113.116.92.255", port, timeount);
        add(checkIPdtos,"113.90.170.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.171.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.172.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.173.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.174.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.175.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.176.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.177.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.178.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.179.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.180.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.181.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.182.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"113.90.183.0", "113.90.179.255", port, timeount);
        add(checkIPdtos,"183.15.87.0", "183.15.87.255", port, timeount);
        add(checkIPdtos,"183.15.88.0", "183.15.88.255", port, timeount);
        add(checkIPdtos,"183.15.89.0", "183.15.89.255", port, timeount);
        add(checkIPdtos,"183.15.90.0", "183.15.90.255", port, timeount);
        add(checkIPdtos,"183.15.91.0", "183.15.91.255", port, timeount);
        add(checkIPdtos,"183.15.92.0", "183.15.92.255", port, timeount);

        int initThreadCount = Thread.activeCount();
        for(int i=1;i<=threadCount;i++){
            CheckIPandPortThread checkIPandPortThread = new CheckIPandPortThread(checkIPdtos,threadCount,i);
            checkIPandPortThread.start();
        }

        while(Thread.activeCount() > initThreadCount && StringUtil.isBlank(CheckIPandPortThread.getIp())){
            System.out.println(Thread.activeCount());
            try {
                Thread.sleep(Long.parseLong("10000"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return StringUtil.isBlank(CheckIPandPortThread.getIp())?"未找到对应IP.":CheckIPandPortThread.getIp();
    }

    public void add(List<CheckIPDto> checkIPdtos,String ipBeg, String ipEnd, int port, int timeout) {
        CheckIPDto checkIPdto = new CheckIPDto();
        checkIPdto.setBegIP(ipBeg);
        checkIPdto.setEndIP(ipEnd);
        checkIPdto.setPort(port);
        checkIPdto.setTimeout(timeout);
        checkIPdtos.add(checkIPdto);
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestFunUnit.Variable {
    }
}
