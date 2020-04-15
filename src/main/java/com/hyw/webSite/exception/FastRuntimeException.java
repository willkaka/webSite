package com.hyw.webSite.exception;

/**
 * 一般用于业务异常集成，统一异常处理拦截此异常。
 * @author chengjie
 */
public class FastRuntimeException extends BaseRuntimeException {
    private static final long serialVersionUID = -4954118251735823026L;

    public FastRuntimeException(String msg) {
        super(msg);
    }

    public FastRuntimeException(String code , Object[] args){
        super(code, "",args);
    }

    public FastRuntimeException(String code , String defaultMsg , Object[] args){
        super(code,defaultMsg,args);
    }

    public FastRuntimeException(String code, String msg) {
        super(code, msg, new Object[0]);
    }

    public FastRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FastRuntimeException(String code, String msg, Throwable cause) {
        super(code, msg, cause, new Object[0]);
    }
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
