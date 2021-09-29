package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.exception.IfThrow;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.WebElementDto;
import com.hyw.webSite.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("displayDir")
@Slf4j
public class DisplayDir implements RequestFun {

    private final String THUMB_IMAGE_PRE = "THUMB_";
    private final String THUMB_IMAGE_DIR_PATH = "src/main/resources/static/image/dir_image.jpg";
    private final String THUMB_IMAGE_UNKONW_PATH = "src/main/resources/static/image/unkonwFile_image.jpg";
    private final int THUMB_IMAGE_WIDTH = 64;
    private final int THUMB_IMAGE_HEIGHT = 64;

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","div_image");//以div形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String dirPath = (String) inputValue.get("localAddress");
        IfThrow.trueThenThrowMsg(StringUtil.isBlank(dirPath),"文件夹位置,不允许为空值!");
        String selectFileType = (String) inputValue.get("selectFileType");
        IfThrow.trueThenThrowMsg(StringUtil.isBlank(dirPath),"筛选类型,不允许为空值!");

        File dir = new File(dirPath);
        File [] files = dir.listFiles();
        IfThrow.trueThenThrowMsg(null == files,"指定文件路径下无文件！");

        List<WebElementDto> elementList = new ArrayList<>();
        for(File file:files){
            if(isMatchCondition(file,selectFileType)) {
                WebElementDto element = new WebElementDto();
                element.setId(file.getName());
                element.setType(FileUtil.getFileType(file));

                File thumbFile = null;
                if(FileUtil.isImage(file)) {
                    //如果文件前缀为THUMB_，则视为已生成的缩略图，跳过不显示。
                    if(file.getName().length()> THUMB_IMAGE_PRE.length() &&
                            file.getName().substring(0, THUMB_IMAGE_PRE.length()).equals(THUMB_IMAGE_PRE)) continue;

                    //thumbFile = getThumbFile(file);
                    thumbFile = file;
                }else if(file.isDirectory()){
                    thumbFile = getThumbFile(new File(THUMB_IMAGE_DIR_PATH));
                }else{
                    thumbFile = getThumbFile(new File(THUMB_IMAGE_UNKONW_PATH));
                }
                // 通过base64来转化图片
                element.setPrompt(FileUtil.getFileBase64(thumbFile));

                elementList.add(element);
            }
        }
        returnDto.getOutputMap().put("elementList", elementList);

        return returnDto;
    }


    /**
     * 判断文件是否满足筛选条件
     * @param file 文件
     * @param selectType 筛选文件类型
     * @return 匹配条件为true
     */
    private boolean isMatchCondition(File file,String selectType){
        if(StringUtil.isBlank(selectType)) return true;
        if("all".equals(selectType)) return true;
        if("file".equals(selectType) && file.isFile()) return true;
        if("video".equals(selectType) && file.isFile()) return true; //todo
        if("photo".equals(selectType) && FileUtil.isImage(file)) return true;
        return "dir".equals(selectType) && file.isDirectory();
    }

    private File getThumbFile(File imageFile){
        if(!imageFile.isFile()) return null;
        String ouputFilePath = imageFile.getParent()+"\\"+ THUMB_IMAGE_PRE +imageFile.getName()+".jpeg";
        return ImageUtil.getImgThumb(imageFile, ouputFilePath, THUMB_IMAGE_WIDTH, THUMB_IMAGE_HEIGHT);
    }
}
