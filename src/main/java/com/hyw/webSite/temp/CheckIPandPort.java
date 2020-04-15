package com.hyw.webSite.temp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckIPandPort extends Thread {
	public static void main(String[] args) {

		int threadCount = 256;  // 64-11min;32-22min;128-6min;256-3.5min
		int port = 81; // 81   3389
		int timeount = 3000;
		
		System.out.println("线程数："+threadCount+",开始时间："+LocalDateTime.now());
		List<CheckIPDto> checkIPdtos = new ArrayList<CheckIPDto>();
		add(checkIPdtos,"113.116.87.0", "113.116.87.255", port, timeount);
		add(checkIPdtos,"113.116.88.0", "113.116.88.255", port, timeount);
		add(checkIPdtos,"113.116.89.0", "113.116.89.255", port, timeount);
		add(checkIPdtos,"113.116.90.0", "113.116.90.255", port, timeount);
		add(checkIPdtos,"113.116.91.0", "113.116.91.255", port, timeount);
		add(checkIPdtos,"113.116.92.0", "113.116.92.255", port, timeount);
		add(checkIPdtos,"183.15.87.0", "183.15.87.255", port, timeount);
		add(checkIPdtos,"183.15.88.0", "183.15.88.255", port, timeount);
		add(checkIPdtos,"183.15.89.0", "183.15.89.255", port, timeount);
		add(checkIPdtos,"183.15.90.0", "183.15.90.255", port, timeount);
		add(checkIPdtos,"183.15.91.0", "183.15.91.255", port, timeount);
		add(checkIPdtos,"183.15.92.0", "183.15.92.255", port, timeount);

		for(int i=1;i<=threadCount;i++){
			CheckIPandPortThread checkIPandPortThread = new CheckIPandPortThread(checkIPdtos,threadCount,i);
			checkIPandPortThread.start();
		}
		
		while(Thread.activeCount()>2){
			//System.out.println(Thread.activeCount());
			try {
				Thread.sleep(Long.parseLong("10000"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("线程数："+threadCount+",结束时间："+LocalDateTime.now());
		
	}
	
	public static void add(List<CheckIPDto> checkIPdtos,String ipBeg, String ipEnd, int port, int timeout) {
		CheckIPDto checkIPdto = new CheckIPDto();
		checkIPdto.setBegIP(ipBeg);
		checkIPdto.setEndIP(ipEnd);
		checkIPdto.setPort(port);
		checkIPdto.setTimeout(timeout);
		checkIPdtos.add(checkIPdto);
	}
}
