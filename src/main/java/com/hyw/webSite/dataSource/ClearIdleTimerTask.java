package com.hyw.webSite.dataSource;

import java.util.TimerTask;

/**
 * 清除空闲连接任务。
 *
 * @author elon
 * @version 2018年2月26日
 */
public class ClearIdleTimerTask extends TimerTask {
    @Override
    public void run() {
        DDSHolder.instance().clearIdleDDS();
    }
}
