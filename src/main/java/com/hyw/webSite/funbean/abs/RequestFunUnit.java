package com.hyw.webSite.funbean.abs;

import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.RequestFun;
import com.hyw.webSite.model.FieldAttr;
import com.hyw.webSite.utils.ObjectUtil;
import com.hyw.webSite.web.dto.RequestDto;
import com.hyw.webSite.web.dto.ReturnDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 功能请求实现单元基类
 */
@Slf4j
public abstract class RequestFunUnit<D, V extends RequestFunUnit.Variable> implements RequestFun {

    /**
     * 执行入口
     * @param requestDto requestDto
     */
    @Override
    public ReturnDto execute(RequestDto requestDto){
        //参数赋值
        V var = getVariable(requestDto);

        checkVariable(var);

        //执行自定义逻辑
        D data = execLogic(requestDto,var);

        //返回数据处理
        return returnData(data,var);
    }

    /**
     * 自动取定义的参数值
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
            if(inputValue.containsKey(fieldName)){
                String value = inputValue.get(fieldName);
                try {
                    if (!field.isAccessible()) { field.setAccessible(true); }
                    field.set(var,value);
                }catch (Exception e) {
                    throw new BizException("给对象("+var.getClass().getName()+")属性("+fieldName+")赋值("+value+")失败!");
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

        returnDto.getOutputMap().put("showType", variable.getOutputShowType());//以表格形式显示
        returnDto.getOutputMap().put("withPage",variable.isWithPage());//表格内容分页显示
        returnDto.getOutputMap().put("isChanged",true); //标识输出区域已改变需要刷新
        returnDto.getOutputMap().put("isClear",true);//清除原有输出内容

        boolean isListMapFieldAttr = false;
        if(data instanceof List){
            for(Object object:(List) data){
                if(object instanceof Map){
                    for(Object key:((Map) object).keySet()){
                        if(key instanceof String && ((Map) object).get(key) instanceof FieldAttr){
                            isListMapFieldAttr = true;
                            break;
                        }
                    }
                }else{

                }
                if(isListMapFieldAttr) break;
            }
        }else if(data instanceof String){
            returnDto.getOutputMap().put("textAreaValue", data);
        }

        if(isListMapFieldAttr){
            returnDto.getOutputMap().put("tableRecordList", data);
            returnDto.getOutputMap().put("totalCount", variable.getTotalCount());
            returnDto.getOutputMap().put("pageNow", variable.getPageNow());
            returnDto.getOutputMap().put("pageSize", variable.getPageSize());
        }

        return returnDto;
    }


    /**
     * 实例化变量
     *
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

    /**
     * 自定义变量
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Variable {
        private String outputShowType; //输出显示格式类型
        private boolean isWithPage; //是否分页
        private int totalCount;
        private int pageNow;
        private int pageSize;
    }
}
