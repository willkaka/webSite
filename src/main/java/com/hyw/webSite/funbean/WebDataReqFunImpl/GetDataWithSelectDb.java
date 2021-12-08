package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.WebElementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("getDataWithSelectDb")
@Slf4j
public class GetDataWithSelectDb implements WebDataReqFun {

    @Autowired
    private DataService dataService;

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        Map<String,Object> changedEleMap = new HashMap<>();
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String selectedDb = inputValue.get("dbName");
        String selectedLib = inputValue.get("libName");
        Map<String,Object> specialValue = (Map<String, Object>) eventInfo.getParamMap().get("specialValue");

        //处理sql
        if(null == eventInfo.getParamMap()) return changedEleMap;
        if(StringUtil.isBlank(selectedDb) || StringUtil.isBlank(selectedLib)) return changedEleMap;

        String sql = getSqlStm(eventInfo,inputValue);
        Connection connection = dataService.getDatabaseConnection(selectedDb,selectedLib);

        List<Map<String,Object>> records = DbUtil.getSqlRecords(connection,sql);
        dataService.closeConnection(connection);

        Map<String,String> map = new HashMap<>();
        //特殊值
        if(CollectionUtil.isNotEmpty(specialValue)){
            for(String key:specialValue.keySet()){
                map.put(key,(String) specialValue.get(key));
            }
        }
        //sql查询值
        for(Map<String,Object> record:records){
            map.put((String)record.get("value"),(String)record.get("name"));
        }
        WebElementDto webElementDto = new WebElementDto();
        webElementDto.setId(eventInfo.getRelEleId());
        webElementDto.setType(eventInfo.getRelEleType());
        webElementDto.setChgType(eventInfo.getRelEleChgType());
        webElementDto.setDataMap(map);
        changedEleMap.put(eventInfo.getRelEleId(), webElementDto);

        return changedEleMap;
    }

    /**
     * 解析sql
     * @param eventInfo 事件定义
     * @param inputValue 输入
     * @return sql
     */
    private String getSqlStm(EventInfo eventInfo,Map<String,String> inputValue){
        String sql = (String) eventInfo.getParamMap().get("sql");

        //在where条件中增加定义的条件
        Map<String,Object> sqlParamMap = (Map<String, Object>) eventInfo.getParamMap().get("sqlParam");
        if(CollectionUtil.isNotEmpty(sqlParamMap)){
            for(String key:sqlParamMap.keySet()){
                String paramValueId = (String) sqlParamMap.get(key);
                if(paramValueId.startsWith("#")){
                    String id = paramValueId.substring(1);
                    String paramValue = inputValue.get(id);
                    if(StringUtil.isNotBlank(paramValue)){
                        sql = sql + " and " + key + " = '" + paramValue + "'";
                    }
                }
            }
        }

        //group by
        String sqlGroup = (String) eventInfo.getParamMap().get("sqlGroup");
        if(StringUtil.isNotBlank(sqlGroup)){
            sql = sql + " " + sqlGroup;
        }

        //order by
        String sqlOrder = (String) eventInfo.getParamMap().get("sqlOrder");
        if(StringUtil.isNotBlank(sqlOrder)){
            sql = sql + " " + sqlOrder;
        }
        return sql;
    }
}
