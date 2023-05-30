package com.hyw.webSite.service;

import com.hyw.webSite.dao.SysParam;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.annotation.NTransactional;
import com.hyw.webSite.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private DataService dataService;

    @NTransactional(rollbackWhen = BizException.class)
    public String test001(){
//        SysParam sysParam = new SysParam();
//        sysParam.setParamRoot("dataSwitch").setParamName("data");
//        dataService.save(sysParam);

        List<SysParam> sysParamList = dataService.list(new NQueryWrapper<SysParam>().setTable(SysParam.class));
        System.out.println("查询记录数："+sysParamList.size());
        for(SysParam sysParam:sysParamList){
            sysParam.setParamDesc("test001");
            dataService.update(sysParam,SysParam::getSysParamId);
        }


//        BizException.trueThrow(true,"rollback");
//        throw new RuntimeException();
        return "success";
    }
}
