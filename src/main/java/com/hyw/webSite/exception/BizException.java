package com.hyw.webSite.exception;

import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;

import java.util.List;

/**
 * 统一的业务异常处理基类
 * 框架已经做了统一异常处理
 * 所有的业务异常可继承此类通过抛出异常的方式来做业务校验等操作
 */
public class BizException extends FastRuntimeException {

    public BizException(String message) { super("9998", message); }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String message, Exception e) {
        super(message, e);
    }

    public BizException(String code, String message, Exception e) {
        super(code, message, e);
    }

    public static void trueThrow(boolean b, String message, Object... params){
        if(!b) return;
        String msg = message;
        for(Object param:params){
            msg = msg.replaceFirst("\\{}",param.toString());
        }
        throw new BizException(msg);
    }

    public static void notInThrowMsg(String s, List<String> list, String message){
        if(CollectionUtil.isEmpty(list)) return;
        if(!list.contains(s)) throw new BizException(message);
    }

    public static void notInThrowMsg(String s, String message, String... list) {
        if(null==list || list.length<=0) return;
        for(String string:list){
            if(StringUtil.isBlank(string) && StringUtil.isBlank(s)) return;
            if(StringUtil.isNotBlank(string) && string.equals(s)) return;
        }
        throw new BizException(message);
    }
}
