package com.hyw.webSite.funbean.RequestFunImpl;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.HttpUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("readHtml")
@Slf4j
public class ReadHtml extends RequestFunUnit<List<Map<String,FieldAttr>>, ReadHtml.QueryVariable> {

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(ReadHtml.QueryVariable variable){
        //输入检查
        if(StringUtil.isBlank(variable.getHttpAddress())){
            throw new BizException("http地址,不允许为空值!");
        }
        if(!variable.getHttpAddress().startsWith("http://") && !variable.getHttpAddress().startsWith("https://")){
            variable.setHttpAddress("http://" + variable.getHttpAddress());
        }
        // (<td( [^="]*="[^="]*")*>(.*?)</td>)  匹配所有<td xxx> xxx </td>
        if(StringUtil.isBlank(variable.getRegex())){
            throw new BizException("请输入 正则表达式 !");
        }
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<Map<String,FieldAttr>> execLogic(RequestDto requestDto, ReadHtml.QueryVariable variable){
        log.info("执行readHtml,regex:"+variable.getRegex());
        List<Map<String,String>> resultListMap = new ArrayList<>();
        resultListMap = HttpUtil.searchAllGroup(variable.getRegex(),
                HttpUtil.getHttpRequestData(variable.getHttpAddress(),variable.getWebCharset()));
        if(CollectionUtil.isEmpty(resultListMap)){
            throw new BizException("无匹配数据！");
        }

        List<Map<String, FieldAttr>> records = new ArrayList<>();
        for(Map<String,String> map:resultListMap){
            Map<String, FieldAttr> record = new LinkedHashMap<>();
            for(String fileName:map.keySet()){
                record.put(fileName,new FieldAttr().setValue(map.get(fileName)).setRemarks(fileName));
            }
            records.add(record);
        }

        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(false);//表格内容分页显示
        return records;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QueryVariable extends RequestFunUnit.Variable {
        private String httpAddress;
        private String httpAddressSuffixBeg;
        private String httpAddressSuffixEnd;
        private String webCharset;
        private String regex;
    }
}
