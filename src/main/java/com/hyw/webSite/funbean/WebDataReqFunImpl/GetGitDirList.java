package com.hyw.webSite.funbean.WebDataReqFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.WebDataReqFun;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service("getGitDirList")
@Slf4j
public class GetGitDirList implements WebDataReqFun {
    @Override
    public Map<String,Object> execute(RequestDto requestDto){
        String dirPath = "D:\\Java\\DaShuSource";
        File dir = new File(dirPath);
        File [] files = dir.listFiles();
        if(null == files){
            throw new BizException("指定文件路径下无文件！");
        }

        Map<String,Object> rtnMap = new HashMap<>();
        for(File file:files){
            if(file.isDirectory()){
                rtnMap.put(file.getPath(),file.getPath());
            }
        }
        return rtnMap;
    }
}
