package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("searchFromTextFile")
public class SearchFromTextFile extends RequestFunUnit<List<Map<String, FieldAttr>>, SearchFromTextFile.QueryVariable> {

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QueryVariable variable){
        //输入检查
        BizException.trueThrow(StringUtil.isBlank(variable.getDirPath()),"查找的目录地址，不允许为空值!");

        BizException.trueThrow(StringUtil.isBlank(variable.getSearchText()),"要查找的字符串,不允许为空值!");

    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<Map<String,FieldAttr>> execLogic(RequestDto requestDto, QueryVariable variable){

        File dirFile = new File(variable.getDirPath());
        List<String> pathList = readDirFile(dirFile,variable.getSearchText(),variable.getSearchFileTypeList());

        List<Map<String,FieldAttr>> records = new ArrayList<>();
        for(String pathString:pathList){
            Map<String,FieldAttr> map = new HashMap<>();
            FieldAttr fieldAttr = new FieldAttr();
            fieldAttr.setColumnName("文件路径");
            fieldAttr.setCurValue(pathString);
            map.put("pathString",fieldAttr);
            records.add(map);
        }

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(true);//表格内容分页显示
        variable.setTotalCount(pathList.size());
        variable.setPageNow(1);
        variable.setPageSize(pathList.size()); //每页中显示多少条记录

        return records;
    }

    private List<String> readDirFile(File dir,String searchText,List<String> searchFileTypeList){
        if(!dir.isDirectory()) return new ArrayList<>();

        File [] files = dir.listFiles();
        if(files == null || files.length == 0) return new ArrayList<>();

        List<String> subFilePathList = new ArrayList<>();
        for(File file:files){
            int lastPointPos = file.getName().lastIndexOf(".");
            if(lastPointPos <= 0) return new ArrayList<>();
            String fileType = file.getName().toUpperCase().substring(lastPointPos);
            if(!searchFileTypeList.contains(fileType)){
                return new ArrayList<>();
            }

            if(file.isDirectory()){
                subFilePathList.addAll( readDirFile(file,searchText,searchFileTypeList) );
            }else{
                String filePath = readFileContent(file,searchText);
                if(StringUtils.isNotBlank(filePath)) {
                    subFilePathList.add(filePath);
                }
            }
        }
        return subFilePathList;
    }

    private String readFileContent(File file,String searchText){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(searchText)){
                    return file.getPath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestPubDto {
        private String dirPath;
        private List<String> searchFileTypeList;
        private String searchText;
    }
}
