package com.hyw.webSite;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest001{

        public static void main(String[] args) throws InterruptedException, ExecutionException {
            long start = System.currentTimeMillis();

            // 等凉菜
            Callable ca1 = new Callable() {

                @Override
                public String call() throws Exception {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "凉菜准备完毕";
                }
            };
            FutureTask<String> ft1 = new FutureTask<String>(ca1);
            new Thread(ft1).start();

            // 等包子 -- 必须要等待返回的结果，所以要调用join方法
            Callable ca2 = new Callable() {

                @Override
                public Object call() throws Exception {
                    try {
                        Thread.sleep(1000 * 3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "包子准备完毕";
                }
            };
            FutureTask<String> ft2 = new FutureTask<String>(ca2);
            new Thread(ft2).start();

            //get()方法可以当任务结束后返回一个结果，如果调用时，工作还没有结束，则会阻塞线程，直到任务执行完毕
            System.out.println(ft1.get());
            System.out.println(ft2.get());

            long end = System.currentTimeMillis();
            System.out.println("准备完毕时间：" + (end - start));
        }
}
