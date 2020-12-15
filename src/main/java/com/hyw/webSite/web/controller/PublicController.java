package com.hyw.webSite.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class PublicController {

//    @Autowired
//    private JSONServiceFacede sf;

    @PostMapping("/caes/api/{transId}")
    public void commonInterface(@PathVariable String transId, @RequestBody Map<String,Object> data) {

//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            jsonObject.put("transId", transId);
//
//            if(jsonObject.toString().length()>1024){
//                log.debug(getCurrentOSSTime() + " 处理请求开始，请求内容:"+jsonObject.toString().substring(0, 1024));
//
//            }else{
//                log.debug(getCurrentOSSTime() + " 处理请求开始，请求内容:"+jsonObject.toString());
//            }
//
//            JSONObject resultJson = sf.doService(jsonObject);
//            if(InterfacesCodeEnum.SUCCESS.getCode().equals(resultJson.get("code").toString())) {
//                return ResponseVo.success(resultJson.get("data"));
//            }else {
//                return ResponseVo.fail(resultJson.get("code").toString(), resultJson.get("data") ,resultJson.get("message").toString());
//            }
//            //return  JSONObject.parseObject(resultJson.toString(), ResponseVo.class);
//
//        } catch (Exception e) {
//
//
//            return ResponseVo.fail(InterfacesCodeEnum.ERROR_0009.getCode(),InterfacesCodeEnum.ERROR_0009.getMessage());
//        }

    }

    /**
     * 获取当前时间精确到毫秒
     *
     * @return
     */
    public  String getCurrentOSSTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return formatter.format(new Date());
    }
}
