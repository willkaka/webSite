package com.hyw.webSite.exception;

import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;

import java.util.List;

public class IfThrow {

    public static void trueThenThrowMsg(boolean b, String message){
        if(b) throw new BizException(message);
    }

    public static void notInThenThrowMsg(String s, List<String> list, String message){
        if(CollectionUtil.isEmpty(list)) return;
        if(!list.contains(s)) throw new BizException(message);
    }

    public static void notInThenThrowMsg(String s,String message,String... list) {
        if(null==list || list.length<=0) return;
        for(String string:list){
            if(StringUtil.isBlank(string) && StringUtil.isBlank(s)) return;
            if(StringUtil.isNotBlank(string) && string.equals(s)) return;
        }
        throw new BizException(message);
    }
}
