package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("queryDatabase")
public class QueryDatabase implements RequestFun {

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        List<String> dbList = new ArrayList<>();

//        ConfigDatabaseInfo configDatabaseInfo = (ConfigDatabaseInfo) reqMap.get("configDatabaseInfo");
//
//        if(Constant.DB_TYPE_SQLITE.equals(configDatabaseInfo.getDatabaseType())){
//            dbList.add(configDatabaseInfo.getDatabaseLabel());
//        }else{
//            Connection connection = DbUtil.getConnection(configDatabaseInfo);
//            dbList = DbUtil.getDbAllLibraries(connection);
//        }
//        Map<String,Object> rtnMap = new HashMap<>();
//        rtnMap.put("dbList", dbList);

        return returnDto;
    }
}
