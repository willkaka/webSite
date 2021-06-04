package com.hyw.webSite.annotationInterceptor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Socket 服务器
 *
 * @author tangjialin on 2020-01-17.
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class BackServer implements InitializingBean, DisposableBean, ApplicationContextAware {
    /** Socket 服务器实例 */
    private ServerSocket serverSocket;
    /** 服务运行标记 */
    private volatile boolean canRun = true;
    /** Spring 上下文环境 */
    private ApplicationContext applicationContext;

    /** 线程执行器 */
    private Executor executor;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet");
        Assert.notNull(executor, "线程执行器不能为空");
        addMonitorMethod();
        start();
    }

    /**
     * 加载监听方法
     */
    protected void addMonitorMethod() {
        Map<String, Object> beansWithAnnotationMap = this.applicationContext
                .getBeansWithAnnotation(com.hyw.webSite.annotation.BackServer.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            for (Method method : clazz.getDeclaredMethods()) {
            }
        }
    }

    /**
     * 启动Socket服务器
     *
     * @throws Exception 启动失败会抛出异常
     */
    @SuppressWarnings("unchecked")
    protected void start() throws Exception {
        executor.execute(() -> {
            //log.info("平安银企直连Socket监听服务启动完毕,监听端口:{}", socketProperties.getPort());
            while (canRun) {
                try {
                    // 监听 Socket 客户端连接，accept 是阻塞函数，有客户端连上来才会往下执行
                    Socket socket = serverSocket.accept();
                    // 在线程池中处理客户端请求
                    executor.execute(new SocketServerProcessor());
                } catch (IOException e) {
                    // canRun == true  表示服务器需要正常工作
                    // canRun == false 表示服务器正在主动停止
                    if (canRun) {
                        log.warn("Socket服务器接收信息监听过程发生了错误", e);
                    }
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        canRun = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
        log.warn("Socket服务器已关闭");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("setApplicationContext");
        this.applicationContext = applicationContext;
    }

    /**
     * Socket服务处理器
     */
    private class SocketServerProcessor implements Runnable {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void run() {
            try {
                log.info("111111111111");
                Thread.sleep(1000);
                // 执行内容
            } catch (Throwable e) {
                logger.error("SocketServer处理消息发生异常", e);
            }
        }
    }
}