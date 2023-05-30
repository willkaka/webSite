package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.exception.IfThrow;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.WebElementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("getJsonFields")
@Slf4j
public class GetJsonFields implements WebDataReqFun {

    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        Map<String,Object> changedEleMap = new HashMap<>();
        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String jsonString = inputValue.get("jsonString");
        String key = inputValue.get("key");

        JSONArray jsonArray = null;
        if(jsonString.startsWith("[")) {
            jsonArray = JSONObject.parseArray(jsonString);
        }else {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            jsonArray = jsonObject.getJSONArray(key);
        }
        IfThrow.isTrue(jsonArray==null,"jsonArray不允许为空!");

        Map<String,String> map = new TreeMap<String, String>(
        new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                // 升序排序
                return obj1.compareTo(obj2);
            }
        });
        for(int i=0;i<jsonArray.size();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            for (String field : object.keySet()){
                map.put(field, field);
            }
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
