package com.hyw.webSite.funbean.RequestFunImpl.checkIP;

import com.hyw.webSite.utils.StringUtil;
import lombok.Synchronized;

import java.util.List;

public class CheckIPandPortThread implements Runnable {
	private Thread t;
	private int threadCnt;
	private int threadNum;
	private List<CheckIPDto> checkIPdtos;

	private static String ip;

	@Synchronized
	public static void setIp(String s){
		ip = s;
	}
	public static boolean isFound(){
		return StringUtil.isNotBlank(ip);
	}
	public static String getIp(){
		return ip;
	}

	public CheckIPandPortThread(List<CheckIPDto> checkIPdtos, int threadCount, int threadNum) {
		//CheckIPandPortThread(String name,CheckIPdto ipdto) {
		this.threadCnt = threadCount;
		this.threadNum = threadNum;
		this.checkIPdtos = checkIPdtos;
		//System.out.println("Creating " + threadName);
	}

	public void run() {
		//System.out.println("Running " + threadNum);
		
		CheckIPConnect.printIsConnectIP(checkIPdtos,threadCnt,threadNum);
		
		//System.out.println("Thread " + threadNum + " exiting.");
	}

	public void start() {
		//System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadNum+"");
			t.start();
		}
	}
}
