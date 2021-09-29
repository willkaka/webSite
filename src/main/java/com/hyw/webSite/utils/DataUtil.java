package com.hyw.webSite.utils;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.exception.IfThrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {

    /**
     * 将List<Map<String,Object>>转为按值保存的Map
     * @param mapList
     * @return
     */
    public static Map<String,String> getValueMap(List<Map<String,Object>> mapList){
        Map<String,String> rtnMap = new HashMap<>();
        for(Map<String,Object> map:mapList){
            IfThrow.trueThenThrowMsg(map.size()>2,"DataUtil.getValueMap要求List中的Map只能包含两个元素！");

            int index=0;
            String rtnKey="",rtnValue="";
            for (String key: map.keySet()) {
                if(index==0) rtnKey = (String) map.get(key);
                if(index==1) rtnValue = (String) map.get(key);
                index++;
            }
            rtnMap.put(rtnKey,rtnValue);
        }
        return rtnMap;
    }
}
