package com.hyw.webSite.temp;

public class CheckIPDto {
	String begIP;
	String endIP;
	int port;
	int timeout;

	public String getBegIP() {
		return begIP;
	}

	public void setBegIP(String begIP) {
		this.begIP = begIP;
	}

	public String getEndIP() {
		return endIP;
	}

	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
