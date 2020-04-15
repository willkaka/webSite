package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.utils.HttpUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("readHtml")
@Slf4j
public class ReadHtml implements RequestFun {

    @Override
    public ReturnDto execute(RequestDto requestDto){
        ReturnDto returnDto = new ReturnDto();
        returnDto.getOutputMap().put("showType","table");//以表格形式显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        String httpAddress = (String) inputValue.get("httpAddress");
        if(StringUtil.isBlank(httpAddress)){
            throw new BizException("http地址,不允许为空值!");
        }
        String httpAddressSuffixBeg = (String) inputValue.get("httpAddressSuffixBeg");//后缀起始
        String httpAddressSuffixEnd = (String) inputValue.get("httpAddressSuffixEnd");//后缀起始
        String webCharset = (String) inputValue.get("webCharset");//网页编码
        String regexString = (String) inputValue.get("regex");//正则表达式
        if(StringUtil.isBlank(regexString)){
            throw new BizException("请输入 正则表达式 !");
        }

        List<String> tableColList = new ArrayList<>();
        tableColList.add("data");

        log.info("执行readHtml,regex:"+regexString);
        List<Map<String,String>> resultListMap = new ArrayList<>();
        List<String> rstList = HttpUtil.search(regexString,HttpUtil.getHttpRequestData(httpAddress,webCharset));
        for(String key:rstList){
            Map<String,String> map = new HashMap<>();
            map.put("data",key.trim());
            resultListMap.add(map);
        }

        returnDto.getOutputMap().put("tableColList", tableColList);
        returnDto.getOutputMap().put("tableRecordList", resultListMap);

        return returnDto;
    }

    public static void main(String[] args){
//        Map<String,Object> rtnMap = new HashMap<>();
//        Map<String,Object> reqMap = new HashMap<>();
//
//        String httpAddress = "http://stockpage.10jqka.com.cn/600118/worth/";
//
//        reqMap.put("I001",httpAddress);
//
//        ReadHtml readHtml = new ReadHtml();
//        rtnMap = readHtml.execute(reqMap);
    }
}
