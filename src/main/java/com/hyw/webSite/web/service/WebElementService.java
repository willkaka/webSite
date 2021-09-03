package com.hyw.webSite.web.service;

import com.alibaba.fastjson.JSON;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebElementService {

    @Autowired
    private DataService dataService;

    @SuppressWarnings("unchecked")
    public List<WebElementDto> getMenuElements(String menu,String area){
        List<WebElementDto> webElementDtoList = new ArrayList<>();

        //取菜单元素
        List<WebElementInfo> webElementInfoList = dataService.list(new NQueryWrapper<WebElementInfo>()
                .eq(WebElementInfo::getMenu,menu)
                .eq(WebElementInfo::getArea,area)
                .orderByAsc(WebElementInfo::getElementSeq));

        int seq = 0;
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

            List<EventInfo> webEventInfoList = getEventInfoList(webElementInfo.getMenu(),webElementInfo.getElement());
            webElementDto.setEventInfoList(webEventInfoList);

            webElementDtoList.add(webElementDto);
        }
        return webElementDtoList;
    }

    private List<EventInfo> getEventInfoList(String menu, String element){
        List<EventInfo> eventInfoList = new ArrayList<>();

        //取配置的事件
        NQueryWrapper<WebEventInfo> nQueryWrapper = new NQueryWrapper<WebEventInfo>()
                .eq(WebEventInfo::getMenu,menu);
        if(StringUtil.isNotBlank(element)){
            nQueryWrapper.eq(WebEventInfo::getElement,element);
        }

        //将web_event_info转为eventInfo
        List<WebEventInfo> webEventInfoList = dataService.list(nQueryWrapper);
        for(WebEventInfo webEventInfo:webEventInfoList){
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEvent(webEventInfo.getEventType());
            eventInfo.setType(webEventInfo.getRequestType());
            eventInfo.setId(webEventInfo.getRequestNo());

//            eventInfo.setSelectedValue(CollectionUtil.getMapFirstOrNull(webElementDto.getDataMap()));

            //事件参数
            Map<String,Object> eventParam = JSON.parseObject(webEventInfo.getParam());//json转map
            eventInfo.setParamMap(eventParam);
            if(CollectionUtil.isNotEmpty(eventParam)) {
                if(eventParam.containsKey("withPage")) {
                    boolean isWithPage = (boolean) eventParam.get("withPage");
                    eventInfo.setWithPage(isWithPage);
                }
            }
            eventInfoList.add(eventInfo);
        }
        return eventInfoList;
    }
}
