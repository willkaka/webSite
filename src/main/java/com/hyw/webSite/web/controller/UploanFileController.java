package com.hyw.webSite.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UploanFileController {

    @Autowired
    ApplicationContext context;

    /**
     * 上传文件
     * @param multipartFiles 上传的文件
     */
    @RequestMapping(value = "/fileReq/uploadFile")
    public ReturnDto uploadFile(@RequestParam("fileList") List<MultipartFile> multipartFiles, HttpServletRequest request){
        ReturnDto returnDto = new ReturnDto();

        String dto = request.getParameter("requestDto");
        RequestDto requestDto = JSONObject.parseObject(dto,RequestDto.class);
        log.info("后台收到请求/fileReq/uploadFile");
        log.info("请求报文内容{}",JSON.toJSONString(requestDto));

        //文件无法与参数一起传输
        Map<String,Object> map = (Map<String,Object>) requestDto.getReqParm().get("inputValue");
        map.put("fileList",multipartFiles);

        String eventId = requestDto.getEventInfo().getId();
        if(StringUtil.isBlank(eventId)){
            returnDto.setRtnCode("9997");
            returnDto.setRtnMsg("未配置该按钮请求("+eventId+")的处理方法！");
            return returnDto;
        }

        log.info("开始执行{}",requestDto.getEventInfo().getId());
        returnDto = ((RequestFun) context.getBean(eventId)).execute(requestDto);

        log.info("返回报文内容{}",returnDto);
        return returnDto;
    }
}