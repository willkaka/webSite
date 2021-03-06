package com.hyw.webSite.web.controller;

import com.hyw.webSite.service.NonStaticResourceHttpRequestHandler;
import com.hyw.webSite.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@AllArgsConstructor
public class VideoController {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/video")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("后台收到请求video");
        String fileName = request.getParameter("path");
        log.info("fileName:"+fileName);
        String path = "E:\\t\\"+fileName;
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            path = "F:\\娱乐\\movies\\"+fileName;
            filePath = Paths.get(path);
        }
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtil.isBlank(mimeType)) {
                log.info("ContentType:"+mimeType);
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }
}