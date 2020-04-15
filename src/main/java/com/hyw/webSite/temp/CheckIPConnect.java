package com.hyw.webSite.temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class CheckIPConnect {
	
	/**
	 * 检查IP+PORT是否可连接
	 * @param urlString
	 * @return
	 */
	public static boolean isHttpConnectable(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = (URLConnection)url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line = reader.readLine();
            if("hyw".equals(line)){
            	reader.close();
               	return true;
            }else{
            	reader.close();
            	return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
	}
	
	/**
	 * 检查IP+PORT是否可连接
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean isHostConnectable(String host, int port) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port));
		} catch (ConnectException e) {
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 判断ip是否可以连接 timeOut是超时时间
	 * 
	 * @param host
	 * @param timeOut
	 * @return
	 */
	public static boolean isHostReachable(String host, Integer timeOut) {
		try {
			return InetAddress.getByName(host).isReachable(timeOut);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 检查输入IP段中可连接的IP
	 * @param checkIPdtos
	 * @param threadCnt
	 * @param threadNum
	 */
	public static void printIsConnectIP(List<CheckIPDto> checkIPdtos, int threadCnt, int threadNum) {

		int ipTotalCnt = getTotalIpCount(checkIPdtos);
		int eachThreadPrcCnt = ipTotalCnt%threadCnt>0?ipTotalCnt/threadCnt + 1:ipTotalCnt/threadCnt;
		
		int begNum = eachThreadPrcCnt * (threadNum - 1);
		int endNum = begNum + eachThreadPrcCnt;
		
		int ipCount = 0;
		int preIpCount = 0;
		for(CheckIPDto checkIPDto:checkIPdtos){
			ipCount = ipCount + getIPCountWithDto(checkIPDto);
			
			if(preIpCount <= begNum && begNum <= ipCount ||
			   preIpCount <= endNum && endNum <= ipCount ){
				
				prcIp(checkIPDto.getBegIP(), checkIPDto.getEndIP(), checkIPDto.getPort(), checkIPDto.getTimeout(),begNum,endNum,preIpCount,threadNum);
				
			}
			preIpCount = ipCount;
		}
		
		
	}
	
	public static void prcIp(String ipBeg,String ipEnd,int port,int timeout,int begNum, int endNum,int ipCount,int threadNum){
		int ipNum1Min = 0, ipNum2Min = 0, ipNum3Min = 0, ipNum4Min = 0;
		try {
			int ipDotPost1 = ipBeg.indexOf('.');
			int ipDotPost2 = ipBeg.indexOf('.', ipDotPost1 + 1);
			int ipDotPost3 = ipBeg.indexOf('.', ipDotPost2 + 1);
			ipNum1Min = Integer.parseInt(ipBeg.substring(0, ipDotPost1));
			ipNum2Min = Integer.parseInt(ipBeg.substring(ipDotPost1 + 1, ipDotPost2));
			ipNum3Min = Integer.parseInt(ipBeg.substring(ipDotPost2 + 1, ipDotPost3));
			ipNum4Min = Integer.parseInt(ipBeg.substring(ipDotPost3 + 1, ipBeg.length()));
		} catch (Exception e) {
			System.out.println("开始IP解释出错！");
			return;
		}

		int ipNum1Max = 0, ipNum2Max = 0, ipNum3Max = 0, ipNum4Max = 0;
		try {
			int ipDotPost1 = ipEnd.indexOf('.');
			int ipDotPost2 = ipEnd.indexOf('.', ipDotPost1 + 1);
			int ipDotPost3 = ipEnd.indexOf('.', ipDotPost2 + 1);
			ipNum1Max = Integer.parseInt(ipEnd.substring(0, ipDotPost1));
			ipNum2Max = Integer.parseInt(ipEnd.substring(ipDotPost1 + 1, ipDotPost2));
			ipNum3Max = Integer.parseInt(ipEnd.substring(ipDotPost2 + 1, ipDotPost3));
			ipNum4Max = Integer.parseInt(ipEnd.substring(ipDotPost3 + 1, ipEnd.length()));
		} catch (Exception e) {
			System.out.println("结束IP解释出错！");
			return;
		}

		for (int ipNum1 = ipNum1Min; ipNum1 <= ipNum1Max; ipNum1++) {
			for (int ipNum2 = ipNum2Min; ipNum2 <= ipNum2Max; ipNum2++) {
				for (int ipNum3 = ipNum3Min; ipNum3 <= ipNum3Max; ipNum3++) {
					for (int ipNum4 = ipNum4Min; ipNum4 <= ipNum4Max; ipNum4++) {
						String ip = ipNum1 + "." + ipNum2 + "." + ipNum3 + "." + ipNum4;
						if(begNum <= ipCount && ipCount <= endNum){
							if(port == 3389){
								if (isHostReachable(ip, timeout)) {
									if (isHostConnectable(ip, port)) {
										System.out.println(ip);
										//System.out.println("------------"+ip + ":" + port + ",true.");
									}
								}
							}else{								
								if(isHttpConnectable("http://" + ip+":"+port+"/hyw.html")){
									System.out.println(ip+"\t********TRUE********");
								}else{
									//System.out.println(ip+"\t FALSE");
								}
							}
						}else{
							//System.out.println("threadNum:"+threadNum+",beg:"+begNum+",end:"+endNum+",skip:"+ip);
						}
						ipCount++;
					}
				}
			}
		}
	}
	
	
	public static int getTotalIpCount(List<CheckIPDto> checkIPdtos){
		int ipTotalCount=0;
		for(CheckIPDto checkIPdto:checkIPdtos){
			ipTotalCount = ipTotalCount + getIPCount(checkIPdto.getBegIP(), checkIPdto.getEndIP());
		}
		return ipTotalCount;
	}
	
	public static int getIPCountWithDto(CheckIPDto checkIPDto){
		return getIPCount(checkIPDto.getBegIP(), checkIPDto.getEndIP());
	}
	
	public static int getIPCount(String ipBeg, String ipEnd){
		int ipCount = 0,ipNum1Min = 0, ipNum2Min = 0, ipNum3Min = 0, ipNum4Min = 0;
		try {
			int ipDotPost1 = ipBeg.indexOf('.');
			int ipDotPost2 = ipBeg.indexOf('.', ipDotPost1 + 1);
			int ipDotPost3 = ipBeg.indexOf('.', ipDotPost2 + 1);
			ipNum1Min = Integer.parseInt(ipBeg.substring(0, ipDotPost1));
			ipNum2Min = Integer.parseInt(ipBeg.substring(ipDotPost1 + 1, ipDotPost2));
			ipNum3Min = Integer.parseInt(ipBeg.substring(ipDotPost2 + 1, ipDotPost3));
			ipNum4Min = Integer.parseInt(ipBeg.substring(ipDotPost3 + 1, ipBeg.length()));
		} catch (Exception e) {
			System.out.println("开始IP解释出错！");
			return 0;
		}

		int ipNum1Max = 0, ipNum2Max = 0, ipNum3Max = 0, ipNum4Max = 0;
		try {
			int ipDotPost1 = ipEnd.indexOf('.');
			int ipDotPost2 = ipEnd.indexOf('.', ipDotPost1 + 1);
			int ipDotPost3 = ipEnd.indexOf('.', ipDotPost2 + 1);
			ipNum1Max = Integer.parseInt(ipEnd.substring(0, ipDotPost1));
			ipNum2Max = Integer.parseInt(ipEnd.substring(ipDotPost1 + 1, ipDotPost2));
			ipNum3Max = Integer.parseInt(ipEnd.substring(ipDotPost2 + 1, ipDotPost3));
			ipNum4Max = Integer.parseInt(ipEnd.substring(ipDotPost3 + 1, ipEnd.length()));
		} catch (Exception e) {
			System.out.println("结束IP解释出错！");
			return 0;
		}

		for (int ipNum1 = ipNum1Min; ipNum1 <= ipNum1Max; ipNum1++) {
			for (int ipNum2 = ipNum2Min; ipNum2 <= ipNum2Max; ipNum2++) {
				for (int ipNum3 = ipNum3Min; ipNum3 <= ipNum3Max; ipNum3++) {
					for (int ipNum4 = ipNum4Min; ipNum4 <= ipNum4Max; ipNum4++) {
						//String ip = ipNum1 + "." + ipNum2 + "." + ipNum3 + "." + ipNum4;
						ipCount ++;
					}
				}
			}
		}
		return ipCount;
	}
	
	public static void main(String[] args){
		if(isHttpConnectable("http://183.15.89.206"+":"+"81"+"/hyw.html")){
			//System.out.println(ip);
		}
	}
}
