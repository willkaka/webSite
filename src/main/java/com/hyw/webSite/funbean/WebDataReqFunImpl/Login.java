package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.dto.EventInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("login")
@Slf4j
public class Login implements RequestFun {

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String userName = (String) inputValue.get("modaluserName");
        BizException.trueThrow(StringUtil.isBlank(userName),"用户名不允许为空值!");
        String passWord = (String) inputValue.get("modalpwd");
        BizException.trueThrow(StringUtil.isBlank(passWord),"密码不允许为空值!");

//        ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(userName);
//        configDatabaseInfo.setDatabaseLabel(passWord);
//        Connection connection = DbUtil.getConnection(configDatabaseInfo);
//
//        DbUtil.closeConnection(connection);

        Map<String,Object> webNextOprMap = new HashMap<>();
        webNextOprMap.put("alert","true");//是否提示成功
        webNextOprMap.put("sucMsg","已登录成功！"); //提示信息
        webNextOprMap.put("type","hide");//执行任务类型
        webNextOprMap.put("hideEle","swBackGround"); //更新成功后关闭更新子窗口。
        webNextOprMap.put("execFun","setUserNameIntoCookie");
        webNextOprMap.put("param",userName);
        //{"eventList":[{"event":"click","type":"buttonReq","id":"queryTableRecords"}]}
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEvent("click");
        eventInfo.setType("");
        eventInfo.setId("initPageInfo");
        webNextOprMap.put("callEven",eventInfo);//重新查询数据，实现自动刷新功能
        returnDto.setWebNextOpr(webNextOprMap);
        return returnDto;
    }
}
