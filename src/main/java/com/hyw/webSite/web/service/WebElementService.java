package com.hyw.webSite.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.DataUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.WebElementDto;
import com.hyw.webSite.web.model.*;
import com.ql.util.express.ExpressRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebElementService {

    @Autowired
    ApplicationContext context;
    @Autowired
    DataService dataService;

    @SuppressWarnings("unchecked")
    public List<WebElementDto> getMenuElements(String menu, String area){
        List<WebElementDto> webElementDtoList = new ArrayList<>();

        //取菜单元素
        List<WebElementInfo> webElementInfoList = dataService.list(new NQueryWrapper<WebElementInfo>()
                .eq(WebElementInfo::getMenu,menu)
                .eq(WebElementInfo::getArea,area)
                .orderByAsc(WebElementInfo::getElementSeq));
        for(WebElementInfo webElementInfo : webElementInfoList){
            WebElementDto webElementDto = new WebElementDto();
            webElementDto.setWebElementId(webElementInfo.getWebElementInfoId());
            webElementDto.setFunc(webElementInfo.getMenu());
            webElementDto.setSeq(String.valueOf(webElementInfo.getElementSeq()));
            webElementDto.setId(webElementInfo.getElement());
            webElementDto.setArea(webElementInfo.getArea());
            webElementDto.setWindow(webElementInfo.getSubArea());
            webElementDto.setType(webElementInfo.getElementType());
            webElementDto.setPrompt(webElementInfo.getElementDesc());

            //解析data字段
            webElementDto.setDefaultValue(getDefaultValue(menu,webElementInfo.getElement()));//默认值
            webElementDto.setDataMap(getDataValue(menu,webElementInfo.getElement()));//下拉选项

            // 属性 eg. class="xxx",width=100px,height=200px
            webElementDto.setAttrMap(getAttrMap(webElementInfo.getElementAttr(), ",", "="));

            List<EventInfo> webEventInfoList = getEventInfoList(webElementInfo.getMenu(),webElementInfo.getElement());
            webElementDto.setEventInfoList(webEventInfoList);

            webElementDtoList.add(webElementDto);
        }
        return webElementDtoList;
    }

    /**
     * 取非触发的下拉数据选项
     * @param menu 菜单
     * @param element 元素
     * @return Map<String, String>
     */
    private Map<String, String> getDataValue(String menu,String element){
        Map<String, String> dataMap = new HashMap<>();
        //取元素配置值
        WebElementData webElementData = dataService.getOne(new NQueryWrapper<WebElementData>()
                .eq(WebElementData::getMenu,menu)
                .eq(WebElementData::getElement,element)
                .eq(WebElementData::getDataType,"dataMap"));
        if(webElementData==null || StringUtil.isBlank(webElementData.getExpress())) return dataMap;

        if("optionList".equals(webElementData.getDataAttr())) {
            JSONArray jsonArray = JSONArray.parseArray(webElementData.getExpress());
            for (int i = 0; i < jsonArray.size(); i++) {
                dataMap.put((String) jsonArray.getJSONObject(i).get("value"), (String) jsonArray.getJSONObject(i).get("text"));
            }
        }else if("sql".equals(webElementData.getDataAttr())) {
            String sql= webElementData.getExpress();
            List<Map<String, Object>> dataMaps = dataService.mapList(new NQueryWrapper<>()
                    .setSql(sql));
            dataMap = DataUtil.getValueMap(dataMaps);
        }else if("fun".equals(webElementData.getDataAttr())) {
            String funBean = webElementData.getExpress();
            Map<String,Object> dataMapObject = ((WebDataReqFun) context.getBean(funBean)).execute(null);
            for(String key:dataMapObject.keySet()){
                dataMap.put(key,(String)dataMapObject.get(key));
            }
        }
        return dataMap;
    }

    /**
     * 取默认值
     * @param menu 菜单
     * @param element 元素
     * @return 默认值
     */
    private String getDefaultValue(String menu,String element){
        //取元素配置值
        WebElementData webElementData = dataService.getOne(new NQueryWrapper<WebElementData>()
                .eq(WebElementData::getMenu,menu)
                .eq(WebElementData::getElement,element)
                .eq(WebElementData::getDataType,"defaultValue"));
        if(webElementData==null || StringUtil.isBlank(webElementData.getExpress())) return null;
        if("constant".equalsIgnoreCase(webElementData.getDataAttr())) {
            return webElementData.getExpress();
        }else if("QLExpress".equalsIgnoreCase(webElementData.getDataAttr())) {
            Object result;
            try {
                ExpressRunner runner = new ExpressRunner(true, false);
                result = runner.execute(webElementData.getExpress(), null, null, true, false);
            } catch (Exception e) {
                log.error("计算表达式(" + webElementData.getExpress() + ")出错!", e);
                throw new BizException("计算表达式(" + webElementData.getExpress() + ")出错!");
            }
            return result == null ? null : result.toString();
        }
        return null;
    }

    /**
     * 取事件信息
     * @param menu 菜单
     * @param element 元素
     * @return List<EventInfo>
     */
    private List<EventInfo> getEventInfoList(String menu, String element){
        List<EventInfo> eventInfoList = new ArrayList<>();

        //取配置的事件
        List<WebEventInfo> webEventInfoList = dataService.list(new NQueryWrapper<WebEventInfo>()
            .eq(WebEventInfo::getMenu,menu)
            .eq(WebEventInfo::getElement,element));
        for(WebEventInfo webEventInfo:webEventInfoList){
            //取由该事件触发的事件
            List<WebTriggerInfo> webTriggerInfoList = dataService.list(new NQueryWrapper<WebTriggerInfo>()
                .eq(WebTriggerInfo::getSourceMenu,webEventInfo.getMenu())
                .eq(WebTriggerInfo::getSourceElement,webEventInfo.getElement()));
            if(CollectionUtil.isEmpty(webTriggerInfoList)) {
                eventInfoList.add(createEventInfo(webEventInfo,null));
            }else{
                webTriggerInfoList.forEach(trigger->eventInfoList.add(createEventInfo(webEventInfo,trigger)));
            }
        }
        return eventInfoList;
    }

    /**
     * 生成事件信息（包含该事件需要触发的事件）
     * @param webEventInfo 事件信息
     * @param webTriggerInfo 触发事件
     * @return EventInfo
     */
    private EventInfo createEventInfo(WebEventInfo webEventInfo,WebTriggerInfo webTriggerInfo){
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEvent(webEventInfo.getEventType());
        eventInfo.setType(webEventInfo.getRequestType());
        eventInfo.setId(webEventInfo.getRequestNo());
        //事件参数
        Map<String, Object> eventParam = JSON.parseObject(webEventInfo.getParam());//json转map
        eventInfo.setParamMap(eventParam);
        if (CollectionUtil.isNotEmpty(eventParam)) {
            if (eventParam.containsKey("withPage")) {
                boolean isWithPage = (boolean) eventParam.get("withPage");
                eventInfo.setWithPage(isWithPage);
            }
        }
        if(webTriggerInfo == null) return eventInfo;
        eventInfo.setRelEleChgType(webTriggerInfo.getTriggerElementType());
        eventInfo.setRelEleType(webTriggerInfo.getTriggerType());
        eventInfo.setRelEleId(webTriggerInfo.getTriggerElement());
        eventInfo.setParamMap(JSON.parseObject(webTriggerInfo.getParam()));
        return eventInfo;
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
        if (StringUtil.isBlank(s)) return attrMap;
        String[] attrs = s.split(separator);
        for (String attrExpress : attrs) {
            String[] express = attrExpress.split(connector);
            attrMap.put(express[0], express[1].replace("\"", ""));
        }
        return attrMap;
    }
}
