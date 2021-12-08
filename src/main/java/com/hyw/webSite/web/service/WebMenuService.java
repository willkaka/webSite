package com.hyw.webSite.web.service;

import com.alibaba.fastjson.JSON;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.web.dto.EventInfo;
import com.hyw.webSite.web.dto.WebElementDto;
import com.hyw.webSite.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebMenuService {

    @Autowired
    private DataService dataService;

    @Autowired
    ApplicationContext context;

    @SuppressWarnings("unchecked")
    public List<WebElementDto> getMenu(){
        List<WebElementDto> webElementDtoList = new ArrayList<>();

        //取菜单集
        int seq = 0;
        List<WebMenuGroupInfo> webMenuGroupInfoList = dataService.list(new NQueryWrapper<WebMenuGroupInfo>()
                .orderByAsc(WebMenuGroupInfo::getWebMenuGroupInfoId));
        for(WebMenuGroupInfo webMenuGroupInfo : webMenuGroupInfoList){
            WebElementDto webElementDto = new WebElementDto();
            webElementDto.setWebElementId(webMenuGroupInfo.getWebMenuGroupInfoId());
            webElementDto.setFunc("menuGroup");
            webElementDto.setSeq(String.valueOf(++seq));
            webElementDto.setId(webMenuGroupInfo.getMenuGroup());
            webElementDto.setArea("menuArea");
            webElementDto.setWindow("");
            webElementDto.setType("menu");
            webElementDto.setPrompt(webMenuGroupInfo.getGroupDesc());

            //菜单信息
            List<WebMenuInfo> webMenuInfoList = dataService.list(new NQueryWrapper<WebMenuInfo>()
                    .eq(WebMenuInfo::getMenuGroup,webMenuGroupInfo.getMenuGroup())
                    .orderByAsc(WebMenuInfo::getMenuSeq));
            List<WebElementDto> subWebElementList = new ArrayList<>();
            for(WebMenuInfo webMenuInfo:webMenuInfoList){
                WebElementDto subWebElement = new WebElementDto();
                subWebElement.setWebElementId(webMenuInfo.getWebMenuInfoId());
                subWebElement.setFunc(webMenuInfo.getMenuGroup());
                subWebElement.setSeq(String.valueOf(webMenuInfo.getMenuSeq()));
                subWebElement.setId(webMenuInfo.getMenu());
                subWebElement.setArea("menuArea");
                subWebElement.setWindow("");
                subWebElement.setType("menu");
                subWebElement.setPrompt(webMenuInfo.getMenuDesc());

                List<EventInfo> webEventInfoList = getEventInfoList(webMenuInfo.getMenu(),null);
                subWebElement.setEventInfoList(webEventInfoList);

                subWebElementList.add(subWebElement);
            }
            webElementDto.setSubElements(subWebElementList);
            webElementDtoList.add(webElementDto);
        }
        return webElementDtoList;
    }


    private List<EventInfo> getEventInfoList(String menu, String element){
        List<EventInfo> eventInfoList = new ArrayList<>();

        //将web_event_info转为eventInfo
        List<WebEventInfo> webEventInfoList = dataService.list(new NQueryWrapper<WebEventInfo>()
                .eq(WebEventInfo::getMenu,menu)
                .eq(WebEventInfo::getElement,element));
        for(WebEventInfo webEventInfo:webEventInfoList){
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEvent(webEventInfo.getEventType());
            eventInfo.setType(webEventInfo.getRequestType());
            eventInfo.setId(webEventInfo.getRequestNo());
//            eventInfo.setTriggerElement(webEventInfo.);
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
