package com.hyw.webSite.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FileController {

    @Autowired
    ApplicationContext context;

    /**
     * 上传文件
     * @param multipartFiles 上传的文件
     */
    @RequestMapping(value = "/fileReq/{fileUploadEventId}")
    public ReturnDto uploadFile(@PathVariable String fileUploadEventId,
                                @RequestParam("fileList") List<MultipartFile> multipartFiles, HttpServletRequest request){
        ReturnDto returnDto = new ReturnDto();

        String dto = request.getParameter("requestDto");
        RequestDto requestDto = JSONObject.parseObject(dto,RequestDto.class);
        log.info("后台收到请求/fileReq/"+ fileUploadEventId);
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

    @RequestMapping(value = "/downLoadFileReq/{eventId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String eventId, @RequestBody RequestDto requestDto) throws IOException {
        log.info("后台收到请求/downLoadFileReq/{}", eventId);
        log.info("请求报文内容{}", JSON.toJSONString(requestDto));

        log.info("开始执行{}", eventId);
        ReturnDto returnDto = ((RequestFun) context.getBean(eventId)).execute(requestDto);
        List<String> filePathList = (List<String>)returnDto.getOutputMap().get("uriList");

        File file = new File(filePathList.get(0));
        if(file.exists()){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
        }else{
            System.out.println("文件不存在...");
            return null;
        }
    }
}