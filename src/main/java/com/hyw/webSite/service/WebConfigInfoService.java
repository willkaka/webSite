package com.hyw.webSite.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.dto.DynamicTableDto;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.model.SpringDatabaseConfig;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DataUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebConfigInfoService {

    @Autowired
    private DynamicTableService dynamicTableService;

    @Autowired
    private SpringDatabaseConfig springDatabaseConfig;
    @Autowired
    private SpringDataSourceService springDataSourceService;

    @Autowired
    ApplicationContext context;

    public List<WebElement> getWebConfigElement(String elementParent, String elementArea) {

        Map<String,Object> changedEleMap = new HashMap<>();
        Map<String,String> inputValue = new HashMap<>();
        RequestDto requestDto = new RequestDto();

        List<WebElement> webElements = new ArrayList<>();
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName("web_element");
        dynamicTableDto.setSelectFields("id,seq,function,area,type,prompt,data,attr,event");
        dynamicTableDto.setSelectWhere("area = '" + elementArea + "' AND function='" + elementParent + "' ");
        dynamicTableDto.setSelectOrderby("seq");
        List<Map<String, Object>> rtnListMap = dynamicTableService.selectAll(dynamicTableDto);
        for (Map<String, Object> rtnMap : rtnListMap) {
            WebElement webElement = new WebElement();
            webElement.setId((String) rtnMap.get("id"));
            webElement.setSeq((String) rtnMap.get("seq"));
            webElement.setFunction((String) rtnMap.get("function"));
            webElement.setArea((String) rtnMap.get("area"));
            webElement.setType((String) rtnMap.get("type"));
            webElement.setPrompt((String) rtnMap.get("prompt"));
            webElement.setDataMap(getDataMap((String) rtnMap.get("data")));

            if(CollectionUtil.isEmpty(webElement.getDataMap())){
                WebElement tempWebElement = (WebElement)changedEleMap.get(webElement.getId());
                if(tempWebElement != null) {
                    webElement.setDataMap(tempWebElement.getDataMap());
                }
            }

            // 属性 eg. class="xxx",width=100px,height=200px
            webElement.setAttrMap(getAttrMap((String) rtnMap.get("attr"), ",", "="));

            // 事件类型，事件处理ID；数据库按：
            // {"eventList":[{"event":"click","type":menuReq","id":"MR001"},{"event":"click","type":menuReq","id":"MR001"}]}
            List<EventInfo> eventInfos = new ArrayList<>();
            String configEvent = (String) rtnMap.get("event");
            if(StringUtil.isNotBlank(configEvent)) {
                JSONObject jsonData = JSONObject.parseObject(configEvent);
                JSONArray jsonArray = jsonData.getJSONArray("eventList");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject event = jsonArray.getJSONObject(i);
                    EventInfo eventInfo = new EventInfo();
                    eventInfo.setEvent(event.getString("event"));
                    eventInfo.setType(event.getString("type"));
                    eventInfo.setId(event.getString("id"));
                    eventInfo.setRelEleId(event.getString("relEleId"));
                    eventInfo.setRelEleType(event.getString("relEleType"));
                    eventInfo.setRelEleChgType(event.getString("relEleChgType"));
                    eventInfo.setSelectedValue(CollectionUtil.getMapFirstOrNull(webElement.getDataMap()));
                    eventInfos.add(eventInfo);

                    inputValue.put(webElement.getId(),CollectionUtil.getMapFirstOrNull(webElement.getDataMap()));
                    Map<String,Object> reqParm = new HashMap<>();
                    reqParm.put("inputValue",inputValue);
                    requestDto.setReqParm(reqParm);
                    requestDto.setEventInfo(eventInfo);
                    if(StringUtil.isNotBlank(eventInfo.getType())
                            && "webDataReq".equals(eventInfo.getType())
                            && StringUtil.isNotBlank(eventInfo.getId())) {
                        changedEleMap.putAll(((WebDataReqFun) context.getBean(eventInfo.getId())).execute(requestDto));
                    }

                }
            }
            webElement.setEventInfoList(eventInfos);
            webElement.setSubElements(getWebConfigElement(webElement.getId(),elementArea));

            webElements.add(webElement);
        }

        return webElements;
    }

    /**
     * 解析分隔符和连接符组成的字符串
     *
     * @param s         字符串
     * @param separator 分隔符
     * @param connector 连接符
     * @return Map<String, String>
     */
    public Map<String, String> getAttrMap(String s, String separator, String connector) {
        Map<String, String> attrMap = new HashMap<>();

        if (StringUtil.isNotBlank(s)) {
            String[] attrs = s.split(separator);
            for (String attrExpress : attrs) {
                String[] express = attrExpress.split(connector);
                attrMap.put(express[0], express[1].replace("\"", ""));
            }
        }
        return attrMap;
    }

    /**
     * 数值处理
     * @param dataString web_element.data
     * @return Map<String,String>
     */
    public Map<String,String> getDataMap(String dataString){
        Map<String,String> dataMap = new HashMap<>();

        if(StringUtil.isBlank(dataString)) return null;

        String dataPre = dataString.substring(0,4);
        switch (dataPre){
            case "sql:":
                String sql= dataString.substring(4,dataString.length());
                List<Map<String,Object>> dataMaps = DbUtil.getSqlRecords(springDataSourceService.getSpringDatabaseConnection(),sql);
                dataMap = DataUtil.getValueMap(dataMaps);
                break;
            case "fun:":
//                String fun= dataString.substring(4,dataString.length());
//                dataMap = getDataWithFun(fun);
                break;
            case "cod:":
                String code = dataString.substring(4,dataString.length());
                dataMap = getWebConfigEnum(code);
                break;
            default:
                if(StringUtil.isNotBlank(dataString)){
                    List<String> array = getStringList(dataString,',','"');
                    for(String map:array){
                        List<String> mapArray = getStringList(map,':','"');
                        if(mapArray.size()>1) {
                            dataMap.put(mapArray.get(0).replace("\"",""), mapArray.get(1).replace("\"",""));
                        }
                    }
                }
        }
        return dataMap;
    }

    /**
     * 取字段枚举值
     *
     * @param enumKey 枚举关键字
     * @return Map<String, String>
     */
    public Map<String, String> getWebConfigEnum(String enumKey) {
        List<Map<String, Object>> rtnListMap = new ArrayList<>();
        Map<String, String> enumKeyValueMap = new HashMap<>();

        String tableName, selectFields, whereCondition, orderBy, rtnMapKey = null, rtnMapValue = null;

        if (StringUtil.isNotBlank(enumKey) && enumKey.length() > 4 && enumKey.substring(0, 4).equals("sql:")) {
            String sql = enumKey.substring(4);

            String dbType = "";
            if (springDatabaseConfig.getDriverClass().toLowerCase().contains("sqlite"))
                dbType = Constant.DB_TYPE_SQLITE;
            if (springDatabaseConfig.getDriverClass().toLowerCase().contains("oracle"))
                dbType = Constant.DB_TYPE_ORACLE;
            if (springDatabaseConfig.getDriverClass().toLowerCase().contains("mysql")) dbType = Constant.DB_TYPE_MYSQL;
            Connection connection = DbUtil.getConnection(dbType,
                    springDatabaseConfig.getDriverClass(), springDatabaseConfig.getUrl(), null, null,
                    springDatabaseConfig.getUserName(), springDatabaseConfig.getPassword());
            rtnListMap = DbUtil.getSqlRecords(connection, sql);

            if (rtnListMap != null) {
                for (Map<String, Object> rtnMap : rtnListMap) {
                    for (String fieldName : rtnMap.keySet()) {
                        enumKeyValueMap.put((String) rtnMap.get(fieldName), (String) rtnMap.get(fieldName));
                    }
                }
            }
        } else {
            tableName = "web_config_enum";
            selectFields = "enum_key,enum_seq,enum_value,enum_text";
            whereCondition = " enum_key = '" + enumKey + "' ";
            orderBy = "enum_seq";
            rtnMapKey = "enum_value";
            rtnMapValue = "enum_text";

            DynamicTableDto dynamicTableDto = new DynamicTableDto();
            dynamicTableDto.setTableName(tableName);
            dynamicTableDto.setSelectFields(selectFields);
            dynamicTableDto.setSelectWhere(whereCondition);
            dynamicTableDto.setSelectOrderby(orderBy);
            rtnListMap = dynamicTableService.selectAll(dynamicTableDto);
            if (rtnListMap != null) {
                for (Map<String, Object> rtnMap : rtnListMap) {
                    enumKeyValueMap.put((String) rtnMap.get(rtnMapKey), (String) rtnMap.get(rtnMapValue));
                }
            }
        }
        return enumKeyValueMap;
    }

    /**
     * 取页面请求配置表执行bean
     *
     * @param reqMapping Mapping
     * @param reqName    请求名称
     * @param reqType    请求类型
     * @return bean name
     */
    public String getReqBean(String reqMapping, String reqName, String reqType) {
        List<WebElement> webElements = new ArrayList<>();
        DynamicTableDto dynamicTableDto = new DynamicTableDto();
        dynamicTableDto.setTableName("web_config_req");
        dynamicTableDto.setSelectFields("bean_name");
        dynamicTableDto.setSelectWhere(" req_mapping = '" + reqMapping + "' AND req_name='" + reqName + "' AND req_type='" + reqType + "' ");
        List<Map<String, Object>> rtnListMap = dynamicTableService.selectAll(dynamicTableDto);
        if (rtnListMap != null) {
            String beanName = (String) rtnListMap.get(0).get("bean_name");
            return beanName;
        }
        return null;
    }

    public List<String> getStringList(String str,char separator,char qMark){
        List<String> rtnList = new ArrayList<>();
        int start = 0;
        boolean mark = false;
        for(int i=0;i < str.length();i++) {
            if(str.charAt(i) == qMark && mark){
                mark = false;
                continue;
            }
            if(str.charAt(i) == qMark && !mark){
                mark = true;
                continue;
            }
            if(mark) continue;

            if(str.charAt(i) == separator){
                rtnList.add(str.substring(start,i));
                start=i+1;
            }
        }
        if(start < str.length()) {
            rtnList.add(str.substring(start, str.length()));
        }
        return rtnList;
    }
}
