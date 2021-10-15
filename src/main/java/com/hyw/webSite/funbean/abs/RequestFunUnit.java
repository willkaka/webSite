package com.hyw.webSite.funbean.abs;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.ObjectUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import com.hyw.webSite.web.model.WebDivDto;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 功能请求实现单元基类
 */
@Slf4j
public abstract class RequestFunUnit<D, V extends RequestPubDto> implements RequestFun {

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
        return returnData(data,var);
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
                    field.set(var, value);
                } catch (Exception e) {
                    throw new BizException("给对象(" + var.getClass().getName() + ")属性(" + fieldName + ")赋值(" + value + ")失败!");
                }
            }
        }
        return var;
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
    public ReturnDto returnData(D data, V variable){
        ReturnDto returnDto = new ReturnDto();

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
            returnDto.getOutputMap().put("tableRecordList", data);
            returnDto.getOutputMap().put("totalCount", variable.getTotalCount());
            returnDto.getOutputMap().put("pageNow", variable.getPageNow());
            returnDto.getOutputMap().put("pageSize", variable.getPageSize());
        }
        if(isDivMap){
            returnDto.getOutputMap().put("mapData", data);
        }

        return returnDto;
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
