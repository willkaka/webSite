package com.hyw.webSite.utils;

import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.xml.sax.helpers.DefaultHandler;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileUtil {

    /**
     * 获取类型
     * @param file 文件
     * @return 文件类型
     */
    public static String getFileType(File file) {
        if (file.isDirectory()) { return "dir"; }
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());
        try (InputStream stream = new FileInputStream(file)) {
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        }catch (Exception e){
            throw new RuntimeException();
        }
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * 判断是否是图片
     * @param file 文件
     * @return true/false
     */
    public static boolean isImage(File file){
        String type = getFileType(file);
        //System.out.println(type);
        Pattern p = Pattern.compile("image/.*");
        Matcher m = p.matcher(type);
        return m.matches();
    }

    public static boolean isVideo(File file) {
        String mimeType;
        try {
            mimeType = Files.probeContentType(file.toPath());
        }catch(Exception e){
            throw new BizException("取文件类型出错！");
        }
        return mimeType.contains("video");
    }

    /**
     * 将文件转为base64码
     * @param file 文件
     * @return base64字符串
     */
    public static String getFileBase64(File file){
        String fileStr=null;
        // 通过base64来转化图片
        BASE64Encoder encoder = new BASE64Encoder();
        //File fileThumb = ImageUtil.getImgThumb(file);
        FileItem fileItem = createFileItem(file.getAbsolutePath());
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        try {
            fileStr = encoder.encode(multipartFile.getBytes());
        } catch (Exception e) {
            log.error("图片转base64出错！", e);
        }
        return fileStr;
    }

    /**
     * 创建fileItem
     * @param filePath 文件路径
     * @return FileItem
     */
    public static FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true, "MyFileName");
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
