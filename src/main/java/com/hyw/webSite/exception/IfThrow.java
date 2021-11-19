package com.hyw.webSite.exception;

import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;

import java.util.List;

public class IfThrow {

    public static void trueThrowMsg(boolean b, String message, Object... params){
        if(!b) return;
        String msg = message;
        for(Object param:params){
            msg = msg.replaceFirst("\\{}",param.toString());
        }
        throw new BizException(msg);
    }

}
