package com.hyw.webSite.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author CFF
 * @Date:Created in 16:47 2019/1/10
 */
@RestController
public class UploadController {

    public String pictureName=null ;
    public String picturePath=null ;

    @RequestMapping(value = "/upload")
    public void uploadPicture(HttpServletRequest request) throws Exception {
        //获取文件需要上传到的路径
        picturePath = "C:\\Users\\CFF\\Desktop\\Project\\PicturesPath\\";

        // 判断存放上传文件的目录是否存在（不存在则创建）
        File dir = new File(picturePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            //获取formdata的值
            Iterator<String> iterator = req.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file=req.getFile(iterator.next());
                //获取文件后缀名
                String fileSuffixName=file.getOriginalFilename().substring(86);
                //真正写到磁盘上
                //全球唯一id
                String uuid= UUID.randomUUID().toString().replace("-","");
                pictureName=uuid+fileSuffixName;

                File file1=new File(picturePath+pictureName);
                OutputStream out=new FileOutputStream(file1);
                out.write(file.getBytes());
                out.close();
                System.out.println("图片上传成功！");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //文件下载相关代码
    @RequestMapping("/download")
    public void fileDownload( HttpServletResponse response){
        File file = new File(picturePath+pictureName);
        if (pictureName != null) {
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String dataTime=dateFormat.format(currentTime);
                //文件重新命名
                String pictureNewName = dataTime+pictureName.substring(pictureName.indexOf("."));
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + pictureNewName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println(pictureNewName+"下载成功！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(pictureNewName+"下载失败！！！"+e);
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
