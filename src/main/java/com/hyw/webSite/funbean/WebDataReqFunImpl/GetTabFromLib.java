package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.*;

@Service("getTabFromLib")
@Slf4j
public class GetTabFromLib implements WebDataReqFun {

    @Autowired
    private DataService dataService;

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        Map<String,Object> changedEleMap = new HashMap<>();
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String selectedDb = inputValue.get("dbName");
        String selectedLib = inputValue.get("libName");

        Connection connection = dataService.getSpringDatabaseConnection(selectedDb,selectedLib);
        List<String> tables = DbUtil.getTableNames(connection, selectedLib);
        DbUtil.closeConnection(connection);

        Map<String,String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 升序排序
                        return obj1.compareTo(obj2);
                    }
                });
        for(String table:tables){
            map.put(table,table);
        }
        WebElementDto webElementDto = new WebElementDto();
        webElementDto.setId(eventInfo.getRelEleId());
        webElementDto.setType(eventInfo.getRelEleType());
        webElementDto.setChgType(eventInfo.getRelEleChgType());
        webElementDto.setDataMap(map);
        changedEleMap.put(eventInfo.getRelEleId(), webElementDto);

        return changedEleMap;
    }
}
