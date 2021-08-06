package com.hyw.webSite.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.dao.WebConfigReq;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author CFF
 * @Date:Created in 16:47 2019/1/10
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    private ExcelTemplateUtil excelTemplateUtil;

    private String LOCAL_PATH = "D:/tmp/";

    /**
     * 上传文件
     * @param request
     * @param response
     */
    @RequestMapping(value = "/fileReq/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response){
        log.info("uploadFile beg...");
        File file = FileUtil.getFileFromRequest(request,LOCAL_PATH);

        String fileName = file.getName().substring(0,file.getName().indexOf('.'));

        if("web_config_req".equalsIgnoreCase(fileName)) {
            List<WebConfigReq> webConfigReqList =
                    excelTemplateUtil.getObjectList(fileName, file, WebConfigReq.class);
        }else if("mgt_pay_plan".equalsIgnoreCase(fileName)){
            List<JSONObject> jsonList = excelTemplateUtil.getExcelRecords(fileName,file);
        }
        System.out.println(fileName);


    }
}
