package com.hyw.webSite.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.dao.ConfigDatabaseInfo;
import com.hyw.webSite.dao.WebConfigMenu;
import com.hyw.webSite.dao.WebConfigReq;
import com.hyw.webSite.dao.WebElement;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.model.SpringDatabaseConfig;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DataUtil;
import com.hyw.webSite.utils.DbUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElementDto;
import com.ql.util.express.ExpressRunner;
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
    private SpringDatabaseConfig springDatabaseConfig;
    @Autowired
    private SpringDataSourceService springDataSourceService;
    @Autowired
    private DataService dataService;

    @Autowired
    ApplicationContext context;

    public List<WebElementDto> getWebConfigElement(RequestDto requestDto, String elementParent, String elementArea) {
        List<WebElementDto> webElementDtoList = new ArrayList<>();
        Map<String,Object> changedEleMap = new HashMap<>();
        Map<String,String> inputValue = new HashMap<>();
        Map<String,String> curInputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");

        List<WebElement> webElementList = dataService.list(new NQueryWrapper<WebElement>()
            .eq(WebElement::getArea,elementArea)
            .eq(WebElement::getFunction,elementParent)
            .orderByAsc(WebElement::getSeq));
        for(WebElement webElement : webElementList){
            WebElementDto webElementDto = new WebElementDto();
            webElementDto.setWebElementId(webElement.getWebElementId());
            webElementDto.setFunction(webElement.getFunction());
            webElementDto.setSeq(webElement.getSeq());
            webElementDto.setId(webElement.getId());
            webElementDto.setArea(webElement.getArea());
            webElementDto.setWindow(webElement.getWindow());
            webElementDto.setType(webElement.getType());
            webElementDto.setPrompt(webElement.getPrompt());

            //解析data字段
            String dataString = webElement.getData();
            if(StringUtil.isNotBlank(dataString)) {
                JSONObject jsonData = JSONObject.parseObject(dataString);
                //默认值
                JSONObject defaultValueObject = jsonData.getJSONObject("defaultValueObject");
                if(null != defaultValueObject) {
                    Object result = null;
                    if ("QLExpress".equals(defaultValueObject.getString("type")) &&
                            StringUtil.isNotBlank(defaultValueObject.getString("express"))) {
                        try {
                            ExpressRunner runner = new ExpressRunner(true, false);
                            result = runner.execute(defaultValueObject.getString("express"), null, null, true, false);
                        } catch (Exception e) {
                            log.error("计算表达式(" + defaultValueObject.getString("express") + ")出错!",e);
                            throw new BizException("计算表达式(" + defaultValueObject.getString("express") + ")出错!");
                        }
                    } else if ("value".equals(defaultValueObject.getString("type"))) {
                        result = defaultValueObject.getString("value");
                    }
                    webElementDto.setDefaultValue((String) result);
                }

                String dataType = jsonData.getString("dataType");
                Map<String, String> dataMap = new HashMap<>();
                if("optionList".equals(dataType)) {
                    JSONArray jsonArray = jsonData.getJSONArray("optionList");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        dataMap.put((String) jsonArray.getJSONObject(i).get("value"), (String) jsonArray.getJSONObject(i).get("text"));
                    }
                }else if("sql".equals(dataType)) {
                    List<Map<String, Object>> dataMaps = new ArrayList<>();
                    String sql= jsonData.getString("sql");
                    String dataBase = jsonData.getString("dataBase");
                    String libName = jsonData.getString("libName");
                    if(StringUtil.isNotBlank(dataBase)){
                        if("#".equals(dataBase.substring(0,1))){
                            String dataBaseNameKey = dataBase.substring(1);
                            String dataBaseName = (String) curInputValue.get(dataBaseNameKey);
                            String libNameKey = libName.substring(1);;
                            String libNameN = (String) curInputValue.get(libNameKey);
                            if(StringUtil.isNotBlank(dataBaseName) && StringUtil.isNotBlank(libNameN)){
//                                ConfigDatabaseInfo configDatabaseInfo = configDatabaseInfoService.getDatabaseConfig(dataBaseName);
                                ConfigDatabaseInfo configDatabaseInfo = dataService.getOne(new NQueryWrapper<ConfigDatabaseInfo>()
                                        .eq(ConfigDatabaseInfo::getDatabaseName,dataBaseName));
                                configDatabaseInfo.setDatabaseLabel(libNameN);
                                Connection connection = DbUtil.getConnection(configDatabaseInfo);
                                dataMaps = DbUtil.getSqlRecords(connection,sql);
                            }
                        }
                    }else {
                        dataMaps = DbUtil.getSqlRecords(springDataSourceService.getSpringDatabaseConnection(), sql);
                    }
                    dataMap = DataUtil.getValueMap(dataMaps);
                }else if("fun".equals(dataType)) {
                    String funBean = jsonData.getString("funBean");
                    Map<String,Object> dataMapObject = ((WebDataReqFun) context.getBean(funBean)).execute(null);
                    for(String key:dataMapObject.keySet()){
                        dataMap.put(key,(String)dataMapObject.get(key));
                    }
                }else if("cod".equals(dataType)) {
                    String code = dataString.substring(4);
                    dataMap = getWebConfigEnum(code);
                }
                webElementDto.setDataMap(dataMap);
            }

            if(CollectionUtil.isEmpty(webElementDto.getDataMap())){
                WebElementDto tempWebElementDto = (WebElementDto)changedEleMap.get(webElement.getId());
                if(tempWebElementDto != null) {
                    webElementDto.setDataMap(tempWebElementDto.getDataMap());
                }
            }

            // 属性 eg. class="xxx",width=100px,height=200px
            webElementDto.setAttrMap(getAttrMap(webElement.getAttr(), ",", "="));

            // 事件类型，事件处理ID；数据库按：
            // {"eventList":[{"event":"click","type":menuReq","id":"MR001"},{"event":"click","type":menuReq","id":"MR001"}]}
            List<EventInfo> eventInfos = new ArrayList<>();
            String configEvent = webElement.getEvent();
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
                    eventInfo.setSelectedValue(CollectionUtil.getMapFirstOrNull(webElementDto.getDataMap()));
                    eventInfo.setParamMap(event.getJSONObject("paramMap"));
                    eventInfo.setWithPage(null==event.getBoolean("withPage")?false:event.getBoolean("withPage"));
                    eventInfos.add(eventInfo);

                    inputValue.put(webElement.getId(),CollectionUtil.getMapFirstOrNull(webElementDto.getDataMap()));
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
            webElementDto.setEventInfoList(eventInfos);
            webElementDto.setSubElements(getWebConfigElement(requestDto, webElement.getId(),elementArea));
            webElementDtoList.add(webElementDto);
        }
        return webElementDtoList;
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
                String fun= dataString.substring(4,dataString.length());
                Map<String,Object> dataMapObject = ((WebDataReqFun) context.getBean(fun)).execute(null);
                for(String key:dataMapObject.keySet()){
                    dataMap.put(key,(String)dataMapObject.get(key));
                }
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
            List<WebConfigMenu> webConfigMenuList = dataService.list(new NQueryWrapper<WebConfigMenu>()
                .eq(WebConfigMenu::getEnumKey,enumKey)
                .orderByAsc(WebConfigMenu::getEnumSeq));
            for(WebConfigMenu webConfigMenu:webConfigMenuList){
                enumKeyValueMap.put(webConfigMenu.getEnumValue(), webConfigMenu.getEnumText());
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
        WebConfigReq webConfigReq = dataService.getOne(new NQueryWrapper<WebConfigReq>()
            .eq(WebConfigReq::getReqName,reqName)
            .eq(WebConfigReq::getReqType,reqType));
        if (webConfigReq != null) {
            return webConfigReq.getBeanName();
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
