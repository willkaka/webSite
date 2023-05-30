package com.hyw.webSite.funbean.RequestFunImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("jsonConvertToTable")
@Slf4j
public class JsonConvertToTable extends RequestFunUnit<List<Map<String, FieldAttr>>, JsonConvertToTable.QryVariable> {

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(JsonConvertToTable.QryVariable variable){
        //输入检查
        BizException.trueThrow(StringUtil.isBlank(variable.getJsonString()),"输入JSON字符串不允许为空值!");
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public List<Map<String, FieldAttr>> execLogic(RequestDto requestDto, JsonConvertToTable.QryVariable variable) {
        List<Map<String, FieldAttr>> records = new ArrayList<>();
        //参数配置
        variable.setOutputShowType(WebConstant.OUTPUT_SHOW_TYPE_TABLE); //以表格形式显示
        variable.setWithPage(false);//表格内容分页显示

        String inputString = variable.getJsonString();
        JSONArray jsonArray = null;
        if(inputString.startsWith("[")) {
            jsonArray = JSONObject.parseArray(inputString);
        }else {
            JSONObject jsonObject = JSONObject.parseObject(inputString);
            jsonArray = jsonObject.getJSONArray(variable.getKey());
        }
        for(int i=0;i<jsonArray.size();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Map<String,FieldAttr> record = new LinkedHashMap<>();
            for (String field : object.keySet()){
                if(CollectionUtil.isNotEmpty(variable.getShowFields()) && !variable.getShowFields().contains(field)){
                    continue;
                }
                record.put(field,new FieldAttr().setValue(object.get(field).toString()).setRemarks(field));
            }
            records.add(record);
        }
        return records;
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
        private String jsonString;  //输入字符串
        private String key; //单双引号
        private List<String> showFields;
    }
}
