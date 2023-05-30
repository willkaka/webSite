package com.hyw.webSite.exception;

public class IfThrow {

    public static void isTrue(boolean b, String message, Object... params){
        if(!b) return;
        String msg = message;
        for(Object param:params){
            msg = msg.replaceFirst("\\{}",param.toString());
        }
        throw new BizException(msg);
    }

}
