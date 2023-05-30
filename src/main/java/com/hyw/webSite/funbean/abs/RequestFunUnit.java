package com.hyw.webSite.funbean.abs;

import com.hyw.webSite.constant.WebConstant;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.ObjectUtil;
import com.hyw.webSite.web.dto.*;
import com.hyw.webSite.web.service.WebElementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 功能请求实现单元基类
 */
@Slf4j
public abstract class RequestFunUnit<D, V extends RequestPubDto> implements RequestFun {

    @Autowired
    private WebElementService webElementService;

    /**
     * 执行入口
     * @param requestDto requestDto
     */
    @Override
    public ReturnDto execute(RequestDto requestDto){
        // 参数赋值，将请求的参数 RequestDto，
        // 转为V（父类为RequestPubDto）并将requestDto.getReqParm().get("inputValue")中的值赋到对应的字段
        V var = getVariable(requestDto);

        // 必要的输入检查
        checkVariable(var);

        //执行自定义逻辑
        D data = execLogic(requestDto,var);

        //返回数据处理
        return returnData(requestDto,data,var);
    }

    /**
     * 自动取定义的参数值
     *   参数赋值，将请求的参数 RequestDto，
     * 转为V（父类为RequestPubDto）并将requestDto.getReqParm().get("inputValue")中的值赋到对应的字段
     * @param requestDto 请求dto
     * @return variable
     */
    private V getVariable(RequestDto requestDto){
        V var = newInstanceVariable();
        //处理参数
        Map<String,String> inputValue = (Map<String,String>) requestDto.getReqParm().get("inputValue");
        List<Field> fields = ObjectUtil.getAllFieldList(var.getClass());
        for(Field field:fields){
            String fieldName = field.getName();
            Object value = inputValue.get(fieldName);
            if(inputValue.containsKey(fieldName)){
                try {
                    if (!field.isAccessible()) { field.setAccessible(true); }
                    field.set(var, valueConvert(field,value));
                } catch (Exception e) {
                    throw new BizException("给对象(" + var.getClass().getName() + ")属性(" + fieldName + ")赋值(" + value + ")失败!");
                }
            }
        }
        return var;
    }

    private Object valueConvert(Field field,Object value){
        if(field.getType() == Integer.class) {
            return Integer.parseInt(value.toString());
        }else if(field.getType() == Double.class) {
            return Double.parseDouble(value.toString());
        }else if(field.getType() == Long.class) {
            return Long.parseLong(value.toString());
        }else if(field.getType() == BigDecimal.class) {
            return new BigDecimal(value.toString());
        }else if(field.getType() == LocalDate.class) {
            return LocalDate.parse(value.toString());
        }else if(field.getType() == LocalTime.class) {
            return LocalTime.parse(value.toString());
        }else if(field.getType() == LocalDateTime.class) {
            return LocalDateTime.parse(value.toString());
        }else{
            return value;
        }
    }

    /**
     * 输入参数检查
     * @param var 参数
     */
    public void checkVariable(V var){ }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param var 参数
     * @return D
     */
    public D execLogic(RequestDto requestDto, V var){
        return null;
    }

    /**
     * 返回数据处理
     * @param data 数据
     * @param variable 参数
     * @return ReturnDto
     */
    public ReturnDto returnData(RequestDto requestDto, D data, V variable){
        ReturnDto returnDto = new ReturnDto();
        EventInfo eventInfo = requestDto.getEventInfo();
        returnDto.getOutputMap().put("showType", variable.getOutputShowType()); //以表格形式显示
        returnDto.getOutputMap().put("withPage",variable.isWithPage()); //表格内容分页显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true); //清除原有输出内容

        //判断目前返回的数据类型
        boolean isListMapFieldAttr = false;
        boolean isDivMap = false;
        if(data instanceof List) {
            for (Object object : (List) data) {
                if (object instanceof Map) {  //List<Map<>> 则以表格的数据形式返回
                    for (Object key : ((Map) object).keySet()) {
                        if (key instanceof String && ((Map) object).get(key) instanceof FieldAttr) {
                            isListMapFieldAttr = true;
                            break;
                        }
                    }
                }
                if (isListMapFieldAttr) break;
                if( object instanceof WebDivDto){
                    isDivMap = true;
                    break;
                }
                if (isDivMap) break;
            }
        }else if(data instanceof String){ // String 则以多行的文本输入形式返回
            returnDto.getOutputMap().put("textAreaValue", data);
        }

        if(isListMapFieldAttr){
            WebTableInfo webTableInfo = new WebTableInfo();
            webTableInfo.setRecordList((List<Map<String, FieldAttr>>) data);
            webTableInfo.setTotalCount(variable.getTotalCount());
            webTableInfo.setPageNow(variable.getPageNow());
            webTableInfo.setPageSize(variable.getPageSize());
            webTableInfo.setWithPage(variable.isWithPage());
            webTableInfo.setFormatInfoList(webElementService.getMenuElements(eventInfo.getMenu(), "outputArea"));

            returnDto.getOutputMap().put("webTableInfo",webTableInfo);
        }
        if(isDivMap){
            returnDto.getOutputMap().put("mapData", data);
        }
        if(WebConstant.OUTPUT_DOWNLOAD_FILE.equals(variable.getOutputShowType())){
            returnDto.getOutputMap().put("uriList",data);
//            Map<String,byte[]> fileStreamMap = new HashMap<>();
            Map<String,char[]> fileStreamMap = new HashMap<>();
//            Map<String,File> fileStreamMap = new HashMap<>();
            List<String> filePathList = (List<String>) data;
            for(String filePath:filePathList) {
                File file = new File(filePath);
                byte[] fileByte;
                try {
                    fileByte = FileUtils.readFileToByteArray(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                fileStreamMap.put(file.getName(),fileByte);
                fileStreamMap.put(file.getName(),getChars(fileByte));
//                fileStreamMap.put(file.getName(),file);
            }
            returnDto.getOutputMap().put("fileStreamMap",fileStreamMap);
            returnDto.getOutputMap().put("isChanged",false); //标识输出区域已改变需要刷新
        }

        return returnDto;
    }

    /**
     * 字节流转字符流
     * @param bytes
     * @return
     */
    private char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes).flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    /**
     * 实例化变量
     * @return 返回实例化的变量信息
     */
    public V newInstanceVariable() {
        Class<V> vClass = null;
        try {
            Type genericSuperclass = null;
            for (Class clazz = getClass();
                 clazz != null && !((genericSuperclass = clazz.getGenericSuperclass()) instanceof ParameterizedType);
                 clazz = getClass().getSuperclass()) {}

            ParameterizedType type = (ParameterizedType) genericSuperclass;
            Type actualTypeArgument = type.getActualTypeArguments()[1];
            //noinspection unchecked
            vClass = (Class<V>) (actualTypeArgument instanceof Class ? actualTypeArgument : ((ParameterizedType) actualTypeArgument).getRawType());
        } catch (Exception e) {
            throw new RuntimeException(getClass() + " 缺少泛型", e);
        }
        try {
            return vClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(vClass + " 实例化失败", e);
        }
    }
}
