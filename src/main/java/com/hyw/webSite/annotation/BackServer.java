package com.hyw.webSite.annotation;

import java.lang.annotation.*;

/**
 * 后台服务
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackServer {
    /** 用于链接的 BackServer 在 Spring 容器中的名称 */
    String server();
}