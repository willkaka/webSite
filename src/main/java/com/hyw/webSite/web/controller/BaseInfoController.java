package com.hyw.webSite.web.controller;

import com.alibaba.fastjson.JSON;
import com.hyw.webSite.constant.Constant;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.service.ConfigDatabaseInfoService;
import com.hyw.webSite.service.WebConfigInfoService;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.EventInfo;
import com.hyw.webSite.web.model.WebElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BaseInfoController {

    @Autowired
    private WebConfigInfoService webConfigInfoService;

    @Autowired
    private ConfigDatabaseInfoService configDatabaseInfoService;

    @Autowired
    ApplicationContext context;

    /**
     * 初始页面
     * @param model model
     * @return index.html
     */
    @RequestMapping("")
    public String startRequest(Model model) {
        model.addAttribute("webSiteName", Constant.WEB_SITE_TITLE);
        return "index";
    }

    @RequestMapping("index2")
    public String startRequest2(Model model) {
        model.addAttribute("webSiteName", Constant.WEB_SITE_TITLE);
        return "index2";
    }

    /**
     * 按钮请求(只改变输出区域数据)
     * @param requestDto 前台传入参数
     * @return ReqJsonDto 后台返回参数
     */
    @RequestMapping(value="initPageInfo")
    @ResponseBody
    public ReturnDto initPageInfo(@RequestBody RequestDto requestDto){
        log.info("后台收到请求initPageInfo");
        log.info("请求报文内容{}",JSON.toJSONString(requestDto));

        ReturnDto returnDto = new ReturnDto();
        returnDto.setRtnCode("0000");
        returnDto.setRtnMsg("success");

        //取站点名称
        returnDto.getTitleInfoMap().put("isChanged", true);
        returnDto.getTitleInfoMap().put("webName", Constant.WEB_SITE_TITLE);

        //取菜单清单
        List<WebElement> menuList = webConfigInfoService.getWebConfigElement("root", "menuArea");//返回要显示的菜单项;
        returnDto.getMenuMap().put("isChanged", true);
        returnDto.getMenuMap().put("menuList", menuList);

        //取导航清单
        returnDto.getNavMap().put("isChanged", false);
        returnDto.getNavMap().put("navList", null);

        //取输入区域元素清单
        returnDto.getInputMap().put("isChanged", false);
        returnDto.getInputMap().put("inputList", null);

        //取输出区域元素清单
        returnDto.getOutputMap().put("isChanged", false);
        returnDto.getOutputMap().put("outputList", null);

        log.info("返回报文内容{}",returnDto);
        return returnDto;
    }

    /**
     * 菜单请求
     * @param requestDto 前台传入参数
     * @return ReqJsonDto 后台返回参数
     */
    @RequestMapping(value="/menuReq/{eventId}")
    @ResponseBody
    public ReturnDto menuReq(@PathVariable String eventId, @RequestBody RequestDto requestDto){
        log.info("后台收到请求/menuReq/{}", eventId);
        log.info("请求报文内容{}",JSON.toJSONString(requestDto));
        ReturnDto returnDto = new ReturnDto();

        //取站点名称
        returnDto.getTitleInfoMap().put("isChanged", false);

        //取菜单清单
        returnDto.getMenuMap().put("isChanged", false);

        //取导航清单
        returnDto.getNavMap().put("isChanged", false);

        //取菜单输入输出格式信息
        List<WebElement> formatInfoList = webConfigInfoService.getWebConfigElement(eventId, "outputArea");
        formatInfoList.addAll(webConfigInfoService.getWebConfigElement(eventId, "modalArea"));
        returnDto.getFormatInfoMap().put("isChanged", true);
        returnDto.getFormatInfoMap().put("formatInfoList", formatInfoList);

        //取输入区域元素清单
        List<WebElement> inputList = webConfigInfoService.getWebConfigElement(eventId, "inputArea");
        returnDto.getInputMap().put("isChanged", true);
        returnDto.getInputMap().put("inputList", inputList);

        //取输出区域元素清单
        returnDto.getOutputMap().put("isChanged", false);
        returnDto.getOutputMap().put("isClear",true);

        returnDto.setRtnCode("0000");
        returnDto.setRtnMsg("success");

        log.info("返回报文内容{}",returnDto);
        return returnDto;
    }

    /**
     * 按钮请求(只改变输出区域数据)
     * @param requestDto 前台传入参数
     * @return ReqJsonDto 后台返回参数
     */
    @RequestMapping(value="/buttonReq/{eventId}")
    @ResponseBody
    public ReturnDto buttonReq(@PathVariable String eventId, @RequestBody RequestDto requestDto){
        log.info("后台收到请求/buttonReq/{}", eventId);
        log.info("请求报文内容{}",JSON.toJSONString(requestDto));
        ReturnDto returnDto = new ReturnDto();

        if(StringUtil.isBlank(eventId)){
            returnDto.setRtnCode("9997");
            returnDto.setRtnMsg("未配置该按钮请求("+eventId+")的处理方法！");
            return returnDto;
        }

        log.info("开始执行{}",eventId);
        returnDto = ((RequestFun) context.getBean(eventId)).execute(requestDto);

        log.info("返回报文内容{}",returnDto);
        return returnDto;
    }

    /**
     * 页面显示数据请求（例：点选下拉选择触发请求改变另一个下拉选择内容）
     * @param requestDto 前台传入参数
     * @return ReqJsonDto 后台返回参数
     */
    @RequestMapping(value="/webDataReq/{eventId}")
    @ResponseBody
    public ReturnDto webDataReq(@PathVariable String eventId, @RequestBody RequestDto requestDto){
        log.info("后台收到请求/webDataReq/{}", eventId);
        log.info("请求报文内容{}", JSON.toJSONString(requestDto));
        ReturnDto returnDto = new ReturnDto();

        EventInfo eventInfo = requestDto.getEventInfo();//事件信息

        log.info("开始执行{}",eventId);
        Map<String,Object> changedEleMap = ((WebDataReqFun) context.getBean(eventId)).execute(requestDto);

        returnDto.getChangedMap().put("isChanged",true);
        returnDto.getChangedMap().put("changedEleMap",changedEleMap);

        returnDto.setRtnCode("0000");
        returnDto.setRtnMsg("success");

        log.info("返回报文内容{}",returnDto);
        return returnDto;
    }
}